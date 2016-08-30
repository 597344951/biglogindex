package com.zltel.bigdatalogindex.service_dao.syslogsearch.service;

import java.util.List;

import com.zltel.bigdatalogindex.service_dao.search.bean.OrderBean;
import com.zltel.bigdatalogindex.service_dao.search.bean.PagerBean;
import com.zltel.bigdatalogindex.service_dao.search.bean.ResultBean;
import com.zltel.bigdatalogindex.service_dao.search.bean.SearchBean;
import com.zltel.bigdatalogindex.service_dao.search.bean.TimeGroupBean;

public interface SyslogSearchService {
	/**
	 * 全局事件搜索日志
	 * 
	 * @param sbs
	 * @param max
	 *            最大返回条数
	 * @return
	 * @throws Exception
	 */
	public ResultBean searchSysLog(List<SearchBean> sbs, String markFields, int max) throws Exception;

	/**
	 * 全局事件索引 搜索日志
	 * 
	 * @param sbs
	 *            搜索条件
	 * @param pb
	 *            分页条件
	 */
	public ResultBean searchSysLog(List<SearchBean> sbs, PagerBean pb, String markFields, TimeGroupBean tb,
			List<OrderBean> orders) throws Exception;

	/**
	 * 搜索 索引片 搜索日志
	 * 
	 * @param indexs
	 *            指定搜索的索引位置
	 * @param sbs
	 * @param pb
	 * @param markFields
	 * @param tb
	 *            时间聚合，不需要设置null
	 * @return
	 * @throws Exception
	 */
	public ResultBean searchSysLog(String[] indexs, List<SearchBean> sbs, PagerBean pb, String markFields,
			TimeGroupBean tb, List<OrderBean> orders) throws Exception;

	/**
	 * 指定 默认搜索时间片 搜索日志
	 * 
	 * @param defaultTime
	 *            默认时间片
	 * @param sbs
	 * @param pb
	 * @param markFields
	 * @param tb
	 * @param order
	 * @return
	 * @throws Exception
	 */
	public ResultBean searchSysLog(String defaultTime, List<SearchBean> sbs, PagerBean pb, String markFields,
			TimeGroupBean tb, List<OrderBean> order) throws Exception;

	/**
	 * 转换json数据为查询 searchbean
	 * 
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public List<SearchBean> handleSearchJsonToSB(String json) throws Exception;

	public List<OrderBean> handleOrder(String order);

	/**
	 * 日志来源 统计
	 * 
	 * @param defaultTime
	 *            默认时间片
	 * @param sbs
	 *            搜索条件
	 * @return
	 */
	public ResultBean syslogCountReport(String defaultTime, List<SearchBean> sbs) throws Exception;

	/**
	 * 日志等级统计
	 * 
	 * @param defaultTime
	 * @param sbs
	 * @return
	 * @throws Exception
	 */
	public ResultBean syslogLevelReport(String defaultTime, List<SearchBean> sbs) throws Exception;

	/**
	 * 日志 TopN报表
	 * 
	 * @param defaultTime
	 *            默认时间
	 * @param sbs
	 *            查询条件
	 * @return
	 * @throws Exception
	 */
	public ResultBean syslogTopNReport(String defaultTime, List<SearchBean> sbs, int topN) throws Exception;
}
