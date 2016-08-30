package com.zltel.bigdatalogindex.service_dao.alarmorder.service;

import java.util.List;
import java.util.Map;

import com.zltel.bigdatalogindex.service_dao.search.bean.PagerBean;
import com.zltel.bigdatalogindex.service_dao.search.bean.ResultBean;
import com.zltel.bigdatalogindex.service_dao.search.bean.SearchBean;

public interface AlarmOrderService {

	public ResultBean searchResult(List<SearchBean> sbs, PagerBean pb);

	public Map load(String index, String id);

	/**
	 * 加载 相关日志
	 * 
	 * @param host
	 * @param time
	 * @return
	 * @throws Exception
	 */
	public ResultBean relateSyslogs(String host, Long time, String searchParam) throws Exception;

	/**
	 * 相关分析
	 * 
	 * @param host
	 * @param time
	 * @param step
	 * @return
	 * @throws Exception
	 */
	public Map relatedAnalyse(String host, Long time, Integer step, String searchParam) throws Exception;

	public List<Map> queryRelatedHost(String hostMap) throws Exception;
}
