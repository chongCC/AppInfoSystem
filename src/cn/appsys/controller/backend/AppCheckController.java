package cn.appsys.controller.backend;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.appsys.pojo.AppCategory;
import cn.appsys.pojo.AppInfo;
import cn.appsys.pojo.AppVersion;
import cn.appsys.pojo.BackendUser;
import cn.appsys.pojo.DataDictionary;
import cn.appsys.service.appinfo.AppInfoService;
import cn.appsys.service.appversion.AppVersionService;
import cn.appsys.service.category.CategoryService;
import cn.appsys.service.developer.DevService;
import cn.appsys.service.dictionary.DictionaryService;
import cn.appsys.tools.Constants;
import cn.appsys.tools.PageSupport;

@Controller
@RequestMapping(value="/back")
public class AppCheckController {
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
		return "backend/main";
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
	@RequestMapping(value="/applist")
	public String applist(Model model,
			@RequestParam(value="querySoftwareName",required=false) String querySoftwareName,
			@RequestParam(value="queryStatus",required=false) Integer queryStatus,
			@RequestParam(value="queryCategoryLevel1",required=false) Integer queryCategoryLevel1,
			@RequestParam(value="queryCategoryLevel2",required=false) Integer queryCategoryLevel2,
			@RequestParam(value="queryCategoryLevel3",required=false) Integer queryCategoryLevel3,
			@RequestParam(value="queryFlatformId",required=false) Integer queryFlatformId,
			@RequestParam(value="pageIndex",required=false) Integer pageIndex,HttpSession session){
		int backId=((BackendUser)session.getAttribute(Constants.USER_SESSION)).getId();
		List<AppInfo> appList=null;
		List<DataDictionary> flatFormList = null;
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
    	int totalCount = 0;
		try {
			totalCount = appInfoService.getAppCount(querySoftwareName, queryStatus, queryCategoryLevel1, queryCategoryLevel2, queryCategoryLevel3, queryFlatformId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PageSupport pages=new PageSupport();
    	pages.setCurrentPageNo(currentPageNo);
    	pages.setPageSize(pageSize);
    	pages.setTotalCount(totalCount);
    	int totalPageCount = pages.getTotalPageCount();
    	if(currentPageNo < 1){
    		currentPageNo = 1;
    	}else if(currentPageNo > totalPageCount){
    		currentPageNo = totalPageCount;
    	}
    	try {
    		flatFormList = dictionaryService.dataDictionaryList("APP_FLATFORM");
			statusList = dictionaryService.dataDictionaryList("APP_STATUS");
			CategoryLevel1List = gategoryService.categorylevellist(null);
			appList = appInfoService.appList(querySoftwareName, queryStatus, queryCategoryLevel1, queryCategoryLevel2, queryCategoryLevel3, queryFlatformId, currentPageNo, pageSize);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.addAttribute("CategoryLevel1List",CategoryLevel1List);
		model.addAttribute("flatFormList",flatFormList);
		model.addAttribute("statusList",statusList);
		model.addAttribute("appInfoList",appList);
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
		return "backend/applist";
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
	 * 跳转审核页面
	 * @param aid
	 * @param vid
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/check",method=RequestMethod.GET)
	public String checkApp(String aid,String vid,Model model){
		AppInfo appinfo = new AppInfo();
		AppVersion appversion = new AppVersion();
		try {
			appinfo = appInfoService.getAppInfoById(Integer.parseInt(aid));
			appversion = appVersionService.getAppVersionByCom(Integer.parseInt(vid));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.addAttribute(appinfo);
		model.addAttribute(appversion);
		return "backend/appcheck";
	}
	/**
	 * 审核判断
	 * @param appinfo
	 * @param status
	 * @return
	 */
	@RequestMapping(value="/checksave",method=RequestMethod.POST)
	public String checkSave(AppInfo appinfo,Integer status){
		
		if(status == 2){
			appinfo.setStatus(3);
			try {
				if(appInfoService.modify(appinfo)){
					return "redirect:/back/applist";
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(status == 3){
			appinfo.setStatus(4);
			try {
				if(appInfoService.modify(appinfo)){
					return "redirect:/back/applist";
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return "redirect:/back/applist";
	}
	/**
	 * 返回功能跳转
	 */
	@RequestMapping(value="/list")
	public String back(){
		return "redirect:/back/applist";
	}	
}
