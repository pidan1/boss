package com.itheima.bos.service.system.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.system.MenuRepository;
import com.itheima.bos.dao.system.PermissionRepository;
import com.itheima.bos.dao.system.RoleRepository;
import com.itheima.bos.domain.system.Menu;
import com.itheima.bos.domain.system.Permission;
import com.itheima.bos.domain.system.Role;
import com.itheima.bos.service.system.RoleService;

/**  
 * ClassName:RoleServiceImpl <br/>  
 * Function:  <br/>  
 * Date:     2018年3月30日 下午4:38:08 <br/>       
 */
@Service
@Transactional
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private MenuRepository menuRepository;
	@Autowired
	private PermissionRepository permissionRepository;

	@Override
	public Page<Role> findAll(Pageable pageable) {
		  
		return roleRepository.findAll(pageable);
	}
	
	@Override
	public void save(Role role, String menuIds, Long[] permissionIds) {
		//保存角色
		roleRepository.save(role);
		// 建立关联的时候,只能使用角色去关联菜单和权限,不能使用菜单和权限去关联角色
		// 原因在与菜单和权限的实体类中声明了mappedBy属性,意味着它们放弃了对关系的维护
		if (StringUtils.isNotEmpty(menuIds)) {
			String[] split = menuIds.split(",");
			for (String menuId : split) {
				Menu menu = new Menu();
				menu.setId(Long.parseLong(menuId));
				//此时 menu是托管态,不会发生 保存瞬时态的异常
				role.getMenus().add(menu);			}
		}
		if (permissionIds!=null &&permissionIds.length>0) {
			for (Long permissionId : permissionIds) {
				Permission permission = new Permission();
				permission.setId(permissionId);
				//此时 permission是托管态,不会发生 保存瞬时态的异常
				role.getPermissions().add(permission);
			}
		}
		
	}

	@Override
	public void method1(Role role, String menuIds, Long[] permissionIds) {
		//保存角色
		roleRepository.save(role);
		// 建立关联的时候,只能使用角色去关联菜单和权限,不能使用菜单和权限去关联角色
		// 原因在与菜单和权限的实体类中声明了mappedBy属性,意味着它们放弃了对关系的维护
		if (StringUtils.isNotEmpty(menuIds)) {
			String[] split = menuIds.split(",");
			for (String menuId : split) {
				Menu menu = menuRepository.findOne(Long.parseLong(menuId));
				role.getMenus().add(menu);			}
		}
		if (permissionIds!=null &&permissionIds.length>0) {
			for (Long permissionId : permissionIds) {
				Permission permission = permissionRepository.findOne(permissionId);
				role.getPermissions().add(permission);
			}
		}
		
	}
	
	
	
	
}
  
