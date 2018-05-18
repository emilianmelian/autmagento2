package com.tests.us8.us8002;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Story;
import net.thucydides.core.annotations.WithTag;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.connectors.mongo.MongoConnector;
import com.steps.frontend.CustomerRegistrationSteps;
import com.steps.frontend.FooterSteps;
import com.steps.frontend.HeaderSteps;
import com.steps.frontend.profile.ProfileNavSteps;
import com.steps.frontend.profile.ProfileSteps;
import com.steps.frontend.reports.StylistsCustomerOrdersReportSteps;
import com.tests.BaseTest;
import com.tools.CustomVerification;
import com.tools.constants.ContextConstants;
import com.tools.constants.SoapKeys;
import com.tools.constants.UrlConstants;
import com.tools.data.backend.OrderModel;
import com.tools.persistance.MongoReader;
import com.tools.requirements.Application;


@WithTag(name = "US8.1 Customer Buy With Forthy Discounts And Jb Test", type = "Scenarios")
@Story(Application.RegularCart.US8_1.class)
@RunWith(SerenityRunner.class)
public class US8001ValidateOrderInStylistsCustomerOrderReportTest extends BaseTest{

	@Steps
	public StylistsCustomerOrdersReportSteps stylistsCustomerOrdersReportSteps;
	@Steps
	public HeaderSteps headerSteps;
	@Steps
	public FooterSteps footerSteps;
	@Steps
	public CustomerRegistrationSteps frontEndSteps;
	@Steps 
	public CustomVerification customVerifications;
	@Steps
	public ProfileNavSteps profileNavSteps;
	@Steps
	public ProfileSteps profileSteps;
	
	private static OrderModel orderModel = new OrderModel();
	private String stylistUsername, stylistPassword;
	

	@Before
	public void setUp() throws Exception {
		Properties prop = new Properties();
		InputStream input = null;

		try {
			input = new FileInputStream(UrlConstants.RESOURCES_PATH + "us8" + File.separator + "us8001.properties");
			prop.load(input);
			stylistUsername = prop.getProperty("stylistUsername");
			stylistPassword = prop.getProperty("stylistPassword");			

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


		MongoConnector.cleanCollection(getClass().getSimpleName() + SoapKeys.GRAB);
		MongoConnector.cleanCollection(getClass().getSimpleName() + SoapKeys.CALC);
		orderModel = MongoReader.grabOrderModels("US8001CustomerBuyWithForthyDiscountsAndJbTest" + SoapKeys.GRAB).get(0);
		
	}
	
	@Test
	public void us8001ValidateOrderInStylistsCustomerOrderReportTest() {
		
		frontEndSteps.performLogin(stylistUsername, stylistPassword);
		if (!headerSteps.succesfullLogin()) {

			footerSteps.selectWebsiteFromFooter(MongoReader.getContext());
		}
		
		headerSteps.goToProfile();
		profileNavSteps.selectMenu(ContextConstants.MEINE_BESTELLUNGEN);
		List<OrderModel> orderHistory = profileSteps.grabOrderDetails(orderModel.getOrderId());

		String orderId = orderHistory.get(0).getOrderId();
		String orderPrice = orderHistory.get(0).getTotalPrice();
		profileSteps.verifyOrderId(orderId, orderModel.getOrderId());
		profileSteps.verifyOrderPrice(orderPrice, orderModel.getTotalPrice());
		
		
		customVerifications.printErrors();
	}
}