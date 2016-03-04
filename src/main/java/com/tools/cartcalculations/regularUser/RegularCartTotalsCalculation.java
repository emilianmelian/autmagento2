package com.tools.cartcalculations.regularUser;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import com.tools.cartcalculations.GeneralCartCalculations;
import com.tools.data.RegularCartCalcDetailsModel;
import com.tools.data.frontend.RegularBasicProductModel;
import com.tools.data.frontend.ShippingModel;
import com.tools.env.constants.ConfigConstants;
import com.tools.env.constants.ContextConstants;

public class RegularCartTotalsCalculation {

	public static RegularCartCalcDetailsModel calculateTotals(List<RegularBasicProductModel> productsList, String taxClass, String voucherValue, String shippingValue) {
		RegularCartCalcDetailsModel result = new RegularCartCalcDetailsModel();

		BigDecimal subtotal = BigDecimal.ZERO;
		BigDecimal tax = BigDecimal.ZERO;
		BigDecimal totalAmount = BigDecimal.ZERO;
		BigDecimal jewerlyDiscount = BigDecimal.ZERO;
		BigDecimal forthyDiscount = BigDecimal.ZERO;
		BigDecimal buy3Get1 = BigDecimal.ZERO;
		BigDecimal voucherPrice = BigDecimal.valueOf(Double.parseDouble(voucherValue));

		for (RegularBasicProductModel product : productsList) {
			subtotal = subtotal.add(BigDecimal.valueOf(Double.parseDouble(product.getFinalPrice())));
			if (product.getBonusType().contentEquals(ContextConstants.JEWELRY_BONUS)) {
				jewerlyDiscount = jewerlyDiscount.add(BigDecimal.valueOf(Double.parseDouble(product.getBunosValue())));
				jewerlyDiscount.setScale(2, RoundingMode.HALF_UP);
			}
			if (product.getBonusType().contentEquals(ContextConstants.DISCOUNT_40_BONUS)) {
				forthyDiscount = forthyDiscount.add(BigDecimal.valueOf(Double.parseDouble(product.getBunosValue())));
				forthyDiscount.setScale(2, RoundingMode.HALF_UP);
			}
		}
		totalAmount = calculateTotalAmount(subtotal, jewerlyDiscount, forthyDiscount, buy3Get1, voucherPrice);

		shippingValue = GeneralCartCalculations.calculateNewShipping(totalAmount, BigDecimal.valueOf(Double.parseDouble(voucherValue)),
				BigDecimal.valueOf(Double.parseDouble(shippingValue)));
		
		System.out.println(" new shippingValue: " + shippingValue);

		tax = totalAmount.add(BigDecimal.valueOf(Double.parseDouble(shippingValue)));
		tax = tax.multiply(BigDecimal.valueOf(Double.parseDouble(taxClass)));
		tax = tax.divide(BigDecimal.valueOf(Double.parseDouble("100") + Double.parseDouble(taxClass)), 2, BigDecimal.ROUND_HALF_UP);

		result.setSubTotal(String.valueOf(subtotal.setScale(2, RoundingMode.HALF_UP)));
		result.setTotalAmount(String.valueOf(totalAmount.setScale(2, RoundingMode.HALF_UP)));
		result.setTax(String.valueOf(tax));
		result.addSegment(ConfigConstants.JEWELRY_BONUS, String.valueOf(jewerlyDiscount));
		result.addSegment(ConfigConstants.DISCOUNT_40_BONUS, String.valueOf(forthyDiscount));
		result.addSegment(ConfigConstants.DISCOUNT_BUY_3_GET_1, String.valueOf(buy3Get1));
		result.addSegment(ConfigConstants.VOUCHER_DISCOUNT, String.valueOf(voucherValue));

		if (voucherPrice.compareTo(subtotal) > 0) {
			result.addSegment(ConfigConstants.VOUCHER_DISCOUNT, String.valueOf(subtotal));
		}

		return result;
	}

