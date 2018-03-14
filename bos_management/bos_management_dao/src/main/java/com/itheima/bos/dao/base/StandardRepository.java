package com.itheima.bos.dao.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.domain.base.Standard;

/**  
 * ClassName:StandardRepository <br/>  
 * Function:  <br/>  
 * Date:     2018年3月14日 下午4:43:13 <br/>       
 */
//泛型1:封装数据的对象的类型
//泛型2 : 对象的主键的类型
public interface StandardRepository extends JpaRepository<Standard, Long>{

		//springDataJPA 提供了一套命名规范,遵循这一套规范自定义查询方法
	//只能定义查询相关的方法
	//必须使用findBy 开头,后面跟javabean属性的名字,首字母必须大写
	List<Standard> findByName(String name);
	
	List<Standard> findByNameLike(String name);
	
	List<Standard> findByNameAndMaxWeight(String name,Integer maxWeight);
	
	
	//如果不想按规范命名,自己写查询语句:JPQL===HQL
	@Query("from Standard where name=? and maxWeight=?")
	List<Standard> findByNameAndMaxWeight123123(String name,Integer maxWeight);
	
	//在?后面追加数字的形式,改变匹配参数的顺序
	@Query("from Standard where name=?2 and maxWeight=?1")
	List<Standard> findByNameAndMaxWeight123123(Integer maxWeight ,String name);
	
	//原生sql
	@Query(value="select * from T_STANDARD where C_NAME = ? and C_MAX_WEIGHT = ?"
			,nativeQuery=true)
	List<Standard> findByNameAndMaxWeightds123123(String name,Integer maxWeight);
	
	
	//自定义删除,只能自己写sql语句的形式
	@Modifying
	@Transactional
	@Query("update Standard set maxWeight = ? where name = ?")
	void updateWeightByName(Integer maxWeight, String name);
	
	
	@Modifying
	@Transactional
	@Query("delete from Standard where name = ?")
	void deleteByName(String name);
	
	
	
	
	
	
	
	
	
	
	
}
  
