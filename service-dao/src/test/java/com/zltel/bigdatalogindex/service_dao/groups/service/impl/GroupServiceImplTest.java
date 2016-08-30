package com.zltel.bigdatalogindex.service_dao.groups.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.zltel.bigdatalogindex.service_dao.groups.bean.GroupBean;
import com.zltel.bigdatalogindex.service_dao.groups.service.GroupService;
import com.zltel.bigdatalogindex.service_dao.groups.service.GroupServiceFacade;
import com.zltel.bigdatalogindex.service_dao.search.bean.PagerBean;
import com.zltel.bigdatalogindex.service_dao.search.bean.SearchBean;
import com.zltel.common.utils.date.Date;
import com.zltel.common.utils.junit.BaseSpringJunitTest;

public class GroupServiceImplTest extends BaseSpringJunitTest {
	private GroupService groupService;

	@Before
	public void setUp() throws Exception {
		groupService = getInstince(GroupServiceFacade.BEANID);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testGroup() throws ParseException {
		String[] indexs = { "store_2015-01-01","store_2015-01-02","store_2015-01-03"};
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
		sb.setFrom("error");
		// sbs.add(sb);

		sb = new SearchBean();
		sb.setField("ci_id");
		sb.setParamType(SearchBean.PARAMTYPE_MUST);
		sb.setSymbal(SearchBean.SYMBAL_CONTAIN);
		sb.setFrom("CONFIGNUM0");
		sbs.add(sb);
		
		sb = new SearchBean();
		sb.setField("time");
		sb.setParamType(SearchBean.PARAMTYPE_MUST);
		sb.setSymbal(SearchBean.SYMBAL_BETWEEN);
		sb.setFrom(String.valueOf(Date.sdf.parse("2016-01-28 00:00:00").getTime()));
		sb.setTo(String.valueOf(System.currentTimeMillis()));
		//sbs.add(sb);

		GroupBean gb = new GroupBean();
		gb.setGroupId("_test1");
		gb.setField("time");
		gb.setInterval(GroupBean.HOUR);

		List<Object[]> _d = groupService.groupByTime( indexs, type, sbs, gb);
		System.out.println("聚合结果");
		for(Object[] os : _d){
			String t = (String)os[0];
			Long c = (Long)os[1];
			System.out.println(t+" 条数： "+c);
		}
		
	}
}
