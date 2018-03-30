package com.itheima.bos.web.action.system;

import java.io.IOException;
import java.util.List;

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

import com.itheima.bos.domain.system.Menu;
import com.itheima.bos.domain.system.Permission;
import com.itheima.bos.service.system.PermissionService;
import com.itheima.bos.web.action.CommomAction;

import net.sf.json.JsonConfig;

/**  
 * ClassName:PermissionAction <br/>  
 * Function:  <br/>  
 * Date:     2018年3月30日 下午4:02:32 <br/>       
 */
@Namespace("/")// 等价于struts.xml文件中package节点namespace属性
@ParentPackage("struts-default")// 等价于struts.xml文件中package节点extends属性
@Controller
@Scope("prototype")
public class PermissionAction extends CommomAction<Permission>{

	public PermissionAction() {
		super(Permission.class);  
		
	}
	@Autowired 
	private PermissionService permissionService;
	
	// 保存../../permissionAction_save.action
		@Action(value="permissionAction_save",
				results={@Result(name="success",location="/pages/system/permission.html",type="redirect")})
		public String save() {
			permissionService.save(getModel());
			return SUCCESS;
		}
	
	// AJAX请求不需要跳转页面
	@Action(value="permissionAction_pageQuery")
	public String pageQuery() throws IOException{
		
		// EasyUI的页码是从1开始的
		// SPringDataJPA的页码是从0开始的
		 // 所以要-1
		Pageable pageable =new PageRequest(
				page-1, rows);
		
		Page<Permission> page = permissionService.findAll(pageable);
		//拿到数据后,我们把需要的数据 从对象中拿出来,不需要的数据 不拿,不要把无效数据返回给页面
		
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setExcludes(new String[]{"roles"});
		// 总数据条数
		page2json(page, jsonConfig);
		
		return NONE;
	}
	
	// AJAX请求不需要跳转页面 permissionAction_findAll
	@Action(value="permissionAction_findAll")
	public String findAll() throws IOException{
		
		
		Page<Permission> page = permissionService.findAll(null);
		//拿到数据后,我们把需要的数据 从对象中拿出来,不需要的数据 不拿,不要把无效数据返回给页面
		List<Permission> list = page.getContent();
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setExcludes(new String[]{"roles"});
		// 总数据条数
		
		list2json(list, jsonConfig);
		return NONE;
	}

}
  
