package com.zltel.bigdatalogindex.service_dao.syslogsearch.service;

import com.zltel.common.utils.spring.AppContext;

/**
 * 获取日志搜索服务 实例
 * 
 * @author Wangch
 * 
 */
public class SyslogSearchServiceFacade {

	public static final String BEANID = "syslogSearchServiceImpl";

	public static final SyslogSearchService getInstince() {
		return AppContext.getBean(BEANID);
	}
}
