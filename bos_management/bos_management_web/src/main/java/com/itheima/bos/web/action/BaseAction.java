package com.itheima.bos.web.action;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.formula.functions.T;
import org.apache.struts2.ServletActionContext;
import org.springframework.data.domain.Page;

import com.itheima.bos.domain.base.Area;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**  
 * ClassName:CommomAction <br/>  
 * Function:  <br/>  
 * Date:     2018年3月16日 下午8:23:11 <br/>       
 */
//public class AreaAction extends BaseAction<Area>
public class BaseAction<T> extends ActionSupport implements ModelDriven<T>{

	private T model;
	
	@Override
	public T getModel() {
		// 以public class AreaAction extends BaseAction<Area>代码为例
		// 调用下面的代码以后,得到的是AreaAction的字节码
		 Class<? extends BaseAction> childClazz = this.getClass();
		
		// 得到的是BaseAction
		 childClazz.getSuperclass();
		// 得到的是BaseAction<Area>
		 Type genericSuperclass = childClazz.getGenericSuperclass();
		 ParameterizedType parameterizedType =(ParameterizedType) genericSuperclass;
		 Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
		  Class<T> clazz = (Class<T>) actualTypeArguments[0];
		  
		  try {
			model=clazz.newInstance();
		} catch (Exception e) {
			e.printStackTrace();  
		}
		  
		return model;
	}
	

}
  
