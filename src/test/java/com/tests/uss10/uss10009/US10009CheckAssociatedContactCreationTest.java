package com.tests.uss10.uss10009;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.steps.backend.BackEndSteps;
import com.steps.backend.stylecoach.StylecoachListBackendSteps;
import com.tests.BaseTest;
import com.tools.CustomVerification;
import com.tools.constants.Credentials;
import com.tools.data.frontend.AddressModel;
import com.tools.data.frontend.ContactDetailsModel;
import com.tools.data.frontend.CustomerFormModel;
import com.tools.persistance.MongoReader;
import com.tools.requirements.Application;
import com.workflows.backend.contact.ContactBackendValidationWorkflows;

import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Story;
import net.thucydides.core.annotations.WithTag;

@WithTag(name = "US6.1 Sc Registration New Customer Test ", type = "Scenarios")
@Story(Application.StylecoachRegistration.US6_1.class)
@RunWith(SerenityRunner.class)
public class US10009CheckAssociatedContactCreationTest extends BaseTest {

	@Steps
	public BackEndSteps backEndSteps;
	@Steps
	public StylecoachListBackendSteps stylecoachListBackendSteps;
	@Steps
	public ContactBackendValidationWorkflows contactBackendValidationWorkflows;
	@Steps
	public CustomVerification customVerifications;

	private ContactDetailsModel expectedContactDetailsModel;
	private CustomerFormModel customerFormModel;
	private AddressModel addressModel;
	private String prefferedStylist = "emilian melian (20)";

	@Before
	public void setUp() throws Exception {

		customerFormModel = MongoReader.grabCustomerFormModels("US10009GuestPageFromEmailCompleteCustomerRegTest").get(0);
		addressModel = MongoReader.grabAddressModels("US10009GuestPageFromEmailCompleteCustomerRegTest").get(0);
		expectedContactDetailsModel = contactBackendValidationWorkflows.populateSimpleContactDetailsModel(customerFormModel, addressModel, prefferedStylist);
	}

	@Test
	public void us10009CheckAssociatedContactCreationTest() {

		backEndSteps.performAdminLogin(Credentials.BE_USER, Credentials.BE_PASS);
		backEndSteps.clickOnContactList();
		stylecoachListBackendSteps.openContactDetails(customerFormModel.getEmailName());
		ContactDetailsModel grabbedContactDetailsModel = stylecoachListBackendSteps.grabContactDetails();
		contactBackendValidationWorkflows.verifyCreatedContactDetailsByAcceptinInvitation(grabbedContactDetailsModel, expectedContactDetailsModel);
		
		customVerifications.printErrors();
	}

}