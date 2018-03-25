package com.itheima.bos.dao.test;

import java.util.Random;

/**  
 * ClassName:RandomTest <br/>  
 * Function:  <br/>  
 * Date:     2018年3月24日 下午7:48:50 <br/>       
 */
public class RandomTest {

	public static void main(String[] args) {
		
		Random lijing = new Random();
		int i = lijing.nextInt(18);
		System.out.println("生成的随机数是:"+i);
	}
}
  
