package com.itheima.bos.service.realms;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.itheima.bos.dao.system.UserRepository;
import com.itheima.bos.domain.system.User;

/**  
 * ClassName:UserRealms <br/>  
 * Function:  <br/>  
 * Date:     2018年3月28日 下午8:53:54 <br/>       
 */
@Component
public class UserRealm extends AuthorizingRealm{

	@Autowired
	private UserRepository userRepository;
	
	//授权的方法
	//每一次访问需要权限的资源的时候,都会调用授权的方法
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {
		  
		return null;
	}

	//认证的方法
	//参数中的token 就是subject.login(token)fangfa中传入的参数
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		  
		UsernamePasswordToken usernamePasswordToken =(UsernamePasswordToken) token;
		
		String username = usernamePasswordToken.getUsername();
		
		//根据用户名查找用户
		User user = userRepository.findByUsername(username);
		
		//找到--对比密码
		if (user!=null) {

            /**
             * @param principal  当事人,主体,通常是从数据库中查询到的用户
             * @param credentials  凭证,密码,是从数据库中查询出来的密码
             * @param realmName 
             */
			AuthenticationInfo info=new SimpleAuthenticationInfo(user,user.getPassword(),getName());
			
			
			//对比成功--执行后续的逻辑
			
			//对比失败--抛异常
			return info;
		}
		
		//找不到--抛出异常
		
		return null;
	}

}
  
