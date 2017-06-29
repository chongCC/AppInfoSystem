package cn.appsys.controller.backend;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.appsys.pojo.BackendUser;
import cn.appsys.service.backend.BackService;
import cn.appsys.tools.Constants;

@Controller
@RequestMapping("/backend")
public class UserLoginController {
	@Resource
	private BackService backService;
	
	@RequestMapping(value="/backLogin")
	public String backLogin(){
		return "backend/backLogin";
	}
	@RequestMapping(value="/doLogin",method=RequestMethod.POST)
	public String doLogin(@RequestParam String userCode,
						@RequestParam String userPassword,
						HttpServletRequest request,
						HttpSession session)throws Exception{
		BackendUser backenduser = backService.login(userCode, userPassword);
		if(null != backenduser){
			session.setAttribute(Constants.USER_SESSION, backenduser);
			return "redirect:/back/main";
		}else{
			request.setAttribute("error", "用户名或密码不正确");
			return "backend/backLogin";
		}		
	}
	
	
	@RequestMapping(value="logout")
	public String logout(HttpSession session)throws Exception{
		session.removeAttribute(Constants.USER_SESSION);
		return "backend/backLogin";
	}
	
}
