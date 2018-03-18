package com.itheima.bos.web.action.base;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.base.SubArea;
import com.itheima.bos.service.base.SubAreaService;
import com.itheima.bos.web.action.CommomAction;

/**  
 * ClassName:SubAreaAction <br/>  
 * Function:  <br/>  
 * Date:     2018年3月18日 下午4:31:10 <br/>       
 */
@Namespace("/")// 等价于struts.xml文件中package节点namespace属性
@ParentPackage("struts-default")// 等价于struts.xml文件中package节点extends属性
@Controller
@Scope("prototype")
public class SubAreaAction extends CommomAction<SubArea> {

	public SubAreaAction() {
		super(SubArea.class);  
		
	}

	@Autowired 
	private SubAreaService subAreaService;
	
	@Action(value="subareaAction_save",results={@Result(name="success",
			location="/pages/base/sub_area.html",type="redirect")})
	public String save(){
		
		//System.out.println(getModel().getKeyWords()+"------");
		
		subAreaService.save(getModel());
		
		return SUCCESS;
	}
}
  
