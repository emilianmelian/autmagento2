package com.tests.uss17.us17003;

import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Story;
import net.thucydides.core.annotations.WithTag;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.connectors.mongo.MongoConnector;
import com.steps.external.EmailClientSteps;
import com.tests.BaseTest;
import com.tools.constants.ConfigConstants;
import com.tools.constants.ContextConstants;
import com.tools.persistance.MongoReader;
import com.tools.requirements.Application;

@WithTag(name = "US17.3 Check reassigned contacts up on canceled SC hierarchy when no new Sc is selected", type = "Scenarios")
@Story(Application.MassAction.US17_3.class)
@RunWith(SerenityRunner.class)
public class US17003ConfirmStylecoachTest extends BaseTest {

	@Steps
	public EmailClientSteps emailClientSteps;

	private String stylistEmail;

	@Before
	public void setUp() throws Exception {

		stylistEmail = MongoReader.grabCustomerFormModels("US17003StyleCoachRegistrationTest").get(0).getEmailName();

		MongoConnector.cleanCollection(getClass().getSimpleName());
	}

	@Test
	public void us17003ConfirmStylecoachTest() {

		emailClientSteps.confirmAccount(stylistEmail.replace("@" + ConfigConstants.WEB_MAIL, ""), ContextConstants.CONFIRM_ACCOUNT_MAIL_SUBJECT);

	}

}