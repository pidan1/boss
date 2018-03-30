package com.itheima.bos.dao.system;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itheima.bos.domain.system.Menu;

/**  
 * ClassName:MenuRepository <br/>  
 * Function:  <br/>  
 * Date:     2018年3月30日 上午11:33:17 <br/>       
 */
public interface MenuRepository extends JpaRepository<Menu, Long>{

	List<Menu> findByParentMenuIsNull();
}
  
