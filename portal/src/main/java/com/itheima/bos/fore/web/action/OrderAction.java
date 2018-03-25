package com.itheima.bos.fore.web.action;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.itheima.bos.domain.base.Area;
import com.itheima.bos.domain.take_delivery.Order;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**  
 * ClassName:OrderAction <br/>  
 * Function:  <br/>  
 * Date:     2018年3月24日 下午2:52:39 <br/>       
 */
public class OrderAction extends ActionSupport implements ModelDriven<Order>{

	private Order model=new Order();
	
	@Override
	public Order getModel() {
		return model;
	}
	
	//通过属性驱动 获取模型驱动收不到的字段
	 // 使用属性驱动获取发件和收件的区域信息
	//省/市/区
    private String recAreaInfo;
    private String sendAreaInfo;
	public void setRecAreaInfo(String recAreaInfo) {
		this.recAreaInfo = recAreaInfo;
	}
	public void setSendAreaInfo(String sendAreaInfo) {
		this.sendAreaInfo = sendAreaInfo;
	}
	
	@Action(value="orderAction_add",
			results={@Result(name="success",
			location="/index.html",type="redirect")})
	public String saveOrder(){
		
		//切割数据,并封装到 order里的 寄件人 收件人 的省市区 的 Area对象里
		if (StringUtils.isNotEmpty(sendAreaInfo)) {
			String[] split = sendAreaInfo .split("/");
			
			String province = split[0];
			String city = split[1];
			String district = split[2];
			//发件人省市区
			province = province.substring(0, province.length()-1);
			city = city.substring(0, city.length()-1);
			district = district.substring(0, district.length()-1);
			// 封装Area
			Area area = new Area();
			area.setProvince(province);
			area.setCity(city);
			area.setDistrict(district);
			model.setSendArea(area);
		}
		
		if (StringUtils.isNotEmpty(recAreaInfo)) {
			String[] split = recAreaInfo .split("/");
			
			String province = split[0];
			String city = split[1];
			String district = split[2];
			//收件人省市区
			 province = province.substring(0, province.length()-1);
			 city = city.substring(0, city.length()-1);
			 district = district.substring(0, district.length()-1);
			// 封装Area
			 Area area = new Area();
			 area.setProvince(province);
			 area.setCity(city);
			 area.setDistrict(district);
			 model.setRecArea(area);
		}
	
		WebClient.create("http://localhost:8080/bos_management_web/webService/orderService/saveOrder")
		.accept(MediaType.APPLICATION_JSON)
		.type(MediaType.APPLICATION_JSON)
		.post(model);
		
		
		
		return SUCCESS;
	}
	
	
}
  
