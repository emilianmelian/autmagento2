package com.tests.uss34.us34003;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.connectors.mongo.MongoConnector;
import com.steps.backend.BackEndSteps;
import com.steps.backend.OrdersSteps;
import com.steps.backend.validations.OrderValidationSteps;
import com.tests.BaseTest;
import com.tools.CustomVerification;
import com.tools.cartcalculations.smf.CartCalculator;
import com.tools.constants.Credentials;
import com.tools.constants.SoapKeys;
import com.tools.data.CalcDetailsModel;
import com.tools.data.backend.OrderItemModel;
import com.tools.data.backend.OrderModel;
import com.tools.data.backend.OrderTotalsModel;
import com.tools.data.frontend.BasicProductModel;
import com.tools.data.frontend.ShippingModel;
import com.tools.persistance.MongoReader;
import com.tools.requirements.Application;
import com.workflows.backend.OrderProductsWorkflows;
import com.workflows.backend.OrderWorkflows;

import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Story;


@Story(Application.Newsletter.US15_4.class)
@RunWith(SerenityRunner.class)
public class US34003CreatePartialCreditMemoOnOrderTestVDV extends BaseTest {

	@Steps
	public BackEndSteps backEndSteps;
	@Steps
	public OrdersSteps ordersSteps;

	@Steps
	public OrderValidationSteps orderValidationSteps;
	@Steps
	public OrderWorkflows orderWorkflows;
	@Steps
	public OrderProductsWorkflows orderProductsWorkflows;
	@Steps
	public CustomVerification customVerifications;
	
	private String orderId;
	public static List<BasicProductModel> productsList = new ArrayList<BasicProductModel>();
	public static List<BasicProductModel> productsListWithDiscount = new ArrayList<BasicProductModel>();

	public static List<CalcDetailsModel> calcDetailsModelList = new ArrayList<CalcDetailsModel>();
	private static OrderTotalsModel orderTotalsModel = new OrderTotalsModel();
	private static OrderTotalsModel shopTotalsModel = new OrderTotalsModel();
	private static OrderTotalsModel shopTotalsModel1 = new OrderTotalsModel();
	private static List<BasicProductModel> recalculatedProductsList = new ArrayList<BasicProductModel>();

	
	
	private static List<ShippingModel> shippingModelList = new ArrayList<ShippingModel>();


	@Before
	public void setUp() throws Exception {

		List<OrderModel> orderModelList = MongoReader.getOrderModel("US3002SfmOrderWithLBandMarketingDiscountTestVDV" + SoapKeys.GRAB);
		productsList = MongoReader.grabBasicProductModel("US3002SfmOrderWithLBandMarketingDiscountTestVDV" + SoapKeys.GRAB);
		productsListWithDiscount= MongoReader.grabBasicProductModel("US3002SfmOrderWithLBandMarketingDiscountTestVDV" + SoapKeys.RECALC);
		
		System.out.println("model: "+productsListWithDiscount);
		shippingModelList = MongoReader.grabShippingModel("US3002SfmOrderWithLBandMarketingDiscountTestVDV" + SoapKeys.CALC);
		calcDetailsModelList = MongoReader.grabCalcDetailsModels("US3002SfmOrderWithLBandMarketingDiscountTestVDV" + SoapKeys.CALC);

		if (orderModelList.size() == 1) {

			orderId = orderModelList.get(0).getOrderId();
		} else {
			Assert.assertTrue("Failure: Could not retrieve orderId. ", orderModelList.size() == 1);
		}

		if (calcDetailsModelList.size() != 1) {
			Assert.assertTrue("Failure: Could not validate Cart Totals Section. " + calcDetailsModelList, calcDetailsModelList.size() == 1);
		}

		if (shippingModelList.size() != 1) {
			Assert.assertTrue("Failure: Could not validate Cart Totals Section. " + calcDetailsModelList, calcDetailsModelList.size() == 1);
		}

		MongoConnector.cleanCollection(getClass().getSimpleName() + SoapKeys.GRAB);
		MongoConnector.cleanCollection(getClass().getSimpleName() + SoapKeys.CALC);

		// Setup Data from all models in first test
		// from Shipping calculations
		shopTotalsModel.setSubtotal(shippingModelList.get(0).getSubTotal());
		shopTotalsModel.setShipping(shippingModelList.get(0).getShippingPrice());
		shopTotalsModel.setTotalAmount(shippingModelList.get(0).getTotalAmount());
		// from calcDetails model calculations
		shopTotalsModel.setTotalIP(calcDetailsModelList.get(0).getIpPoints());
		shopTotalsModel.setTotalMarketingBonus(calcDetailsModelList.get(0).getMarketingBonus());
		shopTotalsModel.setTotalBonusJeverly(calcDetailsModelList.get(0).getJewelryBonus());
		
		shopTotalsModel.setTax(calcDetailsModelList.get(0).getTax());
		shopTotalsModel.setTotalPaid("0.00");
		shopTotalsModel.setTotalRefunded("0.00");
		shopTotalsModel.setTotalPayable(shippingModelList.get(0).getTotalAmount());
		shopTotalsModel.setTotalFortyDiscounts("0.00");
	}


