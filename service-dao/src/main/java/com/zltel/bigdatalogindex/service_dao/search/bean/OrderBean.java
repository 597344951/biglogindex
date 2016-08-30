package com.zltel.bigdatalogindex.service_dao.search.bean;

import org.elasticsearch.search.sort.SortOrder;

public class OrderBean {

	public static final SortOrder ORDER_ASC = SortOrder.ASC;
	public static final SortOrder ORDER_DESC = SortOrder.DESC;
	/** 排序字段 **/
	private String field;
	/** 排序类型 升序还是降序 **/
	private SortOrder orderType;

	public OrderBean(String field, SortOrder orderType) {
		super();
		this.field = field;
		this.orderType = orderType;
	}

	public SortOrder getOrderType() {
		return orderType;
	}

	public void setOrderType(SortOrder orderType) {
		this.orderType = orderType;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

}
