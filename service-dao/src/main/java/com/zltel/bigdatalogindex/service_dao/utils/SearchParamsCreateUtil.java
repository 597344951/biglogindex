package com.zltel.bigdatalogindex.service_dao.utils;

import java.util.List;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import com.zltel.bigdatalogindex.service_dao.search.bean.SearchBean;
import com.zltel.common.utils.string.StringUtil;

/**
 * 创建 搜索参数 工具
 * 
 * @author Wangch
 * 
 */
public class SearchParamsCreateUtil {
	public static final int SLOT = 50;

	/**
	 * 创建搜索参数
	 * 
	 * @param requestBuilder
	 * @param sbs
	 * @param iscount
	 *            是否是统计信息
	 *            (因为Filter统计信息不准确,但是查询性能更高，因此如果统计设置true，查询设置为false),true:
	 *            是，false:查询信息
	 */
	public static void createSearchParams(SearchRequestBuilder requestBuilder, List<SearchBean> sbs, boolean iscount) {
		if (sbs == null || sbs.size() == 0) {
			return;
		}
		BoolQueryBuilder bqb = QueryBuilders.boolQuery();
		BoolQueryBuilder bqFilter = iscount ? bqb : QueryBuilders.boolQuery();// QueryBuilders.boolQuery();
		for (SearchBean sb : sbs) {
			int symbal = sb.getSymbal();
			if (symbal >= 5) {// 范围
				org.elasticsearch.index.query.RangeQueryBuilder rqb = QueryBuilders.rangeQuery(sb.getField());
				if (SearchBean.SYMBAL_BETWEEN == symbal) {
					rqb.from(sb.getFrom()).to(sb.getTo());
				}
				String _inkey = null;
				if (StringUtil.isNotNullAndEmpty(sb.getFrom())) {
					_inkey = sb.getFrom();
				} else if (StringUtil.isNotNullAndEmpty(sb.getTo())) {
					_inkey = sb.getTo();
				}
				if (StringUtil.isNullOrEmpty(_inkey)) {
					continue;
				}

				if (SearchBean.SYMBAL_GT == symbal) {
					rqb.gt(_inkey);
				}
				if (SearchBean.SYMBAL_GTE == symbal) {
					rqb.gte(_inkey);
				}
				if (SearchBean.SYMBAL_LT == symbal) {
					rqb.lt(_inkey);
				}
				if (SearchBean.SYMBAL_LTE == symbal) {
					rqb.lte(_inkey);
				}

				if (sb.getParamType() == SearchBean.PARAMTYPE_MUST) {
					bqFilter.must(rqb);
				} else if (sb.getParamType() == SearchBean.PARAMTYPE_MUST_NOT) {
					bqFilter.mustNot(rqb);
				} else if (sb.getParamType() == SearchBean.PARAMTYPE_SHOULD) {
					bqFilter.should(rqb);
				}
			} else {
				// 等于
				QueryBuilder qb = null;
				if (SearchBean.SYMBAL_CONTAINS == symbal) {
					if (sb.getKeys() != null && !sb.getKeys().isEmpty()) {
						qb = QueryBuilders.termsQuery(sb.getField(), sb.getKeys());
					}
				} else {
					if (StringUtil.isNotNullAndEmpty(sb.getFrom())) {
						qb = QueryBuilders.matchQuery(sb.getField(), sb.getFrom()).slop(SLOT).minimumShouldMatch("75%");
					} else if (StringUtil.isNotNullAndEmpty(sb.getTo())) {
						qb = QueryBuilders.matchQuery(sb.getField(), sb.getTo()).slop(SLOT).minimumShouldMatch("75%");// .cutoffFrequency(0.7f).;
					}
				}

				if (qb != null) {
					if (sb.getParamType() == SearchBean.PARAMTYPE_MUST) {
						bqb.must(qb);
					} else if (sb.getParamType() == SearchBean.PARAMTYPE_MUST_NOT) {
						bqb.mustNot(qb);
					} else if (sb.getParamType() == SearchBean.PARAMTYPE_SHOULD) {
						bqb.should(qb);
					}
				}
			}
		}
		requestBuilder.setPostFilter(bqFilter).setQuery(bqb);
	}
}
