package com.suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.tests.us1.US001StyleCoachShoppingTest;
import com.tests.us1.US001ValidateOrderBackOfficeTest;
import com.tests.us1.US001ValidateUserProfileOrderTest;

@SuiteClasses({
	US001StyleCoachShoppingTest.class,
	US001ValidateOrderBackOfficeTest.class,
	US001ValidateUserProfileOrderTest.class,
})
@RunWith(Suite.class)
public class US001Suite {

}