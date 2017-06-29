package cn.appsys.service.backend;

import cn.appsys.pojo.BackendUser;

public interface BackService {
	public BackendUser login(String userCode,String userPassword)throws Exception;
}
