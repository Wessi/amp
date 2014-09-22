package org.dgfoundation.amp.importers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.postgis.entity.AmpLocator;
import org.hibernate.HibernateException;

public class GazeteerCSVImporter extends CSVImporter {

	private static Logger logger = Logger.getLogger(GazeteerCSVImporter.class);

	public GazeteerCSVImporter(String importFileName, String[] columnNames, Properties extraProperties) {
		super(importFileName, columnNames, extraProperties);
	}

	@Override
	protected Class[] getImportedTypes() {
		return new Class[] { AmpLocator.class };
	}

	@Override
	protected void saveToDB(Map<String, String> o) throws HibernateException {
		logger.info("importing: " + o);
		AmpLocator locator = new AmpLocator();
		if (o.get("geonameId") != null) {
			locator.setGeonameId(Long.valueOf(o.get("geonameId")));

		}
		locator.setName(o.get("name"));
		locator.setAsciiName(o.get("ascciName"));
		locator.setAlternateNames(o.get("alternateNames"));
		if (o.get("featureClass") != null) {
			locator.setFeatureClass(o.get("featureClass").charAt(0));
		}
		locator.setFeatureCode(o.get("featureCode"));
		locator.setCountryIso(o.get("countryIso"));
		locator.setCc2(o.get("cc2"));
		locator.setAdmin1(o.get("adm1"));
		locator.setAdmin2(o.get("adm2"));
		locator.setAdmin3(o.get("adm3"));
		locator.setAdmin4(o.get("adm4"));
		if (o.get("population") != null) {
			locator.setPopulation(Long.valueOf(o.get("population")));
		}
		if (o.get("elevation") != null) {
			locator.setElevation(Integer.valueOf(o.get("elevation")));
		}
		if (o.get("gtopo30") != null) {
			locator.setGtopo30(Integer.valueOf(o.get("gtopo30")));
		}
		locator.setTimezone(o.get("timezone"));
		locator.setLatitude(o.get("latitude"));
		locator.setLongitude(o.get("longitude"));
		
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-mm-dd");
		try {
			locator.setLastModified(formater.parse(o.get("lastModified")));
		} catch (ParseException e) {
			logger.warn("Error parsing date: " + o.get("lastModifed") + " when importing AmpLocator");
		}
		session.save(locator);

	}

}
