package com.tests.uss12.uss12001;

import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Story;
import net.thucydides.core.annotations.WithTag;
import net.thucydides.junit.runners.ThucydidesRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.connectors.http.ApiCalls;
import com.steps.frontend.CustomerRegistrationSteps;
import com.steps.frontend.DashboardSteps;
import com.steps.frontend.FooterSteps;
import com.steps.frontend.HeaderSteps;
import com.steps.frontend.HomeSteps;
import com.steps.frontend.checkout.CheckoutValidationSteps;
import com.steps.frontend.checkout.ConfirmationSteps;
import com.steps.frontend.checkout.PaymentSteps;
import com.steps.frontend.checkout.ShippingSteps;
import com.steps.frontend.checkout.cart.regularCart.RegularUserCartSteps;
import com.steps.frontend.checkout.shipping.regularUser.ShippingPartySectionSteps;
import com.tests.BaseTest;
import com.tools.CustomVerification;
import com.tools.SoapKeys;
import com.tools.data.RegularCartCalcDetailsModel;
import com.tools.data.frontend.CreditCardModel;
import com.tools.data.frontend.CustomerFormModel;
import com.tools.data.frontend.RegularBasicProductModel;
import com.tools.data.soap.ProductDetailedModel;
import com.tools.datahandlers.regularUser.RegularUserCartCalculator;
import com.tools.datahandlers.regularUser.RegularUserDataGrabber;
import com.tools.persistance.MongoReader;
import com.tools.requirements.Application;
import com.workflows.frontend.regularUser.AddRegularProductsWorkflow;
import com.workflows.frontend.regularUser.RegularCartValidationWorkflows;

@WithTag(name = "US12", type = "frontend")
@Story(Application.Shop.RegularCart.class)
@RunWith(ThucydidesRunner.class)
public class US12001CustomerBuyWithContactBoosterTest extends BaseTest {

	@Steps
	public HeaderSteps headerSteps;
	@Steps
	public ShippingSteps shippingSteps;
	@Steps
	public PaymentSteps paymentSteps;
	@Steps
	public ConfirmationSteps confirmationSteps;
	@Steps
	public ShippingPartySectionSteps shippingPartySectionSteps;
	@Steps
	public RegularUserCartSteps regularUserCartSteps;
	@Steps
	public HomeSteps homeSteps;
	@Steps
	public CustomerRegistrationSteps customerRegistrationSteps;
	@Steps
	public AddRegularProductsWorkflow addRegularProductsWorkflow;
	@Steps
	public CheckoutValidationSteps checkoutValidationSteps;
	@Steps
	public RegularCartValidationWorkflows regularCartValidationWorkflows;
	@Steps
	public CustomVerification customVerifications;
	@Steps
	public FooterSteps footerSteps;
	@Steps
	public DashboardSteps dashboardSteps;

	CustomerFormModel customerModel;
	private String voucherCode;

	private CreditCardModel creditCardData = new CreditCardModel();
	public RegularCartCalcDetailsModel total = new RegularCartCalcDetailsModel();

	private ProductDetailedModel genProduct1;

	@Before
	public void setUp() throws Exception {
		RegularUserCartCalculator.wipe();
		RegularUserDataGrabber.wipe();

		genProduct1 = ApiCalls.createPomProductModel();
		genProduct1.setPrice("89.00");
		ApiCalls.createApiProduct(genProduct1);

		customerModel = MongoReader.grabCustomerFormModels("US12001RegularCustomerRegistrationTest").get(0);

		voucherCode = MongoReader.grabKoboModel("US12001KoboSubscriptionUpgradeTest" + SoapKeys.GRAB);
		System.out.println(voucherCode);

	}

	@Test
	public void us12001CustomerBuyWithContactBoosterTest() {
		customerRegistrationSteps.performLogin(customerModel.getEmailName(), customerModel.getPassword());
		if (!headerSteps.succesfullLogin()) {
			footerSteps.selectWebsiteFromFooter(MongoReader.getContext());
		}
		headerSteps.selectLanguage(MongoReader.getContext());
		homeSteps.goToNewItems();
		customerRegistrationSteps.wipeRegularCart();
		RegularBasicProductModel productData;

		productData = addRegularProductsWorkflow.setBasicProductToCart(genProduct1, "1", "0");
		RegularUserCartCalculator.allProductsList.add(productData);

		headerSteps.openCartPreview();
		headerSteps.goToCart();

		regularUserCartSteps.typeCouponCode(voucherCode);
		regularUserCartSteps.submitVoucherCode();

		regularUserCartSteps.clickGoToShipping();
		shippingPartySectionSteps.clickPartyNoOption();

		shippingSteps.clickGoToPaymentMethod();

		paymentSteps.expandCreditCardForm();
		paymentSteps.fillCreditCardForm(creditCardData);

		confirmationSteps.grabRegularProductsList();

		confirmationSteps.agreeAndCheckout();
		checkoutValidationSteps.verifySuccessMessage();

	}

}
