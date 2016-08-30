package com.zltel.bigdatalogindex.service_dao.syslogsearch.dao.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.InternalHistogram;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;

import com.zltel.bigdatalogindex.common.bean.IndexTypeName;
import com.zltel.bigdatalogindex.common.bean.Syslog;
import com.zltel.bigdatalogindex.common.elasticsearch.bean.ESClient;
import com.zltel.bigdatalogindex.common.elasticsearch.utils.ElasticClientPool;
import com.zltel.bigdatalogindex.common.elasticsearch.utils.ElasticClientUtil;
import com.zltel.bigdatalogindex.common.elasticsearch.utils.ElasticSearchUtil;
import com.zltel.bigdatalogindex.service_dao.search.bean.OrderBean;
import com.zltel.bigdatalogindex.service_dao.search.bean.PagerBean;
import com.zltel.bigdatalogindex.service_dao.search.bean.ResultBean;
import com.zltel.bigdatalogindex.service_dao.search.bean.SearchBean;
import com.zltel.bigdatalogindex.service_dao.search.bean.TimeGroupBean;
import com.zltel.bigdatalogindex.service_dao.search.service.ComSearchServiceFacade;
import com.zltel.bigdatalogindex.service_dao.syslogsearch.dao.SyslogSearchDao;
import com.zltel.bigdatalogindex.service_dao.utils.SearchParamsCreateUtil;

public class SyslogSearchDaoImpl implements SyslogSearchDao {

	public ResultBean searchSysLog(String[] index, String type, List<SearchBean> sbs, PagerBean pb, String markFields,
			TimeGroupBean tb, List<OrderBean> orders) {
		return ComSearchServiceFacade.getInstince().searchResult(index, type, sbs, pb, markFields, tb, orders);
	}

	public Map syslogCountReport(String[] indexs, List<SearchBean> sbs) {
		ESClient _esClient = null;
		long timestart = System.currentTimeMillis();
		try {
			_esClient = ElasticClientPool.getClient();
			SearchRequestBuilder searchRequestBuilder = ElasticSearchUtil.createSearchReqBuilder(_esClient, indexs,
					IndexTypeName.syslog);
			SearchParamsCreateUtil.createSearchParams(searchRequestBuilder, sbs, true);
			searchRequestBuilder.setFrom(0).setSize(0);
			searchRequestBuilder.addAggregation(AggregationBuilders
					.dateHistogram("datetime")
					.field(Syslog._time)
					.interval(DateHistogramInterval.DAY)
					.format("yyyy-MM-dd HH:mm:ss")
					.minDocCount(0)
					.subAggregation(
							AggregationBuilders.terms("datetime_source").field(Syslog._source_target).minDocCount(0)));
			SearchResponse response = searchRequestBuilder.execute().actionGet();
			Aggregations aggregations = response.getAggregations();
			InternalHistogram<InternalHistogram.Bucket> time = aggregations.get("datetime");// 获取聚合列表
			List<InternalHistogram.Bucket> l = time.getBuckets();
			Map group = new LinkedHashMap<String, Long>();

			for (int i = 0; i < l.size(); i++) {
				InternalHistogram.Bucket o = l.get(i);
				String _t = o.getKeyAsString();// 聚合key
				long _c = o.getDocCount();// 符合条件文档数
				Map<String, Long> g = new LinkedHashMap<String, Long>();
				Terms st = o.getAggregations().get("datetime_source");// 获取 子聚合
				List<Terms.Bucket> tbs = st.getBuckets();
				for (Terms.Bucket tb : tbs) {
					g.put(tb.getKeyAsString(), tb.getDocCount());
				}
				group.put(_t, g);
			}

			return group;
		} finally {
			ElasticClientUtil.close(_esClient);
		}
	}

	public Map syslogLevelReport(String[] indexs, List<SearchBean> sbs) {
		ESClient _esClient = null;
		long timestart = System.currentTimeMillis();
		try {
			_esClient = ElasticClientPool.getClient();
			SearchRequestBuilder searchRequestBuilder = ElasticSearchUtil.createSearchReqBuilder(_esClient, indexs,
					IndexTypeName.syslog);
			SearchParamsCreateUtil.createSearchParams(searchRequestBuilder, sbs, true);
			searchRequestBuilder.setFrom(0).setSize(0);
			searchRequestBuilder.addAggregation(AggregationBuilders
					.terms("_source_target")
					.field(Syslog._source_target)
					.minDocCount(0)
					.subAggregation(
							AggregationBuilders
									.terms("_level_type")
									.field(Syslog._level_type)
									.minDocCount(0)
									.subAggregation(
											AggregationBuilders.terms("_level").field(Syslog._level).minDocCount(0))));
			SearchResponse response = searchRequestBuilder.execute().actionGet();
			Aggregations aggregations = response.getAggregations();

			Map group = new LinkedHashMap<String, Long>();
			Terms terms = aggregations.get("_source_target");
			List<Terms.Bucket> tbs = terms.getBuckets();
			for (Terms.Bucket tb : tbs) {
				// System.out.println(tb.getKeyAsString() + ":" +
				// tb.getDocCount());
				Map g1 = new LinkedHashMap<String, Long>();
				Terms ts = tb.getAggregations().get("_level_type");
				List<Terms.Bucket> _tbs1 = ts.getBuckets();
				for (Terms.Bucket _tb1 : _tbs1) {

					if (_tb1.getDocCount() <= 0)
						continue;
					// System.out.println("\t" + _tb1.getKeyAsString() + ":" +
					// _tb1.getDocCount());
					Map g2 = new TreeMap<String, Long>();
					Terms ts2 = _tb1.getAggregations().get("_level");
					List<Terms.Bucket> _tbs2 = ts2.getBuckets();
					for (Terms.Bucket _tb2 : _tbs2) {
						// System.out.println("\t\t" + _tb2.getKeyAsString() +
						// ":" + _tb2.getDocCount());
						g2.put(_tb2.getKeyAsString(), _tb2.getDocCount());
					}
					g1.put(_tb1.getKeyAsString(), g2);
				}
				group.put(tb.getKeyAsString(), g1);
			}

			return group;
		} finally {
			ElasticClientUtil.close(_esClient);
		}
	}

	public Map<String, Long> syslogTopNReport(String[] indexs, List<SearchBean> sbs) {
		ESClient _esClient = null;
		long timestart = System.currentTimeMillis();
		try {
			_esClient = ElasticClientPool.getClient();
			SearchRequestBuilder searchRequestBuilder = ElasticSearchUtil.createSearchReqBuilder(_esClient, indexs,
					IndexTypeName.syslog);
			// SearchParamsCreateUtil.createSearchParams(searchRequestBuilder,
			// sbs, true);
			searchRequestBuilder.setFrom(0).setSize(0);
			searchRequestBuilder.addAggregation(AggregationBuilders.terms("_ip").field(Syslog._ip).minDocCount(1));
			SearchResponse response = searchRequestBuilder.execute().actionGet();
			Aggregations aggregations = response.getAggregations();

			Terms terms = aggregations.get("_ip");
			List<Terms.Bucket> tbs = terms.getBuckets();
			Map<String, Long> m = new HashMap<String, Long>();
			for (Terms.Bucket tb : tbs) {
				m.put(tb.getKeyAsString(), tb.getDocCount());
				// System.out.println(tb.getKeyAsString() + ":" +
				// tb.getDocCount());
			}

			return m;
		} finally {
			ElasticClientUtil.close(_esClient);
		}
	}
}
