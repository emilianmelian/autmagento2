package com.tests.us8;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
import com.steps.frontend.checkout.CheckoutValidationSteps;
import com.steps.frontend.checkout.ConfirmationSteps;
import com.steps.frontend.checkout.PaymentSteps;
import com.steps.frontend.checkout.ShippingSteps;
import com.steps.frontend.checkout.cart.regularCart.RegularUserCartSteps;
import com.steps.frontend.checkout.shipping.regularUser.ShippingPartySectionSteps;
import com.tests.BaseTest;
import com.tools.Constants;
import com.tools.CustomVerification;
import com.tools.data.RegularCartCalcDetailsModel;
import com.tools.data.frontend.CreditCardModel;
import com.tools.data.frontend.RegularBasicProductModel;
import com.tools.data.soap.ProductDetailedModel;
import com.tools.datahandlers.CartCalculator;
import com.tools.datahandlers.DataGrabber;
import com.tools.datahandlers.regularUser.RegularUserCartCalculator;
import com.tools.datahandlers.regularUser.RegularUserDataGrabber;
import com.tools.persistance.MongoWriter;
import com.tools.requirements.Application;
import com.tools.utils.FormatterUtils;
import com.tools.utils.PrintUtils;
import com.workflows.frontend.regularUser.AddRegularProductsWorkflow;
import com.workflows.frontend.regularUser.RegularCartValidationWorkflows;

@WithTag(name = "US3006", type = "frontend")
@Story(Application.StyleCoach.Shopping.class)
@RunWith(ThucydidesRunner.class)
public class CustomerBuyWithForthyDiscountsAndJbTest extends BaseTest {

	@Steps
	public HeaderSteps headerSteps;
	@Steps
	public ShippingSteps shippingSteps;
	@Steps
	public PaymentSteps paymentSteps;
	@Steps
	public ConfirmationSteps confirmationSteps;
	@Steps
	public ShippingPartySectionSteps shippingPartySectionSteps;
	@Steps
	public RegularUserCartSteps regularUserCartSteps;
	@Steps
	public CustomerRegistrationSteps customerRegistrationSteps;
	@Steps
	public AddRegularProductsWorkflow addRegularProductsWorkflow;
	@Steps
	public CheckoutValidationSteps checkoutValidationSteps;
	@Steps
	public RegularCartValidationWorkflows regularCartValidationWorkflows;
	@Steps
	public CustomVerification customVerifications;

	private String username, password;
	private String discountClass;
	private String billingAddress;
	private String shippingValue;
	private String voucherCode;
	private String voucherValue;

	private CreditCardModel creditCardData = new CreditCardModel();
	public RegularCartCalcDetailsModel total = new RegularCartCalcDetailsModel();

	private ProductDetailedModel genProduct1;
	private ProductDetailedModel genProduct2;
	private ProductDetailedModel genProduct3;

	@Before
	public void setUp() throws Exception {
		CartCalculator.wipe();
		DataGrabber.wipe();

		genProduct1 = CreateProduct.createProductModel();
		genProduct1.setPrice("89.00");
		CreateProduct.createApiProduct(genProduct1);

		genProduct2 = CreateProduct.createProductModel();
		genProduct2.setPrice("49.90");
		CreateProduct.createApiProduct(genProduct2);

		genProduct3 = CreateProduct.createProductModel();
		genProduct3.setPrice("100.00");
		CreateProduct.createApiProduct(genProduct3);

		Properties prop = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream(Constants.RESOURCES_PATH + "us8" + File.separator + "us8001.properties");
			prop.load(input);
			username = prop.getProperty("username");
			password = prop.getProperty("password");

			discountClass = prop.getProperty("discountClass");
			billingAddress = prop.getProperty("billingAddress");
			shippingValue = prop.getProperty("shippingValue");

			voucherCode = prop.getProperty("voucherCode");
			voucherValue = prop.getProperty("voucherValue");

			creditCardData.setCardNumber(prop.getProperty("cardNumber"));
			creditCardData.setCardName(prop.getProperty("cardName"));
			creditCardData.setMonthExpiration(prop.getProperty("cardMonth"));
			creditCardData.setYearExpiration(prop.getProperty("cardYear"));
			creditCardData.setCvcNumber(prop.getProperty("cardCVC"));

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

		MongoConnector.cleanCollection(getClass().getSimpleName() + Constants.GRAB);
		MongoConnector.cleanCollection(getClass().getSimpleName() + Constants.CALC);
	}

