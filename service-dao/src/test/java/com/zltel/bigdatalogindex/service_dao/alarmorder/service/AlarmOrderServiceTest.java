package com.zltel.bigdatalogindex.service_dao.alarmorder.service;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.zltel.bigdatalogindex.service_dao.search.bean.PagerBean;
import com.zltel.bigdatalogindex.service_dao.search.bean.ResultBean;
import com.zltel.bigdatalogindex.service_dao.search.bean.SearchBean;
import com.zltel.common.utils.date.DateUtil;
import com.zltel.common.utils.junit.BaseSpringJunitTest;

public class AlarmOrderServiceTest extends BaseSpringJunitTest {
	private AlarmOrderService alarmOrderService;

	@Before
	public void setUp() throws Exception {
		alarmOrderService = AlarmOrderServiceFacade.getInstince();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testSearchResult() {
		assertNotNull(alarmOrderService);
		List<SearchBean> sbs = new ArrayList<SearchBean>();
		SearchBean sb = new SearchBean();
		sb.setField("level");
		sb.setParamType(SearchBean.PARAMTYPE_MUST);
		sb.setSymbal(SearchBean.SYMBAL_CONTAIN);
		sb.setFrom("1");
		sbs.add(sb);

		PagerBean pb = new PagerBean();
		ResultBean rb = alarmOrderService.searchResult(sbs, pb);
		System.out.println(rb);
	}

	@Test
	public final void testLoad() {

	}

	@Test
	public final void testrelatedAnalyse() throws Exception {
		String host = "{\"ip\":\"192.168.001.013\",\"host\":\"host3\",\"ci_id\":\"CONFIGNUM3\"}";
		Long time = DateUtil.sdf.parse("2016-02-11 01:00:00").getTime();
		int step = 15;

		Map m = alarmOrderService.relatedAnalyse(host, time, step, "");
		System.out.println("相关告警----------------------");
		System.out.println(m);

	}

	@Test
	public void testqueryRelatedHost() throws Exception {
		String host = "{\"ip\":\"192.168.001.013\",\"host\":\"host3\",\"ci_id\":\"CONFIGNUM3\"}";
		List<Map> l = this.alarmOrderService.queryRelatedHost(host);
		System.out.println(JSON.toJSONString(l));
	}

}
