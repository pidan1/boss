package com.itheima.bos.web.action.system;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
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

import com.itheima.bos.domain.system.Menu;
import com.itheima.bos.domain.system.User;
import com.itheima.bos.service.system.UserService;
import com.itheima.bos.web.action.CommomAction;

import net.sf.json.JsonConfig;

/**  
 * ClassName:UserAction <br/>  
 * Function:  <br/>  
 * Date:     2018年3月28日 下午8:21:32 <br/>       
 */
@Namespace("/")// 等价于struts.xml文件中package节点namespace属性
@ParentPackage("struts-default")// 等价于struts.xml文件中package节点extends属性
@Controller
@Scope("prototype")
public class UserAction extends CommomAction<User>{

	
	public UserAction( ) {
		super(User.class);  
	}
	//属性驱动,获取登录用户输入的验证码
	private String checkcode;
	public void setCheckcode(String checkcode) {
		this.checkcode = checkcode;
	}
	
	
//userAction_login
	@Action(value="userAction_login",results={@Result(name="success",
			location="/index.html",type="redirect"),
			@Result(name="login",
			location="/login.html",type="redirect")})
	public String login(){
		System.out.println("执行登录==="+checkcode);
		//获取存在session中的验证码,拿出来做比对
		String serverCode = (String) ServletActionContext.getRequest().getSession().getAttribute("validateCode");
		//校验 验证码
		if (StringUtils.isNoneEmpty(serverCode)&&StringUtils.isNotEmpty(checkcode)
				&&serverCode.equals(checkcode)) {
			
			//主体,代表当前用户
			Subject subject = SecurityUtils.getSubject();
			//使用代码校验权限,不推荐使用,
			//subject.checkPermission("");
			
			
			//创建令牌
			AuthenticationToken token=new UsernamePasswordToken(getModel().getUsername(), getModel().getPassword());
			//执行登录
			try {
				subject.login(token);
				// 方法的返回值由Realm中doGetAuthenticationInfo方法定义SimpleAuthenticationInfo对象的时候,第一个参数决定的
				User user = (User) subject.getPrincipal();
				//登录成功,存到session中
				ServletActionContext.getRequest().getSession().setAttribute("user", user);
				
				return SUCCESS;
			} catch (UnknownAccountException e) {
				  
				e.printStackTrace();  
				 System.out.println("找不到用户名");
			} catch (IncorrectCredentialsException e) {
				  
				// TODO Auto-generated catch block  
				e.printStackTrace();  
				  System.out.println("密码错误");
			} catch (Exception e) {
				  
				e.printStackTrace();  
				 System.out.println("其他错误");
			}
		}
		return LOGIN;
	}
	//userAction_logout
	@Action(value="userAction_logout",results={@Result(name="success",
			location="/login.html",type="redirect")})
	public String logout(){
		//退出登录  1,注销   2,清空session
		
		//注销
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		
		//清空session
		ServletActionContext.getRequest().getSession().removeAttribute("user");
		
		return SUCCESS;
	}
	
	private Long[] roleIds;
	public void setRoleIds(Long[] roleIds) {
		this.roleIds = roleIds;
	}
	
	@Autowired
	private UserService userService;
	
	//userAction_save.action
	@Action(value="userAction_save",
			results={@Result(name="success",location="/pages/system/menu.html",type="redirect")})
	public String save() {
		userService.save(getModel(),roleIds);
		return SUCCESS;
	}
	
	// AJAX请求不需要跳转页面
	@Action(value="userAction_pageQuery")
	public String pageQuery() throws IOException{
		
		// EasyUI的页码是从1开始的
		// SPringDataJPA的页码是从0开始的
		 // 所以要-1
		Pageable pageable =new PageRequest(page-1, rows);
		
		Page<User> page = userService.findAll(pageable);
		//拿到数据后,我们把需要的数据 从对象中拿出来,不需要的数据 不拿,不要把无效数据返回给页面
		
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setExcludes(new String[]{"roles"});
		// 总数据条数
		page2json(page, jsonConfig);
		
		return NONE;
	}
	
}
  
