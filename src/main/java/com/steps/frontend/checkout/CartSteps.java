package com.steps.frontend.checkout;

import java.util.List;

import net.thucydides.core.annotations.Step;

import com.tools.AbstractSteps;
import com.tools.PrintUtils;
import com.tools.data.CartProductModel;
import com.tools.data.CartTotalsModel;

public class CartSteps extends  AbstractSteps{
	
	private static final long serialVersionUID = 4077671481867589798L;

//	@Step
	public List<CartProductModel> grabProductsData(){
		PrintUtils.printList(cartPage().grabProductsData());
		
		return cartPage().grabProductsData();
	}

//	@Step
	public CartTotalsModel grabTotals() {
		return cartPage().grabTotals();
	}
	
	@Step
	public void clickGoToShipping(){
		cartPage().clickToShipping();
	}

}
