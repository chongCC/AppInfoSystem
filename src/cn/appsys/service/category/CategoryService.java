package cn.appsys.service.category;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.AppCategory;

public interface CategoryService {
	public List<AppCategory> categorylevellist(@Param("parentId")Integer parentId)
			throws Exception;
}
