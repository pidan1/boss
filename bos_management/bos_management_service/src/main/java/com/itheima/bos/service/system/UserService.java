package com.itheima.bos.service.system;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itheima.bos.domain.system.User;

/**  
 * ClassName:UserService <br/>  
 * Function:  <br/>  
 * Date:     2018年3月30日 下午8:35:51 <br/>       
 */
public interface UserService {

	void save(User model, Long[] roleIds);

	Page<User> findAll(Pageable pageable);

}
  
