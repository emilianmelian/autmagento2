package com.steps.frontend.checkout;

import java.util.List;

import net.thucydides.core.annotations.Step;

import org.junit.Assert;

import com.tools.AbstractSteps;
import com.tools.data.frontend.CartProductModel;

public class CheckoutValidationSteps extends AbstractSteps {

	private static final long serialVersionUID = 4274219181280984116L;

	/**
	 * Validate that the message from the succcess screen, on order process is
	 * displayd.
	 */
	@Step
	public void verifySuccessMessage() {
		successPage().verifySuccessMessage();
	}

	@Step
	public void printTotalsModel(String message, String subtotal, String discount, String totalAmount, String tax, String shipping, String jewelryBonus, String ip) {
		System.out.println(" -- Print Totals - " + message);
		System.out.println("SUBTOTAL: " + subtotal);
		System.out.println("DISCOUNT: " + discount);
		System.out.println("TOTAL AMOUNT: " + totalAmount);
		System.out.println("TAX: " + tax);
		System.out.println("SHIPPING: " + shipping);
		System.out.println("JEWERLY BONUS: " + jewelryBonus);
		System.out.println("IP POINTS: " + ip);
	}

	@Step
	public void printCalculationModel(String message, String subtotal, String totalAmount, String ip) {
		System.out.println("-- Calculation Model - " + message);
		System.out.println("SUBTOTAL: " + subtotal);
		System.out.println("FINAL: " + totalAmount);
		System.out.println("IP POINTS: " + ip);
	}

	@Step
	public void validateMatchPrice(String productNow, String compare) {
		Assert.assertTrue("Failure: Price values dont match: " + productNow + " - " + compare, compare.contentEquals(productNow));
	}

	@Step
	public void matchName(String productNow, String compare) {
		//Name is validated on element match -  Only for print purposes hence match not validate
	}

	@Step
	public void validateMatchQuantity(String productNow, String compare) {
		Assert.assertTrue("Failure: Quantity values dont match: " + productNow + " - " + compare, productNow.contentEquals(compare));
	}

	@Step
	public void checkTotalAmountFromUrl(String url, String totalAmount) {
		Assert.assertTrue("Failure: The total amount from URL is incorrect", url.contains(totalAmount));
		
	}


}
