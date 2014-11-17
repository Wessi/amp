package org.digijava.kernel.ampapi.postgis.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.viewfetcher.ColumnValuesCacher;
import org.dgfoundation.amp.ar.viewfetcher.DatabaseViewFetcher;
import org.dgfoundation.amp.ar.viewfetcher.PropertyDescription;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.ar.viewfetcher.ViewFetcher;
import org.digijava.kernel.ampapi.endpoints.dto.SimpleJsonBean;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.ampapi.postgis.entity.AmpLocator;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpIndicatorLayer;
import org.digijava.module.aim.dbentity.AmpRole;
import org.digijava.module.aim.util.OrganisationUtil;
import org.digijava.module.aim.util.OrganizationSkeleton;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.esrigis.dbentity.AmpApiState;
import org.digijava.module.translation.util.ContentTranslationUtil;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.jdbc.Work;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

public class QueryUtil {
	protected static Logger logger = Logger.getLogger(QueryUtil.class);



	public static List<String> getAdminLevels() {
		return new ArrayList<String>(Arrays.asList("Country", "Region", "Zone", "District"));
	}



	public static AmpActivity getActivity(Long ampActivityId) {
		try {
			return (AmpActivity) PersistenceManager.getRequestDBSession().load(AmpActivity.class, ampActivityId);
		} catch (DgException e) {
			logger.error("cannot retrieve activity");
			return null;
		}
	}
	/**
	 * return a list of saved maps.
	 * 
	 * @return
	 * @throws DgException
	 */
	@SuppressWarnings("unchecked")
	public static List<AmpApiState> getMapList(String type) throws DgException {
		Criteria mapsCriteria = PersistenceManager.getRequestDBSession().createCriteria(AmpApiState.class);
		mapsCriteria.add(Restrictions.eq("type", type));
		return mapsCriteria.list();
	}

	public static List<OrganizationSkeleton> getOrganizations(Long roleId) {
		try {
			AmpRole r = (AmpRole) PersistenceManager.getRequestDBSession().load(AmpRole.class, roleId);
			return OrganizationSkeleton.populateOrganisationSkeletonList(r.getRoleCode());
		} catch (DgException e) {
			logger.error("cannot load orgs", e);
			return null;
		}

	}

	@SuppressWarnings("unchecked")
	public static List<AmpLocator> getLocationsWithinDistance(Geometry point, long distance, List <Long> ommitedLocations) {
		// true to use spheroid
		String queryString = "select l from " + AmpLocator.class.getName()
				+ " l where dwithin(l.theGeometry, :geometry, :distance,true) = true and l.id not in (:ommitedLocations)";
		Query q = PersistenceManager.getSession().createQuery(queryString);
		q.setParameter("geometry", point);
		q.setParameter("distance", distance);
		q.setParameterList("ommitedLocations", ommitedLocations);
		return q.list();

	}

