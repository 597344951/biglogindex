package com.zltel.bigdatalogindex.service_dao.syslogcompare.service.impl;

import java.text.ParseException;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.zltel.bigdatalogindex.service_dao.hostgroup.bean.HostInfo;
import com.zltel.bigdatalogindex.service_dao.search.bean.ResultBean;
import com.zltel.bigdatalogindex.service_dao.syslogcompare.service.SyslogCompareService;
import com.zltel.bigdatalogindex.service_dao.syslogcompare.service.SyslogCompareServiceFacade;
import com.zltel.common.utils.junit.BaseSpringJunitTest;

public class SyslogCompareServiceImplTest extends BaseSpringJunitTest {
	private SyslogCompareService compareService;

	@Before
	public void setUp() throws Exception {
		compareService = SyslogCompareServiceFacade.getInstince();// getInstince(SyslogCompareServiceFacade.BEANID);
		sb.append("[ {");
		sb.append("		\"ci_id\" : \"CONFIGNUM4\",");
		sb.append("		\"hostname\" : \"host4\",");
		sb.append("		\"ip\" : \"192.168.001.014\",");
		sb.append("		\"offset\" : 12");
		sb.append("	}, {");
		sb.append("		\"ci_id\" : \"CONFIGNUM5\",");
		sb.append("		\"hostname\" : \"host5\",");
		sb.append("		\"ip\" : \"192.168.001.015\",");
		sb.append("		\"offset\" : -12");
		sb.append("	} ]");
	}

	@After
	public void tearDown() throws Exception {
	}

	private StringBuffer sb = new StringBuffer();

	@Test
	public final void testHandleToNodes() throws ParseException {

		List<HostInfo> hosts = this.compareService.handleToNodes(sb.toString(), baseTime, timeRange, timebn);
		Assert.assertNotNull(hosts);
		System.out.println("要进行对比的节点：");
		for (HostInfo hi : hosts) {
			System.out.println(hi.toString());
		}
	}

	private String baseTime = "2016-01-21 00:00:00";
	private int timeRange = 120;
	private int timebn = 1;
	private int timeGroup = 1;
	private int timeOrder = 1;

	@Test
	public final void testSyslogCompare() throws Exception {
		List<HostInfo> hosts = this.compareService.handleToNodes(sb.toString(), baseTime, timeRange, timebn);
		ResultBean rb = this.compareService.syslogCompare(hosts, null, timeGroup, timeOrder);
		Object data = rb.getData();
		Assert.assertNotNull(data);
		System.out.println("耗时： " + rb.getCostTimeMiles() + "ms  总条数:" + rb.getCount());
	}

}
