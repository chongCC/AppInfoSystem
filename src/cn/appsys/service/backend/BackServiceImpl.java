package cn.appsys.service.backend;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.appsys.dao.backend.BackMapper;
import cn.appsys.pojo.BackendUser;
@Service
public class BackServiceImpl implements BackService{
	@Resource
	private BackMapper backMapper;
	public BackendUser login(String userCode, String userPassword)
			throws Exception {
		BackendUser backenduser = null;
		try {
			backenduser = backMapper.getBackLogin(userCode);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		if(null != backenduser){
			if(!backenduser.getUserPassword().equals(userPassword)){
				backenduser = null;
			}
		}
		return backenduser;
	}

}
