package com.tests.uss11.us11010;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.connectors.http.ApacheHttpHelper;
import com.connectors.http.MagentoProductCalls;
import com.connectors.mongo.MongoConnector;
import com.steps.frontend.CustomerRegistrationSteps;
import com.steps.frontend.FooterSteps;
import com.steps.frontend.HeaderSteps;
import com.steps.frontend.LoungeSteps;
import com.steps.frontend.PartyDetailsSteps;
import com.steps.frontend.checkout.CheckoutValidationSteps;
import com.steps.frontend.checkout.ConfirmationSteps;
import com.steps.frontend.checkout.PaymentSteps;
import com.steps.frontend.checkout.ShippingSteps;
import com.steps.frontend.checkout.cart.GeneralCartSteps;
import com.steps.frontend.checkout.cart.partyHost.OrderForCustomerCartSteps;
import com.steps.frontend.checkout.cart.regularCart.RegularUserCartSteps;
import com.steps.frontend.checkout.shipping.regularUser.ShippingPartySectionSteps;
import com.tests.BaseTest;
import com.tools.CustomVerification;
import com.tools.cartcalculations.partyHost.HostCartCalculator;
import com.tools.constants.EnvironmentConstants;
import com.tools.constants.UrlConstants;
import com.tools.data.frontend.CreditCardModel;
import com.tools.data.frontend.HostBasicProductModel;
import com.tools.data.soap.ProductDetailedModel;
import com.tools.datahandler.HostDataGrabber;
import com.tools.persistance.MongoReader;
import com.tools.persistance.MongoWriter;
import com.tools.requirements.Application;
import com.tools.utils.DateUtils;
import com.tools.utils.FormatterUtils;
import com.workflows.frontend.partyHost.AddProductsForCustomerWorkflow;
import com.workflows.frontend.partyHost.HostCartValidationWorkflows;

import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Story;
import net.thucydides.core.annotations.WithTag;

@WithTag(name = "US11.7 Party Host Buys For Customer With Term Purchase Immediate And Out Of Stock Products Test", type = "Scenarios")
@Story(Application.PlaceACustomerOrderCart.US11_10.class)
@RunWith(SerenityRunner.class)
public class US11010PartyHostBuysForCustomerImmediateWithTpTest extends BaseTest {

	@Steps
	public HeaderSteps headerSteps;
	@Steps
	public FooterSteps footerSteps;
	@Steps
	public PartyDetailsSteps partyDetailsSteps;
	@Steps
	public ShippingSteps shippingSteps;
	@Steps
	public PaymentSteps paymentSteps;
	@Steps
	public OrderForCustomerCartSteps orderForCustomerCartSteps;
	@Steps
	public GeneralCartSteps generalCartSteps;
	@Steps
	public ConfirmationSteps confirmationSteps;
	@Steps
	public LoungeSteps loungeSteps;
	@Steps
	public RegularUserCartSteps regularUserCartSteps;
	@Steps
	public ShippingPartySectionSteps shippingPartySectionSteps;
	@Steps
	public CustomerRegistrationSteps customerRegistrationSteps;
	@Steps
	public AddProductsForCustomerWorkflow addProductsForCustomerWorkflow;
	@Steps
	public CheckoutValidationSteps checkoutValidationSteps;
	@Steps
	public HostCartValidationWorkflows hostCartValidationWorkflows;
	@Steps
	public CustomVerification customVerifications;

	private String username, password, customerName;
	private String discountClass;
	private String billingAddress;
	private String country;
	private String plz;
	private String voucherCode;
	private String shippingValue;
	private String voucherValue;
	private CreditCardModel creditCardData = new CreditCardModel();
	private ProductDetailedModel genProduct1;
	private ProductDetailedModel genProduct2;
	private ProductDetailedModel genProduct3;
	private ProductDetailedModel genProduct4;

	public static List<ProductDetailedModel> createdProductsList = new ArrayList<ProductDetailedModel>();

