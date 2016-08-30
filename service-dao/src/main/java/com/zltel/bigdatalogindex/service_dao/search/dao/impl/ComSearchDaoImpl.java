package com.zltel.bigdatalogindex.service_dao.search.dao.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.HistogramBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.InternalHistogram;
import org.elasticsearch.search.sort.SortBuilders;

import com.zltel.bigdatalogindex.common.elasticsearch.bean.ESClient;
import com.zltel.bigdatalogindex.common.elasticsearch.utils.ElasticClientPool;
import com.zltel.bigdatalogindex.common.elasticsearch.utils.ElasticClientUtil;
import com.zltel.bigdatalogindex.common.elasticsearch.utils.ElasticSearchUtil;
import com.zltel.bigdatalogindex.service_dao.search.bean.OrderBean;
import com.zltel.bigdatalogindex.service_dao.search.bean.PagerBean;
import com.zltel.bigdatalogindex.service_dao.search.bean.ResultBean;
import com.zltel.bigdatalogindex.service_dao.search.bean.SearchBean;
import com.zltel.bigdatalogindex.service_dao.search.bean.TimeGroupBean;
import com.zltel.bigdatalogindex.service_dao.search.dao.ComSearchDao;
import com.zltel.bigdatalogindex.service_dao.utils.SearchParamsCreateUtil;
import com.zltel.common.utils.string.StringUtil;

public class ComSearchDaoImpl implements ComSearchDao {
	private static final String startTag = "<keyword>";
	private static final String endTag = "</keyword>";

	public ResultBean searchResult(String[] index, String type, List<SearchBean> sbs, PagerBean pb, String markFields,
			TimeGroupBean tb, List<OrderBean> orders) {
		ResultBean rb = new ResultBean();
		ESClient _esClient = null;
		long timestart = System.currentTimeMillis();
		try {
			_esClient = ElasticClientPool.getClient();
			SearchRequestBuilder searchRequestBuilder = ElasticSearchUtil
					.createSearchReqBuilder(_esClient, index, type);
			// 统计信息
			if (null != tb) {
				// 统计整个时间段内
				DateHistogramBuilder dtb = AggregationBuilders.dateHistogram("_timeGroup")// 增加聚合查询名称,后续根据此定义名称取出聚合
						.field(tb.getField())// 聚合字段
						.interval(tb.getInterval())// 聚合颗粒度,可根据目标选择时间颗粒度
						.minDocCount(1);// .timeZone(IndexTypeName.Time_Zone);
				// .extendedBounds(min, max);//设置时间范围
				if (StringUtil.isNotNullAndEmpty(tb.getFormat())) {
					dtb.format(tb.getFormat());// 格式化时间字符串
				}
				searchRequestBuilder.addAggregation(dtb);
				// 按 条件统计
				HistogramBuilder hb = AggregationBuilders.histogram("_timeGroup").field(tb.getField()).minDocCount(1)
						.interval(1000 * 3600 * 24);
				// searchRequestBuilder.addAggregation(hb);
			}

			// 排序
			if (null != orders) {
				for (OrderBean order : orders) {
					// SortBuilders.fieldSort(order.getField()).order(order.getOrderType())
					searchRequestBuilder.addSort(SortBuilders.fieldSort(order.getField()).order(order.getOrderType()));
				}
			}

			this.createSearchRequest(searchRequestBuilder, sbs);

			searchRequestBuilder.setExplain(true);
			if (StringUtil.isNotNullAndEmpty(markFields)) {
				ElasticSearchUtil.createHighlightedField(searchRequestBuilder, markFields, startTag, endTag);
			}
			if (pb == null) {
				pb = new PagerBean();
			}
			searchRequestBuilder.setFrom(pb.getStart()).setSize(pb.getPageSize());

			SearchResponse response = searchRequestBuilder.execute().actionGet();
			SearchHits shits = response.getHits();
			pb.setTotalRecords(shits.getTotalHits());// 总条数
			SearchHit[] hits = shits.getHits();
			rb.setCount(hits.length);
			List<Map> values = new ArrayList<Map>(hits.length);
			List<Map> tags = new ArrayList<Map>(hits.length);

			for (SearchHit hit : hits) {
				// 获取结果
				if (StringUtil.isNotNullAndEmpty(markFields)) {
					Map<String, String> tg = ElasticSearchUtil.readAllHighlighted(hit);
					tags.add(tg);
				}
				Map<String, Object> v = hit.getSource();
				v.put("_id", hit.getId());
				v.put("_score", String.valueOf(hit.getScore()));
				v.put("_type", hit.getType());
				v.put("_index", hit.getIndex());
				v.put("_version", hit.getVersion());
				values.add(v);
			}
			rb.setResult(values);
			rb.setMark(tags);
			rb.setPagerBean(pb);
			// 有统计信息
			if (null != tb) {
				Aggregations aggregations = response.getAggregations();
				InternalHistogram<InternalHistogram.Bucket> time = aggregations.get("_timeGroup");// 获取聚合列表
				List<InternalHistogram.Bucket> l = time.getBuckets();
				Map<String, Long> groups = new LinkedHashMap<String, Long>();
				for (int i = 0; i < l.size(); i++) {
					InternalHistogram.Bucket o = l.get(i);
					String _t = o.getKeyAsString();// 聚合key
					long _c = o.getDocCount();// 符合条件文档数

					groups.put(_t, _c);
				}
				rb.setTimeGroup(groups);
			}
		} finally {
			ElasticClientUtil.close(_esClient);
		}
		timestart = System.currentTimeMillis() - timestart;
		rb.setCostTimeMiles(timestart);
		return rb;
	}

	public void createSearchRequest(SearchRequestBuilder requestBuilder, List<SearchBean> sbs) {
		SearchParamsCreateUtil.createSearchParams(requestBuilder, sbs, true);
	}
}
