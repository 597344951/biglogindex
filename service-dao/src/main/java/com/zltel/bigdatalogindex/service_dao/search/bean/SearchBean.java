package com.zltel.bigdatalogindex.service_dao.search.bean;

import java.util.List;

public class SearchBean {
	/** 条件 必须符合 **/
	public static final int PARAMTYPE_MUST = 1;
	/** 条件 必须不符合 **/
	public static final int PARAMTYPE_MUST_NOT = -1;
	/** 条件 可以符合 **/
	public static final int PARAMTYPE_SHOULD = 0;

	// --------------------------------------------------------------
	/** 判断逻辑 强制等于,对于候选字段使用此规则 **/
	public static final int SYMBAL_MUST_EQUAL = 1;

	/** 判断逻辑 等于 **/
	public static final int SYMBAL_EQUAL = 2;
	/** 判断逻辑 包含 **/
	public static final int SYMBAL_CONTAIN = 3;
	/** 包含多个 **/
	public static final int SYMBAL_CONTAINS = 4;

	/*
	 * 后续 的符号判断 需要两个值
	 */
	/** 判断逻辑 大于 **/
	public static final int SYMBAL_GT = 5;
	/** 判断逻辑 大于等于 **/
	public static final int SYMBAL_GTE = 6;
	/** 判断逻辑 小于 **/
	public static final int SYMBAL_LT = 7;
	/** 判断逻辑 小于等于 **/
	public static final int SYMBAL_LTE = 8;
	/** 判断逻辑 范围 **/
	public static final int SYMBAL_BETWEEN = 9;

	/** 搜索类型 **/
	private Integer paramType;
	/** 搜索对应的字段 **/
	private String field;
	/** 操作符号 **/
	private Integer symbal;
	/** 值 开始 **/
	private String from;
	/** 值结束 **/
	private String to;

	private List<String> keys;

	public SearchBean() {
	}

	public SearchBean(String field, Integer paramType, Integer symbal) {
		super();
		this.field = field;
		this.paramType = paramType;
		this.symbal = symbal;
	}

	public final Integer getParamType() {
		return paramType;
	}

	public final String getField() {
		return field;
	}

	public final Integer getSymbal() {
		return symbal;
	}

	public final String getFrom() {
		return from;
	}

	public final String getTo() {
		return to;
	}

	public final void setParamType(Integer paramType) {
		this.paramType = paramType;
	}

	public final void setField(String field) {
		this.field = field;
	}

	public final void setSymbal(Integer symbal) {
		this.symbal = symbal;
	}

	public final SearchBean setFrom(String from) {
		this.from = from;
		return this;
	}

	public final SearchBean setTo(String to) {
		this.to = to;
		return this;
	}

	public final List<String> getKeys() {
		return keys;
	}

	public final void setKeys(List<String> keys) {
		this.keys = keys;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SearchBean [paramType=");
		builder.append(paramType);
		builder.append(", field=");
		builder.append(field);
		builder.append(", symbal=");
		builder.append(symbal);
		builder.append(", from=");
		builder.append(from);
		builder.append(", to=");
		builder.append(to);
		builder.append("]");
		return builder.toString();
	}

	/**
	 * 设置逻辑类型
	 * 
	 * @param bool
	 */
	public void setParamType(String bool) {
		String _b = bool.trim().toLowerCase();
		if ("must".equals(_b)) {
			this.setParamType(PARAMTYPE_MUST);
		} else if ("should".equals(_b)) {
			this.setParamType(PARAMTYPE_SHOULD);
		} else if ("not".equals(_b)) {
			this.setParamType(PARAMTYPE_MUST_NOT);
		}
	}

	public void setSymbal(String symbal2, String from2, String to2, String value) {
		String _symbal = symbal2.trim().toLowerCase();
		if ("eq".equals(_symbal)) {
			this.setSymbal(SYMBAL_EQUAL);
			this.setFrom(value);
		} else if ("gt".equals(_symbal)) {
			this.setSymbal(SYMBAL_GT);
			this.setFrom(value);
		} else if ("gte".equals(_symbal)) {
			this.setSymbal(SYMBAL_GTE);
			this.setFrom(value);
		} else if ("lt".equals(_symbal)) {
			this.setSymbal(SYMBAL_LT);
			this.setFrom(value);
		} else if ("lte".equals(_symbal)) {
			this.setSymbal(SYMBAL_LTE);
			this.setFrom(value);
		} else if ("between".equals(_symbal)) {
			this.setSymbal(SYMBAL_BETWEEN);
			this.setFrom(from2);
			this.setTo(to2);
		}
	}
}
