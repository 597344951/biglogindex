package com.zltel.bigdatalogindex.service_dao.groups.service.impl;

import java.util.List;

import com.zltel.bigdatalogindex.service_dao.groups.bean.GroupBean;
import com.zltel.bigdatalogindex.service_dao.groups.dao.GroupDao;
import com.zltel.bigdatalogindex.service_dao.groups.service.GroupService;
import com.zltel.bigdatalogindex.service_dao.search.bean.SearchBean;

public class GroupServiceImpl implements GroupService {

	private GroupDao groupDao;

	public final GroupDao getGroupDao() {
		return groupDao;
	}

	public final void setGroupDao(GroupDao groupDao) {
		this.groupDao = groupDao;
	}

	public List<Object[]> groupByTime(String[] indexs, String type, List<SearchBean> sbs, GroupBean gbs) {
		return this.groupDao.groupByTime(indexs, type, sbs, gbs);
	}

}
