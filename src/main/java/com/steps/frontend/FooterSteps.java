package com.steps.frontend;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.annotations.StepGroup;

import com.tools.requirements.AbstractSteps;

public class FooterSteps extends AbstractSteps {

	private static final long serialVersionUID = 1L;

	@Step
	public void clickRegistrierungLink() {
		footerPage().clickRegistrierungLink();
	}

	@Step
	public void clickStarterSetLink() {
		footerPage().clickStarterSetLink();
	}

	@Step
	public void clickIncentivereisenLink() {
		footerPage().clickIncentivereisenLink();
	}

	@Step
	public void clickTrainingLink() {
		footerPage().clickTrainingLink();
	}

	@Step
	public void clickErfolgsgeschichtenLink() {
		footerPage().clickErfolgsgeschichtenLink();
	}

	@Step
	public void clickTraumkarriereStyleCoachLink() {
		footerPage().clickTraumkarriereStyleCoachLink();
	}
	
	@StepGroup
	public void navigateToRegisterFormFromStarterSetLink() {
		footerPage().clickStarterSetLink();
		stylistCampaignPage().clickJetztStartenButton();
		starterSetPage().clickOnJetztStyleCoachWerdenButton();
	}

	@StepGroup
	public void navigateToRegisterFormFromTrainingLink() {
		footerPage().clickTrainingLink();
		stylistCampaignPage().clickJetztStartenButton();
		starterSetPage().clickOnJetztStyleCoachWerdenButton();
	}

	@StepGroup
	public void navigateToRegisterFormFromIncentivereisenLink() {
		footerPage().clickIncentivereisenLink();
		stylistCampaignPage().clickStarteJetztButton();
		starterSetPage().clickOnJetztStyleCoachWerdenButton();
	}

	@StepGroup
	public void navigateToRegisterFormFromErfolgsgeschichtenLink() {
		footerPage().clickErfolgsgeschichtenLink();
		stylistCampaignPage().clickStarteJetztButton();
		starterSetPage().clickOnJetztStyleCoachWerdenButton();
	}

	@StepGroup
	public void navigateToRegisterFormFromTraumkarriereStyleCoachLink() {
		footerPage().clickTraumkarriereStyleCoachLink();
		stylistCampaignPage().clickStarteJetztButton();
		starterSetPage().clickOnJetztStyleCoachWerdenButton();
	}
	
	@StepGroup
	public void navigateToRegisterFormFromRegistrierungLink() {
		footerPage().clickRegistrierungLink();

	}

}