	@Test
	public void customerCartTest() {
		customerRegistrationSteps.performLogin(username, password);
		customerRegistrationSteps.wipeRegularCart();
		RegularBasicProductModel productData;

		productData = addRegularProductsWorkflow.setBasicProductToCart(genProduct1, "1", "0");
		RegularUserCartCalculator.allProductsList.add(productData);
		productData = addRegularProductsWorkflow.setBasicProductToCart(genProduct2, "1", "0");
		RegularUserCartCalculator.allProductsList.add(productData);
		productData = addRegularProductsWorkflow.setBasicProductToCart(genProduct3, "4", "0");
		RegularUserCartCalculator.allProductsList.add(productData);

		headerSteps.openCartPreview();
		headerSteps.goToCart();
		// TODO put the updateProductList in selectProductDiscountType method
		// (maybe)
		regularUserCartSteps.selectProductDiscountType(genProduct1.getSku(), Constants.JEWELRY_BONUS);
		regularUserCartSteps.updateProductList(RegularUserCartCalculator.allProductsList, genProduct1.getSku(), Constants.JEWELRY_BONUS);
		regularUserCartSteps.selectProductDiscountType(genProduct2.getSku(), Constants.DISCOUNT_40_BONUS);
		regularUserCartSteps.updateProductList(RegularUserCartCalculator.allProductsList, genProduct2.getSku(), Constants.DISCOUNT_40_BONUS);

		regularUserCartSteps.typeCouponCode(voucherCode);
		regularUserCartSteps.submitVoucherCode();
		// TODO move this (maybe)
		regularUserCartSteps.validateThatVoucherCannotBeAppliedMessage();

		RegularUserDataGrabber.grabbedRegularCartProductsList = regularUserCartSteps.grabProductsData();
		System.out.println("FATALITATE");
		PrintUtils.printListRegularCartProductModel(RegularUserDataGrabber.grabbedRegularCartProductsList);
		RegularUserDataGrabber.regularUserGrabbedCartTotals = regularUserCartSteps.grabTotals();

		RegularUserCartCalculator.calculateCartAndShippingTotals(RegularUserCartCalculator.allProductsList, discountClass, shippingValue, voucherValue);

		System.out.println("--------CALC CART AND SHIPPING----------------");
		PrintUtils.printRegularCartCalcDetailsModel(RegularUserCartCalculator.calculatedTotalsDiscounts);
		PrintUtils.printShippingTotals(RegularUserCartCalculator.shippingCalculatedModel);

		regularUserCartSteps.clickGoToShipping();
		shippingPartySectionSteps.clickPartyNoOption();
		shippingSteps.selectAddress(billingAddress);
		shippingSteps.setSameAsBilling(true);

		RegularUserDataGrabber.grabbedRegularShippingProductsList = shippingSteps.grabRegularProductsList();
		System.out.println("-----------SHIPPING-------");
		PrintUtils.printListRegularCartProductModel(RegularUserDataGrabber.grabbedRegularShippingProductsList);
		RegularUserDataGrabber.regularUserShippingTotals = shippingSteps.grabSurveyData();
		PrintUtils.printShippingTotals(RegularUserDataGrabber.regularUserShippingTotals);
		shippingSteps.clickGoToPaymentMethod();

		String url = shippingSteps.grabUrl();
		DataGrabber.urlModel.setName("Payment URL");
		DataGrabber.urlModel.setUrl(url);
		RegularUserDataGrabber.orderModel.setTotalPrice(FormatterUtils.extractPriceFromURL(url));
		RegularUserDataGrabber.orderModel.setOrderId(FormatterUtils.extractOrderIDFromURL(url));

		paymentSteps.expandCreditCardForm();
		paymentSteps.fillCreditCardForm(creditCardData);

		System.out.println("------CONFIRMATION-------");
		confirmationSteps.grabRegularProductsList();
		PrintUtils.printListRegularCartProductModel(RegularUserDataGrabber.grabbedRegularConfirmationProductsList);
		RegularUserDataGrabber.regularUserConfirmationTotals = confirmationSteps.grabConfirmationTotals();
		PrintUtils.printShippingTotals(RegularUserDataGrabber.regularUserConfirmationTotals);
		confirmationSteps.grabBillingData();
		confirmationSteps.grabSippingData();

		confirmationSteps.agreeAndCheckout();
		checkoutValidationSteps.verifySuccessMessage();

		regularCartValidationWorkflows.setBillingShippingAddress(billingAddress, billingAddress);
		regularCartValidationWorkflows.performCartValidations();

		customVerifications.printErrors();
	}

