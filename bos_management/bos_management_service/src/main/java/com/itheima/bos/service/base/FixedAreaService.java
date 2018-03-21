package com.itheima.bos.service.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itheima.bos.domain.base.FixedArea;
import com.itheima.bos.domain.base.Standard;

/**  
 * ClassName:FixedAreaService <br/>  
 * Function:  <br/>  
 * Date:     2018年3月19日 下午5:52:52 <br/>       
 */
public interface FixedAreaService {

	void save(FixedArea fixedArea);

	Page<FixedArea> findAll(Pageable pageable);

}
  
