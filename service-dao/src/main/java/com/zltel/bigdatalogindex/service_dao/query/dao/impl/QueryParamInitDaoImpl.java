package com.zltel.bigdatalogindex.service_dao.query.dao.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.zltel.bigdatalogindex.common.bean.IndexTypeName;
import com.zltel.bigdatalogindex.common.elasticsearch.bean.ESClient;
import com.zltel.bigdatalogindex.common.elasticsearch.utils.ElasticClientPool;
import com.zltel.bigdatalogindex.common.elasticsearch.utils.ElasticClientUtil;
import com.zltel.bigdatalogindex.common.elasticsearch.utils.ElasticIndexManageUtil;
import com.zltel.bigdatalogindex.common.elasticsearch.utils.ElasticSearchUtil;
import com.zltel.bigdatalogindex.service_dao.query.dao.QueryParamInitDao;

public class QueryParamInitDaoImpl implements QueryParamInitDao {

	public Map<String, Long> querySyslogLevel_type() {
		ESClient esClient = null;
		try {
			esClient = ElasticClientPool.getClient();
			String type = IndexTypeName.syslog;
			String field = com.zltel.bigdatalogindex.common.bean.Syslog._level_type;// "level_type";
			String[] indexs = ElasticIndexManageUtil.getAllIndexNames(esClient, IndexTypeName.STORE_PREFIX);
			Map<String, Long> ret = new TreeMap<String, Long>();
			for (String index : indexs) {
				Map<String, Long> _m = ElasticSearchUtil.GroupBy(esClient, index, type, field);
				if (_m != null) {
					ret.putAll(_m);
				}
			}
			return ret;
		} finally {
			ElasticClientUtil.close(esClient);
		}

	}

	public Map<String, Long> querySyslogLevel() {
		ESClient esClient = null;
		try {
			esClient = ElasticClientPool.getClient();
			String type = IndexTypeName.syslog;
			String field = com.zltel.bigdatalogindex.common.bean.Syslog._level;// "level";
			String[] indexs = ElasticIndexManageUtil.getAllIndexNames(esClient);
			Map<String, Long> ret = new HashMap<String, Long>();
			for (String index : indexs) {
				Map<String, Long> _m = ElasticSearchUtil.GroupBy(esClient, index, type, field);
				if (_m != null) {
					ret.putAll(_m);
				}
			}
			return ret;
		} finally {
			ElasticClientUtil.close(esClient);
		}
	}

}
