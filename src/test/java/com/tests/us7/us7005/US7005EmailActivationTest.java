package com.tests.us7.us7005;

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

import com.connectors.gmail.GmailConnector;
import com.steps.EmailSteps;
import com.steps.external.EmailClientSteps;
import com.steps.frontend.CustomerRegistrationSteps;
import com.tests.BaseTest;
import com.tools.CustomVerification;
import com.tools.EmailConstants;
import com.tools.data.email.EmailCredentialsModel;
import com.tools.env.variables.ContextConstants;
import com.tools.env.variables.UrlConstants;
import com.tools.persistance.MongoReader;
import com.tools.requirements.Application;


@WithTag(name = "US7", type = "external")
@Story(Application.Registration.Customer.class)
@RunWith(ThucydidesRunner.class)
public class US7005EmailActivationTest extends BaseTest{

	@Steps
	public EmailClientSteps emailClientSteps;
	@Steps
	public EmailSteps emailSteps;
	@Steps
	public CustomVerification customVerifications;
	@Steps
	public CustomerRegistrationSteps frontEndSteps;
	
	public String clientName;
	public String validateURL;
	private String password;
	private String context;
	private String username;
	private String emailUser;
	private String emailPass;
	

	@Before
	public void setUp() throws Exception {
		
		Properties prop = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream(UrlConstants.RESOURCES_PATH + "us7" + File.separator + "us7005.properties");
			prop.load(input);
			username = prop.getProperty("username");
			context = prop.getProperty("context");
			
			emailUser = prop.getProperty("emailUser");
			emailPass = prop.getProperty("emailPass");

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

		int size = MongoReader.grabCustomerFormModels("US7005RegularKnownUserRegistrationLandingPageTest").size();
		if (size > 0) {
			clientName = MongoReader.grabCustomerFormModels("US7005RegularKnownUserRegistrationLandingPageTest").get(0).getEmailName();
			password = MongoReader.grabCustomerFormModels("US7005RegularKnownUserRegistrationLandingPageTest").get(0).getPassword();
			
			System.out.println(clientName);
		} else
			System.out.println("The database has no entries");
		
		EmailCredentialsModel emailData = new EmailCredentialsModel();

		emailData.setHost(EmailConstants.RECEIVING_HOST);
		emailData.setProtocol(EmailConstants.PROTOCOL);
		emailData.setUsername(emailUser);
		emailData.setPassword(emailPass);

		gmailConnector = new GmailConnector(emailData);
	}
	
	@Test
	public void us7005EmailActivationTest() {
		
//		frontEndSteps.performLogin(username, password);

		String message = gmailConnector.searchForMail("", ContextConstants.CONFIRM_ACCOUNT_MAIL_SUBJECT, true);

		System.out.println(message);
		String linkURL = emailSteps.grabConfirmationLink(message);
		System.out.println("urllllllllllllllllllllllllllll  " + linkURL);
		emailSteps.navigate(linkURL);
		emailSteps.validateEmail(password, message);
//		emailSteps.validateEmail(context, linkURL);

//		customVerifications.printErrors();


	}
}
