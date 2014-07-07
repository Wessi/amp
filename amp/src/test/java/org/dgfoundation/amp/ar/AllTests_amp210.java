package org.dgfoundation.amp.ar;

//import org.dgfoundation.amp.testutils.LiberiaFiller;
import org.dgfoundation.amp.ar.amp210.ETLTests;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * entry point for AMP 2.8 tests. Initializes standalone AMP as part of the discovery process.
 * standalone AMP configuration is taken off standAloneAmpHibernate.cfg.xml
 * @author Dolghier Constantin
 *
 */
public class AllTests_amp210
{

	public static Test suite() {
		
		setUp();
		//new MoldovaTranslationsSplit().doMoldovaTranslations();
		
//		LiberiaFiller.fillInDatabase();
		TestSuite suite = new TestSuite(AllTests_amp210.class.getName());
		suite.addTest(ETLTests.suite());
		//$JUnit-BEGIN$

		//$JUnit-END$
		return suite;
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	/**
	 * disabled for now
	 */
	public static void setUp()
	{
/*		try
		{ 
			HibernateClassLoader.HIBERNATE_CFG_XML = "/standAloneAmpHibernate.cfg.xml";
			HibernateClassLoader.HIBERNATE_CFG_OVERRIDE_DATABASE = "jdbc:postgresql://localhost/amp_tests_210";
			//HibernateClassLoader.HIBERNATE_CFG_OVERRIDE_DATABASE = "jdbc:postgresql://localhost/amp_moldova_27";
			//HibernateClassLoader.HIBERNATE_CFG_OVERRIDE_DATABASE = "jdbc:postgresql://localhost:15434/amp_moldova";
    	
			ResourceStreamHandlerFactory.installIfNeeded();

			DigiConfigManager.initialize("./repository");
			PersistenceManager.initialize(false, null);
//			Configuration cfg = HibernateClassLoader.getConfiguration();
			//System.out.println("AMP started up!");
			TLSUtils.getThreadLocalInstance().setForcedLangCode(SiteUtils.getDefaultSite().getDefaultLanguage().getCode());
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
		
		// PersistenceManager.cleanup();*/
	}
	
}

