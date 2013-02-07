package org.dgfoundation.amp.ar;

import javax.servlet.http.HttpSession;

import org.apache.commons.collections.map.LRUMap;
import org.digijava.kernel.request.TLSUtils;
import java.util.Map;

import edu.emory.mathcs.backport.java.util.Collections;

/**
 * per-HttpSession LRU cache holding GroupReportData instances, which knows how to regenerate them.<br />
 * GroupReportData instances are keyed by a ReportContextData - because a RCD holds all the necessary 
 * data to regenerate a GroupReportData instance 
 * @author Dolghier Constantin
 *
 */
public class GroupReportDataCacher 
{
	public final static int MAX_CACHED_REPORTS_PER_USER = 7;
	public final static String GROUP_REPORT_DATA_CACHER_SESSION_ATTRIBUTE = "group_report_cache";
	
	private Map<Integer, GroupReportData> lru = Collections.synchronizedMap(new LRUMap(MAX_CACHED_REPORTS_PER_USER));		 
	
	public GroupReportDataCacher()
	{
		// empty
	}
	
	public void memorize(ReportContextData key, GroupReportData reportData)
	{
		if (reportData == null)
			throw new IllegalArgumentException("not allowed to store nulls into GroupReportDataCacher!");
		
		int mapKey = System.identityHashCode(key);
		lru.put(mapKey, reportData);
	}
	
	public GroupReportData recall(ReportContextData key)
	{
		int mapKey = System.identityHashCode(key);
		GroupReportData res = lru.get(mapKey);
		if (res != null)
			return res;
		
		GroupReportData newRes = regenerateGroupReportData(key);
		lru.put(mapKey, newRes);
		return newRes;
	}
	
	public void delete(ReportContextData key)
	{
		int mapKey = System.identityHashCode(key);
		lru.remove(mapKey);
	}
	
	private GroupReportData regenerateGroupReportData(ReportContextData key)
	{
		GroupReportData res = ARUtil.generateReport(key.getReportMeta(), key.getFilter(), false);
		
		if (key.getReportSorters() != null && key.getReportSorters().size() > 0)
		{
			res.importLevelSorters(key.getReportSorters(), key.getReportMeta().getHierarchies().size());
			res.applyLevelSorter();
		}
		
		if (key.getFilter().getSortBy() != null)
		{
			res.setSorterColumn(key.getFilter().getSortBy());
			res.setSortAscending(key.getFilter().getSortByAsc());
		}
		return res;
	}
	
	public static GroupReportData staticRecall(ReportContextData key)
	{
		HttpSession session = TLSUtils.getRequest().getSession();
		GroupReportDataCacher inst = (GroupReportDataCacher) session.getAttribute(GROUP_REPORT_DATA_CACHER_SESSION_ATTRIBUTE);
		if (inst == null)
			throw new IllegalStateException("could not find GroupReportDataCache map in session!");
		return inst.recall(key);
	}
	
	public static void staticMemorize(ReportContextData key, GroupReportData reportData)
	{
		GroupReportDataCacher inst = getOrCreateCacher();
		inst.memorize(key, reportData);
	}
	
	public static void staticDelete(ReportContextData key)
	{
		GroupReportDataCacher inst = getOrCreateCacher();
		inst.delete(key);
	}
	
	private static GroupReportDataCacher getOrCreateCacher()
	{
		HttpSession session = TLSUtils.getRequest().getSession();
		GroupReportDataCacher inst = (GroupReportDataCacher) session.getAttribute(GROUP_REPORT_DATA_CACHER_SESSION_ATTRIBUTE);
		if (inst == null)
		{
			inst = new GroupReportDataCacher();
			session.setAttribute(GROUP_REPORT_DATA_CACHER_SESSION_ATTRIBUTE, inst);
		}
		return inst;
	}
}

