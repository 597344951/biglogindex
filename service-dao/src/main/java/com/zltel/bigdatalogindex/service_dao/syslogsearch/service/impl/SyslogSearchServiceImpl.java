package com.zltel.bigdatalogindex.service_dao.syslogsearch.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zltel.bigdatalogindex.common.bean.IndexTypeName;
import com.zltel.bigdatalogindex.common.bean.Syslog;
import com.zltel.bigdatalogindex.common.elasticsearch.utils.ElasticIndexManageUtil;
import com.zltel.bigdatalogindex.common.elasticsearch.utils.TypeConverToUtil;
import com.zltel.bigdatalogindex.service_dao.search.bean.OrderBean;
import com.zltel.bigdatalogindex.service_dao.search.bean.PagerBean;
import com.zltel.bigdatalogindex.service_dao.search.bean.ResultBean;
import com.zltel.bigdatalogindex.service_dao.search.bean.SearchBean;
import com.zltel.bigdatalogindex.service_dao.search.bean.TimeGroupBean;
import com.zltel.bigdatalogindex.service_dao.syslogsearch.dao.SyslogSearchDao;
import com.zltel.bigdatalogindex.service_dao.syslogsearch.service.SyslogSearchService;
import com.zltel.common.utils.date.Date;
import com.zltel.common.utils.date.DateUtil;
import com.zltel.common.utils.string.StringUtil;
import com.zltel.common.utils.time.DateTimeUtil;

public class SyslogSearchServiceImpl implements SyslogSearchService {

	private SyslogSearchDao syslogSearchDao;

	public final SyslogSearchDao getSyslogSearchDao() {
		return syslogSearchDao;
	}

	public final void setSyslogSearchDao(SyslogSearchDao syslogSearchDao) {
		this.syslogSearchDao = syslogSearchDao;
	}

	/**
	 * 计算 时间索引
	 * 
	 * @param sbs
	 * @return
	 * @throws ParseException
	 */
	private Set<String> calcTimeIndex(List<SearchBean> sbs) throws ParseException {
		Set<String> set = null;
		for (SearchBean sb : sbs) {
			String field = sb.getField().trim().toLowerCase();
			/*
			 * 根据 查询时间， 计算 索引数，加快查询时间
			 */
			if (sb.getParamType() == SearchBean.PARAMTYPE_MUST_NOT) {
				continue;
			}
			if (Syslog._time.equals(field) || Syslog._save_time.equals(field)) {
				Set<String> _st = IndexTypeName.analyseIndexsByStartEndTime(sb.getSymbal(), sb.getFrom(), sb.getTo());
				if (_st != null) {
					if (set == null) {
						set = new TreeSet<String>();
						set.addAll(_st);
					} else {
						set.retainAll(_st);
					}
				}
			}
		}
		return set;
	}

	/**
	 * 计算 不包括的 索引
	 * 
	 * @param sbs
	 * @return
	 * @throws ParseException
	 */
	private Set<String> calcNotTimeIndex(List<SearchBean> sbs) throws ParseException {
		Set<String> set = null;
		for (SearchBean sb : sbs) {
			String field = sb.getField().trim().toLowerCase();
			/*
			 * 根据 查询时间， 计算 索引数，加快查询时间
			 */
			if (sb.getParamType() != SearchBean.PARAMTYPE_MUST_NOT) {
				continue;
			}
			if (Syslog._time.equals(field) || Syslog._save_time.equals(field)) {
				Set<String> _st = IndexTypeName.analyseIndexsByStartEndTime(sb.getSymbal(), sb.getFrom(), sb.getTo());
				if (_st != null) {
					if (set == null) {
						set = new TreeSet<String>();
						set.addAll(_st);
					} else {
						set.retainAll(_st);
					}
				}
			}
		}
		return set;
	}

	public ResultBean searchSysLog(List<SearchBean> sbs, String markFields, int max) throws Exception {
		PagerBean pb = new PagerBean();
		pb.setPageSize(max);

		return this.searchSysLog(sbs, pb, markFields, null, null);
	}

	public ResultBean searchSysLog(List<SearchBean> sbs, PagerBean pb, String markFields, TimeGroupBean tb,
			List<OrderBean> order) throws Exception {
		String[] indexs = null;
		Set<String> set = this.calcTimeIndex(sbs);
		if (set == null) {
			indexs = ElasticIndexManageUtil.getAllIndexNames(IndexTypeName.STORE_PREFIX);
			set = new TreeSet<String>(Arrays.asList(indexs));
		} else {
			if (set.size() == 0) {
				return new ResultBean("查询时间范围内没有索引,请重新设定查询时间!", false);
			}
		}

		Set<String> notdefset = this.calcNotTimeIndex(sbs);
		if (notdefset != null && notdefset.size() > 0) {
			set.removeAll(notdefset);
		}
		indexs = set.toArray(new String[set.size()]);
		return this.searchSysLog(indexs, sbs, pb, markFields, tb, order);
	}

