package cn.appsys.service.developer;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.appsys.dao.developer.DevMapper;
import cn.appsys.pojo.AppCategory;
import cn.appsys.pojo.AppInfo;
import cn.appsys.pojo.DataDictionary;
import cn.appsys.pojo.DeveloperUser;
@Service
public class DevServiceImpl implements DevService{
	@Resource
	private DevMapper devMapper;
	public DeveloperUser login(String devCode, String devPassword)
			throws Exception {
		DeveloperUser developeruser = null;
		try {
			developeruser = devMapper.getDevLogin(devCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(null != developeruser){
			if(!developeruser.getDevPassword().equals(devPassword)){
				developeruser = null;
			}
		}
		return developeruser;
	}
}
