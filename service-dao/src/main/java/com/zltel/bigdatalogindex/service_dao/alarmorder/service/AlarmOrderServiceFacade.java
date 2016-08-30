package com.zltel.bigdatalogindex.service_dao.alarmorder.service;

import com.zltel.bigdatalogindex.service_dao.alarmorder.service.impl.AlarmOrderServiceImpl;
import com.zltel.common.utils.spring.AppContext;

public class AlarmOrderServiceFacade {
	public static final String BEANID = "alarmOrderService";

	public static final AlarmOrderService getInstince() {
		return AppContext.getBean(AlarmOrderServiceImpl.class);
	}

}
