package com.tests.us7.us7009;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.connectors.mongo.MongoConnector;
import com.steps.external.EmailClientSteps;
import com.steps.frontend.ContactBoosterRegistrationSteps;
import com.steps.frontend.FancyBoxSteps;
import com.steps.frontend.HeaderSteps;
import com.steps.frontend.KoboSuccesFormSteps;
import com.steps.frontend.KoboValidationSteps;
import com.steps.frontend.PomProductDetailsSteps;
import com.steps.frontend.checkout.CheckoutValidationSteps;
import com.steps.frontend.checkout.ConfirmationSteps;
import com.steps.frontend.checkout.PaymentSteps;
import com.steps.frontend.checkout.ShippingSteps;
import com.steps.frontend.checkout.shipping.regularUser.ShippingPomSteps;
import com.steps.frontend.profile.ProfileSteps;
import com.tests.BaseTest;
import com.tools.constants.UrlConstants;
import com.tools.data.frontend.AddressModel;
import com.tools.data.frontend.CreditCardModel;
import com.tools.data.frontend.CustomerFormModel;
import com.tools.data.soap.ProductDetailedModel;
import com.tools.datahandler.RegularUserDataGrabber;
import com.tools.persistance.MongoReader;
import com.tools.persistance.MongoWriter;
import com.tools.requirements.Application;

import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Story;
import net.thucydides.core.annotations.WithTag;

@WithTag(name = "US7.9 Kobo Registration Not On Voucher Owner Context Test ", type = "Scenarios")
@Story(Application.KoboRegistration.US7_9.class)
@RunWith(SerenityRunner.class)
public class US7009KoboRegOnNotVoucherOwnerContextTest1 extends BaseTest {

	@Steps
	public HeaderSteps headerSteps;
	@Steps
	public ContactBoosterRegistrationSteps contactBoosterRegistrationSteps;
	@Steps
	public KoboValidationSteps koboValidationSteps;
	@Steps
	public KoboSuccesFormSteps koboSuccesFormSteps;
	@Steps
	public EmailClientSteps emailClientSteps;
	@Steps
	public PomProductDetailsSteps pomProductDetailsSteps;
	@Steps
	public FancyBoxSteps fancyBoxSteps;
	@Steps
	public ShippingPomSteps shippingPomSteps;
	@Steps
	public ShippingSteps shippingSteps;
	@Steps
	public ProfileSteps profileSteps;
	@Steps
	public PaymentSteps paymentSteps;
	@Steps
	public ConfirmationSteps confirmationSteps;
	@Steps
	public CheckoutValidationSteps checkoutValidationSteps;

	private String context;
	private CustomerFormModel dataModel;
	private AddressModel addressModel;
	private String koboCode;
	private CreditCardModel creditCardData = new CreditCardModel();
	private ProductDetailedModel genProduct1;
	public static List<ProductDetailedModel> createdProductsList = new ArrayList<ProductDetailedModel>();
	
	@Before
	public void setUp() throws Exception {
		RegularUserDataGrabber.wipe();



		Properties prop = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream(UrlConstants.RESOURCES_PATH + "us7" + File.separator + "us7009.properties");
			prop.load(input);
			context = prop.getProperty("context");
			
			input = new FileInputStream(UrlConstants.ENV_PATH + "koboVouchers.properties");
			prop.load(input);
			koboCode = prop.getProperty("koboCodemihaialexandrubarta@gmail.com");

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
		dataModel = new CustomerFormModel();
		addressModel = new AddressModel();
		MongoConnector.cleanCollection(getClass().getSimpleName());
	}

	@Test
	public void us7009KoboRegOnNotVoucherOwnerContextTest1() {
		System.out.println("url "+MongoReader.getBaseURL());
		koboValidationSteps.enterKoboCodeAndGoToRegistrationProcess(MongoReader.getBaseURL() + context, koboCode);
		contactBoosterRegistrationSteps.fillContactBoosterRegistrationForm(dataModel, addressModel);
		koboSuccesFormSteps.verifyKoboFormIsSuccsesfullyFilledIn();
		
		
		

	}

	@After
	public void saveData() {
		MongoWriter.saveCustomerFormModel(dataModel, getClass().getSimpleName());
	//	MongoWriter.saveOrderModel(RegularUserDataGrabber.orderModel, getClass().getSimpleName());
	}

}