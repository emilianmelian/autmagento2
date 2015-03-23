package com.steps.frontend.checkout;

import java.util.List;

import net.thucydides.core.annotations.Step;

import com.tools.Constants;
import com.tools.data.frontend.CartProductModel;
import com.tools.data.frontend.HostCartProductModel;
import com.tools.data.frontend.RegularUserCartProductModel;
import com.tools.data.frontend.ShippingModel;
import com.tools.requirements.AbstractSteps;

public class ShippingSteps extends AbstractSteps {

	private static final long serialVersionUID = 8727875042758615102L;

	public ShippingModel grabSurveyData() {
		waitABit(Constants.TIME_CONSTANT);
		return surveyPage().grabSurveyData();
	}

	@Step
	public void clickGoToPaymentMethod() {
		surveyPage().clickGoToPaymentMethod();
		waitABit(Constants.TIME_CONSTANT);
	}

	public List<CartProductModel> grabProductsList() {
		waitABit(Constants.TIME_CONSTANT);
		return surveyPage().grabProductsList();
	}
	public List<RegularUserCartProductModel> grabRegularProductsList() {
		waitABit(Constants.TIME_CONSTANT);
		return surveyPage().grabRegularProductsList();
	}
	public List<HostCartProductModel> grabHostProductsList() {
		waitABit(Constants.TIME_CONSTANT);
		return surveyPage().grabHostProductsList();
	}

	@Step
	public void selectAddress(String address) {
		billingFormPage().selectAdressDropdown(address);
	}

	@Step
	public void setSameAsBilling(boolean checked) {
		shippingFormPage().setSameAsBilling(checked);
	}

	@Step
	public String grabUrl() {
		return getDriver().getCurrentUrl();
	}

	@Step
	public void selectShippingAddress(String value) {
		shippingFormPage().selectShippingAddress(value);
	}

}
