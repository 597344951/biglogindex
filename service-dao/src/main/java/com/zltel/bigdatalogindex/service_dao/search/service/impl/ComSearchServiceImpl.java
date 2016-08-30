package com.zltel.bigdatalogindex.service_dao.search.service.impl;

import java.util.List;

import org.elasticsearch.action.search.SearchRequestBuilder;

import com.zltel.bigdatalogindex.service_dao.search.bean.OrderBean;
import com.zltel.bigdatalogindex.service_dao.search.bean.PagerBean;
import com.zltel.bigdatalogindex.service_dao.search.bean.ResultBean;
import com.zltel.bigdatalogindex.service_dao.search.bean.SearchBean;
import com.zltel.bigdatalogindex.service_dao.search.bean.TimeGroupBean;
import com.zltel.bigdatalogindex.service_dao.search.dao.ComSearchDao;
import com.zltel.bigdatalogindex.service_dao.search.service.ComSearchService;

public class ComSearchServiceImpl implements ComSearchService {
	private ComSearchDao comSearchDao;

	public final ComSearchDao getComSearchDao() {
		return comSearchDao;
	}

	public final void setComSearchDao(ComSearchDao comSearchDao) {
		this.comSearchDao = comSearchDao;
	}

	public ResultBean searchResult(String index, String type, List<SearchBean> sbs, PagerBean pb, String markFields,
			TimeGroupBean tb, List<OrderBean> orders) {

		return comSearchDao.searchResult(new String[] { index }, type, sbs, pb, markFields, tb, orders);
	}

	public ResultBean searchResult(String[] indexs, String type, List<SearchBean> sbs, PagerBean pb, String markFields,
			TimeGroupBean tb, List<OrderBean> orders) {
		return comSearchDao.searchResult(indexs, type, sbs, pb, markFields, tb, orders);
	}

	public void createSearchRequest(SearchRequestBuilder requestBuilder, List<SearchBean> sbs) {
		this.comSearchDao.createSearchRequest(requestBuilder, sbs);
	}
}
