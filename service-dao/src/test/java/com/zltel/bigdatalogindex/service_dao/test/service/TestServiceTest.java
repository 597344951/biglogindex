package com.zltel.bigdatalogindex.service_dao.test.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.zltel.bigdatalogindex.service_dao.test.bean.TestBean;
import com.zltel.common.utils.conf.ConfigUtil;
import com.zltel.common.utils.junit.BaseSpringJunitTest;

public class TestServiceTest extends BaseSpringJunitTest {
	TestService testService;

	@Before
	public void setUp() throws Exception {
		testService = getInstince(TestServiceFacade.BEANID);
	}

	@After
	public void tearDown() throws Exception {
	}

	 
	public final void testQueryForList() throws SQLException {
		List<TestBean> tbs = testService.queryForList(new TestBean());
		System.out.println(tbs.size());
		Assert.assertNotNull(tbs);
		Map<String, String> map = ConfigUtil.resolveConfigProFile("elasticsearch.properties");
		System.out.println("读取参数:" + map);
	}

}
