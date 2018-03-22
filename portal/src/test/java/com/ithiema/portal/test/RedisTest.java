package com.ithiema.portal.test;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**  
 * ClassName:RedisTest <br/>  
 * Function:  <br/>  
 * Date:     2018年3月22日 下午9:00:48 <br/>       
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class RedisTest {

	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	@Test
	public void test1(){
		// 存储数据
		//redisTemplate.opsForValue().set("name", "zhangsan");
		
		// 存储数据并设置有效期
		
		//参数3： 时间值
		//参数4 ： 时间单位
		redisTemplate.opsForValue().set("age", "12", 10, TimeUnit.SECONDS);
		
	}
	
	
	
}
  
