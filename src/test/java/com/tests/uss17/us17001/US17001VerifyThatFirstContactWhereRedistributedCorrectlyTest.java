package com.tests.uss17.us17001;

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
import com.steps.frontend.LoungeSteps;
import com.steps.frontend.MyContactsListSteps;
import com.tests.BaseTest;
import com.tools.data.frontend.CustomerFormModel;
import com.tools.data.frontend.DateModel;
import com.tools.env.constants.FilePaths;
import com.tools.env.variables.UrlConstants;
import com.tools.persistance.MongoReader;
import com.tools.requirements.Application;

@WithTag(name = "US17", type = "backend")
@Story(Application.MassAction.class)
@RunWith(ThucydidesRunner.class)
public class US17001VerifyThatFirstContactWhereRedistributedCorrectlyTest extends BaseTest {

	@Steps
	public CustomerRegistrationSteps customerRegistrationSteps;
	@Steps
	public LoungeSteps loungeSteps;
	@Steps
	public FooterSteps footerSteps;
	@Steps
	public HeaderSteps headerSteps;
	@Steps
	public MyContactsListSteps myContactsListSteps;

	public CustomerFormModel stylistRegistrationData;

	public CustomerFormModel contactModel;
	public CustomerFormModel customerModel;
	public DateModel dateModel;

	private String secondStyleCoachUsername;
	private String secondStyleCoachPassword;

	@Before
	public void setUp() throws Exception {

		Properties prop = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream(UrlConstants.RESOURCES_PATH + FilePaths.US_17_FOLDER + File.separator + "us17001.properties");
			prop.load(input);

			secondStyleCoachUsername = prop.getProperty("secondStyleCoachUsername");
			secondStyleCoachPassword = prop.getProperty("secondStyleCoachPassword");

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
		customerModel = MongoReader.grabCustomerFormModels("US17001SecondRegularCustomerRegistrationTest").get(0);
		contactModel = MongoReader.grabCustomerFormModels("US17001AddNewContactToStyleCoachTest").get(0);
		dateModel = MongoReader.grabStylistDateModels("US17001AddSecondNewContactToStyleCoachTest").get(0);

		MongoConnector.cleanCollection(getClass().getSimpleName());
	}

	@Test
	public void us17001VerifyThatFirstContactWhereRedistributedCorrectlyTest() {

		customerRegistrationSteps.performLogin(secondStyleCoachUsername, secondStyleCoachPassword);
		if (!headerSteps.succesfullLogin()) {
			footerSteps.selectWebsiteFromFooter(MongoReader.getContext());
		}
		headerSteps.selectLanguage(MongoReader.getContext());
		loungeSteps.goToContactsList();
		myContactsListSteps.verifyThatContactIsInTheList(customerModel.getEmailName());
		myContactsListSteps.verifyThatContactIsUniqueInStylecoachList(customerModel.getFirstName());
//		myContactsListSteps.verifyThatContactMatchesAllTerms(contactModel.getEmailName(), dateModel.getDate());
		myContactsListSteps.verifyThatContactIsUniqueInStylecoachList(contactModel.getFirstName());

	}

}
