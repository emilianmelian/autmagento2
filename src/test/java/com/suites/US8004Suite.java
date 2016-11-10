package com.suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.tests.us8.us8004.US8004ChangeBackCustomersContextTest;
import com.tests.us8.us8004.US8004CustomerBuyWithContactBoosterTest;
import com.tests.us8.us8004.US8004ValidateOrderBackOfficeTest;

@SuiteClasses({
	US8004CustomerBuyWithContactBoosterTest.class,
//	US8004CheckOrderOnCustomerProfileTest.class,	
//	US8004ValidateOrderEmailTest.class,	
	US8004ValidateOrderBackOfficeTest.class,
//	US8004ValidateOrderInStylistsCustomerOrderReportTest.class,
	US8004ChangeBackCustomersContextTest.class,
	
})
@RunWith(Suite.class)
public class US8004Suite {

}