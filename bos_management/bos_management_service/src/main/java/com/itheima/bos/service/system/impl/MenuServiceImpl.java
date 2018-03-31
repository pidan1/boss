package com.itheima.bos.service.system.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.system.MenuRepository;
import com.itheima.bos.domain.system.Menu;
import com.itheima.bos.domain.system.User;
import com.itheima.bos.service.system.MenuService;

/**  
 * ClassName:MenuServiceImpl <br/>  
 * Function:  <br/>  
 * Date:     2018年3月30日 上午11:32:30 <br/>       
 */
@Service
@Transactional
public class MenuServiceImpl implements MenuService {

	@Autowired
	private MenuRepository menuRepository;
	
	@Override
	public List<Menu> findLevelOne() {
		  
		return menuRepository.findByParentMenuIsNull();
	}

	@Override
	public void save(Menu menu) {
		// 判断用户是否要添加一级菜单,父菜单是否id为null
		//如果我们添加 父菜单,就不勾选下拉框,但是保存时,会自动new一个父菜单出来,但是id是null,发生保存瞬时态异常
		//我们直接把框架new的父菜单 对象置为null,就不存在 瞬时态问题了
		if (menu.getParentMenu()!=null 
				&& menu.getParentMenu().getId()==null) {
			menu.setParentMenu(null);
		}
		menuRepository.save(menu);
		
	}

	@Override
	public Page<Menu> findAll(Pageable pageable) {
		  
		return menuRepository.findAll(pageable);
	}

	@Override
	public List<Menu> findbyUser(User user) {
		String username = user.getUsername();
		//如果是管理员,直接返回所有的菜单
		  if ("admin".equals(user.getUsername())) {
			  return  menuRepository.findAll();
		}
		return menuRepository.findbyUser(user.getId());
	}


	
	
}
  
