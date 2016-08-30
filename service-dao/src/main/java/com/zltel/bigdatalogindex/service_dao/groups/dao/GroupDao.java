package com.zltel.bigdatalogindex.service_dao.groups.dao;

import java.util.List;

import com.zltel.bigdatalogindex.service_dao.groups.bean.GroupBean;
import com.zltel.bigdatalogindex.service_dao.search.bean.SearchBean;

public interface GroupDao {

	public List<Object[]> groupByTime(String[] indexs, String type, List<SearchBean> sbs, GroupBean gb);

}
