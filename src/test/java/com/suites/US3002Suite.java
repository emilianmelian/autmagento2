package com.suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.tests.us3002.US3002CartSegmentationWithVatBillingTest;
import com.tests.us3002.US3002UserProfileOrderIdTest;
import com.tests.us3002.US3002ValidateOrderBackOfficeTest;
import com.tests.us3002.US3002ValidateOrderEmailTest;

@SuiteClasses({
	US3002CartSegmentationWithVatBillingTest.class,
	US3002UserProfileOrderIdTest.class,
	US3002ValidateOrderEmailTest.class,
	US3002ValidateOrderBackOfficeTest.class,
})
@RunWith(Suite.class)
public class US3002Suite {

}