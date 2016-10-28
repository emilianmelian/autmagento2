package com.tests.us9.us9006;

import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Story;
import net.thucydides.core.annotations.WithTag;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.steps.backend.promotion.ShoppingCartPriceRulesSteps;
import com.tests.BaseTest;
import com.tools.requirements.Application;

@WithTag(name = "US3.6 Shop for myself VAT valid and SMB billing and shipping DE",type = "Scenarios")
@Story(Application.ShopForMyselfCart.US3_6.class)
@RunWith(SerenityRunner.class)
public class US9006DeactivateDiscountOnCartRuleTest extends BaseTest {

	@Steps
	public ShoppingCartPriceRulesSteps shoppingCartPriceRulesSteps;

	@Test
	public void us9006DeactivateDiscountOnCartRuleTest() {
		shoppingCartPriceRulesSteps.deactivateRule("AUT-Money voucher working on total - all carts");
	}
}
