package com.tests.uss10.us10001;

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

import com.connectors.mongo.MongoConnector;
import com.steps.frontend.CustomerRegistrationSteps;
import com.steps.frontend.FooterSteps;
import com.steps.frontend.HeaderSteps;
import com.steps.frontend.PartyDetailsSteps;
import com.tests.BaseTest;
import com.tools.SoapKeys;
import com.tools.data.UrlModel;
import com.tools.data.frontend.DateModel;
import com.tools.env.variables.UrlConstants;
import com.tools.persistance.MongoReader;
import com.tools.requirements.Application;

@WithTag(name = "US10", type = "frontend")
@Story(Application.StyleParty.class)
@RunWith(ThucydidesRunner.class)
public class US10001ClosePartyTest extends BaseTest {
	@Steps
	public HeaderSteps headerSteps;
	@Steps
	public FooterSteps footerSteps;
	@Steps
	public CustomerRegistrationSteps customerRegistrationSteps;
	@Steps
	public PartyDetailsSteps partyDetailsSteps;
	public static UrlModel urlModel = new UrlModel();
	public static DateModel dateModel = new DateModel();
	private String username, password;
	boolean runTest = true;

	@Before
	public void setUp() throws Exception {

		Properties prop = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream(UrlConstants.RESOURCES_PATH + "uss10" + File.separator + "us10001.properties");
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

		MongoConnector.cleanCollection(getClass().getSimpleName());

		urlModel = MongoReader.grabUrlModels("US10001CreatePartyWithStylistHostTest" + SoapKeys.GRAB).get(0);
		dateModel = MongoReader.grabStylistDateModels("US10001CreatePartyWithStylistHostTest" + SoapKeys.GRAB).get(0);

		Long partyCreationTime = Long.parseLong(dateModel.getDate());
		Long currentTime = System.currentTimeMillis();

		// if less than 30 minutes passed skip the test
		if (currentTime - partyCreationTime < 1800000) {
			runTest = false;
		}

	}

	@Test
	public void us10001ClosePartyTest() {
		if (runTest) {
			customerRegistrationSteps.performLogin(username, password);
			if (!headerSteps.succesfullLogin()) {
				footerSteps.selectWebsiteFromFooter(MongoReader.getContext());
			}
			headerSteps.selectLanguage(MongoReader.getContext());
			customerRegistrationSteps.navigate(urlModel.getUrl());
			partyDetailsSteps.closeTheParty("10");
			partyDetailsSteps.verifyClosedPartyAvailableActions();
		}
	}

}
