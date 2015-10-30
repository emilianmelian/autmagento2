package com.tests.uss21.uss21001;

import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Story;
import net.thucydides.core.annotations.WithTag;
import net.thucydides.junit.runners.ThucydidesRunner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.connectors.mongo.MongoConnector;
import com.steps.backend.BackEndSteps;
import com.steps.external.commission.CommissionReportSteps;
import com.tests.BaseTest;
import com.tools.data.backend.RewardPointsOfStylistModel;
import com.tools.env.variables.UrlConstants;
import com.tools.persistance.MongoWriter;
import com.tools.requirements.Application;

@WithTag(name = "US21.1 Verify Closed Month Frontend and Backend Performance", type = "Scenarios")
@Story(Application.CloseMonthRewardPoints.US21_1.class)
@RunWith(ThucydidesRunner.class)
public class US21001CloseMonthTest extends BaseTest {

	@Steps
	public BackEndSteps backEndSteps;
	@Steps
	public CommissionReportSteps commissionReportSteps;

	RewardPointsOfStylistModel calculatedRewordPointsOfStylistModel = new RewardPointsOfStylistModel();

	@Before
	public void setUp() {
		MongoConnector.cleanCollection(getClass().getSimpleName());
	}

	@Test
	public void us21001CloseMonthTest() throws Exception {

		backEndSteps.navigate(UrlConstants.COMMISSION_REPORTS_URL);
		//get from previous test activation date
		calculatedRewordPointsOfStylistModel = commissionReportSteps.closeMonthAndCalculateRewardPoints("1835","2015-09-20 12:06:49");

	}

	@After
	public void save() {
		MongoWriter.saveRewardPointsModel(calculatedRewordPointsOfStylistModel, getClass().getSimpleName());
	}
}
