package com.tests.uss15.us15002;

import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Story;
import net.thucydides.core.annotations.WithTag;
import net.thucydides.junit.runners.ThucydidesRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.connectors.mongo.MongoConnector;
import com.steps.external.mailchimp.MailchimpListsSteps;
import com.steps.external.mailchimp.MailchimpLoginSteps;
import com.steps.external.mailchimp.MailchimpSearchSteps;
import com.steps.external.mailchimp.MailchimpSubscriberProfileSteps;
import com.tests.BaseTest;
import com.tools.data.frontend.CustomerFormModel;
import com.tools.data.frontend.DateModel;
import com.tools.data.frontend.RegularBasicProductModel;
import com.tools.data.frontend.ShippingModel;
import com.tools.data.newsletter.SubscriberModel;
import com.tools.env.constants.ConfigConstants;
import com.tools.env.variables.Credentials;
import com.tools.persistance.MongoReader;
import com.tools.requirements.Application;
import com.tools.utils.PrintUtils;
import com.workflows.mailchimp.MailchimpValidationWorkflows;

@WithTag(name = "US15.2 Check registered user with kobo all states in mailchimp ", type = "Scenarios")
@Story(Application.Newsletter.US15_2.class)
@RunWith(ThucydidesRunner.class)
public class US15002CheckRevenuesZeroInMailchimpConfigTest extends BaseTest {

	@Steps
	public MailchimpLoginSteps mailchimpLoginSteps;
	@Steps
	public MailchimpListsSteps mailchimpListsSteps;
	@Steps
	public MailchimpSearchSteps mailchimpSearchSteps;
	@Steps
	public MailchimpValidationWorkflows mailchimpValidationWorkflows;
	@Steps
	public MailchimpSubscriberProfileSteps mailchimpSubscriberProfileSteps;

	private SubscriberModel grabbedSubscriberModel = new SubscriberModel();
	private SubscriberModel expectedSubscriberModel = new SubscriberModel();
	private ShippingModel shippingModel = new ShippingModel();
	private RegularBasicProductModel product = new RegularBasicProductModel();
	private CustomerFormModel dataModel;
	private DateModel dateModel;
	private String koboCode;
	private String listName = "staging_AUT_newsletter_all_subscribers";

	@Before
	public void setUp() {

		product = MongoReader.grabRegularBasicProductModel("US15002SubscribedCustomerBuyWithContactBoosterTest").get(2);
		shippingModel = MongoReader.grabShippingModel("US15002SubscribedCustomerBuyWithContactBoosterTest").get(0);
		dataModel = MongoReader.grabCustomerFormModels("US15002KoboRegistrationNewsletterSubscribeTest").get(0);
		dataModel.setEmailName(dataModel.getEmailName().replace(ConfigConstants.MAILINATOR, ConfigConstants.EVOZON));
		dateModel = MongoReader.grabDateModels("US15002ConfirmCustomerTest").get(0);
		koboCode = MongoReader.grabKoboModel("US15002KoboRegistrationNewsletterSubscribeTest");
		MongoConnector.cleanCollection(getClass().getSimpleName());
	
	}

	@Test
	public void us15002CheckRevenuesZeroInMailchimpConfigTest() {

		mailchimpLoginSteps.loginOnMailchimp(Credentials.MAILCHIMP_USERNAME, Credentials.MAILCHIMP_PASSWORD);
		mailchimpListsSteps.goToDesiredList(listName);
		mailchimpSearchSteps.searchForSubscriber(dataModel.getEmailName());
		grabbedSubscriberModel = mailchimpSubscriberProfileSteps.grabSubribersData();

		expectedSubscriberModel = mailchimpValidationWorkflows.populateNewCustomerWithKoboFromExistingDataWithZeroRevenue(dataModel, dateModel, product, shippingModel, koboCode);

		 System.out.println("---------------");
		 PrintUtils.printCustomerSubscriberData(expectedSubscriberModel);
		 mailchimpValidationWorkflows.validateNewCustomerOrderWithKoboMailchimpProperties(grabbedSubscriberModel,
		 expectedSubscriberModel);
	}
}
