package com.steps.frontend.checkout;

import java.util.List;

import net.thucydides.core.annotations.Step;

import com.tools.AbstractSteps;
import com.tools.data.frontend.CartProductModel;
import com.tools.data.frontend.CartTotalsModel;

public class ShippingSteps extends AbstractSteps{

	private static final long serialVersionUID = 8727875042758615102L;

	
	public CartTotalsModel grabSurveyData(){
		return surveyPage().grabSurveyData();
	}
	
	@Step
	public void clickGoToPaymentMethod(){
		surveyPage().clickGoToPaymentMethod();
		waitABit(9000);
	}

	public List<CartProductModel> grabProductsList() {
		return surveyPage().grabProductsList();
	}
	
	@Step
	public void selectAddress(String address){
		billingFormPage().selectAdressDropdown(address);
	}
	
	@Step
	public void setSameAsBilling(boolean checked){
		shippingFormPage().setSameAsBilling(checked);
	}

	@Step
	public String grabUrl() {
		return getDriver().getCurrentUrl();		
	}
	
}
