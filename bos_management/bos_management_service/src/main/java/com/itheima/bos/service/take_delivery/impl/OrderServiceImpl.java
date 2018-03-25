package com.itheima.bos.service.take_delivery.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.base.AreaRepository;
import com.itheima.bos.dao.base.FixedAreaRepository;
import com.itheima.bos.dao.take_delivery.OrderRepository;
import com.itheima.bos.dao.take_delivery.WorkBillRepository;
import com.itheima.bos.domain.base.Area;
import com.itheima.bos.domain.base.Courier;
import com.itheima.bos.domain.base.FixedArea;
import com.itheima.bos.domain.base.SubArea;
import com.itheima.bos.domain.take_delivery.Order;
import com.itheima.bos.domain.take_delivery.WorkBill;
import com.itheima.bos.service.take_delivery.OrderService;

/**  
 * ClassName:OrderServiceImpl <br/>  
 * Function:  <br/>  
 * Date:     2018年3月24日 下午3:33:45 <br/>       
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private AreaRepository areaRepository;
	@Autowired
	private FixedAreaRepository fixedAreaRepository;
	@Autowired
	private WorkBillRepository workBillRepository;
	
	@Override
	public void saveOrder(Order order) {
		//save the transient instance before flushing
		//因为传过来的 order 里面的 Area 是new 出来的，是瞬时态，保存瞬时态 会发生上面的错误
		//我们需要 根据 省市区，查出 Area 是持久态的，才能 保存
		
		Area sendArea = order.getSendArea();
		if (sendArea!=null) {
			
			String province = sendArea.getProvince();
			String city = sendArea.getCity();
			String district = sendArea.getDistrict();
			 // 持久态对象
			Area sendAreaDB = areaRepository.findByProvinceAndCityAndDistrict(province,city,district);
			order.setSendArea(sendAreaDB);
		}
		Area recArea = order.getRecArea();
		if (sendArea!=null) {
			String province = recArea.getProvince();
			String city = recArea.getCity();
			String district = recArea.getDistrict();
			 // 持久态对象
			Area recAreaDB = areaRepository.findByProvinceAndCityAndDistrict(province,city,district);
			order.setRecArea(recAreaDB);
		}
		
		//保存订单
		//封装 订单号，和下单时间
		order.setOrderNum(UUID.randomUUID().toString().replaceAll("-",""));
		order.setOrderTime(new Date());
		orderRepository.save(order);
		
		//自动分单
		//取出发件人填写的详细地址
		String sendAddress = order.getSendAddress();
		
		if (StringUtils.isNotEmpty(sendAddress)) {//客户填写了地址
			//根据发件地址完全匹配客户表的地址，让crm系统根据发件地址 查找定区id
			//在做下面的测试之前，必须在定区中关联一个客户，下单的页面填写的地址，必须和这个客户的地址一致
			String fixedAreaId = WebClient.create("http://localhost:8180/crm/webService/customerService/findFixedAreaIdByAdddress")
			.type(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.query("address", sendAddress)
			.get(String.class);
			
			if (StringUtils.isNotEmpty(fixedAreaId)) {
				//定区id不为空，根据定区id查出定区
				FixedArea fixedArea = fixedAreaRepository.findOne(Long.parseLong(fixedAreaId));
				if (fixedArea!=null) {
					//查出快递员
					Set<Courier> couriers = fixedArea.getCouriers();
					if (!couriers.isEmpty()) {
						//实际逻辑 需要根据快递员的上班时间，/收派能力/闲忙程度 来指定快递员
						//我们直接使用第一个（假设）
						Iterator<Courier> iterator = couriers.iterator();//调用迭代器
						Courier courier = iterator.next();//从迭代器 拿到第一个
						
						//指派快递员
						order.setCourier(courier);
						
						//生成工单
						WorkBill workBill = new WorkBill();
						workBill.setAttachbilltimes(0);
						workBill.setBuildtime(new Date());
						workBill.setCourier(courier);
						workBill.setOrder(order);
						workBill.setPickstate("新单");
						workBill.setRemark(order.getRemark());
						workBill.setSmsNumber("111");
						workBill.setType("新");
						
						//保存工单
						workBillRepository.save(workBill);
						//发送信息 推送给快递员
						//分单匹配完毕,中断代码的执行
						order.setOrderType("自动分单");
						return;
					}
				}
			}else {
				//不能根据下单的详细地址 匹配到定区
				//跟据省市区 查找到分区
				
				
				Area sendArea2 = order.getSendArea();//获取寄件人选择的省市区
				if (sendArea2!=null) {
					Set<SubArea> subareas = sendArea2.getSubareas();//找出省市区下的全部分区
					for (SubArea subArea : subareas) {//遍历 出每一个分区
						String keyWords = subArea.getKeyWords();          //取出分区的关键字
						String assistKeyWords = subArea.getAssistKeyWords();//取出分区的辅助关键字
						if (sendAddress.contains(keyWords)
								|| sendAddress.contains(assistKeyWords)) {//把下单时 客户输入的地址 跟分区的关键字 作模糊比较 contains 是包含的意思
							//A contains B 判断 A是否包含B 返回 boolean 类型的值
							//如果包含,那就是这个分区了
							FixedArea fixedArea2 = subArea.getFixedArea();//找到分区后,取出该分区关联的定区
							if (fixedArea2!=null) {
								//查出快递员
								Set<Courier> couriers = fixedArea2.getCouriers();
								if (!couriers.isEmpty()) {
									//实际逻辑 需要根据快递员的上班时间，/收派能力/闲忙程度 来指定快递员
									//我们直接使用第一个（假设）
									Iterator<Courier> iterator = couriers.iterator();//调用迭代器
									Courier courier = iterator.next();//从迭代器 拿到第一个
									
									//指派快递员
									order.setCourier(courier);
									
									//生成工单
									WorkBill workBill = new WorkBill();
									workBill.setAttachbilltimes(0);
									workBill.setBuildtime(new Date());
									workBill.setCourier(courier);
									workBill.setOrder(order);
									workBill.setPickstate("新单");
									workBill.setRemark(order.getRemark());
									workBill.setSmsNumber("111");
									workBill.setType("新");
									
									//保存工单
									workBillRepository.save(workBill);
									//发送信息 推送给快递员
									//分单匹配完毕,中断代码的执行
									order.setOrderType("自动分单");
									return;
								}
							}
						}
					}
				}
			}
			
			
		}
			//客户没有填写地址
			//人工分单
		order.setOrderType("人工分单");
	}

}
  
