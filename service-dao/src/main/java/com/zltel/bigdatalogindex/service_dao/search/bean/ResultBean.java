package com.zltel.bigdatalogindex.service_dao.search.bean;

import java.util.List;
import java.util.Map;

public class ResultBean {
	/** 记录 **/
	private List<Map> result;
	/** 标记匹配 **/
	private List<Map> mark;
	private PagerBean pagerBean;

	private Map<String, Long> timeGroup;

	/** 花费时间 **/
	private long costTimeMiles;
	/** 总条数 */
	private long count;

	/** 消息 **/
	private String msg;
	/** 是否成功 **/
	private boolean success = true;

	private Object data;

	public ResultBean() {
		super();
	}

	public ResultBean(String msg, boolean success) {
		super();
		this.msg = msg;
		this.success = success;
	}

	public ResultBean(String msg, boolean success, Object data) {
		super();
		this.msg = msg;
		this.success = success;
		this.data = data;
	}

	public final List<Map> getResult() {
		return result;
	}

	public final List<Map> getMark() {
		return mark;
	}

	public final void setResult(List<Map> result) {
		this.result = result;
	}

	public final void setMark(List<Map> mark) {
		this.mark = mark;
	}

	public final PagerBean getPagerBean() {
		return pagerBean;
	}

	public final void setPagerBean(PagerBean pagerBean) {
		this.pagerBean = pagerBean;
	}

	public final Map<String, Long> getTimeGroup() {
		return timeGroup;
	}

	public final void setTimeGroup(Map<String, Long> timeGroup) {
		this.timeGroup = timeGroup;
	}

	public final long getCostTimeMiles() {
		return costTimeMiles;
	}

	/**
	 * @return the data
	 */
	public final Object getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public final void setData(Object data) {
		this.data = data;
	}

	public final void setCostTimeMiles(long costTimeMiles) {
		this.costTimeMiles = costTimeMiles;
	}

	/**
	 * @return the 消息
	 */
	public final String getMsg() {
		return msg;
	}

	/**
	 * @return the 是否成功
	 */
	public final boolean isSuccess() {
		return success;
	}

	/**
	 * @param 消息
	 *            the msg to set
	 */
	public final void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * @param 是否成功
	 *            the success to set
	 */
	public final void setSuccess(boolean success) {
		this.success = success;
	}

	/**
	 * @return the 总条数
	 */
	public final long getCount() {
		return count;
	}

	/**
	 * @param 总条数
	 *            the count to set
	 */
	public final void setCount(long count) {
		this.count = count;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ResultBean [result size=");
		builder.append(result.size());
		builder.append(", mark size=");
		builder.append(", pagerBean=");
		builder.append(pagerBean.toString());
		builder.append(", cosetTimeMiles=");
		builder.append(costTimeMiles);
		builder.append("]");
		return builder.toString();
	}

}