	public ResultBean searchSysLog(String defaultTime, List<SearchBean> sbs, PagerBean pb, String markFields,
			TimeGroupBean tb, List<OrderBean> orders) throws Exception {
		Set<String> tiset = this.calcTimeIndex(sbs);
		if (tiset == null) {
			String[] indexs = ElasticIndexManageUtil.getAllIndexNames(IndexTypeName.STORE_PREFIX);
			tiset = new TreeSet<String>(Arrays.asList(indexs));
		} else {
			if (tiset.size() == 0) {
				return new ResultBean("查询时间范围内没有索引,请重新设定查询时间!", false);
			}
		}
		Set<String> defSet = null;
		String startTime = null;
		String endTime = new Date().toDateTime();
		DateTimeUtil dtu = DateTimeUtil.getInstince();
		if (!"all".equals(defaultTime) && null != defaultTime) {

			if ("day".equals(defaultTime)) {
				String i = IndexTypeName.indexName(endTime);
				if (StringUtil.isNotNullAndEmpty(i)) {
					defSet = new TreeSet<String>();
					defSet.add(i);
				}
			} else {
				if ("week".equals(defaultTime)) {
					dtu.setDayOfNow(-7);
					startTime = dtu.to24DateTimeStr();
				} else if ("month".equals(defaultTime)) {
					dtu.setMonthOfNow(-1);
					startTime = dtu.to24DateTimeStr();
				}
				defSet = IndexTypeName.analyseIndexsByStartEndTime(0, startTime, endTime);
			}
		}
		if (defSet != null) {
			tiset.retainAll(defSet);
		}
		if (tiset.size() == 0) {
			return new ResultBean("此时间范围内没有数据,请重新设置时间!", false);
		}
		Set<String> notdefset = this.calcNotTimeIndex(sbs);
		if (notdefset != null && notdefset.size() > 0) {
			tiset.removeAll(notdefset);
		}

		String[] indexs = tiset.toArray(new String[tiset.size()]);
		return this.searchSysLog(indexs, sbs, pb, markFields, tb, orders);
	}

	public ResultBean searchSysLog(String[] index, List<SearchBean> sbs, PagerBean pb, String markFields,
			TimeGroupBean tb, List<OrderBean> orders) throws Exception {
		String type = IndexTypeName.syslog;

		ResultBean rb = this.syslogSearchDao.searchSysLog(index, type, sbs, pb, markFields, tb, orders);
		List<Map> results = rb.getResult();
		for (Map _m : results) {
			// 转换相应数据
			String key = Syslog._save_time;
			String value = null;

			value = String.valueOf(_m.get(key));
			if (StringUtil.isNotNullAndEmpty(value) && !"null".equals(value)) {
				// _m.put(key, TypeConverToUtil.formatNormalDateStr(value));
				_m.put(key + "_long", Date.sdf.parse(value).getTime());
			}
			key = Syslog._time;
			value = String.valueOf(_m.get(key));
			if (StringUtil.isNotNullAndEmpty(value) && !"null".equals(value)) {
				// _m.put(key, TypeConverToUtil.formatNormalDateStr(value));
				_m.put(key + "_long", Date.sdf.parse(value).getTime());
			}
			key = Syslog._ip;
			value = String.valueOf(_m.get(key));
			if (StringUtil.isNotNullAndEmpty(value) && !"null".equals(value)) {
				_m.put(key, TypeConverToUtil.converLongToIP(value));
				_m.put(key + "_long", value);
			}
		}
		return rb;
	}

	public List<SearchBean> handleSearchJsonToSB(String json) throws ParseException {
		List<SearchBean> sbs = new ArrayList<SearchBean>();
		JSONArray jsonarray = JSON.parseArray(json);
		for (int i = 0; i < jsonarray.size(); i++) {
			JSONObject o = (JSONObject) jsonarray.get(i);
			String name = o.getString("name");
			String bool = o.getString("bool");
			String symbal = o.getString("symbal");
			String value = o.getString("value");
			String from = o.getString("from");
			String to = o.getString("to");
			String initType = o.getString("initType");
			if (StringUtil.isNotNullAndEmpty(initType)) {
				if ("ip".equals(initType)) {
					if (StringUtil.isNotNullAndEmpty(value)) {
						value = TypeConverToUtil.converIpToLong(value);
					}
					if (StringUtil.isNotNullAndEmpty(from)) {
						value = TypeConverToUtil.converIpToLong(from);
					}
					if (StringUtil.isNotNullAndEmpty(to)) {
						value = TypeConverToUtil.converIpToLong(to);
					}
				} else if ("datetime".equals(initType)) {
					if (StringUtil.isNotNullAndEmpty(value)) {
						value = String.valueOf(DateUtil.converToDate(DateUtil.sdf, value).toDateTime());
					}
					if (StringUtil.isNotNullAndEmpty(from)) {
						from = String.valueOf(DateUtil.converToDate(DateUtil.sdf, from).toDateTime());
					}
					if (StringUtil.isNotNullAndEmpty(to)) {
						to = String.valueOf(DateUtil.converToDate(DateUtil.sdf, to).toDateTime());
					}
				}
			}

			SearchBean sb = new SearchBean();
			sb.setField(name);
			sb.setParamType(bool);
			sb.setSymbal(symbal, from, to, value);

			sbs.add(sb);
		}
		return sbs;
	}

