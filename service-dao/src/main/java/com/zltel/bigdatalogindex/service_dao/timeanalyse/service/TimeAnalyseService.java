package com.zltel.bigdatalogindex.service_dao.timeanalyse.service;

import java.util.List;

import com.zltel.bigdatalogindex.service_dao.search.bean.ResultBean;
import com.zltel.bigdatalogindex.service_dao.timeanalyse.bean.TimeAnalyseParam;

public interface TimeAnalyseService {
	/**
	 * 获取 指定机器,指定时间工况性能信息
	 * 
	 * @param param
	 *            查询条件
	 * @return 查询信息
	 * @throws Exception 
	 */
	public ResultBean timeAnalysePerf(TimeAnalyseParam param) throws Exception;

	/**
	 * 获取指定机器，时间 日志信息
	 * 
	 * @param param
	 *            查询条件
	 * @return 查询信息
	 */
	public ResultBean timeAnalyseSyslog(TimeAnalyseParam param) throws Exception;

	public List<Object[]> syslogGroup(TimeAnalyseParam param) throws Exception;

	public ResultBean syslogDetail(TimeAnalyseParam param) throws Exception;
}
