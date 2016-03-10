package com.tests.us3.us3001;

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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.connectors.gmail.GmailConnector;
import com.steps.EmailSteps;
import com.steps.frontend.CustomerRegistrationSteps;
import com.steps.frontend.HeaderSteps;
import com.steps.frontend.ProfileSteps;
import com.tests.BaseTest;
import com.tools.CustomVerification;
import com.tools.data.backend.OrderModel;
import com.tools.data.email.EmailCredentialsModel;
import com.tools.env.constants.EmailConstants;
import com.tools.env.constants.FilePaths;
import com.tools.env.constants.SoapKeys;
import com.tools.env.constants.UrlConstants;
import com.tools.persistance.MongoReader;
import com.tools.requirements.Application;

@WithTag(name = "US3.1 Shop for myself VAT valid and no SMB billing and shipping AT",type = "Scenarios")
@Story(Application.ShopForMyselfCart.US3_1.class)
@RunWith(ThucydidesRunner.class)
public class US3001ValidateOrderEmailTest extends BaseTest{
	
	@Steps
	public CustomerRegistrationSteps frontEndSteps;
	@Steps
	public HeaderSteps headerSteps;
	@Steps
	public ProfileSteps profileSteps;
	@Steps
	public EmailSteps emailSteps;
	@Steps 
	public CustomVerification customVerifications;
	
	private String username, password, emailPassword;
	private List<OrderModel> orderModel = new ArrayList<OrderModel>();
	
	@Before
	public void setUp() throws Exception {
		Properties prop = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream(UrlConstants.RESOURCES_PATH + FilePaths.US_03_FOLDER + File.separator + "us3001.properties");
			prop.load(input);
			username = prop.getProperty("username");
			password = prop.getProperty("password");
			emailPassword = prop.getProperty("Emailpassword");

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
		
		orderModel = MongoReader.getOrderModel("US3001SfmValidVatNoSmbBillingShippingNotDeTest" + SoapKeys.GRAB);
		
		EmailCredentialsModel emailData = new EmailCredentialsModel();
		
		emailData.setHost(EmailConstants.RECEIVING_HOST);
		emailData.setProtocol(EmailConstants.PROTOCOL);
		emailData.setUsername(username);
		emailData.setPassword(emailPassword);
        
		gmailConnector = new GmailConnector(emailData);
		System.out.println(orderModel.get(0).getOrderId());
	
	}
	
	@Test
	public void us3001ValidateOrderEmailTest() {
		frontEndSteps.performLogin(username, password);
		
		String message = gmailConnector.searchForMail("", orderModel.get(0).getOrderId(), true);
		emailSteps.validateEmailContent(orderModel.get(0).getOrderId(), message);
		
		customVerifications.printErrors();
	}

}
