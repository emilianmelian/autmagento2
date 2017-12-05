package com.tests.uss41.us41001_Borrow;

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
import com.steps.backend.BackEndSteps;
import com.steps.backend.ImportExport.ImportExportSteps;
import com.steps.backend.borrow.BorrowSystemConfigurationSteps;
import com.steps.frontend.CustomerRegistrationSteps;
import com.steps.frontend.FooterSteps;
import com.steps.frontend.HeaderSteps;
import com.steps.frontend.HomeSteps;
import com.steps.frontend.LoungeSteps;
import com.steps.frontend.MyBusinessSteps;
import com.steps.frontend.ShopSteps;
import com.steps.frontend.checkout.ConfirmationSteps;
import com.steps.frontend.checkout.PaymentSteps;
import com.steps.frontend.checkout.ShippingSteps;
import com.steps.frontend.checkout.cart.GeneralCartSteps;
import com.steps.frontend.checkout.cart.borrowCart.BorrowCartSteps;
import com.tests.BaseTest;
import com.tools.CustomVerification;
import com.tools.cartcalculations.borrowCart.BorrowCartCalculator;
import com.tools.constants.Credentials;
import com.tools.constants.FilePaths;
import com.tools.constants.SoapKeys;
import com.tools.constants.UrlConstants;
import com.tools.data.frontend.BorrowProductModel;
import com.tools.data.frontend.CreditCardModel;
import com.tools.data.soap.ProductDetailedModel;
import com.tools.datahandler.BorrowDataGrabber;
import com.tools.datahandler.DataGrabber;
import com.tools.persistance.MongoReader;
import com.tools.persistance.MongoWriter;
import com.tools.requirements.Application;
import com.tools.utils.FormatterUtils;
import com.workflows.frontend.AddProductsWorkflow;
import com.workflows.frontend.borrowCart.AddBorrowedProductsWorkflow;
import com.workflows.frontend.borrowCart.BorrowCartValidationWorkflows;

import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Story;
import net.thucydides.core.annotations.WithTag;

@WithTag(name = "US16.1 SC borrow products Test", type = "Scenarios")
@Story(Application.BorrowCart.US16_1.class)
@RunWith(SerenityRunner.class)
public class US41001StyleCoachBorrowsProductsTest extends BaseTest {

	@Steps
	public CustomerRegistrationSteps customerRegistrationSteps;
	@Steps
	public HeaderSteps headerSteps;
	@Steps
	public BorrowCartSteps borrowCartSteps;
	@Steps
	public HomeSteps homeSteps;
	@Steps
	public LoungeSteps loungeSteps;
	@Steps
	public FooterSteps footerSteps;
	@Steps
	public ShippingSteps shippingSteps;
	@Steps
	public GeneralCartSteps generalCartSteps;
	@Steps
	public ConfirmationSteps confirmationSteps;
	@Steps
	public AddProductsWorkflow addProductsWorkflow;
	@Steps
	public AddBorrowedProductsWorkflow addBorrowedProductsWorkflow;
	@Steps
	public PaymentSteps paymentSteps;
	@Steps
	public BorrowCartValidationWorkflows borrowCartValidationWorkflows;
	@Steps
	public CustomVerification customVerifications;
	@Steps
	public BackEndSteps backEndSteps;
	@Steps
	public BorrowSystemConfigurationSteps borrowSystemConfigurationSteps;
	@Steps
	public MyBusinessSteps myBusinessSteps;
	@Steps
	public ShopSteps shopSteps;
	
	@Steps
	public ImportExportSteps importExportSteps;

	private String username, password;
	private static String billingAddress;
	private static String shippingValue;
	private static String taxClass;
	private CreditCardModel creditCardData = new CreditCardModel();

	private ProductDetailedModel genProduct1;
	private ProductDetailedModel genProduct2;
	public static List<ProductDetailedModel> createdProductsList = new ArrayList<ProductDetailedModel>();



	@Before
	public void setUp() throws Exception {
		BorrowCartCalculator.wipe();
		BorrowDataGrabber.wipe();
		DataGrabber.wipe();
		
		genProduct1 =new ProductDetailedModel();
		genProduct1.setPrice("49.90");
		genProduct1.setSku("soft-pearl-necklace");
		
		genProduct2=new ProductDetailedModel();
		genProduct2.setPrice("89.00");
		genProduct2.setSku("cross-berry-bracelet");
		
//		createdProductsList = MongoReader.grabProductDetailedModel("CreateProductsTest" + SoapKeys.GRAB);
//			
//		genProduct1 = createdProductsList.get(0);
//		genProduct2 = createdProductsList.get(1);
		
		/*if(!createdProductsList.isEmpty() && createdProductsList.size()>=2){
			genProduct1 = createdProductsList.get(0);
			genProduct2 = createdProductsList.get(1);
		}else{
			genProduct1 = MagentoProductCalls.createProductModel();
			 genProduct1.setIp("84");
			 genProduct1.setPrice("49.90");
			 MagentoProductCalls.createApiProduct(genProduct1);
			
			 genProduct2 = MagentoProductCalls.createProductModel();
			 genProduct2.setIp("25");
			 genProduct2.setPrice("89.00");
			 MagentoProductCalls.createApiProduct(genProduct2);
			
		}*/

		Properties prop = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream(UrlConstants.RESOURCES_PATH + FilePaths.US_41_FOLDER + File.separator + "us41001borrow.properties");
			prop.load(input);
			username = prop.getProperty("username");
			password = prop.getProperty("password");
			billingAddress = prop.getProperty("billingAddress");
			shippingValue = prop.getProperty("shippingPrice");
			taxClass = prop.getProperty("taxClass");

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

		MongoConnector.cleanCollection(getClass().getSimpleName() + SoapKeys.GRAB);
		MongoConnector.cleanCollection(getClass().getSimpleName() + SoapKeys.CALC);
	}

