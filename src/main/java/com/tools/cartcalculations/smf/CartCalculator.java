package com.tools.cartcalculations.smf;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.connectors.http.MagentoProductCalls;
import com.tools.data.CalcDetailsModel;
import com.tools.data.frontend.BasicProductModel;
import com.tools.data.frontend.ShippingModel;
import com.tools.data.soap.ProductDetailedModel;
import com.tools.utils.PrintUtils;
import com.workflows.frontend.AddProductsWorkflow;

public class CartCalculator {

	public static List<BasicProductModel> allProductsList = new ArrayList<BasicProductModel>();
	//public static List<RegularBasicProductModel> allRegularProductsList = new ArrayList<RegularBasicProductModel>();
	public static List<BasicProductModel> allProductsListDiscountRule = new ArrayList<BasicProductModel>();
	public static List<BasicProductModel> productsList25 = new ArrayList<BasicProductModel>();
	public static List<BasicProductModel> productsList50 = new ArrayList<BasicProductModel>();
	public static List<BasicProductModel> productsListMarketing = new ArrayList<BasicProductModel>();
	public static List<BasicProductModel> productsList25DiscountRule = new ArrayList<BasicProductModel>();
	public static List<BasicProductModel> productsList50DiscountRule = new ArrayList<BasicProductModel>();
	public static List<BasicProductModel> productsListMarketingDiscountRule = new ArrayList<BasicProductModel>();
	public static List<BasicProductModel> productListForBuy3Get1 = new ArrayList<BasicProductModel>();
	public static List<BasicProductModel> productList25AndMmWithBuy3Get1Applied = new ArrayList<BasicProductModel>();

	public static List<BasicProductModel> calculatedProductsList25 = new ArrayList<BasicProductModel>();
	public static List<BasicProductModel> calculatedProductsList50 = new ArrayList<BasicProductModel>();
	public static List<BasicProductModel> calculatedProductsListMarketing = new ArrayList<BasicProductModel>();
	public static List<BasicProductModel> allProductsListRecalculated = new ArrayList<BasicProductModel>();
	public static List<BasicProductModel> shippingProductsList = new ArrayList<BasicProductModel>();
	
	public static List<BasicProductModel> shippingProductsListTp0 = new ArrayList<BasicProductModel>();
	public static List<BasicProductModel> shippingProductsListTp1 = new ArrayList<BasicProductModel>();
	public static List<BasicProductModel> shippingProductsListTp2 = new ArrayList<BasicProductModel>();
	//emilina
	
	public static List<BasicProductModel> productsListTp0 = new ArrayList<BasicProductModel>();
	public static List<BasicProductModel> productsListTp1 = new ArrayList<BasicProductModel>();
	public static List<BasicProductModel> productsListTp2 = new ArrayList<BasicProductModel>();
	public static List<BasicProductModel> productsListTp3 = new ArrayList<BasicProductModel>();
	public static List<BasicProductModel> productsListTp4 = new ArrayList<BasicProductModel>();
	public static List<BasicProductModel> productsListTp5 = new ArrayList<BasicProductModel>();
	
	
	public static CalcDetailsModel calculatedTotalsDiscounts = new CalcDetailsModel();
	//emilian
	public static CalcDetailsModel calculatedTotalsDiscountsTp0 = new CalcDetailsModel();
	public static CalcDetailsModel calculatedTotalsDiscountsTp1 = new CalcDetailsModel();
	public static CalcDetailsModel calculatedTotalsDiscountsTp2 = new CalcDetailsModel();
	public static CalcDetailsModel calculatedTotalsDiscountsTp3 = new CalcDetailsModel();
			
			
	public static ShippingModel shippingCalculatedModel = new ShippingModel();
	
	//emilian
	public static ShippingModel shippingCalculatedModeTP0 = new ShippingModel();
	public static ShippingModel shippingCalculatedModeTP1 = new ShippingModel();
	public static ShippingModel shippingCalculatedModeTP2 = new ShippingModel();
	public static ShippingModel shippingCalculatedModeTP3 = new ShippingModel();

