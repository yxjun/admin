package per.zc.system.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;

import per.zc.common.constant.Constant;
 
import per.zc.system.model.SysOrg;
import per.zc.system.model.SysUser;
import per.zc.util.BaseController;
 

public class SysOrgController  extends BaseController{
	
	public  void index(){
		 render( "system/sysOrg.html");   
	}
	
	public void orgQuery(){
		List<SysOrg> sysOrgs = SysOrg.dao.find("select * from sys_org order by sort");
		 
		SysUser loginUser = getSessionAttr(Constant.SYSTEM_USER);
		Integer orgId = loginUser.getOrgId();
		
		List<Map<String, Object>> maps = new ArrayList<>();
		for(SysOrg sysOrg : sysOrgs){
			Map<String, Object> map = new HashMap<>();
			map.put("id", sysOrg.getId());
			map.put("pid", sysOrg.getPid());
			
			map.put("open", true);   // ztree
			map.put("state", "open"); // easyui tree
			
			map.put("name", sysOrg.getOrgName());  
			
		    if (orgId == sysOrg.getId()) {
		    	map.put("checked", true); // easyui tree
			}
			
		    maps.add(map);
		}
		renderJson(maps);
		
	}
	
	
	public void orgAdd(){
		SysOrg sysOrg = getBean(SysOrg.class,"");
		boolean saveFlag = sysOrg.save();
		if(saveFlag) {
			renderText(Constant.ADD_SUCCESS);	
		}else {
			renderText(Constant.ADD_FAIL);
		}
	}
	
	
	public void  orgUpdate(){
		
		SysOrg sysOrg = getBean(SysOrg.class,"");
		boolean updateFlag = sysOrg.update();
		if (updateFlag) {
			renderText(Constant.UPDATE_SUCCESS);
		}else {
			renderText(Constant.UPDATE_FAIL);
		}
	}
	
	
	
	
	/**
	 * 1: 删除当前结构和子组织机构
	 * 2： 当前机构和子组织机构的 人员 orgId 设置为null
	 */
	@Before(Tx.class)
	public void orgDelete(){
		
		Integer id =getParaToInt("id");
		
		Record record = Db.findFirst("select getChildLst(?,'sys_org') as childrenIds ",id);
		String childrenIds = record.getStr("childrenIds");  // 子、孙 id
		 
		
		String deleteSql = "delete from sys_org where  id  in ("+childrenIds+")";
		Db.update(deleteSql);
		
		String updateSql = "update sys_user set org_id = null where  org_id in ("+childrenIds+")";
		Db.update(updateSql);
		
		renderText(Constant.DELETE_SUCCESS);
		
	}

}
