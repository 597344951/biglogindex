package com.zltel.bigdatalogindex.service_dao.search.dao;

import java.util.List;

import org.elasticsearch.action.search.SearchRequestBuilder;

import com.zltel.bigdatalogindex.service_dao.search.bean.OrderBean;
import com.zltel.bigdatalogindex.service_dao.search.bean.PagerBean;
import com.zltel.bigdatalogindex.service_dao.search.bean.ResultBean;
import com.zltel.bigdatalogindex.service_dao.search.bean.SearchBean;
import com.zltel.bigdatalogindex.service_dao.search.bean.TimeGroupBean;

public interface ComSearchDao {

	ResultBean searchResult(String[] index, String type, List<SearchBean> sbs, PagerBean pb, String markFields,
			TimeGroupBean tb, List<OrderBean> orders);

	void createSearchRequest(SearchRequestBuilder requestBuilder, List<SearchBean> sbs);

}
