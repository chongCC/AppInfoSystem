package cn.appsys.service.category;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.appsys.dao.category.CateMapper;
import cn.appsys.pojo.AppCategory;

@Service
public class CategoryServiceImpl implements CategoryService{
	@Resource
	private CateMapper cateMapper;
	public List<AppCategory> categorylevellist(Integer parentId)
			throws Exception {
		// TODO Auto-generated method stu
		return cateMapper.categorylevellist(parentId);
	}

}
