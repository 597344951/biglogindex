package com.zltel.bigdatalogindex.service_dao.alarmorder.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;

import com.zltel.bigdatalogindex.common.bean.Alarmorder;
import com.zltel.bigdatalogindex.common.bean.IndexTypeName;
import com.zltel.bigdatalogindex.common.elasticsearch.bean.ESClient;
import com.zltel.bigdatalogindex.common.elasticsearch.utils.ElasticClientPool;
import com.zltel.bigdatalogindex.common.elasticsearch.utils.ElasticClientUtil;
import com.zltel.bigdatalogindex.common.elasticsearch.utils.ElasticIndexManageUtil;
import com.zltel.bigdatalogindex.common.elasticsearch.utils.ElasticSearchUtil;
import com.zltel.bigdatalogindex.common.elasticsearch.utils.TypeConverToUtil;
import com.zltel.bigdatalogindex.service_dao.alarmorder.dao.AlarmOrderDao;
import com.zltel.bigdatalogindex.service_dao.alarmorder.utils.TextFormatUtil;
import com.zltel.bigdatalogindex.service_dao.search.bean.PagerBean;
import com.zltel.bigdatalogindex.service_dao.search.bean.ResultBean;
import com.zltel.bigdatalogindex.service_dao.search.bean.SearchBean;
import com.zltel.bigdatalogindex.service_dao.utils.SearchParamsCreateUtil;

public class AlarmOrderDaoImpl implements AlarmOrderDao {

	public ResultBean searchResult(List<SearchBean> sbs, PagerBean pb) {
		ResultBean rb = new ResultBean();
		ESClient _esClient = null;
		long timestart = System.currentTimeMillis();
		try {
			_esClient = ElasticClientPool.getClient();
			SearchRequestBuilder searchRequestBuilder = ElasticSearchUtil.createSearchReqBuilder(_esClient,
					ElasticIndexManageUtil.getAllIndexNames(_esClient), IndexTypeName.alarmorder);

			// 排序

			searchRequestBuilder.addSort(SortBuilders.fieldSort(Alarmorder._time).order(SortOrder.DESC));
			this.createSearchRequest(searchRequestBuilder, sbs);
			searchRequestBuilder.setExplain(true);

			if (pb == null) {
				pb = new PagerBean();
			}
			searchRequestBuilder.setFrom(pb.getStart()).setSize(pb.getPageSize());

			SearchResponse response = searchRequestBuilder.execute().actionGet();
			SearchHits shits = response.getHits();
			pb.setTotalRecords(shits.getTotalHits());// 总条数
			SearchHit[] hits = shits.getHits();

			List<Map> values = new ArrayList<Map>(hits.length);

			for (SearchHit hit : hits) {
				Map<String, Object> v = hit.getSource();
				v.put("_id", hit.getId());
				v.put("_score", String.valueOf(hit.getScore()));
				v.put("_type", hit.getType());
				v.put("_index", hit.getIndex());
				v.put("_version", hit.getVersion());

				filterValue(v);

				values.add(v);
			}
			rb.setResult(values);
			rb.setPagerBean(pb);
		} finally {
			ElasticClientUtil.close(_esClient);
		}
		timestart = System.currentTimeMillis() - timestart;
		rb.setCostTimeMiles(timestart);
		return rb;
	}

	public void filterValue(Map v) {
		// ip
		Object _value = v.get(Alarmorder._ip);
		if (_value != null) {
			v.put(Alarmorder._ip, TypeConverToUtil.converLongToIP(String.valueOf(_value)));
		}
		_value = v.get(Alarmorder._detail);
		if (_value != null) {
			v.put(Alarmorder._detail, TextFormatUtil.format(String.valueOf(_value)));
		}
	}

	public void createSearchRequest(SearchRequestBuilder requestBuilder, List<SearchBean> sbs) {
		SearchParamsCreateUtil.createSearchParams(requestBuilder, sbs, true);
	}

	public Map load(String index, String id) {
		ESClient _esClient = null;
		try {
			_esClient = ElasticClientPool.getClient();
			GetResponse gr = ElasticSearchUtil.get(_esClient, index, IndexTypeName.alarmorder, id);
			Map<String, Object> v = gr.getSource();
			v.put("_id", gr.getId());
			v.put("_type", gr.getType());
			v.put("_index", gr.getIndex());
			v.put("_version", gr.getVersion());

			filterValue(v);
			return v;
		} finally {
			ElasticClientUtil.close(_esClient);
		}

	}

}
