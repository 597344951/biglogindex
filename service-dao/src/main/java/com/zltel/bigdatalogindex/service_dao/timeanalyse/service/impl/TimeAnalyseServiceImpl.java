package com.zltel.bigdatalogindex.service_dao.timeanalyse.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.zltel.bigdatalogindex.common.bean.IndexTypeName;
import com.zltel.bigdatalogindex.common.bean.Syslog;
import com.zltel.bigdatalogindex.common.elasticsearch.utils.TypeConverToUtil;
import com.zltel.bigdatalogindex.service_dao.groups.bean.GroupBean;
import com.zltel.bigdatalogindex.service_dao.groups.service.GroupServiceFacade;
import com.zltel.bigdatalogindex.service_dao.search.bean.OrderBean;
import com.zltel.bigdatalogindex.service_dao.search.bean.PagerBean;
import com.zltel.bigdatalogindex.service_dao.search.bean.ResultBean;
import com.zltel.bigdatalogindex.service_dao.search.bean.SearchBean;
import com.zltel.bigdatalogindex.service_dao.syslogsearch.service.SyslogSearchServiceFacade;
import com.zltel.bigdatalogindex.service_dao.timeanalyse.bean.TimeAnalyseParam;
import com.zltel.bigdatalogindex.service_dao.timeanalyse.dao.TimeAnalyseDao;
import com.zltel.bigdatalogindex.service_dao.timeanalyse.service.TimeAnalyseService;
import com.zltel.common.utils.string.StringUtil;

public class TimeAnalyseServiceImpl implements TimeAnalyseService {
	private TimeAnalyseDao timeAnalyseDao;

	public final TimeAnalyseDao getTimeAnalyseDao() {
		return timeAnalyseDao;
	}

	public final void setTimeAnalyseDao(TimeAnalyseDao timeAnalyseDao) {
		this.timeAnalyseDao = timeAnalyseDao;
	}

	public ResultBean timeAnalysePerf(TimeAnalyseParam param) throws Exception {

		return this.timeAnalyseDao.timeAnalysePerf(param);
	}

	public ResultBean timeAnalyseSyslog(TimeAnalyseParam param) throws Exception {

		return this.timeAnalyseDao.timeAnalyseSyslog(param);
	}

	public List<Object[]> syslogGroup(TimeAnalyseParam param) throws Exception {
		long start = param.getTime();
		String idx = IndexTypeName.indexName(start);// ,1
		String[] indexs = new String[] { idx };
		String type = "syslog";
		List<SearchBean> sbs = new ArrayList<SearchBean>();
		SearchBean sb = null;
		if (StringUtil.isNotNullAndEmpty(param.getCi_id())) {
			sb = new SearchBean(Syslog._ci_id, SearchBean.PARAMTYPE_MUST, SearchBean.SYMBAL_CONTAIN);
			sb.setFrom(param.getCi_id());
			sbs.add(sb);
		} else if (StringUtil.isNotNullAndEmpty(param.getIp(), param.getHostname())) {
			sb = new SearchBean(Syslog._ip, SearchBean.PARAMTYPE_MUST, SearchBean.SYMBAL_CONTAIN);
			sb.setFrom(TypeConverToUtil.converIpToLong(param.getIp()));
			sbs.add(sb);
			sb = new SearchBean(Syslog._hostname, SearchBean.PARAMTYPE_MUST, SearchBean.SYMBAL_CONTAIN);
			sb.setFrom(param.getHostname());
			sbs.add(sb);
		} else {
			throw new RuntimeException("查询具体参数不明确, ci_id或者ip+hostname");
		}
		// todo: 根据时间选择 索引

		GroupBean gb = new GroupBean();
		gb.setGroupId("_test1");
		gb.setField("time");
		gb.setInterval(GroupBean.HOUR);
		return GroupServiceFacade.getInstince().groupByTime(indexs, type, sbs, gb);
	}

	public ResultBean syslogDetail(TimeAnalyseParam param) throws Exception {
		String index = IndexTypeName.indexName(param.getTime());
		List<SearchBean> sbs = new ArrayList<SearchBean>();
		SearchBean sb = null;
		if (StringUtil.isNotNullAndEmpty(param.getCi_id())) {
			sb = new SearchBean(Syslog._ci_id, SearchBean.PARAMTYPE_MUST, SearchBean.SYMBAL_CONTAIN);
			sb.setFrom(param.getCi_id());
			sbs.add(sb);
		} else if (StringUtil.isNotNullAndEmpty(param.getIp(), param.getHostname())) {
			sb = new SearchBean(Syslog._ip, SearchBean.PARAMTYPE_MUST, SearchBean.SYMBAL_CONTAIN);
			sb.setFrom(TypeConverToUtil.converIpToLong(param.getIp()));
			sbs.add(sb);
			sb = new SearchBean(Syslog._hostname, SearchBean.PARAMTYPE_MUST, SearchBean.SYMBAL_CONTAIN);
			sb.setFrom(param.getHostname());
			sbs.add(sb);
		} else {
			throw new RuntimeException("查询具体参数不明确, ci_id或者ip+hostname");
		}

		// 时间
		sb = new SearchBean("time", SearchBean.PARAMTYPE_MUST, SearchBean.SYMBAL_BETWEEN);
		sb.setFrom(param.getStartHour()).setTo(param.getEndHour());
		sbs.add(sb);

		PagerBean pb = new PagerBean();
		pb.setPageSize(300);// 最多值

		OrderBean order = new OrderBean("time", OrderBean.ORDER_ASC);

		return SyslogSearchServiceFacade.getInstince().searchSysLog(new String[] { index }, sbs, pb, null, null,
				Arrays.asList(order));
	}

}
