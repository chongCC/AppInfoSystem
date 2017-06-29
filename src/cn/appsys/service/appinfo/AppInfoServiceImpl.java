package cn.appsys.service.appinfo;

import java.io.File;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.appsys.dao.appinfo.InfoMapper;
import cn.appsys.pojo.AppInfo;
@Service
public class AppInfoServiceImpl implements AppInfoService{
	@Resource
	private InfoMapper infoMapper;
	public List<AppInfo> appInfoList(String querySoftwareName,
			Integer queryStatus, Integer queryCategoryLevel1,
			Integer queryCategoryLevel2, Integer queryCategoryLevel3,
			Integer queryFlatformId, Integer devId, Integer from,
			Integer pageSize) throws Exception {
		// TODO Auto-generated method stub
		from = (from-1)*pageSize;
		return infoMapper.appInfoList(querySoftwareName, queryStatus, queryCategoryLevel1, queryCategoryLevel2, queryCategoryLevel3, queryFlatformId, devId, from, pageSize);
	}
	public int getAppInfoCount(String querySoftwareName, Integer queryStatus,
			Integer queryCategoryLevel1, Integer queryCategoryLevel2,
			Integer queryCategoryLevel3, Integer queryFlatformId,
			Integer devId) throws Exception {
		// TODO Auto-generated method stub
		return infoMapper.getAppInfoCount(querySoftwareName, queryStatus, queryCategoryLevel1, queryCategoryLevel2, queryCategoryLevel3, queryFlatformId, devId);
	}
	public boolean add(AppInfo appinfo) throws Exception {
		boolean flag = false;
		if(infoMapper.appinfoadd(appinfo) > 0){
			flag = true;
		}
		return flag;
	}
	public AppInfo selectAppInfoAPKNameExist(String APKName) throws Exception {
		// TODO Auto-generated method stub
		return infoMapper.selectAPKName(APKName);
	}
	public boolean modify(AppInfo appinfo) throws Exception {
		// TODO Auto-generated method stub
		boolean flag = false;
		if(infoMapper.appinfomodify(appinfo) > 0)
			flag = true;
		return flag;
	}
	public AppInfo getAppInfoById(Integer id) throws Exception {
		// TODO Auto-generated method stub
		return infoMapper.getAppInfoById(id);
	}
	public boolean delAppInfoById(Integer id) throws Exception {
		// TODO Auto-generated method stub
		boolean flag = true;
		AppInfo appinfo = infoMapper.getAppInfoById(id);
		if(appinfo.getLogoLocPath() != null && !appinfo.getLogoLocPath().equals("")){
			File file = new File(appinfo.getLogoLocPath());
			if(file.exists()){
				flag = file.delete();
			}
		}
		if(appinfo.getLogoPicPath() != null && !appinfo.getLogoPicPath().equals("")){
			File file = new File(appinfo.getLogoPicPath());
			if(file.exists()){
				flag = file.delete();
			}
		}
		if(flag){
			if(infoMapper.delAppInfoById(id) < 1)
				flag = false;
		}
		return flag;
	}
	public List<AppInfo> appList(String querySoftwareName, Integer queryStatus,
			Integer queryCategoryLevel1, Integer queryCategoryLevel2,
			Integer queryCategoryLevel3, Integer queryFlatformId, Integer from,
			Integer pageSize) throws Exception {
		// TODO Auto-generated method stub
		from = (from-1)*pageSize;
		return infoMapper.appList(querySoftwareName, queryStatus, queryCategoryLevel1, queryCategoryLevel2, queryCategoryLevel3, queryFlatformId, from, pageSize);
	}
	public int getAppCount(String querySoftwareName, Integer queryStatus,
			Integer queryCategoryLevel1, Integer queryCategoryLevel2,
			Integer queryCategoryLevel3, Integer queryFlatformId)
			throws Exception {
		// TODO Auto-generated method stub
		return infoMapper.getAppCount(querySoftwareName, queryStatus, queryCategoryLevel1, queryCategoryLevel2, queryCategoryLevel3, queryFlatformId);
	}
	public boolean updatePath(AppInfo appinfo) throws Exception {
		// TODO Auto-generated method stub
		boolean flag = false;
		if(infoMapper.updatePath(appinfo) > 0)
			flag = true;
		return flag;
	}
	
}
