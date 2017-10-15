package per.zc.system.controller;

import java.util.List;

 

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.core.ActionKey;
import com.jfinal.ext.interceptor.SessionInViewInterceptor;
import com.jfinal.kit.HashKit;
import com.jfinal.kit.StrKit;

import per.zc.common.constant.Constant;
import per.zc.common.interceptor.AuthorityInterceptor;
import per.zc.system.model.SysMenu;
import per.zc.system.model.SysOrg;
import per.zc.system.model.SysRole;
import per.zc.system.model.SysUser;
import per.zc.system.model.SysUserRole;
import per.zc.util.BaseController;
import per.zc.util.TreeBuild;
import per.zc.util.TreeNode;

public class LoginController  extends BaseController {
	
	
	private final static Logger LOG = Logger.getLogger(LoginController.class);
	 
	
   /**
    * 登录静态页面
    */
   @Clear(AuthorityInterceptor.class)
   public void index(){
	   render( "system/login.html");  // 不可以斜杠开头，会认为 绝对路径
   }
   
   
   @Clear(AuthorityInterceptor.class)
   @Before(SessionInViewInterceptor.class)
   @ActionKey("/main")
   public void main(){
	   render("system/main.html");
   }
   
   
   
   @Clear(AuthorityInterceptor.class)
   @ActionKey("/treeMenu")
   public void treeMenu(){
	   // 查询菜单权限
	   List<SysMenu> ownSysMenus = getSessionAttr(Constant.OWN_MENU) ;  // 拥有的权限菜单
	   List<SysMenu> allSysMenus = SysMenu.dao.find("select * from sys_menu");        // 全部权限菜单
	   
	   List<TreeNode> treeNodes   = TreeBuild. easyuiTreeBuild(allSysMenus, ownSysMenus);
	   renderJson(treeNodes);
	    
   }  
   
   /**
    * 登陆表单提交地址
    */
   @Clear(AuthorityInterceptor.class)
   public void action(){
	   String username = getPara("username");
	   String password = getPara("password"); 
	   
	   if (StrKit.isBlank(username)) {
		 setAttr("errMsg", "请填写用户名。");
		   render("system/login.html");
		   return;
	   }
	   
	   if(StrKit.isBlank(password)){
		   setAttr("errMsg", "请填写密码。");
		   render("system/login.html");
		   return ;
	   }
	   
	   password = HashKit.sha1(password);
	   
	   String sql = "SELECT"
	   		+ " su.*,"
	   		+ " so.org_name AS orgName"
	   		+ " FROM sys_user su"
	   		+ " LEFT JOIN sys_org so ON su.org_id = so.id"
	   		+ " where username = ? and password = ? and disabled = '0'";
	   
	   List<SysUser> sysUsers = SysUser.dao.find(sql,username,password);
	   if(sysUsers.size()==0){
		   setAttr("errMsg", "用户名和密码不匹配！");
		   render("system/login.html");
		   return ;
	   }
	   
	   //登录用户信息
	   SysUser sysUser = sysUsers.get(0);
	   setSessionAttr(Constant.SYSTEM_USER, sysUser);
	   
	   // 查询角色
	   String  roleSql = "SELECT"
	   		+ " GROUP_CONCAT(sur.role_id) AS roleIds,"
	   		+ " GROUP_CONCAT(sr.role_name) AS roleNames"
	   		+ " FROM sys_user_role sur"
	   		+ " LEFT JOIN sys_role sr ON sur.role_id = sr.id"
	   		+ " WHERE user_id = ? "
	   		+ " GROUP BY sur.user_id";
	   SysUserRole sysUserRole = SysUserRole.dao.findFirst(roleSql,sysUser.getId());
	   // 角色名称
	   setSessionAttr(Constant.SYSTEM_USER_ROLES, sysUserRole.get("roleNames"));
	  
	    
	 
	   String ownMenuSql = "SELECT"
		   		+ " DISTINCT sm.*"
		   		+ " FROM sys_role_menu srm"
		   		+ " LEFT JOIN sys_menu sm ON srm.menu_id = sm.id"
		   		+ " WHERE role_id IN (?)"
		   		+ " order by 'sm.sort'";
		   
		  
	   List<SysMenu> ownSysMenus =SysMenu.dao.find(ownMenuSql,sysUserRole.get("roleIds"));  // 拥有的权限菜单
	   LOG.info("权限集合："+JSON.toJSONString(ownSysMenus));
	   setSessionAttr(Constant.OWN_MENU, ownSysMenus);
	 
	   redirect("/main");	   
   }
   
   
   @Clear(AuthorityInterceptor.class)
   @ActionKey("/logout")
   public void logout() {
	   removeSessionAttr(Constant.SYSTEM_USER);
	   removeSessionAttr(Constant.SYSTEM_USER_ROLES);
	   removeSessionAttr(Constant.OWN_MENU);
	   redirect("/login");
   }

	

}
