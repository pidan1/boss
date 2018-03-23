package com.itheima.bos.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itheima.bos.domain.base.SubArea;

/**  
 * ClassName:SubAreaService <br/>  
 * Function:  <br/>  
 * Date:     2018年3月18日 下午4:28:37 <br/>       
 */
public interface SubAreaService {

	void save(SubArea model);

	//分页查询
	Page<SubArea> findAll(Pageable pageable);

	//查询未关联定区的 分区信息
	List<SubArea> findUnAssociatedSubAreas();

	//查询已关联定区的 分区信息
	List<SubArea> findAssociatedSubAreas(Long id);

}
  
