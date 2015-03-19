package com.tests.us6.us6003;

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

import com.steps.backend.BackEndSteps;
import com.steps.frontend.CustomerRegistrationSteps;
import com.steps.frontend.FooterSteps;
import com.steps.frontend.HeaderSteps;
import com.steps.frontend.HomeSteps;
import com.steps.frontend.StylistRegistrationSteps;
import com.tests.BaseTest;
import com.tools.Constants;
import com.tools.requirements.Application;

@WithTag(name = "US6003", type = "frontend")
@Story(Application.StyleCoach.Shopping.class)
@RunWith(ThucydidesRunner.class)
public class US6003StyleCoachRegistrationTest extends BaseTest {

	@Steps
	public FooterSteps footerSteps;
	@Steps
	public HeaderSteps headerSteps;
	@Steps
	public HomeSteps homeSteps;
	@Steps
	public BackEndSteps backEndSteps;
	@Steps
	public StylistRegistrationSteps stylistRegistrationSteps;
	@Steps
	public CustomerRegistrationSteps customerRegistrationSteps;

	private String username;
	private String password;

	@Before
	public void setUp(){

		Properties prop = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream(Constants.RESOURCES_PATH + "us6" + File.separator + "us6003.properties");
			prop.load(input);
			username = prop.getProperty("username");
			password = prop.getProperty("password");
		}catch (IOException ex) {
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
	public void us6003NavigateToStyleCoachRegisterPageTest() {

		// navigate to register page without being logged in
		headerSteps.navigateToRegisterForm();
		stylistRegistrationSteps.validateStylistRegisterPageTitle();

		headerSteps.navigateToRegisterFormFromStylistRegistrationLinkAndStarteJetzButton();
		stylistRegistrationSteps.validateStylistRegisterPageTitle();

		homeSteps.navigateToRegisterFormFromStyleCoachLinkAndJetzStarten();
		stylistRegistrationSteps.validateStylistRegisterPageTitle();

		homeSteps.navigateToRegisterFormFromStyleCoachLinkAndStarteJetzt();
		stylistRegistrationSteps.validateStylistRegisterPageTitle();

		// navigate to register page being logged in as regular customer
		customerRegistrationSteps.navigateToLoginPageAndPerformLogin(username, password);

		homeSteps.navigateToRegisterFormFromStyleCoachLinkAndJetzStarten();
		stylistRegistrationSteps.validateStylistRegisterPageTitle();

		homeSteps.navigateToRegisterFormFromStyleCoachLinkAndStarteJetzt();
		stylistRegistrationSteps.validateStylistRegisterPageTitle();

		footerSteps.navigateToRegisterFormFromRegistrierungLink();
		stylistRegistrationSteps.validateStylistRegisterPageTitle();

		footerSteps.navigateToRegisterFormFromStarterSetLink();
		stylistRegistrationSteps.validateStylistRegisterPageTitle();

		footerSteps.navigateToRegisterFormFromTrainingLink();
		stylistRegistrationSteps.validateStylistRegisterPageTitle();

		footerSteps.navigateToRegisterFormFromIncentivereisenLink();
		stylistRegistrationSteps.validateStylistRegisterPageTitle();

		footerSteps.navigateToRegisterFormFromErfolgsgeschichtenLink();
		stylistRegistrationSteps.validateStylistRegisterPageTitle();

		footerSteps.navigateToRegisterFormFromTraumkarriereStyleCoachLink();
		stylistRegistrationSteps.validateStylistRegisterPageTitle();

	}

}