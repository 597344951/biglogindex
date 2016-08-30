package com.zltel.bigdatalogindex.service_dao.manage;

import java.util.List;
import java.util.Map;

public interface IndexManageService {

	public boolean delete(String index) throws Exception;

	public List<Map<String, String>> list() throws Exception;
}
