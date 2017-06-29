package cn.appsys.dao.category;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.AppCategory;

public interface CateMapper {
	public List<AppCategory> categorylevellist(@Param("parentId")Integer parentId)throws Exception;
}
