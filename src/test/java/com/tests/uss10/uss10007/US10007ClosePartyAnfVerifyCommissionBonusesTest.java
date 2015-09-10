package com.tests.uss10.uss10007;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Story;
import net.thucydides.core.annotations.WithTag;
import net.thucydides.junit.runners.ThucydidesRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.steps.frontend.CustomerRegistrationSteps;
import com.steps.frontend.FooterSteps;
import com.steps.frontend.HeaderSteps;
import com.steps.frontend.PartyDetailsSteps;
import com.tests.BaseTest;
import com.tools.SoapKeys;
import com.tools.calculation.PartyBonusCalculation;
import com.tools.data.UrlModel;
import com.tools.data.frontend.PartyBonusCalculationModel;
import com.tools.env.variables.UrlConstants;
import com.tools.persistance.MongoReader;
import com.tools.requirements.Application;

@WithTag(name = "US10", type = "frontend")
@Story(Application.StyleParty.class)
@RunWith(ThucydidesRunner.class)
public class US10007ClosePartyAnfVerifyCommissionBonusesTest extends BaseTest {

	@Steps
	public HeaderSteps headerSteps;
	@Steps
	public FooterSteps footerSteps;
	@Steps
	public CustomerRegistrationSteps customerRegistrationSteps;
	@Steps
	public PartyDetailsSteps partyDetailsSteps;
	public static UrlModel urlModel = new UrlModel();
	List<PartyBonusCalculationModel> partyBonusCalculationModelList = new ArrayList<PartyBonusCalculationModel>();
	private String username, password;

	@Before
	public void setUp() throws Exception {

		Properties prop = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream(UrlConstants.RESOURCES_PATH + "uss10" + File.separator + "us10001.properties");
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

		

		partyBonusCalculationModelList.add(MongoReader.grabPartyBonusCalculationModel("US10007OrderForCustomerAsPartyHostTest").get(0));
		partyBonusCalculationModelList.add(MongoReader.grabPartyBonusCalculationModel("US10007PlacePippaTermPurchaseOrderAsPartyHostTest").get(0));
		partyBonusCalculationModelList.add(MongoReader.grabPartyBonusCalculationModel("US10007PlaceTermPurchaseOrderAsPartyHostTest").get(0));

		urlModel = MongoReader.grabUrlModels("US10007CreatePartyWithCustomerHostTest" + SoapKeys.GRAB).get(0);

		BigDecimal jb = PartyBonusCalculation.calculatePartyJewelryBonus(partyBonusCalculationModelList, false);
		System.out.println(jb);

	}

	@Test
	public void us10007ClosePartyAnfVerifyCommissionBonusesTest() {

		customerRegistrationSteps.performLogin(username, password);
		if (!headerSteps.succesfullLogin()) {
			footerSteps.selectWebsiteFromFooter(MongoReader.getContext());
		}
		headerSteps.selectLanguage(MongoReader.getContext());
		customerRegistrationSteps.navigate(urlModel.getUrl());
//		partyDetailsSteps.closeTheParty("10");
//		partyDetailsSteps.verifyClosedPartyAvailableActions();

	}
}
