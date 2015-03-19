
package com.steps.frontend.checkout.cart.regularCart;

import java.util.List;

import net.thucydides.core.annotations.Step;

import com.tools.data.frontend.RegularBasicProductModel;
import com.tools.data.frontend.RegularUserCartProductModel;
import com.tools.data.frontend.RegularUserCartTotalsModel;
import com.tools.requirements.AbstractSteps;

public class RegularUserCartSteps extends AbstractSteps {

	private static final long serialVersionUID = 1L;

	@Step
	public void selectProductDiscountType(String productCode, String discountType) {
		regularUserCartPage().selectProductDiscountType(productCode, discountType);
	}

	@Step
	public void validateThatVoucherCannotBeAppliedMessage() {
		regularUserCartPage().validateThatVoucherCannotBeAppliedMessage();
	}

	@Step
	public void updateProductList(List<RegularBasicProductModel> productsList, String productCode, String discountType) {
		regularUserCartPage().updateProductList(productsList, productCode, discountType);
	}

	@Step
	public List<RegularUserCartProductModel> grabProductsData() {
		return regularUserCartPage().grabProductsData();
	}

	@Step
	public RegularUserCartTotalsModel grabTotals() {
		return regularUserCartPage().grabTotals();
	}

	@Step
	public void typeCouponCode(String code) {
		regularUserCartPage().typeCouponCode(code);
	}

	@Step
	public void submitVoucherCode() {
		regularUserCartPage().submitVoucherCode();
	}

	@Step
	public void selectShippingOption(String option) {
		regularUserCartPage().selectShippingOption(option);
	}

	@Step
	public void updateProductQuantity(String quantity, String... terms) {
		regularUserCartPage().updateProductQuantity(quantity, terms);
	}

	@Step
	public void clickGoToShipping() {
		regularUserCartPage().clickToShipping();
	}

	@Step
	public void updateCart() {
		regularUserCartPage().clickUpdateCart();
		getDriver().navigate().refresh();
	}

	@Step
	public void wipeCart(String URL) {
		// modify URL to wipe cart
		URL = URL.replace("stylist/lounge/", "checkout/cart/clearAllItems/");
		// call cart wipe
		getDriver().get(URL);
		regularUserCartPage().verifyWipeCart();
	}
}

