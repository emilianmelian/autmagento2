package com.parallelSuites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.suites.US9001PlaceHost;
import com.suites.US9002PlaceHost;


@SuiteClasses({
	US9001PlaceHost.class,
	US9002PlaceHost.class
})
@RunWith(Suite.class)
public class Run_PlaceHostOderSuite {

}
