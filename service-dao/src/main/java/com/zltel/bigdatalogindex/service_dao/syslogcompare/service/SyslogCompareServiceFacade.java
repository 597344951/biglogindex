package com.zltel.bigdatalogindex.service_dao.syslogcompare.service;

import com.zltel.common.utils.spring.AppContext;

public class SyslogCompareServiceFacade {
	public static final String BEANID = "syslogCompareService";

	public static final SyslogCompareService getInstince() {
		return AppContext.getBean(BEANID);
	}

}
