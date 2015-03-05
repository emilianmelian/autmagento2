package com.tools.datahandlers;

import java.util.ArrayList;
import java.util.List;

import com.tools.calculation.CartBuy3Get1Calculation;
import com.tools.calculation.CartCalculation;
import com.tools.calculation.CartDiscountsCalculation;
import com.tools.calculation.CartTotalsCalculation;
import com.tools.data.CalcDetailsModel;
import com.tools.data.frontend.BasicProductModel;
import com.tools.data.frontend.ShippingModel;

public class CartCalculator {
	
	public static List<BasicProductModel> allProductsList = new ArrayList<BasicProductModel>();
	public static List<BasicProductModel> productsList25 = new ArrayList<BasicProductModel>();
	public static List<BasicProductModel> productsList50 = new ArrayList<BasicProductModel>();
	public static List<BasicProductModel> productsListMarketing = new ArrayList<BasicProductModel>();
	public static List<BasicProductModel> productListForBuy3Get1 = new ArrayList<BasicProductModel>();
	public static List<BasicProductModel> productList25AndMmWithBuy3Get1Applied = new ArrayList<BasicProductModel>();
	
	public static List<BasicProductModel> calculatedProductsList25 = new ArrayList<BasicProductModel>();
	public static List<BasicProductModel> calculatedProductsList50 = new ArrayList<BasicProductModel>();
	public static List<BasicProductModel> calculatedProductsListMarketing = new ArrayList<BasicProductModel>();
	public static List<BasicProductModel> allProductsListRecalculated = new ArrayList<BasicProductModel>();
	
	public static CalcDetailsModel calculatedTotalsDiscounts = new CalcDetailsModel();
	public static ShippingModel shippingCalculatedModel = new ShippingModel();
	
	public static void wipe(){
		allProductsList = new ArrayList<BasicProductModel>();
		productsList25 = new ArrayList<BasicProductModel>();
		productsList50 = new ArrayList<BasicProductModel>();
		productsListMarketing = new ArrayList<BasicProductModel>();
		
		productListForBuy3Get1 = new ArrayList<BasicProductModel>();
		productList25AndMmWithBuy3Get1Applied = new ArrayList<BasicProductModel>();
		
		calculatedProductsList25 = new ArrayList<BasicProductModel>();
		calculatedProductsList50 = new ArrayList<BasicProductModel>();
		calculatedProductsListMarketing = new ArrayList<BasicProductModel>();
		allProductsListRecalculated = new ArrayList<BasicProductModel>();
		
		calculatedTotalsDiscounts = new CalcDetailsModel();
		shippingCalculatedModel = new ShippingModel();
		
	}
	
	public static void calculateJMDiscounts(String jewelryDiscount, String marketingDiscount, String taxClass, String shippingValue){
		allProductsList.addAll(productsList25);
		allProductsList.addAll(productsList50);
		allProductsList.addAll(productsListMarketing);
		
		calculatedProductsList25 = CartDiscountsCalculation.calculateProductsfor25Discount(productsList25, jewelryDiscount);

		calculatedProductsList50 = CartDiscountsCalculation.calculateProductsfor50Discount(productsList50,productsList25, jewelryDiscount);

		calculatedProductsListMarketing = CartDiscountsCalculation.calculateProductsforMarketingMaterial(productsListMarketing, marketingDiscount);

		allProductsListRecalculated.addAll(calculatedProductsList50);
		allProductsListRecalculated.addAll(calculatedProductsList25);
		allProductsListRecalculated.addAll(calculatedProductsListMarketing);
		
		calculatedTotalsDiscounts = CartTotalsCalculation.calculateCartProductsTotals(allProductsListRecalculated, jewelryDiscount, marketingDiscount,taxClass);
	
		shippingCalculatedModel = CartCalculation.calculateShippingTotals(calculatedTotalsDiscounts, shippingValue);
	}
	
	
	public static void calculate3P1Rule(String jewelryDiscount, String marketingDiscount, String taxClass, String shippingValue, String shippingValueForLessThan150){
		// 3+1 rule
		productListForBuy3Get1.addAll(productsList25);
		productListForBuy3Get1.addAll(productsListMarketing);
		productList25AndMmWithBuy3Get1Applied = CartBuy3Get1Calculation.applyBuy3Get1OnTheCart(productListForBuy3Get1);
		
		allProductsList.addAll(productsList50);
		allProductsList.addAll(productList25AndMmWithBuy3Get1Applied);
		
		calculatedProductsList25 = CartDiscountsCalculation.calculateProductsfor25Discount(productsList25, jewelryDiscount);

		calculatedProductsList50 = CartDiscountsCalculation.calculateProductsfor50Discount(productsList50,productsList25, jewelryDiscount);

		calculatedProductsListMarketing = CartDiscountsCalculation.calculateProductsforMarketingMaterial(productsListMarketing, marketingDiscount);

		allProductsListRecalculated.addAll(calculatedProductsList50);
		allProductsListRecalculated.addAll(calculatedProductsList25);
		allProductsListRecalculated.addAll(calculatedProductsListMarketing);
		
		calculatedTotalsDiscounts = CartTotalsCalculation.calculateCartProductsTotalsBuy3GetOneRuleApplied(allProductsListRecalculated, jewelryDiscount, marketingDiscount,taxClass);
		shippingValue = Double.parseDouble(calculatedTotalsDiscounts.getTotalAmount()) >= 150 ?
				 shippingValue : shippingValueForLessThan150;
		shippingCalculatedModel = CartCalculation.calculateShippingTotals(calculatedTotalsDiscounts, shippingValue);
	}
}
