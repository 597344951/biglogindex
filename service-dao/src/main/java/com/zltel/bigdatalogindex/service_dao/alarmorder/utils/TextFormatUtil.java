package com.zltel.bigdatalogindex.service_dao.alarmorder.utils;

import com.zltel.common.utils.string.StringUtil;

public class TextFormatUtil {

	public static final String format(String str) {
		if (StringUtil.isNullOrEmpty(str)) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		String[] as = str.split(" = ");
		String rgx = "_eq_";
		for (String s : as) {
			s += rgx;
			boolean f = false;
			for (int i = s.length() - 1; i >= 0; i--) {
				if (' ' == s.charAt(i)) {
					sb.append(s.substring(0, i)).append("\n").append(s.substring(i));
					f = true;
					break;
				}
			}
			if (!f) {
				sb.append(s);
			}
		}
		String ret = sb.toString().replace(rgx, ":");
		ret = ret.replaceAll(",", "\n");

		return ret;
	}

	public static void main(String[] args) {
		String str = "告警对设备的影响: 告警对业务的影响: 告警地市:省公司 告警正文:[+++] 告警流水号 = ZJHZ-px-bppm-35-alr-23904 告警ID = IP地址 = 10.212.205.52 主机名称 = GPRSTNES45 设备类型 = X86 设备厂家 = HP 一级分类 = IT维护室 二级分类 = 移动互联网 三级分类 = GPRS 告警标题 = Free Space 告警级别 = CRITICAL 告警位置 = D: 告警正文 = Logical Disk Free Space [= 10% and outside all baselines for 5 min.当前值0.92 发生时间 = 2015-12-25 14:44:00 插入时间 = 2015-12-25 14:44:27 告警状态 = OPEN 附加信息 = 设备名称:GPRSTNES45,设备位置:SQ3#-4F-C03,设备序列号:CZJ32304C8,设备型号:DL360 G8,设备星级:4星,责任人:方军-裴达兵/孔华明,处理级别:三级处理,是否维护移交:是,生命周期状态:在网 设备CI号 = PCS00001407 告警来源 = bmc 地市 = 省公司 [---]";
		String s = format(str);

		System.out.println(s);
	}
}
