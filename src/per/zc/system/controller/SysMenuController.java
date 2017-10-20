package per.zc.system.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;

import per.zc.common.constant.Constant;
import per.zc.system.model.SysMenu;
import per.zc.system.model.SysRole;
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
	
	
	
	
	@Before(SearchSql.class)
	public void query() {
		int pageNumber = getAttr("pageNumber");
		int pageSize = getAttr("pageSize");
		
		// TODO  pid 只能查出子，不能查出孙，需要修改
		String where = getAttr(Constant.SEARCH_SQL);

		String sqlSelect = " select * ";
		String sqlExceptSelect = " from sys_menu  ";
		if (StrKit.notBlank(where)) {
			sqlExceptSelect += " where " + where;
		}
		sqlExceptSelect += " order by id asc , sort asc ";
		Page<SysMenu> sysMenus = SysMenu.dao.paginate(pageNumber, pageSize, sqlSelect, sqlExceptSelect);

		renderDatagrid(sysMenus);
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
		Integer id =getParaToInt("id");
	 
		Record record = Db.findFirst("select getChildLst(?,'sys_menu') as childrenIds ",id);
		String childrenIds = record.getStr("childrenIds");  // 子、孙 id
	 
		
		//TODO 级联删除
		String deleteSql = "delete from sys_menu where id in ("+childrenIds+")";
		Db.update(deleteSql);
		
		
		// 删除相应 的 角色权限关联
		deleteSql ="delete from sys_role_menu where menu_id in ("+childrenIds+") ";
		Db.update(deleteSql);
		
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
	
	
	/**
	 * 全部菜单
	 */
	public void allMenu(){
		List<SysMenu> sysMenus =  SysMenu.dao.findAll();
		List<Map<String,Object>> maps = new ArrayList<>();
		for(SysMenu sysMenu: sysMenus){
			Map<String,Object> map = new HashMap<>();
			map.put("id", sysMenu.getId());
			map.put("pid", sysMenu.getPid());
			map.put("text", sysMenu.getName());
			map.put("icon", sysMenu.getIcon());
			map.put("open", true);
			maps.add(map);
		}
		renderJson(maps);
	}

}
