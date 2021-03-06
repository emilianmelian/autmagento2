package com.pages.frontend.profile;

import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.tools.constants.ContextConstants;
import com.tools.requirements.AbstractPage;

import net.serenitybdd.core.annotations.findby.By;
import net.serenitybdd.core.annotations.findby.FindBy;

public class ProfileNavPage extends AbstractPage {
	
	@FindBy(css = "a.button.pink-button.confirm-party")
	private WebElement stylePartyDetailsButton;
	
	@FindBy(css = "a#inviteGuests")
	private WebElement sendPartyInvitationButton;

	public void selectMenu(String menu) {
		List<WebElement> menuList = getDriver().findElements(By.cssSelector("div.block-content ul li a"));
		boolean found = false;
		for (WebElement webElement : menuList) {
			if (webElement.getText().contains(menu)) {
				webElement.click();
				found = true;
				break;
			}
		}
		waitFor(ExpectedConditions.invisibilityOfElementWithText(By.cssSelector(".blockUI.blockMsg.blockElement"), ContextConstants.LOADING_MESSAGE));
		Assert.assertTrue("The menu was not found", found);
		waitABit(5000);
		
	}
	
	
	public void clickStylePartyDetailsButton() {
		element(stylePartyDetailsButton).waitUntilVisible();
		stylePartyDetailsButton.click();
	}
	
	public void inviteGuestsToPartyButton() {
		element(sendPartyInvitationButton).waitUntilVisible();
		sendPartyInvitationButton.click();
	}
	
	public void checkSosMenuIsNotPresent(String menu) {
		List<WebElement> menuList = getDriver().findElements(By.cssSelector("div.block-content ul li a"));
		boolean found = false;
		for (WebElement webElement : menuList) {
			if (webElement.getText().contains(menu)) {
				webElement.click();
				found = true;
				break;
			}
			else Assert.assertFalse("The menu was not found", found);
		}
		
	}
	
}
