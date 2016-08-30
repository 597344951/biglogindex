package com.zltel.bigdatalogindex.service_dao.search.service;

import com.zltel.common.utils.spring.AppContext;

public class ComSearchServiceFacade {

	public static final String BEANID = "comSearchService";

	public static final ComSearchService getInstince() {
		return AppContext.getBean(BEANID);
	}
}
