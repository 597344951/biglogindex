package com.zltel.bigdatalogindex.service_dao.manage;

import com.zltel.common.utils.spring.AppContext;

public class IndexManageServiceFacade {

	public static final String BEANID = "indexManageService";

	public static final IndexManageService getInstince() {
		return AppContext.getBean(BEANID);
	}

}
