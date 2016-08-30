package com.zltel.bigdatalogindex.service_dao.timeanalyse.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.zltel.bigdatalogindex.service_dao.search.bean.ResultBean;
import com.zltel.bigdatalogindex.service_dao.timeanalyse.bean.TimeAnalyseParam;
import com.zltel.common.utils.junit.BaseSpringJunitTest;
import com.zltel.common.utils.spring.AppContext;

public class TimeAnalyseServiceTest extends BaseSpringJunitTest {
	TimeAnalyseService timeAnalyseService;
	long _time = 1420070400000l;

	@Before
	public void setUp() throws Exception {
		AppContext.setCtx(getCtx());
		timeAnalyseService = getInstince(TimeAnalyseServiceFacade.BEANID);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testTimeAnalysePerf() throws Exception {
		TimeAnalyseParam param = new TimeAnalyseParam();
		param.setCi_id("CONFIGNUM0");
		param.setHostname("host0");
		param.setIp("192168001010");
		param.setTime(_time);
		param.setStatus(TimeAnalyseParam.STATUS_PERF);

		ResultBean rb = timeAnalyseService.timeAnalysePerf(param);
		System.out.println("耗时：" + rb.getCostTimeMiles());
		System.out.println("perf count:" + rb.getResult().size());

	}

	
	public void testTimeAnalyseSyslog() throws Exception {
		TimeAnalyseParam param = new TimeAnalyseParam();
		param.setTimeRange(3600000 * 2);
		param.setCi_id("CONFIGNUM0");
		param.setHostname("host0");
		param.setIp("192168001010");
		param.setTime(_time);
		param.setStatus(TimeAnalyseParam.STATUS_SYSLOG);
		ResultBean rb = timeAnalyseService.timeAnalyseSyslog(param);
		System.out.println("耗时：" + rb.getCostTimeMiles());
		System.out.println("syslog count:" + rb.getResult().size());
	}

	@Test
	public void testsyslogDetail() throws Exception {
		TimeAnalyseParam param = new TimeAnalyseParam();
		param.setTimeRange(3600000 * 2);
		param.setCi_id("CONFIGNUM0");
		param.setHostname("host0");
		param.setIp("192168001010");
		param.setTime(_time);
		param.setStatus(TimeAnalyseParam.STATUS_SYSLOG);
		ResultBean rb = timeAnalyseService.syslogDetail(param);
		System.out.println("syslog detail");
		System.out.println(rb);
	}

}
