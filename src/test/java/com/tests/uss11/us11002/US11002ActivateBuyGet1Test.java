package com.tests.uss11.us11002;

import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Story;
import net.thucydides.core.annotations.WithTag;
import net.thucydides.junit.runners.ThucydidesRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.steps.backend.promotion.PromotionSteps;
import com.tests.BaseTest;
import com.tools.requirements.Application;

@WithTag(name = "US11.2 Party Host Buys For Customer With Buy 3 Get 1 For 50% ", type = "Scenarios")
@Story(Application.PlaceACustomerOrderCart.US11_2.class)
@RunWith(ThucydidesRunner.class)
public class US11002ActivateBuyGet1Test extends BaseTest {
	@Steps
	public PromotionSteps promotionSteps;

	@Test
	public void us11002ActivateBuyGet1Test() {
		promotionSteps.activateBuy3Get1ForPlaceACustomerOrder();
	}
}
