package com.zltel.bigdatalogindex.service_dao.timeanalyse.dao;

import com.zltel.bigdatalogindex.service_dao.search.bean.ResultBean;
import com.zltel.bigdatalogindex.service_dao.timeanalyse.bean.TimeAnalyseParam;

public interface TimeAnalyseDao {

	ResultBean timeAnalysePerf(TimeAnalyseParam param)throws Exception;

	ResultBean timeAnalyseSyslog(TimeAnalyseParam param) throws Exception;

}
