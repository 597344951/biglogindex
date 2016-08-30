package com.zltel.bigdatalogindex.service_dao.search.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.zltel.bigdatalogindex.service_dao.search.bean.PagerBean;
import com.zltel.bigdatalogindex.service_dao.search.bean.ResultBean;
import com.zltel.bigdatalogindex.service_dao.search.bean.SearchBean;
import com.zltel.bigdatalogindex.service_dao.search.bean.TimeGroupBean;
import com.zltel.bigdatalogindex.service_dao.search.service.ComSearchService;
import com.zltel.bigdatalogindex.service_dao.search.service.ComSearchServiceFacade;
import com.zltel.common.utils.junit.BaseSpringJunitTest;

public class ComSearchServiceImplTest extends BaseSpringJunitTest {
	private ComSearchService comSearchService;

	@Before
	public void setUp() throws Exception {
		comSearchService = getInstince(ComSearchServiceFacade.BEANID);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testSearchResult() throws Exception {
		try {
			String index = "store_2016-02-01";
			String type = "syslog";
			List<SearchBean> sbs = new ArrayList<SearchBean>();
			PagerBean pb = new PagerBean();
			pb.setStart(0);
			SearchBean sb = new SearchBean();
			sb.setField("level");
			sb.setParamType(SearchBean.PARAMTYPE_MUST);
			sb.setSymbal(SearchBean.SYMBAL_BETWEEN);
			sb.setFrom("1");
			sb.setTo("8");
			// sbs.add(sb);
			sb = new SearchBean();
			sb.setField("detail");
			sb.setParamType(SearchBean.PARAMTYPE_MUST);
			sb.setSymbal(SearchBean.SYMBAL_CONTAIN);
			sb.setFrom("SOFTWARE PROGRAM ERROR");
			sbs.add(sb);

			TimeGroupBean tb = new TimeGroupBean();
			tb.setField("time");
			tb.setInterval(TimeGroupBean.HOUR);
			tb.setFormat(TimeGroupBean.FORMAT_DATE_TIME);
			ResultBean rb = comSearchService.searchResult(index, type, sbs, pb, "detail", tb, null);
			Assert.assertNotNull(rb);
			System.out.println("总条数:" + rb.getPagerBean().getTotalRecords());
			System.out.println(rb.getResult());
			System.out.println(rb.getMark());
			System.out.println("发生时间频率统计");
			for (Map.Entry<String, Long> entry : rb.getTimeGroup().entrySet()) {
				System.out.println(entry.getKey() + " - " + entry.getValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
