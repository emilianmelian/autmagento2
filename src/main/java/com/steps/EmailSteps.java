package com.steps;

import org.junit.Assert;

import net.thucydides.core.annotations.Step;

import com.tools.requirements.AbstractSteps;

public class EmailSteps extends AbstractSteps {

	private static final long serialVersionUID = 7847714736572013908L;

	@Step
	public void printEmailContent(String email) {
		System.out.println("message: " + email);
	}

	@Step
	public void validateEmailContent(String orderId, String message) {
		Assert.assertTrue("Failure: Message does not contain the total price", message.contains(orderId));
	}
	
	@Step
	public void validateURL(String URL, String context){
		Assert.assertTrue("Failure: URL does not contain the context provided", URL.contains(context));
	}
}
