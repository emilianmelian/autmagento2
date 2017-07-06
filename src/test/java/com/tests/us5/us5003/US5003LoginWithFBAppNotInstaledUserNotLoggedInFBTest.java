package com.tests.us5.us5003;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import com.steps.backend.BackEndSteps;
import com.steps.external.FacebookRegistrationSteps;
import com.steps.external.ospm.OnlineStylePartyManagerSteps;
import com.steps.frontend.HeaderSteps;
import com.tests.BaseTest;
import com.tools.constants.FilePaths;
import com.tools.constants.UrlConstants;

import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Steps;

@RunWith(SerenityRunner.class)
public class US5003LoginWithFBAppNotInstaledUserNotLoggedInFBTest extends BaseTest {
	@Steps
	public BackEndSteps backEndSteps;
	@Steps
	public FacebookRegistrationSteps facebookRegistrationSteps;

	@Steps
	public HeaderSteps headerSteps;

	@Steps
	OnlineStylePartyManagerSteps fBpermissionSteps;

	private String fbUser, fbPass;
	public WebDriver driverx;

	@Before
	public void setUp() throws Exception {

		Properties prop = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream(
					UrlConstants.RESOURCES_PATH + FilePaths.US_05_FOLDER + File.separator + "us5001.properties");
			prop.load(input);
			fbUser = prop.getProperty("fbUser");
			fbPass = prop.getProperty("fbPass");
			

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

		System.out.println("fbUser" + fbUser);
		System.out.println("fbPass" + fbPass);
	}

	@Test
	public void us5003LoginWithFBAppNotInstaledUserNotLoggedInFBTest() {
		facebookRegistrationSteps.goToFacebookLogin();
		facebookRegistrationSteps.loginToFacebookPippa(fbUser, fbPass);
		fBpermissionSteps.acceptAllThePermissionsFBRegistration();
		facebookRegistrationSteps.switchToMainPage();
		headerSteps.checkSucesfullLoginDE();

	}
}
