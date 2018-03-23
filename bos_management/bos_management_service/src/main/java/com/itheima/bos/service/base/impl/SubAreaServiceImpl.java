package com.itheima.bos.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.inject.PrivateBinder;
import com.itheima.bos.dao.base.SubAreaRepository;
import com.itheima.bos.domain.base.FixedArea;
import com.itheima.bos.domain.base.SubArea;
import com.itheima.bos.service.base.SubAreaService;

/**  
 * ClassName:SubAreaServiceImpl <br/>  
 * Function:  <br/>  
 * Date:     2018年3月18日 下午4:29:06 <br/>       
 */
@Service
@Transactional
public class SubAreaServiceImpl implements SubAreaService {

	@Autowired
	private SubAreaRepository subAreaRepository;

	@Override
	public void save(SubArea subArea) {
		  
		subAreaRepository.save(subArea);
	}

	@Override
	public Page<SubArea> findAll(Pageable pageable) {
		  
		return subAreaRepository.findAll(pageable);
	}

	//查询未关联 定区的 分区信息
	@Override
	public List<SubArea> findUnAssociatedSubAreas() {
		  
		return subAreaRepository.findByfixedAreaIsNull();
	}

	//查询已关联 定区的 分区信息,根据定区信息
	@Override
	public List<SubArea> findAssociatedSubAreas(Long id) {
		FixedArea fixedArea = new FixedArea();
		fixedArea.setId(id);
		
		return subAreaRepository.findByfixedArea(fixedArea);
	}
	
}
  
