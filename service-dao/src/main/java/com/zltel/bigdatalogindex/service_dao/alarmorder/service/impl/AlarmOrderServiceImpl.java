package com.zltel.bigdatalogindex.service_dao.alarmorder.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zltel.bigdatalogindex.common.bean.Alarmorder;
import com.zltel.bigdatalogindex.common.bean.IndexTypeName;
import com.zltel.bigdatalogindex.common.bean.Syslog;
import com.zltel.bigdatalogindex.common.elasticsearch.utils.TypeConverToUtil;
import com.zltel.bigdatalogindex.service_dao.alarmorder.dao.AlarmOrderDao;
import com.zltel.bigdatalogindex.service_dao.alarmorder.service.AlarmOrderService;
import com.zltel.bigdatalogindex.service_dao.groups.bean.GroupBean;
import com.zltel.bigdatalogindex.service_dao.groups.service.GroupServiceFacade;
import com.zltel.bigdatalogindex.service_dao.search.bean.PagerBean;
import com.zltel.bigdatalogindex.service_dao.search.bean.ResultBean;
import com.zltel.bigdatalogindex.service_dao.search.bean.SearchBean;
import com.zltel.bigdatalogindex.service_dao.syslogsearch.service.SyslogSearchService;
import com.zltel.bigdatalogindex.service_dao.syslogsearch.service.SyslogSearchServiceFacade;
import com.zltel.common.utils.date.Date;
import com.zltel.common.utils.string.StringUtil;

public class AlarmOrderServiceImpl implements AlarmOrderService {
	private AlarmOrderDao alarmOrderDao;

	/**
	 * @return the alarmOrderDao
	 */
	public final AlarmOrderDao getAlarmOrderDao() {
		return alarmOrderDao;
	}

	/**
	 * @param alarmOrderDao
	 *            the alarmOrderDao to set
	 */
	public final void setAlarmOrderDao(AlarmOrderDao alarmOrderDao) {
		this.alarmOrderDao = alarmOrderDao;
	}

	public ResultBean searchResult(List<SearchBean> sbs, PagerBean pb) {
		return this.alarmOrderDao.searchResult(sbs, pb);
	}

	public Map load(String indexs, String id) {
		return this.alarmOrderDao.load(indexs, id);
	}

	public ResultBean relateSyslogs(String host, Long time, String searchParam) throws Exception {
		List<SearchBean> sbs = new ArrayList<SearchBean>();
		sbs.addAll(this.converHostToSearchBean(host));
		sbs.add(this.converLongToSearchBean(time, 60000l));
		SyslogSearchService syslogService = SyslogSearchServiceFacade.getInstince();
		if (StringUtil.isNotNullAndEmpty(searchParam)) {
			List<SearchBean> _l = syslogService.handleSearchJsonToSB(searchParam);
			if (_l != null && _l.size() > 0) {
				sbs.addAll(_l);
			}
		}
		return syslogService.searchSysLog(sbs, Syslog._detail, 500);
	}

	private SearchBean converLongToSearchBean(Long time, Long step) {
		SearchBean sb = new SearchBean(Alarmorder._time, SearchBean.PARAMTYPE_MUST, SearchBean.SYMBAL_BETWEEN).setFrom(
				new Date(time).toDateTime()).setTo(new Date(time + step).toDateTime());
		return sb;
	}

	private SearchBean converTimeRangeToSearchBean(Long time, Integer range) {
		SearchBean sb = new SearchBean(Alarmorder._time, SearchBean.PARAMTYPE_MUST, SearchBean.SYMBAL_BETWEEN);

		sb.setFrom(new Date(time - range * 60000).toDateTime()).setTo(new Date(time + range * 60000).toDateTime());
		return sb;
	}