	@After
	public void saveData() {
		MongoWriter.saveRegularCartCalcDetailsModel(RegularUserCartCalculator.calculatedTotalsDiscounts, getClass().getSimpleName() + Constants.CALC);
		MongoWriter.saveOrderModel(RegularUserDataGrabber.orderModel, getClass().getSimpleName() + Constants.GRAB);
		MongoWriter.saveUrlModel(RegularUserDataGrabber.urlModel, getClass().getSimpleName() + Constants.GRAB);
		for (RegularBasicProductModel product : RegularUserCartCalculator.allProductsList) {
			MongoWriter.saveRegularBasicProductModel(product, getClass().getSimpleName() + Constants.CALC);
		}
	}
}
//package com.tests.us8;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.Properties;
//
//import net.thucydides.core.annotations.Steps;
//import net.thucydides.core.annotations.Story;
//import net.thucydides.core.annotations.WithTag;
//import net.thucydides.junit.runners.ThucydidesRunner;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import com.connectors.http.CreateProduct;
//import com.connectors.mongo.MongoConnector;
//import com.steps.frontend.CustomerRegistrationSteps;
//import com.steps.frontend.HeaderSteps;
//import com.steps.frontend.checkout.ShippingSteps;
//import com.steps.frontend.checkout.cart.regularCart.RegularUserCartSteps;
//import com.steps.frontend.checkout.shipping.regularUser.ShippingPartySectionSteps;
//import com.tests.BaseTest;
//import com.tools.Constants;
//import com.tools.data.RegularCartCalcDetailsModel;
//import com.tools.data.frontend.CreditCardModel;
//import com.tools.data.frontend.RegularBasicProductModel;
//import com.tools.data.soap.ProductDetailedModel;
//import com.tools.datahandlers.CartCalculator;
//import com.tools.datahandlers.DataGrabber;
//import com.tools.datahandlers.regularUser.RegularUserCartCalculator;
//import com.tools.datahandlers.regularUser.RegularUserDataGrabber;
//import com.tools.requirements.Application;
//import com.tools.utils.PrintUtils;
//import com.workflows.frontend.regularUser.AddRegularProductsWorkflow;
//
//@WithTag(name = "US3006", type = "frontend")
//@Story(Application.StyleCoach.Shopping.class)
//@RunWith(ThucydidesRunner.class)
//public class CustomerBuyWithForthyDiscountsAndJbTest extends BaseTest {
//
//	@Steps
//	public HeaderSteps headerSteps;
//	@Steps
//	public ShippingSteps shippingSteps;
//	@Steps
//	public ShippingPartySectionSteps shippingPartySectionSteps;
//	@Steps
//	public RegularUserCartSteps regularUserCartSteps;
//	@Steps
//	public CustomerRegistrationSteps customerRegistrationSteps;
//	@Steps
//	public AddRegularProductsWorkflow addRegularProductsWorkflow;
//
//	private String username, password;
//	private String discountClass;
//	private String billingAddress;
//
//	private CreditCardModel creditCardData = new CreditCardModel();
//	public RegularCartCalcDetailsModel total = new RegularCartCalcDetailsModel();
//
//	private ProductDetailedModel genProduct1;
//	private ProductDetailedModel genProduct2;
//	private ProductDetailedModel genProduct3;
//
//	@Before
//	public void setUp() throws Exception {
//		CartCalculator.wipe();
//		DataGrabber.wipe();
//
//		genProduct1 = CreateProduct.createProductModel();
//		genProduct1.setPrice("89.00");
//		CreateProduct.createApiProduct(genProduct1);
//
//		genProduct2 = CreateProduct.createProductModel();
//		genProduct2.setPrice("49.90");
//		CreateProduct.createApiProduct(genProduct2);
//
//		genProduct3 = CreateProduct.createProductModel();
//		genProduct3.setPrice("100.00");
//		CreateProduct.createApiProduct(genProduct3);
//
//		Properties prop = new Properties();
//		InputStream input = null;
//
//		try {
//
//			input = new FileInputStream(Constants.RESOURCES_PATH + "us8" + File.separator + "us8001.properties");
//			prop.load(input);
//			username = prop.getProperty("username");
//			password = prop.getProperty("password");
//			
//			discountClass = prop.getProperty("discountClass");			
//			billingAddress = prop.getProperty("billingAddress");
//
//			creditCardData.setCardNumber(prop.getProperty("cardNumber"));
//			creditCardData.setCardName(prop.getProperty("cardName"));
//			creditCardData.setMonthExpiration(prop.getProperty("cardMonth"));
//			creditCardData.setYearExpiration(prop.getProperty("cardYear"));
//			creditCardData.setCvcNumber(prop.getProperty("cardCVC"));
//
//		} catch (IOException ex) {
//			ex.printStackTrace();
//		} finally {
//			if (input != null) {
//				try {
//					input.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//
//		MongoConnector.cleanCollection(getClass().getSimpleName() + Constants.GRAB);
//		MongoConnector.cleanCollection(getClass().getSimpleName() + Constants.CALC);
//	}
//
//	@Test
//	public void customerCartTest() {
//		customerRegistrationSteps.performLogin(username, password);
//		customerRegistrationSteps.wipeRegularCart();
//		RegularBasicProductModel productData;
//
//		productData = addRegularProductsWorkflow.setBasicProductToCart(genProduct1, "1", "0");
//		RegularUserCartCalculator.allProductsList.add(productData);
//		productData = addRegularProductsWorkflow.setBasicProductToCart(genProduct2, "1", "0");
//		RegularUserCartCalculator.allProductsList.add(productData);
//		productData = addRegularProductsWorkflow.setBasicProductToCart(genProduct3, "4", "0");
//		RegularUserCartCalculator.allProductsList.add(productData);	
//
//
//		headerSteps.openCartPreview();
//		headerSteps.goToCart();
//		//TODO put the updateProductList in selectProductDiscountType method (maybe)
//		regularUserCartSteps.selectProductDiscountType(genProduct1.getSku(), Constants.JEWELRY_BONUS);
//		regularUserCartSteps.updateProductList(RegularUserCartCalculator.allProductsList, genProduct1.getSku(), Constants.JEWELRY_BONUS);
//		regularUserCartSteps.selectProductDiscountType(genProduct2.getSku(), Constants.DISCOUNT_40_BONUS);
//		regularUserCartSteps.updateProductList(RegularUserCartCalculator.allProductsList, genProduct2.getSku(), Constants.DISCOUNT_40_BONUS);
//		
//		regularUserCartSteps.grabProductsData();
//		regularUserCartSteps.grabTotals();
////		RegularUserDataGrabber.grabbedRegularCartProductsList = regularUserCartSteps.grabProductsData();
////		RegularUserDataGrabber.regularUserGrabbedCartTotals  = regularUserCartSteps.grabTotals();
//		
//		RegularUserCartCalculator.calculateCartTotals(RegularUserCartCalculator.allProductsList, discountClass);
//		PrintUtils.printRegularCartCalcDetailsModel(RegularUserCartCalculator.calculatedTotalsDiscounts);
//		
//		regularUserCartSteps.clickGoToShipping();
//		shippingPartySectionSteps.clickPartyNoOption();
//		shippingSteps.selectAddress(billingAddress);
//		shippingSteps.setSameAsBilling(true);	
//		
//		RegularUserDataGrabber.grabbedRegularShippingProductsList = shippingSteps.grabRegularProductsList();
//		System.out.println("-----------SHIPPING-------");
//		PrintUtils.printListRegularCartProductModel(RegularUserDataGrabber.grabbedRegularShippingProductsList);
//		RegularUserDataGrabber.regularUserShippingTotals = shippingSteps.grabSurveyData();
//		PrintUtils.printShippingTotals(RegularUserDataGrabber.regularUserShippingTotals);
//		shippingSteps.clickGoToPaymentMethod();
//		
//		
//	
//	}
//
//>>>>>>> branch 'master' of git@evogit.evozon.com:pippajeanautotester/pippajeanautotester.git
//}
