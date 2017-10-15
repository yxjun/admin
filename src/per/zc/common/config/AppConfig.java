package per.zc.common.config;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.ext.interceptor.SessionInViewInterceptor;
import com.jfinal.kit.PropKit;
import com.jfinal.template.Engine;

import freemarker.template.TemplateModelException;
import per.zc.common.interceptor.AuthorityInterceptor;
import per.zc.common.route.SystemRoute;
import per.zc.system.model.SystemMappingKit;

import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.json.FastJsonFactory;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.render.FreeMarkerRender;
import com.jfinal.render.ViewType;

public class AppConfig extends JFinalConfig {
	/**
	 * 配置JFinal常量
	 */
	@Override
	public void configConstant(Constants me) {
		//读取数据库配置文件
		PropKit.use("config.properties");
		//设置当前是否为开发模式
		me.setDevMode(PropKit.getBoolean("devMode"));
		//设置默认上传文件保存路径 getFile等使用
		me.setBaseUploadPath("upload/temp/");
		//设置上传最大限制尺寸
		//me.setMaxPostSize(1024*1024*10);
		//设置默认下载文件路径 renderFile使用
		me.setBaseDownloadPath("download");
		//设置默认视图类型
		me.setViewType(ViewType.FREE_MARKER);
		//设置404渲染视图
		//me.setError404View("404.html");
		//设置json工厂
		me.setJsonFactory(FastJsonFactory.me());
		
		
	}
	 
	@Override
	public void configRoute(Routes me) {
	    me.add(new SystemRoute()); // system 模块 路由
	}
	 
	@Override
	public void configPlugin(Plugins me) {
	 
		DruidPlugin dbPlugin=new DruidPlugin(PropKit.get("jdbcUrl"), PropKit.get("user"), PropKit.get("password"));
		ActiveRecordPlugin arp=new ActiveRecordPlugin(dbPlugin);
		arp.setShowSql(PropKit.getBoolean("devMode"));
		arp.setDialect(new MysqlDialect());
		
		// 系统模块 设置
		SystemMappingKit.mapping(arp);
		
		me.add(dbPlugin);
		me.add(arp);
	}
	 
	@Override
	public void configInterceptor(Interceptors me) {
		
		// Action 级别 权限拦截器
		me.addGlobalActionInterceptor(new AuthorityInterceptor());
		me.addGlobalActionInterceptor(new SessionInViewInterceptor());
	}
	
 
	@Override
	public void configHandler(Handlers me) {

	}
	
 
	@Override
	public void afterJFinalStart() {
		// freemarker 中添加上下文路径
		try {
			FreeMarkerRender.getConfiguration().setSharedVariable("ctx", JFinal.me().getContextPath());
		} catch (TemplateModelException e) {
			e.printStackTrace();
		}	
	}
 
	@Override
	public void beforeJFinalStop() {
		
	}
	
	/**
	 * 配置模板引擎 
	 */
	@Override
	public void configEngine(Engine me) {
		//这里只有选择JFinal TPL的时候才用
		//配置共享函数模板
		//me.addSharedFunction("/view/common/layout.html")
	}
	
	public static void main(String[] args) {
		JFinal.start("WebRoot", 80, "/", 5);
	}
	

}
