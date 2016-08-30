package com.zltel.bigdatalogindex.service_dao.search.bean;

/**
 * 分页计算 bean
 * 
 * @author Wangch
 * 
 */
public class PagerBean {
	/** 开始位置 **/
	private int start;

	private int pageIndex = 1; // 需要显示的页码
	private int totalPages = 1; // 总页数
	private int pageSize = 20; // 每页记录数
	private long totalRecords = 0; // 总记录数
	private boolean isHavePrePage = false; // 是否有上一页
	private boolean isHaveNextPage = false; // 是否有下一页

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public long getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(long totalRecords) {
		if (totalRecords < 0) {
			throw new RuntimeException("总记录数不能小于0!");
		}
		// 设置总记录数
		this.totalRecords = totalRecords;
		if (this.pageSize == 0) {
			return;
		}
		// 计算总页数
		this.totalPages = (int) (this.totalRecords / this.pageSize);
		if (this.totalRecords % this.pageSize != 0) {
			this.totalPages++;
		}
		// 计算是否有上一页
		if (this.pageIndex > 1) {
			this.isHavePrePage = true;
		} else {
			this.isHavePrePage = false;
		}
		// 计算是否有下一页
		if (this.pageIndex < this.totalPages) {
			this.isHaveNextPage = true;
		} else {
			this.isHaveNextPage = false;
		}
	}

	public int getTotalPages() {
		return totalPages;
	}

	public boolean getIsHavePrePage() {
		return isHavePrePage;
	}

	public boolean getIsHaveNextPage() {
		return isHaveNextPage;
	}

	public final int getStart() {
		if (pageIndex >= 1) {
			return (pageIndex - 1) * pageSize;
		}
		return 0;
	}

	public final void setStart(int start) {
		this.start = start;
	}

}
