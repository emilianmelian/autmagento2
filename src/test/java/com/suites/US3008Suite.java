package com.suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.tests.us3.us3008.US3008SfmNoVatNoSmbBillingDeShippingAtTest;
import com.tests.us3.us3008.US3008UserProfileOrderIdTest;
import com.tests.us3.us3008.US3008ValidateOrderBackOfficeTest;
import com.tests.us3.us3008.US3008ValidateOrderEmailTest;

@SuiteClasses({
	US3008SfmNoVatNoSmbBillingDeShippingAtTest.class,
	US3008UserProfileOrderIdTest.class,
	US3008ValidateOrderEmailTest.class,
	US3008ValidateOrderBackOfficeTest.class,
})
@RunWith(Suite.class)
public class US3008Suite {

}
