package com.tests.uss32.us32007;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Story;
import net.thucydides.core.annotations.WithTag;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.connectors.http.MagentoProductCalls;
import com.steps.frontend.CustomerRegistrationSteps;
import com.steps.frontend.FooterSteps;
import com.steps.frontend.HeaderSteps;
import com.steps.frontend.HomeSteps;
import com.steps.frontend.ProductSteps;
import com.steps.frontend.SearchSteps;
import com.steps.frontend.checkout.cart.GeneralCartSteps;
import com.steps.frontend.checkout.cart.regularCart.RegularUserCartSteps;
import com.steps.frontend.checkout.wishlist.WishlistSteps;
import com.tests.BaseTest;
import com.tools.CustomVerification;
import com.tools.cartcalculations.regularUser.RegularUserCartCalculator;
import com.tools.constants.ContextConstants;
import com.tools.constants.UrlConstants;
import com.tools.data.soap.ProductDetailedModel;
import com.tools.datahandler.DataGrabber;
import com.tools.datahandler.RegularUserDataGrabber;
import com.tools.persistance.MongoReader;
import com.tools.requirements.Application;
import com.tools.utils.DateUtils;
import com.workflows.frontend.partyHost.AddProductsForCustomerWorkflow;
import com.workflows.frontend.regularUser.AddRegularProductsWorkflow;
import com.workflows.frontend.regularUser.RegularCartValidationWorkflows;

@WithTag(name = "US33.2 Regular CustomerOrder Allowed For TP", type = "Scenarios")
@Story(Application.RegularCart.US8_7.class)
@RunWith(SerenityRunner.class)
public class US32007RegularOrderForNotAllowedCustomerTest extends BaseTest {

	@Steps
	public HeaderSteps headerSteps;
	@Steps
	public GeneralCartSteps generalCartSteps;
	@Steps
	public RegularUserCartSteps regularUserCartSteps;
	@Steps
	public HomeSteps homeSteps;
	@Steps
	public CustomerRegistrationSteps customerRegistrationSteps;
	@Steps
	public AddRegularProductsWorkflow addRegularProductsWorkflow;
	@Steps
	public RegularCartValidationWorkflows regularCartValidationWorkflows;
	@Steps
	public CustomVerification customVerifications;
	@Steps
	public FooterSteps footerSteps;
	@Steps
	public AddProductsForCustomerWorkflow addProductsForCustomerWorkflow;
	@Steps
	public WishlistSteps wishlistSteps;
	@Steps
	public ProductSteps productSteps;
	@Steps
	public SearchSteps searchSteps;

	private String username, password;
	private ProductDetailedModel genProduct1;
	private ProductDetailedModel genProduct2;
	private ProductDetailedModel genProduct3;
	public static List<ProductDetailedModel> allProductsList;
	public static List<ProductDetailedModel> createdProductsList = new ArrayList<ProductDetailedModel>();
	List<List<String>> dropdownDatesList = new ArrayList<List<String>>();

	@Before
	public void setUp() throws Exception {
		RegularUserCartCalculator.wipe();
		RegularUserDataGrabber.wipe();
		DataGrabber.wipe();

		// createdProductsList =
		// MongoReader.grabProductDetailedModel("CreateProductsTest" +
		// SoapKeys.GRAB);
		// genProduct1 = createdProductsList.get(1);
		// genProduct2 = createdProductsList.get(8);
		// genProduct3 = createdProductsList.get(9);

		allProductsList = new ArrayList<ProductDetailedModel>();

		// immediate
		genProduct1 = MagentoProductCalls.createProductModel();
		MagentoProductCalls.createApiProduct(genProduct1);
		genProduct1.getStockData().setEarliestAvailability(DateUtils.getCurrentDate("yyyy-MM-dd"));

		// immediate with TP
		genProduct2 = MagentoProductCalls.createProductModel();
		genProduct2.getStockData().setAllowedTermPurchase("1");
		MagentoProductCalls.createApiProduct(genProduct2);
		genProduct2.getStockData().setEarliestAvailability(DateUtils.getCurrentDate("yyyy-MM-dd"));

		// TP
		genProduct3 = MagentoProductCalls.createNotAvailableYetProductModel();
		MagentoProductCalls.createApiProduct(genProduct3);

		Properties prop = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream(UrlConstants.RESOURCES_PATH + "uss11" + File.separator + "us11001.properties");
			prop.load(input);
			username = prop.getProperty("customerUsername");
			password = prop.getProperty("customerPassword");


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
	public void us32007RegularOrderAllowedOnlyForNotAvailableTPTest() throws ParseException {
		customerRegistrationSteps.performLogin(username, password);
		if (!headerSteps.succesfullLogin()) {
			footerSteps.selectWebsiteFromFooter(MongoReader.getContext());
		}
		headerSteps.selectLanguage(MongoReader.getContext());
		homeSteps.goToNewItems();
		headerSteps.openCartPreview();
		headerSteps.goToCart();
		generalCartSteps.clearCart();
		headerSteps.clickOnWishlistButton();
		wishlistSteps.removeProductsFromWishlist();

		

		addProductsForCustomerWorkflow.addProductToCart(genProduct2, "1", "0");
//		allProductsList.add(genProduct2);
		headerSteps.openCartPreview();
		headerSteps.goToCart();
		regularUserCartSteps.verifyThatTermPurchasePaymentAndShippingBlockIsNotAvailable();
		regularUserCartSteps.verifyThatTermPurchaseIsNotAvailable(genProduct2.getSku());

		addProductsForCustomerWorkflow.addProductToCart(genProduct1, "1", "0");
//		allProductsList.add(genProduct1);
		headerSteps.openCartPreview();
		headerSteps.goToCart();
		regularUserCartSteps.verifyThatTermPurchasePaymentAndShippingBlockIsNotAvailable();
		regularUserCartSteps.verifyThatTermPurchaseIsNotAvailable(genProduct1.getSku());

		searchSteps.navigateToProductPage(genProduct3.getSku());
		productSteps.verifyThatProductStatusIsCorrect(ContextConstants.CURRENTLY_OUT_OF_STOCK);
		productSteps.verifyAddToCartButton(false);
		addRegularProductsWorkflow.setBasicProductToWishlist(genProduct3, "1", "0");
		wishlistSteps.verifyPresenceOfAddAllToCartButton(false);

	}

}
