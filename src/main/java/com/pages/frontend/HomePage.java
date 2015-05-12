package com.pages.frontend;

import net.thucydides.core.annotations.findby.FindBy;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.tools.persistance.MongoReader;
import com.tools.requirements.AbstractPage;

public class HomePage extends AbstractPage {

	@FindBy(css = "div.categories li:nth-child(3) a")
	private WebElement styleCoachLink;
	
	@FindBy(css = "a#contactBoosterDetails")
	private WebElement contactBoosterDetails;

	@FindBy(css = "#cssmenu > ul > li:first-child a")
	private WebElement generalView;

	@FindBy(css = "#cssmenu > ul > li:nth-child(2) > a")
	private WebElement jewelriesMenu;

	@FindBy(css = "#cssmenu > ul > li:nth-child(2) > ul li:nth-child(1) > a")
	private WebElement newMenu;

	@FindBy(css = "#cssmenu > ul > li:nth-child(2) > ul li:nth-child(2) > a")
	private WebElement newMenuDe;

	public void clickStyleCoachLink() {
		styleCoachLink.click();

	}
	public void clickOnContactBoosterDetails() {
		element(contactBoosterDetails).waitUntilVisible();
		contactBoosterDetails.click();
		
	}

	public void clickonGeneralView() {
		element(generalView).waitUntilVisible();
		generalView.click();
		waitABit(2000);

	}

	// TODO a switch-case should be added here
	public void goToNewItems() {
		Actions builder = new Actions(getDriver());
		builder.moveToElement(jewelriesMenu).build().perform();
		if (MongoReader.getContext().contentEquals("de")) {
			element(newMenuDe).waitUntilVisible();
			newMenuDe.click();
		} else {
			element(newMenu).waitUntilVisible();
			newMenu.click();
		}
	}

}
