package com.tests.us1;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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

import com.connectors.gmail.GmailConnector;
import com.steps.EmailSteps;
import com.steps.frontend.CustomerRegistrationSteps;
import com.steps.frontend.HeaderSteps;
import com.steps.frontend.ProfileSteps;
import com.tests.BaseTest;
import com.tools.Constants;
import com.tools.data.OrderModel;
import com.tools.persistance.MongoWriter;
import com.tools.requirements.Application;


@WithTag(name = "US001", type = "frontend")
@Story(Application.StyleCoach.Shopping.class)
@RunWith(ThucydidesRunner.class)
public class US001ValidateUserProfileOrderTest extends BaseTest{
	
	@Steps
	public CustomerRegistrationSteps frontEndSteps;
	@Steps
	public HeaderSteps headerSteps;
	@Steps
	public ProfileSteps profileSteps;
	@Steps
	public EmailSteps emailSteps;
	
	private String username, password;
	private OrderModel orderNumber = new OrderModel();
	
	@Before
	public void setUp() throws Exception {
		Properties prop = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream(Constants.RESOURCES_PATH + "us1\\us001.properties");
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
	}
	
	@Test
	public void us001ValidateUserProfileOrderTest() {
		frontEndSteps.performLogin(username, password);
		headerSteps.goToProfile();
		profileSteps.openProfileHistory();
		List<OrderModel> orderHistory = profileSteps.grabOrderHistory();
		System.out.println("ORDER ID: " + orderHistory.get(0).getOrderId());
		
		String orderId = orderHistory.get(0).getOrderId();
		orderNumber.setOrderId(orderId);
		String message = GmailConnector.searchForMail("", orderHistory.get(0).getOrderId(), false);
		
		emailSteps.validateEmailContent(orderId, message);
	}
	
	
	@After
	public void saveData(){
		MongoWriter.saveOrderModel(orderNumber , getClass().getSimpleName());
	}

}