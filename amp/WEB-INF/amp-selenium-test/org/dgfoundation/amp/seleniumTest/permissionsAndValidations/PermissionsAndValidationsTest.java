package org.dgfoundation.amp.seleniumTest.permissionsAndValidations;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.seleniumTest.SeleniumTestUtil;
import org.dgfoundation.amp.seleniumTest.activityForm.ActivityFormTest;
import org.dgfoundation.amp.seleniumTest.reports.ReportTest;
import org.dgfoundation.amp.seleniumTest.reports.TabTest;

import com.thoughtworks.selenium.SeleneseTestCase;
import com.thoughtworks.selenium.Selenium;

public class PermissionsAndValidationsTest extends SeleneseTestCase {
	
	private static Logger logger = Logger.getLogger(PermissionsAndValidationsTest.class);
	
	public void setUp() throws Exception {
		setUp("http://localhost:8080/", "*firefox");
//		setUp("http://senegal.staging.ampdev.net/", "*chrome");
	}
	public static void testPermissionsAndValidations(Selenium selenium) throws Exception {
		
		String testTime =  String.valueOf(System.currentTimeMillis());
		String activityName ="Activity of testing setup " + testTime;
		
		//Login as TM WS Team (create an activity)
		selenium.open("/");
		selenium.type("j_username", "uattm@amp.org");
		selenium.type("j_password", "abc");
		selenium.click("submitButton");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=UAT Team Workspace");
		selenium.waitForPageToLoad("30000");
		addActivity(selenium, activityName);
		selenium.click("//a[contains(@href, \"/aim/j_acegi_logout\")]");
		selenium.waitForPageToLoad("30000");

		//Login as TL WS Team - Computed (create a tab and check that the activity is NOT present)
		logger.info("Permissions and Validation UAT Step 2'");
		selenium.type("j_username", "uattl@amp.org");
		selenium.type("j_password", "abc");
		selenium.click("submitButton");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=UAT Team Workspace - Computed");
		selenium.waitForPageToLoad("30000");
		TabTest.addBasicTab(selenium, "Test Tab TL " + testTime, testTime);
		assertTrue(!selenium.isTextPresent(activityName));
		selenium.click("//a[contains(@href, \"/aim/j_acegi_logout\")]");
		selenium.waitForPageToLoad("30000");
		
		//Login as TL WS Team (create a tab and validate the activity)
		logger.info("Permissions and Validation UAT Step 3'");
		selenium.type("j_username", "uattl@amp.org");
		selenium.type("j_password", "abc");
		selenium.click("submitButton");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=UAT Team Workspace");
		selenium.waitForPageToLoad("30000");
		TabTest.addBasicTab(selenium, "Test Tab TL " + testTime, testTime);
		selenium.click("//table[@id='reportTable']/tbody/tr[2]/td[1]/a/font/div");
		selenium.waitForPageToLoad("30000");
		selenium.click("//a[@onclick=\"javascript:fnEditProject(document.getElementById('tempActivity').value); return false;\"]");
		selenium.waitForPageToLoad("30000");
		selenium.click("//input[@onclick='saveClicked()']");
		selenium.waitForPageToLoad("50000");
		selenium.click("//a[contains(@href, \"/aim/j_acegi_logout\")]");
		selenium.waitForPageToLoad("30000");
		
		//Login as TL WS Team - Computed (use the tab created and check the activity)
		selenium.type("j_username", "uattl@amp.org");
		selenium.type("j_password", "abc");
		selenium.click("submitButton");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=UAT Team Workspace - Computed");
		selenium.waitForPageToLoad("30000");
		selenium.click("//a[@id='Tab-Test Tab TL " + testTime + "']/div");
		Thread.sleep(12000);
		selenium.click("//a[@id='Tab-Test Tab TL " + testTime + "']/div");
		Thread.sleep(12000);
		assertTrue(selenium.isTextPresent(activityName));
		selenium.click("//table[@id='reportTable']/tbody/tr[2]/td[1]/a/font/div");
		selenium.waitForPageToLoad("30000");
//		selenium.click("//a[@onclick=\"javascript:fnEditProject(document.getElementById('tempActivity').value); return false;\"]");
//		selenium.waitForPageToLoad("30000");
		selenium.click("//a[contains(@href, \"/aim/j_acegi_logout\")]");
		selenium.waitForPageToLoad("30000");
		
		//Login as TL WS Management (create a tab and check the activity)
		// aca hay un issue cuando creas un tab en un managment workspace filtra por draft=true en settings
		/*
		logger.info("Permissions and Validation UAT Step 4'");
		selenium.type("j_username", "uattl@amp.org");
		selenium.type("j_password", "abc");
		selenium.click("submitButton");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=UAT Management Workspace");
		selenium.waitForPageToLoad("30000");
		addTab("Test Tab TL " + testTime, testTime);
		selenium.click("//table[@id='reportTable']/tbody/tr[2]/td[1]/a/font/div");
		selenium.waitForPageToLoad("30000");
		selenium.click("//a[@onclick=\"javascript:fnEditProject(document.getElementById('tempActivity').value); return false;\"]");
		selenium.waitForPageToLoad("30000");
		selenium.click("//input[@onclick='saveClicked()']");
		selenium.waitForPageToLoad("50000");
		selenium.click("//a[contains(@href, \"/aim/j_acegi_logout\")]");
		selenium.waitForPageToLoad("30000");
		*/
		
		//Login as Admin and change the permissions on feature manager 
		logger.info("Permissions and Validation UAT Step 5'");
		selenium.type("j_username", "admin@amp.org");
		selenium.type("j_password", "admin");
		selenium.click("submitButton");
		selenium.waitForPageToLoad("30000");
		selenium.click("//a[contains(@href, \"/aim/visibilityManager.do\")]");
		selenium.waitForPageToLoad("30000");
		boolean done = false;
		int cnt = 0;
		while (!done) {
			if (selenium.isElementPresent("//a[contains(@href, \"/aim/visibilityManager.do~templateId="+cnt+"~action=edit\")]") && !selenium.isElementPresent("//a[contains(@href, \"/aim/visibilityManager.do~templateId="+cnt+"~action=delete\")]")) {
				selenium.click("//a[contains(@href, \"/aim/visibilityManager.do~templateId="+cnt+"~action=edit\")]");
				selenium.waitForPageToLoad("30000");
				done = true;
			}
			cnt++;
		}
		selenium.click("//a[@onclick= \"openFieldPermissionsPopup(89)\"]"); // Project Title
		//selenium.waitForPopUp(selenium.getAllWindowTitles()[1], "50000");
		Thread.sleep(5000);
        selenium.selectWindow(selenium.getAllWindowTitles()[1]); 
        selenium.uncheck("faRead");
        selenium.uncheck("faEdit");
        selenium.click("//input[@onclick='javascript:savePermissions();']");
        selenium.selectWindow("null"); 
        Thread.sleep(5000);
        selenium.click("//a[@onclick= \"openFieldPermissionsPopup(418)\"]"); // Project Title
		//selenium.waitForPopUp(selenium.getAllWindowTitles()[1], "50000");
		Thread.sleep(5000);
        selenium.selectWindow(selenium.getAllWindowTitles()[1]); 
        selenium.uncheck("faRead");
        selenium.uncheck("faEdit");
        selenium.click("//input[@onclick='javascript:savePermissions();']");
        selenium.selectWindow("null"); 
        Thread.sleep(5000);
        selenium.click("//input[@name='saveTreeVisibility']");
        selenium.waitForPageToLoad("30000");
		selenium.click("//a[contains(@href, \"/aim/j_acegi_logout\")]");
		selenium.waitForPageToLoad("30000");
		
		//Login as TM WS Team (create a tab and check if the Project Title and Status are enabled)
		logger.info("Permissions and Validation UAT Step 6'");
		selenium.type("j_username", "uattm@amp.org");
		selenium.type("j_password", "abc");
		selenium.click("submitButton");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=UAT Team Workspace");
		selenium.waitForPageToLoad("30000");
		TabTest.addBasicTab(selenium, "Test Tab TM " + testTime, testTime);
		selenium.click("//table[@id='reportTable']/tbody/tr[2]/td[1]/a/font/div");
		selenium.waitForPageToLoad("30000");
		selenium.click("//a[@onclick=\"javascript:fnEditProject(document.getElementById('tempActivity').value); return false;\"]");
		selenium.waitForPageToLoad("30000");
		try {
			String dis = selenium.getAttribute("identification.title@disabled");
			assertTrue(!dis.equalsIgnoreCase("disabled"));			
		} catch (Exception e) {
			logger.info("Attribute 'Disabled' is not available for 'identification.title'");
		}
		try {
			String dis = selenium.getAttribute("planning.statusId@disabled");
			assertTrue(!dis.equalsIgnoreCase("disabled"));			
		} catch (Exception e) {
			logger.info("Attribute 'Disabled' is not available for 'planning.statusId'");
		}
		selenium.click("//a[contains(@href, \"/aim/j_acegi_logout\")]");
		selenium.waitForPageToLoad("30000");
        
		//Login as TL WS Team (use the tab created and check if the Project Title and Status are enabled)
		logger.info("Permissions and Validation UAT Step 7'");
		selenium.type("j_username", "uattl@amp.org");
		selenium.type("j_password", "abc");
		selenium.click("submitButton");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=UAT Team Workspace");
		selenium.waitForPageToLoad("30000");
		selenium.click("//a[contains(@href, \"showDesktop.do\")]");
		selenium.waitForPageToLoad("30000");
		selenium.click("//a[@id='Tab-Test Tab TL " + testTime + "']/div");
		Thread.sleep(12000);
		selenium.click("//a[@id='Tab-Test Tab TL " + testTime + "']/div");
		Thread.sleep(12000);
		selenium.click("//table[@id='reportTable']/tbody/tr[2]/td[1]/a/font/div");
		selenium.waitForPageToLoad("30000");
		selenium.click("//a[@onclick=\"javascript:fnEditProject(document.getElementById('tempActivity').value); return false;\"]");
		selenium.waitForPageToLoad("30000");
		try {
			String dis = selenium.getAttribute("identification.title@disabled");
			assertTrue(!dis.equalsIgnoreCase("disabled"));			
		} catch (Exception e) {
			logger.info("Attribute 'Disabled' is not available for 'identification.title'");
		}
		try {
			String dis = selenium.getAttribute("planning.statusId@disabled");
			assertTrue(!dis.equalsIgnoreCase("disabled"));			
		} catch (Exception e) {
			logger.info("Attribute 'Disabled' is not available for 'planning.statusId'");
		}
		selenium.click("//a[contains(@href, \"/aim/j_acegi_logout\")]");
		selenium.waitForPageToLoad("30000");
        
		//Login as TMC WS Team (create a tab and check if the Project Title and Status are disabled)
		logger.info("Permissions and Validation UAT Step 8'");
		selenium.type("j_username", "uattmc@amp.org");
		selenium.type("j_password", "abc");
		selenium.click("submitButton");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=UAT Team Workspace");
		selenium.waitForPageToLoad("30000");
		TabTest.addBasicTab(selenium, "Test Tab TMC " + testTime, testTime);
		selenium.click("//table[@id='reportTable']/tbody/tr[2]/td[1]/a/font/div");
		selenium.waitForPageToLoad("30000");
		selenium.click("//a[@onclick=\"javascript:fnEditProject(document.getElementById('tempActivity').value); return false;\"]");
		selenium.waitForPageToLoad("30000");
		assertTrue(selenium.getAttribute("identification.title@disabled").equalsIgnoreCase("disabled"));
		assertTrue(selenium.getAttribute("planning.statusId@disabled").equalsIgnoreCase("disabled"));
		selenium.click("//a[contains(@href, \"/aim/j_acegi_logout\")]");
		selenium.waitForPageToLoad("30000");
      
		//Login as TL WS Team (change the application settings)
		logger.info("Permissions and Validation UAT Step 9'");
		selenium.type("j_username", "uattl@amp.org");
		selenium.type("j_password", "abc");
		selenium.click("submitButton");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=UAT Team Workspace");
		selenium.waitForPageToLoad("30000");
		selenium.click("//a[@onclick=\"return teamWorkspaceSetup('-1');\"]");
		selenium.waitForPageToLoad("30000");
		selenium.click("//a[contains(@href, \"/aim/defaultSettings.do\")]");
		selenium.waitForPageToLoad("30000");
		selenium.select("validation", "index=0");
		selenium.click("//input[@onclick='validade();']");
		selenium.waitForPageToLoad("30000");
		selenium.click("//a[contains(@href, \"/aim/j_acegi_logout\")]");
		selenium.waitForPageToLoad("30000");
      
		//Login as TM WS Team (use the tab created, add an activity and check if the title is green and with an *)
		String testTime2 =  String.valueOf(System.currentTimeMillis());
		String activityName2 ="Activity of testing setup 2 " + testTime2;
		selenium.type("j_username", "uattm@amp.org");
		selenium.type("j_password", "abc");
		selenium.click("submitButton");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=UAT Team Workspace");
		selenium.waitForPageToLoad("30000");
		addActivity(selenium, activityName2);
		TabTest.addBasicTab(selenium, "Test Tab TL 2 " + testTime2, testTime2);
		selenium.click("//a[contains(@href, \"showDesktop.do\")]");
		selenium.waitForPageToLoad("30000");
		selenium.click("//a[@id='Tab-Test Tab TL 2 " + testTime2 + "']/div");
		Thread.sleep(12000);
		selenium.click("//a[@id='Tab-Test Tab TL 2 " + testTime2 + "']/div");
		Thread.sleep(12000);
		assertTrue(selenium.getAttribute("//table[@id='reportTable']/tbody/tr[2]/td[1]/a/font@color").equalsIgnoreCase("GREEN"));
		assertTrue(selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[1]/a/font/div").contains("*"));
		selenium.click("//a[contains(@href, \"/aim/j_acegi_logout\")]");
		selenium.waitForPageToLoad("30000");
		
		//Login as TL WS Team (create a tab and validate the activity)
		logger.info("Permissions and Validation UAT Step 10'");
		selenium.type("j_username", "uattl@amp.org");
		selenium.type("j_password", "abc");
		selenium.click("submitButton");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=UAT Team Workspace");
		selenium.waitForPageToLoad("30000");
		TabTest.addBasicTab(selenium, "Test Tab TL 2 " + testTime2, testTime2);
		selenium.click("//table[@id='reportTable']/tbody/tr[2]/td[1]/a/font/div");
		selenium.waitForPageToLoad("30000");
		selenium.click("//a[@onclick=\"javascript:fnEditProject(document.getElementById('tempActivity').value); return false;\"]");
		selenium.waitForPageToLoad("30000");
		selenium.click("//input[@onclick='saveClicked()']");
		selenium.waitForPageToLoad("50000");
		selenium.click("//a[contains(@href, \"showDesktop.do\")]");
		selenium.waitForPageToLoad("30000");
		selenium.click("//a[@id='Tab-Test Tab TL 2 " + testTime2 + "']/div");
		Thread.sleep(12000);
		selenium.click("//a[@id='Tab-Test Tab TL 2 " + testTime2 + "']/div");
		Thread.sleep(12000);
		try {
			assertTrue(selenium.getAttribute("//table[@id='reportTable']/tbody/tr[2]/td[1]/a/font@color").equalsIgnoreCase(""));
		} catch (Exception e) {
			logger.info("Attribute 'color' is not available");
		}
		assertTrue(!selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[1]/a/font/div").contains("*"));
		selenium.click("//a[contains(@href, \"/aim/j_acegi_logout\")]");
		selenium.waitForPageToLoad("30000");

		//Login as TL WS Team (change the application settings)
		logger.info("Permissions and Validation UAT Step 12'");
		selenium.type("j_username", "uattl@amp.org");
		selenium.type("j_password", "abc");
		selenium.click("submitButton");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=UAT Team Workspace");
		selenium.waitForPageToLoad("30000");
		selenium.click("//a[@onclick=\"return teamWorkspaceSetup('-1');\"]");
		selenium.waitForPageToLoad("30000");
		selenium.click("//a[contains(@href, \"/aim/defaultSettings.do\")]");
		selenium.waitForPageToLoad("30000");
		selenium.select("validation", "index=1");
		selenium.click("//input[@onclick='validade();']");
		selenium.waitForPageToLoad("30000");
		selenium.click("//a[contains(@href, \"/aim/j_acegi_logout\")]");
		selenium.waitForPageToLoad("30000");
      
		//Login as TM WS Team (use the tab created and check if the Project Title and Status are enabled)
		selenium.type("j_username", "uattm@amp.org");
		selenium.type("j_password", "abc");
		selenium.click("submitButton");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=UAT Team Workspace");
		selenium.waitForPageToLoad("30000");
		selenium.click("//a[contains(@href, \"showDesktop.do\")]");
		selenium.waitForPageToLoad("30000");
		selenium.click("//a[@id='Tab-Test Tab TL 2 " + testTime2 + "']/div");
		Thread.sleep(12000);
		selenium.click("//a[@id='Tab-Test Tab TL 2 " + testTime2 + "']/div");
		Thread.sleep(12000);
		selenium.click("//table[@id='reportTable']/tbody/tr[2]/td[1]/a/font/div");
		selenium.waitForPageToLoad("30000");
		selenium.click("//a[@onclick=\"javascript:fnEditProject(document.getElementById('tempActivity').value); return false;\"]");
		selenium.waitForPageToLoad("30000");
		selenium.type("identification.title", activityName2 + " mod");
		selenium.click("//input[@onclick='saveClicked()']");
		selenium.waitForPageToLoad("50000");
		selenium.click("//a[contains(@href, \"showDesktop.do\")]");
		selenium.waitForPageToLoad("30000");
		selenium.click("//a[@id='Tab-Test Tab TL 2 " + testTime2 + "']/div");
		Thread.sleep(12000);
		selenium.click("//a[@id='Tab-Test Tab TL 2 " + testTime2 + "']/div");
		Thread.sleep(12000);
		assertTrue(selenium.getAttribute("//table[@id='reportTable']/tbody/tr[2]/td[1]/a/font@color").equalsIgnoreCase("GREEN"));
		assertTrue(!selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[1]/a/font/div").contains("*"));
		selenium.click("//a[contains(@href, \"/aim/j_acegi_logout\")]");
		selenium.waitForPageToLoad("30000");
		
		//Login as TL WS Team (create a tab and validate the activity)
		logger.info("Permissions and Validation UAT Step 13 and 14'");
		selenium.type("j_username", "uattl@amp.org");
		selenium.type("j_password", "abc");
		selenium.click("submitButton");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=UAT Team Workspace");
		selenium.waitForPageToLoad("30000");
		selenium.click("//a[contains(@href, \"showDesktop.do\")]");
		selenium.waitForPageToLoad("30000");
		selenium.click("//a[@id='Tab-Test Tab TL 2 " + testTime2 + "']/div");
		Thread.sleep(12000);
		selenium.click("//a[@id='Tab-Test Tab TL 2 " + testTime2 + "']/div");
		Thread.sleep(12000);
		selenium.click("//table[@id='reportTable']/tbody/tr[2]/td[1]/a/font/div");
		selenium.waitForPageToLoad("30000");
		selenium.click("//a[@onclick=\"javascript:fnEditProject(document.getElementById('tempActivity').value); return false;\"]");
		selenium.waitForPageToLoad("30000");
		selenium.click("//input[@onclick='saveClicked()']");
		selenium.waitForPageToLoad("50000");
		selenium.click("//a[contains(@href, \"showDesktop.do\")]");
		selenium.waitForPageToLoad("30000");
		selenium.click("//a[@id='Tab-Test Tab TL 2 " + testTime2 + "']/div");
		Thread.sleep(12000);
		selenium.click("//a[@id='Tab-Test Tab TL 2 " + testTime2 + "']/div");
		Thread.sleep(12000);
		selenium.click("//table[@id='reportTable']/tbody/tr[2]/td[1]/a/font/div");
		selenium.waitForPageToLoad("30000");
		assertTrue(selenium.isElementPresent("//a[contains(@href, \"mailto:uattm@amp.org\")]")); //do this to validate the creator
		selenium.click("//a[@onclick=\"javascript:fnEditProject(document.getElementById('tempActivity').value); return false;\"]");
		selenium.waitForPageToLoad("30000");
		selenium.click("//input[@onclick='saveAsDraftClicked()']");
		selenium.waitForPageToLoad("50000");
		selenium.click("//a[contains(@href, \"showDesktop.do\")]");
		selenium.waitForPageToLoad("30000");
		selenium.click("//a[@id='Tab-Test Tab TL 2 " + testTime2 + "']/div");
		Thread.sleep(12000);
		selenium.click("//a[@id='Tab-Test Tab TL 2 " + testTime2 + "']/div");
		Thread.sleep(12000);
		assertTrue(selenium.getAttribute("//table[@id='reportTable']/tbody/tr[2]/td[1]/a/font@color").equalsIgnoreCase("RED"));
		assertTrue(!selenium.getText("//table[@id='reportTable']/tbody/tr[2]/td[1]/a/font/div").contains("*"));
		selenium.click("//table[@id='reportTable']/tbody/tr[2]/td[1]/a/font/div");
		selenium.waitForPageToLoad("30000");
		assertTrue(selenium.isElementPresent("//a[contains(@href, \"mailto:uattm@amp.org\")]")); //do this to validate the creator
		selenium.click("//a[@onclick=\"javascript:fnEditProject(document.getElementById('tempActivity').value); return false;\"]");
		selenium.waitForPageToLoad("30000");
		selenium.click("//input[@onclick='saveClicked()']");
		selenium.waitForPageToLoad("50000");
		logger.info("Permissions and Validation UAT Step 15'");
		selenium.click("//a[@onclick=\"return teamWorkspaceSetup('-1');\"]");
		selenium.waitForPageToLoad("30000");
		selenium.click("//a[contains(@href, \"/aim/teamActivityList.do~dest=teamLead~tId=-1~subtab=0\")]");
		selenium.waitForPageToLoad("30000");
		String aId = "";
		done = false;
		cnt = 2;
		while (!done){
			if (selenium.isElementPresent("link=" + activityName2+ " mod")) {
				aId = selenium.getAttribute("link=" + activityName2 + " mod@href");
				aId = aId.substring(aId.indexOf("yId=")+4, aId.indexOf("~page"));
				selenium.check("//input[@name='selActivities' and @value='"+aId+"']");
				selenium.click("//input[@onclick='return confirmDelete()']");
				selenium.getConfirmation();
				selenium.waitForPageToLoad("30000");
				done = true;
			} else {
				try {
					selenium.click("//a[contains(@href, \"javascript:page("+cnt+")\")]");
					selenium.waitForPageToLoad("30000");
					cnt++;
				} catch (Exception e) {
					done = true;
				}
			}
		}
	
		selenium.click("//a[contains(@href, \"showDesktop.do\")]");
		selenium.waitForPageToLoad("30000");
		selenium.click("//a[@id='Tab-Test Tab TL 2 " + testTime2 + "']/div");
		Thread.sleep(12000);
		assertTrue(!selenium.isTextPresent(activityName2));
		logger.info("Permissions and Validation UAT Step 16'");
		selenium.click("//a[@onclick=\"return teamWorkspaceSetup('-1');\"]");
		selenium.waitForPageToLoad("30000");
		selenium.click("//a[contains(@href, \"/aim/teamActivityList.do~dest=teamLead~tId=-1~subtab=0\")]");
		selenium.waitForPageToLoad("30000");
		selenium.click("//a[contains(@href, \"/aim/updateTeamActivity.do~dest=teamLead~tId=-1~subtab=0\")]");
		selenium.waitForPageToLoad("30000");
		done = false;
		cnt = 2;
		while (!done){
			if (selenium.isElementPresent("//input[@name='selActivities' and @value='"+aId+"']")) {
				selenium.check("//input[@name='selActivities' and @value='"+aId+"']");
				selenium.select("memberId", "UATtmc UATtmc");
				selenium.click("//input[@onclick='return checkSelActivities()']");
				selenium.waitForPageToLoad("30000");
				done = true;
			} else {
				try {
					selenium.click("//a[contains(@href, \"/aim/updateTeamActivity.do~page="+cnt+"\")]");
					selenium.waitForPageToLoad("30000");
					cnt++;
				} catch (Exception e) {
					done = true;
				}
			}
		}
		selenium.click("//a[contains(@href, \"showDesktop.do\")]");
		selenium.waitForPageToLoad("30000");
		selenium.click("//a[@id='Tab-Test Tab TL 2 " + testTime2 + "']/div");
		Thread.sleep(12000);
		selenium.click("//a[@id='Tab-Test Tab TL 2 " + testTime2 + "']/div");
		Thread.sleep(12000);
		selenium.click("//table[@id='reportTable']/tbody/tr[2]/td[1]/a/font/div");
		selenium.waitForPageToLoad("30000");
		assertTrue(selenium.isElementPresent("//a[contains(@href, \"mailto:uattmc@amp.org\")]")); //do this to validate the new owner
		selenium.click("//a[contains(@href, \"/aim/j_acegi_logout\")]");
		selenium.waitForPageToLoad("30000");
		
		//Login as TMC WS Team (create a tab and check if the Project Title and Status are enabled)
		logger.info("Permissions and Validation UAT Step 17'");
		selenium.type("j_username", "uattmc@amp.org");
		selenium.type("j_password", "abc");
		selenium.click("submitButton");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=UAT Team Workspace");
		selenium.waitForPageToLoad("30000");
		selenium.click("//a[contains(@href, \"showDesktop.do\")]");
		selenium.waitForPageToLoad("30000");
		TabTest.addBasicTab(selenium, "Test Tab TMC 2 " + testTime2, testTime2);
		//selenium.click("//a[@id='Tab-Test Tab TMC " + testTime + "']/div");
		//Thread.sleep(12000);
		selenium.click("//table[@id='reportTable']/tbody/tr[2]/td[1]/a/font/div");
		selenium.waitForPageToLoad("30000");
		selenium.click("//a[@onclick=\"javascript:fnEditProject(document.getElementById('tempActivity').value); return false;\"]");
		selenium.waitForPageToLoad("30000");
		try {
			String dis = selenium.getAttribute("identification.title@disabled");
			assertTrue(!dis.equalsIgnoreCase("disabled"));			
		} catch (Exception e) {
			logger.info("Attribute 'Disabled' is not available for 'identification.title'");
		}
		try {
			String dis = selenium.getAttribute("planning.statusId@disabled");
			assertTrue(!dis.equalsIgnoreCase("disabled"));			
		} catch (Exception e) {
			logger.info("Attribute 'Disabled' is not available for 'planning.statusId'");
		}
		selenium.click("//a[contains(@href, \"/aim/j_acegi_logout\")]");
		selenium.waitForPageToLoad("30000");
		
		selenium.type("j_username", "uattl@amp.org");
		selenium.type("j_password", "abc");
		selenium.click("submitButton");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=UAT Team Workspace");
		selenium.waitForPageToLoad("30000");
		selenium.click("//a[contains(@href, \"/viewTeamReports.do?tabs=true\")]");
		selenium.waitForPageToLoad("50000");
		TabTest.deleteAllTabs(selenium);
		selenium.click("//a[contains(@href, \"/aim/j_acegi_logout\")]");
		selenium.waitForPageToLoad("30000");
		
		selenium.type("j_username", "uattm@amp.org");
		selenium.type("j_password", "abc");
		selenium.click("submitButton");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=UAT Team Workspace");
		selenium.waitForPageToLoad("30000");
		selenium.click("//a[contains(@href, \"/viewTeamReports.do?tabs=true\")]");
		selenium.waitForPageToLoad("50000");
		TabTest.deleteAllTabs(selenium);
		selenium.click("//a[contains(@href, \"/aim/j_acegi_logout\")]");
		selenium.waitForPageToLoad("30000");
		
		selenium.type("j_username", "uattmc@amp.org");
		selenium.type("j_password", "abc");
		selenium.click("submitButton");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=UAT Team Workspace");
		selenium.waitForPageToLoad("30000");
		selenium.click("//a[contains(@href, \"/viewTeamReports.do?tabs=true\")]");
		selenium.waitForPageToLoad("50000");
		TabTest.deleteAllTabs(selenium);
		selenium.click("//a[contains(@href, \"/aim/j_acegi_logout\")]");
		selenium.waitForPageToLoad("30000");
		
		selenium.type("j_username", "uattl@amp.org");
		selenium.type("j_password", "abc");
		selenium.click("submitButton");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=UAT Team Workspace - Computed");
		selenium.waitForPageToLoad("30000");
		selenium.click("//a[contains(@href, \"/viewTeamReports.do?tabs=true\")]");
		selenium.waitForPageToLoad("50000");
		TabTest.deleteAllTabs(selenium);
		selenium.click("//a[contains(@href, \"/aim/j_acegi_logout\")]");
		selenium.waitForPageToLoad("30000");
		
		selenium.type("j_username", "admin@amp.org");
		selenium.type("j_password", "admin");
		selenium.click("submitButton");
		selenium.waitForPageToLoad("30000");
		if (selenium.isElementPresent("//a[contains(@href, \"/aim/activityManager.do\")]")) {
			selenium.click("//a[contains(@href, \"/aim/activityManager.do\")]");
			selenium.waitForPageToLoad("30000");
			selenium.type("keyword", activityName);
			selenium.click("//input[@onclick=\"return searchActivity()\"]");
			selenium.waitForPageToLoad("30000");
			selenium.click("//a[@onclick=\"return deleteIndicator()\"]");
			selenium.getConfirmation();
			selenium.waitForPageToLoad("30000");
			selenium.type("keyword", activityName2);
			selenium.click("//input[@onclick=\"return searchActivity()\"]");
			selenium.waitForPageToLoad("30000");
			selenium.click("//a[@onclick=\"return deleteIndicator()\"]");
			selenium.getConfirmation();
			selenium.waitForPageToLoad("30000");
		} else {
			logger.info("Activity Manager is not available");
		}
		selenium.click("//a[contains(@href, \"/aim/j_acegi_logout\")]");
		selenium.waitForPageToLoad("30000");
		
		logger.info("Permissions and Validations Test Finished Successfully");
		
	}
	
	
	
	/**
	 * 
	 * @param activityName
	 */
	private static void addActivity (Selenium selenium, String activityName){
		String version = selenium.getText("//div[@class=\"footerText\"]");
		version = version.substring(version.indexOf("1.1"), version.indexOf("1.1")+4);		
		boolean addAvailable = false;
		try {
			selenium.click("//a[contains(@href, \"javascript:addActivity()\")]");
			selenium.waitForPageToLoad("120000");
			addAvailable = true;
		} catch (Exception e) {
			logger.error("Option \"Add Activity\" is not available.");			
		}
		if (addAvailable) {
			selenium.type("identification.title", activityName);
			try {
				selenium.select("planning.statusId", "index=1");
			} catch (Exception e) {
				logger.info("Field \"Status\" is not available.");
			}
			
			selenium.click("//a[@href='javascript:gotoStep(2)']");
			selenium.waitForPageToLoad("50000");
			
			//Add Primary Sector
			try {
				selenium.click("//input[@name='submitButton' and @onclick='addSectors(false,1);']");
				SeleniumTestUtil.waitForElement(selenium,"sector", 90);
				try {
					selenium.select("sector", "index=1");
					Thread.sleep(5000);
					selenium.click("addButton");
					selenium.waitForPageToLoad("50000");					
				} catch (Exception e) {
					logger.info("Sectors no found for Primary Sector");
				}
			} catch (Exception e) {
				logger.info("Option \"Add Primary Sector\" is not available.");
			}
			try {
				selenium.type("activitySectors[0].sectorPercentage", "100");
			} catch (Exception e) {}
			//Add Secondary Sector
			try {
				selenium.click("//input[@name='submitButton' and @onclick='addSectors(false,2);']");
				SeleniumTestUtil.waitForElement(selenium,"sector", 90);
				try {
					selenium.select("sector", "index=1");
					Thread.sleep(5000);
					selenium.click("addButton");
					selenium.waitForPageToLoad("50000");					
				} catch (Exception e) {
					logger.info("Sectors no found for Secondary Sector");
				}
			} catch (Exception e) {
				logger.info("Option \"Add Secondary Sector\" is not available.");
			}
			try {
				selenium.type("activitySectors[1].sectorPercentage", "100");
			} catch (Exception e) {}
			//Add Funding
			boolean fundingAvailable = false;
			try {
				selenium.click("//a[@href='javascript:gotoStep(3)']");
				selenium.waitForPageToLoad("50000");
				fundingAvailable = true;
			} catch (Exception e) {
				logger.info("Step \"Funding\" is not available.");
			}
			if (fundingAvailable) {
				try {
					if (version.equals(SeleniumTestUtil.VERSION_BRANCH)) {
						selenium.click("//input[@onclick=\"window.open('/aim/selectOrganizationComponent.do~edit=true~reset=true~PARAM_RESET_FORM=true~PARAM_REFRESH_PARENT=true~PARAM_CALLBACKFUNCTION_NAME=doNothing();~PARAM_COLLECTION_NAME=fundingOrganizations~PARAM_NAME_DELEGATE_CLASS=org.digijava.module.aim.uicomponents.ToFundingOrganizationDelegate~','addOrganisationWindows','height=400,width=600,scrollbars=yes,resizable=yes')\"]");
						//selenium.waitForPopUp(selenium.getAllWindowTitles()[1], "50000");
						Thread.sleep(6000);
				        selenium.selectWindow(selenium.getAllWindowTitles()[1]); 
				       
						SeleniumTestUtil.waitForElement(selenium,"//input[@onclick='return searchOrganization()']", 90);
						selenium.type("keyword", "World Bank");
						selenium.click("//input[@onclick='return searchOrganization()']");
						SeleniumTestUtil.waitForElement(selenium,"selOrganisations", 90);
						selenium.click("selOrganisations"); 
						selenium.click("//input[@onclick='return selectOrganization()']");
						selenium.selectWindow("null");
						selenium.waitForPageToLoad("50000");
						
					} else {
						selenium.click("//input[@onclick=\"javascript:selectOrg('/aim/selectOrganizationComponent.do~edit=true~reset=true~PARAM_RESET_FORM=true~PARAM_REFRESH_PARENT=true~PARAM_CALLBACKFUNCTION_NAME=doNothing();~PARAM_COLLECTION_NAME=fundingOrganizations~PARAM_NAME_DELEGATE_CLASS=org.digijava.module.aim.uicomponents.ToFundingOrganizationDelegate~','addOrganisationWindows','height=400,width=600,scrollbars=yes,resizable=yes')\"]");
						SeleniumTestUtil.waitForElement(selenium,"//input[@onclick='return searchOrganization()']", 90);
						selenium.type("keyword", "World Bank");
						selenium.click("//input[@onclick='return searchOrganization()']");
						SeleniumTestUtil.waitForElement(selenium,"selOrganisations", 90);
						selenium.click("selOrganisations");
						selenium.click("//input[@onclick='return selectOrganization()']");
						selenium.waitForPageToLoad("50000");
					}
					
				} catch (Exception e) {
					logger.info("Option \"Add Organizations\" is not available.");
					logger.error(e);
				}	
			}
			selenium.click("//input[@onclick='saveClicked()']");
			selenium.waitForPageToLoad("50000");
			if (selenium.isElementPresent("//input[@onclick='saveClicked()']")) {
				logger.error("Save Activity Fail");				
			}
			if (selenium.isElementPresent("//input[@onclick='overwrite()']")) {
				selenium.click("//input[@onclick='overwrite()']");			
			}
		}
	}
	
}
