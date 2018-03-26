package com.itheima.bos.web.action.take_delivery;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.base.Standard;
import com.itheima.bos.domain.take_delivery.WayBill;
import com.itheima.bos.service.take_delivery.WaybillService;
import com.itheima.bos.web.action.CommomAction;

/**  
 * ClassName:WaybillAction <br/>  
 * Function:  <br/>  
 * Date:     2018年3月26日 下午8:40:46 <br/>       
 */
@Namespace("/")// 等价于struts.xml文件中package节点namespace属性
@ParentPackage("struts-default")// 等价于struts.xml文件中package节点extends属性
@Controller
@Scope("prototype")
public class WaybillAction extends CommomAction<WayBill>{

	
	public WaybillAction() {
		super(WayBill.class);  
	}
	@Autowired
	private WaybillService waybillService;
	
	// AJAX请求不需要跳转页面waybillAction_save
	@Action(value="waybillAction_save")
	public String save() throws IOException {
		String msg ="1";
		
			try {
			//	int i = 1/0;
				waybillService.save(getModel());
			} catch (Exception e) {
				  msg="0";
				e.printStackTrace();  
			}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write(msg);
			
		return NONE;
	}
	
	
	

}
  
