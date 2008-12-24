package org.dgfoundation.amp.test;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.dgfoundation.amp.test.categorymanager.AllCategoryManagerTest;
import org.dgfoundation.amp.test.customfields.AllCustomFieldsTest;
import org.dgfoundation.amp.test.jobs.AllJobsTest;
import org.dgfoundation.amp.test.messages.AllMessagesTest;
import org.dgfoundation.amp.test.orgProfile.AllOrgProfileTest;
import org.dgfoundation.amp.test.parisindicator.AllParisIndicatorsManagerTest;
import org.dgfoundation.amp.test.reports.AllReportTest;
import org.dgfoundation.amp.test.widget.AllWidgetTest;
/**
 * This suite include all test suite modules 
 * @author Sebas
 *
 */
public class AllTest {

	public static Test suite() {
		TestSuite suite = new TestSuite();

		suite.addTest(AllReportTest.suite());
		suite.addTest(AllCategoryManagerTest.suite());
		suite.addTest(AllWidgetTest.suite());
		suite.addTest(AllParisIndicatorsManagerTest.suite());
        suite.addTest(AllOrgProfileTest.suite());
        suite.addTest(AllCustomFieldsTest.suite());
        suite.addTest(AllJobsTest.suite());
        suite.addTest(AllMessagesTest.suite());
		return suite;
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

}