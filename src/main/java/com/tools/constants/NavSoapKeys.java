package com.tools.constants;

import com.tools.persistance.MongoReader;

public class NavSoapKeys {
	
	// General keys
	public static final String SALES_ORDER_URN_PREFIX = "sal";
	public static final String BILL_OF_MATERIAL_URN_PREFIX = "bil";
	public static final String INVENTORY_SYNC_URN_PREFIX = "inv";
	public static final String SOAP_PREFIX = "soapenv";
	public static final String SALES_ORDER_SERVER_URI = "urn:microsoft-dynamics-schemas/page/salesorder";
	public static final String BILL_OF_MATERIAL_SERVER_URI = "urn:microsoft-dynamics-schemas/page/billofmaterial";
	public static final String INVENTORY_SYNC_SERVER_URI = "urn:microsoft-dynamics-schemas/page/invsynclogentries";
	public static final String READ_MULTIPLE = "ReadMultiple";
	public static final String FILTER = "filter";
	public static final String FIELD = "Field";
	public static final String CRITERIA = "Criteria";
	public static final String NO = "No";
	
	// pippajean-upgrade or staging-aut
//	public static final String SALES_ORDER_API_URI = MongoReader.geteEnvironmentConstantsItem("SALES_ORDER_API_URI");
//	public static final String BILL_OF_MATERIAL_API_URI=MongoReader.geteEnvironmentConstantsItem("BILL_OF_MATERIAL_API_URI");
//	public static final String INVENTORY_SYNC_API_URI=MongoReader.geteEnvironmentConstantsItem("INVENTORY_SYNC_API_URI");
	
	
	//Cloud
//	public static final String SALES_ORDER_API_URI = "http://185.48.116.231:9918/ErpPippaAut/WS/PippaJean/Page/SalesOrder";
//	public static final String BILL_OF_MATERIAL_API_URI = "http://185.48.116.231:9918/ErpPippaAut/WS/PippaJean/Page/BillOfMaterial";
//	public static final String INVENTORY_SYNC_API_URI = "http://185.48.116.231:9918/ErpPippaAut/WS/PippaJean/Page/InvSyncLogEntries";
	

	// staging-aut  - >ErpPippaTestGoLive - Cloud Go live
	public static final String SALES_ORDER_API_URI = "http://185.48.116.231:9953/ErpPippaTestGoLive/WS/PippaJean/Page/SalesOrder";
	public static final String BILL_OF_MATERIAL_API_URI = "http://185.48.116.231:9953/ErpPippaTestGoLive/WS/PippaJean/Page/BillOfMaterial";
	public static final String INVENTORY_SYNC_API_URI = "http://185.48.116.231:9953/ErpPippaTestGoLive/WS/PippaJean/Page/InvSyncLogEntries";
}