	public static String delta = "0";

	public static void wipe() {
		allProductsList = new ArrayList<BasicProductModel>();
	//	allRegularProductsList = new ArrayList<RegularBasicProductModel>();
		allProductsListDiscountRule = new ArrayList<BasicProductModel>();
		productsList25 = new ArrayList<BasicProductModel>();
		productsList50 = new ArrayList<BasicProductModel>();
		productsListMarketing = new ArrayList<BasicProductModel>();
		productsList25DiscountRule = new ArrayList<BasicProductModel>();
		productsList50DiscountRule = new ArrayList<BasicProductModel>();
		productsListMarketingDiscountRule = new ArrayList<BasicProductModel>();
		productListForBuy3Get1 = new ArrayList<BasicProductModel>();
		productList25AndMmWithBuy3Get1Applied = new ArrayList<BasicProductModel>();
		shippingProductsList = new ArrayList<BasicProductModel>();
		calculatedProductsList25 = new ArrayList<BasicProductModel>();
		calculatedProductsList50 = new ArrayList<BasicProductModel>();
		calculatedProductsListMarketing = new ArrayList<BasicProductModel>();
		allProductsListRecalculated = new ArrayList<BasicProductModel>();
		calculatedTotalsDiscounts = new CalcDetailsModel();
		shippingCalculatedModel = new ShippingModel();
		delta = "0";
	}

	public static void calculateShippingWith19PercentRemoved(String shippingValue) {

		shippingCalculatedModel = CartCalculation.remove119VAT(calculatedTotalsDiscounts, shippingValue);
		shippingProductsList = CartCalculation.remove19(allProductsList);

	}
	
	
	public static ShippingModel calculateShippingWith19PercentRemovedForTp(CalcDetailsModel calculatedTotalsDiscountsTp,String shippingValue) {
		return CartCalculation.remove119VAT(calculatedTotalsDiscountsTp, shippingValue);

	}
	
	public static List<BasicProductModel> calculatedShippingForAllProductsTP(List<BasicProductModel> allProductsList){
		return CartCalculation.remove19(allProductsList);
	}

	public static void calculateJMDiscounts(String jewelryDiscount, String marketingDiscount, String taxClass, String shippingValue) {
		allProductsList.addAll(productsList25);
		allProductsList.addAll(productsList50);
		allProductsList.addAll(productsListMarketing);

		calculatedProductsList25 = CartDiscountsCalculation.calculateProductsfor25Discount(productsList25, jewelryDiscount);

	//	calculatedProductsList50 = CartDiscountsCalculation.calculateProductsfor50Discount(productsList50, productsList25, jewelryDiscount);

	//	calculatedProductsListMarketing = CartDiscountsCalculation.calculateProductsforMarketingMaterial(productsListMarketing, marketingDiscount);

	//	allProductsListRecalculated.addAll(calculatedProductsList50);
		allProductsListRecalculated.addAll(calculatedProductsList25);
		//allProductsListRecalculated.addAll(calculatedProductsListMarketing);

		calculatedTotalsDiscounts = CartTotalsCalculation.calculateCartProductsTotals(allProductsListRecalculated, jewelryDiscount, marketingDiscount, taxClass, shippingValue,
				shippingValue);
		shippingCalculatedModel = CartCalculation.calculateShippingTotals(calculatedTotalsDiscounts, shippingValue);
	}
	
