package com.steps.external;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.annotations.StepGroup;

import com.tools.env.constants.TimeConstants;
import com.tools.persistance.MongoReader;
import com.tools.requirements.AbstractSteps;

public class FacebookRegistrationSteps extends AbstractSteps {

	private static final long serialVersionUID = -2649339632021723245L;

	@StepGroup
	public void goToFacebookLogin(String user, String pass) {
		clickOnFacebookLogin();
		findFrame("Facebook");
		performFacebookLogin(user, pass);
		waitABit(3000);
	}

	@Step
	public void confirmAccessRequest() {
		facebookEMBLoginConfirmPage().clickOnSubmmit();
	}

	@Step
	public void clickOnFacebookLogin() {
		getDriver().get(MongoReader.getBaseURL());
		headerPage().clickAnmeldenButton();
		loginPage().clickOnFacebookSignIn();
	}

	@Step
	public void performFacebookLogin(String user, String pass) {
		facebookEMBLoginPage().inputUser(user);
		facebookEMBLoginPage().inputPass(pass);
		facebookEMBLoginPage().clickLogin();
	}

	@Step
	public void fillFacebookRegistration(String zipCode, String selectOption, String passsword) {
		waitABit(TimeConstants.TIME_CONSTANT);
		findFrame("Create new account");
		facebookRegistrationFormPage().zipInput(zipCode);
		facebookRegistrationFormPage().countrySelect(selectOption);
		facebookRegistrationFormPage().passwordInput(passsword);
		facebookRegistrationFormPage().noSearchStyleCoach();
		facebookRegistrationFormPage().agreeAndConfirm();
	}
}
