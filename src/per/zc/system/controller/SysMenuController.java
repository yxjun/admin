package per.zc.system.controller;

import java.util.List;
import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;

import per.zc.common.constant.Constant;
import per.zc.system.model.SysMenu;
import per.zc.util.BaseController;
import per.zc.util.SearchSql;
import per.zc.util.TreeBuild;
import per.zc.util.TreeNode;

/**
 * 系统菜单管理
 * @author Administrator
 *
 */
public class SysMenuController  extends BaseController {
	
	public  void index(){
		 render( "system/sysMenu.html");  // 不可以斜杠开头，会认为 绝对路径
	}
	
	
	
	
	public void query() {
		List<SysMenu> sysMenus = SysMenu.dao.findAll();
		List<Map<String, Object>> treeNodes =  TreeBuild.easyuiMenuTreegridBuild(sysMenus, sysMenus);
		renderJson(treeNodes);
	}
	
    
	
	
	public void add(){
		SysMenu sysMenu = getBean(SysMenu.class,"");
		boolean saveFlag =  sysMenu.save();
		if(saveFlag) {
			renderText(Constant.ADD_SUCCESS);	
		}else {
			renderText(Constant.ADD_FAIL);
		}
		 
	}
	
	@Before(Tx.class)
	public  void delete(){
		Integer sysMenuId =getParaToInt("id");
	 
		
		//TODO 级联删除
		String deleteSql = "delete from sys_menu where pid = ? or id = ?";
		Db.update(deleteSql,sysMenuId,sysMenuId);
		
		
		// 删除相应 的 角色权限关联
		deleteSql ="delete from sys_role_menu where menu_id = ? ";
		Db.update(deleteSql,sysMenuId);
		
		renderText(Constant.DELETE_SUCCESS);
		 
	}
	
	
	public void update(){
		SysMenu sysMenu = getBean(SysMenu.class,"");
		boolean updateFlag = sysMenu.update();
		if (updateFlag) {
			renderText(Constant.UPDATE_SUCCESS);
		}else {
			renderText(Constant.UPDATE_FAIL);
		}
		
	}

}
