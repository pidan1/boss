package com.itheima.bos.service.base.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.base.CourierRepository;
import com.itheima.bos.dao.base.FixedAreaRepository;
import com.itheima.bos.dao.base.SubAreaRepository;
import com.itheima.bos.dao.base.TakeTimeRepository;
import com.itheima.bos.domain.base.Courier;
import com.itheima.bos.domain.base.FixedArea;
import com.itheima.bos.domain.base.Standard;
import com.itheima.bos.domain.base.SubArea;
import com.itheima.bos.domain.base.TakeTime;
import com.itheima.bos.service.base.FixedAreaService;

/**  
 * ClassName:FixedAreaServiceImpl <br/>  
 * Function:  <br/>  
 * Date:     2018年3月19日 下午5:53:32 <br/>       
 */
@Service
@Transactional
public class FixedAreaServiceImpl implements FixedAreaService {

	@Autowired
	private FixedAreaRepository fixedAreaRepository;
	@Autowired
	private CourierRepository courierRepository;
	@Autowired
	private TakeTimeRepository takeTimeRepository;
	@Autowired
	private SubAreaRepository subAreaRepository;
	
	@Override
	public void save(FixedArea fixedArea) {
		  
		fixedAreaRepository.save(fixedArea);		
	}

	@Override
	public Page<FixedArea> findAll(Pageable pageable) {
		  
		return fixedAreaRepository.findAll( pageable);
	}

	//定区关联快递员
	@Override
	public void associationCourierToFixedArea(Long id, Long courierId, Long takeTimeId) {
		  //查出三个对象,通过操作持久态对象 进行关联,注入另外两个 dao
		FixedArea fixedArea = fixedAreaRepository.findOne(id);
		Courier courier = courierRepository.findOne(courierId);
		TakeTime takeTime = takeTimeRepository.findOne(takeTimeId);
		
		//建立快递员和时间的关系
		courier.setTakeTime(takeTime);
		
		//建立定区 和快递员之间的关系,注意 放弃维护的一方不可以操作
		fixedArea.getCouriers().add(courier);
		
	}

	//定区关联 分区
	@Override
	public void assignSubAreas2FixedArea(Long id, Long[] subAreaIds) {
		  
		//在实体类中,定区放弃了外键维护,所以 我们要通过 操作 分区来 操作数据
		//先解绑,找出该定区的所有分区,把分区里的定区字段 设为空
		FixedArea fixedArea = fixedAreaRepository.findOne(id);
		Set<SubArea> subareas = fixedArea.getSubareas();
		for (SubArea subArea : subareas) {
			subArea.setFixedArea(null);
		}
		
		//再绑定
		//根据 分区id 查出分区,在设置 分区里的定区字段
		for (Long subAreaId : subAreaIds) {
			SubArea subArea = subAreaRepository.findOne(subAreaId);
			subArea.setFixedArea(fixedArea);
		}
	}

}
  
