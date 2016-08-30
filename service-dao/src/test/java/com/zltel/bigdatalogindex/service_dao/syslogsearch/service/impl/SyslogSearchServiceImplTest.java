package com.zltel.bigdatalogindex.service_dao.syslogsearch.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.zltel.bigdatalogindex.common.bean.Syslog;
import com.zltel.bigdatalogindex.service_dao.search.bean.OrderBean;
import com.zltel.bigdatalogindex.service_dao.search.bean.ResultBean;
import com.zltel.bigdatalogindex.service_dao.search.bean.SearchBean;
import com.zltel.bigdatalogindex.service_dao.syslogsearch.service.SyslogSearchService;
import com.zltel.bigdatalogindex.service_dao.syslogsearch.service.SyslogSearchServiceFacade;
import com.zltel.common.utils.junit.BaseSpringJunitTest;

public class SyslogSearchServiceImplTest extends BaseSpringJunitTest {
	SyslogSearchService syslogSearchService;

	@Before
	public void setUp() throws Exception {
		syslogSearchService = getInstince(SyslogSearchServiceFacade.BEANID);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testSearchSysLog() throws Exception {
		List<SearchBean> sbs = new ArrayList<SearchBean>();
		SearchBean sb = new SearchBean();
		sb.setField("level");
		sb.setParamType(SearchBean.PARAMTYPE_MUST);
		sb.setSymbal(SearchBean.SYMBAL_EQUAL);
		sb.setFrom("1");
		sbs.add(sb);

		ResultBean rb = this.syslogSearchService.searchSysLog(sbs, null, Syslog._detail, null, null);
		System.out.println(rb);
	}

	@Test
	public final void testHandleSearchJsonToSB() throws Exception {
		String json = "[{\"cate\":\"Syslog\",\"name\":\"level_type\",\"bool\":\"must\",\"symbal\":\"eq\",\"value\":\"auth\"},{\"cate\":\"Syslog\",\"name\":\"level\",\"bool\":\"must\",\"symbal\":\"eq\",\"value\":\"4\"}]";
		syslogSearchService.handleSearchJsonToSB(json);
	}

	@Test
	public final void testSyslogCountReport() throws Exception {
		List<SearchBean> sbs = new ArrayList<SearchBean>();
		SearchBean sb = new SearchBean();
		sb.setField("level");
		sb.setParamType(SearchBean.PARAMTYPE_MUST);
		sb.setSymbal(SearchBean.SYMBAL_EQUAL);
		sb.setFrom("1");
		sbs.add(sb);

		ResultBean m = this.syslogSearchService.syslogCountReport(null, sbs);
		System.out.println("日志聚合结果:");
		System.out.println(m);
	}

	@Test
	public final void testsyslogLevelReport() throws Exception {
		List<SearchBean> sbs = new ArrayList<SearchBean>();
		SearchBean sb = new SearchBean();
		sb.setField("level");
		sb.setParamType(SearchBean.PARAMTYPE_MUST);
		sb.setSymbal(SearchBean.SYMBAL_EQUAL);
		sb.setFrom("1");
		// sbs.add(sb);

		ResultBean m = this.syslogSearchService.syslogLevelReport(null, sbs);
		System.out.println("日志dengji聚合结果:");
		System.out.println(m);
	}

	@Test
	public final void testhandleOrder() {
		String str = "[{\"field\":\"field\",\"type\":false}]";
		List<OrderBean> obs = this.syslogSearchService.handleOrder(str);
		System.out.println(obs.toString());
	}

	@Test
	public final void testsyslogTopNReport() throws Exception {
		List<SearchBean> sbs = new ArrayList<SearchBean>();
		SearchBean sb = new SearchBean(Syslog._level, SearchBean.PARAMTYPE_MUST, SearchBean.SYMBAL_GTE);
		sb.setFrom("1");
		sbs.add(sb);
		ResultBean rb = this.syslogSearchService.syslogTopNReport("month", sbs, 20);

		System.out.println(rb);
	}

}
