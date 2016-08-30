package com.zltel.bigdatalogindex.service_dao.syslogsearch.dao;

import java.util.List;
import java.util.Map;

import com.zltel.bigdatalogindex.service_dao.search.bean.OrderBean;
import com.zltel.bigdatalogindex.service_dao.search.bean.PagerBean;
import com.zltel.bigdatalogindex.service_dao.search.bean.ResultBean;
import com.zltel.bigdatalogindex.service_dao.search.bean.SearchBean;
import com.zltel.bigdatalogindex.service_dao.search.bean.TimeGroupBean;

public interface SyslogSearchDao {

	public ResultBean searchSysLog(String[] index, String type, List<SearchBean> sbs, PagerBean pb, String markFields,
			TimeGroupBean tb, List<OrderBean> orders);

	public Map syslogCountReport(String[] indexs, List<SearchBean> sbs);

	public Map syslogLevelReport(String[] indexs, List<SearchBean> sbs);

	public Map<String, Long> syslogTopNReport(String[] indexs, List<SearchBean> sbs);
}
