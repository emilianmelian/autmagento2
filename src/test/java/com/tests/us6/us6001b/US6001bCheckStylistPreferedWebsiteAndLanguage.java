package com.tests.us6.us6001b;

import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Story;
import net.thucydides.core.annotations.WithTag;
import net.thucydides.junit.runners.ThucydidesRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.steps.frontend.CustomerRegistrationSteps;
import com.tests.BaseTest;
import com.tools.data.frontend.CustomerFormModel;
import com.tools.persistance.MongoReader;
import com.tools.requirements.Application;

@WithTag(name = "US6", type = "backend")
@Story(Application.Registration.Stylist.class)
@RunWith(ThucydidesRunner.class)
public class US6001bCheckStylistPreferedWebsiteAndLanguage extends BaseTest {

	@Steps
	public CustomerRegistrationSteps customerRegistrationSteps;
	public CustomerFormModel stylistRegistrationData = new CustomerFormModel("");

	@Before
	public void setUp() throws Exception {

		int size = MongoReader.grabCustomerFormModels("US6001bStyleCoachRegistrationTest").size();
		if (size > 0) {
			stylistRegistrationData = MongoReader.grabCustomerFormModels("US6001bStyleCoachRegistrationTest").get(0);
		} else
			System.out.println("The database has no entries");
	}

	@Test
	public void us6001bCheckCustomerActivationTest() {

		customerRegistrationSteps.performLoginAndVerifyWebsiteAndLanguage(stylistRegistrationData.getEmailName(), stylistRegistrationData.getPassword(),MongoReader.getContext(),MongoReader.getContext());

	}
}
