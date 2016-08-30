package com.zltel.bigdatalogindex.service_dao.hostgroup.bean;

/**
 * 群组信息
 * 
 * @author Wangch
 * 
 */
public class GroupInfo {
	/** 群组名称 **/
	private String name;
	/** 群组节点数量 **/
	private int count;
	/** 群组创建时间 **/
	private String createTime;
	/** 群组创建人 **/
	private String createUser;

	/**
	 * @return the 群组名称
	 */
	public final String getName() {
		return name;
	}

	/**
	 * @return the 群组节点数量
	 */
	public final int getCount() {
		return count;
	}

	/**
	 * @return the 群组创建时间
	 */
	public final String getCreateTime() {
		return createTime;
	}

	/**
	 * @return the 群组创建人
	 */
	public final String getCreateUser() {
		return createUser;
	}

	/**
	 * @param 群组名称
	 *            the name to set
	 */
	public final void setName(String name) {
		this.name = name;
	}

	/**
	 * @param 群组节点数量
	 *            the count to set
	 */
	public final void setCount(int count) {
		this.count = count;
	}

	/**
	 * @param 群组创建时间
	 *            the createTime to set
	 */
	public final void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	/**
	 * @param 群组创建人
	 *            the createUser to set
	 */
	public final void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

}
