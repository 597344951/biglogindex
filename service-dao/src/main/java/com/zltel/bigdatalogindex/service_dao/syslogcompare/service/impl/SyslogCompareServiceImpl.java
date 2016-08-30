package com.zltel.bigdatalogindex.service_dao.syslogcompare.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zltel.bigdatalogindex.common.bean.Syslog;
import com.zltel.bigdatalogindex.common.elasticsearch.utils.TypeConverToUtil;
import com.zltel.bigdatalogindex.service_dao.hostgroup.bean.HostInfo;
import com.zltel.bigdatalogindex.service_dao.search.bean.ResultBean;
import com.zltel.bigdatalogindex.service_dao.search.bean.SearchBean;
import com.zltel.bigdatalogindex.service_dao.syslogcompare.dao.SyslogCompareDao;
import com.zltel.bigdatalogindex.service_dao.syslogcompare.service.SyslogCompareService;
import com.zltel.bigdatalogindex.service_dao.syslogsearch.service.SyslogSearchServiceFacade;
import com.zltel.common.utils.date.DateUtil;
import com.zltel.common.utils.string.StringUtil;

public class SyslogCompareServiceImpl implements SyslogCompareService {
	private static final Log logout = LogFactory.getLog(SyslogCompareServiceImpl.class);
	private SyslogCompareDao syslogCompareDao;

	/**
	 * @return the syslogCompareDao
	 */
	public final SyslogCompareDao getSyslogCompareDao() {
		return syslogCompareDao;
	}

	/**
	 * @param syslogCompareDao
	 *            the syslogCompareDao to set
	 */
	public final void setSyslogCompareDao(SyslogCompareDao syslogCompareDao) {
		this.syslogCompareDao = syslogCompareDao;
	}

	public List<HostInfo> handleToNodes(String nodes, String baseTime, Integer timeRange, Integer timebn)
			throws ParseException {
		JSONArray ja = JSON.parseArray(nodes);
		JSONObject jo = null;
		List<HostInfo> hostinfos = new ArrayList<HostInfo>();
		for (int i = 0; i < ja.size(); i++) {
			jo = (JSONObject) ja.get(i);
			String ci_id = jo.getString("ci_id");
			String hostname = jo.getString("hostname");
			String ip = jo.getString("ip");
			int offset = jo.getInteger("offset");
			String startTime = baseTime;

			if ((StringUtil.isNotNullAndEmpty(ci_id) || StringUtil.isNotNullAndEmpty(ip, hostname))
					&& StringUtil.isNotNullAndEmpty(startTime)) {
				HostInfo hi = new HostInfo(ci_id, hostname, ip);
				hi.setStartEndTime(startTime, offset, timeRange, timebn);
				hostinfos.add(hi);
			} else {
				throw new RuntimeException("关键参数不能为空!");
			}
		}
		return hostinfos;
	}

