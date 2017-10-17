package per.zc.system.controller;

import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;

import per.zc.common.constant.Constant;
 
import per.zc.system.model.SysOrg;
import per.zc.util.BaseController;
import per.zc.util.TreeBuild;

public class SysOrgController  extends BaseController{
	
	public  void index(){
		 render( "system/sysOrg.html");  // 不可以斜杠开头，会认为 绝对路径
	}
	
	public void query(){
		List<SysOrg> sysOrgs = SysOrg.dao.find("select * from sys_org order by sort");
		List<SysOrg> sysOrgTreeGrid = TreeBuild.easyuiOrgTreegridBuild(sysOrgs);
		renderJson(sysOrgTreeGrid);
		
	}
	
	
	public void add(){
		SysOrg sysOrg = getBean(SysOrg.class,"");
		boolean saveFlag = sysOrg.save();
		if(saveFlag) {
			renderText(Constant.ADD_SUCCESS);	
		}else {
			renderText(Constant.ADD_FAIL);
		}
	}
	
	
	public void  update(){
		
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
	public void delete(){
		
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