	public static void calculateLMDiscounts(String jewelryDiscount, String marketingDiscount, String taxClass, String shippingValue) {
		allProductsList.addAll(productsList25);
		allProductsList.addAll(productsList50);
		allProductsList.addAll(productsListMarketing);

		calculatedProductsList25 = CartDiscountsCalculation.calculateProductsfor25Discount(productsList25, jewelryDiscount);

	//	calculatedProductsList50 = CartDiscountsCalculation.calculateProductsfor50Discount(productsList50, productsList25, jewelryDiscount);

		calculatedProductsListMarketing = CartDiscountsCalculation.calculateProductsforMarketingMaterial(productsListMarketing, marketingDiscount);

	//	allProductsListRecalculated.addAll(calculatedProductsList50);
		allProductsListRecalculated.addAll(calculatedProductsList25);
		allProductsListRecalculated.addAll(calculatedProductsListMarketing);

		calculatedTotalsDiscounts = CartTotalsCalculation.calculateCartProductsTotals(allProductsListRecalculated, jewelryDiscount, marketingDiscount, taxClass, shippingValue,
				shippingValue);
		shippingCalculatedModel = CartCalculation.calculateShippingTotals(calculatedTotalsDiscounts, shippingValue);
	}
	
	
	public static void calculateJMDiscountsTP(String jewelryDiscount, String marketingDiscount, String taxClass, String shippingValue) {
		allProductsList.addAll(productsList25);
		allProductsList.addAll(productsList50);
		allProductsList.addAll(productsListMarketing);
		
		

		calculatedProductsList25 = CartDiscountsCalculation.calculateProductsfor25Discount(productsList25, jewelryDiscount);
		calculatedProductsList50 = CartDiscountsCalculation.calculateProductsfor50Discount(productsList50, productsList25, jewelryDiscount);
		calculatedProductsListMarketing = CartDiscountsCalculation.calculateProductsforMarketingMaterial(productsListMarketing, marketingDiscount);

		allProductsListRecalculated.addAll(calculatedProductsList50);
		allProductsListRecalculated.addAll(calculatedProductsList25);
		allProductsListRecalculated.addAll(calculatedProductsListMarketing);

		calculatedTotalsDiscounts = CartTotalsCalculation.calculateCartProductsTotals(allProductsListRecalculated, jewelryDiscount, marketingDiscount, taxClass, shippingValue,
				shippingValue);
		calculatedTotalsDiscountsTp0=CartTotalsCalculation.calculateCartProductsTotalsTP(allProductsListRecalculated,productsListTp0 ,jewelryDiscount, marketingDiscount, taxClass, shippingValue, shippingValue);
		calculatedTotalsDiscountsTp1=CartTotalsCalculation.calculateCartProductsTotalsTP(allProductsListRecalculated,productsListTp1 ,jewelryDiscount, marketingDiscount, taxClass, shippingValue, shippingValue);
		if(!productsListTp2.isEmpty()){
			calculatedTotalsDiscountsTp2=CartTotalsCalculation.calculateCartProductsTotalsTP(allProductsListRecalculated,productsListTp2 ,jewelryDiscount, marketingDiscount, taxClass, shippingValue, shippingValue);
		}
		
	}
	
	
	
	
	public static ShippingModel calculateShippingTotalsTP(CalcDetailsModel calculatedTotalsDiscountsTp,String shippingValue){
		return CartCalculation.calculateShippingTotals(calculatedTotalsDiscountsTp, shippingValue);
	}

