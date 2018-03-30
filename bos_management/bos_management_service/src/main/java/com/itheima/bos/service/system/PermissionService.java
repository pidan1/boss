package com.itheima.bos.service.system;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itheima.bos.domain.system.Permission;

/**  
 * ClassName:PermissionService <br/>  
 * Function:  <br/>  
 * Date:     2018年3月30日 下午4:05:23 <br/>       
 */
public interface PermissionService {

	Page<Permission> findAll(Pageable pageable);

	void save(Permission model);

}
  
