package com.itheima.bos.web.action.system;

import java.io.IOException;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.ServletActionContext;
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

import com.itheima.bos.domain.base.Standard;
import com.itheima.bos.domain.system.Menu;
import com.itheima.bos.service.system.MenuService;
import com.itheima.bos.web.action.CommomAction;

import net.sf.json.JsonConfig;

/**  
 * ClassName:MenuAction <br/>  
 * Function:  <br/>  
 * Date:     2018年3月30日 上午11:27:17 <br/>       
 */
@Namespace("/")// 等价于struts.xml文件中package节点namespace属性
@ParentPackage("struts-default")// 等价于struts.xml文件中package节点extends属性
@Controller
@Scope("prototype")
public class MenuAction extends CommomAction<Menu>{

	
	
	public MenuAction() {
		super(Menu.class);  
	}

	@Autowired
	private MenuService menuService;
	
	
	@Action(value="menuAction_findLevelOne")
	public String findLevelOne() throws IOException{
		
	 List<Menu> list =	menuService.findLevelOne();
		
	JsonConfig jsonConfig = new JsonConfig();
	jsonConfig.setExcludes(new String[]{"roles","childrenMenus","parentMenu"});
	
	list2json(list, jsonConfig);
	
		return NONE;
	}
	
	//menuAction_save.action 保存
	@Action(value="menuAction_save",
			results={@Result(name="success",location="/pages/system/menu.html",type="redirect")})
	public String save() {
		menuService.save(getModel());
		return SUCCESS;
	}
	
	//menuAction_pageQuery  分页
	// AJAX请求不需要跳转页面
	@Action(value="menuAction_pageQuery")
	public String pageQuery() throws IOException{
		
		// EasyUI的页码是从1开始的
		// SPringDataJPA的页码是从0开始的
		 // 所以要-1
		Pageable pageable =new PageRequest(
				Integer.parseInt(getModel().getPage())-1, rows);
		
		Page<Menu> page = menuService.findAll(pageable);
		//拿到数据后,我们把需要的数据 从对象中拿出来,不需要的数据 不拿,不要把无效数据返回给页面
		
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setExcludes(new String[]{"roles","childrenMenus","parentMenu"});
		// 总数据条数
		page2json(page, jsonConfig);
		
		return NONE;
	}
	
	
	
}
  
