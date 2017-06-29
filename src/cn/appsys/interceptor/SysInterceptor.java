package cn.appsys.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.appsys.pojo.BackendUser;
import cn.appsys.pojo.DeveloperUser;
import cn.appsys.tools.Constants;


public class SysInterceptor extends HandlerInterceptorAdapter {
	private Logger logger=Logger.getLogger(SysInterceptor.class);
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response,
			Object handler)throws Exception{
		HttpSession session=request.getSession();
		BackendUser backenduser=(BackendUser) session.getAttribute(Constants.USER_SESSION);
		DeveloperUser developeruser=(DeveloperUser) session.getAttribute(Constants.DEV_USER_SESSION);
		if(null != backenduser){
			return true;
		}else if(null != developeruser){
			return true;
		}else{
			response.sendRedirect(request.getContextPath()+"/403.jsp");
			return false;
		}
	}
}
