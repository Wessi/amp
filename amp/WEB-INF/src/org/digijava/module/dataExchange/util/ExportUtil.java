package org.digijava.module.dataExchange.util;

import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.util.TeamUtil;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;

public class ExportUtil {

    private static Logger logger = Logger.getLogger(ExportUtil.class);
	
	/*
	 * @deprecated do not use. using getActivities();
	 */
    public static Collection<AmpActivity> getAllTeamAmpActivities(Long teamId) {
        Session session = null;
        Collection<AmpActivity> retValue = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "";
            Query qry = null;
            if(teamId == null) {
            	queryString = "select act from "+ AmpActivity.class.getName() + " act where act.team is null";
            	qry=session.createQuery(queryString);
            }
            else{
            	queryString = "select act from "  + AmpActivity.class.getName() + " act where (act.team=:teamId)";
            	qry=session.createQuery(queryString);
            	qry.setParameter("teamId", teamId, Hibernate.LONG);
            }
            retValue = qry.list();
        } catch(Exception e) {
            logger.debug("Exception from getAllTeamAmpActivities()");
            logger.debug(e.toString());
            throw new RuntimeException(e);
        }
        return retValue;
    }
	
    
    public static List<AmpActivity> getActivities(Long teamId, Long[] donorTypes, Long[] donorGroups, Long[] donorAgences,
    		Long[] primarySectors, Long[] secondarySectors) {
    	List<AmpActivity> retValue = null;
        Session session = null;    	

        if(teamId == null) {
            logger.debug("TeamId is null");
            throw new RuntimeException("TeamId is null");
        } 
    	
        try {
            session = PersistenceManager.getRequestDBSession();
            StringBuffer from = new StringBuffer("select distinct act from " + AmpActivity.class.getName() + " as act, ");
            StringBuffer mainWhere =  new StringBuffer(" where (act.team=:teamId) ");
            
            if ((primarySectors != null && primarySectors.length > 0) ||
            		(secondarySectors != null && secondarySectors.length > 0)){
            	from.append(AmpActivitySector.class.getName() + " as aas, ");
            	mainWhere.append(" and act.ampActivityId = aas.activityId.ampActivityId ");
            }
            
            
            if ((donorTypes != null && donorTypes.length > 0) ||
            		(donorGroups != null && donorGroups.length > 0) ||
            		(donorAgences != null && donorAgences.length > 0)){
            	from.append(AmpFunding.class.getName() + " as afund, ");
            	from.append(AmpOrganisation.class.getName() + " as aOrg, ");
            	mainWhere.append(" and act.ampActivityId = afund.ampActivityId.ampActivityId ");
            	mainWhere.append(" and afund.ampDonorOrgId.ampOrgId = aOrg.ampOrgId ");

            }
            from.delete(from.length()-2, from.length());
            
            
            StringBuffer primarySectorWhere = null;
            StringBuffer secondarySectorWhere = null;
            StringBuffer donorTypesWhere = null;
            StringBuffer donorGroupsWhere = null;
            StringBuffer donorAgencesWhere = null;
            
            if ((primarySectors != null && primarySectors.length > 0)){
           		primarySectorWhere = new StringBuffer(" ( aas.sectorId.ampSectorId in ( ");
            	for (Long elem : primarySectors) {
            		primarySectorWhere.append(elem);
            		primarySectorWhere.append(", ");
				}
            	primarySectorWhere.delete(primarySectorWhere.length()-2, primarySectorWhere.length());
            	primarySectorWhere.append(" ) )");
            }

            if ((secondarySectors != null && secondarySectors.length > 0)){
            	secondarySectorWhere = new StringBuffer(" ( aas.sectorId.ampSectorId in ( ");
            	for (Long elem : secondarySectors) {
            		secondarySectorWhere.append(elem);
            		secondarySectorWhere.append(", ");
				}
            	secondarySectorWhere.delete(secondarySectorWhere.length()-2, secondarySectorWhere.length());
            	secondarySectorWhere.append(" ) )");
            }

            if ((donorTypes != null && donorTypes.length > 0)){
            	donorTypesWhere = new StringBuffer(" ( aOrg.orgTypeId.ampOrgTypeId in ( ");
            	for (Long elem : donorTypes) {
            		donorTypesWhere.append(elem);
            		donorTypesWhere.append(", ");
				}
            	donorTypesWhere.delete(donorTypesWhere.length()-2, donorTypesWhere.length());
            	donorTypesWhere.append(" ) )");
            }

            if ((donorGroups != null && donorGroups.length > 0)){
            	donorGroupsWhere = new StringBuffer(" ( aOrg.orgGrpId.ampOrgGrpId in ( ");
            	for (Long elem : donorGroups) {
            		donorGroupsWhere.append(elem);
            		donorGroupsWhere.append(", ");
				}
            	donorGroupsWhere.delete(donorGroupsWhere.length()-2, donorGroupsWhere.length());
            	donorGroupsWhere.append(" ) )");
            }

            if ((donorAgences != null && donorAgences.length > 0)){
            	donorAgencesWhere = new StringBuffer(" ( aOrg.ampOrgId in ( ");
            	for (Long elem : donorAgences) {
            		donorAgencesWhere.append(elem);
            		donorAgencesWhere.append(", ");
				}
            	donorAgencesWhere.delete(donorAgencesWhere.length()-2, donorAgencesWhere.length());
            	donorAgencesWhere.append(" ) )");
            }
            
            if (primarySectorWhere != null || secondarySectorWhere != null 
            		|| donorTypesWhere != null || donorGroupsWhere!= null ||  donorAgencesWhere != null){
            	mainWhere.append(" and ( ");
            	if (primarySectorWhere != null){
            		mainWhere.append(primarySectorWhere);
            		mainWhere.append(" or ");
            	}

            	if (secondarySectorWhere != null){
            		mainWhere.append(secondarySectorWhere);
            		mainWhere.append(" or ");
            	}

            	if (donorTypesWhere != null){
            		mainWhere.append(donorTypesWhere);
            		mainWhere.append(" or ");
            	}

            	if (donorGroupsWhere != null){
            		mainWhere.append(donorGroupsWhere);
            		mainWhere.append(" or ");
            	}
            	
            	if (donorAgencesWhere != null){
            		mainWhere.append(donorAgencesWhere);
            		mainWhere.append(" or ");
            	}
            	
            	mainWhere.delete(mainWhere.length()-3, mainWhere.length());
            	mainWhere.append(" ) ");
            }
            
            
            Query qry=session.createQuery(from.toString() + mainWhere.toString() );
            qry.setParameter("teamId", teamId, Hibernate.LONG);
            retValue = qry.list();
        } catch(Exception e) {
            logger.debug("Exception from getAllTeamAmpActivities()");
            logger.debug(e.toString());
            throw new RuntimeException(e);
        }    	
    	return retValue;
    }
}
