package com.zltel.bigdatalogindex.service_dao.manage.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.zltel.bigdatalogindex.common.bean.IndexTypeName;
import com.zltel.bigdatalogindex.common.elasticsearch.utils.ElasticIndexManageUtil;
import com.zltel.bigdatalogindex.service_dao.manage.IndexManageService;

public class IndexManageServiceImpl implements IndexManageService {

	public boolean delete(String index) throws Exception {

		return ElasticIndexManageUtil.deleteIndex(index);
	}

	public List<Map<String, String>> list() throws Exception {
		List<Map<String, String>> ret = new ArrayList<Map<String, String>>();
		String[] iss = ElasticIndexManageUtil.getAllIndexNames(IndexTypeName.STORE_PREFIX);
		for (String idx : iss) {
			Map<String, String> m = new LinkedHashMap<String, String>();
			m.put("name", idx);
			ret.add(m);
		}
		return ret;
	}

}
