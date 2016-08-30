package com.zltel.bigdatalogindex.service_dao.query.service;

import com.zltel.common.utils.spring.AppContext;

public class QueryParamInitServiceFacade {
	public static final String BEANID = "paramInitService";

	public static final QueryParamInitService getInstince() {
		return AppContext.getBean(BEANID, QueryParamInitService.class);
	}
}
