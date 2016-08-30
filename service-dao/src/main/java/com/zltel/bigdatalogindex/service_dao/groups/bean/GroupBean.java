package com.zltel.bigdatalogindex.service_dao.groups.bean;

import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;

public class GroupBean {
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

	// ///////////////////
	/** 聚合分组 id **/
	private String groupId;
	/** 字段 **/
	private String field;
	/** 聚合颗粒度 **/
	private DateHistogramInterval interval;

	/** 格式化 格式 **/
	private String format;
	/** 最少个数 **/
	private int minCount = 1;

	public final String getGroupId() {
		return groupId;
	}

	public final String getField() {
		return field;
	}

	public DateHistogramInterval getInterval() {
		return interval;
	}

	public void setInterval(DateHistogramInterval interval) {
		this.interval = interval;
	}

	public final void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public final void setField(String field) {
		this.field = field;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	/**
	 * @return the 最少个数
	 */
	public final int getMinCount() {
		return minCount;
	}

	/**
	 * @param 最少个数
	 *            the minCount to set
	 */
	public final void setMinCount(int minCount) {
		this.minCount = minCount;
	}

}
