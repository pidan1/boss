package com.itheima.activemq;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.springframework.stereotype.Component;

import com.itheima.utils.SmsUtils;

/**  
 * ClassName:SmsConsumer <br/>  
 * Function:  <br/>  
 * Date:     2018年3月25日 下午9:50:41 <br/>       
 */
@Component
public class SmsConsumer implements MessageListener{

	@Override
	public void onMessage(Message msg) {
		  MapMessage mapMessage=(MapMessage) msg;
		  try {
			String code = mapMessage.getString("code");
			String tel = mapMessage.getString("tel");
			
			System.out.println(tel+"====="+code);
		//	SmsUtils.sendSms(tel, code);
		} catch (JMSException e) {
			  
			e.printStackTrace();  
			
		}
		
	}

}
  
