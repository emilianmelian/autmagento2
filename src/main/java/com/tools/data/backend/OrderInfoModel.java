package com.tools.data.backend;

public class OrderInfoModel {
	
	private String orderDate;
	private String orderStatus;
	private String aquiredBy;
	private String orderIP;
	
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getAquiredBy() {
		return aquiredBy;
	}
	public void setAquiredBy(String aquiredBy) {
		this.aquiredBy = aquiredBy;
	}
	public String getOrderIP() {
		return orderIP;
	}
	public void setOrderIP(String orderIP) {
		this.orderIP = orderIP;
	}
	

}
