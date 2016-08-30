package com.zltel.bigdatalogindex.service_dao.syslogcompare.service;

import java.text.ParseException;
import java.util.List;

import com.zltel.bigdatalogindex.service_dao.hostgroup.bean.HostInfo;
import com.zltel.bigdatalogindex.service_dao.search.bean.ResultBean;
import com.zltel.bigdatalogindex.service_dao.search.bean.SearchBean;

public interface SyslogCompareService {

	/**
	 * 转换 json 为查询信息
	 * 
	 * @param nodes
	 *            节点信息字符串
	 * @param startStep
	 *            开始偏移位置
	 * @param limitStep
	 * @return
	 * @throws ParseException
	 */
	List<HostInfo> handleToNodes(String nodes, String baseTime, Integer timeRange, Integer timebn)
			throws ParseException;

	/**
	 * 比较 相关 hosts 的日志，并聚合
	 * 
	 * @param hosts
	 * @param limitStep
	 * @return
	 * @throws Exception
	 */
	ResultBean syslogCompare(List<HostInfo> hosts, List<SearchBean> sbs, long groupMinute, Integer timeOrder)
			throws Exception;

}