	public static RegularCartCalcDetailsModel calculateTotalsWithBuy3Get1Active(List<RegularBasicProductModel> productsList,
			List<RegularBasicProductModel> productsListForBuy3Get1, String taxClass, String voucherValue, String shippingValue) {

		RegularCartCalcDetailsModel result = new RegularCartCalcDetailsModel();

		BigDecimal subtotal = BigDecimal.ZERO;
		BigDecimal tax = BigDecimal.ZERO;
		BigDecimal totalAmount = BigDecimal.ZERO;
		BigDecimal jewerlyDiscount = BigDecimal.ZERO;
		BigDecimal forthyDiscount = BigDecimal.ZERO;
		BigDecimal buy3Get1 = RegularCartBuy3Get1Calculation.calculateTotalBuy3Get1Discount(productsListForBuy3Get1);
		BigDecimal voucherPrice = BigDecimal.valueOf(Double.parseDouble(voucherValue));

		for (RegularBasicProductModel product : productsList) {
			subtotal = subtotal.add(BigDecimal.valueOf(Double.parseDouble(product.getFinalPrice())));
			if (product.getBonusType().contentEquals(ContextConstants.JEWELRY_BONUS)) {
				jewerlyDiscount = jewerlyDiscount.add(BigDecimal.valueOf(Double.parseDouble(product.getBunosValue())));
				jewerlyDiscount.setScale(2, RoundingMode.HALF_UP);
			}
			if (product.getBonusType().contentEquals(ContextConstants.DISCOUNT_40_BONUS)) {
				forthyDiscount = forthyDiscount.add(BigDecimal.valueOf(Double.parseDouble(product.getBunosValue())));
				forthyDiscount.setScale(2, RoundingMode.HALF_UP);
			}
		}
		totalAmount = calculateTotalAmount(subtotal, jewerlyDiscount, forthyDiscount, buy3Get1, voucherPrice);

		tax = totalAmount.add(BigDecimal.valueOf(Double.parseDouble(shippingValue)));
		tax = tax.multiply(BigDecimal.valueOf(Double.parseDouble(taxClass)));
		tax = tax.divide(BigDecimal.valueOf(Double.parseDouble("100") + Double.parseDouble(taxClass)), 2, BigDecimal.ROUND_HALF_UP);

		result.setSubTotal(String.valueOf(subtotal.setScale(2, RoundingMode.HALF_UP)));
		result.setTotalAmount(String.valueOf(totalAmount.setScale(2, RoundingMode.HALF_UP)));
		result.setTax(String.valueOf(tax));
		result.addSegment(ConfigConstants.JEWELRY_BONUS, String.valueOf(jewerlyDiscount));
		result.addSegment(ConfigConstants.DISCOUNT_40_BONUS, String.valueOf(forthyDiscount));
		result.addSegment(ConfigConstants.DISCOUNT_BUY_3_GET_1, String.valueOf(buy3Get1));
		result.addSegment(ConfigConstants.VOUCHER_DISCOUNT, String.valueOf(voucherValue));

		return result;
	}

	private static BigDecimal calculateTotalAmount(BigDecimal subtotal, BigDecimal jewelryDiscount, BigDecimal forthyDiscount, BigDecimal buy3Get1, BigDecimal voucherPrice) {

		BigDecimal result = BigDecimal.ZERO;

		result = result.add(subtotal);
		result = result.subtract(jewelryDiscount);
		result = result.subtract(forthyDiscount);
		result = result.subtract(buy3Get1);
		result = result.subtract(voucherPrice);

		result = result.compareTo(BigDecimal.ZERO) > 0 ? result : BigDecimal.ZERO;

		return result.setScale(2, RoundingMode.HALF_UP);
	}

	public static ShippingModel calculateShippingTotals(RegularCartCalcDetailsModel discountCalculationModel, String shippingValue) {
		ShippingModel result = new ShippingModel();

		result.setSubTotal(discountCalculationModel.getSubTotal());

		// discount calculation
		BigDecimal discountCalculation = BigDecimal.ZERO;
		discountCalculation = discountCalculation.add(BigDecimal.valueOf(Double.parseDouble(discountCalculationModel.getSegments().get(ConfigConstants.JEWELRY_BONUS))));
		discountCalculation = discountCalculation.add(BigDecimal.valueOf(Double.parseDouble(discountCalculationModel.getSegments().get(ConfigConstants.DISCOUNT_40_BONUS))));
		discountCalculation = discountCalculation.add(BigDecimal.valueOf(Double.parseDouble(discountCalculationModel.getSegments().get(ConfigConstants.DISCOUNT_BUY_3_GET_1))));
		discountCalculation = discountCalculation.add(BigDecimal.valueOf(Double.parseDouble(discountCalculationModel.getSegments().get(ConfigConstants.VOUCHER_DISCOUNT))));

		String newShippingValue = GeneralCartCalculations.calculateNewShipping(BigDecimal.valueOf(Double.parseDouble(discountCalculationModel.getTotalAmount())),
				BigDecimal.valueOf(Double.parseDouble(discountCalculationModel.getSegments().get(ConfigConstants.VOUCHER_DISCOUNT))),
				BigDecimal.valueOf(Double.parseDouble(shippingValue)));

		result.setDiscountPrice(discountCalculation.toString());
		result.setShippingPrice(shippingValue);

		// totals calculation
		BigDecimal totalAmountCalculation = BigDecimal.ZERO;
		totalAmountCalculation = totalAmountCalculation.add(BigDecimal.valueOf(Double.parseDouble(discountCalculationModel.getTotalAmount())));
		totalAmountCalculation = totalAmountCalculation.add(BigDecimal.valueOf(Double.parseDouble(newShippingValue)));
		result.setTotalAmount(totalAmountCalculation.toString());

		return result;
	}

}