	public static void calculateJMDiscountsWithActiveDiscountVoucher(String ruleDiscount, String jewelryDiscount, String marketingDiscount, String taxClass, String shippingValue) {

		allProductsList.addAll(productsList25);
		allProductsList.addAll(productsList50);
		allProductsList.addAll(productsListMarketing);

		BigDecimal productsSum = CartDiscountsCalculation.calculateSubtotal(allProductsList);

		productsList25DiscountRule = CartDiscountsCalculation.calculateAskingPriceWithActiveDiscountRule(productsList25, ruleDiscount, productsSum);
		productsList50DiscountRule = CartDiscountsCalculation.calculateAskingPriceWithActiveDiscountRule(productsList50, ruleDiscount, productsSum);
		productsListMarketingDiscountRule = CartDiscountsCalculation.calculateAskingPriceWithActiveDiscountRule(productsListMarketing, ruleDiscount, productsSum);

		calculatedProductsList25 = CartDiscountsCalculation.calculateProductsfor25Discount(productsList25DiscountRule, jewelryDiscount);

		calculatedProductsList50 = CartDiscountsCalculation.calculateProductsfor50Discount(productsList50DiscountRule, productsList25DiscountRule, jewelryDiscount);

		calculatedProductsListMarketing = CartDiscountsCalculation.calculateProductsforMarketingMaterial(productsListMarketingDiscountRule, marketingDiscount);

		allProductsListRecalculated.addAll(calculatedProductsList50);
		allProductsListRecalculated.addAll(calculatedProductsList25);
		allProductsListRecalculated.addAll(calculatedProductsListMarketing);


		calculatedTotalsDiscounts = CartTotalsCalculation.calculateCartProductsTotalsWithDiscountRuleActive(allProductsListRecalculated, ruleDiscount, jewelryDiscount,
				marketingDiscount, taxClass, shippingValue);
		shippingCalculatedModel = CartCalculation.calculateShippingTotals(calculatedTotalsDiscounts, shippingValue);
	}

	public static void calculateKoboSubscriptionTotals(String jewelryDiscount, String marketingDiscount, String taxClass, String shippingValue) {

		calculatedTotalsDiscounts = CartTotalsCalculation.calculateCartProductsTotals(allProductsList, jewelryDiscount, marketingDiscount, taxClass, shippingValue, shippingValue);
		PrintUtils.printCalcDetailsModel(calculatedTotalsDiscounts);
		shippingCalculatedModel = CartCalculation.calculateShippingTotals(calculatedTotalsDiscounts, shippingValue);
		PrintUtils.printShippingTotals(shippingCalculatedModel);
	}

	public static void calculateJMDiscountsForBuy3Get1Rule(String jewelryDiscount, String marketingDiscount, String taxClass, String shippingValue,
			String shippingValueForLessThan150) {
		allProductsList.clear();
		allProductsList.addAll(productsList25);
		allProductsList.addAll(productsList50);
		allProductsList.addAll(productsListMarketing);

		calculatedProductsList25 = CartDiscountsCalculation.calculateProductsfor25Discount(productsList25, jewelryDiscount);

		calculatedProductsList50 = CartDiscountsCalculation.calculateProductsfor50Discount(productsList50, productsList25, jewelryDiscount);

		calculatedProductsListMarketing = CartDiscountsCalculation.calculateProductsforMarketingMaterial(productsListMarketing, marketingDiscount);

		allProductsListRecalculated.clear();
		allProductsListRecalculated.addAll(calculatedProductsList50);
		allProductsListRecalculated.addAll(calculatedProductsList25);
		allProductsListRecalculated.addAll(calculatedProductsListMarketing);
		
		for (BasicProductModel basicProductModel : calculatedProductsList25) {
			System.out.println("Produs din 25 section: "+basicProductModel);
		}

		calculatedTotalsDiscounts = CartTotalsCalculation.calculateCartProductsTotals(allProductsListRecalculated, jewelryDiscount, marketingDiscount, taxClass, shippingValue,
				shippingValueForLessThan150);
		shippingValue = Double.parseDouble(calculatedTotalsDiscounts.getTotalAmount()) >= 150 ? shippingValue : shippingValueForLessThan150;
		shippingCalculatedModel = CartCalculation.calculateShippingTotals(calculatedTotalsDiscounts, shippingValue);
	}

