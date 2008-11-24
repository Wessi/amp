package org.dgfoundation.amp.test;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.dgfoundation.amp.test.example.MockStrutTest;
import org.dgfoundation.amp.test.example.MockTagLibTest;
import org.dgfoundation.amp.test.example.SimpleUnitTest;
import org.dgfoundation.amp.test.example.TestTest;

public class AllTest {

	public static Test suite() {
		TestSuite suite = new TestSuite();
		//suite.addTestSuite(MockTagLibTest.class);
		//suite.addTestSuite(MockStrutTest.class);
		//suite.addTestSuite(SimpleUnitTest.class);
		//suite.addTestSuite(UserRegistrationTest.class);
		suite.addTestSuite(TestTest.class);
		return suite;
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

}