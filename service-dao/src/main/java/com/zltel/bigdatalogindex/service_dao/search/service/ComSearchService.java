package com.zltel.bigdatalogindex.service_dao.search.service;

import java.util.List;

import org.elasticsearch.action.search.SearchRequestBuilder;

import com.zltel.bigdatalogindex.service_dao.search.bean.OrderBean;
import com.zltel.bigdatalogindex.service_dao.search.bean.PagerBean;
import com.zltel.bigdatalogindex.service_dao.search.bean.ResultBean;
import com.zltel.bigdatalogindex.service_dao.search.bean.SearchBean;
import com.zltel.bigdatalogindex.service_dao.search.bean.TimeGroupBean;

public interface ComSearchService {
	/**
	 * 按条件搜索
	 * 
	 * @param index
	 *            索引
	 * @param type
	 *            类型
	 * @param sbs
	 *            条件集合
	 * @param pb
	 *            页面控制
	 * @param markFields
	 *            要做标记的字段
	 * @param timeCountField
	 *            时间聚合字段
	 * @return 搜索结果合集
	 */
	public ResultBean searchResult(String index, String type, List<SearchBean> sbs, PagerBean pb, String markFields,
			TimeGroupBean tb, List<OrderBean> orders);

	public ResultBean searchResult(String[] indexs, String type, List<SearchBean> sbs, PagerBean pb, String markFields,
			TimeGroupBean tb, List<OrderBean> orders);

	/**
	 * 创建 ES 搜索条件
	 * 
	 * @param requestBuilder请求对象
	 * @param indexs
	 *            索引
	 * @param type
	 *            类型
	 * @param sbs
	 */
	public void createSearchRequest(SearchRequestBuilder requestBuilder, List<SearchBean> sbs);

}
