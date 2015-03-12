package com.suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;



@SuiteClasses({
	BackendSuite.class,
	FrontEndSuite.class,
})
@RunWith(Suite.class)
public class FullSuite {

}