	@Before
	public void setUp() throws Exception {
		HostCartCalculator.wipe();
		HostDataGrabber.wipe();

		genProduct1 = MagentoProductCalls.createProductModel();
		genProduct1.setPrice("29.00");
		genProduct1.setIp("0");
		MagentoProductCalls.createApiProduct(genProduct1);

		genProduct2 = MagentoProductCalls.createNotAvailableYetProductModel();
		genProduct2.setPrice("10.00");
		genProduct2.setIp("0");
		MagentoProductCalls.createApiProduct(genProduct2);

		genProduct3 = MagentoProductCalls.createProductModel();
		genProduct3.setPrice("29.90");
		genProduct3.setIp("0");
		genProduct3.setStockData(
				MagentoProductCalls.createNotAvailableYetStockData(DateUtils.getNextMonthMiddle("yyyy-MM-dd")));
		MagentoProductCalls.createApiProduct(genProduct3);

		genProduct4 = MagentoProductCalls.createProductModel();
		genProduct4.getStockData().setAllowedTermPurchase("1");
		genProduct4.setPrice("50.00");
		genProduct4.setIp("0");
		MagentoProductCalls.createApiProduct(genProduct4);
		genProduct4.setStockData(
				MagentoProductCalls.createNotAvailableYetStockData(DateUtils.getCurrentDate("yyyy-MM-dd")));

		// createdProductsList =
		// MongoReader.grabProductDetailedModel("CreateProductsTest");
		//
		// genProduct1 = createdProductsList.get(3);
		// genProduct2 = createdProductsList.get(12);
		// genProduct3 = createdProductsList.get(11);

		Properties prop = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream(UrlConstants.RESOURCES_PATH + "uss11" + File.separator + "us11007.properties");
			prop.load(input);
			username = prop.getProperty("username");
			password = prop.getProperty("password");
			customerName = prop.getProperty("customerName");

			country = prop.getProperty("country");
			plz = prop.getProperty("plz");
			discountClass = prop.getProperty("discountClass");
			billingAddress = prop.getProperty("billingAddress");
			shippingValue = prop.getProperty("shippingValue");
			voucherCode = prop.getProperty("voucherCode");
			voucherValue = prop.getProperty("voucherValue");

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

		MongoConnector.cleanCollection(getClass().getSimpleName() + "TP0");
		MongoConnector.cleanCollection(getClass().getSimpleName() + "TP1");
		MongoConnector.cleanCollection(getClass().getSimpleName() + "TP2");
		MongoConnector.cleanCollection(getClass().getSimpleName());

	}

	@Test
	public void us11010PartyHostBuysForCustomerImmediateWithTpTest() throws ParseException {
		customerRegistrationSteps.performLogin(username, password);
		if (!headerSteps.succesfullLogin()) {
			footerSteps.selectWebsiteFromFooter(MongoReader.getContext());
		}
		headerSteps.selectLanguage(MongoReader.getContext());
		do {
			loungeSteps.clickOrderForCustomer();
			partyDetailsSteps.orderForCustomer();
			partyDetailsSteps.orderForCustomerFromParty(customerName);
		} while (!orderForCustomerCartSteps.getCartOwnerInfo().contains(customerName.toUpperCase()));
		generalCartSteps.clearCart();

		String deliveryTP1 = DateUtils.getFirstFridayAfterDate(genProduct2.getStockData().getEarliestAvailability(),
				"yyyy-MM-dd");

		String deliveryTP2 = DateUtils.getFirstFridayAfterDate(
				DateUtils.addDaysToAAGivenDate(genProduct3.getStockData().getEarliestAvailability(), "yyyy-MM-dd", 7),
				"yyyy-MM-dd");

		HostBasicProductModel productData;
		
		productData = addProductsForCustomerWorkflow.setHostProductToCart(genProduct1, "1", "0");
		// we don't need to set here the delivery date
		HostCartCalculator.allProductsListTp0.add(productData);

		productData = addProductsForCustomerWorkflow.setHostProductToCart(genProduct2, "2", "0");
		if (!genProduct2.getStockData().getEarliestAvailability().contentEquals(""))
			productData.setDeliveryDate(deliveryTP1);
		HostCartCalculator.allProductsListTp1.add(productData);

		productData = addProductsForCustomerWorkflow.setHostProductToCart(genProduct3, "4", "0");
		if (!genProduct3.getStockData().getEarliestAvailability().contentEquals(""))
			productData.setDeliveryDate(deliveryTP2);
		HostCartCalculator.allProductsListTp2.add(productData);

		productData = addProductsForCustomerWorkflow.setHostProductToCart(genProduct4, "1", "0");
		if (!genProduct4.getStockData().getEarliestAvailability().contentEquals(""))
			productData.setDeliveryDate(deliveryTP1);
		HostCartCalculator.allProductsListTp1.add(productData);

		HostCartCalculator.allProductsList.addAll(HostCartCalculator.allProductsListTp0);
		HostCartCalculator.allProductsList.addAll(HostCartCalculator.allProductsListTp1);
		HostCartCalculator.allProductsList.addAll(HostCartCalculator.allProductsListTp2);

		headerSteps.openCartPreview();
		headerSteps.goToCart();

		String deliveryTP1Locale = DateUtils.parseDate(deliveryTP1, "yyyy-MM-dd", "dd. MMM. yy",
				new Locale.Builder().setLanguage(MongoReader.getContext()).build());

		String deliveryTP2Locale = DateUtils.parseDate(deliveryTP2, "yyyy-MM-dd", "dd. MMM. yy",
				new Locale.Builder().setLanguage(MongoReader.getContext()).build());

		// we set the delivery for product4 to be the same with the delivery for
		// product 2 (same order)
		regularUserCartSteps.selectDeliveryDate(genProduct2.getSku(), deliveryTP1Locale);
		regularUserCartSteps.selectDeliveryDate(genProduct3.getSku(), deliveryTP2Locale);
		regularUserCartSteps.selectDeliveryDate(genProduct4.getSku(), deliveryTP1Locale);

		regularUserCartSteps.verifyMultipleDeliveryOption();

		orderForCustomerCartSteps.typeCouponCode(voucherCode);

		orderForCustomerCartSteps.grabProductsData();
		orderForCustomerCartSteps.grabTotals(voucherCode);
		HostCartCalculator.calculateOrderForCustomerTotals(discountClass, shippingValue, voucherValue);
		HostCartCalculator.calculateShippingTotals(shippingValue, voucherValue, discountClass);

		orderForCustomerCartSteps.clickGoToShipping();

		shippingPartySectionSteps.clickPartyNoOption();
		shippingPartySectionSteps.checkItemNotReceivedYet();

		shippingPartySectionSteps.enterPLZ(plz);
		shippingPartySectionSteps.selectCountry(country);

		shippingSteps.grabHostProductsListTp0();
		shippingSteps.grabHostProductsListTp1();
		shippingSteps.grabHostProductsListTp2();

		HostDataGrabber.hostShippingTotalsTp0 = shippingSteps.grabSurveyDataTp0();
		HostDataGrabber.hostShippingTotalsTp1 = shippingSteps.grabSurveyDataTp1();
		HostDataGrabber.hostShippingTotalsTp2 = shippingSteps.grabSurveyDataTp2();

		shippingSteps.goToPaymentMethod();

		String url = shippingSteps.grabUrl();
		String orderId = FormatterUtils.getOrderId(url);
		HostDataGrabber.orderModel.setOrderId(FormatterUtils.incrementOrderId(orderId, 1));
		HostDataGrabber.orderModel.setTotalPrice(FormatterUtils.extractPriceFromURL(url));
		HostDataGrabber.orderModelTp1.setOrderId(FormatterUtils.incrementOrderId(orderId, 2));
		HostDataGrabber.orderModelTp1.setDeliveryDate(deliveryTP1);
		HostDataGrabber.orderModelTp2.setOrderId(FormatterUtils.incrementOrderId(orderId, 3));
		HostDataGrabber.orderModelTp2.setDeliveryDate(deliveryTP2);

		paymentSteps.expandCreditCardForm();
		paymentSteps.fillCreditCardForm(creditCardData);

		confirmationSteps.grabHostProductsListTp0();
		confirmationSteps.grabHostProductsListTp1();
		confirmationSteps.grabHostProductsListTp2();

		HostDataGrabber.hostConfirmationTotalsTp0 = confirmationSteps.grabConfirmationTotalsTp0();
		HostDataGrabber.hostConfirmationTotalsTp1 = confirmationSteps.grabConfirmationTotalsTp1();
		HostDataGrabber.hostConfirmationTotalsTp2 = confirmationSteps.grabConfirmationTotalsTp2();

		confirmationSteps.grabBillingData();
		confirmationSteps.grabSippingData();

		confirmationSteps.agreeAndCheckout();

		hostCartValidationWorkflows.setBillingShippingAddress(billingAddress, billingAddress);
		hostCartValidationWorkflows.performTpCartValidationsWithVoucher();

		customVerifications.printErrors();

	}

