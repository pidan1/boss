package com.itheima.bos.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itheima.bos.domain.base.Area;

/**  
 * ClassName:AreaService <br/>  
 * Function:  <br/>  
 * Date:     2018年3月16日 下午5:38:25 <br/>       
 */
public interface AreaService {

	void save(List<Area> list);

	Page<Area> findAll(Pageable pageable);

	List<Area> findByQ(String q);

}
  
