package cn.appsys.service.dictionary;

import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import cn.appsys.dao.dictionary.DicMapper;
import cn.appsys.pojo.DataDictionary;

@Service
public class DictionaryServiceImpl implements DictionaryService{
	@Resource
	private DicMapper dicMapper;
	public List<DataDictionary> dataDictionaryList(String typeCode)
			throws Exception {
		// TODO Auto-generated method stub
		return dicMapper.dataDictionaryList(typeCode);
	}
}
