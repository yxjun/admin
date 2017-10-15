package per.zc.util;

import java.util.Date;

import com.jfinal.aop.Before;
import com.jfinal.aop.Duang;
import com.jfinal.kit.HashKit;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.plugin.druid.DruidPlugin;

import per.zc.system.model.SysMenu;
import per.zc.system.model.SysOrg;
import per.zc.system.model.SysRole;
import per.zc.system.model.SysRoleMenu;
import per.zc.system.model.SysUser;
import per.zc.system.model.SystemMappingKit;

/**
 * 初始数据 初始化
 * @author Administrator
 *
 */
public class DataInit {
	
	
	
	@Before(Tx.class)
	public   void dataInit(){
		
		// 初始化 角色 管理员
	    SysRole sysRole = new SysRole();
	    sysRole.setRoleName("管理员");
	    sysRole.setCreateTime(new Date());
	    sysRole.setSort(0);
	    sysRole.save();
	    
	    
	    // 初始化部门
	    SysOrg sysOrg = new SysOrg();
	    sysOrg.setOrgName("zc科技有限公司");
	    sysOrg.setSort(0);
	    sysOrg.save();
	    
	    
	    // 初始化用户
	    String userId = Identities.uuid2();
	    SysUser sysUser = new SysUser();
	    sysUser.setId(userId);
	    sysUser.setUsername("admin");
	    sysUser.setPassword(HashKit.sha1("123456"));
	    sysUser.setName("张闯");
	    sysUser.setOrgId(sysOrg.getId());
	    sysUser.setRoleId(sysRole.getId()+"");
	    sysUser.setCreateTime(new Date());
	    sysUser.save();
	    
	    
	   
	    SysMenu sysMenu = new SysMenu();
	    sysMenu.setName("系统管理");
	    sysMenu.setSort(0);
	    sysMenu.save();
	    
	    SysMenu sysMenu2 = new SysMenu();
	    sysMenu2.setName("菜单管理");
	    sysMenu2.setSort(0);
	    sysMenu2.setUrl("/menuMan");
	    sysMenu2.setPid(sysMenu.getId());
	    sysMenu2.save();
	    
	    
	    
	    // 角色 - 菜单表中 叶子和 根 全存
	    SysRoleMenu  sysRoleMenu = new SysRoleMenu();
	    sysRoleMenu.setRoleId(sysRole.getId());
	    sysRoleMenu.setMenuId(sysMenu.getId());
	    sysRoleMenu.save();
	    
	    sysRoleMenu.setMenuId(sysMenu2.getId());
	    sysRoleMenu.save();
	    
	    
	    
	       
	}
	

	public static void main(String[] args) {
		 
		PropKit.use("config.properties");
		DruidPlugin dbPlugin=new DruidPlugin(PropKit.get("jdbcUrl"), PropKit.get("user"), PropKit.get("password"));
		ActiveRecordPlugin arp=new ActiveRecordPlugin(dbPlugin);
		arp.setShowSql(true);
		SystemMappingKit.mapping(arp);
		dbPlugin.start();
		arp.start();
        
		DataInit dataInit = Duang.duang(DataInit.class);
		dataInit.dataInit();
		
		   

	}

}
