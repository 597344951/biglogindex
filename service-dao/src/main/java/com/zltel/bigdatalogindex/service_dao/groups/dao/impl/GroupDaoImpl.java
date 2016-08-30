package com.zltel.bigdatalogindex.service_dao.groups.dao.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.InternalHistogram;
import org.elasticsearch.search.aggregations.bucket.histogram.InternalHistogram.Bucket;

import com.zltel.bigdatalogindex.common.elasticsearch.bean.ESClient;
import com.zltel.bigdatalogindex.common.elasticsearch.utils.ElasticClientPool;
import com.zltel.bigdatalogindex.common.elasticsearch.utils.ElasticClientUtil;
import com.zltel.bigdatalogindex.common.elasticsearch.utils.ElasticSearchUtil;
import com.zltel.bigdatalogindex.service_dao.groups.bean.GroupBean;
import com.zltel.bigdatalogindex.service_dao.groups.dao.GroupDao;
import com.zltel.bigdatalogindex.service_dao.search.bean.SearchBean;
import com.zltel.bigdatalogindex.service_dao.utils.SearchParamsCreateUtil;
import com.zltel.common.utils.string.StringUtil;

public class GroupDaoImpl implements GroupDao {

	public List<Object[]> groupByTime(String[] indexs, String type, List<SearchBean> sbs, GroupBean gb) {
		List<Object[]> ret = new ArrayList<Object[]>();
		ESClient _esClient = null;
		long timestart = System.currentTimeMillis();
		try {
			_esClient = ElasticClientPool.getClient();
			SearchRequestBuilder searchRequestBuilder = ElasticSearchUtil.createSearchReqBuilder(_esClient, indexs,
					type);
			// 统计信息
			if (null != gb) {
				// 统计整个时间段内
				DateHistogramBuilder dtb = AggregationBuilders.dateHistogram(gb.getGroupId())// 增加聚合查询名称,后续根据此定义名称取出聚合
						.field(gb.getField())// 聚合字段
						.interval(gb.getInterval())// 聚合颗粒度,可根据目标选择时间颗粒度
						.minDocCount(gb.getMinCount());// .timeZone(IndexTypeName.Time_Zone);
				if (StringUtil.isNotNullAndEmpty(gb.getFormat())) {
					dtb.format(gb.getFormat());// 格式化时间字符串
				}
				searchRequestBuilder.addAggregation(dtb);
				// 按 条件统计
			}
			SearchParamsCreateUtil.createSearchParams(searchRequestBuilder, sbs, true);
			searchRequestBuilder.setExplain(true);
			searchRequestBuilder.setFrom(0).setSize(0);// 没有查询结果

			SearchResponse response = searchRequestBuilder.execute().actionGet();
			// 有统计信息
			if (null != gb) {
				Aggregations aggregations = response.getAggregations();
				for (Map.Entry<String, Aggregation> entry : aggregations.asMap().entrySet()) {
					String key = entry.getKey();
					InternalHistogram<InternalHistogram.Bucket> time = (InternalHistogram<Bucket>) entry.getValue();
					List<InternalHistogram.Bucket> l = time.getBuckets();
					Map<String, Long> groups = new LinkedHashMap<String, Long>();
					for (int i = 0; i < l.size(); i++) {
						InternalHistogram.Bucket o = l.get(i);

						ret.add(new Object[] { o.getKeyAsString(), o.getDocCount() });
					}

				}

			}
		} finally {
			ElasticClientUtil.close(_esClient);
		}
		timestart = System.currentTimeMillis() - timestart;
		return ret;
	}
}
