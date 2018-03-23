package com.itheima.bos.fore.web.action;

import java.util.concurrent.TimeUnit;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.crm.domain.Customer;
import com.itheima.utils.MailUtils;
import com.itheima.utils.SmsUtils;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**  
 * ClassName:CustomerAction <br/>  
 * Function:  <br/>  
 * Date:     2018年3月22日 下午2:48:11 <br/>       
 */
@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class CustomerAction extends ActionSupport implements ModelDriven<Customer>{

	private Customer model=new Customer();
	@Override
	public Customer getModel() {
		
		return model;
	}
	
	//customerAction_sendSMS   该方法 生成用于向用户输入的手机号发送验证码
	@Action(value="customerAction_sendSMS")
	public String sendSMS(){//此方法 用于 给正在注册的用户 发送手机验证码
		
		//生成随机验证码
		String code = RandomStringUtils.randomNumeric(6);
		System.out.println("code----"+code);
		//存到session中
		ServletActionContext.getRequest().getSession().setAttribute("code", code);
		
		//发送短信
		try {
			SmsUtils.sendSms(model.getTelephone(), code);
		} catch (ClientException e) {
			
			e.printStackTrace();  
		}
		
		return NONE;
	}
	
	private String checkcode;
	public void setCheckcode(String checkcode) {
		this.checkcode = checkcode;
	}
	
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	//customerAction_regist.action
	@Action(value="customerAction_regist",
			results={@Result(name="success",
			location="/signup-success.html",type="redirect"),
					@Result(name="error",
			location="/signup-fail.html",type="redirect")})
	public String regist(){  //此方法 用于用户注册,并在注册成功后 给用户发送激活邮件
		
		//1，校验验证码 
		//拿出后台的验证码,前台的验证码需要通过属性驱动 获得
		String serverCode = (String) ServletActionContext.getRequest().getSession().getAttribute("code");
		if (StringUtils.isNotEmpty(checkcode)&& StringUtils.isNotEmpty(serverCode) 
				&& checkcode.equals(serverCode)) {
			//验证码校验成功，执行注册，向crm
			
			WebClient.create("http://localhost:8180/crm/webService/customerService/save")
			.type(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.post(model);
			
			//生成验证码
			String activeCode =RandomStringUtils.randomNumeric(32);
			//存储验证码
			redisTemplate.opsForValue().set(model.getTelephone(), activeCode, 1, TimeUnit.DAYS);
			
			//注册成功，发送激活邮件
			String emailBody = "欢迎注册本网站，请在24小时内点击<a href='http://localhost:8280/portal/customerAction_active.action?activeCode="
                            + activeCode + "&telephone=" + model.getTelephone()
                            + "'>本链接</a>激活你的账户";
			
			MailUtils.sendMail(model.getEmail(), "激活账号", emailBody);
			return SUCCESS;
		}
		return ERROR;
	}
	
	//customerAction_active
	//使用属性驱动获取激活码
	private String activeCode;
	public void setActiveCode(String activeCode) {
		this.activeCode = activeCode;
	}
	
	@Action(value="customerAction_active",
			results={@Result(name="success",
			location="/login.html",type="redirect"),
					@Result(name="error",
			location="/error.html",type="redirect")})
	public String active(){  //此方法 用于用户账号激活
		
		//从redis 取出激活码进行比对
		String serverCode = redisTemplate.opsForValue().get(model.getTelephone());
		
		if (StringUtils.isNotEmpty(activeCode) && StringUtils.isNoneEmpty(serverCode) &&
				activeCode.equals(serverCode)) {
			//激活码校验成功,激活
			WebClient.create("http://localhost:8180/crm/webService/customerService/active")
			.type(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.query("telephone", model.getTelephone())
			.put(null);
			return SUCCESS;
		}
		
		return ERROR;
	}
	//写方法 用于 用户登录
	//登录的逻辑1, 校验验证码, 2,校验用户是否激活  , 3,然后 校验 账号 密码
	//页面提交过来的验证码 mane 属性是 checkcode 跟注册的时候提交的属性名是一样的,所以 可以用之前的属性驱动
	
	@Action(value="customerAction_login",
			results={@Result(name="success",
			location="/index.html",type="redirect"),
					@Result(name="error",
			location="/login.html",type="redirect"),
					@Result(name="unactived",
			location="/login.html",type="redirect")})
	public String login(){
		//System.out.println("开始登录-------");
		//拿出生成后存在session中的验证码 validateCode
		String serverCode = (String) ServletActionContext.getRequest().getSession().getAttribute("validateCode");
		//校验验证码
		if (StringUtils.isNotEmpty(serverCode)&& StringUtils.isNotEmpty(checkcode)
				&&serverCode.equals(checkcode)) {
			//校验是否激活
			Customer customer = WebClient.create("http://localhost:8180/crm/webService/customerService/isActived")
			.type(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.query("telephone", model.getTelephone())
			.get(Customer.class);
			//System.out.println("校验激活-------"+customer);
			if (customer!=null && customer.getType()!=null) {
				if (customer.getType()==1) {
					//激活了
					//校验账号密码
			Customer c = WebClient.create("http://localhost:8180/crm/webService/customerService/login")
			.type(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.query("telephone", model.getTelephone())
			.query("password", model.getPassword())
			.get(Customer.class);	
			//System.out.println("校验账号-------"+c);
			if (c!=null) {
				//登录成功
				ServletActionContext.getRequest().getSession().setAttribute("user", c);
				return SUCCESS;
				
			}else {
				//登录失败
				return ERROR;
			}
			
			
				}else {
					//已注册,但您还未激活
					return "unactived";
				}
				
			}
		}
		
		return ERROR;
	}
	
	
}
  
