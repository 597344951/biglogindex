package com.zltel.bigdatalogindex.service_dao.timeanalyse.bean;

import java.text.ParseException;
import java.util.Calendar;

import com.zltel.common.utils.date.Date;
import com.zltel.common.utils.date.DateUtil;
import com.zltel.common.utils.string.StringUtil;

/**
 * 时间分析参数
 * 
 * @author Wangch
 * 
 */
public class TimeAnalyseParam {
	public static final int STATUS_PERF = 1;
	public static final int STATUS_SYSLOG = 2;
	public static final int STATUS_ERPT = 3;

	/** IP **/
	private String ip;
	/** 主机名 **/
	private String hostname;
	/** 配置项编号 **/
	private String ci_id;
	/** 分析时间 **/
	private Long time;
	/** 时间前后间隔 **/
	private int timeRange = 30 * 60 * 1000;

	private int status;

	private String startTime;
	private String endTime;
	/** 开始 小时数 字符串 2015-12-23 11:00:00 **/
	private String startHour;
	/** 结束小时数 字符串 2015-12-23 11:00:00 **/
	private String endHour;

	public final int getStatus() {
		return status;
	}

	public final void setStatus(int status) {
		this.status = status;
	}

	public final String getIp() {
		return ip;
	}

	public final String getHostname() {
		return hostname;
	}

	public final String getCi_id() {
		return ci_id;
	}

	public final Long getTime() {
		return time;
	}

	public final void setIp(String ip) {
		this.ip = ip;
	}

	public final void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public final void setCi_id(String ci_id) {
		this.ci_id = ci_id;
	}

	public final int getTimeRange() {
		return timeRange;
	}

	public final void setTimeRange(int timeRange) {
		this.timeRange = timeRange;
	}

	public final void setTime(Long time) {
		this.time = time;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getStartHour() throws ParseException {
		String st = DateUtil.sdf_hour.format(new Date(this.time));
		java.util.Date dt = DateUtil.sdf_hour.parse(st);
		return DateUtil.sdf.format(dt);
	}

	 

	public String getEndHour() throws ParseException {
		String st = DateUtil.sdf_hour.format(new Date(this.time));
		java.util.Date dt = DateUtil.sdf_hour.parse(st);
		dt = new Date(dt.getTime()+3600*1000);
		return DateUtil.sdf.format(dt);
	}
	 

	/**
	 * 判断参数是否 完整可用
	 * 
	 * @return true:是,false:不是
	 */
	public boolean getIsAvaliable() {
		boolean ret = true;
		if (StringUtil.isNotNullAndEmpty(this.getIp(), this.getHostname())
				|| StringUtil.isNotNullAndEmpty(this.getCi_id())) {
			if (this.getTime() == null) {
				ret = false;
			}
		} else {
			ret = false;
		}
		if (this.getStatus() == 0) {
			ret = false;
		}
		return ret;
	}
}
