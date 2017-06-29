package cn.appsys.dao.backend;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.BackendUser;

public interface BackMapper {
	public BackendUser getBackLogin(@Param("userCode")String userCode)throws Exception;
}
