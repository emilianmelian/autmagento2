package com.tests.testCopyOf3009;

import java.io.File;
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

import com.connectors.http.CreateProduct;
import com.connectors.mongo.MongoConnector;
import com.steps.frontend.CustomerRegistrationSteps;
import com.steps.frontend.HeaderSteps;
import com.steps.frontend.ProductSteps;
import com.steps.frontend.SearchSteps;
import com.steps.frontend.checkout.CartSteps;
import com.steps.frontend.checkout.CheckoutValidationSteps;
import com.steps.frontend.checkout.ConfirmationSteps;
import com.steps.frontend.checkout.PaymentSteps;
import com.steps.frontend.checkout.ShippingSteps;
import com.tests.BaseTest;
import com.tools.Constants;
import com.tools.CustomVerification;
import com.tools.calculation.CartCalculation;
import com.tools.calculation.NewCalculation;
import com.tools.data.CalcDetailsModel;
import com.tools.data.UrlModel;
import com.tools.data.backend.OrderModel;
import com.tools.data.frontend.BasicProductModel;
import com.tools.data.frontend.CartTotalsModel;
import com.tools.data.frontend.CreditCardModel;
import com.tools.data.frontend.ShippingModel;
import com.tools.data.soap.ProductDetailedModel;
import com.tools.persistance.MongoTableKeys;
import com.tools.persistance.MongoWriter;
import com.tools.requirements.Application;
import com.tools.utils.PrintUtils;
import com.tools.utils.RandomGenerators;
import com.workflows.frontend.CartWorkflows;

@WithTag(name = "US3009", type = "frontend")
@Story(Application.StyleCoach.Shopping.class)
@RunWith(ThucydidesRunner.class)
public class US3009Test extends BaseTest {
	
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
	public CartWorkflows cartWorkflows;
	@Steps
	public PaymentSteps paymentSteps;
	@Steps 
	public CustomVerification customVerifications;
	
	private String username, password;
	private String billingAddress;
	private BasicProductModel productBasicModel = new BasicProductModel();
	private CreditCardModel creditCardData = new CreditCardModel();
	private static ShippingModel shippingCalculatedModel = new ShippingModel();
	private static List<BasicProductModel> productsList25 = new ArrayList<BasicProductModel>();
	private static List<BasicProductModel> productsList50 = new ArrayList<BasicProductModel>();
	private static ShippingModel confirmationTotals = new ShippingModel();
	private static ShippingModel shippingTotals = new ShippingModel();
	private static UrlModel urlModel = new UrlModel();
	private static OrderModel orderModel = new OrderModel();
	private static CartTotalsModel cartTotals = new CartTotalsModel();
	private CalcDetailsModel total = new CalcDetailsModel();
	private static String jewelryDiscount;
	private static String marketingDiscount;
	private static String shippingValue;
	private static String taxClass;
	private static String cardNumber;
	private static String cardName;
	private static String cardMonth;
	private static String cardYear;
	private static String cardCVC;
	
	private ProductDetailedModel genProduct1;
	private ProductDetailedModel genProduct2;


	@Before
	public void setUp() throws Exception {
		
		genProduct1 = CreateProduct.createProductModel();
		genProduct1.setPrice(RandomGenerators.generateRandomDoubleAsString(100, 40));
		CreateProduct.createApiProduct(genProduct1);
		
		genProduct2 = CreateProduct.createProductModel();
		genProduct2.setPrice(RandomGenerators.generateRandomDoubleAsString(100, 40));
		CreateProduct.createApiProduct(genProduct2);

		Properties prop = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream(Constants.RESOURCES_PATH + "us3009" + File.separator + "us3009.properties");
			prop.load(input);
			username = prop.getProperty("username");
			password = prop.getProperty("password");
			billingAddress = prop.getProperty("billingAddress");
			jewelryDiscount = prop.getProperty("jewelryDiscount");
			marketingDiscount = prop.getProperty("marketingDiscount");
			shippingValue = prop.getProperty("shippingPrice");
			taxClass = prop.getProperty("taxClass");

			cardNumber = prop.getProperty("cardNumber");
			cardName = prop.getProperty("cardName");
			cardMonth = prop.getProperty("cardMonth");
			cardYear = prop.getProperty("cardYear");
			cardCVC = prop.getProperty("cardCVC");

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

		creditCardData.setCardNumber(cardNumber);
		creditCardData.setCardName(cardName);
		creditCardData.setMonthExpiration(cardMonth);
		creditCardData.setYearExpiration(cardYear);
		creditCardData.setCvcNumber(cardCVC);

		// Clean DB
		MongoConnector.cleanCollection(getClass().getSimpleName() + Constants.GRAB);
		MongoConnector.cleanCollection(getClass().getSimpleName() + Constants.CALC);

	}

