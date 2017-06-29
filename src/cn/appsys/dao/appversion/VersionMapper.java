package cn.appsys.dao.appversion;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.AppVersion;


public interface VersionMapper {
	public int appversionadd(AppVersion appversion)throws Exception;
	
	public ArrayList<AppVersion> getAppVersionById(@Param("appId")Integer appId)throws Exception;
	
	public AppVersion getAppVersionByCon(@Param("appId")Integer appId,@Param("versionNo")String versionNo)throws Exception;
	
	public AppVersion getAppVersionByCom(@Param("id")Integer id)throws Exception;
	
	public int appversionmodify(AppVersion appversion)throws Exception;
}
