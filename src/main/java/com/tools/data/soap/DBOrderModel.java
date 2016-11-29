package com.tools.data.soap;

import java.util.List;

import com.tools.data.IpOverViewPayedOrdersModel;
import com.tools.data.navision.SalesOrderInfoModel;


public class DBOrderModel {

	private String incrementId;
	private String stylistId;
	private String createdAt;
	private String paidAt;
	private String status;
	private String orderType;
	private String cartType;
	private String grandTotal;
	private String totalIp;
	private String totalIpRefunded;
	private String termPurchaseType;
	private String orderCustomerName;
	private String scheduledDeliveryDate;
	private List<SalesOrderInfoModel> itemInfo;

	public String getScheduledDeliveryDate() {
		return scheduledDeliveryDate;
	}

	public List<SalesOrderInfoModel> getItemInfo() {
		return itemInfo;
	}

	public void setItemInfo(List<SalesOrderInfoModel> itemInfo) {
		this.itemInfo = itemInfo;
	}

	public void setScheduledDeliveryDate(String scheduledDeliveryDate) {
		this.scheduledDeliveryDate = scheduledDeliveryDate;
	}

	public String getOrderCustomerName() {
		return orderCustomerName;
	}

	public void setOrderCustomerName(String orderCustomerName) {
		this.orderCustomerName = orderCustomerName;
	}

	public String getIncrementId() {
		return incrementId;
	}

	public void setIncrementId(String incrementId) {
		this.incrementId = incrementId;
	}

	public String getStylistId() {
		return stylistId;
	}

	public void setStylistId(String stylistId) {
		this.stylistId = stylistId;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getCartType() {
		return cartType;
	}

	public void setCartType(String cartType) {
		this.cartType = cartType;
	}

	public String getTotalIp() {
		return totalIp;
	}

	public void setTotalIp(String totalIp) {
		this.totalIp = totalIp;
	}

	public String getTotalIpRefunded() {
		return totalIpRefunded;
	}

	public void setTotalIpRefunded(String totalIpRefunded) {
		this.totalIpRefunded = totalIpRefunded;
	}

	public String getGrandTotal() {
		return grandTotal;
	}

	public void setGrandTotal(String grandTotal) {
		this.grandTotal = grandTotal;
	}

	public String getPaidAt() {
		return paidAt;
	}

	public void setPaidAt(String paidAt) {
		this.paidAt = paidAt;
	}

	public String getTermPurchaseType() {
		return termPurchaseType;
	}

	public void setTermPurchaseType(String termPurchaseType) {
		this.termPurchaseType = termPurchaseType;
	}

}