	@Test
	public void us3009CartSegmentationWithVatAndSmbBillingDeShippingAtTest() {
		frontEndSteps.performLogin(username, password);
		frontEndSteps.wipeCart();
		BasicProductModel productData;
		
		String askingPrice = NewCalculation.calculateAskingPrice(genProduct1.getPrice(), "1");
		String finalPrice = NewCalculation.calculateFinalPrice(askingPrice, Constants.DISCOUNT_25);

		searchSteps.searchAndSelectProduct(genProduct1.getSku(), genProduct1.getName());
		productData = productSteps.setBasicProductAddToCart("2", "0",Constants.DISCOUNT_25,askingPrice,finalPrice,genProduct1.getIp());		
		
		BasicProductModel prod1_25 = productBasicModel.newProductObject(productData);
		prod1_25.setQuantity("1");
		prod1_25.setDiscountClass(Constants.DISCOUNT_25);
		BasicProductModel prod1_50 = new BasicProductModel();
		prod1_50 = productBasicModel.newProductObject(productData);
		prod1_50.setDiscountClass(Constants.DISCOUNT_50);
		prod1_50.setQuantity("3");		
		productsList25.add(prod1_25);
		productsList50.add(prod1_50);
		
		PrintUtils.printListBasicProductModel(productsList25);
		PrintUtils.printListBasicProductModel(productsList50);

////		searchSteps.searchAndSelectProduct(genProduct2.getSku(), genProduct2.getName());
////		productData = productSteps.setBasicProductAddToCart("1", "0");
////		productsList50.add(productData);
////
////		searchSteps.searchAndSelectProduct("M101", "STYLE BOOK HERBST / WINTER 2014 (270 STK)");
////		productData = productSteps.setBasicProductAddToCart("2", "0");
////		productsListMarketing.add(productData);
//
//		allProductsList.addAll(productsList25);
//		allProductsList.addAll(productsList50);
//		allProductsList.addAll(productsListMarketing);
//
//		headerSteps.openCartPreview();
//		headerSteps.goToCart();
//
//		List<CartProductModel> cartProductsWith50Discount = cartSteps.grabProductsDataWith50PercentDiscount();
//
//		List<CartProductModel> cartProductsWith25Discount = cartSteps.grabProductsDataWith25PercentDiscount();
//
//		List<CartProductModel> cartMarketingMaterialsProducts = cartSteps.grabMarketingMaterialProductsData();
//
//		cartProds.addAll(cartProductsWith50Discount);
//		cartProds.addAll(cartProductsWith25Discount);
//		cartProds.addAll(cartMarketingMaterialsProducts);
//
//		cartSteps.typeJewerlyBonus(jewelryDiscount);
//		cartSteps.updateJewerlyBonus();
//		cartSteps.typeMarketingBonus(marketingDiscount);
//		cartSteps.updateMarketingBonus();
//		
//		List<CartProductModel> calculatedProductsList25 = CartCalculation.calculateProductsfor25Discount(cartProductsWith25Discount, jewelryDiscount);
//		PrintUtils.printList(calculatedProductsList25);
//
//		List<CartProductModel> calculatedProductsList50 = CartCalculation.calculateProductsfor50Discount(cartProductsWith50Discount,cartProductsWith25Discount, jewelryDiscount);
//		PrintUtils.printList(calculatedProductsList50);
//
//		List<CartProductModel> calculatedProductsListMarketing = CartCalculation.calculateProductsforMarketingMaterial(cartMarketingMaterialsProducts, marketingDiscount);
//		PrintUtils.printList(calculatedProductsListMarketing);
//		
//		allProductsListRecalculated.addAll(calculatedProductsList50);
//		allProductsListRecalculated.addAll(calculatedProductsList25);
//		allProductsListRecalculated.addAll(calculatedProductsListMarketing);
//		
//		List<CartProductModel> cartProductsWith50DiscountDiscounted = cartSteps.grabProductsDataWith50PercentDiscount();
//
//		List<CartProductModel> cartProductsWith25DiscountDiscounted = cartSteps.grabProductsDataWith25PercentDiscount();
//
//		List<CartProductModel> cartMarketingMaterialsProductsDiscounted = cartSteps.grabMarketingMaterialProductsData();			
//
//		cartTotals = cartSteps.grabTotals();
//
//		total = CartCalculation.calculateCartProductsTotals(allProductsListRecalculated, jewelryDiscount, marketingDiscount,taxClass);
//		PrintUtils.printCalcDetailsModel(total);
//
//		cartSteps.clickGoToShipping();
//
//		shippingSteps.selectAddress(billingAddress);
//		shippingSteps.setSameAsBilling(true);	
//
//		List<CartProductModel> shippingProducts = shippingSteps.grabProductsList();
//
//		shippingTotals = shippingSteps.grabSurveyData();
//
//		shippingCalculatedModel = calculationSteps.calculateShippingTotals(total, shippingValue);
//		PrintUtils.printShippingTotals(shippingCalculatedModel);
//
//		shippingSteps.clickGoToPaymentMethod();
//
//		String url = shippingSteps.grabUrl();
//		urlModel.setName("Payment URL");
//		urlModel.setUrl(url);
//		orderModel.setTotalPrice(FormatterUtils.extractPriceFromURL(url));
//		orderModel.setOrderId(FormatterUtils.extractOrderIDFromURL(url));
//
//		paymentSteps.expandCreditCardForm();
//		paymentSteps.fillCreditCardForm(creditCardData);
//
//		List<CartProductModel> confirmationProducts = confirmationSteps.grabProductsList();
//		confirmationTotals = confirmationSteps.grabConfirmationTotals();
//		
//		AddressModel grabbedBillingAddress =  confirmationSteps.grabBillingData();
//		AddressModel grabbedShippingAddress = confirmationSteps.grabSippingData();
//
//		confirmationSteps.agreeAndCheckout();
//
//		checkoutValidationSteps.verifySuccessMessage();
		
		//validate products before discount to be applied
		
//		cartWorkflows.setValidateProductsModels(productsList50, cartProductsWith50Discount);
//		cartWorkflows.validateProducts("CART PHASE PRODUCTS VALIDATION FOR 50 SECTION");
//
//		cartWorkflows.setValidateProductsModels(productsList25, cartProductsWith25Discount);
//		cartWorkflows.validateProducts("CART PHASE PRODUCTS VALIDATION FOR 25 SECTION");
//
//		cartWorkflows.setValidateProductsModels(productsListMarketing, cartMarketingMaterialsProducts);
//		cartWorkflows.validateProducts("CART PHASE PRODUCTS VALIDATION FOR MARKETING MATERIAL SECTION");
//		
//		//validations products after discount is applied
//		cartWorkflows.setRecalculatedCartProductsModels(cartProductsWith50DiscountDiscounted,calculatedProductsList50);
//		cartWorkflows.validateRecalculatedProducts("CART PHASE PRODUCTS VALIDATION FOR 50 SECTION -RECALCULATED");
//		
//		cartWorkflows.setRecalculatedCartProductsModels(cartProductsWith25DiscountDiscounted, calculatedProductsList25);
//		cartWorkflows.validateRecalculatedProducts("CART PHASE PRODUCTS VALIDATION FOR 25 SECTION -RECALCULATED");
//		
//		cartWorkflows.setRecalculatedCartProductsModels(cartMarketingMaterialsProductsDiscounted, calculatedProductsListMarketing);
//		cartWorkflows.validateRecalculatedProducts("CART PHASE PRODUCTS VALIDATION FOR MARKETING MATERIAL SECTION -RECALCULATED");
//
//		cartWorkflows.setValidateProductsModels(allProductsList, shippingProducts);
//		cartWorkflows.validateProducts("SHIPPING PHASE PRODUCTS VALIDATION");
//
//		cartWorkflows.setValidateProductsModels(allProductsList, confirmationProducts);
//		cartWorkflows.validateProducts("CONFIRMATION PHASE PRODUCTS VALIDATION");
//
//		cartWorkflows.setVerifyTotalsDiscount(cartTotals, total);
//		cartWorkflows.verifyTotalsDiscountNoMarketing("CART TOTALS");
//
//		cartWorkflows.setVerifyShippingTotals(shippingTotals, shippingCalculatedModel);
//		cartWorkflows.verifyShippingTotals("SHIPPING TOTALS");
//
//		cartWorkflows.setVerifyShippingTotals(confirmationTotals, shippingCalculatedModel);
//		cartWorkflows.verifyShippingTotals("CONFIRMATION TOTALS");
//		
//		cartWorkflows.setBillingAddressModels(billingAddress,grabbedBillingAddress);
//		cartWorkflows.validateBillingAddress("BILLING ADDRESS");
//		
//		cartWorkflows.setShippingAddressModels(billingAddress,grabbedShippingAddress);
//		cartWorkflows.validateShippingAddress("SHIPPING ADDRESS");
//		
//		customVerifications.printErrors();

	}

	@After
	public void saveData() {

		MongoWriter.saveCalcDetailsModel(total, getClass().getSimpleName() + Constants.CALC);
		MongoWriter.saveShippingModel(shippingCalculatedModel, getClass().getSimpleName() + Constants.CALC);
		MongoWriter.saveShippingModel(confirmationTotals, getClass().getSimpleName() + MongoTableKeys.GRAB);
		MongoWriter.saveOrderModel(orderModel, getClass().getSimpleName() + Constants.GRAB);
		MongoWriter.saveUrlModel(urlModel, getClass().getSimpleName() + Constants.GRAB);
//		for (ProductBasicModel product : allProductsList) {
//			MongoWriter.saveProductBasicModel(product, getClass().getSimpleName() + Constants.GRAB);
//		}

	}

}