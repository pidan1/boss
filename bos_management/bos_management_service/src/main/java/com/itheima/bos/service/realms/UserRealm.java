package com.itheima.bos.service.realms;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.itheima.bos.dao.system.PermissionRepository;
import com.itheima.bos.dao.system.RoleRepository;
import com.itheima.bos.dao.system.UserRepository;
import com.itheima.bos.domain.system.Permission;
import com.itheima.bos.domain.system.Role;
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
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private PermissionRepository permissionRepository;
	
	
	//授权的方法
	//每一次访问需要权限的资源的时候,都会调用授权的方法
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		//这个是我们返回值类型的 实现类
		SimpleAuthorizationInfo info =new SimpleAuthorizationInfo();
		//不同的用户,对应的权限时不同的,所以我们需要根据不同用户,授予不同权限
		//1,获取当前用户,2,根据用户查权限和角色3,授权,和角色
		
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getPrincipal();
		
		if ("admin".equals(user.getUsername())) {
			//内置管理员的权限和角色是最大的,拥有全部,写死即可
			List<Role> roles = roleRepository.findAll();
			for (Role role : roles) {
				info.addRole(role.getKeyword());//授予角色
			}
			List<Permission> permissions = permissionRepository.findAll();
			for (Permission permission : permissions) {
				info.addStringPermission(permission.getKeyword());//授予权限
			}
			
		}else {
			//不是内置管理员,根据用户查权限,角色,然后 授予
			List<Role> roles = roleRepository.findbyUid(user.getId());
			for (Role role : roles) {
				info.addRole(role.getKeyword());//授予角色
			}
			List<Permission> permissions = permissionRepository.findbyUid(user.getId());
			for (Permission permission : permissions) {
				info.addStringPermission(permission.getKeyword());//授予权限
			}
		}
		
		
		
		
		
		
		
		//授权
		info.addStringPermission("courierAction_pageQuery");
		
		//授予角色
		info.addRole("admin");
		return info;
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
  
