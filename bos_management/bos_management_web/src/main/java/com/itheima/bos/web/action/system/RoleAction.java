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

import com.itheima.bos.domain.system.Permission;
import com.itheima.bos.domain.system.Role;
import com.itheima.bos.service.system.PermissionService;
import com.itheima.bos.service.system.RoleService;
import com.itheima.bos.web.action.CommomAction;

import net.sf.json.JsonConfig;

/**  
 * ClassName:RoleAction <br/>  
 * Function:  <br/>  
 * Date:     2018年3月30日 下午4:39:07 <br/>       
 */
@Namespace("/")// 等价于struts.xml文件中package节点namespace属性
@ParentPackage("struts-default")// 等价于struts.xml文件中package节点extends属性
@Controller
@Scope("prototype")
public class RoleAction extends CommomAction<Role>{

	public RoleAction() {
		super(Role.class);  
	}
	@Autowired
	private RoleService roleService;
	
	// 使用属性驱动获取菜单和权限的ID
	private String menuIds;
	private Long[] permissionIds;

	public void setMenuIds(String menuIds) {
		this.menuIds = menuIds;
	}
	public void setPermissionIds(Long[] permissionIds) {
		this.permissionIds = permissionIds;
	}
	
	//roleAction_save.action
	@Action(value="roleAction_save",
			results={@Result(name="success",location="/pages/system/role.html",type="redirect")})
	public String save() {
		roleService.save(getModel(),menuIds,permissionIds);
		return SUCCESS;
	}
	
	// AJAX请求不需要跳转页面
	@Action(value="roleAction_pageQuery")
	public String pageQuery() throws IOException{
		
		// EasyUI的页码是从1开始的
		// SPringDataJPA的页码是从0开始的
		 // 所以要-1
		Pageable pageable =new PageRequest(
				page-1, rows);
		
		Page<Role> page = roleService.findAll(pageable);
		//拿到数据后,我们把需要的数据 从对象中拿出来,不需要的数据 不拿,不要把无效数据返回给页面
		
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setExcludes(new String[]{"users","permissions","menus"});
		// 总数据条数
		page2json(page, jsonConfig);
		
		return NONE;
	}
	
	// AJAX请求不需要跳转页面 roleAction_findAll
	@Action(value="roleAction_findAll")
	public String findAll() throws IOException{
		
		
		Page<Role> page = roleService.findAll(null);
		//拿到数据后,我们把需要的数据 从对象中拿出来,不需要的数据 不拿,不要把无效数据返回给页面
		List<Role> list = page.getContent();
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setExcludes(new String[]{"users","permissions","menus"});
		// 总数据条数
		list2json(list, jsonConfig);
		
		return NONE;
	}
	
}
  
