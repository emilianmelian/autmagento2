package com.steps.frontend;

import net.thucydides.core.annotations.Step;

import com.tools.requirements.AbstractSteps;

public class LoungeSteps extends AbstractSteps {

	private static final long serialVersionUID = 1L;

	@Step
	public void goToMyBusiness() {
		loungePage().goToMyBusiness();
	}

	@Step
	public void clickCreateParty() {
		loungePage().clickCreateParty();
	}

	@Step
	public void clickOrderForCustomer() {
		loungePage().clickOrderForCustomer();
	}
	@Step
	public void clickGoToBorrowCart() {
		loungePage().clickGoToBorrowCart();
	}

	@Step
	public void selectCustomerToOrderFor(String name) {
		loungePage().typeContactName(name);
		loungePage().startOrderForCustomer();
	}
	
	@Step
	public void orderForNewCustomer(){
		loungePage().clickOrderForCustomer();
		loungePage().clickAddContact();
	}

}
