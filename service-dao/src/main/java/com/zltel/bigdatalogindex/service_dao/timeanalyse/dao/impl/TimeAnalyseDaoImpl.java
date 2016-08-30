package com.zltel.bigdatalogindex.service_dao.timeanalyse.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;

import com.zltel.bigdatalogindex.common.bean.Bmc;
import com.zltel.bigdatalogindex.common.bean.IndexTypeName;
import com.zltel.bigdatalogindex.common.bean.Syslog;
import com.zltel.bigdatalogindex.common.elasticsearch.bean.ESClient;
import com.zltel.bigdatalogindex.common.elasticsearch.utils.ElasticClientPool;
import com.zltel.bigdatalogindex.common.elasticsearch.utils.ElasticClientUtil;
import com.zltel.bigdatalogindex.common.elasticsearch.utils.ElasticSearchUtil;
import com.zltel.bigdatalogindex.service_dao.search.bean.ResultBean;
import com.zltel.bigdatalogindex.service_dao.timeanalyse.bean.TimeAnalyseParam;
import com.zltel.bigdatalogindex.service_dao.timeanalyse.dao.TimeAnalyseDao;
import com.zltel.common.utils.string.StringUtil;

public class TimeAnalyseDaoImpl implements TimeAnalyseDao {
	public static final String TYPE = IndexTypeName.bmc;

	public ResultBean timeAnalysePerf(TimeAnalyseParam param) {
		String index = IndexTypeName.indexName(param.getTime());
		long timestart = System.currentTimeMillis();
		ESClient _client = null;
		ResultBean rb = new ResultBean();
		try {
			_client = ElasticClientPool.getClient();
			SearchRequestBuilder searchRequestBuilder = ElasticSearchUtil.createSearchReqBuilder(_client, index, TYPE);

			BoolQueryBuilder bqb = QueryBuilders.boolQuery();
			BoolQueryBuilder bqFilter = QueryBuilders.boolQuery();
			// bqFilter.must(QueryBuilders.rangeQuery("time").from(stime).to(etime));

			if (StringUtil.isNotNullAndEmpty(param.getIp(), param.getHostname())) {
				bqb.must(QueryBuilders.matchQuery(Bmc._ip, param.getIp())).must(
						QueryBuilders.matchQuery(Bmc._hostname, param.getHostname()));
			} else if (StringUtil.isNotNullAndEmpty(param.getCi_id())) {
				bqb.must(QueryBuilders.matchQuery(Bmc._ci_id, param.getCi_id()));
			} else {
				throw new RuntimeException("查询需要 ip+hostname 或者 ci_id");
			}
			// 排序
			searchRequestBuilder.setPostFilter(bqFilter).setQuery(bqb)
					.addSort(SortBuilders.fieldSort(Bmc._time).order(SortOrder.ASC)).setSize(2000);
			List<SearchHit> hits = ElasticSearchUtil.readAllScollData(_client, searchRequestBuilder, 60);
			List<Map> values = new ArrayList<Map>();
			for (SearchHit hit : hits) {
				values.add(hit.getSource());
			}
			rb.setResult(values);
		} finally {
			ElasticClientUtil.close(_client);
		}
		timestart = System.currentTimeMillis() - timestart;
		rb.setCostTimeMiles(timestart);
		return rb;
	}

	public ResultBean timeAnalyseSyslog(TimeAnalyseParam param) throws Exception {
		String index = IndexTypeName.indexName(param.getTime());
		String type = IndexTypeName.syslog;
		long timestart = System.currentTimeMillis();
		ESClient _client = null;
		ResultBean rb = new ResultBean();
		try {
			_client = ElasticClientPool.getClient();
			SearchRequestBuilder searchRequestBuilder = ElasticSearchUtil.createSearchReqBuilder(_client, index, type);

			BoolQueryBuilder bqb = QueryBuilders.boolQuery();
			BoolQueryBuilder bqFilter = QueryBuilders.boolQuery();
			String stime = param.getStartHour();
			String etime = param.getEndHour();

			bqFilter.must(QueryBuilders.rangeQuery(Syslog._time).from(stime).to(etime));

			if (StringUtil.isNotNullAndEmpty(param.getIp(), param.getHostname())) {
				bqb.must(QueryBuilders.matchQuery(Syslog._ip, param.getIp())).must(
						QueryBuilders.matchQuery(Syslog._hostname, param.getHostname()));
			} else if (StringUtil.isNotNullAndEmpty(param.getCi_id())) {
				bqb.must(QueryBuilders.matchQuery(Syslog._ci_id, param.getCi_id()));
			} else {
				throw new RuntimeException("查询需要 ip+hostname 或者 ci_id");
			}
			// 排序
			searchRequestBuilder.setPostFilter(bqFilter).setQuery(bqb)
					.addSort(SortBuilders.fieldSort(Syslog._time).order(SortOrder.ASC)).setSize(2000);
			List<SearchHit> hits = ElasticSearchUtil.readAllScollData(_client, searchRequestBuilder, 60);
			List<Map> values = new ArrayList<Map>();
			for (SearchHit hit : hits) {
				values.add(hit.getSource());
			}
			rb.setResult(values);
		} finally {
			ElasticClientUtil.close(_client);
		}
		timestart = System.currentTimeMillis() - timestart;
		rb.setCostTimeMiles(timestart);
		return rb;
	}
}