	public static void calculate3P1Rule(String jewelryDiscount, String marketingDiscount, String taxClass, String shippingValue, String shippingValueForLessThan150) {
		// 3+1 rule
		productListForBuy3Get1.addAll(productsList25);
		productListForBuy3Get1.addAll(productsListMarketing);
		productList25AndMmWithBuy3Get1Applied = CartBuy3Get1Calculation.applyBuy3Get1OnTheCart(productListForBuy3Get1);

		allProductsList.addAll(productsList50);
		allProductsList.addAll(productList25AndMmWithBuy3Get1Applied);

		calculatedProductsList25 = CartDiscountsCalculation.calculateProductsfor25Discount(productList25AndMmWithBuy3Get1Applied, jewelryDiscount);
		for (BasicProductModel basicProductModel : calculatedProductsList25) {
			System.out.println("calculated 25 pr list: "+basicProductModel);
		}

		calculatedProductsList50 = CartDiscountsCalculation.calculateProductsfor50Discount(productsList50, productsList25, jewelryDiscount);

		calculatedProductsListMarketing = CartDiscountsCalculation.calculateProductsforMarketingMaterial(productsListMarketing, marketingDiscount);

		allProductsListRecalculated.addAll(calculatedProductsList50);
		allProductsListRecalculated.addAll(calculatedProductsList25);
		allProductsListRecalculated.addAll(calculatedProductsListMarketing);

		calculatedTotalsDiscounts = CartTotalsCalculation.calculateCartProductsTotalsBuy3GetOneRuleApplied(allProductsListRecalculated, jewelryDiscount, marketingDiscount,
				taxClass, shippingValue, shippingValueForLessThan150);
		shippingValue = Double.parseDouble(calculatedTotalsDiscounts.getTotalAmount()) >= 150 ? shippingValue : shippingValueForLessThan150;
		shippingCalculatedModel = CartCalculation.calculateShippingTotals(calculatedTotalsDiscounts, shippingValue);
	}
	
	
	private static ProductDetailedModel genProduct1 = new ProductDetailedModel();
	private static ProductDetailedModel genProduct2 = new ProductDetailedModel();
	private static ProductDetailedModel genProduct3 = new ProductDetailedModel();
//	private static List<BasicProductModel> productsList =new ArrayList<BasicProductModel>();
	private static BasicProductModel productData;
	private static AddProductsWorkflow addProductsWorkflow;
//	private static BasicProductModel productData2;
//	private static BasicProductModel productData3;
	public static List<BasicProductModel> productsList25Beta = new ArrayList<BasicProductModel>();
	
	public static void main(String[] args) {
		
		genProduct1 = MagentoProductCalls.createProductModelBeta();
		genProduct1.setIp("0");
		genProduct1.setPrice("49.90");
		
		genProduct2 = MagentoProductCalls.createProductModelBeta();
		genProduct2.setIp("0");
		genProduct2.setPrice("89.90");
		
		genProduct3 = MagentoProductCalls.createProductModelBeta();
		genProduct3.setIp("25");
		genProduct3.setPrice("49.90");
		
		productData = addProductsWorkflow.setBasicChildProductToCart(genProduct1, "1", "0","20");
		productData = addProductsWorkflow.setBasicChildProductToCart(genProduct2, "2", "0", "20");
/*		productData = addInCart(genProduct1, "1", "0", ConfigConstants.DISCOUNT_20);
		productData2=addInCart(genProduct2, "2", "0", ConfigConstants.DISCOUNT_20);*/
//		productData3=addInCart(genProduct3, "1", "0", ConfigConstants.DISCOUNT_20);
	/*	productsList25Beta.add(productData);
		productsList25Beta.add(productData2);*/
		productsList25.add(productData);
		productsList25.add(productData);
		calculateJMDiscounts("150","0","19","3.9");
		
		
		
//		productsList25Beta.add(productData3);
		
//		System.out.println(productsList25Beta.get(0).getFinalPrice());
//		
		/*BigDecimal sum25 = calculateDiscountAskingPriceSum(productsList25Beta, ConfigConstants.DISCOUNT_20);
		System.out.println("sumaaaa "+sum25);
	
		List<BasicProductModel> cartProductsBeta=calculateProductsfor25Discount(productsList25Beta,"150");
		
		System.out.println(""+cartProductsBeta.get(1).getFinalPrice());*/
	}
}
