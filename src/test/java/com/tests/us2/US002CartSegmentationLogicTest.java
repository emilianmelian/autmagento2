package com.tests.us2;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Story;
import net.thucydides.core.annotations.WithTag;
import net.thucydides.junit.runners.ThucydidesRunner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.connectors.mongo.MongoConnector;
import com.steps.frontend.CustomerRegistrationSteps;
import com.steps.frontend.HeaderSteps;
import com.steps.frontend.ProductSteps;
import com.steps.frontend.ProfileSteps;
import com.steps.frontend.SearchSteps;
import com.steps.frontend.checkout.CartSteps;
import com.steps.frontend.checkout.CheckoutValidationSteps;
import com.steps.frontend.checkout.ConfirmationSteps;
import com.steps.frontend.checkout.PaymentSteps;
import com.steps.frontend.checkout.ShippingSteps;
import com.tests.BaseTest;
import com.tools.Constants;
import com.tools.calculation.CartCalculation;
import com.tools.data.CalculationModel;
import com.tools.data.backend.OrderModel;
import com.tools.data.frontend.CartProductModel;
import com.tools.data.frontend.CartTotalsModel;
import com.tools.data.frontend.CreditCardModel;
import com.tools.data.frontend.ProductBasicModel;
import com.tools.data.frontend.ShippingModel;
import com.tools.persistance.MongoWriter;
import com.tools.requirements.Application;
import com.tools.utils.PrintUtils;
import com.workflows.frontend.CartWorkflows;

@WithTag(name = "US002", type = "frontend")
@Story(Application.StyleCoach.Shopping.class)
@RunWith(ThucydidesRunner.class)
public class US002CartSegmentationLogicTest extends BaseTest {

	@Steps
	public CustomerRegistrationSteps frontEndSteps;
	@Steps
	public ProductSteps productSteps;
	@Steps
	public SearchSteps searchSteps;
	@Steps
	public HeaderSteps headerSteps;
	@Steps
	public CartSteps cartSteps;
	@Steps
	public CartCalculation calculusSteps;
	@Steps
	public CheckoutValidationSteps validationSteps;
	@Steps
	public ShippingSteps shippingSteps;
	@Steps
	public ConfirmationSteps confirmationSteps;
	@Steps
	public PaymentSteps paymentSteps;
	@Steps
	public ProfileSteps profileSteps;
	@Steps
	public CartWorkflows cartWorkflows;

	private static OrderModel orderNumber = new OrderModel();
	private static List<ProductBasicModel> allProductsList = new ArrayList<ProductBasicModel>();
	private static List<ProductBasicModel> productsList = new ArrayList<ProductBasicModel>();
	private static List<ProductBasicModel> productsList25 = new ArrayList<ProductBasicModel>();
	private static List<ProductBasicModel> productsList50 = new ArrayList<ProductBasicModel>();
	private static List<ProductBasicModel> productsListMarketing = new ArrayList<ProductBasicModel>();
	private List<CalculationModel> totalsList = new ArrayList<CalculationModel>();
	private CreditCardModel creditCardData = new CreditCardModel();
	private String username, password;
	private static CartTotalsModel cartTotals = new CartTotalsModel();

	@Before
	public void setUp() throws Exception {
		Properties prop = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream(Constants.RESOURCES_PATH + "us2\\us002.properties");
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

		creditCardData.setCardNumber("4111 1111 1111 1111");
		creditCardData.setCardName("test");
		creditCardData.setMonthExpiration("06");
		creditCardData.setYearExpiration("2016");
		creditCardData.setCvcNumber("737");

		MongoConnector.cleanCollection(getClass().getSimpleName());
	}

