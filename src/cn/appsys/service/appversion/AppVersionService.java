package cn.appsys.service.appversion;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.AppVersion;


public interface AppVersionService {
	public boolean add(AppVersion appversion) throws Exception;
	
	public ArrayList<AppVersion> getAppVersionById(Integer appId)throws Exception;
	
	public AppVersion getAppVersionByCon(Integer appId,String versionNo)throws Exception;
	
	public AppVersion getAppVersionByCom(Integer id)throws Exception;
	
	public boolean appversionmodify(AppVersion appversion)throws Exception;
}
