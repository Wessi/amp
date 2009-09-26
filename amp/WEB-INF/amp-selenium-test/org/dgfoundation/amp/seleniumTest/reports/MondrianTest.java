package org.dgfoundation.amp.seleniumTest.reports;

import org.apache.log4j.Logger;

import com.thoughtworks.selenium.SeleneseTestCase;
import com.thoughtworks.selenium.Selenium;

public class MondrianTest extends SeleneseTestCase{
	
	private static Logger logger = Logger.getLogger(MondrianTest.class);
	
	public void setUp() throws Exception {
//		setUp("http://malawi.ampdev.net/", "*chrome");
		setUp("http://senegal.staging.ampdev.net/", "*chrome");
	}
	public static void testMondrian(Selenium selenium) throws Exception {
		selenium.open("/");
		selenium.type("j_username", "uattl@amp.org");
		selenium.type("j_password", "abc");
		selenium.click("submitButton");
		selenium.waitForPageToLoad("50000");
		
		String version = selenium.getText("//div[@class=\"footerText\"]");
		version = version.substring(version.indexOf("1.1"), version.indexOf("1.1")+4);
		
		selenium.click("link=UAT Team Workspace");
		selenium.waitForPageToLoad("50000");
		if (selenium.isElementPresent("//a[contains(@href, \"/mondrian/mainreports.do\")]")) {
			selenium.click("//a[contains(@href, \"/mondrian/mainreports.do\")]");
			selenium.waitForPageToLoad("30000");
			selenium.click("link=Report by Sectors");
			selenium.waitForPageToLoad("30000");
			selenium.click("toolbar01.cubeNaviButton");
			selenium.waitForPageToLoad("30000");
			selenium.click("//table[@id=\"navi01\"]/tbody/tr[2]/td[1]/div/a");
			selenium.waitForPageToLoad("30000");
			selenium.check("//table[@id='navi01']/tbody/tr[2]/td/div/input[1]");
			selenium.check("//table[@id='navi01']/tbody/tr[3]/td/div/input[1]");
			selenium.check("//table[@id='navi01']/tbody/tr[4]/td/div/input[1]");
			String measure1 = selenium.getText("//table[@id='navi01']/tbody/tr[2]/td/div");
			String measure2 = selenium.getText("//table[@id='navi01']/tbody/tr[3]/td/div");
			String measure3 = selenium.getText("//table[@id='navi01']/tbody/tr[4]/td/div");
			selenium.click("navi01.membernav.ok");
			selenium.waitForPageToLoad("30000");
			selenium.click("//table[@id=\"navi01\"]/tbody/tr[4]/td[1]/div/a");
			selenium.waitForPageToLoad("30000");
			selenium.click("//table[@id='navi01']/tbody/tr[2]/td/div/input[1]");
			selenium.click("//table[@id='navi01']/tbody/tr[2]/td/div/input[3]");
			selenium.waitForPageToLoad("30000");
			selenium.click("//table[@id='navi01']/tbody/tr[3]/td/div/input[3]");
			selenium.waitForPageToLoad("30000");
			int sectors = 0;
			String[] sector = null;
			try {
				selenium.click("//table[@id='navi01']/tbody/tr[4]/td/div/input[1]");
				sector[0] = selenium.getText("//table[@id='navi01']/tbody/tr[4]/td/div");
				sectors++;
			} catch (Exception e) {}
			try {
				selenium.click("//table[@id='navi01']/tbody/tr[5]/td/div/input[1]");
				sector[1] = selenium.getText("//table[@id='navi01']/tbody/tr[5]/td/div");
				sectors++;
			} catch (Exception e) {}
			try {
				selenium.click("//table[@id='navi01']/tbody/tr[6]/td/div/input[1]");
				sector[2] = selenium.getText("//table[@id='navi01']/tbody/tr[6]/td/div");
				sectors++;
			} catch (Exception e) {}
			selenium.click("navi01.membernav.ok");
			selenium.waitForPageToLoad("30000");
			selenium.click("navi01.hiernav.ok");
			selenium.waitForPageToLoad("30000");
			selenium.click("toolbar01.chartPropertiesButton01");
			selenium.waitForPageToLoad("30000");
			selenium.type("chartform01.204", "1990");
			selenium.type("chartform01.205", "1990");
			selenium.click("chartform01.214");
			selenium.waitForPageToLoad("30000");
			selenium.click("toolbar01.chartButton01");
			selenium.waitForPageToLoad("30000");
			assertTrue(selenium.getText("//table[@id='table01']/tbody/tr[2]/th[3]").equals(measure1));
			assertTrue(selenium.getText("//table[@id='table01']/tbody/tr[2]/th[4]").equals(measure2));
			assertTrue(selenium.getText("//table[@id='table01']/tbody/tr[2]/th[5]").equals(measure3));
			for (int i = 0; i < sectors; i++) {
				assertTrue(selenium.getText("//table[@id='table01']/tbody/tr[" +(i+3)+ "]/th[1]").equals(sector[i]));		
			}
			assertTrue(selenium.isElementPresent("//img[@width='1990' and @height='1990']"));
		} else {
			logger.warn("Mondrian reports are not available");
		}
		selenium.click("//a[contains(@href, \"/aim/j_acegi_logout\")]");
		selenium.waitForPageToLoad("30000");
		logger.info("Mondrian Test Finished Successfully");
	}

}
