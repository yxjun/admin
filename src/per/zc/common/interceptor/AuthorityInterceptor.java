package per.zc.common.interceptor;

import java.util.List;

import org.apache.log4j.Logger;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.StrKit;

import per.zc.common.constant.Constant;
import per.zc.system.model.SysMenu;
import per.zc.util.BaseController;


/**
 * 权限拦截器
 * @author Administrator
 *
 */
public class AuthorityInterceptor implements Interceptor {
	
	private final static Logger LOG = Logger.getLogger(AuthorityInterceptor.class);

	@Override
	public void intercept(Invocation inv) {
		String actionKey = inv.getActionKey();
		List<SysMenu> ownSysMenus = inv.getController().getSessionAttr(Constant.OWN_MENU);
		for(SysMenu sysMenu : ownSysMenus){
			// 拥有权限
			if(StrKit.notBlank(sysMenu.getUrl()) && actionKey.startsWith(sysMenu.getUrl())){
				System.err.println("权限拦截器   通过");
				inv.invoke();
				return ;
			}
		}
		
		System.err.println("权限拦截器   禁止");
		BaseController baseController =(BaseController)inv.getController();
		baseController.addOpLog("访问无权限路径["+actionKey+"]");
		 
		inv.getController().render("/WEB-INF/views/common/no_permission.html");
		 
	}

}