	public List<OrderBean> handleOrder(String order) {
		if (StringUtil.isNotNullAndEmpty(order)) {
			List<OrderBean> orders = new ArrayList<OrderBean>();
			JSONArray ja = JSON.parseArray(order);
			for (int i = 0; i < ja.size(); i++) {
				JSONObject jo = (JSONObject) ja.get(i);
				if (jo != null) {
					String field = jo.getString("field");
					boolean t = jo.getBoolean("orderType");
					if (StringUtil.isNotNullAndEmpty(field)) {
						orders.add(new OrderBean(field, t ? OrderBean.ORDER_ASC : OrderBean.ORDER_DESC));
					}
				}
			}
			return orders;
		}
		return null;
	}

	public ResultBean syslogCountReport(String defaultTime, List<SearchBean> sbs) throws Exception {
		Set<String> tiset = this.analyseIndex(defaultTime, sbs);
		if (tiset == null) {
			return new ResultBean("此时间范围内没有数据,请重新设置时间!", false);
		}
		String[] indexs = tiset.toArray(new String[tiset.size()]);
		return new ResultBean("查询成功", true, this.syslogSearchDao.syslogCountReport(indexs, sbs));
	}

	private Set<String> analyseIndex(String defaultTime, List<SearchBean> sbs) throws ParseException {
		Set<String> tiset = this.calcTimeIndex(sbs);
		if (tiset == null) {
			String[] indexs = ElasticIndexManageUtil.getAllIndexNames(IndexTypeName.STORE_PREFIX);
			tiset = new TreeSet<String>(Arrays.asList(indexs));
		} else {
			if (tiset.size() == 0) {
				return null;
				// throw new RuntimeException("查询时间范围内没有索引,请重新设定查询时间!");
			}
		}
		Set<String> defSet = null;
		String startTime = null;
		String endTime = new Date().toDateTime();
		DateTimeUtil dtu = DateTimeUtil.getInstince();
		if (!"all".equals(defaultTime) && null != defaultTime) {

			if ("day".equals(defaultTime)) {
				String i = IndexTypeName.indexName(endTime);
				if (StringUtil.isNotNullAndEmpty(i)) {
					defSet = new TreeSet<String>();
					defSet.add(i);
				}
			} else {
				if ("week".equals(defaultTime)) {
					dtu.setDayOfNow(-7);
					startTime = dtu.to24DateTimeStr();
				} else if ("month".equals(defaultTime)) {
					dtu.setMonthOfNow(-1);
					startTime = dtu.to24DateTimeStr();
				}
				defSet = IndexTypeName.analyseIndexsByStartEndTime(0, startTime, endTime);
			}
		}
		if (defSet != null) {
			tiset.retainAll(defSet);
		}
		if (tiset.size() == 0) {
			return null;
			// throw new RuntimeException("此时间范围内没有数据,请重新设置时间!");
		}
		Set<String> notdefset = this.calcNotTimeIndex(sbs);
		if (notdefset != null && notdefset.size() > 0) {
			tiset.removeAll(notdefset);
		}
		return tiset;
	}

	public ResultBean syslogLevelReport(String defaultTime, List<SearchBean> sbs) throws Exception {
		Set<String> tiset = this.analyseIndex(defaultTime, sbs);
		if (tiset == null) {
			return new ResultBean("此时间范围内没有数据,请重新设置时间!", false);
		}
		String[] indexs = tiset.toArray(new String[tiset.size()]);
		return new ResultBean("查询成功", true, this.syslogSearchDao.syslogLevelReport(indexs, sbs));
	}

	public ResultBean syslogTopNReport(String defaultTime, List<SearchBean> sbs, int topN) throws Exception {
		Set<String> tiset = this.analyseIndex(defaultTime, sbs);
		if (tiset == null) {
			return new ResultBean("此时间范围内没有数据,请重新设置时间!", false);
		}
		String[] indexs = tiset.toArray(new String[tiset.size()]);
		Map<String, Long> maps = this.syslogSearchDao.syslogTopNReport(indexs, sbs);
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for (Map.Entry<String, Long> entry : maps.entrySet()) {
			String k = entry.getKey();
			Long v = entry.getValue();
			Map<String, String> m = new HashMap<String, String>();
			m.put("field", TypeConverToUtil.converLongToIP(k));
			m.put("count", String.valueOf(v));
			list.add(m);
		}
		Collections.sort(list, new Comparator<Map<String, String>>() {

			public int compare(Map<String, String> o1, Map<String, String> o2) {
				return Integer.valueOf(o2.get("count")).compareTo(Integer.valueOf(o1.get("count")));
			}
		});

		return new ResultBean("查询成功", true, list);
	}

}
