package com.tests.uss10.us10003;

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

import com.steps.frontend.CustomerRegistrationSteps;
import com.steps.frontend.HeaderSteps;
import com.steps.frontend.LoungeSteps;
import com.steps.frontend.PartyCreationSteps;
import com.steps.frontend.PartyDetailsSteps;
import com.tests.BaseTest;
import com.tools.Constants;
import com.tools.data.UrlModel;
import com.tools.data.frontend.DateModel;
import com.tools.persistance.MongoReader;
import com.tools.requirements.Application;

@WithTag(name = "US10003", type = "frontend")
@Story(Application.StyleParty.CreateParty.class)
@RunWith(ThucydidesRunner.class)
public class US10003ClosePartyTest extends BaseTest {

	@Steps
	public CustomerRegistrationSteps customerRegistrationSteps;
	@Steps
	public HeaderSteps headerSteps;
	@Steps
	public LoungeSteps loungeSteps;
	@Steps
	public PartyCreationSteps partyCreationSteps;
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

			input = new FileInputStream(Constants.RESOURCES_PATH + "uss10" + File.separator + "us10003.properties");
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

		urlModel = MongoReader.grabUrlModels("US10003CreatePartyWithNewContactHostTest" + Constants.GRAB).get(0);
		dateModel = MongoReader.grabStylistDateModels("US10003CreatePartyWithNewContactHostTest" + Constants.GRAB).get(0);

		Long partyCreationTime = Long.parseLong(dateModel.getDate());
		Long currentTime = System.currentTimeMillis();
		
		// if less than 15 minutes passed skip the test
		if (currentTime - partyCreationTime < 90000) {
			runTest = false;
		}

	}

	@Test
	public void us10003ClosePartyTest() {
		if (runTest) {
			customerRegistrationSteps.performLogin(username, password);
			customerRegistrationSteps.navigate(urlModel.getUrl());
			partyDetailsSteps.closeTheParty(Constants.TEN);
//			partyDetailsSteps.verifyThatPartyIsClosed();
		}
	}

}
