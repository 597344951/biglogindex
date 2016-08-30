package com.zltel.bigdatalogindex.service_dao.alarmorder.dao;

import java.util.List;
import java.util.Map;

import com.zltel.bigdatalogindex.service_dao.search.bean.PagerBean;
import com.zltel.bigdatalogindex.service_dao.search.bean.ResultBean;
import com.zltel.bigdatalogindex.service_dao.search.bean.SearchBean;

public interface AlarmOrderDao {
	public ResultBean searchResult(List<SearchBean> sbs, PagerBean pb);

	public Map load(String indexs, String id);
}
