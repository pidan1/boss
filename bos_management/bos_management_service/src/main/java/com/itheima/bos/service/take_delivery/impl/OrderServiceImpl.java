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
import com.itheima.bos.domain.base.Area;
import com.itheima.bos.domain.base.Courier;
import com.itheima.bos.domain.base.FixedArea;
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
						
						
					}
				}
				
				
			}
			
			
			
			
			
		}else{
			//客户没有填写地址
			//人工分单
		}
		
		
	}

}
  
