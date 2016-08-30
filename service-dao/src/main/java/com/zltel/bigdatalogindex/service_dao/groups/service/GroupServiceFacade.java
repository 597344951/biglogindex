package com.zltel.bigdatalogindex.service_dao.groups.service;

import com.zltel.common.utils.spring.AppContext;

public class GroupServiceFacade {

	public static final String BEANID = "groupService";

	public static final GroupService getInstince() {
		return AppContext.getBean(BEANID);
	}
}
