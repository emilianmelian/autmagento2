package com.tests.us9.us9001;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Story;
import net.thucydides.core.annotations.WithTag;
import net.thucydides.junit.runners.ThucydidesRunner;

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
import com.steps.frontend.checkout.cart.partyHost.HostCartSteps;
import com.steps.frontend.checkout.shipping.regularUser.ShippingPartySectionSteps;
import com.tests.BaseTest;
import com.tools.Constants;
import com.tools.CustomVerification;
import com.tools.data.RegularCartCalcDetailsModel;
import com.tools.data.frontend.CreditCardModel;
import com.tools.data.frontend.HostBasicProductModel;
import com.tools.data.soap.ProductDetailedModel;
import com.tools.datahandlers.DataGrabber;
import com.tools.datahandlers.partyHost.HostCartCalculator;
import com.tools.datahandlers.partyHost.HostDataGrabber;
import com.tools.datahandlers.regularUser.RegularUserCartCalculator;
import com.tools.datahandlers.regularUser.RegularUserDataGrabber;
import com.tools.requirements.Application;
import com.tools.utils.FormatterUtils;
import com.tools.utils.PrintUtils;
import com.workflows.frontend.partyHost.AddHostProductsWorkflow;
import com.workflows.frontend.partyHost.HostCartValidationWorkflows;

@WithTag(name = "US8001", type = "frontend")
@Story(Application.StyleCoach.Shopping.class)
@RunWith(ThucydidesRunner.class)
public class US9001PartyHostBuyWithForthyDiscountsAndJbTest extends BaseTest {

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
	public HostCartSteps hostCartSteps;
	@Steps
	public CustomerRegistrationSteps customerRegistrationSteps;
	@Steps
	public AddHostProductsWorkflow addHostProductsWorkflow;
	@Steps
	public CheckoutValidationSteps checkoutValidationSteps;
	@Steps
	public HostCartValidationWorkflows hostCartValidationWorkflows;
	@Steps
	public CustomVerification customVerifications;

	private String username, password;
	private String discountClass;
	private String billingAddress;
	private String shippingValue;


	private CreditCardModel creditCardData = new CreditCardModel();
	public RegularCartCalcDetailsModel total = new RegularCartCalcDetailsModel();

	private ProductDetailedModel genProduct1;
	private ProductDetailedModel genProduct2;
	private ProductDetailedModel genProduct3;

	@Before
	public void setUp() throws Exception {
		RegularUserCartCalculator.wipe();
		RegularUserDataGrabber.wipe();

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

			input = new FileInputStream(Constants.RESOURCES_PATH + "us9" + File.separator + "us9001.properties");
			prop.load(input);
			username = prop.getProperty("username");
			password = prop.getProperty("password");

			discountClass = prop.getProperty("discountClass");
			billingAddress = prop.getProperty("billingAddress");
			shippingValue = prop.getProperty("shippingValue");

	

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
	public void us8001CustomerBuyWithForthyDiscountsAndJbTest() {
		customerRegistrationSteps.performLogin(username, password);
		headerSteps.navigateToPartyPageAndStartOrder("11832");
		customerRegistrationSteps.wipeRegularCart();
		HostBasicProductModel productData;

		productData = addHostProductsWorkflow.setHostProductToCart(genProduct1, "1", "0");
		HostCartCalculator.allProductsList.add(productData);
		productData = addHostProductsWorkflow.setHostProductToCart(genProduct2, "1", "0");
		HostCartCalculator.allProductsList.add(productData);
		productData = addHostProductsWorkflow.setHostProductToCart(genProduct3, "4", "0");
		HostCartCalculator.allProductsList.add(productData);
		
		System.out.println("----basic list----");
		PrintUtils.printListHostBasicProductModel(HostCartCalculator.allProductsList);

		headerSteps.openCartPreview();
		headerSteps.goToCart();
		
		hostCartSteps.selectProductDiscountType(genProduct1.getSku(), Constants.JEWELRY_BONUS);
		hostCartSteps.updateProductList(HostCartCalculator.allProductsList, genProduct1.getSku(), Constants.JEWELRY_BONUS);
		hostCartSteps.selectProductDiscountType(genProduct2.getSku(), Constants.DISCOUNT_40_BONUS);
		hostCartSteps.updateProductList(HostCartCalculator.allProductsList, genProduct2.getSku(), Constants.DISCOUNT_40_BONUS);

		hostCartSteps.grabProductsData();		
		hostCartSteps.grabTotals();
		PrintUtils.printListHostCartProductModel(HostDataGrabber.grabbedHostCartProductsList);
		PrintUtils.printHostCartTotalsModel(HostDataGrabber.hostGrabbedCartTotals);

		HostCartCalculator.calculateCartAndShippingTotals(HostCartCalculator.allProductsList, discountClass, shippingValue);
		PrintUtils.printHostCartCalcDetailsModel(HostCartCalculator.calculatedTotalsDiscounts);
		PrintUtils.printShippingTotals(HostCartCalculator.shippingCalculatedModel);

		hostCartSteps.clickGoToShipping();
		shippingSteps.selectAddress(billingAddress);
		shippingSteps.setSameAsBilling(true);

		HostDataGrabber.grabbedHostShippingProductsList = shippingSteps.grabHostProductsList();	
		HostDataGrabber.hostShippingTotals = shippingSteps.grabSurveyData();
		
		System.out.println("-----Shipping------");
		PrintUtils.printListHostCartProductModel(HostDataGrabber.grabbedHostShippingProductsList);
		PrintUtils.printShippingTotals(HostDataGrabber.hostShippingTotals);
		shippingSteps.clickGoToPaymentMethod();
		

		String url = shippingSteps.grabUrl();
		DataGrabber.urlModel.setName("Payment URL");
		DataGrabber.urlModel.setUrl(url);
		HostDataGrabber.orderModel.setTotalPrice(FormatterUtils.extractPriceFromURL(url));
		HostDataGrabber.orderModel.setOrderId(FormatterUtils.extractOrderIDFromURL(url));

		paymentSteps.expandCreditCardForm();
		paymentSteps.fillCreditCardForm(creditCardData);
	
		confirmationSteps.grabRegularProductsList();
		
		HostDataGrabber.hostConfirmationTotals = confirmationSteps.grabConfirmationTotals();
		
		confirmationSteps.grabBillingData();
		confirmationSteps.grabSippingData();

//		confirmationSteps.agreeAndCheckout();


		hostCartValidationWorkflows.setBillingShippingAddress(billingAddress, billingAddress);
		hostCartValidationWorkflows.performCartValidationsWith40DiscountAndJb();

		customVerifications.printErrors();
	}

//	@After
//	public void saveData() {
//		MongoWriter.saveRegularCartCalcDetailsModel(RegularUserCartCalculator.calculatedTotalsDiscounts, getClass().getSimpleName() + Constants.CALC);
//		MongoWriter.saveOrderModel(RegularUserDataGrabber.orderModel, getClass().getSimpleName() + Constants.GRAB);
//		MongoWriter.saveShippingModel(RegularUserCartCalculator.shippingCalculatedModel, getClass().getSimpleName() + Constants.CALC);
//		MongoWriter.saveUrlModel(RegularUserDataGrabber.urlModel, getClass().getSimpleName() + Constants.GRAB);
//		for (RegularBasicProductModel product : RegularUserCartCalculator.allProductsList) {
//			MongoWriter.saveRegularBasicProductModel(product, getClass().getSimpleName() + Constants.CALC);
//		}
//	}
}


