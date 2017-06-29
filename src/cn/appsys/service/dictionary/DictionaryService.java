package cn.appsys.service.dictionary;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.DataDictionary;

public interface DictionaryService {
	public List<DataDictionary> dataDictionaryList(@Param("typeCode")String typeCode)
			throws Exception;
}
