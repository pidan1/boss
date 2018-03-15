package com.itheima.bos.service.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.itheima.bos.domain.base.Courier;

/**  
 * ClassName:CourierService <br/>  
 * Function:  <br/>  
 * Date:     2018年3月15日 下午4:32:00 <br/>       
 */
public interface CourierService {

	void save(Courier courier);

	Page<Courier> findAll(Pageable pageable);

	void batchDel(String ids);

	Page<Courier> findAll(Specification<Courier> specification, Pageable pageable);

}
  
