package cn.appsys.service.appversion;

import java.util.ArrayList;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.appsys.dao.appversion.VersionMapper;
import cn.appsys.pojo.AppVersion;
@Service
public class AppVersionServiceImpl implements AppVersionService{
	@Resource
	private VersionMapper versionMapper;
	
	public boolean add(AppVersion appversion) throws Exception {
		boolean flag = false;
		if(versionMapper.appversionadd(appversion) > 0){
			flag = true;
		}
		return flag;
	}

	public ArrayList<AppVersion> getAppVersionById(Integer appId) throws Exception {
		// TODO Auto-generated method stub
		return versionMapper.getAppVersionById(appId);
	}

	public AppVersion getAppVersionByCon(Integer appId,String versionNo) throws Exception {
		// TODO Auto-generated method stub
		return versionMapper.getAppVersionByCon(appId, versionNo);
	}

	public AppVersion getAppVersionByCom(Integer id) throws Exception {
		// TODO Auto-generated method stub
		return versionMapper.getAppVersionByCom(id);
	}

	public boolean appversionmodify(AppVersion appversion) throws Exception {
		// TODO Auto-generated method stub
		boolean flag = false;
		if(versionMapper.appversionmodify(appversion) > 0)
			flag = true;
		return flag;
	}
}

