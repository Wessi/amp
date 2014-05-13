package org.digijava.module.aim.ar.util;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.*;
import java.util.Map.Entry;

import org.apache.batik.gvt.renderer.DynamicRenderer;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ColumnFilterGenerator;
import org.dgfoundation.amp.ar.ViewDonorFilteringInfo;
import org.dgfoundation.amp.ar.viewfetcher.InternationalizedModelDescription;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.exception.reports.ReportException;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.AdvancedReportUtil;
import org.digijava.module.aim.util.caching.AmpCaching;
import org.digijava.module.translation.util.ContentTranslationUtil;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * various utils for reports
 * @author Dolghier Constantin
 *
 */
public class ReportsUtil {
	private static Logger logger	= Logger.getLogger(ReportsUtil.class);
	
//	@SuppressWarnings("unchecked")
//	public static Collection<AmpOrganisation> getAllOrgByRole(String roleCode ){
//        Session session = null;
//        Collection<AmpOrganisation> col 	= null;
//
//        try {
//            session = PersistenceManager.getRequestDBSession();
//            String queryString = "select distinct aor.organisation from " + AmpOrgRole.class.getName() + " as aor  "
//                + " inner join aor.role as rol where rol.roleCode=:roleCode order by aor.organisation.name";
//            Query qry = session.createQuery(queryString);
//            qry.setString("roleCode", roleCode);
//            col = qry.list();
//        } catch (Exception e) {
//            logger.debug("Exception from getAllOrgByRole()");
//            e.printStackTrace();
//        }
//        return col;
//    }
	
	@SuppressWarnings("unchecked")
	public static Collection<AmpOrganisation> getAllOrgByRoleOfPortfolio(String roleCode) {
		if (AmpCaching.getInstance().allOrgByRoleOfPortfolio.containsKey(roleCode))
			return new ArrayList<AmpOrganisation>(AmpCaching.getInstance().allOrgByRoleOfPortfolio.get(roleCode));
		
        Session session = null;
        List<AmpOrganisation> col = null;
        try {
            session = PersistenceManager.getRequestDBSession();

            String rewrittenColumns = SQLUtils.rewriteQuery("amp_organisation", "ao", 
            		new HashMap<String, String>(){{
            			put("name", InternationalizedModelDescription.getForProperty(AmpOrganisation.class, "name").getSQLFunctionCall("ao.amp_org_id"));
            			put("description", InternationalizedModelDescription.getForProperty(AmpOrganisation.class, "description").getSQLFunctionCall("ao.amp_org_id"));
            		}});
            
            String orgIdsSource;
            boolean roleCodeNeededInQuery;
            if (roleCode.equals(Constants.ROLE_CODE_DONOR))
            {
            	orgIdsSource = "select DISTINCT(amp_donor_org_id) FROM amp_funding";
            	roleCodeNeededInQuery = false;
            }
            else
            {
            	roleCodeNeededInQuery = true;
            	orgIdsSource = "select DISTINCT(organisation) FROM amp_org_role WHERE role = (SELECT amp_role_id FROM amp_role WHERE role_code=:roleCode)";
            }
            
            String queryString = "select distinct " + rewrittenColumns + " from amp_organisation ao " +
            		"WHERE ao.amp_org_id IN (" + orgIdsSource + ") AND " +
            		"(ao.deleted is null or ao.deleted = false) ";

            Query qry = session.createSQLQuery(queryString).addEntity(AmpOrganisation.class);
            qry.setCacheable(true);
            
            if (roleCodeNeededInQuery)
            	qry = qry.setString("roleCode", roleCode);
            
            col = qry.list();

            Collections.sort(col, new Comparator<AmpOrganisation>() {
                public int compare(AmpOrganisation o1, AmpOrganisation o2) {
                    return o1.getName().trim().compareTo(o2.getName().trim());
                }
            });

        } catch (Exception e) {
            logger.debug("Exception from getAllOrgByRoleOfPortfolio()");
            logger.debug(e.toString());
        }

        AmpCaching.getInstance().allOrgByRoleOfPortfolio.put(roleCode, new ArrayList<AmpOrganisation>(col));
        return col;

    }
	
	public static Set<AmpOrganisation> processSelectedFilters(Object[] src)
	{
		return (Set<AmpOrganisation>) processSelectedFilters(src, AmpOrganisation.class);
	}
	
