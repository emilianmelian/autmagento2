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

import org.w3c.dom.NodeList;

import com.tools.constants.EnvironmentConstants;
import com.tools.constants.SoapConstants;
import com.tools.constants.SoapKeys;
import com.tools.constants.UrlConstants;
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
			
			//billing address details
			order.setItemInfo(orderWithInfo.getItemInfo());
			order.setBillToFirstName(orderWithInfo.getBillToFirstName());
			order.setBillToLastName(orderWithInfo.getBillToLastName());
			order.setBillToStreetAddress(orderWithInfo.getBillToStreetAddress());
			order.setBillToCity(orderWithInfo.getBillToCity());
			order.setBillCountryId(orderWithInfo.getBillCountryId());
			order.setBillToPostcode(orderWithInfo.getBillToPostcode());

			//shipping address details
			order.setShipToFirstName(orderWithInfo.getShipToFirstName());
			order.setShipToLastName(orderWithInfo.getShipToLastName());
			order.setShipToStreetAddress(orderWithInfo.getShipToStreetAddress());
			order.setShipToCity(orderWithInfo.getShipToCity());
			order.setShipCountryId(orderWithInfo.getShipCountryId());
			order.setShipToPostcode(orderWithInfo.getShipToPostcode());
			
			//
			order.setUpdatedNav(orderWithInfo.getUpdatedNav());
			order.setOrderCurrencyCode(orderWithInfo.getOrderCurrencyCode());
		//	order.setStylePartyId(orderWithInfo.getStylePartyId());
			order.setKoboSingleArticle(orderWithInfo.getKoboSingleArticle());
			
			order.setTaxPrecent(orderWithInfo.getTaxPrecent());
			
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



