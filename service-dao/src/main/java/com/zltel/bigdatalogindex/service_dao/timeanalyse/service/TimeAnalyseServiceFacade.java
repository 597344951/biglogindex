package com.zltel.bigdatalogindex.service_dao.timeanalyse.service;

import com.zltel.common.utils.spring.AppContext;

public class TimeAnalyseServiceFacade {
	public static final String BEANID = "timeAnalyseService";

	public static final TimeAnalyseService getInstince() {
		return AppContext.getBean(BEANID);
	}

}