	/**
	 * returns a Set of objects of a given class whose ids are given in the input array
	 * @param src
	 * @param colObjClass
	 * @return
	 */
	public static Set processSelectedFilters(Object [] src, Class clazz) {
		try{
			if (src != null && src.length > 0) {
				HashSet set = new HashSet();
				for (int i=0; i<src.length; i++) {
					Long id 		= Long.parseLong(src[i].toString());
					Object ampObj	= Util.getSelectedObject(clazz, id);
					if (ampObj != null)
						set.add(ampObj);
				}
				return set;
			}
		}
		catch(Exception E) {
			E.printStackTrace();
		}
		return null;
	}	
	
	public static void throwErrorIfNotEmpty(String errMsg){
		if (!errMsg.isEmpty()) {
			logger.error("Database sanity check - FAIL: " + errMsg);
			throw new Error(errMsg);
		}
	}
	
	/**
	 * throws an exception containing the error message if the database does not respect some kind of minimum sanity checks. Returns normally if everything is fine
	 */
	public static void checkDatabaseSanity(Session session) throws Exception
	{
		String errMsg = "";
		
		logger.debug("Database sanity check - in progress...");
		List<?> res = session.createSQLQuery("select DISTINCT(amp_report_id) from amp_report_column arc WHERE " + 
				"(SELECT count(*) from amp_report_column arc2 WHERE arc2.amp_report_id = arc.amp_report_id AND arc2.columnid = arc.columnid) > 1").list();
		if (!res.isEmpty())
			errMsg += "The following reports have a column repeated at least twice each: amp_report_id IN (" + Util.toCSString(res) + ")" + System.lineSeparator();
		
		res = session.createSQLQuery("select DISTINCT(columnname) from amp_columns col WHERE " + 
				"(SELECT count(*) FROM amp_columns col2 WHERE col.columnname = col2.columnname) > 1").list();
		if (!res.isEmpty())
			errMsg += "The following column(s) are defined at least twice in amp_columns: (" + Util.toCSString(res) + ")" + System.lineSeparator();

		res = session.createQuery("select m.measureName from AmpMeasures as m group by m.measureName, m.type having count(m) >1").list(); 
		if (!res.isEmpty())
			errMsg +="Duplicate measurenames are found in AMP_MEASURES tables: (" + Util.toCSString(res) + ")" + System.lineSeparator();

		throwErrorIfNotEmpty(errMsg);
		logger.debug("Database sanity check - PASS");
	}
	
	/**
	 * checks that the "while fetching view X, filter by columns Y in case they are set in the filter bean" table is sane, e.g. references existing views and columns in them
	 * also checks that "fetch column X if filter field Y is not null" configuration is valid
	 * @param session
	 * @throws Exception
	 */
	public static void checkPledgesViewsSanity(Session session) throws Exception{
		String errMsg = "";
		logger.debug("Checking pledges reports sanity...");
		
		ArrayList<Field> fields = new ArrayList<Field>();
		ContentTranslationUtil.getAllFields(fields, AmpARFilter.class); // getting into _fields_ the list of all the fields of the class
		Map<String, Field> fieldsByNames = new HashMap<String, Field>();
		for(Field field:fields)
			fieldsByNames.put(field.getName(), field);
		
		for(String viewName:ColumnFilterGenerator.PLEDGES_VIEWS_FILTERED_COLUMNS.keySet()){
			Set<ViewDonorFilteringInfo> filteredColumns = ColumnFilterGenerator.PLEDGES_VIEWS_FILTERED_COLUMNS.get(viewName);
			Set<String> colNames = SQLUtils.getTableColumns(viewName);
			for(ViewDonorFilteringInfo dfi:filteredColumns){
				if (!colNames.contains(dfi.getViewFieldName()))
					errMsg += String.format("The view %s does not contain column %s referenced in the ColumnFiltersConf", viewName, dfi.getViewFieldName());
				if (!fieldsByNames.containsKey(dfi.getBeanFieldName()))
					errMsg += String.format("AmpARFilter does not contain property %s referenced in the ColumnFiltersConf for view %s", dfi.getBeanFieldName(), dfi.getViewFieldName());
			}
		}
		
		for(Entry<String, List<String>> entries:ColumnFilterGenerator.PLEDGES_COLUMNS_FILTERS.entrySet()){
			if (!fieldsByNames.containsKey(entries.getKey()))
				errMsg += String.format("field %s referenced in PLEDGES_COLUMNS_FILTERS does not exist in the DB", entries.getKey());
			for(String colName:entries.getValue()){
				if (AdvancedReportUtil.getColumnByName(colName) == null)
					errMsg += String.format("column %s referenced in PLEDGES_COLUMNS_FILTERS does not exist in the DB", colName);
			}
		}
		throwErrorIfNotEmpty(errMsg);
		logger.debug("Pledges reports sanity - PASS");
	}
}
