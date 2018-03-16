package com.itheima.bos.dao.test;

import com.itheima.utils.PinYin4jUtils;

/**  
 * ClassName:PinyinTest <br/>  
 * Function:  <br/>  
 * Date:     2018年3月16日 下午4:45:37 <br/>       
 */
public class PinyinTest {
 
	public static void main(String[] args) {
		
		String province = "广东省";
        String city = "深圳市";
        String district = "宝安区";
        
        province = province.substring(0,province.length()-1);
        city = city.substring(0, city.length() - 1);
        district = district.substring(0, district.length() - 1);
     
        // 城市编码 SHENZHEN 简码 GDSZBA
		String hanziToPinyin = PinYin4jUtils.hanziToPinyin(city);
		String hanziToPinyin2 = PinYin4jUtils.hanziToPinyin(city, "").toUpperCase();
		System.out.println(hanziToPinyin);
		System.out.println(hanziToPinyin2);
		
		String[] headByString = PinYin4jUtils.getHeadByString(province+city+district);
		String string = PinYin4jUtils.stringArrayToString(headByString);
		System.out.println(string);
		
		
	}
	
	
}
  
