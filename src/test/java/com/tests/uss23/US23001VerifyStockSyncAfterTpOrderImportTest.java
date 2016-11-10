package com.tests.uss23;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Story;
import net.thucydides.core.annotations.WithTag;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.connectors.http.MagentoProductCalls;
import com.connectors.navSqlServer.NavQueries;
import com.tests.BaseTest;
import com.tools.CustomVerification;
import com.tools.constants.SoapKeys;
import com.tools.data.backend.OrderModel;
import com.tools.data.navision.OrderStatusModel;
import com.tools.data.navision.SyncInfoModel;
import com.tools.generalCalculation.StockCalculations;
import com.tools.persistance.MongoReader;
import com.tools.requirements.Application;
import com.tools.utils.DateUtils;
import com.workflows.stockSynk.StockProductsValidations;
import com.workflows.stockSynk.StockSyncValidations;

@WithTag(name = "US23.1 Stock Sync", type = "Scenarios")
@Story(Application.StockSync.US23_1.class)
@RunWith(SerenityRunner.class)
public class US23001VerifyStockSyncAfterTpOrderImportTest extends BaseTest {

	@Steps
	StockSyncValidations stockSyncValidations;
	@Steps
	public CustomVerification customVerifications;
	@Steps
	StockProductsValidations stockProductsValidations;

	private List<String> boughtProductsQuantities = new ArrayList<String>();

	List<SyncInfoModel> initialChangingMagentoProducts = new ArrayList<SyncInfoModel>();
	List<SyncInfoModel> initialChangingNavProducts = new ArrayList<SyncInfoModel>();

	private List<SyncInfoModel> changingStockMagentoProducts = new ArrayList<SyncInfoModel>();
	private List<SyncInfoModel> changingStockNavProduct = new ArrayList<SyncInfoModel>();

	private static List<String> changingStockIdList = new ArrayList<String>(Arrays.asList("1292", "1658", "2558", "1872", "2552"));
	private static List<String> changingStockSkuList = new ArrayList<String>(Arrays.asList("R065SV-18", "N093SV", "N052NL", "N094SV", "B098BK"));

	private OrderModel orderModel;
	boolean isSyncronyzed = false;

	@Before
	public void setUp() throws Exception {

		orderModel = MongoReader.getOrderModel("US23001PlaceTermPurchaseOrderTest").get(0);
		OrderStatusModel orderStatusModel = NavQueries.getProductSyncronizedStatus(orderModel.getOrderId().toUpperCase());
		String[] parts = orderStatusModel.getSyncDate().split(Pattern.quote("."));
		String syncDate = parts[0];

		initialChangingMagentoProducts = MongoReader.grabStockInfoModel("US23001GetMagAndNavStockBerforeTpOrderTest" + SoapKeys.MAGENTO_INITIAL_CHANGING_STOCK);
		initialChangingNavProducts = MongoReader.grabStockInfoModel("US23001GetMagAndNavStockBerforeTpOrderTest" + SoapKeys.NAVISION_INITIAL_CHANGING_STOCK);
		boughtProductsQuantities = MongoReader.grabStringValue("US23001PlaceTermPurchaseOrderTest");

		initialChangingNavProducts = StockCalculations.calculateNewStock(initialChangingNavProducts, boughtProductsQuantities);

		for (String id : changingStockIdList) {
			changingStockMagentoProducts.add(MagentoProductCalls.getMagProductInfo(id));
		}
		for (String sku : changingStockSkuList) {
			String[] skuParts = sku.split("-");
			changingStockNavProduct.add(NavQueries.getSyncProductInfo(skuParts[0], skuParts.length == 1 ? "" : skuParts[1]));
		}

		changingStockMagentoProducts = StockCalculations.calculateStockBasedOnPendingOrders(changingStockMagentoProducts);

		if (DateUtils.isDateAfter(DateUtils.getCurrentDateTwoHoursBack("YYYY-MM-dd HH:mm:ss"), syncDate, "YYYY-MM-dd HH:mm:ss")
				&& orderStatusModel.getSyncStatus().contentEquals("Yes")) {
			isSyncronyzed = true;
		}
	}

	@Test
	public void us23001VerifyStockSyncAfterTpOrderImportTest() throws SQLException {

		stockProductsValidations.validateSyncronizedStatus(isSyncronyzed);

		stockSyncValidations.setValidateProductsModels(initialChangingNavProducts, changingStockNavProduct);
		stockSyncValidations.validateProducts("VALIDATE NAVISION STOCK IS DECREASED - CHANGING STOCK NAVISION PRODUCTS");

		stockSyncValidations.setValidateProductsModels(changingStockNavProduct, changingStockMagentoProducts);
		stockSyncValidations.validateProducts("VALIDATE MAGENTO STOCK IS SYNCRONIZED WITH MAGENTO STOCK - CHANGING STOCK PRODUCTS");

		customVerifications.printErrors();
	}

}