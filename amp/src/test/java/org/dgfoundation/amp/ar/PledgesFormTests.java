package org.dgfoundation.amp.ar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.struts.mock.MockHttpServletRequest;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.dgfoundation.amp.testutils.AmpTestCase;
import org.dgfoundation.amp.testutils.ReportTestingUtils;
import org.dgfoundation.amp.testutils.AmpRunnable;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.admin.helper.AmpActivityFake;
import org.digijava.module.aim.ar.util.ReportsUtil;
import org.digijava.module.aim.dbentity.AmpComponent;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.ComponentsUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.fundingpledges.dbentity.FundingPledges;
import org.digijava.module.fundingpledges.dbentity.FundingPledgesDetails;
import org.digijava.module.fundingpledges.dbentity.FundingPledgesLocation;
import org.digijava.module.fundingpledges.dbentity.FundingPledgesProgram;
import org.digijava.module.fundingpledges.dbentity.FundingPledgesSector;
import org.digijava.module.fundingpledges.dbentity.PledgesEntityHelper;
import org.digijava.module.message.jobs.CloseExpiredActivitiesJob;
import org.digijava.module.translation.util.MultilingualInputFieldValues;
import org.hibernate.Session;

import java.util.Arrays;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * multilingual editor tests
 * @author Dolghier Constantin
 *
 */
public class PledgesFormTests extends AmpTestCase
{
	
	private PledgesFormTests(String name)
	{
		super(name);		
	}
	
	public static Test suite()
	{
		TestSuite suite = new TestSuite(MultilingualTests28.class.getName());
		suite.addTest(new PledgesFormTests("testPledgesEntityHelperSanity"));
		suite.addTest(new PledgesFormTests("testFetchEntities"));
		//suite.addTest(new MultilingualTests28("testSerializationAllLanguagesFilled"));
		return suite;
	}
	
	public void testFetchEntities()
	{
		List<FundingPledges> pledges = PledgesEntityHelper.fetchEntities(FundingPledges.class, "id", 3L);
		
		assertEquals(1, pledges.size());
		FundingPledges pledge = pledges.get(0);
		assertEquals("Test pledge 1", pledge.getTitleFreeText());
		assertNull(pledge.getTitle());
		assertEquals("European", pledge.getOrganizationGroup().getOrgGrpName());
		
		assertEquals(1, pledge.getSectorlist().size());
		FundingPledgesSector fps = pledge.getSectorlist().iterator().next();
		assertEquals("3 NATIONAL COMPETITIVENESS", fps.getSector().getName());
		assertEquals(Float.valueOf(100.0f), fps.getSectorpercentage());
		
		assertEquals(2, pledge.getLocationlist().size());
		List<FundingPledgesLocation> locs = new ArrayList<>(pledge.getLocationlist());
		Collections.sort(locs, new Comparator<FundingPledgesLocation>(){
					@Override public int compare(FundingPledgesLocation loc1, FundingPledgesLocation loc2)
					{
						return loc1.getLocation().getName().compareTo(loc2.getLocation().getName());
					}});
		assertEquals("Balti County", locs.get(0).getLocation().getName());
		assertEquals("Cahul County", locs.get(1).getLocation().getName());
		
		
		TLSUtils.getThreadLocalInstance().setForcedLangCode("ru");
		pledges = PledgesEntityHelper.fetchEntities(FundingPledges.class, "id", 3L);
		
		assertEquals(1, pledges.size());
		pledge = pledges.get(0);
		assertEquals("Test pledge 1", pledge.getTitleFreeText());
		assertNull(pledge.getTitle());
		assertEquals("Европейская", pledge.getOrganizationGroup().getOrgGrpName());
		
		AmpOrganisation org = (AmpOrganisation) PledgesEntityHelper.fetchEntities(AmpOrganisation.class, "ampOrgId", 21698L, "name", "Finland").get(0);
		assertEquals(Long.valueOf(21698L), org.getAmpOrgId());
		assertEquals("Финляндия", org.getName());
		
		assertEquals(0, PledgesEntityHelper.fetchEntities(AmpOrganisation.class, "ampOrgId", 12L, "name", "Finland").size());
	}
	
	public void testPledgesEntityHelperSanity()
	{
		FundingPledges pledge = PledgesEntityHelper.getPledgesById(3L);
		assertTrue(PledgesEntityHelper.getPledges().size() > 0); // more like a check on non-crashing
		assertTrue(PledgesEntityHelper.getFundingRelatedToPledges(pledge).size() == 0);
		assertEquals(1, PledgesEntityHelper.getPledgesByDonorGroup(18L).size());
		assertEquals("Test pledge 1", PledgesEntityHelper.getPledgesByDonorGroup(18L).get(0).getTitleFreeText());
		assertEquals(0, PledgesEntityHelper.getPledgesByDonor(21696L).size());
		assertEquals(0, PledgesEntityHelper.getPledgesByDonorAndTitle(21696L, "Test pledge 1").size());
		assertEquals(0, PledgesEntityHelper.getPledgesByDonorAndTitle(2L, "Test pledge 1").size());
		assertEquals(0, PledgesEntityHelper.getPledgesByDonorAndTitle(21696L, "Test pledge 321").size());
		assertEquals(0, PledgesEntityHelper.getPledgesDetails(3L).size());
		assertEquals(2, PledgesEntityHelper.getPledgesLocations(3L).size());
		assertEquals(0, PledgesEntityHelper.getPledgesPrograms(3L).size());
		assertEquals(1, PledgesEntityHelper.getPledgesSectors(3L).size());
		
		// non-existant pledge id		
		assertEquals(0, PledgesEntityHelper.getPledgesDetails(3333333L).size());
		assertEquals(0, PledgesEntityHelper.getPledgesLocations(3333333L).size());
		assertEquals(0, PledgesEntityHelper.getPledgesPrograms(3333333L).size());
		assertEquals(0, PledgesEntityHelper.getPledgesSectors(333333L).size());

	}
	
	@Override
    protected void setUp() throws Exception
    {
		TLSUtils.getThreadLocalInstance().setForcedLangCode("en");
		super.setUp();
        // do nothing now                
    }
}