package per.zc.common.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import per.zc.common.constant.Constant;
import per.zc.util.UrlUtil;


/**
 * 登陆拦截器
 * @author Administrator
 *
 */
public class LoginFilter implements Filter{
 

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		 
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
			throws IOException, ServletException {
		// 强制 转换类型
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		
		//等同参数 true， 有则返回，无则创建； 
		HttpSession session = request.getSession(); 
		
		// 当前路径
		String curUrl = UrlUtil.formatUrl(request.getRequestURI());
		
		//上下文路径
		String contextPath = request.getContextPath();
		req.setAttribute("ctx", contextPath);
		 
		// 不拦截 掉登陆 页面和静态资源
		if(!curUrl.endsWith("login/index") 
				&&!curUrl.endsWith("login/action") 
				&& !curUrl.startsWith(contextPath+"/res")){
			
			// 未登陆，跳转到登陆界面
			if(session.getAttribute(Constant.SYSTEM_USER)==null){
				response.sendRedirect(request.getContextPath()+"/login/index");   // 跳转到登陆界面
				return; 
			}
			
			// 当前路径为 contextPath 或者 contextPath+"index" 时 跳转到  main
			if(curUrl.equals(contextPath) || curUrl.equals(contextPath+"/index")){
				response.sendRedirect(request.getContextPath()+"/main");
				return; 
			}
		}
		filterChain.doFilter(req, res);
	}


	@Override
	public void destroy() {
		 
		
	}
 
}
