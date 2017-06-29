package cn.appsys.controller.developer;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;

import cn.appsys.dao.appversion.VersionMapper;
import cn.appsys.pojo.AppCategory;
import cn.appsys.pojo.AppInfo;
import cn.appsys.pojo.AppVersion;
import cn.appsys.pojo.DataDictionary;
import cn.appsys.pojo.DeveloperUser;
import cn.appsys.service.appinfo.AppInfoService;
import cn.appsys.service.appversion.AppVersionService;
import cn.appsys.service.category.CategoryService;
import cn.appsys.service.developer.DevService;
import cn.appsys.service.dictionary.DictionaryService;
import cn.appsys.tools.Constants;
import cn.appsys.tools.PageSupport;

@Controller
@RequestMapping(value="/dev")
public class AppController {
	@Resource
	private DevService devService;
	@Resource
	private AppInfoService appInfoService;
	@Resource
	private CategoryService gategoryService;
	@Resource
	private DictionaryService dictionaryService;
	@Resource
	private AppVersionService appVersionService;
	@RequestMapping(value="/main")
	public String main(){
		return "developer/main";
	}
	/**
	 * 展示APP信息
	 * @param model
	 * @param querySoftwareName
	 * @param queryStatus
	 * @param queryCategoryLevel1
	 * @param queryCategoryLevel2
	 * @param queryCategoryLevel3
	 * @param queryFlatformId
	 * @param pageIndex
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/appinfolist")
	public String appinfolist(Model model,
			@RequestParam(value="querySoftwareName",required=false) String querySoftwareName,
			@RequestParam(value="queryStatus",required=false) Integer queryStatus,
			@RequestParam(value="queryCategoryLevel1",required=false) Integer queryCategoryLevel1,
			@RequestParam(value="queryCategoryLevel2",required=false) Integer queryCategoryLevel2,
			@RequestParam(value="queryCategoryLevel3",required=false) Integer queryCategoryLevel3,
			@RequestParam(value="queryFlatformId",required=false) Integer queryFlatformId,
			@RequestParam(value="pageIndex",required=false) Integer pageIndex,HttpSession session){
		
		int devId=((DeveloperUser)session.getAttribute(Constants.DEV_USER_SESSION)).getId();
		List<AppInfo> appInfoList=null;
		List<DataDictionary> flatformList = null;
		List<DataDictionary> statusList = null;
		List<AppCategory> CategoryLevel1List = null;
		List<AppCategory> CategoryLevel2List = null;
		List<AppCategory> CategoryLevel3List = null;
		
		
		
		int pageSize = Constants.pageSize;
    	//当前页码
    	int currentPageNo = 1;
	
    	if(pageIndex != null){
    		try{
    			currentPageNo = Integer.valueOf(pageIndex);
    		}catch(NumberFormatException e){
    			return "redirect:/syserror.html";
    		}
    	}	
    	//总数量（表）	
    	int totalCount = 0;
		try {
			totalCount = appInfoService.getAppInfoCount(querySoftwareName, queryStatus, queryCategoryLevel1, queryCategoryLevel2, queryCategoryLevel3, queryFlatformId, devId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	//总页数
    	PageSupport pages=new PageSupport();
    	pages.setCurrentPageNo(currentPageNo);
    	pages.setPageSize(pageSize);
    	pages.setTotalCount(totalCount);
    	int totalPageCount = pages.getTotalPageCount();
    	//控制首页和尾页
    	if(currentPageNo < 1){
    		currentPageNo = 1;
    	}else if(currentPageNo > totalPageCount){
    		currentPageNo = totalPageCount;
    	}
		try {
			flatformList = dictionaryService.dataDictionaryList("APP_FLATFORM");
			statusList = dictionaryService.dataDictionaryList("APP_STATUS");
			CategoryLevel1List = gategoryService.categorylevellist(null);
			appInfoList = appInfoService.appInfoList(querySoftwareName, queryStatus, queryCategoryLevel1, queryCategoryLevel2, queryCategoryLevel3, queryFlatformId, devId, currentPageNo, pageSize);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.addAttribute("CategoryLevel1List",CategoryLevel1List);
		model.addAttribute("flatformList",flatformList);
		model.addAttribute("statusList",statusList);
		model.addAttribute("appInfoList",appInfoList);
		model.addAttribute("querySoftwareName",querySoftwareName);
		model.addAttribute("queryStatus",queryStatus);
		model.addAttribute("queryCategoryLevel1",queryCategoryLevel1);
		model.addAttribute("queryCategoryLevel2",queryCategoryLevel2);
		model.addAttribute("queryCategoryLevel3",queryCategoryLevel3);
		model.addAttribute("queryFlatformId",queryFlatformId);
		model.addAttribute("pages", pages);
		
		if(queryCategoryLevel1 != null && !queryCategoryLevel1.equals("")){
			CategoryLevel2List = categorylevellist(queryCategoryLevel1);
			model.addAttribute("CategoryLevel2List",CategoryLevel2List);
		}
		if(queryCategoryLevel2 != null && !queryCategoryLevel2.equals("")){
			CategoryLevel3List = categorylevellist(queryCategoryLevel2);
			model.addAttribute("CategoryLevel3List",CategoryLevel3List);
		}
		
		return "developer/appinfolist";
	}
	/**
	 * 实现级联分类
	 * @param pid
	 * @return
	 */
	@RequestMapping(value="/categorylevellist.json")
	@ResponseBody
	public List<AppCategory> categorylevellist(@RequestParam Integer pid){
		List<AppCategory> listcategory=new ArrayList<AppCategory>();
		try {
			listcategory = gategoryService.categorylevellist(pid);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listcategory;
	}
	/**
	 * 进入添加页面
	 * @param appinfo
	 * @return
	 */
	@RequestMapping(value="/appinfoadd",method=RequestMethod.GET)
	public String appinfoadd(@ModelAttribute("appinfo") AppInfo appinfo){
		return "developer/appinfoadd";
	}
	/**
	 * 添加保存方法
	 * @param appinfo
	 * @param session
	 * @param request
	 * @param attach
	 * @return
	 */
	@RequestMapping(value="/appinfoaddsave",method=RequestMethod.POST)
	public String appinfoaddSave(AppInfo appinfo,HttpSession session,HttpServletRequest request,
			@RequestParam(value="a_logoPicPath",required=false)MultipartFile attach){
		DeveloperUser devUser=(DeveloperUser)session.getAttribute(Constants.DEV_USER_SESSION);
		appinfo.setDevId(devUser.getId());
		appinfo.setStatus(1);
		appinfo.setCreatedBy(devUser.getCreatedBy());
		appinfo.setCreatedBy(devUser.getId());
		appinfo.setCreationDate(new Date());
		String logoLocPath = null;
		String logoPicPath = null;
		if(!attach.isEmpty()){
			String path = request.getSession().getServletContext()
					.getRealPath("statics"+File.separator+"uploadfiles");
		String oldFileName = attach.getOriginalFilename();
		String prefix=FilenameUtils.getExtension(oldFileName);
		int filesize = 50000;
		if(attach.getSize() > filesize){
			request.setAttribute("fileUploadError", " * 上传大小不得超过50KB");
			return "developer/appinfoadd";
		}else if(prefix.equalsIgnoreCase("jpg") || prefix.equalsIgnoreCase("png") 
        		|| prefix.equalsIgnoreCase("jpeg") || prefix.equalsIgnoreCase("pneg")){//上传图片格式不正确
        	String fileName = System.currentTimeMillis()+RandomUtils.nextInt(1000000)+"_Personal.jpg";  
            File targetFile = new File(path, fileName);  
            if(!targetFile.exists()){  
                targetFile.mkdirs();  
            }
            try {  
            	attach.transferTo(targetFile);  
            } catch (Exception e) {  
                e.printStackTrace();  
                request.setAttribute("fileUploadError", " * 上传失败！");
                return "developer/appinfoadd";
            }
            logoLocPath = path+File.separator+fileName;
            logoPicPath="/AppInfoSystem/statics/uploadfiles/"+fileName;
		}else{
			request.setAttribute("fileUploadError", " * 上传图片格式不正确");
			return "developer/appinfoadd";
		}
	}	
		appinfo.setLogoLocPath(logoLocPath);
		appinfo.setLogoPicPath(logoPicPath);
		try {
			if(appInfoService.add(appinfo)){
				return "redirect:/dev/appinfolist";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "developer/appinfoadd";
	}
	/**
	 * 通过id展示app信息
	 * @param id
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/appview/{id}",method=RequestMethod.GET)
	public String appinfoview(@PathVariable String id,Model model,HttpServletRequest request){
		AppInfo appinfo = new AppInfo();
		ArrayList<AppVersion> appVersionList = new ArrayList<AppVersion>();
		try {
			appinfo = appInfoService.getAppInfoById(Integer.parseInt(id));
			appVersionList = appVersionService.getAppVersionById(Integer.parseInt(id));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.addAttribute(appinfo);
		model.addAttribute(appVersionList);
		return "developer/appinfoview";
	}
	/**
	 * 跳转APP修改页面
	 * @param id
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/appinfomodify",method=RequestMethod.GET)
	public String appinfomodify(String id,Model model,HttpServletRequest request){
		AppInfo appinfo = new AppInfo();
		try {
			appinfo = appInfoService.getAppInfoById(Integer.parseInt(id));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.addAttribute(appinfo);
		return "developer/appinfomodify";
	}
	/**
	 * 修改APP信息方法
	 * @param appinfo
	 * @param session
	 * @param request
	 * @param attach
	 * @return
	 */
	@RequestMapping(value="/appinfomodifysave",method=RequestMethod.POST)
	public String modifyAppInfoSave(AppInfo appinfo,HttpSession session,HttpServletRequest request,
			@RequestParam(value="attach",required=false)MultipartFile attach){
		System.out.println("******************************************************************");
		DeveloperUser devUser=(DeveloperUser)session.getAttribute(Constants.DEV_USER_SESSION);
		appinfo.setDevId(devUser.getId());
		appinfo.setStatus(1);
		appinfo.setModifyBy(devUser.getModifyBy());
		appinfo.setModifyDate(new Date());
		appinfo.setUpdateDate(new Date());
		String logoLocPath = null;
		String logoPicPath = null;
		if(!attach.isEmpty()){
			String path = request.getSession().getServletContext()
					.getRealPath("statics"+File.separator+"uploadfiles");
		String oldFileName = attach.getOriginalFilename();
		String prefix=FilenameUtils.getExtension(oldFileName);
		int filesize = 50000;
		if(attach.getSize() > filesize){
			request.setAttribute("fileUploadError", " * 上传大小不得超过50KB");
			return "developer/appinfomodify";
		}else if(prefix.equalsIgnoreCase("jpg") || prefix.equalsIgnoreCase("png") 
        		|| prefix.equalsIgnoreCase("jpeg") || prefix.equalsIgnoreCase("pneg")){//上传图片格式不正确
        	String fileName = System.currentTimeMillis()+RandomUtils.nextInt(1000000)+"_Personal.jpg";  
            File targetFile = new File(path, fileName);  
            if(!targetFile.exists()){  
                targetFile.mkdirs();  
            }
            try {  
            	attach.transferTo(targetFile);  
            } catch (Exception e) {  
                e.printStackTrace();  
                request.setAttribute("fileUploadError", " * 上传失败！");
                return "developer/appinfomodify";
            }
            logoLocPath = path+File.separator+fileName;
            logoPicPath="/AppInfoSystem/statics/uploadfiles/"+fileName;
		}else{
			request.setAttribute("fileUploadError", " * 上传图片格式不正确");
			return "developer/appinfomodify";
		}
	}	
		appinfo.setLogoLocPath(logoLocPath);
		appinfo.setLogoPicPath(logoPicPath);
		try {
			if(appInfoService.modify(appinfo)){
				return "redirect:/dev/appinfolist";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "developer/appinfomodify";
	}
	/**
	 * 跳转新增版本页面
	 * @param appversion
	 * @return
	 */
	@RequestMapping(value="/appversionadd",method=RequestMethod.GET)
	public String appversionadd(AppVersion appVersion,String id,Model model){
		AppInfo appinfo = new AppInfo();
		ArrayList<AppVersion> appVersionList = new ArrayList<AppVersion>();
		try {
			appinfo = appInfoService.getAppInfoById(Integer.parseInt(id));
			appVersionList = appVersionService.getAppVersionById(Integer.parseInt(id));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		appVersion.setAppId(appinfo.getId());
		model.addAttribute("appVersion",appVersion);
		model.addAttribute(appinfo);
		model.addAttribute(appVersionList);
		return "developer/appversionadd";
	}
	/**
	 * 保存新增版本信息
	 * @param appversion
	 * @param session
	 * @param request
	 * @param appId
	 * @param attach
	 * @return
	 */
	@RequestMapping(value="/addversionsave",method=RequestMethod.POST)
	public String appversionaddsave(AppVersion appversion,HttpSession session,HttpServletRequest request,
			String appId,@RequestParam(value="a_downloadLink",required=false)MultipartFile attach){
		DeveloperUser devUser=(DeveloperUser)session.getAttribute(Constants.DEV_USER_SESSION);
		AppInfo appinfo = new AppInfo();
		try {
			appinfo = appInfoService.getAppInfoById(Integer.parseInt(appId));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		appversion.setAppId(Integer.parseInt(appId));
		appversion.setCreatedBy(devUser.getId());
		appversion.setCreationDate(new Date());
		appversion.setModifyBy(devUser.getId());
		appversion.setModifyDate(new Date());
		String apkLocPath = null;
		String downloadLink = null;
		String APKName = null;
		if(!attach.isEmpty()){
			String path = request.getSession().getServletContext()
					.getRealPath("statics"+File.separator+"uploadAPKfiles");
		String oldFileName = attach.getOriginalFilename();
		String prefix=FilenameUtils.getExtension(oldFileName);
		
		int filesize = 500000000;
		if(attach.getSize() > filesize){
			request.setAttribute("fileUploadError", " * 上传大小不得超过500MB");
			return "developer/appversionadd";
		}else if(prefix.equalsIgnoreCase("apk")){//上传图片格式不正确
        	String fileName = appinfo.getAPKName()+"-"+appversion.getVersionNo()+prefix;  
            File targetFile = new File(path, fileName);  
            if(!targetFile.exists()){  
                targetFile.mkdirs();  
            }
            try {  
            	attach.transferTo(targetFile);  
            } catch (Exception e) {  
                e.printStackTrace();  
                request.setAttribute("fileUploadError", " * 上传失败！");
                return "developer/appversionadd";
            }
            APKName = fileName;
            apkLocPath = path+File.separator+fileName;
            downloadLink="/AppInfoSystem/statics/uploadAPKfiles/"+fileName;
		}else{
			request.setAttribute("fileUploadError", " * 上传文件格式不正确");
			return "developer/appversionadd";
		}
	}	
		appversion.setApkFileName(APKName);
		appversion.setApkLocPath(apkLocPath);
		appversion.setDownloadLink(downloadLink);
		try {
			if(appVersionService.add(appversion)){
				appinfo.setVersionId(appVersionService.getAppVersionByCon(appversion.getAppId(), appversion.getVersionNo()).getId());
				appInfoService.modify(appinfo);
				return "redirect:/dev/appinfolist";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "developer/appversionadd";
	}
	/**
	 * 跳转修改页面方法
	 * @param vid
	 * @param aid
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/appversionmodify",method=RequestMethod.GET)
	public String appversionmodify(String vid,String aid,Model model){
		AppVersion appversion = new AppVersion();
		ArrayList<AppVersion> appVersionList = new ArrayList<AppVersion>();
		try {
			appVersionList = appVersionService.getAppVersionById(Integer.parseInt(aid));
			appversion = appVersionService.getAppVersionByCom(Integer.parseInt(vid));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.addAttribute(appVersionList);
		model.addAttribute(appversion);
		return "developer/appversionmodify";
	}
	/**
	 * 修改版本保存方法
	 * @param appversion
	 * @param session
	 * @param request
	 * @param id
	 * @param attach
	 * @return
	 */
	@RequestMapping(value="/appversionmodifysave",method=RequestMethod.POST)
	public String appversionmodifysave(AppVersion appversion,HttpSession session,HttpServletRequest request,
			String appId,@RequestParam(value="attach",required=false)MultipartFile attach){
		DeveloperUser devUser=(DeveloperUser)session.getAttribute(Constants.DEV_USER_SESSION);
		AppInfo appinfo = new AppInfo();
		try {
			appinfo = appInfoService.getAppInfoById(Integer.parseInt(appId));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		appversion.setAppId(Integer.parseInt(appId));
		appversion.setModifyBy(devUser.getModifyBy());
		appversion.setModifyDate(new Date());
		String apkLocPath = null;
		String downloadLink = null;
		String APKName = null;
		if(!attach.isEmpty()){
			String path = request.getSession().getServletContext()
					.getRealPath("statics"+File.separator+"uploadAPKfiles");
		String oldFileName = attach.getOriginalFilename();
		String prefix=FilenameUtils.getExtension(oldFileName);
		
		int filesize = 500000000;
		if(attach.getSize() > filesize){
			request.setAttribute("fileUploadError", " * 上传大小不得超过500MB");
			return "developer/appversionmodify";
		}else if(prefix.equalsIgnoreCase("apk")){//上传图片格式不正确
        	String fileName = appinfo.getAPKName()+"-"+appversion.getVersionNo()+prefix;  
            File targetFile = new File(path, fileName);  
            if(!targetFile.exists()){  
                targetFile.mkdirs();  
            }
            try {  
            	attach.transferTo(targetFile);  
            } catch (Exception e) {  
                e.printStackTrace();  
                request.setAttribute("fileUploadError", " * 上传失败！");
                return "developer/appversionmodify";
            }
            APKName = fileName;
            apkLocPath = path+File.separator+fileName;
            downloadLink="/AppInfoSystem/statics/uploadAPKfiles/"+fileName;
		}else{
			request.setAttribute("fileUploadError", " * 上传文件格式不正确");
			return "developer/appversionmodify";
		}
	}	
		appversion.setApkFileName(APKName);
		appversion.setApkLocPath(apkLocPath);
		appversion.setDownloadLink(downloadLink);
		try {
			if(appVersionService.appversionmodify(appversion)){
				return "redirect:/dev/appinfolist";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "developer/appversionmodify";
	}
	/**
	 * 返回功能跳转
	 */
	@RequestMapping(value="/list")
	public String back(){
		return "redirect:/dev/appinfolist";
	}	
	/**
	 * 加载平台的异步验证
	 * @param tcode
	 * @return
	 */
	@RequestMapping(value="/datadictionarylist.json")
	@ResponseBody
	public List<DataDictionary> datadictionarylist(@RequestParam String tcode){
		List<DataDictionary> listdictionary=new ArrayList<DataDictionary>();
		try {
			listdictionary = dictionaryService.dataDictionaryList(tcode);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listdictionary;
	}
	/**
	 * 删除信息的异步验证
	 * @param appinfoid
	 * @return
	 */
	@RequestMapping(value="/delapp.json")
	@ResponseBody
	public Object delappinfo(@RequestParam String id){
		HashMap<String, String> resultMap = new HashMap<String, String>();
		System.out.println("***********************************************************");
		if(StringUtils.isNullOrEmpty(id)){
			System.out.println("***********************************************************");
			resultMap.put("delResult", "notexist");
		}else{
			System.out.println("***********************************************************");
			boolean flag = true;
			try {
				if(flag){
					flag = appInfoService.delAppInfoById(Integer.parseInt(id));
					flag = false;
					resultMap.put("delResult", "true");
				}else{
					resultMap.put("delResult", "false");
				}
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return resultMap;
	}
	/**
	 * 修改方法中删除APP上传图片的异步验证
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/delfile.json",method=RequestMethod.GET)
	@ResponseBody
	public Object delfile(@RequestParam String id){
		HashMap<String, String> resultMap = new HashMap<String, String>();
		if(StringUtils.isNullOrEmpty(id)){
			resultMap.put("result", "notexist");
		}else{
			AppInfo appinfo = null;
			try {
				appinfo = appInfoService.getAppInfoById(Integer.parseInt(id));
				if(appinfo.getLogoLocPath() != null && !appinfo.getLogoLocPath().equals("")){
					File file = new File(appinfo.getLogoLocPath());
					if(file.exists()){
						file.delete();
					}
					appinfo.setLogoLocPath(null);
					appinfo.setLogoPicPath(null);
				}
				if(appInfoService.updatePath(appinfo)){
					if(appinfo.getLogoLocPath()==null && appinfo.getLogoPicPath()==null){
						resultMap.put("result", "success");
					}else{
						resultMap.put("result", "filed");
					}
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return JSONArray.toJSONString(resultMap);
	}
	/**
	 * 删除apk文件的异步验证
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/delfiles.json",method=RequestMethod.GET)
	@ResponseBody
	public Object delfiles(@RequestParam String id){
		HashMap<String, String> resultMap = new HashMap<String, String>();
		if(StringUtils.isNullOrEmpty(id)){
			resultMap.put("result", "notexist");
		}else{
			AppVersion appversion = null;
			try {
				appversion = appVersionService.getAppVersionByCom(Integer.parseInt(id));
				if(appversion.getApkLocPath() != null && !appversion.getApkLocPath().equals("")){
					File file = new File(appversion.getApkLocPath());
					if(file.exists()){
						file.delete();
					}
					appversion.setApkLocPath(null);
					appversion.setDownloadLink(null);
					appversion.setApkFileName(null);
				}
				if(appversion.getDownloadLink() == null && appversion.getApkLocPath() == null && appversion.getApkFileName() == null){
					resultMap.put("result", "success");
				}else{
					resultMap.put("result", "failed");
				}
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return JSONArray.toJSONString(resultMap);
	}
	/**
	 * 判断APKName与数据库已有APKName是否相同
	 * @param APKName
	 * @return
	 */
	@RequestMapping(value="/apkexist.json",method=RequestMethod.POST)
	@ResponseBody
	public Object APKNameIsExit(@RequestParam String APKName){
		HashMap<String, String> resultMap = new HashMap<String, String>();
		if(StringUtils.isNullOrEmpty(APKName)){
			resultMap.put("APKName", "exist");
		}else{
			AppInfo appinfo = null;
			try {
				appinfo = appInfoService.selectAppInfoAPKNameExist(APKName);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(null != appinfo){
				resultMap.put("APKName", "exist");
			}else{
				resultMap.put("APKName", "noexist");
			}
		}
		return JSONArray.toJSONString(resultMap);
	}
	/**
	 * 上下架的异步验证
	 * @param appId
	 * @return
	 */
	@RequestMapping(value="/{appId}/sale.json",method=RequestMethod.PUT)
	@ResponseBody
	public Object modifyStatus(@PathVariable String appId,HttpSession session){
		HashMap<String, String> resultMsg = new HashMap<String, String>();
		DeveloperUser devUser=(DeveloperUser)session.getAttribute(Constants.DEV_USER_SESSION);
		AppInfo appinfo = null;
		AppVersion appversion = null;
		Integer sta = null;
		boolean flag = true;
		try {
			
			appinfo = appInfoService.getAppInfoById(Integer.parseInt(appId));
			appversion = appVersionService.getAppVersionByCom(appinfo.getVersionId());
			sta = appinfo.getStatus();
			if(flag){
				if(sta==2 || sta==5){
					System.out.println("进来了吗！！！！！！！！！！");
					appinfo.setStatus(4);
					appinfo.setModifyBy(devUser.getId());
					appinfo.setModifyDate(new Date());
					appinfo.setOnSaleDate(new Date());
					System.out.println("进来了吗！！！！！！！！！！");
					appversion.setPublishStatus(2);
					System.out.println("进来了吗！！！！！！！！！！");
					flag = appInfoService.modify(appinfo);
					System.out.println("进来了吗！！！！！！！！！！");
					flag = appVersionService.appversionmodify(appversion);
					System.out.println("进来了吗！！！！！！！！！！");
					resultMsg.put("result", "success");
					flag = false;
				}else if(sta == 4){
					appinfo.setStatus(5);
					appinfo.setModifyBy(devUser.getId());
					appinfo.setModifyDate(new Date());
					appinfo.setOffSaleDate(new Date());
					appversion.setPublishStatus(1);
					flag = appInfoService.modify(appinfo);
					flag = appVersionService.appversionmodify(appversion);
					resultMsg.put("result", "success");
					System.out.println("进来了吗！！！！！！！！！！");
					flag = false;
				}else{
					
				}
			}else{
				resultMsg.put("result", "failed");
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return JSONArray.toJSONString(resultMsg);
	}
}
