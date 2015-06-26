package com.workflows.frontend.borrowCart;

import net.thucydides.core.annotations.StepGroup;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Title;

import com.steps.frontend.ProductSteps;
import com.steps.frontend.SearchSteps;
import com.tools.data.frontend.BorrowProductModel;
import com.tools.data.soap.ProductDetailedModel;

public class AddBorrowedProductsWorkflow {
	@Steps
	public SearchSteps searchSteps;
	@Steps
	public ProductSteps productSteps;

	@StepGroup
	@Title("Add borrowed product to cart")
	public BorrowProductModel setBorrowedProductToCart(ProductDetailedModel model, String price, String finalPrice) {
		return productSteps.setBorrowedProductAddToCart(model,price,finalPrice);
	}

}
