package com.itheima.crm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.itheima.crm.domain.Customer;

/**  
 * ClassName:CustomerRepository <br/>  
 * Function:  <br/>  
 * Date:     2018年3月19日 下午4:16:35 <br/>       
 */
public interface CustomerRepository extends JpaRepository<Customer, Long>{

	//找到未关联定区的客户
	List<Customer> findByFixedAreaIdIsNull();

	// 查找关联到指定定区的客户
	List<Customer> findByFixedAreaId(String fixedAreaId);

	@Modifying
	@Query("update Customer set fixedAreaId = null where fixedAreaId = ?")
	void unbindCustomerByFixedArea(String fixedAreaId);

	@Modifying
	@Query("update Customer set fixedAreaId = ?2 where id = ?1")
	void bindCustomer2FixedArea(Long customerId, String fixedAreaId);

	@Modifying
	@Query("update Customer set type = 1 where telephone = ?")
	void active(String telephone);

	
	//判断是否激活,用手机号码 查找对象
	Customer findByTelephone(String telephone);

	//用于登录
	Customer findByTelephoneAndPassword(String telephone, String password);

	//根据地址查询定区id
	@Query("select fixedAreaId from Customer where address = ?")
	String findFixedAreaIdByAdddress(String address);

	
	
}
  
