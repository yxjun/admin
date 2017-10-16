package per.zc.common.route;

import com.jfinal.config.Routes;

import per.zc.common.constant.Constant;
import per.zc.system.controller.LoginController;
import per.zc.system.controller.SysMenuController;
import per.zc.system.controller.SysOrgController;
import per.zc.system.controller.SysRoleController;
import per.zc.system.controller.SysUserController;
 

public class SystemRoute extends Routes {

	@Override
	public void config() {
	 
		// 登陆控制器
		add("/login", LoginController.class,Constant.VIEW_PATH);
		
		//菜单管理
		add("/sysMenu", SysMenuController.class,Constant.VIEW_PATH);
		
		// 角色管理
		add("/sysRole", SysRoleController.class,Constant.VIEW_PATH);
		
		//用户管理
	    add("/sysUser", SysUserController.class,Constant.VIEW_PATH);
	    
	    add("/sysOrg",SysOrgController.class,Constant.VIEW_PATH);
	 
	}

}
