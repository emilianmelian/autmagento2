package com.tests.uss10.uss10007;

import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Story;
import net.thucydides.core.annotations.WithTag;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.steps.backend.BackEndSteps;
import com.steps.backend.stylecoach.PartyListBackendSteps;
import com.tests.BaseTest;
import com.tools.CustomVerification;
import com.tools.constants.Credentials;
import com.tools.data.UrlModel;
import com.tools.data.backend.PartyBackendPerformanceModel;
import com.tools.data.frontend.ClosedPartyPerformanceModel;
import com.tools.persistance.MongoReader;
import com.tools.requirements.Application;
import com.tools.utils.PrintUtils;
import com.workflows.backend.BackendPartyPerformanceValidationWorkflows;

@WithTag(name = "US10.7 Check party and follow up party performance and bonuses", type = "Scenarios")
@Story(Application.PartyPerformance.US10_7.class)
@RunWith(SerenityRunner.class)
public class US10007ValidateFollowUpPartyBackendPerformanceTest extends BaseTest {

	@Steps
	public BackEndSteps backEndSteps;
	@Steps
	public BackendPartyPerformanceValidationWorkflows backendPartyPerformanceValidationWorkflows;
	@Steps
	public CustomVerification customVerifications;
	@Steps
	public PartyListBackendSteps partyListBackendSteps;
	
	private ClosedPartyPerformanceModel expectedModel;
	private static UrlModel urlModel = new UrlModel();
	private String partyId;

	@Before
	public void setUp() {
		urlModel = MongoReader.grabUrlModels("US10007CloseFollowUpPartyAnfVerifyCommissionBonusesTest").get(0);
		expectedModel = MongoReader.grabClosedPartyPerformanceModel("US10007CloseFollowUpPartyAnfVerifyCommissionBonusesTest").get(0);
		PrintUtils.printClosedPartyModel(expectedModel);
		// TODO make a method or smth for this
		String[] parts = urlModel.getUrl().split("/");
		partyId = parts[parts.length - 1];
		System.out.println("party Id:" + partyId);
	}

	@Test
	public void us10007ValidateFollowUpPartyBackendPerformanceTest() {
		backEndSteps.performAdminLogin(Credentials.BE_USER, Credentials.BE_PASS);
		backEndSteps.clickOnStyleParties();
		partyListBackendSteps.openPartyDetails(partyId);
		PartyBackendPerformanceModel grabbedModel = partyListBackendSteps.getPartyBackendPerformance();
		backendPartyPerformanceValidationWorkflows.validateClosedPartyPerformance(expectedModel, grabbedModel);

		customVerifications.printErrors();
	}

}