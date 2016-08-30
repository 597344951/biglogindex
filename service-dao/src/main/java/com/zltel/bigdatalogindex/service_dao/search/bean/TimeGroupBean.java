package com.zltel.bigdatalogindex.service_dao.search.bean;

import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;

/**
 * 时间聚合 控制bean
 * 
 * @author Wangch
 * 
 */
public class TimeGroupBean {
	public static final DateHistogramInterval SECOND = DateHistogramInterval.SECOND;
	public static final DateHistogramInterval MINUTE = DateHistogramInterval.MINUTE;
	public static final DateHistogramInterval HOUR = DateHistogramInterval.HOUR;
	public static final DateHistogramInterval DAY = DateHistogramInterval.DAY;
	public static final DateHistogramInterval WEEK = DateHistogramInterval.WEEK;
	public static final DateHistogramInterval MONTH = DateHistogramInterval.MONTH;
	public static final DateHistogramInterval QUARTER = DateHistogramInterval.QUARTER;
	public static final DateHistogramInterval YEAR = DateHistogramInterval.YEAR;

	public static final String FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
	public static final String FORMAT_DATE = "yyyy-MM-dd";
	public static final String FORMAT_TIME = "HH:mm:ss";

	/** 字段 **/
	private String field;
	/** 聚合颗粒度 **/
	private DateHistogramInterval interval;
	/** 格式化 格式 **/
	private String format;

	public final String getField() {
		return field;
	}

	public final DateHistogramInterval getInterval() {
		return interval;
	}

	public final void setInterval(DateHistogramInterval interval) {
		this.interval = interval;
	}

	public final String getFormat() {
		return format;
	}

	public final void setField(String field) {
		this.field = field;
	}

	public final void setFormat(String format) {
		this.format = format;
	}

}
