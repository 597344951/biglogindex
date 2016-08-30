package com.zltel.bigdatalogindex.service_dao.test.service.impl;

import java.sql.SQLException;
import java.util.List;

import com.zltel.bigdatalogindex.service_dao.test.bean.TestBean;
import com.zltel.bigdatalogindex.service_dao.test.dao.TestDao;
import com.zltel.bigdatalogindex.service_dao.test.service.TestService;

public class TestServiceImpl implements TestService {
	private TestDao testDao;

	public int save(TestBean tb) throws SQLException {
		this.testDao.save(tb);
		return 0;
	}

	public int update(TestBean tb) throws SQLException {
		this.testDao.update(tb);
		return 0;
	}

	public int delete(TestBean tb) throws SQLException {
		this.testDao.delete(tb);
		return 0;
	}

	public List<TestBean> queryForList(TestBean tb) throws SQLException {
		return this.testDao.queryForList(tb);
	}

	public final TestDao getTestDao() {
		return testDao;
	}

	public final void setTestDao(TestDao testDao) {
		this.testDao = testDao;
	}

}
