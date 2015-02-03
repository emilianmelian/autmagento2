package com.tests.us3;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Story;
import net.thucydides.core.annotations.WithTag;
import net.thucydides.junit.runners.ThucydidesRunner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.connectors.mongo.MongoConnector;
import com.steps.frontend.CustomerRegistrationSteps;
import com.steps.frontend.HeaderSteps;
import com.steps.frontend.ProductSteps;
import com.steps.frontend.ProfileSteps;
import com.steps.frontend.SearchSteps;
import com.steps.frontend.checkout.CartSteps;
import com.steps.frontend.checkout.CheckoutValidationSteps;
import com.steps.frontend.checkout.ConfirmationSteps;
import com.steps.frontend.checkout.PaymentSteps;
import com.steps.frontend.checkout.ShippingSteps;
import com.tests.BaseTest;
import com.tools.Constants;
import com.tools.calculation.CartCalculation;
import com.tools.data.CalcDetailsModel;
import com.tools.data.CalculationModel;
import com.tools.data.backend.OrderModel;
import com.tools.data.frontend.CartProductModel;
import com.tools.data.frontend.CartTotalsModel;
import com.tools.data.frontend.CreditCardModel;
import com.tools.data.frontend.ProductBasicModel;
import com.tools.data.frontend.ShippingModel;
import com.tools.persistance.MongoWriter;
import com.tools.requirements.Application;
import com.tools.utils.FormatterUtils;
import com.tools.utils.PrintUtils;
import com.workflows.frontend.CartWorkflows;


@WithTag(name = "US003", type = "frontend")
@Story(Application.StyleCoach.Shopping.class)
@RunWith(ThucydidesRunner.class)
public class US003CartSegmentationWithVatTest extends BaseTest {
	
	@Steps
	public CustomerRegistrationSteps frontEndSteps;
	@Steps
	public ProductSteps productSteps;
	@Steps
	public SearchSteps searchSteps;
	@Steps
	public HeaderSteps headerSteps;
	@Steps
	public CartSteps cartSteps;
	@Steps
	public CartCalculation calculationSteps;
	@Steps
	public CheckoutValidationSteps checkoutValidationSteps;
	@Steps
	public ShippingSteps shippingSteps;
	@Steps
	public ConfirmationSteps confirmationSteps;
	@Steps
	public ProfileSteps profileSteps;
	@Steps
	public CartWorkflows cartWorkflows;	
	@Steps
	public PaymentSteps paymentSteps;

	
	private CreditCardModel creditCardData = new CreditCardModel();
	private static CartTotalsModel cartTotals = new CartTotalsModel();
	private static List<ProductBasicModel> productsList = new ArrayList<ProductBasicModel>();
	private static CalcDetailsModel discountCalculationModel;
	
	//extracted from URL in first test - validated in second test
	private static OrderModel orderModel;
	
	private List<CalculationModel> totalsList = new ArrayList<CalculationModel>();
	private String username, password;
	private static String jewelryDisount = "100";
	private static String marketingDisount = "150";


	@Before
	public void setUp() throws Exception {
		
		Properties prop = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream(Constants.RESOURCES_PATH + "us3\\us003.properties");
			prop.load(input);
			username = prop.getProperty("username");
			password = prop.getProperty("password");

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		creditCardData.setCardNumber("4111 1111 1111 1111");
		creditCardData.setCardName("test");
		creditCardData.setMonthExpiration("06");
		creditCardData.setYearExpiration("2016");
		creditCardData.setCvcNumber("737");
	
		//Clean DB
		MongoConnector.cleanCollection(getClass().getSimpleName());
	}

