package com.tests.uss21.uss21001;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Story;
import net.thucydides.core.annotations.WithTag;
import net.thucydides.junit.runners.ThucydidesRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.steps.frontend.CustomerRegistrationSteps;
import com.steps.frontend.DashboardSteps;
import com.steps.frontend.FooterSteps;
import com.steps.frontend.HeaderSteps;
import com.steps.frontend.reports.JewelryBonusHistorySteps;
import com.tests.BaseTest;
import com.tools.data.backend.RewardPointsOfStylistModel;
import com.tools.env.constants.FilePaths;
import com.tools.env.variables.UrlConstants;
import com.tools.persistance.MongoReader;
import com.tools.requirements.Application;

@WithTag(name = "US21", type = "frontend")
@Story(Application.ZzzProducts.class)
@RunWith(ThucydidesRunner.class)
public class US21001CheckClosedMonthFrontendRewardsOnStylistTest extends BaseTest {

	@Steps
	public HeaderSteps headerSteps;
	@Steps
	public FooterSteps footerSteps;
	@Steps
	public CustomerRegistrationSteps customerRegistrationSteps;
	@Steps
	public JewelryBonusHistorySteps jewelryBonusHistorySteps;
	@Steps
	public DashboardSteps dashboardSteps;

	private String username, password;

	private RewardPointsOfStylistModel finalRewardPointsOfStylistModel = new RewardPointsOfStylistModel();

	@Before
	public void setUp() {
		
		Properties prop = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream(UrlConstants.RESOURCES_PATH + FilePaths.US_21_FOLDER + File.separator + "us21001.properties");
			prop.load(input);
			username = prop.getProperty("username");
			password = prop.getProperty("password");

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


		finalRewardPointsOfStylistModel = MongoReader.grabReviewPoints("US21001CheckStylistBonusesAfterClosedMonthTest").get(0);

	}

	@Test
	public void us21001CheckClosedMonthFrontendRewardsOnStylistTest() {
		customerRegistrationSteps.performLogin(username, password);
		if (!headerSteps.succesfullLogin()) {
			footerSteps.selectWebsiteFromFooter(MongoReader.getContext());
		}
		headerSteps.selectLanguage(MongoReader.getContext());
		headerSteps.goToProfile();

		String dashboardTotalJb = dashboardSteps.getJewelryBonusWithFourDecimals();
		String dashboardTotalMmb = dashboardSteps.getMarketingMaterialBonusWithFourDecimals();

		dashboardSteps.validateDashboardTotalJewerlyBonus(finalRewardPointsOfStylistModel.getJewelryBonus(), dashboardTotalJb);
		dashboardSteps.validateDashboardTotalMarketingBonus(finalRewardPointsOfStylistModel.getMarketingMaterialBonus(), dashboardTotalMmb);

	}

}