	public static List<AmpLocator> getLocationsFromKeyword(String keyword) {
		final List <AmpLocator> locationList = new ArrayList <AmpLocator> ();
		try {
			int distance = ScoreCalculator.getMaxAllowedDistance(keyword);
			keyword = keyword.toLowerCase();
			final String queryString = "select  id,name,latitude,longitude,levenshtein('"+keyword+"',lower (name),1,1,1) as distance,thegeometry from amp_locator where levenshtein('"+keyword+"',lower (name),1,1,1) <= "+distance+
					 " or lower (name) like '%"+keyword+"%'";
			
			PersistenceManager.getSession().doWork(new Work() {

				@Override
				public void execute(Connection connection) throws SQLException {
					try(java.sql.ResultSet rs = SQLUtils.rawRunQuery(connection, queryString, null)) {
						while (rs.next()) {
							AmpLocator locator = new AmpLocator();
							locator.setId(rs.getLong("id"));
							locator.setName(rs.getString("name"));
							locator.setLatitude(rs.getString("latitude"));
							locator.setLongitude(rs.getString("longitude"));
							locator.setDistance(rs.getInt("distance"));
							locator.setTheGeometry(wktToGeometry ("POINT ("+locator.getLongitude()+" "+locator.getLatitude()+")"));
							locationList.add(locator);
						}
					}
				}});
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
		return locationList;
	}
	
	private static Geometry wktToGeometry(String wktPoint) {
        WKTReader fromText = new WKTReader();
        Geometry geom = null;
        try {
            geom = fromText.read(wktPoint);
        } catch (ParseException e) {
            throw new RuntimeException("Not a WKT string:" + wktPoint);
        }
        return geom;
    }
	
	public static List <Long> getIdsList (List <AmpLocator> locations) {
		List <Long> ids = new ArrayList <Long> ();
		for (AmpLocator locator:locations) {
			ids.add(locator.getId());
		}
		return ids;
		
	}

	/**
	 * Filter organization skelleton by orgGrpId
	 * 
	 * @param orgGrpId
	 * @return
	 */
	public static List<OrganizationSkeleton> getOrganizations(
			List<Long> orgGrpId) {
		final List<String> roleCodes = OrganisationUtil.getVisibleRoles();
		return OrganizationSkeleton
				.populateOrganisationSkeletonListByOrgGrpIp(orgGrpId, roleCodes);
	}
	
	public static List<JsonBean> getOrgTypes(){
		final List<JsonBean> orgTypes=new ArrayList<JsonBean>();
		PersistenceManager.getSession().doWork(new Work(){
				public void execute(Connection conn) throws SQLException {
					
					Map<Long, String> orgTypesName=QueryUtil.getTranslatedName(conn, "amp_org_type", "amp_org_type_id", "org_type");
					
					String query="select  aot.amp_org_type_id orgTypeId,aog.amp_org_grp_id orgGrpId " + 
								" ,aot.org_type orgTypeName, aot.org_type_code orgTypeCode " +
								" from amp_org_group aog,amp_org_type aot " +
								" where aot.amp_org_type_id=aog.org_type" +
								" order by orgTypeId";
					ResultSet rs = SQLUtils.rawRunQuery(conn, query, null);
					Long lastOrgTypeId = 0L;
					List<Long> orgsGrpId = null;
					while (rs.next()) {
						if (!lastOrgTypeId.equals(rs.getLong("orgTypeId"))) {
							lastOrgTypeId = rs.getLong("orgTypeId");
							JsonBean orgType = new JsonBean();
							orgsGrpId = new ArrayList<Long>();
							orgType.set("id", rs.getLong("orgTypeId"));
							if (orgTypesName != null){
								orgType.set("name", orgTypesName.get(lastOrgTypeId));
							} else {
								orgType.set("name", rs.getString("orgTypeName"));
							}
							orgType.set("groupIds", orgsGrpId);
							orgTypes.add(orgType);
						}
						orgsGrpId.add(rs.getLong("orgGrpId"));
					}
	
				}
	    });
	    return orgTypes;
	}

public static List<JsonBean> getOrgGroups() {
	final List<JsonBean> orgGroups=new ArrayList<JsonBean>();
    PersistenceManager.getSession().doWork(new Work(){
			public void execute(Connection conn) throws SQLException {
				Map<Long,String>orgGroupsNames=QueryUtil.getTranslatedName(conn, "amp_org_group", "amp_org_grp_id", "org_grp_name");
				String query=" select aog.amp_org_grp_id orgGrpId, "+
							 " aog.org_grp_name grpName, "+
							 " aog.org_grp_code orgCode, "+
							 " aog.org_type  orgType, "+
							 " o.amp_org_id orgId  "+
							 " from amp_org_group aog "+ 
							 " left outer join amp_organisation o on aog.amp_org_grp_id=o.org_grp_id "+
							 " order by orgGrpId";
				ResultSet rs=SQLUtils.rawRunQuery(conn, query, null);
				Long lastOrgGrpId=0L;
				List <Long>orgsId=null;
				while(rs.next()){
				if(!lastOrgGrpId.equals(rs.getLong("orgGrpId"))){
					lastOrgGrpId=rs.getLong("orgGrpId");
					JsonBean orgGrp=new JsonBean();
					orgsId=new ArrayList<Long>();
					orgGrp.set("id", rs.getLong("orgGrpId"));
						if(orgGroupsNames!=null){
							orgGrp.set("name", orgGroupsNames.get(lastOrgGrpId));	
						}else{
							orgGrp.set("name", rs.getString("grpName"));
						}
					orgGrp.set("typeId", rs.getLong("orgType"));
					orgGrp.set("orgIds",orgsId);
					orgGroups.add(orgGrp);
				}
					if(rs.getLong("orgId")!=0){
						orgsId.add(rs.getLong("orgId"));
					}
				}

			}
    });
    return orgGroups;
}

	public static List<JsonBean> getOrgs() {
		final List<JsonBean> orgs = new ArrayList<JsonBean>();
		final List<String> roleCodes = OrganisationUtil.getVisibleRoles();
		PersistenceManager.getSession().doWork(new Work() {
			public void execute(Connection conn) throws SQLException {
				
				//go and fetch translated version of organisation name if multilingual is enabled
				
				Map<Long, String> organisationsNames = QueryUtil.getTranslatedName(conn,"amp_organisation","amp_org_id","name");
				String query = " select distinct o.amp_org_id orgId, "+
						" o.name ,  "+
						" aor.role roleId , "+ 
						" o.org_grp_id grpId  "+
						" from amp_org_role aor,amp_organisation o "+
						" where aor.organisation=o.amp_org_id " +
						" and aor.role IN " +
							"(SELECT r.amp_role_id FROM amp_role r WHERE r.role_code IN (" + 
							Util.toCSStringForIN(roleCodes) + "))" +
						" order by o.amp_org_id";
				ResultSet rs = SQLUtils.rawRunQuery(conn, query, null);
				Long lastOrgId = 0L;
				List<Long> rolesId = null;
				while (rs.next()) {
					if (!lastOrgId .equals(rs.getLong("orgId"))) {
						lastOrgId  = rs.getLong("orgId");
						JsonBean org = new JsonBean();
						rolesId = new ArrayList<Long>();
						org.set("id", lastOrgId);
						if(ContentTranslationUtil.multilingualIsEnabled()){
							org.set("name", organisationsNames.get(lastOrgId));
						}else{
							org.set("name", rs.getString("name"));
						}
						org.set("groupId", rs.getLong("grpId"));	
						org.set("rolesIds", rolesId);
						orgs.add(org);
					}
					rolesId.add(rs.getLong("roleId"));
				}

			}


		});
		return orgs;
	}
	private static Map<Long, String> getTranslatedName(Connection conn,String tableName,String id,String name)
			throws SQLException {
		Map<Long, String> names = null;
		if (ContentTranslationUtil.multilingualIsEnabled()) {
			names  = new HashMap<Long, String>();
			ViewFetcher v = DatabaseViewFetcher.getFetcherForView(tableName,"",TLSUtils.getEffectiveLangCode(),
					new HashMap<PropertyDescription, ColumnValuesCacher>(),conn, "*");
			ResultSet rs = v.fetch(null);
			while (rs.next()) {
				names .put(rs.getLong(id),rs.getString(name));
			}

		}
		return names;
	}

	public static List<SimpleJsonBean> getOrgRoles() {
		// //yet not translatable but its ready when it is
		final List<SimpleJsonBean> rogRoles = new ArrayList<SimpleJsonBean>();
		PersistenceManager.getSession().doWork(new Work() {
			public void execute(Connection conn) throws SQLException {
				ViewFetcher v = DatabaseViewFetcher.getFetcherForView("amp_role","",TLSUtils.getEffectiveLangCode(),
						new HashMap<PropertyDescription, ColumnValuesCacher>(),conn, "*");
				ResultSet rs = v.fetch(null);
				
				while (rs.next()) {
					rogRoles.add(new SimpleJsonBean(rs.getLong("amp_role_id"),rs.getString("name")));
				}

			}
		});
		return rogRoles;
	}
	
	public static List <AmpIndicatorLayer> getIndicatorLayers () {
		Session dbSession = PersistenceManager.getSession();
		String queryString = "select ind from "
				+ AmpIndicatorLayer.class.getName() + " ind";
		Query qry = dbSession.createQuery(queryString);
		qry.setCacheable(true);
		return qry.list();
 }
	
	 public static List <AmpIndicatorLayer> getIndicatorByCategoryValue (String value) {
			Session dbSession = PersistenceManager.getSession();
			String queryString = "select ind from "
					+ AmpIndicatorLayer.class.getName()
					+ " ind where upper(ind.admLevel.value)=:value)";
			Query qry = dbSession.createQuery(queryString);
			qry.setCacheable(true);
			qry.setString("value", value.toUpperCase());
			return qry.list();
		 
	 }
	 
	 @SuppressWarnings("unchecked")
		public static List<AmpCategoryValue> getClusterLevels() {
			List<AmpCategoryValue> al = null;
			String queryString = "select distinct a.parentCategoryValue from " + AmpCategoryValueLocations.class.getName()+ " a";
				Query q = PersistenceManager.getSession().createQuery(queryString);
				q.setMaxResults(100);
				al = q.list();
			return al;

		}
	 public static List<String>getImplementationLocationsInUse(){
		 final List<String>list=new ArrayList<String>();
		 
		 
		
		PersistenceManager.getSession().doWork(new Work() {
			public void execute(Connection conn) throws SQLException {
				String query = "select * from amp_category_value cv,amp_category_class cc "
						+ " where cv.amp_category_class_id =cc.id "
						+ " and keyname ='"+ CategoryConstants.IMPLEMENTATION_LOCATION_KEY +"'  "
						+ " and cv.id in(select distinct parent_category_value from amp_category_value_location )  "
						+ " order by cv.index_column ";		
				ResultSet rs = SQLUtils.rawRunQuery(conn, query, null);
				while (rs.next()) {
					list.add(rs.getString("category_value"));
				}
				rs.close();
			}});
				
		return list;
	}
}

