package com.itheima.bos.dao.system;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itheima.bos.domain.system.User;

/**  
 * ClassName:UserRepository <br/>  
 * Function:  <br/>  
 * Date:     2018年3月28日 下午9:04:13 <br/>       
 */
public interface UserRepository extends JpaRepository<User, Long>{

	User findByUsername(String username);
}
  
