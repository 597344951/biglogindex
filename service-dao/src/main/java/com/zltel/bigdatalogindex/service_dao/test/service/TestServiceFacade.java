package com.zltel.bigdatalogindex.service_dao.test.service;

import com.zltel.common.utils.spring.AppContext;

public class TestServiceFacade {
	public static final String BEANID = "testService";

	public static final TestService getInstince() {
		return AppContext.getBean(BEANID);
	}

}
