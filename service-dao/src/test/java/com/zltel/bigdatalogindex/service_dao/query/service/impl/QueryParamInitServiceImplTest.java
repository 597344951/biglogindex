package com.zltel.bigdatalogindex.service_dao.query.service.impl;

import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.zltel.bigdatalogindex.service_dao.query.service.QueryParamInitService;
import com.zltel.bigdatalogindex.service_dao.query.service.QueryParamInitServiceFacade;
import com.zltel.common.utils.junit.BaseSpringJunitTest;

public class QueryParamInitServiceImplTest extends BaseSpringJunitTest {
	QueryParamInitService queryParamInitService;

	@Before
	public void setUp() throws Exception {
		queryParamInitService = getInstince(QueryParamInitServiceFacade.BEANID);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testQuerySyslogLevel_type() {
		System.out.println("出现过的日志类别");
		Map<String, Long> map = queryParamInitService.querySyslogLevel_type();
		for (Map.Entry<String, Long> entry : map.entrySet()) {
			System.out.println(entry.getKey() + " - " + entry.getValue());
		}
		Assert.assertNotNull(map);
	}

	@Test
	public final void testQuerySyslogLevel() {

	}

	@Test
	public final void testqueryAllIndexNames() {
		Set<String> sets = this.queryParamInitService.queryAllIndexNames();
		System.out.println("所有索引：");
		System.out.println(sets);
	}
}
