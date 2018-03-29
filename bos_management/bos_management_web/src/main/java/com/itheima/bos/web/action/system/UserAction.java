package com.itheima.bos.web.action.system;

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
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.system.User;
import com.itheima.bos.web.action.CommomAction;

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
	
	
	
	
}
  