	public ResultBean syslogCompare(List<HostInfo> hosts, List<SearchBean> _sbs, long groupMinute,
			final Integer timeOrder) throws Exception {
		int endStep = 1;
		long tcount = 0;
		long timeCost = System.currentTimeMillis();
		// 排序比较器
		Comparator<Long> com = new Comparator<Long>() {
			public int compare(Long o1, Long o2) {
				return timeOrder == 1 ? o1.compareTo(o2) : o2.compareTo(o1);
			}
		};
		// <偏移量,<IP,日志>>
		Map<Long, Map<String, List<Map>>> syslogmap = new TreeMap<Long, Map<String, List<Map>>>(com);
		for (HostInfo hi : hosts) {
			List<SearchBean> sbs = this.converToSearchBean(hi);
			if (_sbs != null) {
				sbs.addAll(_sbs);
			}

			ResultBean rb = SyslogSearchServiceFacade.getInstince().searchSysLog(sbs, Syslog._detail, 500);
			List<Map> result = rb.getResult();
			List<Map> marks = rb.getMark();
			if (result != null) {
				tcount += result.size();
				for (int idx = 0; idx < result.size(); idx++) {
					Map map = result.get(idx);
					if (marks != null && marks.size() > idx) {
						Map m = marks.get(idx);
						Object ds = map.get(Syslog._detail);
						Object ms = m.get(Syslog._detail);

						if (ms != null) {
							map.put(Syslog._detail, ms);
						}
					}

					String time = String.valueOf(map.get(Syslog._time));
					if (StringUtil.isNotNullAndEmpty(time)) {
						long _at = DateUtil.sdf.parse(time).getTime();
						long _st = DateUtil.sdf.parse(hi.getStartTime()).getTime();
						long step = (_at - _st) / (groupMinute * 60 * 1000);// 偏移量
						Map<String, List<Map>> ipSys = syslogmap.get(step);
						if (ipSys == null) {
							ipSys = new HashMap<String, List<Map>>();
						}
						List<Map> syss = ipSys.get(hi.getIp());
						if (syss == null) {
							syss = new ArrayList<Map>();
						}
						syss.add(map);
						ipSys.put(hi.getIp(), syss);
						syslogmap.put(step, ipSys);
					} else {
						logout.warn("查询到没有时间的数据! 请注意解析是否正确,此次结果跳过。");
					}
				}
			}
		}
		List list = new ArrayList();
		for (Map.Entry<Long, Map<String, List<Map>>> _e1 : syslogmap.entrySet()) {
			Long t = _e1.getKey();
			Map tmap = new LinkedHashMap();
			tmap.put("timeRange", t);
			List<Map> datas = new ArrayList<Map>();
			for (Map.Entry<String, List<Map>> _e2 : _e1.getValue().entrySet()) {
				String ip = _e2.getKey();
				List<Map> logs = _e2.getValue();
				Map hm = new LinkedHashMap();
				for (HostInfo hi : hosts) {
					if (TypeConverToUtil.converIpToLong(hi.getIp()).equals(TypeConverToUtil.converIpToLong(ip))) {
						hm.put("ip", ip);
						hm.put("ci_id", hi.getCi_id());
						hm.put("hostname", hi.getHostname());
						break;
					}
				}
				hm.put("syslogs", logs);
				datas.add(hm);
			}
			tmap.put("data", datas);
			list.add(tmap);
		}
		timeCost = System.currentTimeMillis() - timeCost;
		ResultBean rb = new ResultBean("成功", true, list);
		rb.setCostTimeMiles(timeCost);
		rb.setCount(tcount);
		return rb;
	}

	private List<SearchBean> converToSearchBean(HostInfo hi) throws ParseException {
		List<SearchBean> sbs = new ArrayList<SearchBean>();
		SearchBean sb = null;

		if (StringUtil.isNotNullAndEmpty(hi.getCi_id())) {
			sb = new SearchBean();
			sb.setField(Syslog._ci_id);
			sb.setSymbal(SearchBean.SYMBAL_EQUAL);
			sb.setParamType(SearchBean.PARAMTYPE_MUST);
			sb.setFrom(hi.getCi_id());
			sbs.add(sb);
		} else if (StringUtil.isNotNullAndEmpty(hi.getHostname(), hi.getIp())) {
			sb = new SearchBean();
			sb.setField(Syslog._ip);
			sb.setSymbal(SearchBean.SYMBAL_EQUAL);
			sb.setParamType(SearchBean.PARAMTYPE_MUST);
			sb.setFrom(hi.getIp());
			sbs.add(sb);
			sb = new SearchBean();
			sb.setField(Syslog._hostname);
			sb.setSymbal(SearchBean.SYMBAL_EQUAL);
			sb.setParamType(SearchBean.PARAMTYPE_MUST);
			sb.setFrom(hi.getHostname());
			sbs.add(sb);
		}

		sb = new SearchBean();
		sb.setField(Syslog._time);
		sb.setSymbal(SearchBean.SYMBAL_BETWEEN);
		sb.setParamType(SearchBean.PARAMTYPE_MUST);

		if (DateUtil.sdf.parse(hi.getStartTime()).getTime() < DateUtil.sdf.parse(hi.getEndTime()).getTime()) {
			sb.setFrom(hi.getStartTime()).setTo(hi.getEndTime());
		} else {
			sb.setFrom(hi.getEndTime()).setTo(hi.getStartTime());
		}

		sbs.add(sb);

		return sbs;
	}
}
