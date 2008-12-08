package org.dgfoundation.amp.test.helper;

import java.io.IOException;
import java.util.Properties;

import org.dgfoundation.amp.StandaloneJndiAMPInitializer;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.util.DigiConfigManager;

public class Configuration {
	private static boolean started = false;

	private Configuration() {
		try {
			//Initialize a JNDI reference to the real datasource deployed by jboss-web.xml
			//You need to have a JBoss instance running on localhost! 
			StandaloneJndiAMPInitializer.initAMPUnifiedJndiAlias();
			
			String repository = getProperties().getProperty("repository");
			String path=this.getClass().getResource("/").getPath().replaceAll("/WEB-INF/classes/", repository);
			
			DigiConfigManager.initialize(path);
			PersistenceManager.initialize(true, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void initConfig() {
		if (!started) {
			new Configuration();
			started = true;
		}
	}

	public static Properties getProperties() {
		Properties properties = new Properties();
		try {
			properties.load(Configuration.class.getResourceAsStream("amp-test.properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return properties;
	}
}
