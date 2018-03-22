package com.itheima.bos.web.action.base;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.base.Courier;
import com.itheima.bos.domain.base.Standard;
import com.itheima.bos.service.base.CourierService;
import com.itheima.bos.web.action.CommomAction;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**  
 * ClassName:CourierAction <br/>  
 * Function:  <br/>  
 * Date:     2018年3月15日 下午4:32:51 <br/>       
 */

@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class CourierAction extends CommomAction<Courier> {
	

	public CourierAction() {
		super(Courier.class);  
	}
	@Autowired
	private CourierService courierService;
	
	@Action(value="courierAction_save",
			results={@Result(name="success",location="/pages/base/courier.html",type="redirect")})
	public String save() {
		courierService.save(getModel());
		return SUCCESS;
	}
	
	//分页查询的方法
	@Action(value="courierAction_pageQuery")
	public String pageQuery() throws IOException{
		
		//dao实现了，条件查询的接口后，就可以使用相应的方法了，需要构建查询条件 并往下传
		//构建查询条件
		Specification<Courier> specification =new Specification<Courier>() {

			/**
			 * 参数1：root ：根对象，简单的理解为泛型对象，
			 *   参数2 ： cb :构建查询条件的对象
			 */
			@Override
			public Predicate toPredicate(Root<Courier> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				  
				//页面传递过来的数据，都在模型了，我们取出来，判空 并构建查询条件
				String courierNum = getModel().getCourierNum();//工号
				String company = getModel().getCompany();//公司
				String type = getModel().getType();//类型
				Standard standard = getModel().getStandard();
				
				List<Predicate> list=new ArrayList<>();
				
				if (StringUtils.isNotEmpty(courierNum)) {
					//快递员 工号不为空，构建一个等值查询条件
					 // where courierNum = "001"
                    // 参数二 : 具体的要比较的值
					Predicate p1 = cb.equal(root.get("courierNum").as(String.class), courierNum);
					list.add(p1);
				}
				if (StringUtils.isNotEmpty(company)) {
					// 公司不为空，构建一个模糊查询条件
					Predicate p2 = cb.like(root.get("company").as(String.class), "%"+company+"%");
					list.add(p2);
				}
				if (StringUtils.isNotEmpty(type)) {
					// 如果类型不为空,构建一个等值查询条件
					
					Predicate p3 = cb.equal(root.get("type").as(String.class), type);
					list.add(p3);
				}
				
				if (standard!=null) {
					String name = standard.getName();
					//在这里我们要判断的是 standard中的name字段
					if (StringUtils.isNotEmpty(name)) {
						// 连表查询,查询标准的名字
						Join<Object, Object> join = root.join("standard");
						Predicate p4 = cb.equal(join.get("name").as(String.class), name);
						list.add(p4);
					}
				}
				//如果用户没有输入查询条件
				if (list.size()==0) {
					return null;
					
				}
				
				//用户输入了查询条件
				//将多个条件进行组合
				Predicate[] arr =new Predicate[list.size()];
				list.toArray(arr);
				//当我们要把所有条件组合起来是，这里接受的参数 是一个Predicate的数组，所以我们在上面 构建一个list 收集条件，最后转成 数组
				Predicate predicate = cb.and(arr);
				
				return predicate;
			}
		};
		
		Pageable pageable = new PageRequest(page-1, rows);
		Page<Courier> page = courierService.findAll(specification,pageable);
		
		//忽略不需要 返回的数据
		JsonConfig jsonConfig = new JsonConfig();
		//fixedAreas
		jsonConfig.setExcludes(new String[]{"fixedAreas","takeTime"});
		
		page2json(page, jsonConfig);
		
		return NONE;
	}
	// 使用属性驱动获取要删除的快递员的Id
	private String ids;
	public void setIds(String ids) {
		this.ids = ids;
	}
	
	@Action(value="courierAction_batchDel",
			results={@Result(name="success",
			location="/pages/base/courier.html",type="redirect")})
	public String batchDel(){
		courierService.batchDel(ids);
		return SUCCESS;
	}
	
	@Action("courierAction_listajax")
	public String listajax() throws IOException{
		//查询所有在职的快递员
		Specification<Courier> specification = new Specification<Courier>() {
			@Override
			public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				  Predicate predicate = cb.isNull(root.get("deltag").as(Character.class));
				return predicate;
			}
		};
		Page<Courier> page = courierService.findAll(specification, null);
		List<Courier> list = page.getContent();
		
		//忽略不需要 返回的数据
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setExcludes(new String[]{"fixedAreas","takeTime"});
		
		list2json(list, jsonConfig);
		return NONE;
	}
	
	@Action("courierAction_listajax2")
	public String listajax2() throws IOException{
		//查询所有在职的快递员
		List<Courier> list = courierService.findAvaible();
		
		//忽略不需要 返回的数据
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setExcludes(new String[]{"fixedAreas","takeTime"});
		
		list2json(list, jsonConfig);
		return NONE;
	}
	
	
	
	
	
}
  
