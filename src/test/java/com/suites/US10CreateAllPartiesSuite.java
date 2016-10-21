package com.suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.tests.uss10.us10001b.US10001bCreatePartyWithStylistHostTest;
import com.tests.uss10.us10002b.US10002bCreatePartyWithCustomerHostTest;
import com.tests.uss10.uss10007.US10007CreatePartyWithStylistHostTest;
import com.tests.uss10.uss10008.US10008CreatePartyWithExistingContactHostTest;
import com.tests.uss11.us11008.US10009CreatePartyWithStylistHostTest;
import com.tests.uss24.US24001CreatePartyWithNewContactPlzValidationTest;
import com.tests.uss25.US25001CreatePartyWithNewContactTest;

@SuiteClasses({
	
	US10001bCreatePartyWithStylistHostTest.class,
	US10002bCreatePartyWithCustomerHostTest.class,
//	US10006CreatePartyWithNewContactHostTest.class,
	US10007CreatePartyWithStylistHostTest.class,
	US10008CreatePartyWithExistingContactHostTest.class,
	US10009CreatePartyWithStylistHostTest.class,
	US24001CreatePartyWithNewContactPlzValidationTest.class,
	US25001CreatePartyWithNewContactTest.class,
	
})
@RunWith(Suite.class)
public class US10CreateAllPartiesSuite {

}
