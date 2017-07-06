package com.tests.uss17.us17001;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Story;
import net.thucydides.core.annotations.WithTag;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.connectors.mongo.MongoConnector;
import com.steps.frontend.ContactDetailsSteps;
import com.steps.frontend.CustomerRegistrationSteps;
import com.steps.frontend.FooterSteps;
import com.steps.frontend.HeaderSteps;
import com.steps.frontend.LoungeSteps;
import com.steps.frontend.MyContactsListSteps;
import com.tests.BaseTest;
import com.tools.CustomVerification;
import com.tools.constants.ContextConstants;
import com.tools.constants.FilePaths;
import com.tools.constants.UrlConstants;
import com.tools.data.frontend.AddressModel;
import com.tools.data.frontend.ContactModel;
import com.tools.data.frontend.CustomerFormModel;
import com.tools.data.frontend.DateModel;
import com.tools.persistance.MongoReader;
import com.tools.requirements.Application;
import com.tools.utils.PrintUtils;
import com.workflows.frontend.contact.ContactValidationWorkflows;

@WithTag(name = "US17.1 Check reassigned duplicate contacts and customer associated contacts when new SC is selected", type = "Scenarios")
@Story(Application.MassAction.US17_1.class)
@RunWith(SerenityRunner.class)
public class US17001VerifyThatContactWasReassignedToUnqualifiedSelectedScTest extends BaseTest {

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
	@Steps
	public ContactValidationWorkflows contactValidationWorkflows;
	@Steps
	public ContactDetailsSteps contactDetailsSteps;
	@Steps
	public CustomVerification customVerifications;

	private CustomerFormModel oldStylistModel;
	private CustomerFormModel contactModel;
	private DateModel dateModel;
	private AddressModel addressModel;
	private ContactModel expectedDetailsModel = new ContactModel();
	private ContactModel grabbedDetailsModel;
	private String stylecoachUsername;
	private String stylecoachPassword;

	@Before
	public void setUp() throws Exception {

		Properties prop = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream(UrlConstants.RESOURCES_PATH + FilePaths.US_17_FOLDER + File.separator + "us17001.properties");
			prop.load(input);
			
			stylecoachUsername = prop.getProperty("stylecoachUsername");
			stylecoachPassword = prop.getProperty("stylecoachPassword");

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
		oldStylistModel = MongoReader.grabCustomerFormModels("US17001StyleCoachRegistrationTest").get(0);
		contactModel = MongoReader.grabCustomerFormModels("US17001AddThirdContactToStyleCoachTest").get(0);
		dateModel = MongoReader.grabDateModels("US17001AddForthContactToStyleCoachTest").get(0);
		addressModel = MongoReader.grabAddressModels("US17001AddForthContactToStyleCoachTest").get(0);
		
		expectedDetailsModel = contactValidationWorkflows.populateExpectedContactModel(oldStylistModel,contactModel, dateModel, addressModel); 

		expectedDetailsModel.setHasPartyHostInterrest(true);
		expectedDetailsModel.setHasStyleCoachInterrest(false);
		expectedDetailsModel.setIsNewsletterSubscribed(true);
		
		

		PrintUtils.printContactModel(expectedDetailsModel);

		MongoConnector.cleanCollection(getClass().getSimpleName());
	}

	@Test
	public void us17001VerifyThatContactWasReassignedToUnqualifiedSelectedScTest() {

		customerRegistrationSteps.performLogin(stylecoachUsername, stylecoachPassword);
		if (!headerSteps.succesfullLogin()) {
			footerSteps.selectWebsiteFromFooter(MongoReader.getContext());
		}
		headerSteps.selectLanguage(MongoReader.getContext());
		loungeSteps.goToContactsList();
		myContactsListSteps.verifyUnicAndOpenContactDetails(contactModel.getFirstName(),contactModel.getLastName());

	//	myContactsListSteps.verifyUnicAndOpenContactDetails(contactModel.getFirstName(), dateModel.getDate());
		grabbedDetailsModel = contactDetailsSteps.grabContactDetails();
		contactValidationWorkflows.validateContactDetails(expectedDetailsModel, grabbedDetailsModel);
		customVerifications.printErrors();

	}

}
