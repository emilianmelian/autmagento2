package com.tests.uss31.uss31006;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.steps.backend.BackEndSteps;
import com.steps.backend.termPurchase.TermPurchaseSystemConfigurationSteps;
import com.tests.BaseTest;
import com.tools.constants.ConfigConstants;
import com.tools.constants.Credentials;
import com.tools.requirements.Application;

import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Story;
import net.thucydides.core.annotations.WithTag;

@WithTag(name = "US31.1 TP execution cron - Manual", type = "Scenarios")
@Story(Application.TermPurchaseExecution.US31_3.class)
@RunWith(SerenityRunner.class)
public class US31006ActivateManualCronTest extends BaseTest {

	@Steps
	public BackEndSteps backEndSteps;
	@Steps
	public TermPurchaseSystemConfigurationSteps termPurchaseSystemConfigurationSteps;

	@Test
	public void us31006ActivateManualCronTest() {
		backEndSteps.performAdminLogin(Credentials.BE_USER, Credentials.BE_PASS);
		backEndSteps.clickOnSystemConfiguration();
		termPurchaseSystemConfigurationSteps.goToTermPurchaseTab();
		termPurchaseSystemConfigurationSteps.selectCronExecutionType(ConfigConstants.MANUAL);
	}
}