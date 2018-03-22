package com.itheima.crm.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.crm.dao.CustomerRepository;
import com.itheima.crm.domain.Customer;
import com.itheima.crm.service.CustomerService;

/**  
 * ClassName:CustomerServiceImpl <br/>  
 * Function:  <br/>  
 * Date:     2018年3月19日 下午4:13:52 <br/>       
 */
@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerRepository customerRepository;
	
	//查询所有客户的方法
	@Override
	public List<Customer> findAll() {

		return customerRepository.findAll();
	}

	// 查找未关联定区的客户
	@Override
	public List<Customer> findCustomersUnAssociated() {
		  
		return customerRepository.findByFixedAreaIdIsNull();
	}

	//查找关联到指定定区的客户
	@Override
	public List<Customer> findCustomersAssociated2FixedArea(String fixedAreaId) {
		  
		return customerRepository.findByFixedAreaId(fixedAreaId);
	}

	@Override
	public void assignCustomers2FixedArea(Long[] customerIds, String fixedAreaId) {
		  
		 // 根据定区ID,把关联到这个定区的所有客户全部解绑
		if (StringUtils.isNotEmpty(fixedAreaId)) {
			customerRepository.unbindCustomerByFixedArea(fixedAreaId);
		}
		
		// 要关联的数据和定区Id进行绑定
		if (StringUtils.isNotEmpty(fixedAreaId) && customerIds.length>0) {
			
			for (Long customerId : customerIds) {
				
				customerRepository.bindCustomer2FixedArea(customerId ,fixedAreaId);
			}
			
		}
	}

	//用于注册 保存用户
	@Override
	public void save(Customer customer) {
		  
		customerRepository.save(customer);		
	}

	@Override
	public void active(String telephone) {
		  
		customerRepository.active(telephone);
	}

}
  
