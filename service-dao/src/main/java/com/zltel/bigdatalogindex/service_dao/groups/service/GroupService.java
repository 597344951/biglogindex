package com.zltel.bigdatalogindex.service_dao.groups.service;

import java.util.List;

import com.zltel.bigdatalogindex.service_dao.groups.bean.GroupBean;
import com.zltel.bigdatalogindex.service_dao.search.bean.SearchBean;

/**
 * 聚合 服务
 * 
 * @author Wangch
 * 
 */
public interface GroupService {

	public List<Object[]> groupByTime(String[] index, String type, List<SearchBean> sbs, GroupBean gbs);

}
