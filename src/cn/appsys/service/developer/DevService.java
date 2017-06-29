package cn.appsys.service.developer;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.AppCategory;
import cn.appsys.pojo.AppInfo;
import cn.appsys.pojo.DataDictionary;
import cn.appsys.pojo.DeveloperUser;


public interface DevService {
	
	public DeveloperUser login(String devCode,String devPassword)throws Exception;

}