	@Test
	public void uS003CartSegmentationWithVatTest() {
		frontEndSteps.performLogin(username, password);
		ProductBasicModel productData;
		
//		searchSteps.searchAndSelectProduct("Prod1_ioana", "PRODUS SIMPLU IOANA");
//		productData = productSteps.setProductAddToCart("1", "0");
//		productsList.add(productData);
//		
//		searchSteps.searchAndSelectProduct("Prod1_ioana", "PRODUS SIMPLU IOANA");
//		productData = productSteps.setProductAddToCart("1", "0");
//		productsList.add(productData);
		searchSteps.searchAndSelectProduct("K010SV", "CLARA SET");
		productData = productSteps.setProductAddToCart("1", "0");
		productsList.add(productData);
		
		searchSteps.searchAndSelectProduct("K010SV", "CLARA SET");
		productData = productSteps.setProductAddToCart("1", "0");
		productsList.add(productData);
		
		searchSteps.searchAndSelectProduct("R025WT", "DAMARIS RING");
		productData = productSteps.setProductAddToCart("1", "16");
		productsList.add(productData);
		
		searchSteps.searchAndSelectProduct("M101 ", "STYLE BOOK HERBST / WINTER 2014 (270 STK)");
		productData = productSteps.setProductAddToCart("1", "0");
		productsList.add(productData);
		
		headerSteps.openCartPreview();
		headerSteps.goToCart();
		
		List<CartProductModel> cartProductsWith50Discount = cartSteps.grabProductsDataWith50PercentDiscount();		

		List<CartProductModel> cartProductsWith25Discount = cartSteps.grabProductsDataWith25PercentDiscount();
		
		List<CartProductModel> cartMarketingMaterialsProducts = cartSteps.grabMarketingMaterialProductsData();
		
		totalsList.add(CartCalculation.calculateTableProducts(cartProductsWith25Discount));
		totalsList.add(CartCalculation.calculateTableProducts(cartProductsWith50Discount));
		totalsList.add(CartCalculation.calculateTableProducts(cartMarketingMaterialsProducts));
		CalculationModel totalsCalculated = CartCalculation.calculateTotalSum(totalsList);
		PrintUtils.printCalculationModel(totalsCalculated);		
		
		List<CartProductModel> cartProducts = cartSteps.grabProductsData();
		cartTotals = cartSteps.grabTotals();

		//APPLY DISCOUNTS
		cartSteps.typeJewerlyBonus(jewelryDisount);
		cartSteps.updateJewerlyBonus();
		cartSteps.typeMarketingBonus(marketingDisount);
		cartSteps.updateMarketingBonus();		

		discountCalculationModel = calculationSteps.calculateDiscountTotals(totalsList, jewelryDisount, marketingDisount);
		ShippingModel shippingCalculatedModel = calculationSteps.remove119VAT(discountCalculationModel, "5.04");
		
		
		CartTotalsModel discountTotals = new CartTotalsModel();
		discountTotals = cartSteps.grabTotals();
		
		cartSteps.clickGoToShipping();
		
		shippingSteps.selectAddress("sss sss, tttt, 3, 2345 Wien, Österreich");
		shippingSteps.setSameAsBilling(true);

		List<CartProductModel> shippingProducts = shippingSteps.grabProductsList();
		PrintUtils.printList(shippingProducts);

		ShippingModel shippingTotals = shippingSteps.grabSurveyData();
		PrintUtils.printShippingTotals(shippingTotals);

		
		shippingSteps.clickGoToPaymentMethod();
		
		//Grab data from URL //TODO validate URL price 
		String url = shippingSteps.grabUrl();
		orderModel.setTotalPrice(FormatterUtils.extractPriceFromURL(url));
		orderModel.setOrderId(FormatterUtils.extractOrderIDFromURL(url));
		
		paymentSteps.expandCreditCardForm();
		paymentSteps.fillCreditCardForm(creditCardData);		

		List<CartProductModel> confirmationProducts = confirmationSteps.grabProductsList();
		
		//Totals validation
		cartWorkflows.setCheckCalculationTotalsModels(totalsCalculated, cartTotals);
		cartWorkflows.checkCalculationTotals("CART TOTALS");
		
		cartWorkflows.setVerifyTotalsDiscount(discountTotals, discountCalculationModel);
		cartWorkflows.verifyTotalsDiscount("DISCOUNT TOTALS");
		
		
		
		
		//TODO Create a shipping totals RIGHT - Investigating As BUG
//		cartWorkflows.setVerifyTotalsDiscount(shippingTotals, discountCalculationModel);
//		cartWorkflows.verifyTotalsDiscount("SHIPPING TOTALS");
		
		checkoutValidationSteps.checkTotalAmountFromUrl(orderModel.getTotalPrice(), discountCalculationModel.getTotalAmount().replace(".", ""));

		//Products List validation
		cartWorkflows.setValidateProductsModels(productsList, cartProducts);
		cartWorkflows.validateProducts("CART PHASE PRODUCTS VALIDATION");

		cartWorkflows.setValidateProductsModels(productsList, shippingProducts);
		cartWorkflows.validateProducts("SHIPPING PHASE PRODUCTS VALIDATION");

		cartWorkflows.setValidateProductsModels(productsList, confirmationProducts);
		cartWorkflows.validateProducts("CONFIRMATION PHASE PRODUCTS VALIDATION");
		
		
		//Steps to finalize order
//		confirmationSteps.agreeAndCheckout();
//		checkoutValidationSteps.verifySuccessMessage();
	}


	@Test
	public void us003UserProfileOrderId() {

		// After validation - grab order number
		headerSteps.redirectToProfileHistory();
		List<OrderModel> orderHistory = profileSteps.grabOrderHistory();

		String orderId = orderHistory.get(0).getOrderId();
		String orderPrice = orderHistory.get(0).getTotalPrice();
		profileSteps.verifyOrderId(orderId, orderModel.getOrderId());
		profileSteps.verifyOrderPrice(orderPrice, orderModel.getTotalPrice());
		orderModel = orderHistory.get(0);
	}
	
	
	@After
	public void saveData() {
		MongoWriter.saveTotalsModel(cartTotals, getClass().getSimpleName());
		MongoWriter.saveCalcDetailsModel(discountCalculationModel, getClass().getSimpleName());
		MongoWriter.saveOrderModel(orderModel, getClass().getSimpleName());
		for (ProductBasicModel product : productsList) {
			MongoWriter.saveProductBasicModel(product, getClass().getSimpleName());
		}
	}

}
