package org.dgfoundation.amp.ar;

//import org.dgfoundation.amp.testutils.LiberiaFiller;
import org.dgfoundation.amp.ar.moldovamigration.MoldovaTranslationsSplit;
import org.dgfoundation.amp.esri    .EsriTestCases;
import org.digijava.kernel.persistence.HibernateClassLoader;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.util.DigiConfigManager;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.kernel.util.resource.ResourceStreamHandlerFactory;
import org.hibernate.cfg.Configuration;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * entry point for AMP 2.7 tests. Initializes standalone AMP as part of the discovery process.
 * standalone AMP configuration is taken off standAloneAmpHibernate.cfg.xml
 * @author Dolghier Constantin
 *
 */
public class AllTests_amp27 {

	public static Test suite() {
		
		setUp();
		//new MoldovaTranslationsSplit().doMoldovaTranslations();
		
//		LiberiaFiller.fillInDatabase();
		TestSuite suite = new TestSuite(AllTests_amp27.class.getName());
		//$JUnit-BEGIN$

		suite.addTest(DirectedDisbursementsTests_amp27.suite());
		suite.addTest(MultilingualTests.suite());
//		suite.addTest(MtefTests.suite());
//		suite.addTest(ComputedMeasuresTests.suite());
//		suite.addTest(MiscColumnsTests.suite());
//		suite.addTest(EsriTestCases.suite());
//		suite.addTest(FiltersTests.suite());
		//$JUnit-END$
		return suite;
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static void setUp()
	{
		try
		{ 
			HibernateClassLoader.HIBERNATE_CFG_XML = "/standAloneAmpHibernate.cfg.xml";
			HibernateClassLoader.HIBERNATE_CFG_OVERRIDE_DATABASE = "jdbc:postgresql://localhost/amp_tests_27";
			//HibernateClassLoader.HIBERNATE_CFG_OVERRIDE_DATABASE = "jdbc:postgresql://localhost/amp_moldova_27";
    	
			ResourceStreamHandlerFactory.installIfNeeded();

			DigiConfigManager.initialize("./repository");
			PersistenceManager.initialize(false, null);
			Configuration cfg = HibernateClassLoader.getConfiguration();
			System.out.println("AMP started up!");
			TLSUtils.getThreadLocalInstance().setForcedLangCode(SiteUtils.getDefaultSite().getDefaultLanguage().getCode());
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
		
		// PersistenceManager.cleanup();
	}
	
}