	@Test
	public void us41001StyleCoachBorrowsProductsTest() {
		
		backEndSteps.performAdminLogin(Credentials.BE_USER, Credentials.BE_PASS);
		backEndSteps.clickOnSystemConfiguration();
		borrowSystemConfigurationSteps.goToBorrowTab();
		borrowSystemConfigurationSteps.selectDisabledBorrowOption("Nein");
		borrowSystemConfigurationSteps.selectBorrowProcessType("(Old process) Allow all products to be borrowed");
		borrowSystemConfigurationSteps.selectCountries();
		borrowSystemConfigurationSteps.saveConfiguration();
		
		
		customerRegistrationSteps.performLogin(username, password);
		if (!headerSteps.succesfullLogin()) {
			footerSteps.selectWebsiteFromFooter(MongoReader.getContext());
		}
		headerSteps.selectLanguage(MongoReader.getContext());
		loungeSteps.clickGoToBorrowCart();
		
		generalCartSteps.clearBorrowCart();
		BorrowProductModel productData;

		productData = addBorrowedProductsWorkflow.setBorrowedDefaultProductToCart();
		BorrowCartCalculator.allBorrowedProductsList.add(productData);
		productData = addBorrowedProductsWorkflow.setBorrowedProductToCart(genProduct1, "0.00");
		BorrowCartCalculator.allBorrowedProductsList.add(productData);
		productData = addBorrowedProductsWorkflow.setBorrowedProductToCart(genProduct2, "0.00");
		BorrowCartCalculator.allBorrowedProductsList.add(productData);

		BorrowCartCalculator.calculateCartAndShippingTotals(taxClass, shippingValue);

		headerSteps.openCartPreview();
		headerSteps.goToCart();

		BorrowDataGrabber.grabbedBorrowCartProductsList = borrowCartSteps.grabProductsData();

		borrowCartSteps.grabTotals();
		borrowCartSteps.clickGoToShipping();

		shippingSteps.selectAddress(billingAddress);
		shippingSteps.setSameAsBilling(true);
		shippingSteps.checkTermsCheckbox();

		shippingSteps.grabBorrowedProductsList();
		shippingSteps.grabSurveyData();
		shippingSteps.goToPaymentMethod();

		String url = shippingSteps.grabUrl();
		DataGrabber.urlModel.setName("Payment URL");
		DataGrabber.urlModel.setUrl(url);
		DataGrabber.orderModel.setTotalPrice(FormatterUtils.extractPriceFromURL(url));
		DataGrabber.orderModel.setOrderId(FormatterUtils.extractOrderIDFromURL(url));

		paymentSteps.expandCreditCardForm();
		paymentSteps.fillCreditCardForm(creditCardData);

		confirmationSteps.grabProductsList();
		confirmationSteps.grabConfirmationTotals();
		confirmationSteps.grabBillingData();
		confirmationSteps.grabSippingData();

		confirmationSteps.agreeAndCheckout();

		borrowCartValidationWorkflows.setBillingShippingAddress(billingAddress, billingAddress);
	//	borrowCartValidationWorkflows.performBorrowCartValidations();
		importExportSteps.createOrderFile(FormatterUtils.extractOrderIDFromURL(url));
		customVerifications.printErrors();
	
	}

	@After
	public void saveData() {

		MongoWriter.saveBorrowCartCalcDetailsModel(BorrowCartCalculator.borrowCartCalcDetailsModel, getClass().getSimpleName() + SoapKeys.CALC);
		MongoWriter.saveShippingModel(BorrowCartCalculator.shippingCalculatedModel, getClass().getSimpleName() + SoapKeys.CALC);
		MongoWriter.saveShippingModel(BorrowDataGrabber.borrowCartConfirmationTotals, getClass().getSimpleName() + SoapKeys.GRAB);
		MongoWriter.saveOrderModel(DataGrabber.orderModel, getClass().getSimpleName() + SoapKeys.GRAB);
		MongoWriter.saveUrlModel(DataGrabber.urlModel, getClass().getSimpleName() + SoapKeys.GRAB);
		for (BorrowProductModel product : BorrowCartCalculator.allBorrowedProductsList) {
			MongoWriter.saveBorrowProductModel(product, getClass().getSimpleName() + SoapKeys.GRAB);
		}
	}
}