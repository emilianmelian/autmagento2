package com.suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.tests.uss12.US12001ApplyCreditMemoOnKoboSubscriptionOrderTest;
import com.tests.uss12.US12001ApplyCreditMemoOnKoboUpgradeOrderTest;
import com.tests.uss12.US12001CancelCreditMemoForKoboSubscriptionTest;
import com.tests.uss12.US12001CancelCreditMemoForKoboUpgradeTest;
import com.tests.uss12.US12001CancelKoboInitialSubscriptionTest;
import com.tests.uss12.US12001ChechUsesPerCouponAfterSubscriptionCancelCmTest;
import com.tests.uss12.US12001ChechUsesPerCouponAfterSubscriptionCmTest;
import com.tests.uss12.US12001ChechUsesPerCouponAfterSubscriptionUpgardeCMTest;
import com.tests.uss12.US12001ChechUsesPerCouponAfterSubscriptionUpgradeCancelCMTest;
import com.tests.uss12.US12001ChechUsesPerCouponAfterSubscriptionUpgradeTest;
import com.tests.uss12.US12001InitialKoboSubscriptionTest;
import com.tests.uss12.US12001KoboSubscriptionOrderEmailTest;
import com.tests.uss12.US12001KoboSubscriptionTest;
import com.tests.uss12.US12001KoboSubscriptionUpgradeOrderEmailTest;
import com.tests.uss12.US12001KoboSubscriptionUpgradeTest;
import com.tests.uss12.US12001MarkAsPaidInitialKoboOrderTest2;
import com.tests.uss12.US12001MarkAsPaidKoboOrderTest2;
import com.tests.uss12.US12001StyleCoachRegistrationTest;
import com.tests.uss12.US12001StylistActivationTest;
import com.tests.uss12.US12001VerifyStylistKoboStatusAfterCancelCmOnSubscriptionTest;
import com.tests.uss12.US12001VerifyStylistKoboStatusAfterCmOnSubscriptionTest;
import com.tests.uss12.US12001VerifyStylistKoboStatusAfterSubscriptionTest;


@SuiteClasses({
	US12001StyleCoachRegistrationTest.class,
	US12001StylistActivationTest.class,
	//testing purpose
	US12001InitialKoboSubscriptionTest.class,
	US12001CancelKoboInitialSubscriptionTest.class,
	//	
	US12001KoboSubscriptionTest.class,
	US12001MarkAsPaidKoboOrderTest2.class,
	US12001MarkAsPaidInitialKoboOrderTest2.class,
	US12001KoboSubscriptionOrderEmailTest.class,
	
	//
	US12001VerifyStylistKoboStatusAfterSubscriptionTest.class,
	US12001ApplyCreditMemoOnKoboSubscriptionOrderTest.class,
	US12001VerifyStylistKoboStatusAfterCmOnSubscriptionTest.class,
	US12001ChechUsesPerCouponAfterSubscriptionCmTest.class,
	US12001CancelCreditMemoForKoboSubscriptionTest.class,
	US12001VerifyStylistKoboStatusAfterCancelCmOnSubscriptionTest.class,
	US12001ChechUsesPerCouponAfterSubscriptionCancelCmTest.class,
	//
	US12001KoboSubscriptionUpgradeTest.class,
	US12001KoboSubscriptionUpgradeOrderEmailTest.class,
	US12001ChechUsesPerCouponAfterSubscriptionUpgradeTest.class,
	//
	US12001ApplyCreditMemoOnKoboUpgradeOrderTest.class,
	US12001ChechUsesPerCouponAfterSubscriptionUpgardeCMTest.class,
	US12001CancelCreditMemoForKoboUpgradeTest.class,
	US12001ChechUsesPerCouponAfterSubscriptionUpgradeCancelCMTest.class,
	
})
@RunWith(Suite.class)
public class US12001Suite {

}
