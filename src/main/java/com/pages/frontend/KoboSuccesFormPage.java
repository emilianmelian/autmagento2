package com.pages.frontend;

import net.thucydides.core.annotations.findby.FindBy;

import org.junit.Assert;
import org.openqa.selenium.WebElement;

import com.tools.env.variables.ContextConstants;
import com.tools.persistance.MongoReader;
import com.tools.requirements.AbstractPage;

public class KoboSuccesFormPage extends AbstractPage {

	@FindBy(css = "div.col-main")
	private WebElement textContainer;

	public void verifyKoboFormIsSuccsesfullyFilledIn() {
		element(textContainer).waitUntilVisible();
		Assert.assertTrue("Failure: Email notification text was not found. ", textContainer.getText().contains(ContextConstants.SUCCES_KOBO_FORM));
	}

	public void verifyThatTheWebsiteChanged() {
		System.out.println(getDriver().getCurrentUrl());
		System.out.println(MongoReader.getSoapURL() + ContextConstants.NOT_PREFERED_WEBSITE);
		Assert.assertTrue("The url does not reflect the website change !!", getDriver().getCurrentUrl().contains(MongoReader.getSoapURL() + ContextConstants.NOT_PREFERED_WEBSITE));
	}
}