//		 SOAPMessage soapResponse =
//		 soapConnection.call(getOrdersListRequest(sessID, stylistId),
//		 "http://aut-pippajean.evozon.com/" + UrlConstants.API_URI);
//		 SOAPMessage soapResponse =
//				 soapConnection.call(getOrdersListRequest(sessID, stylistId),
//				 "https://pippajean-upgrade.evozon.com/" + UrlConstants.API_URI);

	


		return soapResponse;
	}

	public static SOAPMessage soapGetOrdersRangeList(String orderLimit1, String orderLimit2)
			throws SOAPException, IOException {
		String sessID = HttpSoapConnector.performLogin();
		SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
		SOAPConnection soapConnection = soapConnectionFactory.createConnection();

		SOAPMessage soapResponse = soapConnection.call(getOrdersListRangeRequest(sessID, orderLimit1, orderLimit2),
				EnvironmentConstants.SOAP_URL + UrlConstants.API_URI);

//		 SOAPMessage soapResponse =
//		 soapConnection.call(getOrdersListRangeRequest(sessID, orderLimit1, orderLimit2),
//		 "http://aut-pippajean.evozon.com/" + UrlConstants.API_URI);

//		 SOAPMessage soapResponse =
//				 soapConnection.call(getOrdersListRangeRequest(sessID, orderLimit1, orderLimit2),
//				 "https://pippajean-upgrade.evozon.com/" + UrlConstants.API_URI);

		return soapResponse;
	}

	public static SOAPMessage soapGetPartyOrdersList(String partyId) throws SOAPException, IOException {
		String sessID = HttpSoapConnector.performLogin();
		SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
		SOAPConnection soapConnection = soapConnectionFactory.createConnection();
		SOAPMessage soapResponse = soapConnection.call(getPartyOrdersListRequest(sessID, partyId),
				EnvironmentConstants.SOAP_URL + UrlConstants.API_URI);
//		 SOAPMessage soapResponse =
//		 soapConnection.call(getPartyOrdersListRequest(sessID, partyId),
//		 "https://pippajean-upgrade.evozon.com/" + UrlConstants.API_URI);

		return soapResponse;
	}

	private static SOAPMessage getOrdersListRequest(String ssID, String stylistId) throws SOAPException, IOException {
		SOAPMessage soapMessage = HttpSoapConnector.createSoapDefaultMessage();

		SOAPBody soapBody = soapMessage.getSOAPPart().getEnvelope().getBody();
		SOAPElement getStylistRequestParam = soapBody.addChildElement(SoapKeys.ORDERS_LIST, SoapKeys.URN_PREFIX);

		SOAPElement sessionID = getStylistRequestParam.addChildElement(SoapKeys.SESSION_ID);
		sessionID.addTextNode(ssID);

		SOAPElement filters = getStylistRequestParam.addChildElement(SoapKeys.FILTERS);
		SOAPElement complexFilter = filters.addChildElement(SoapKeys.COMPLEX_FILTER);
		SOAPElement complexObjectArray = complexFilter.addChildElement(SoapKeys.COMPLEX_OBJECT_ARRAY);
		SOAPElement key = complexObjectArray.addChildElement(SoapKeys.KEY);
		key.addTextNode(SoapConstants.STYLIST_ID_FILTER);
		SOAPElement value = complexObjectArray.addChildElement(SoapKeys.VALUE);
		SOAPElement key2 = value.addChildElement(SoapKeys.KEY);
		key2.addTextNode(SoapConstants.EQUAL);
		SOAPElement value2 = value.addChildElement(SoapKeys.VALUE);
		value2.addTextNode(stylistId);

		SOAPElement complexObjectArrayB = complexFilter.addChildElement(SoapKeys.COMPLEX_OBJECT_ARRAY);
		SOAPElement keyB = complexObjectArrayB.addChildElement(SoapKeys.KEY);
		keyB.addTextNode(SoapConstants.SOAP_CREATED_AT_FILTER);
		SOAPElement valueB = complexObjectArrayB.addChildElement(SoapKeys.VALUE);
		SOAPElement key2B = valueB.addChildElement(SoapKeys.KEY);
		key2B.addTextNode(SoapConstants.GREATER_THAN);
		SOAPElement value2B = valueB.addChildElement(SoapKeys.VALUE);
		value2B.addTextNode("2016-08-01 00:00:00");

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
		SOAPElement complexObjectArray = complexFilter.addChildElement(SoapKeys.COMPLEX_OBJECT_ARRAY);
		SOAPElement key = complexObjectArray.addChildElement(SoapKeys.KEY);
		key.addTextNode("order_id");
		SOAPElement value = complexObjectArray.addChildElement(SoapKeys.VALUE);
		SOAPElement key2 = value.addChildElement(SoapKeys.KEY);
		key2.addTextNode("gteg");
		SOAPElement value2 = value.addChildElement(SoapKeys.VALUE);
		value2.addTextNode(orderLimit1);

		SOAPElement complexObjectArrayB = complexFilter.addChildElement(SoapKeys.COMPLEX_OBJECT_ARRAY);
		SOAPElement keyB = complexObjectArrayB.addChildElement(SoapKeys.KEY);
		keyB.addTextNode("order_id");
		SOAPElement valueB = complexObjectArrayB.addChildElement(SoapKeys.VALUE);
		SOAPElement key2B = valueB.addChildElement(SoapKeys.KEY);
		key2B.addTextNode("lteg");
		SOAPElement value2B = valueB.addChildElement(SoapKeys.VALUE);
		value2B.addTextNode(orderLimit2);

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
			String customerName="";
			if (orderList.item(i).getParentNode().getNodeName().equalsIgnoreCase("result")) {
				DBOrderModel model = new DBOrderModel();
				model.setTotalIp("0");
				model.setPaidAt("2010-01-21 11:45:03");
				model.setStylePartyId("0");

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
						model.setPaymentMethodTypet(childNodes.item(j).getTextContent());
					}
					
					if (childNodes.item(j).getNodeName().equalsIgnoreCase("shipping_type")) {
						model.setShippingType(childNodes.item(j).getTextContent());
					}
					
//					if (childNodes.item(j).getNodeName().equalsIgnoreCase("style_party_id")) {
//						System.out.println(" ce cauti aici ??");
//						model.setStylePartyId(childNodes.item(j).getTextContent());
//					}
					
					if (childNodes.item(j).getNodeName().equalsIgnoreCase("is_preshipped")) {
						model.setIsPreshipped(childNodes.item(j).getTextContent());
					}
					
					if (childNodes.item(j).getNodeName().equalsIgnoreCase("is_pom")) {
						model.setIsPom(childNodes.item(j).getTextContent());
					}
					if (childNodes.item(j).getNodeName().equalsIgnoreCase("website_code")) {
						model.setWebsiteCode(childNodes.item(j).getTextContent());
					}
					
					if (childNodes.item(j).getNodeName().equalsIgnoreCase("store_language")) {
						model.setStoreLanguage(childNodes.item(j).getTextContent());
					}
					
					if (childNodes.item(j).getNodeName().equalsIgnoreCase("customer_firstname")) {
						customerName=customerName.concat(childNodes.item(j).getTextContent()+" ");
					}
					
					if (childNodes.item(j).getNodeName().equalsIgnoreCase("customer_lastname")) {
						customerName=customerName.concat(childNodes.item(j).getTextContent());
					}
					
					if (childNodes.item(j).getNodeName().equalsIgnoreCase("tax_amount")) {
						model.setTaxAmount(childNodes.item(j).getTextContent());
					}
					
					
					if (childNodes.item(j).getNodeName().equalsIgnoreCase("subtotal")) {
						model.setBaseSubtotal(childNodes.item(j).getTextContent());
					}
					

					if (childNodes.item(j).getNodeName().equalsIgnoreCase("scheduled_delivery_date")) {
						model.setScheduledDeliveryDate(childNodes.item(j).getTextContent());
					}

					model.setCustomerName(customerName);
				}
				orderModelList.add(model);
			}
		}
		return orderModelList;
	}

	 public static void main(String args[]) throws SOAPException, IOException {
	 
	 }

}