	private List<SearchBean> converHostToSearchBean(String host) {
		if (StringUtil.isNotNullAndEmpty(host)) {
			List<SearchBean> sbs = new ArrayList<SearchBean>();
			JSONObject jo = JSON.parseObject(host);
			String ip = jo.getString(Alarmorder._ip);
			String hostname = jo.getString(Alarmorder._hostname);
			String ci_id = jo.getString(Alarmorder._ci_id);

			if (StringUtil.isNotNullAndEmpty(ci_id)) {
				sbs.add(new SearchBean(Alarmorder._ci_id, SearchBean.PARAMTYPE_MUST, SearchBean.SYMBAL_EQUAL)
						.setFrom(ci_id));
			} else if (StringUtil.isNotNullAndEmpty(ip, hostname)) {
				ip = TypeConverToUtil.converIpToLong(ip);
				sbs.add(new SearchBean(Alarmorder._ip, SearchBean.PARAMTYPE_MUST, SearchBean.SYMBAL_EQUAL).setFrom(ip));
				sbs.add(new SearchBean(Alarmorder._hostname, SearchBean.PARAMTYPE_MUST, SearchBean.SYMBAL_EQUAL)
						.setFrom(hostname));
			} else {
				throw new RuntimeException("查询 host信息不全!");
			}

			return sbs;
		} else {
			throw new RuntimeException("查询 host信息不全!");
		}
	}

	public Map relatedAnalyse(String host, Long time, Integer step, String searchParam) throws Exception {
		SyslogSearchService syslogService = SyslogSearchServiceFacade.getInstince();
		SearchBean timeSB = this.converTimeRangeToSearchBean(time, step);

		Map ret = new LinkedHashMap();
		List<SearchBean> searchSBS = null;
		if (StringUtil.isNotNullAndEmpty(searchParam)) {
			searchSBS = syslogService.handleSearchJsonToSB(searchParam);
		}
		Map tm = new LinkedHashMap();
		tm.put("start", timeSB.getFrom());
		tm.put("end", timeSB.getTo());

		ret.put("time", tm);
		ret.put("host", JSON.parse(host));

		GroupBean gbs = new GroupBean();
		gbs.setMinCount(0);
		gbs.setGroupId("_timegroup");
		gbs.setField(Syslog._time);
		gbs.setInterval(GroupBean.MINUTE);
		Set<String> iset = IndexTypeName.indexNames(time, 1);
		String[] indexs = iset.toArray(new String[iset.size()]);
		List<SearchBean> sbs = this.converHostToSearchBean(host);
		sbs.add(timeSB);
		if (searchSBS != null && searchSBS.size() > 0) {
			sbs.addAll(searchSBS);
		}
		List<Object[]> datas = GroupServiceFacade.getInstince().groupByTime(indexs, IndexTypeName.syslog, sbs, gbs);
		List<Map> ds = new ArrayList<Map>();
		for (Object[] d : datas) {
			Map m = new LinkedHashMap();
			m.put("time", d[0]);
			m.put("count", d[1]);
			ds.add(m);
		}
		ret.put("datas", ds);
		return ret;
	}

	/**
	 * 查询相关的 host
	 * 
	 * @param host
	 * @return
	 */
	private List<Map> queryRelateHosts(String host) {

		return testRelateHosts(host);
	}

	private List<Map> testRelateHosts(String host) {
		List<Map> list = new ArrayList<Map>();
		JSONObject jo = JSON.parseObject(host);
		String ci_id = jo.getString(Alarmorder._ci_id);
		String n = StringUtil.regexMatchGroups(ci_id, "\\d+").get(0).get("0");
		Random r = new Random(System.currentTimeMillis());
		int idx = Integer.valueOf(n);
		int step = 2;
		int v = 0;
		int count = 28;
		for (int i = 25; i < count; i++) {
			v = i;

			Map<String, String> m = new HashMap<String, String>();
			m.put(Alarmorder._ip, "192.168.1.1" + v);
			m.put(Alarmorder._ci_id, "CONFIGNUM" + v);
			m.put(Alarmorder._hostname, "host" + v);

			list.add(m);
		}
		return list;
	}

	public List<Map> queryRelatedHost(String hostMap) throws Exception {

		return testRelateHosts(hostMap);
	}

}
