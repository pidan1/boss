package com.itheima.bos.service.base.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.base.CourierRepository;
import com.itheima.bos.dao.base.FixedAreaRepository;
import com.itheima.bos.dao.base.TakeTimeRepository;
import com.itheima.bos.domain.base.Courier;
import com.itheima.bos.domain.base.FixedArea;
import com.itheima.bos.domain.base.Standard;
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

}
  
