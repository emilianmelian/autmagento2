package com.tests.uss32.us32004;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Locale;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.connectors.http.MagentoProductCalls;
import com.steps.frontend.CustomerRegistrationSteps;
import com.steps.frontend.FooterSteps;
import com.steps.frontend.HeaderSteps;
import com.steps.frontend.LoungeSteps;
import com.steps.frontend.ProductSteps;
import com.steps.frontend.checkout.cart.GeneralCartSteps;
import com.steps.frontend.checkout.cart.partyHost.AddProductsModalSteps;
import com.steps.frontend.checkout.cart.partyHost.OrderForCustomerCartSteps;
import com.steps.frontend.checkout.wishlist.WishlistSteps;
import com.steps.frontend.registration.party.CreateNewContactSteps;
import com.tests.BaseTest;
import com.tools.constants.UrlConstants;
import com.tools.data.frontend.AddressModel;
import com.tools.data.frontend.CustomerFormModel;
import com.tools.data.soap.ProductDetailedModel;
import com.tools.persistance.MongoReader;
import com.tools.requirements.Application;
import com.tools.utils.DateUtils;
import com.workflows.frontend.partyHost.AddProductsForCustomerWorkflow;

import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Story;
import net.thucydides.core.annotations.WithTag;

@WithTag(name = "US32.1 Check restriction for product available with TP", type = "Scenarios")
@Story(Application.CheckTpProductsRestrictions.US32_1.class)
@RunWith(SerenityRunner.class)
public class US32004CheckPlaceCustomerOrderTpRestrictionsTest extends BaseTest {

	@Steps
	public HeaderSteps headerSteps;
	@Steps
	public FooterSteps footerSteps;
	@Steps
	public LoungeSteps loungeSteps;
	@Steps
	public ProductSteps productSteps;
	@Steps
	public WishlistSteps wishlistSteps;
	@Steps
	public CreateNewContactSteps createNewContactSteps;
	@Steps
	public AddProductsModalSteps addProductsModalSteps;
	@Steps
	public OrderForCustomerCartSteps orderForCustomerCartSteps;
	@Steps
	public CustomerRegistrationSteps customerRegistrationSteps;
	@Steps
	public GeneralCartSteps generalCartSteps;
	@Steps
	public AddProductsForCustomerWorkflow addProductsForCustomerWorkflow;

	private String username, password;
	private CustomerFormModel customerData;
	private AddressModel addressData;
	private ProductDetailedModel genProduct1;

	String formatedAvailabilityDate;

	@Before
	public void setUp() throws Exception {

		customerData = new CustomerFormModel();
		addressData = new AddressModel();

		genProduct1 = MagentoProductCalls.createNotAvailableYetProductModel();
		genProduct1.getStockData().setIsDiscontinued("0");
		MagentoProductCalls.createApiProduct(genProduct1);

		String unformatedAvailabilityDate = DateUtils
				.getFirstFridayAfterDate(genProduct1.getStockData().getEarliestAvailability(), "yyyy-MM-dd");

		formatedAvailabilityDate = DateUtils.parseDate(unformatedAvailabilityDate, "yyyy-MM-dd", "dd. MMM. yyyy",
				new Locale.Builder().setLanguage(MongoReader.getContext()).build());

		Properties prop = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream(UrlConstants.RESOURCES_PATH + "uss15" + File.separator + "us15004.properties");
			prop.load(input);
			username = prop.getProperty("username");
			password = prop.getProperty("password");

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Test
	public void us32004CheckPlaceCustomerOrderTpRestrictionsTest() throws ParseException {
		customerRegistrationSteps.performLogin(username, password);
		if (!headerSteps.succesfullLogin()) {
			footerSteps.selectWebsiteFromFooter(MongoReader.getContext());
		}
		headerSteps.selectLanguage(MongoReader.getContext());
		headerSteps.goToProfile();

		loungeSteps.orderForNewCustomer();
		createNewContactSteps.fillCreateNewContactDirectly(customerData, addressData);
		generalCartSteps.clearCart();
		
		orderForCustomerCartSteps.openSearchProductsModal();

		addProductsModalSteps.searchForProduct(genProduct1.getSku());
		addProductsModalSteps.verifyProductPropertiesInModalWindow(genProduct1.getSku(), genProduct1.getName(),
				formatedAvailabilityDate);
		addProductsModalSteps.closeModal();

		addProductsForCustomerWorkflow.setHostProductToCart(genProduct1, "1", "0");

		productSteps.verifyThatAvailabilityDateIsCorrect(formatedAvailabilityDate);

		addProductsForCustomerWorkflow.addProductToWishlist(genProduct1, "1", "0");
		
		wishlistSteps.verifyPresenceOfAddAllToCartButton(true);
		wishlistSteps.addProductToCart(genProduct1.getName());

	}

}