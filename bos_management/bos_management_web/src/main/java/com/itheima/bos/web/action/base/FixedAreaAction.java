package com.itheima.bos.web.action.base;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.base.FixedArea;
import com.itheima.bos.domain.base.Standard;
import com.itheima.bos.service.base.FixedAreaService;
import com.itheima.bos.web.action.CommomAction;
import com.itheima.crm.domain.Customer;

import net.sf.json.JsonConfig;

/**  
 * ClassName:FixedAreaAction <br/>  
 * Function:  <br/>  
 * Date:     2018年3月19日 下午5:49:33 <br/>       
 */
@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class FixedAreaAction extends CommomAction<FixedArea> {

	
	public FixedAreaAction() {
		super(FixedArea.class);  
	}
	
	@Autowired
	private FixedAreaService fixedAreaService;
	
	@Action(value="fixedAreaAction_save",results={@Result(name="success",
			location="/pages/base/fixed_area.html",type="redirect")})
	public String save(){
		System.out.println(getModel().getCompany()+"----------------");
		fixedAreaService.save(getModel());
		return SUCCESS;
	}
	
	
	// AJAX请求不需要跳转页面
		@Action(value="fixedAreaAction_pageQuery")
		public String pageQuery() throws IOException{
			
			// EasyUI的页码是从1开始的
			// SPringDataJPA的页码是从0开始的
			 // 所以要-1
			Pageable pageable =new PageRequest(page-1, rows);
			Page<FixedArea> page = fixedAreaService.findAll(pageable);
			//拿到数据后,我们把需要的数据 从对象中拿出来,不需要的数据 不拿,不要把无效数据返回给页面
			
			JsonConfig jsonConfig=new JsonConfig();
			jsonConfig.setExcludes(new String[]{"subareas","couriers"});
			
			// 总数据条数
			page2json(page, jsonConfig);
			
			return NONE;
		}
	
		
		// AJAX请求不需要跳转页面
		@Action(value="fixedAreaAction_findCustomersUnAssociated")
		public String findCustomersUnAssociated() throws IOException{
			
			List<Customer> list = (List<Customer>) WebClient
			.create("http://localhost:8180/crm/webService/customerService/findCustomersUnAssociated")
			.type(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.getCollection(Customer.class);
			
			list2json(list, null);
			
			return NONE;
		}
	
	
		// AJAX请求不需要跳转页面
		@Action(value="fixedAreaAction_findCustomersAssociated2FixedArea")
		public String findCustomersAssociated2FixedArea() throws IOException{
			
			List<Customer> list = (List<Customer>) WebClient
					.create("http://localhost:8180/crm/webService/customerService/findCustomersAssociated2FixedArea")
					.type(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.query("fixedAreaId", getModel().getId())
					.getCollection(Customer.class);
			
			list2json(list, null);
			
			return NONE;
		}
		
		//使用 属性驱动 获取所选中的需要关联的客户的id数组： customerIds 
		private Long[] customerIds;
		public void setCustomerIds(Long[] customerIds) {
			this.customerIds = customerIds;
		}
		
		// 向crm系统 发起请求，关联客户
		@Action(value="fixedAreaAction_assignCustomers2FixedArea",results={@Result(name="success",
				location="/pages/base/fixed_area.html",type="redirect")})
		public String assignCustomers2FixedArea() throws IOException{
			
			 WebClient
				.create("http://localhost:8180/crm/webService/customerService/assignCustomers2FixedArea")
				.type(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.query("fixedAreaId", getModel().getId())
				.query("customerIds", customerIds)
				.put(null);
			return SUCCESS;
		}
		
		
		private Long courierId;
		private Long takeTimeId;
		public void setCourierId(Long courierId) {
			this.courierId = courierId;
		}
		public void setTakeTimeId(Long takeTimeId) {
			this.takeTimeId = takeTimeId;
		}
	
	//定区关联快递员  fixedAreaAction_associationCourierToFixedArea
		@Action(value="fixedAreaAction_associationCourierToFixedArea",results={@Result(name="success",
				location="/pages/base/fixed_area.html",type="redirect")})
		public String associationCourierToFixedArea(){
			
			fixedAreaService.associationCourierToFixedArea(getModel().getId(),courierId,takeTimeId);
			
			
			
			return SUCCESS;
		}
		

}
  
