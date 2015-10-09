package com.workflows.commission;

import net.thucydides.core.annotations.Step;

import com.tools.CustomVerification;
import com.tools.data.backend.IpModel;
import com.tools.data.commission.CommissionStylistModel;
import com.tools.data.frontend.LoungeIpPerformanceModel;
import com.tools.env.constants.Separators;

public class StylecoachPerformanceValidationWorkflow {
	
	public LoungeIpPerformanceModel populateLoungeIpPerformanceModel(IpModel ipModel, CommissionStylistModel commissionStylistModel) {

		LoungeIpPerformanceModel result = new LoungeIpPerformanceModel();

		result.setCareerLevel(commissionStylistModel.getCareerLevel());
		result.setPayLevel(commissionStylistModel.getPaylevel());
		result.setIndividualPoints(ipModel.getIp());
		result.setTeamPoints(String.valueOf(commissionStylistModel.getTeamPoints()));
		result.setGoldStyleCoaches(commissionStylistModel.getGoldFrontliners());
		
		return result;
	}

	@Step
	public void validatePerformanceValues(LoungeIpPerformanceModel expectedModel, LoungeIpPerformanceModel grabbedModel) {
		verifyCareerLevel(expectedModel.getCareerLevel(), grabbedModel.getCareerLevel());
		verifyPayLevel(expectedModel.getPayLevel(), grabbedModel.getPayLevel());
		verifyIndividulPoints(expectedModel.getIndividualPoints(), grabbedModel.getIndividualPoints());
		verifyTeamPoints(expectedModel.getTeamPoints(), grabbedModel.getTeamPoints());
//		verifyStylecoachFirstLevel(expectedModel.getStyleCoachFirstLevel(), grabbedModel.getStyleCoachFirstLevel());
		verifyGoldStyleCoaches(expectedModel.getGoldStyleCoaches(), grabbedModel.getGoldStyleCoaches());
//		verifyMonthYear(expectedModel.getMonthYear(), grabbedModel.getMonthYear());
	}

	private void verifyMonthYear(String expectedValue, String grabbedValue) {
		CustomVerification.verifyTrue("Failure: Month and year doesn't match: " + expectedValue + " Actual: " + grabbedValue, expectedValue.contentEquals(grabbedValue));

	}

	private void verifyGoldStyleCoaches(String expectedValue, String grabbedValue) {
		CustomVerification.verifyTrue("Failure: Gold Style coach value doesn't match: " + expectedValue + " Actual: " + grabbedValue, expectedValue.contentEquals(grabbedValue));

	}

	private void verifyStylecoachFirstLevel(String expectedValue, String grabbedValue) {
		CustomVerification.verifyTrue("Failure: Style coach first value doesn't match: " + expectedValue + " Actual: " + grabbedValue, expectedValue.contentEquals(grabbedValue));

	}

	private void verifyTeamPoints(String expectedValue, String grabbedValue) {
		CustomVerification.verifyTrue("Failure: Team points value doesn't match: " + expectedValue + " Actual: " + grabbedValue, expectedValue.contentEquals(grabbedValue));

	}

	private void verifyIndividulPoints(String expectedValue, String grabbedValue) {
		CustomVerification.verifyTrue("Failure: Individual points value doesn't match: " + expectedValue + " Actual: " + grabbedValue, expectedValue.contentEquals(grabbedValue));

	}

	private void verifyPayLevel(String expectedValue, String grabbedValue) {
		CustomVerification.verifyTrue("Failure: Pay Level doesn't match: " + expectedValue + " Actual: " + grabbedValue, expectedValue.contentEquals(grabbedValue));

	}

	private void verifyCareerLevel(String expectedValue, String grabbedValue) {
		CustomVerification.verifyTrue("Failure: Career level doesn't match: " + expectedValue + " Actual: " + grabbedValue, expectedValue.contentEquals(grabbedValue));
	}

}
