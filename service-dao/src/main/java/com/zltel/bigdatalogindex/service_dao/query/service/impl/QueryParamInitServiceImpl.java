package com.zltel.bigdatalogindex.service_dao.query.service.impl;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.zltel.bigdatalogindex.common.bean.IndexTypeName;
import com.zltel.bigdatalogindex.common.elasticsearch.utils.ElasticIndexManageUtil;
import com.zltel.bigdatalogindex.service_dao.query.dao.QueryParamInitDao;
import com.zltel.bigdatalogindex.service_dao.query.service.QueryParamInitService;

public class QueryParamInitServiceImpl implements QueryParamInitService {
	private QueryParamInitDao paramInitDao;

	public Map<String, Long> querySyslogLevel_type() {

		return paramInitDao.querySyslogLevel_type();
	}

	public Map<String, Long> querySyslogLevel() {

		return this.paramInitDao.querySyslogLevel();
	}

	public final QueryParamInitDao getParamInitDao() {
		return paramInitDao;
	}

	public final void setParamInitDao(QueryParamInitDao paramInitDao) {
		this.paramInitDao = paramInitDao;
	}

	public Set<String> queryAllIndexNames() {
		Set<String> s = new TreeSet<String>();
		s.addAll(Arrays.asList(ElasticIndexManageUtil.getAllIndexNames(IndexTypeName.STORE_PREFIX)));
		return s;
	}

}
