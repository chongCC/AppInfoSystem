package cn.appsys.controller.developer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.appsys.pojo.DeveloperUser;
import cn.appsys.service.developer.DevService;
import cn.appsys.tools.Constants;


@Controller
@RequestMapping(value="/developer")
public class DevLoginController {
	@Resource
	private DevService devService;
	@RequestMapping(value="/devLogin")
	public String devLogin(){
		return "developer/devLogin";
	}
	@RequestMapping(value="/doLogin",method=RequestMethod.POST)
	public String doLogin(@RequestParam String devCode,
						@RequestParam String devPassword,
						HttpServletRequest request,
						HttpSession session)throws Exception{
		System.out.println("**************");
		DeveloperUser developeruser = devService.login(devCode, devPassword);
		if(null != developeruser){
			session.setAttribute(Constants.DEV_USER_SESSION, developeruser);
			return "redirect:/dev/main";
		}else{
			request.setAttribute("error", "用户名或密码不正确");
			return "developer/devLogin";
		}		
	}
	
	
	@RequestMapping(value="/logout")
	public String logout(HttpSession session)throws Exception{
		session.removeAttribute(Constants.DEV_USER_SESSION);
		return "developer/devLogin";
	}
}