	@Test
	public void uS002CartSegmentationLogicTest() {
		frontEndSteps.performLogin(username, password);
		ProductBasicModel productData;

		//commented products are for demo
//		searchSteps.searchAndSelectProduct("N100SV", "MAXI CHAIN (SILVER)");
//		productData = productSteps.setProductAddToCart("1", "medium");			
		searchSteps.searchAndSelectProduct("K010SV", "CLARA SET");
		productData = productSteps.setProductAddToCart("1", "0");			
		//we add this into both sections because the quantity will be increased at 2, so 1 piece will be added into 25 section 		
		ProductBasicModel newProduct = newProductObject(productData.getName(), productData.getPrice(), productData.getType(), "2");		
		productsList50.add(productData);
		productsList25.add(productData);		
		productsList.add(newProduct);
		
//		searchSteps.searchAndSelectProduct("B002BE", "FLORENCE BRACELET (BEIGE)");
//		productData = productSteps.setProductAddToCart("2", "medium");	
		searchSteps.searchAndSelectProduct("MAGIC VIOLETTA", "MAGIC VIOLETTA");
		productData = productSteps.setProductAddToCart("2", "0");
		ProductBasicModel newProduct2 = newProductObject(productData.getName(), productData.getPrice(), productData.getType(), "1");
		productsList.add(newProduct2);
		productsList50.add(newProduct2);
	
		//we remove the item product from 50 section because after updating the quantity will remain just 1 item.evend if we set 0 quantity
		//for the product from 50 section, the other piece will not remain in the 25 section ,it will come into the 50 section
		//productsList50.add(productData);
		
//		searchSteps.searchAndSelectProduct("E106SV","JOANNA EARRINGS");
//		productData = productSteps.setProductAddToCart("1", "0");
		searchSteps.searchAndSelectProduct("Rosemary Ring", "ROSEMARY RING");
		productData = productSteps.setProductAddToCart("1", "18");
		productsList.add(productData);
		productsList50.add(productData);
		
		searchSteps.searchAndSelectProduct("M064", "SCHMUCKBROSCHÜRE (40 STK.)");
		productData = productSteps.setProductAddToCart("2", "0");
		productsList.add(productData);
		productsListMarketing.add(productData);				

		searchSteps.searchAndSelectProduct("M101", "STYLE BOOK HERBST / WINTER 2014 (270 STK)");
		productData = productSteps.setProductAddToCart("1", "0");
		productsList.add(productData);
		productsListMarketing.add(productData);
		
		allProductsList.addAll(productsList25);
		allProductsList.addAll(productsList50);
		allProductsList.addAll(productsListMarketing);
		
		

		headerSteps.openCartPreview();
		headerSteps.goToCart();
		//TODO change the update method to set the quantity in the model
		//TODO handle witch product needs to be updated if we have the same product into different discount tables
//		cartSteps.updateProductQuantityIn50DiscountArea("2","N100SV");	
//		cartSteps.updateProductQuantityIn50DiscountArea("0","B002BE");
		cartSteps.updateProductQuantityIn50DiscountArea("2","K010SV");	
		cartSteps.updateProductQuantityIn50DiscountArea("0","K017DK");

		cartSteps.updateCart();

		List<CartProductModel> cartProducts = cartSteps.grabProductsData();

		List<CartProductModel> cartProductsWith50Discount = cartSteps.grabProductsDataWith50PercentDiscount();

		List<CartProductModel> cartProductsWith25Discount = cartSteps.grabProductsDataWith25PercentDiscount();

		List<CartProductModel> cartMarketingMaterialsProducts = cartSteps.grabMarketingMaterialProductsData();

		totalsList.add(CartCalculation.calculateTableProducts(cartProductsWith25Discount));
		totalsList.add(CartCalculation.calculateTableProducts(cartProductsWith50Discount));
		totalsList.add(CartCalculation.calculateTableProducts(cartMarketingMaterialsProducts));
		CalculationModel totalsCalculated = CartCalculation.calculateTotalSum(totalsList);

		System.out.println("TOTALS FOR CHECKOUT ,SHIPPING AND CONFIRMATION");

		cartTotals = cartSteps.grabTotals();
		
		
		System.out.println("lists size");
		System.out.println(productsList.size());
		System.out.println(productsList.get(0).getName());
		System.out.println(productsList.get(0).getQuantity());
		System.out.println(productsList.get(1).getName());
		System.out.println(productsList.get(1).getQuantity());
		System.out.println(productsList.get(2).getName());
		System.out.println(productsList.get(2).getQuantity());
		System.out.println(productsList.get(3).getName());
		System.out.println(productsList.get(3).getQuantity());
		System.out.println(productsList.get(4).getName());
		System.out.println(productsList.get(4).getQuantity());
		System.out.println("----------------------");
		System.out.println(productsList50.size());
		System.out.println(productsList50.get(0).getName());
		System.out.println(productsList50.get(0).getQuantity());
		System.out.println(productsList50.get(1).getName());
		System.out.println(productsList50.get(1).getQuantity());
		System.out.println(productsList50.get(2).getName());
		System.out.println(productsList50.get(2).getQuantity());
		System.out.println("----------------------");
		System.out.println(productsList25.size());
		System.out.println(productsList25.get(0).getName());
		System.out.println(productsList25.get(0).getQuantity());
		System.out.println("----------------------");
		System.out.println(productsListMarketing.size());
		System.out.println(productsListMarketing.get(0).getName());
		System.out.println(productsListMarketing.get(0).getQuantity());
		System.out.println(productsListMarketing.get(1).getName());
		System.out.println(productsListMarketing.get(1).getQuantity());		
		System.out.println("----------------------");
		System.out.println(allProductsList.size());
		System.out.println(allProductsList.get(0).getName());
		System.out.println(allProductsList.get(0).getQuantity());
		System.out.println(allProductsList.get(1).getName());
		System.out.println(allProductsList.get(1).getQuantity());
		System.out.println(allProductsList.get(2).getName());
		System.out.println(allProductsList.get(2).getQuantity());
		System.out.println(allProductsList.get(3).getName());
		System.out.println(allProductsList.get(3).getQuantity());
		System.out.println(allProductsList.get(4).getName());
		System.out.println(allProductsList.get(4).getQuantity());
		System.out.println(allProductsList.get(5).getName());
		System.out.println(allProductsList.get(5).getQuantity());

		cartSteps.clickGoToShipping();

		List<CartProductModel> shippingProducts = shippingSteps.grabProductsList();
		PrintUtils.printList(shippingProducts);
		

		ShippingModel shippingTotals = shippingSteps.grabSurveyData();
		

		shippingSteps.clickGoToPaymentMethod();

		paymentSteps.expandCreditCardForm();

		paymentSteps.fillCreditCardForm(creditCardData);

		List<CartProductModel> confirmationProducts = confirmationSteps.grabProductsList();

		CartTotalsModel confirmationTotals = confirmationSteps.grabSurveyData();		

		confirmationSteps.agreeAndCheckout();

		validationSteps.verifySuccessMessage();		
		
		
		cartWorkflows.setValidateProductsModels(productsList50, cartProductsWith50Discount);
		cartWorkflows.validateProducts("CART PHASE PRODUCTS VALIDATION FOR 50 SECTION");

		cartWorkflows.setValidateProductsModels(productsList25, cartProductsWith25Discount);
		cartWorkflows.validateProducts("CART PHASE PRODUCTS VALIDATION FOR 25 SECTION");
		
		cartWorkflows.setValidateProductsModels(productsListMarketing, cartMarketingMaterialsProducts);
		cartWorkflows.validateProducts("CART PHASE PRODUCTS VALIDATION FOR MARKETING MATERIAL SECTION");

		cartWorkflows.setValidateProductsModels(allProductsList, shippingProducts);
		cartWorkflows.validateProducts2("SHIPPING PHASE PRODUCTS VALIDATION");

		cartWorkflows.setValidateProductsModels(allProductsList, confirmationProducts);
		cartWorkflows.validateProducts2("CONFIRMATION PHASE PRODUCTS VALIDATION");

//		cartWorkflows.setCheckCalculationTotalsModels(totalsCalculated, cartTotals);
//		cartWorkflows.checkCalculationTotals("CART TOTALS");
		

	}

	@Test
	public void us002UserProfileOrderId() {

		// After validation - grab order number
		headerSteps.redirectToProfileHistory();
		List<OrderModel> orderHistory = profileSteps.grabOrderHistory();

		String orderId = orderHistory.get(0).getOrderId();
		orderNumber.setOrderId(orderId);

	}

	@After
	public void saveData() {
		MongoWriter.saveOrderModel(orderNumber, getClass().getSimpleName());
		MongoWriter.saveTotalsModel(cartTotals, getClass().getSimpleName());
		//TODO verify if productsList or allProductsList should be persisted,depending on how the products appear in order (backend)
		for (ProductBasicModel product : allProductsList) {
			MongoWriter.saveProductBasicModel(product, getClass().getSimpleName());
		}

	}
	
	
	private ProductBasicModel newProductObject(String name, String price, String type, String quantity){
		ProductBasicModel newProduct = new ProductBasicModel();
		newProduct.setName(name);
		newProduct.setPrice(price);
		newProduct.setQuantity(quantity);
		newProduct.setType(type);
		
		return newProduct;
	}

}
