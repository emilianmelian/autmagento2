package com.pages.frontend;

import java.util.List;

import net.serenitybdd.core.annotations.findby.FindBy;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.tools.env.constants.TimeConstants;
import com.tools.requirements.AbstractPage;

public class PomProductListPage extends AbstractPage {

	@FindBy(className = "pom-category-products")
	private WebElement productCatalogContainer;

	public void findProductAndClick(String productName) {

		element(productCatalogContainer).waitUntilVisible();
		List<WebElement> productsList = productCatalogContainer.findElements(By.cssSelector("li.item"));
		boolean found = false;
		theFor: for (WebElement webElement : productsList) {
			String productText = webElement.findElement(By.cssSelector("div h2")).getText();
			if (productText.contains(productName)) {

				String url = webElement.findElement(By.cssSelector("div div a")).getAttribute("href");
				elementjQueryClick("a[href='" + url + "']");
				waitABit(TimeConstants.WAIT_TIME_SMALL);
				found = true;
				break theFor;
			}
		}
		Assert.assertTrue("The searched product was not found !!!", found);
	}

}
