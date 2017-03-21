package com.connectors.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.jruby.RubyProcess.Sys;
import org.w3c.dom.NodeList;

import com.tools.constants.EnvironmentConstants;
import com.tools.constants.SoapConstants;
import com.tools.constants.SoapKeys;
import com.tools.constants.UrlConstants;
import com.tools.data.navision.SalesOrderInfoModel;
import com.tools.data.soap.DBOrderModel;

/**
 * @author mihaibarta
 *
 */

public class OrdersInfoMagentoCalls {

	// private static String sessID = LoginSoapCall.performLogin();

	public static List<DBOrderModel> getOrdersList(String stylistId) {

		List<DBOrderModel> orderList = new ArrayList<DBOrderModel>();

		try {
			SOAPMessage response = soapGetOrdersList(stylistId);
			System.out.println(response);
			try {
				orderList = extractOrderData(response);
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (SOAPException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return orderList;
	}

	public static List<DBOrderModel> getOrdersInRangeList(String orderLimit1, String orderLimit2) {

		List<DBOrderModel> orderList = new ArrayList<DBOrderModel>();

		try {
			SOAPMessage response = soapGetOrdersRangeList(orderLimit1, orderLimit2);
			System.out.println(response);
			try {
				orderList = extractOrderData(response);
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (SOAPException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return orderList;

	}

	public static List<DBOrderModel> getOrderWithItems(String orderLimit1, String orderLimit2)
			throws SOAPException, IOException {

		List<DBOrderModel> orderList = getOrdersInRangeList(orderLimit1, orderLimit2);
		String sessID = HttpSoapConnector.performLogin();

		for (DBOrderModel order : orderList) {
			DBOrderModel orderWithInfo = OrderInfoMagCalls.getOrdersInfo(order.getIncrementId(), sessID);

			String customerFirstName = orderWithInfo.getCustomerFirstName();
			String customerLastName = orderWithInfo.getCustomerLastName().replaceAll("\\s", "");

			order.setCustomerName(customerFirstName.concat(customerLastName));
			order.setCustomerFirstName(orderWithInfo.getCustomerFirstName());
			order.setCustomerLastName(orderWithInfo.getCustomerLastName());
			order.setIsPom(orderWithInfo.getIsPom());
			order.setOrderCustomerName(orderWithInfo.getOrderCustomerName());
			// billing address details
			order.setItemInfo(orderWithInfo.getItemInfo());
			order.setBillToFirstName(orderWithInfo.getBillToFirstName());
			order.setBillToLastName(orderWithInfo.getBillToLastName());
			order.setBillToStreetAddress(orderWithInfo.getBillToStreetAddress());
			order.setBillToCity(orderWithInfo.getBillToCity());
			order.setBillCountryId(orderWithInfo.getBillCountryId());
			order.setBillToPostcode(orderWithInfo.getBillToPostcode());

			// shipping address details
			order.setShipToFirstName(orderWithInfo.getShipToFirstName());
			order.setShipToLastName(orderWithInfo.getShipToLastName());
			order.setShipToStreetAddress(orderWithInfo.getShipToStreetAddress());
			order.setShipToCity(orderWithInfo.getShipToCity());
			order.setShipCountryId(orderWithInfo.getShipCountryId());
			order.setShipToPostcode(orderWithInfo.getShipToPostcode());
			order.setShipToHousNumber(orderWithInfo.getShipToHousNumber());
			//
			order.setUpdatedNav(orderWithInfo.getUpdatedNav());
			order.setOrderCurrencyCode(orderWithInfo.getOrderCurrencyCode());
			order.setStylePartyId(orderWithInfo.getStylePartyId());
			order.setKoboSingleArticle(orderWithInfo.getKoboSingleArticle());

			order.setTaxPrecent(orderWithInfo.getTaxPrecent());
			order.setGrandTotal(orderWithInfo.getGrandTotal());
			order.setTaxAmount(orderWithInfo.getTaxAmount());
			order.setShippingAmount(orderWithInfo.getShippingAmount());
			// calulated values

			order.setCalculatedGrandTotal(orderWithInfo.getCalculatedGrandTotal());
			order.setCalculatedTaxAmount(orderWithInfo.getCalculatedTaxAmount());
			order.setVatNumber(orderWithInfo.getVatNumber());
			order.setBanckAccountNumber(orderWithInfo.getBanckAccountNumber());
			order.setSmallBusinessMan(orderWithInfo.getSmallBusinessMan());

		}

		return orderList;

	}

	public static List<DBOrderModel> getPartyOrdersList(String partyId) {

		List<DBOrderModel> orderList = new ArrayList<DBOrderModel>();

		try {
			SOAPMessage response = soapGetPartyOrdersList(partyId);
			System.out.println(response);
			try {
				orderList = extractOrderData(response);
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (SOAPException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return orderList;
	}

	public static SOAPMessage soapGetOrdersList(String stylistId) throws SOAPException, IOException {
		String sessID = HttpSoapConnector.performLogin();
		SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
		SOAPConnection soapConnection = soapConnectionFactory.createConnection();

		SOAPMessage soapResponse = soapConnection.call(getOrdersListRequest(sessID, stylistId),
				EnvironmentConstants.SOAP_URL + UrlConstants.API_URI);

		// SOAPMessage soapResponse =
		// soapConnection.call(getOrdersListRequest(sessID, stylistId),
		// "http://aut-pippajean.evozon.com/" + UrlConstants.API_URI);
		// SOAPMessage soapResponse =
		// soapConnection.call(getOrdersListRequest(sessID, stylistId),
		// "https://pippajean-upgrade.evozon.com/" + UrlConstants.API_URI);

		return soapResponse;
	}

	public static SOAPMessage soapGetOrdersRangeList(String orderLimit1, String orderLimit2)
			throws SOAPException, IOException {
		String sessID = HttpSoapConnector.performLogin();
		SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
		SOAPConnection soapConnection = soapConnectionFactory.createConnection();

		SOAPMessage soapResponse = soapConnection.call(getOrdersListRangeRequest(sessID, orderLimit1, orderLimit2),
				EnvironmentConstants.SOAP_URL + UrlConstants.API_URI);

		// SOAPMessage soapResponse =
		// soapConnection.call(getOrdersListRangeRequest(sessID, orderLimit1,
		// orderLimit2),
		// "http://aut-pippajean.evozon.com/" + UrlConstants.API_URI);

		// SOAPMessage soapResponse =
		// soapConnection.call(getOrdersListRangeRequest(sessID, orderLimit1,
		// orderLimit2),
		// "https://pippajean-upgrade.evozon.com/" + UrlConstants.API_URI);

		return soapResponse;
	}

	public static SOAPMessage soapGetPartyOrdersList(String partyId) throws SOAPException, IOException {
		String sessID = HttpSoapConnector.performLogin();
		SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
		SOAPConnection soapConnection = soapConnectionFactory.createConnection();
		SOAPMessage soapResponse = soapConnection.call(getPartyOrdersListRequest(sessID, partyId),
				EnvironmentConstants.SOAP_URL + UrlConstants.API_URI);
		// SOAPMessage soapResponse =
		// soapConnection.call(getPartyOrdersListRequest(sessID, partyId),
		// "https://pippajean-upgrade.evozon.com/" + UrlConstants.API_URI);

		return soapResponse;
	}

	public static void addOrderFilter(SOAPElement complexFilter, String filterKey, String operator, String filterValue)
			throws SOAPException {

		SOAPElement complexObjectArray = complexFilter.addChildElement(SoapKeys.COMPLEX_OBJECT_ARRAY);
		SOAPElement key = complexObjectArray.addChildElement(SoapKeys.KEY);
		key.addTextNode(filterKey);
		SOAPElement value = complexObjectArray.addChildElement(SoapKeys.VALUE);
		SOAPElement key2 = value.addChildElement(SoapKeys.KEY);
		key2.addTextNode(operator);
		SOAPElement value2 = value.addChildElement(SoapKeys.VALUE);
		value2.addTextNode(filterValue);
	}

	private static SOAPMessage getOrdersListRequest(String ssID, String stylistId) throws SOAPException, IOException {
		SOAPMessage soapMessage = HttpSoapConnector.createSoapDefaultMessage();

		SOAPBody soapBody = soapMessage.getSOAPPart().getEnvelope().getBody();
		SOAPElement getStylistRequestParam = soapBody.addChildElement(SoapKeys.ORDERS_LIST, SoapKeys.URN_PREFIX);

		SOAPElement sessionID = getStylistRequestParam.addChildElement(SoapKeys.SESSION_ID);
		sessionID.addTextNode(ssID);

		SOAPElement filters = getStylistRequestParam.addChildElement(SoapKeys.FILTERS);
		SOAPElement complexFilter = filters.addChildElement(SoapKeys.COMPLEX_FILTER);
		addOrderFilter(complexFilter, SoapConstants.STYLIST_ID_FILTER, SoapConstants.EQUAL, stylistId);
		addOrderFilter(complexFilter, SoapConstants.SOAP_CREATED_AT_FILTER, SoapConstants.GREATER_THAN,
				"2016-08-01 00:00:00");

		SOAPElement clinetKey = getStylistRequestParam.addChildElement(SoapKeys.CLIENT_KEY);
		clinetKey.addTextNode(SoapKeys.CLIENT_KEY_VALUE);

		soapMessage.saveChanges();

		System.out.print("Request SOAP Message:");
		soapMessage.writeTo(System.out);
		System.out.println();

		return soapMessage;
	}

	private static SOAPMessage getOrdersListRangeRequest(String ssID, String orderLimit1, String orderLimit2)
			throws SOAPException, IOException {
		SOAPMessage soapMessage = HttpSoapConnector.createSoapDefaultMessage();

		SOAPBody soapBody = soapMessage.getSOAPPart().getEnvelope().getBody();
		SOAPElement getStylistRequestParam = soapBody.addChildElement(SoapKeys.ORDERS_LIST, SoapKeys.URN_PREFIX);

		SOAPElement sessionID = getStylistRequestParam.addChildElement(SoapKeys.SESSION_ID);
		sessionID.addTextNode(ssID);

		SOAPElement filters = getStylistRequestParam.addChildElement(SoapKeys.FILTERS);
		SOAPElement complexFilter = filters.addChildElement(SoapKeys.COMPLEX_FILTER);

//		addOrderFilter(complexFilter, "increment_id", "from", orderLimit1);
//		addOrderFilter(complexFilter, "increment_id", "to", orderLimit2);
		addOrderFilter(complexFilter, "order_id", "from", orderLimit1);
		addOrderFilter(complexFilter, "order_id", "to", orderLimit2);
		//addOrderFilter(complexFilter, "status", "in", "payment_complete,complete,canceled_system");
		addOrderFilter(complexFilter, "status", "in", "payment_complete,complete");
		SOAPElement clinetKey = getStylistRequestParam.addChildElement(SoapKeys.CLIENT_KEY);
		clinetKey.addTextNode(SoapKeys.CLIENT_KEY_VALUE);

		soapMessage.saveChanges();

		System.out.print("Request SOAP Message:");
		soapMessage.writeTo(System.out);
		System.out.println();

		return soapMessage;
	}

	private static SOAPMessage getPartyOrdersListRequest(String ssID, String partyId)
			throws SOAPException, IOException {
		SOAPMessage soapMessage = HttpSoapConnector.createSoapDefaultMessage();

		SOAPBody soapBody = soapMessage.getSOAPPart().getEnvelope().getBody();
		SOAPElement getStylistRequestParam = soapBody.addChildElement(SoapKeys.ORDERS_LIST, SoapKeys.URN_PREFIX);

		SOAPElement sessionID = getStylistRequestParam.addChildElement(SoapKeys.SESSION_ID);
		sessionID.addTextNode(ssID);

		SOAPElement filters = getStylistRequestParam.addChildElement(SoapKeys.FILTERS);

		SOAPElement complexFilter = filters.addChildElement(SoapKeys.COMPLEX_FILTER);
		SOAPElement complexObjectArray = complexFilter.addChildElement(SoapKeys.COMPLEX_OBJECT_ARRAY);
		SOAPElement key = complexObjectArray.addChildElement(SoapKeys.KEY);
		key.addTextNode(SoapConstants.STYLE_PARTY_ID_FILTER);
		SOAPElement value = complexObjectArray.addChildElement(SoapKeys.VALUE);
		SOAPElement key2 = value.addChildElement(SoapKeys.KEY);
		key2.addTextNode(SoapConstants.EQUAL);
		SOAPElement value2 = value.addChildElement(SoapKeys.VALUE);
		value2.addTextNode(partyId);

		soapMessage.saveChanges();

		System.out.print("Request SOAP Message:");
		soapMessage.writeTo(System.out);
		System.out.println();

		return soapMessage;
	}

	private static List<DBOrderModel> extractOrderData(SOAPMessage response) throws Exception {

		List<DBOrderModel> orderModelList = new ArrayList<DBOrderModel>();

		NodeList orderList = response.getSOAPBody().getElementsByTagName("complexObjectArray");
		for (int i = 0; i < orderList.getLength(); i++) {
			String customerName = "";
			if (orderList.item(i).getParentNode().getNodeName().equalsIgnoreCase("result")) {
				DBOrderModel model = OrderInfoMagCalls.defaultDBOrderModelValues();
				model.setTotalIp("0");
				model.setPaidAt("2010-01-21 11:45:03");
				model.setStylePartyId("0");
				String cartType = "";
				String orderType = "";

				NodeList childNodes = orderList.item(i).getChildNodes();
				for (int j = 0; j < childNodes.getLength(); j++) {

					if (childNodes.item(j).getNodeName().equalsIgnoreCase("increment_id")) {
						model.setIncrementId(childNodes.item(j).getTextContent());
					}

					if (childNodes.item(j).getNodeName().equalsIgnoreCase("grand_total")) {

						model.setGrandTotal(childNodes.item(j).getTextContent());
					}
					if (childNodes.item(j).getNodeName().equalsIgnoreCase("created_at")) {
						model.setCreatedAt(childNodes.item(j).getTextContent());
					}
					if (childNodes.item(j).getNodeName().equalsIgnoreCase("status")) {
						model.setStatus(childNodes.item(j).getTextContent());
					}
					if (childNodes.item(j).getNodeName().equalsIgnoreCase("payment_complete_at")) {
						model.setPaidAt(childNodes.item(j).getTextContent());
					}
					if (childNodes.item(j).getNodeName().equalsIgnoreCase("stylist_id")) {
						model.setStylistId(childNodes.item(j).getTextContent());
					}
					if (childNodes.item(j).getNodeName().equalsIgnoreCase("total_ip")) {
						model.setTotalIp(childNodes.item(j).getTextContent());
					}
					if (childNodes.item(j).getNodeName().equalsIgnoreCase("order_id")) {
						model.setOrderId(childNodes.item(j).getTextContent());
					}

					if (childNodes.item(j).getNodeName().equalsIgnoreCase("order_type")) {
						model.setOrderType(childNodes.item(j).getTextContent());
					}
					if (childNodes.item(j).getNodeName().equalsIgnoreCase("cart_type")) {
						model.setCartType(childNodes.item(j).getTextContent());
					}
					if (childNodes.item(j).getNodeName().equalsIgnoreCase("term_purchase_type")) {
						model.setTermPurchaseType(childNodes.item(j).getTextContent());
					}

					if (childNodes.item(j).getNodeName().equalsIgnoreCase("shipping_incl_tax")) {
						model.setShippingAmount(childNodes.item(j).getTextContent());
					}

					if (childNodes.item(j).getNodeName().equalsIgnoreCase("customer_id")) {
						model.setCustomerId(childNodes.item(j).getTextContent());
					}
					if (childNodes.item(j).getNodeName().equalsIgnoreCase("stylist_customer_id")) {
						model.setStylistCustomerId(childNodes.item(j).getTextContent());
					}

					if (childNodes.item(j).getNodeName().equalsIgnoreCase("payment_method_type")) {
						if (!childNodes.item(j).getTextContent().replaceAll("\\s+", "").contentEquals("")) {
							model.setPaymentMethodTypet(childNodes.item(j).getTextContent());

						}
					}

					if (childNodes.item(j).getNodeName().equalsIgnoreCase("shipping_type")) {
						model.setShippingType(childNodes.item(j).getTextContent());
					}

					// if
					// (childNodes.item(j).getNodeName().equalsIgnoreCase("style_party_id"))
					// {
					// System.out.println(" ce cauti aici ??");
					// model.setStylePartyId(childNodes.item(j).getTextContent());
					// }

					if (childNodes.item(j).getNodeName().equalsIgnoreCase("is_preshipped")) {
						model.setIsPreshipped(childNodes.item(j).getTextContent());
					}

					// if
					// (childNodes.item(j).getNodeName().equalsIgnoreCase("is_pom"))
					// {
					// model.setIsPom(childNodes.item(j).getTextContent());
					// }
					if (childNodes.item(j).getNodeName().equalsIgnoreCase("website_code")) {
						model.setWebsiteCode(childNodes.item(j).getTextContent());
					}

					if (childNodes.item(j).getNodeName().equalsIgnoreCase("store_language")) {
						String storeLanguage = childNodes.item(j).getTextContent().toUpperCase();

						model.setStoreLanguage(childNodes.item(j).getTextContent());
						switch (childNodes.item(j).getTextContent().toUpperCase()) {
						case "DE":
							model.setLanguageCode(storeLanguage + "U");
							break;

						case "ES":
							model.setLanguageCode(storeLanguage + "P");
							break;

						case "EN":
							model.setLanguageCode(storeLanguage + "U");
							break;
						default:
							break;
						}
					}

					if (childNodes.item(j).getNodeName().equalsIgnoreCase("firstname")) {
						customerName = customerName.concat(childNodes.item(j).getTextContent() + " ");
					}

					if (childNodes.item(j).getNodeName().equalsIgnoreCase("lastname")) {
						customerName = customerName.concat(childNodes.item(j).getTextContent());
					}

					// if
					// (childNodes.item(j).getNodeName().equalsIgnoreCase("tax_amount"))
					// {
					// model.setTaxAmount(childNodes.item(j).getTextContent());
					// }

					if (childNodes.item(j).getNodeName().equalsIgnoreCase("subtotal")) {
						model.setBaseSubtotal(childNodes.item(j).getTextContent());
					}

					if (childNodes.item(j).getNodeName().equalsIgnoreCase("scheduled_delivery_date")) {
						model.setScheduledDeliveryDate(childNodes.item(j).getTextContent());
					}

					if (childNodes.item(j).getNodeName().equalsIgnoreCase("order_customer_email")) {
						model.setOrderCustomerEmail(childNodes.item(j).getTextContent());
					}
					if (childNodes.item(j).getNodeName().equalsIgnoreCase("contact_customer_id")) {
						model.setContactid(childNodes.item(j).getTextContent());
					}

					if ((model.getOrderType().contentEquals("3") && model.getCartType().contentEquals("null"))
							|| (model.getOrderType().contentEquals("3") && model.getCartType().contentEquals("1"))) {

						model.setCartType("0");

					}

					

					// model.setCustomerName(customerName);
				}
				orderModelList.add(model);
			}
		}
		return orderModelList;
	}

	public static void main(String args[]) throws SOAPException, IOException {
		// getOrderWithItems("212953","212953");

		// System.out.println(OrdersInfoMagentoCalls.getPartyOrdersList("14830").get(0).getIncrementId());
		// List<DBOrderModel>
		// list=OrdersInfoMagentoCalls.getOrderWithItems("212468","212468");
		//
		//
		// for (DBOrderModel dbOrderModel : list) {
		// System.out.println(dbOrderModel.getIncrementId());
		//
		// List<SalesOrderInfoModel> listmod= dbOrderModel.getItemInfo();
		// for (SalesOrderInfoModel salesOrderInfoModel : listmod) {
		// System.out.println(salesOrderInfoModel.getSku());
		// }
		//
		//
		// }

		List<DBOrderModel> dbmodel = OrdersInfoMagentoCalls.getOrderWithItems("10021787500", "10021787500");
		// ("213473", "213474");

		for (DBOrderModel dbOrderModel : dbmodel) {
			System.out.println("open order" + dbOrderModel.getIncrementId());
			System.out.println("f name  " + dbOrderModel.getCustomerFirstName());
			System.out.println("kobo " + dbOrderModel.getKoboSingleArticle());
			System.out.println("customer name " + dbOrderModel.getCustomerName());
			System.out.println("getIncrementId  : " + dbOrderModel.getIncrementId());
			System.out.println("getGrandTotal  : " + dbOrderModel.getGrandTotal());
			System.out.println("getCreatedAt : " + dbOrderModel.getCreatedAt());
			System.out.println("getBillToStreetAddress : " + dbOrderModel.getBillToStreetAddress());
			System.out.println("getStatus : " + dbOrderModel.getStatus());
			System.out.println("getPaidAt : " + dbOrderModel.getPaidAt());

			System.out.println("getStylistId  : " + dbOrderModel.getStylistId());
			System.out.println("getTotalIp  : " + dbOrderModel.getTotalIp());
			System.out.println("getCreatedAt  : " + dbOrderModel.getCreatedAt());
			System.out.println("getOrderId : " + dbOrderModel.getOrderId());
			System.out.println("getOrderType : " + dbOrderModel.getOrderType());
			System.out.println("aaaaaaaaaaagetCartType : " + dbOrderModel.getCartType());

			System.out.println("getTermPurchaseType : " + dbOrderModel.getTermPurchaseType());
			System.out.println("getShippingAmount : " + dbOrderModel.getShippingAmount());
			System.out.println("getCustomerId : " + dbOrderModel.getCustomerId());
			System.out.println("getOrderId : " + dbOrderModel.getOrderId());
			System.out.println("getStylistCustomerId: " + dbOrderModel.getStylistCustomerId());
			System.out.println("getPaymentMethodTypet: " + dbOrderModel.getPaymentMethodTypet());

			System.out.println("getShippingType : " + dbOrderModel.getShippingType());
			System.out.println("getStylePartyId : " + dbOrderModel.getStylePartyId());
			System.out.println("getIsPreshipped: " + dbOrderModel.getIsPreshipped());
			System.out.println("getIsPom: " + dbOrderModel.getIsPom());
			System.out.println("getWebsiteCode : " + dbOrderModel.getWebsiteCode());
			System.out.println("getStoreLanguage : " + dbOrderModel.getStoreLanguage());

			System.out.println("getCustomerFirstName  : " + dbOrderModel.getCustomerFirstName());
			System.out.println("getCustomerLastName  : " + dbOrderModel.getCustomerLastName());
			System.out.println("getTaxAmount: " + dbOrderModel.getTaxAmount());
			System.out.println("getBaseSubtotal : " + dbOrderModel.getBaseSubtotal());

			System.out.println("bill post  : " + dbOrderModel.getBillToPostcode());
			System.out.println("bill fname  : " + dbOrderModel.getBillToFirstName());
			System.out.println("billl lname : " + dbOrderModel.getBillToLastName());
			System.out.println("bill street : " + dbOrderModel.getBillToStreetAddress());
			System.out.println("ship city : " + dbOrderModel.getBillToCity());
			System.out.println("ship country id : " + dbOrderModel.getBillCountryId());

			// shipp

			System.out.println("shipp post : " + dbOrderModel.getShipToPostcode());
			System.out.println("fname  : " + dbOrderModel.getShipToFirstName());
			System.out.println("ship lname : " + dbOrderModel.getShipToLastName());
			System.out.println("ship street : " + dbOrderModel.getShipToStreetAddress());
			System.out.println("ship city : " + dbOrderModel.getShipToCity());
			System.out.println("ship country id : " + dbOrderModel.getShipCountryId());
			System.out.println("ship house no: " + dbOrderModel.getShipToHousNumber());
			System.out.println("order customer email" + dbOrderModel.getOrderCustomerEmail());
			System.out.println("vat number" + dbOrderModel.getVatNumber());
			System.out.println("small b" + dbOrderModel.getSmallBusinessMan());
			System.out.println("banck accout" + dbOrderModel.getBanckAccountNumber());
			System.out.println("langCode" + dbOrderModel.getLanguageCode());
			// List<SalesOrderInfoModel> list = dbOrderModel.getItemInfo();
			// for (SalesOrderInfoModel salesOrderInfoModel : list) {
			// System.out.println("sku: " + salesOrderInfoModel.getSku());
			// }
		}

	}

}
