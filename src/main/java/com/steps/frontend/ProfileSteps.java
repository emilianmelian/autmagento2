package com.steps.frontend;

import java.util.List;

import org.junit.Assert;

import net.thucydides.core.annotations.Step;

import com.tools.data.backend.OrderModel;
import com.tools.requirements.AbstractSteps;

public class ProfileSteps extends AbstractSteps{

	private static final long serialVersionUID = -6439771925934414965L;
	
	@Step
	public void openProfileHistory(){
		dashboardMenuPage().clickOnProfileHistory();
	}
	
	/**
	 * Grab Order History List data from Profile OrderHistory
	 * @return
	 */
	public List<OrderModel> grabOrderHistory() {
		return profileHistoryPage().grabOrderHistory();
	}
	
	
	@Step
	public void verifyOrderId(String orderId, String compare){
		Assert.assertTrue("Failure: Order id is not as expected. Expected: " + compare + " Actual: " + orderId, orderId.contentEquals(compare));
	}

	@Step
	public void verifyOrderPrice(String orderPrice, String compare) {
		Assert.assertTrue("Failure: Order Price is not as expected. Expected: " + compare + " Actual: " + orderPrice, orderPrice.contentEquals(compare));
	}

}
