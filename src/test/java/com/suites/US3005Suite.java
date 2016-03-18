package com.suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.tests.us3.us3005.US3005SfmValidVatSmbBillingDeShippingAtTest;
import com.tests.us3.us3005.US3005ValidateOrderBackOfficeTest;

@SuiteClasses({
	US3005SfmValidVatSmbBillingDeShippingAtTest.class,
//	US3005CheckOrderOnStylecoachProfileTest.class,
//	US3005ValidateOrderEmailTest.class,
	US3005ValidateOrderBackOfficeTest.class,
})
@RunWith(Suite.class)
public class US3005Suite {

}
