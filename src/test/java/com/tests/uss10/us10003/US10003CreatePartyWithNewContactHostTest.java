
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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.connectors.mongo.MongoConnector;
import com.steps.frontend.CustomerRegistrationSteps;
import com.steps.frontend.HeaderSteps;
import com.steps.frontend.PartyCreationSteps;
import com.steps.frontend.PartyDetailsSteps;
import com.steps.frontend.registration.party.CreateNewContactSteps;
import com.tests.BaseTest;
import com.tools.SoapKeys;
import com.tools.data.UrlModel;
import com.tools.data.frontend.AddressModel;
import com.tools.data.frontend.CustomerFormModel;
import com.tools.data.frontend.DateModel;
import com.tools.env.variables.UrlConstants;
import com.tools.persistance.MongoWriter;
import com.tools.requirements.Application;

@WithTag(name = "US10", type = "frontend")
@Story(Application.StyleParty.class)
@RunWith(ThucydidesRunner.class)
public class US10003CreatePartyWithNewContactHostTest extends BaseTest {

	@Steps
	public CustomerRegistrationSteps customerRegistrationSteps;
	@Steps
	public CreateNewContactSteps createNewContactSteps;
	@Steps
	public HeaderSteps headerSteps;
	@Steps
	public PartyDetailsSteps partyDetailsSteps;
	@Steps
	public PartyCreationSteps partyCreationSteps;

	
	public static UrlModel urlModel = new UrlModel();
	public static DateModel dateModel = new DateModel();
	private String username, password;
	
	public CustomerFormModel customerData;
	public CustomerFormModel inviteData;
	public AddressModel addressData;


	@Before
	public void setUp() throws Exception {
		
		customerData = new CustomerFormModel();
		inviteData = new CustomerFormModel();
		addressData = new AddressModel();
		addressData.setCountryName("España");

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

		MongoConnector.cleanCollection(getClass().getSimpleName() + SoapKeys.GRAB);
		MongoConnector.cleanCollection(getClass().getSimpleName());
	}

	@Test
	public void us10003CreatePartyWithNewContactHostTest() {
		customerRegistrationSteps.performLogin(username, password);
		headerSteps.goToCreatePartyWithNewContactPage();
		createNewContactSteps.fillCreateNewContact(customerData, addressData);
		urlModel.setUrl(partyCreationSteps.fillPartyDetailsForNewCustomerHost());
		dateModel.setDate(String.valueOf(System.currentTimeMillis()));
		partyDetailsSteps.verifyPlannedPartyAvailableActions();
		partyDetailsSteps.sendInvitationToGest(inviteData);
		partyDetailsSteps.verifyThatGuestIsInvited(inviteData.getFirstName());
	
	}

	@After
	public void saveData() {
		MongoWriter.saveCustomerFormModel(customerData, getClass().getSimpleName()+ SoapKeys.GRAB);
		MongoWriter.saveCustomerFormModel(inviteData, getClass().getSimpleName());
		MongoWriter.saveUrlModel(urlModel, getClass().getSimpleName() + SoapKeys.GRAB);
		MongoWriter.saveDateModel(dateModel, getClass().getSimpleName() + SoapKeys.GRAB);
	}
}

