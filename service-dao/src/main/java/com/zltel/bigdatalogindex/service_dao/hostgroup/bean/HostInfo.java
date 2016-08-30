package com.zltel.bigdatalogindex.service_dao.hostgroup.bean;

import java.text.ParseException;
import java.util.Date;

import com.zltel.common.utils.date.DateUtil;

/**
 * 节点 信息bean
 * 
 * @author Wangch
 * 
 */
public class HostInfo {

	/** 配置项编号 **/
	private String ci_id;
	/** 主机名 **/
	private String hostname;
	/** ip地址 **/
	private String ip;

	/** 查询开始时间 **/
	private String startTime;
	/** 查询结束时间 **/
	private String endTime;

	public HostInfo(String ci_id, String hostname, String ip) {
		super();
		this.ci_id = ci_id;
		this.hostname = hostname;
		this.ip = ip;
	}

	public HostInfo() {
		super();
	}

	/**
	 * @return the 配置项编号
	 */
	public final String getCi_id() {
		return ci_id;
	}

	/**
	 * @return the 主机名
	 */
	public final String getHostname() {
		return hostname;
	}

	/**
	 * @return the ip地址
	 */
	public final String getIp() {
		return ip;
	}

	/**
	 * @param 配置项编号
	 *            the ci_id to set
	 */
	public final void setCi_id(String ci_id) {
		this.ci_id = ci_id;
	}

	/**
	 * @param 主机名
	 *            the hostname to set
	 */
	public final void setHostname(String hostname) {
		this.hostname = hostname;
	}

	/**
	 * @param ip地址
	 *            the ip地址 to set
	 */
	public final void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * @return the 查询开始时间
	 */
	public final String getStartTime() {
		return startTime;
	}

	/**
	 * @return the 查询结束时间
	 */
	public final String getEndTime() {
		return endTime;
	}

	/**
	 * @param 查询开始时间
	 *            the startTime to set
	 */
	public final void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	/**
	 * @param 查询结束时间
	 *            the endTime to set
	 */
	public final void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	/**
	 * 设置 开始时间 和 结束时间
	 * 
	 * @param startTime2
	 * @param startStep
	 * @param limitStep
	 * @throws ParseException
	 */
	public void setStartEndTime(String baseTime, Integer offset, Integer timeRange, Integer timebn)
			throws ParseException {
		Date _dt = DateUtil.converToDate(DateUtil.sdf, baseTime);
		long st_long = _dt.getTime() + offset * 60 * 1000;
		long et_long = st_long + (timebn == 1 ? 1 : -1) * timeRange * 60 * 1000;
		this.setStartTime(new com.zltel.common.utils.date.Date(st_long).toDateTime());
		this.setEndTime(new com.zltel.common.utils.date.Date(et_long).toDateTime());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("HostInfo [ci_id=");
		builder.append(ci_id);
		builder.append(", hostname=");
		builder.append(hostname);
		builder.append(", ip=");
		builder.append(ip);
		builder.append(", startTime=");
		builder.append(startTime);
		builder.append(", endTime=");
		builder.append(endTime);
		builder.append("]");
		return builder.toString();
	}

}
