package com.itheima.bos.dao.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/**  
 * ClassName:POITest <br/>  
 * Function:  <br/>  
 * Date:     2018年3月16日 下午4:14:03 <br/>       
 */
public class POITest {

	//在pom文件 引入poi坐标
	public static void main(String[] args) throws IOException {
		
		File file1 = new File("D:\\aa.xls");
		System.out.println(file1.getAbsolutePath());
		
		System.out.println("---------------------------------------");
		
		//读取文件C:\Users\58324\Desktop
		File file2 = new File("‪D:\\aa.xls");
		System.out.println(file2.getAbsolutePath());
		
		
		
		HSSFWorkbook workbook =new HSSFWorkbook(new FileInputStream(file1));
		
		
		//获取工作簿
		HSSFSheet sheet = workbook.getSheetAt(0);
		
		//代表一行
		for (Row row : sheet) {
			int rowNum = row.getRowNum();
			if (rowNum == 0) {
				continue;
			}
			//代表一行中的各个单元格
			for (Cell cell : row) {
				String value = cell.getStringCellValue();
				System.out.print(value+"\t");
			}
			System.out.println();
		}
		
	}
	
	
}
  