	@After
	public void saveData() {
		MongoWriter.saveOrderModel(HostDataGrabber.orderModel, getClass().getSimpleName() + "TP0");
		MongoWriter.saveOrderModel(HostDataGrabber.orderModelTp1, getClass().getSimpleName() + "TP1");
		MongoWriter.saveOrderModel(HostDataGrabber.orderModelTp2, getClass().getSimpleName() + "TP2");
		MongoWriter.saveShippingModel(HostCartCalculator.shippingCalculatedModel, getClass().getSimpleName() + "TP0");
		MongoWriter.saveShippingModel(HostCartCalculator.shippingCalculatedModelTp1,
				getClass().getSimpleName() + "TP1");
		MongoWriter.saveShippingModel(HostCartCalculator.shippingCalculatedModelTp2,
				getClass().getSimpleName() + "TP2");
		for (HostBasicProductModel product : HostCartCalculator.allProductsListTp0) {
			MongoWriter.saveHostBasicProductModel(product, getClass().getSimpleName() + "TP0");
		}
		for (HostBasicProductModel product : HostCartCalculator.allProductsListTp1) {
			MongoWriter.saveHostBasicProductModel(product, getClass().getSimpleName() + "TP1");
		}
		for (HostBasicProductModel product : HostCartCalculator.allProductsListTp2) {
			MongoWriter.saveHostBasicProductModel(product, getClass().getSimpleName() + "TP2");
		}

		try {
			ApacheHttpHelper.sendGet(EnvironmentConstants.RUN_IP_SCRIPT_JOB_URL,
					EnvironmentConstants.USERNAME_JENKINS_COMM, EnvironmentConstants.PASSWORD_JENKINS_COMM);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}