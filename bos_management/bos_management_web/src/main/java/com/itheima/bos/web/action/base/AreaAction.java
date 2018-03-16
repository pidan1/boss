package com.itheima.bos.web.action.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.bouncycastle.jce.provider.JDKDSASigner.noneDSA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.base.Area;
import com.itheima.bos.service.base.AreaService;
import com.itheima.bos.web.action.CommomAction;
import com.itheima.utils.PinYin4jUtils;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**  
 * ClassName:AreaAction <br/>  
 * Function:  <br/>  
 * Date:     2018年3月16日 下午5:39:24 <br/>       
 */
@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class AreaAction extends CommomAction<Area>{

	
	public AreaAction() {
		super(Area.class);  
	}


	@Autowired
	private AreaService areaService;
	
	//获取导入的文件,用插件jQuery OCUpload插件的upload方法 导入 文件的属性名 默认是 file
	//我们通过属性驱动 获取它
	private File file;
	public void setFile(File file) {
		this.file = file;
	}
	
	//这个方法实现文件的导入
	@Action(value="areaAction_importXLS",results={@Result(name="success",
			location="/pages/base/area.html",type="redirect")})
	public String importXLS(){
		try {
		FileInputStream inputStream = new FileInputStream(file);
		//获取文件对象
		HSSFWorkbook workbook=new HSSFWorkbook(inputStream);
		//获取第一个工作簿
		HSSFSheet sheet = workbook.getSheetAt(0);
		
		List<Area> list=new ArrayList<>();
		
		for (Row row : sheet) {
			//跳过第一行
			if (row.getRowNum()==0) {
				continue;
			}
			//跳过第一列
			//获取一行中的 某个下标的 单元格
			//获得省,市,区,邮编
			String province = row.getCell(1).getStringCellValue();
			String city = row.getCell(2).getStringCellValue();
			String district = row.getCell(3).getStringCellValue();
			String postcode = row.getCell(4).getStringCellValue();
			//截取 去掉最后一个字符 省,市,区
			province = province.substring(0,province.length()-1);
			city = city.substring(0,city.length()-1);
			district = district.substring(0,district.length()-1);
			
			// 获取城市编码和简码
			String citycode = PinYin4jUtils.hanziToPinyin(city, "").toUpperCase();
			
			 String[] headByString = PinYin4jUtils.getHeadByString(province+city+district);
			String shortcode = PinYin4jUtils.stringArrayToString(headByString);
			
			// 封装数据
			Area area = new Area();
			area.setProvince(province);
			area.setCity(city);
            area.setDistrict(district);
            area.setPostcode(postcode);
            area.setCitycode(citycode);
            area.setShortcode(shortcode);
            
            list.add(area);
			
		}
		
		areaService.save(list);
		
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();  
		}
		return SUCCESS;
	}
	
	
	
	// AJAX请求不需要跳转页面
		@Action(value="areaAction_pageQuery")
		public String pageQuery() throws IOException{
			
			// EasyUI的页码是从1开始的
			// SPringDataJPA的页码是从0开始的
			 // 所以要-1
			Pageable pageable =new PageRequest(page-1, rows);
			
		 Page<Area> page =	areaService.findAll(pageable);
		 
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.setExcludes(new String[]{"subareas"});
			
			page2json(page, jsonConfig);
			
			return NONE;
		}

}
  
