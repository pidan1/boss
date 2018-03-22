package com.itheima.crm.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.itheima.crm.domain.Customer;

/**  
 * ClassName:CustomerService <br/>  
 * Function:  <br/>  
 * Date:     2018年3月19日 下午4:08:54 <br/>       
 */
@Produces(MediaType.APPLICATION_JSON)//指定返回值的数据传输格式
@Consumes(MediaType.APPLICATION_JSON)//指定参数的传输格式
public interface CustomerService {

	//查询所有客户的信息
	@GET
	@Path("/findAll")
	public List<Customer> findAll();
	
	//定义查找未关联定区的客户
	@GET
	@Path("/findCustomersUnAssociated")
	public List<Customer> findCustomersUnAssociated();
	
	//定义查找已关联定区的客户
	@GET 
	@Path("/findCustomersAssociated2FixedArea")
	public List<Customer> findCustomersAssociated2FixedArea(@QueryParam("fixedAreaId") String fixedAreaId);
	
	
	//定区关联客户，定区id
	//先把该定区所有用户解绑
	//然后 根据要关联的数据，跟定区id进行绑定
	@PUT
	@Path("/assignCustomers2FixedArea")
	void assignCustomers2FixedArea(@QueryParam("customerIds") Long[] customerIds,@QueryParam("fixedAreaId") String fixedAreaId);
	
	//用于注册 保存用户
	@POST
	@Path("/save")
	void save(Customer customer);
	
}
  
