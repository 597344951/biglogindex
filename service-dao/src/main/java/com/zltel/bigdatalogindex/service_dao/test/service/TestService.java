package com.zltel.bigdatalogindex.service_dao.test.service;

import java.sql.SQLException;
import java.util.List;

import com.zltel.bigdatalogindex.service_dao.test.bean.TestBean;

public interface TestService {
	public int save(TestBean tb) throws SQLException;

	public int update(TestBean tb) throws SQLException;

	public int delete(TestBean tb) throws SQLException;

	public List<TestBean> queryForList(TestBean tb) throws SQLException;
}
