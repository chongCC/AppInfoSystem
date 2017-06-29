package cn.appsys.dao.dictionary;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.DataDictionary;

public interface DicMapper {
	public List<DataDictionary> dataDictionaryList(@Param("typeCode")String typeCode)throws Exception;
}