	@Test
	public void us34003CreateCreditMemoOnOrderTestVDV() throws Exception {

		backEndSteps.performAdminLogin(Credentials.BE_USER, Credentials.BE_PASS);
		backEndSteps.clickOnSalesOrders();
		backEndSteps.searchOrderByOrderId(orderId);
		backEndSteps.openOrderDetails(orderId);
		
		ordersSteps.clickcreditMemoButton();
		
		List<OrderItemModel> orderItemsList = ordersSteps.grabCreditMomoOrderItems();
		orderTotalsModel = ordersSteps.grabTotals();
		
		orderWorkflows.setValidateCalculationTotals(orderTotalsModel, shopTotalsModel);
		orderWorkflows.validateInvoiceCalculationTotals("TOTALS VALIVATION");

		orderProductsWorkflows.setValidateProductsModels(productsList, orderItemsList);
		orderProductsWorkflows.validateProducts("PRODUCTS VALIDATION");
		
		////////////////////////////////////////////////////////////////////////////////
		
		//refund
		ordersSteps.refundQty(productsList.get(0).getProdCode(), "1");
		ordersSteps.updateQty();
				
		ordersSteps.waitABit(3000);
				
		recalculatedProductsList=CartCalculator.calculatePartialRefundProducts(productsListWithDiscount,productsList.get(0),"1");
		shopTotalsModel1=CartCalculator.calculateTotalPartialRefund(shopTotalsModel,recalculatedProductsList);
	
		orderTotalsModel = ordersSteps.grabTotals();
		
		orderWorkflows.setValidateCalculationTotals(orderTotalsModel, shopTotalsModel1);
		orderWorkflows.validateInvoiceCalculationTotals("TOTALS VALIVATION");
		
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
		
		
		ordersSteps.refundOffline();

		
		orderTotalsModel = ordersSteps.grabTotals();
		
		shopTotalsModel.setTotalRefunded(shopTotalsModel1.getTotalAmount());
		shopTotalsModel.setDiscount(shopTotalsModel.getDiscount());
		
		orderWorkflows.setValidateCalculationTotals(orderTotalsModel, shopTotalsModel);
		orderWorkflows.validatePartialCMCalculationTotalsAfterRefund("TOTALS VALIVATION AFTER REFUND ");
		
		
//////////////////////////////////////////////////////////<3>
		
		ordersSteps.clickcreditMemoButton();

		
		
		shopTotalsModel=CartCalculator.calculateTotalCM(shopTotalsModel,recalculatedProductsList.get(0),"1");
		orderTotalsModel = ordersSteps.grabTotals();

		orderWorkflows.setValidateCalculationTotals(orderTotalsModel, shopTotalsModel);
		orderWorkflows.validatePartialCMCalculationTotalsBeforeRefund("TOTALS VALIVATION AFTER CREDIT MEMO");
		
		ordersSteps.submitMemoButton();
		/*backEndSteps.clickOnSalesCreditOrders();
		backEndSteps.searchCreditMemoByOrderId(orderId);
		backEndSteps.openCreditMemoDetails(orderId);*/
		
		customVerifications.printErrors();
		
	}

}
