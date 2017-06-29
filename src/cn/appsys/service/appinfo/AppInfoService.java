package cn.appsys.service.appinfo;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.AppInfo;

public interface AppInfoService {
	public List<AppInfo> appInfoList(String querySoftwareName,
									Integer queryStatus,
									Integer queryCategoryLevel1,
									Integer queryCategoryLevel2,
									Integer queryCategoryLevel3,
									Integer queryFlatformId,
									Integer devId,
									Integer from,
									Integer pageSize)throws Exception;

	public int getAppInfoCount(String querySoftwareName,
							Integer queryStatus,
							Integer queryCategoryLevel1,
							Integer queryCategoryLevel2,
							Integer queryCategoryLevel3,
							Integer queryFlatformId,
							Integer devId)throws Exception;
	
	public boolean add(AppInfo appinfo) throws Exception;
	
	public AppInfo selectAppInfoAPKNameExist(String APKName) throws Exception;
	
	public boolean modify(AppInfo appinfo) throws Exception;
	
	public AppInfo getAppInfoById(Integer id) throws Exception;
	
	public boolean delAppInfoById(Integer id) throws Exception;
	
	public boolean updatePath(AppInfo appinfo)throws Exception;
	
	
	public List<AppInfo> appList(String querySoftwareName,
								Integer queryStatus,
								Integer queryCategoryLevel1,
								Integer queryCategoryLevel2,
								Integer queryCategoryLevel3,
								Integer queryFlatformId,
								Integer from,
								Integer pageSize)throws Exception;
	
	public int getAppCount(String querySoftwareName,
						Integer queryStatus,
						Integer queryCategoryLevel1,
						Integer queryCategoryLevel2,
						Integer queryCategoryLevel3,
						Integer queryFlatformId)throws Exception;
}
