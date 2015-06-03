package com.pages.frontend.registration.contactBooster;

import net.thucydides.core.annotations.findby.FindBy;

import org.openqa.selenium.WebElement;

import com.tools.requirements.AbstractPage;

public class KoboCampaignPage extends AbstractPage {

	@FindBy(css = "a.button.variable-color")
	private WebElement registerButton;

	@FindBy(id = "firstname")
	private WebElement firstNameInput;

	@FindBy(id = "nachname")
	private WebElement lastNameInput;

	@FindBy(id = "email")
	private WebElement emailInput;

	@FindBy(css = "input#password")
	private WebElement passwordInput;

	@FindBy(css = "input#confirmation")
	private WebElement passwordConfirmInput;
	// no method
	@FindBy(id = "is_subscribed")
	private WebElement isSubscribedCheckbox;
	// no method
	@FindBy(id = "flag_stylist_parties")
	private WebElement stylePartiesCheckbox;
	// no method
	@FindBy(id = "flag_stylist_member")
	private WebElement styleMemberCheckbox;

	@FindBy(id = "terms")
	private WebElement termsCheckbox;

	@FindBy(css = "button#kostenlos-anmelden")
	private WebElement submitButton;

	@FindBy(css = "#selectContainer input#by_default")
	private WebElement noInviteCheckbox;

	// ---------------------------------------------------
	@FindBy(id = "street_1")
	private WebElement streetInput;

	@FindBy(id = "house_number")
	private WebElement streetNumberInput;

	@FindBy(id = "zip")
	private WebElement postCodeInput;

	@FindBy(id = "city")
	private WebElement cityInput;

	@FindBy(id = "registration-distribution-country")
	private WebElement countrySelect;

	@FindBy(id = "telephone")
	private WebElement telephoneInput;

	public void clickRegister() {
		element(registerButton).waitUntilVisible();
		registerButton.click();
	}

	public void checkIsSubscribedCheckbox() {
		element(isSubscribedCheckbox).waitUntilVisible();
		isSubscribedCheckbox.click();
	}

	public void checkStylePartiesCheckbox() {
		element(stylePartiesCheckbox).waitUntilVisible();
		stylePartiesCheckbox.click();
	}

	public void checkStyleMemberCheckbox() {
		element(styleMemberCheckbox).waitUntilVisible();
		styleMemberCheckbox.click();
	}

	public void firstNameInput(String firstName) {
		element(firstNameInput).waitUntilVisible();
		firstNameInput.sendKeys(firstName);
	}

	public void lastNameInput(String lastName) {
		element(lastNameInput).waitUntilVisible();
		lastNameInput.sendKeys(lastName);
	}

	public void emailInput(String email) {
		element(emailInput).waitUntilVisible();
		element(emailInput).sendKeys(email);
	}

	public void passwordInput(String password) {
		element(passwordInput).waitUntilVisible();
		element(passwordInput).sendKeys(password);
	}

	public void passwordConfirmInput(String password) {
		element(passwordConfirmInput).waitUntilVisible();
		element(passwordConfirmInput).sendKeys(password);
	}

	public void checkIAgree() {
		element(termsCheckbox).waitUntilVisible();
		termsCheckbox.click();
		waitABit(1000);
	}

	public void submitAndContinue() {
		element(submitButton).waitUntilVisible();
		submitButton.click();
	}

	public void inputStreetAddress(String streetAddress) {
		element(streetInput).waitUntilVisible();
		streetInput.sendKeys(streetAddress);
	}

	public void inputStreetNumber(String streetNumber) {
		streetNumberInput.sendKeys(streetNumber);
	}

	public void inputPostCode(String postCode) {
		postCodeInput.sendKeys(postCode);
	}

	public void inputHomeTown(String homeTown) {
		cityInput.sendKeys(homeTown);
	}

	public void selectCountryName(String countryName) {
		element(countrySelect).waitUntilVisible();
		element(countrySelect).selectByVisibleText(countryName);
	}

	public void inputPhoneNumber(String phoneNumber) {
		telephoneInput.sendKeys(phoneNumber);
	}

	public void checkNoInvite() {
		element(noInviteCheckbox).waitUntilVisible();
		noInviteCheckbox.click();
		noInviteCheckbox.click();
		waitABit(2000);
	}
}
