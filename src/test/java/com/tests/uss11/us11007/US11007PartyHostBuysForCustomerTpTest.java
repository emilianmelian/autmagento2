package com.tests.uss11.us11007;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Locale;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.connectors.http.MagentoProductCalls;
import com.connectors.mongo.MongoConnector;
import com.steps.frontend.CustomerRegistrationSteps;
import com.steps.frontend.FooterSteps;
import com.steps.frontend.HeaderSteps;
import com.steps.frontend.PartyDetailsSteps;
import com.steps.frontend.checkout.CheckoutValidationSteps;
import com.steps.frontend.checkout.ConfirmationSteps;
import com.steps.frontend.checkout.PaymentSteps;
import com.steps.frontend.checkout.ShippingSteps;
import com.steps.frontend.checkout.cart.partyHost.OrderForCustomerCartSteps;
import com.steps.frontend.checkout.cart.regularCart.RegularUserCartSteps;
import com.steps.frontend.checkout.shipping.regularUser.ShippingPartySectionSteps;
import com.tests.BaseTest;
import com.tools.CustomVerification;
import com.tools.cartcalculations.partyHost.HostCartCalculator;
import com.tools.data.UrlModel;
import com.tools.data.frontend.CreditCardModel;
import com.tools.data.frontend.HostBasicProductModel;
import com.tools.data.soap.ProductDetailedModel;
import com.tools.datahandler.HostDataGrabber;
import com.tools.env.constants.SoapKeys;
import com.tools.env.constants.UrlConstants;
import com.tools.persistance.MongoReader;
import com.tools.persistance.MongoWriter;
import com.tools.requirements.Application;
import com.tools.utils.FormatterUtils;
import com.workflows.frontend.partyHost.AddProductsForCustomerWorkflow;
import com.workflows.frontend.partyHost.HostCartValidationWorkflows;

import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Story;
import net.thucydides.core.annotations.WithTag;

@WithTag(name = "US11.7 Party Host Buys For Customer With Term Purchase Test", type = "Scenarios")
@Story(Application.PlaceACustomerOrderCart.US11_7.class)
@RunWith(SerenityRunner.class)
public class US11007PartyHostBuysForCustomerTpTest extends BaseTest {

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
	public ConfirmationSteps confirmationSteps;
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
	private static UrlModel urlModel = new UrlModel();
	private ProductDetailedModel genProduct1;
	private ProductDetailedModel genProduct2;
	private ProductDetailedModel genProduct3;

	@Before
	public void setUp() throws Exception {
		HostCartCalculator.wipe();
		HostDataGrabber.wipe();

		genProduct1 = MagentoProductCalls.createProductModel();
		genProduct1.setPrice("29.00");
		genProduct1.setIp("25");
		MagentoProductCalls.createApiProduct(genProduct1);

		genProduct2 = MagentoProductCalls.createNotAvailableYetProductModel();
		genProduct2.setPrice("10.00");
		genProduct2.setIp("8");
		MagentoProductCalls.createApiProduct(genProduct2);

		genProduct3 = MagentoProductCalls.createNotAvailableYetProductModel();
		genProduct3.setPrice("29.90");
		genProduct3.setIp("25");
		MagentoProductCalls.createApiProduct(genProduct3);

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

		urlModel = MongoReader.grabUrlModels("US11002CreatePartyWithCustomerHostTest" + SoapKeys.GRAB).get(0);

		MongoConnector.cleanCollection(getClass().getSimpleName() + "TP0");
		MongoConnector.cleanCollection(getClass().getSimpleName() + "TP1");
		MongoConnector.cleanCollection(getClass().getSimpleName() + "TP2");

	}

	@Test
	public void us11007PartyHostBuysForCustomerTpTest() throws ParseException {
		customerRegistrationSteps.performLogin(username, password);
		if (!headerSteps.succesfullLogin()) {
			footerSteps.selectWebsiteFromFooter(MongoReader.getContext());
		}
		headerSteps.selectLanguage(MongoReader.getContext());
		customerRegistrationSteps.navigate(urlModel.getUrl());
		partyDetailsSteps.orderForCustomer();
		partyDetailsSteps.orderForCustomerFromParty(customerName);
		customerRegistrationSteps.wipeHostCart();
		HostBasicProductModel productData;

		productData = addProductsForCustomerWorkflow.setHostProductToCart(genProduct1, "1", "0");
		HostCartCalculator.allProductsListTp0.add(productData);
		productData = addProductsForCustomerWorkflow.setHostProductToCart(genProduct2, "2", "0");
		HostCartCalculator.allProductsListTp1.add(productData);
		productData = addProductsForCustomerWorkflow.setHostProductToCart(genProduct3, "4", "0");
		HostCartCalculator.allProductsListTp2.add(productData);

		HostCartCalculator.allProductsList.addAll(HostCartCalculator.allProductsListTp0);
		HostCartCalculator.allProductsList.addAll(HostCartCalculator.allProductsListTp1);
		HostCartCalculator.allProductsList.addAll(HostCartCalculator.allProductsListTp2);

		headerSteps.openCartPreview();
		headerSteps.goToCart();

		String deliveryTp1 = regularUserCartSteps.getDeliveryDate(genProduct2.getSku(),
				new Locale.Builder().setLanguage(MongoReader.getContext()).build());
		String deliveryTp2 = regularUserCartSteps.selectDeliveryDate(genProduct3.getSku(),
				new Locale.Builder().setLanguage(MongoReader.getContext()).build());

		regularUserCartSteps.verifyMultipleDeliveryOption();

		orderForCustomerCartSteps.typeCouponCode(voucherCode);
		orderForCustomerCartSteps.submitVoucherCode();

		orderForCustomerCartSteps.grabProductsData();
		orderForCustomerCartSteps.grabTotals(voucherCode);
		HostCartCalculator.calculateOrderForCustomerTotals(discountClass, shippingValue, voucherValue);
		HostCartCalculator.calculateShippingTotals(shippingValue, voucherValue, discountClass);

		orderForCustomerCartSteps.clickGoToShipping();
		shippingPartySectionSteps.checkItemNotReceivedYet();

		shippingPartySectionSteps.selectCountry(country);
		shippingPartySectionSteps.enterPLZ(plz);

		shippingSteps.grabHostProductsListTp0();
		shippingSteps.grabHostProductsListTp1();
		shippingSteps.grabHostProductsListTp2();

		HostDataGrabber.hostShippingTotalsTp0 = shippingSteps.grabSurveyDataTp0();
		HostDataGrabber.hostShippingTotalsTp1 = shippingSteps.grabSurveyDataTp1();
		HostDataGrabber.hostShippingTotalsTp2 = shippingSteps.grabSurveyDataTp2();

		shippingSteps.goToPaymentMethod();

		String url = shippingSteps.grabUrl();
		HostDataGrabber.orderModel.setOrderId(FormatterUtils.getOrderId(url, 1));
		HostDataGrabber.orderModel.setTotalPrice(FormatterUtils.extractPriceFromURL(url));
		HostDataGrabber.orderModelTp1.setOrderId(FormatterUtils.getOrderId(url, 2));
		HostDataGrabber.orderModelTp1.setDeliveryDate(deliveryTp1);
		HostDataGrabber.orderModelTp2.setOrderId(FormatterUtils.getOrderId(url, 3));
		HostDataGrabber.orderModelTp2.setDeliveryDate(deliveryTp2);

		if (!paymentSteps.isCreditCardFormExpended()) {
			paymentSteps.expandCreditCardForm();
		}
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
	}

}
