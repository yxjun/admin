package per.zc.system.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;

import per.zc.common.constant.Constant;
import per.zc.system.model.SysMenu;
import per.zc.system.model.SysRole;
import per.zc.system.model.SysRoleMenu;
import per.zc.util.BaseController;
import per.zc.util.SearchSql;

public class SysRoleController extends BaseController {

	public void index() {

		render("system/sysRole.html");

	}

	/**
	 * 全部菜单树，并根据角色选中
	 */
	public void menuTreePermissionChecked(){
		Integer id= getParaToInt(0);
		List<SysRoleMenu> sysRoleMenus =
				SysRoleMenu.dao.find("select * from sys_role_menu where role_id = ? ", id);

		List<SysMenu> sysMenus = SysMenu.dao.findAll();
		List<Map<String,Object>> maps = new ArrayList<Map<String,Object>>();

		for(SysMenu sysMenu : sysMenus){
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("id",sysMenu.getId());
			map.put("pid",sysMenu.getPid());
			map.put("name",sysMenu.getName());
			map.put("open",true);
			for(SysRoleMenu sysRoleMenu : sysRoleMenus){
				if(sysRoleMenu.getMenuId()==sysMenu.getId()){
					map.put("checked",true);
					break;
				}
			}
			maps.add(map);
		}
		renderJson(maps);
	}



	@Before(SearchSql.class)
	public void query() {

		int pageNumber = getAttr("pageNumber");
		int pageSize = getAttr("pageSize");
		String where = getAttr(Constant.SEARCH_SQL);

		String sqlSelect = " select * ";
		String sqlExceptSelect = " from sys_role order by sort asc ";
		if (StrKit.notBlank(where)) {
			sqlExceptSelect += " where " + where;
		}
		Page<SysRole> sysMenus = SysRole.dao.paginate(pageNumber, pageSize, sqlSelect, sqlExceptSelect);

		renderDatagrid(sysMenus);
	}

	
	public void add() {
		SysRole sysRole = getBean(SysRole.class, "");
		sysRole.setCreateTime(new Date());
		boolean saveFlag = sysRole.save();
		if (saveFlag) {
			renderText(Constant.ADD_SUCCESS);
		} else {
			renderText(Constant.ADD_FAIL);
		}

	}

	public void update() {
		SysRole sysRole = getBean(SysRole.class,"");
		boolean updateFlag = sysRole.update();
		if (updateFlag) {
			renderText(Constant.UPDATE_SUCCESS);
		}else {
			renderText(Constant.UPDATE_FAIL);
		}

	}

	
	@Before(Tx.class)
	public void delete() {
		
		Integer id = getParaToInt("id");
		String deleteSql = "delete from sys_role where id = ?";
		Db.update(deleteSql,id);
		
		deleteSql = "delete from sys_role_menu where role_id = ?";
		Db.update(deleteSql,id);
		
		deleteSql = "delete from sys_user_role where role_id = ?";
		Db.update(deleteSql,id);
		
		renderText(Constant.DELETE_SUCCESS);

	}
	
	


	/**
	 * 用户赋予权限
	 */
	@Before(Tx.class)
	public void givePermission(){
		Integer roleId  =getParaToInt("roleId");
		String permissIds = getPara("permissIds");
        String deleteSql = "delete from  sys_role_menu where role_id = ?";
		Db.update(deleteSql,roleId);

		String[] menuIds = permissIds.split(";");
		for(int i=0;i<menuIds.length;i++){
			SysRoleMenu sysRoleMenu = new SysRoleMenu();
			sysRoleMenu.setRoleId(roleId);
			sysRoleMenu.setMenuId(Integer.parseInt(menuIds[i]));
			sysRoleMenu.save();
		}

		renderText("赋权成功");
	}

}
