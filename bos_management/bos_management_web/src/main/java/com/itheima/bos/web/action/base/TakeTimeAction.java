package com.itheima.bos.web.action.base;

import java.io.IOException;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.base.Courier;
import com.itheima.bos.domain.base.TakeTime;
import com.itheima.bos.service.base.TakeTimeService;
import com.itheima.bos.web.action.CommomAction;

import net.sf.json.JsonConfig;

/**  
 * ClassName:TakeTimeAction <br/>  
 * Function:  <br/>  
 * Date:     2018年3月22日 上午9:52:52 <br/>       
 */
@Namespace("/")// 等价于struts.xml文件中package节点namespace属性
@ParentPackage("struts-default")// 等价于struts.xml文件中package节点extends属性
@Controller
@Scope("prototype")
public class TakeTimeAction extends CommomAction<TakeTime> {

	public TakeTimeAction() {
		super(TakeTime.class);  
	}
	@Autowired
	private TakeTimeService takeTimeService;
	
	@Action("takeTimeAction_listajax")
	public String listajax() throws IOException{
		//查询所有在职的快递员
		List<TakeTime> list = takeTimeService.findAll();
		
		list2json(list, null);
		return NONE;
	}
	
	
	
	
	
}
  
