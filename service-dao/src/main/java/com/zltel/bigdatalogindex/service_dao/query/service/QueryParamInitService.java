package com.zltel.bigdatalogindex.service_dao.query.service;

import java.util.Map;
import java.util.Set;

/**
 * 加载查询条件参数<br>
 * 例如： 日志类别
 * 
 * @author Wangch
 * 
 */
public interface QueryParamInitService {

	/**
	 * 查询日志 等级类型字段<br>
	 * 例如:auth
	 * 
	 * @return
	 */
	public Map<String, Long> querySyslogLevel_type();

	/**
	 * 查询日志 出现过的 等级
	 * 
	 * @deprecated 非字符串类型无法使用
	 * @return
	 */
	@Deprecated
	public Map<String, Long> querySyslogLevel();
	
	/**
	 * 查询所有 索引名称
	 * @return 索引名称
	 */
	public Set<String> queryAllIndexNames();
}
