package org.digijava.module.aim.util;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.Collator;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.JDBCException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.apache.log4j.Logger;
import org.apache.struts.util.LabelValueBean;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.CellColumn;
import org.dgfoundation.amp.ar.Column;
import org.dgfoundation.amp.ar.FilterParam;
import org.dgfoundation.amp.ar.cell.Cell;
import org.digijava.kernel.dbentity.Country;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.user.Group;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityInternalId;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpAhsurvey;
import org.digijava.module.aim.dbentity.AmpAhsurveyIndicator;
import org.digijava.module.aim.dbentity.AmpAhsurveyQuestion;
import org.digijava.module.aim.dbentity.AmpAhsurveyResponse;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpCategoryValue;
import org.digijava.module.aim.dbentity.AmpClosingDateHistory;
import org.digijava.module.aim.dbentity.AmpComments;
import org.digijava.module.aim.dbentity.AmpComponent;
import org.digijava.module.aim.dbentity.AmpField;
import org.digijava.module.aim.dbentity.AmpFilters;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpFundingMTEFProjection;
import org.digijava.module.aim.dbentity.AmpIndicatorValue;
import org.digijava.module.aim.dbentity.AmpLevel;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpPages;
import org.digijava.module.aim.dbentity.AmpPhysicalComponentReport;
import org.digijava.module.aim.dbentity.AmpPhysicalPerformance;
import org.digijava.module.aim.dbentity.AmpReportCache;
import org.digijava.module.aim.dbentity.AmpReportLocation;
import org.digijava.module.aim.dbentity.AmpReportLog;
import org.digijava.module.aim.dbentity.AmpReportPhysicalPerformance;
import org.digijava.module.aim.dbentity.AmpReportSector;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.dbentity.AmpRole;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpStatus;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.AmpTeamPageFilters;
import org.digijava.module.aim.dbentity.AmpTeamReports;
import org.digijava.module.aim.dbentity.AmpTermsAssist;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.dbentity.CMSContentItem;
import org.digijava.module.aim.dbentity.IPAContract;
import org.digijava.module.aim.dbentity.IndicatorActivity;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.helper.AmpPrgIndicatorValue;
import org.digijava.module.aim.helper.AmpProjectBySector;
import org.digijava.module.aim.helper.Assistance;
import org.digijava.module.aim.helper.CategoryConstants;
import org.digijava.module.aim.helper.CategoryManagerUtil;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.CountryBean;
import org.digijava.module.aim.helper.CurrencyWorker;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.helper.Documents;
import org.digijava.module.aim.helper.FiscalCalendar;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.Indicator;
import org.digijava.module.aim.helper.ParisIndicator;
import org.digijava.module.aim.helper.Question;
import org.digijava.module.aim.helper.SurveyFunding;
import org.digijava.module.aim.helper.fiscalcalendar.BaseCalendar;
import org.digijava.module.aim.helper.fiscalcalendar.EthiopianCalendar;
import org.digijava.module.calendar.dbentity.AmpCalendar;
import org.digijava.module.calendar.dbentity.Calendar;
import org.digijava.module.common.util.DateTimeUtil;

import com.sun.rowset.CachedRowSetImpl;

public class DbUtil {
	private static Logger logger = Logger.getLogger(DbUtil.class);
        
           public static String filter(String text) {

		String result = null;
             
		if (text != null) {
			result=text.replaceAll("&", "&amp;");
			result = result.replaceAll(">", "&gt;");
			result = result.replaceAll("<", "&lt;");
			result = result.replaceAll("'", "\'");//"&acute;");
			result = result.replaceAll("\"", "&quot;");
			
		}
                

		return result;

	}
	public static String getDescParsed(String str)
	{
		StringBuffer strbuff = new StringBuffer();
		char[] ch = new char[str.length()];

		ch = str.toCharArray();

		for(int i=0; i<ch.length; i++)
		{
			if(ch[i] == '<')
			{
				while(ch[i] != '>')
					i++;
			}
			else if(ch[i] == '&')
			{
				if(ch[i+1] == 'n' && ch[i+2] == 'b' && ch[i+3] == 's' && ch[i+4] == 'p' && ch[i+5] == ';')
					i = i+5;
				else
					strbuff.append(ch[i]);
			}
			else
			{
				if(i < ch.length)
					strbuff.append(ch[i]);
			}
		}
		str = new String(strbuff);
		return str;
	}


    /**
	 * Removes the team-reports and member-reports association table.
	 * @param reportId	A Long array of the reports to be updated
	 * @param teamId  	The teamId of the team whose association with
	 * 					the specified reports must be removed. When the
	 * 					teams are dissociated with the reports, the association
	 * 					from the members of that team also gets removed.
	 */
	public static void removeTeamReports(Long reportId[],Long teamId) {
		Session session = null;
		Transaction tx = null;

		if (reportId == null || reportId.length <= 0) return;

		try {
			session = PersistenceManager.getSession();
			tx = session.beginTransaction();

			String queryString = "select tm from "
				+ AmpTeamMember.class.getName()
				+ " tm where (tm.ampTeam=:teamId)";

			Query qry = session.createQuery(queryString);
			qry.setParameter("teamId", teamId, Hibernate.LONG);
			Collection col = qry.list();
			if (col != null && col.size() > 0) {
				for (int i = 0;i < reportId.length;i ++) {
					if (reportId[i] != null) {
						queryString = "select r from "
							+ AmpReports.class.getName()
							+ " r where (r.ampReportId=:repId)";
						qry = session.createQuery(queryString);
						qry.setParameter("repId",reportId[i],Hibernate.LONG);
						Iterator itr = qry.list().iterator();
						if (itr.hasNext()) {
							AmpReports ampReport = (AmpReports) itr.next();
							if (ampReport.getMembers() != null) {
								/*
								 * removing the team members association with the
								 * report
								 */
								ampReport.getMembers().removeAll(col);
								session.update(ampReport);
							}
						}

						/*
						 * removing the teams association with the report
						 */
						queryString = "select tr from " + AmpTeamReports.class.getName()
							+ " tr where (tr.team=:teamId) and "
							+ " (tr.report=:repId)";
						qry = session.createQuery(queryString);
						qry.setParameter("teamId",teamId,Hibernate.LONG);
						qry.setParameter("repId",reportId[i],Hibernate.LONG);
						itr = qry.list().iterator();
						if (itr.hasNext()) {
							AmpTeamReports ampTeamRep = (AmpTeamReports) itr.next();
							session.delete(ampTeamRep);
						}
					}
				}
			}
			tx.commit();

		} catch (Exception e) {
			logger.error("Exception from updateMemberReports");
			logger.error(e.getMessage());
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception tex) {
					logger.error("Transaction rollback failed");
					logger.error(tex);
				}
			}
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception ex) {
					logger.error("Failed to release session");
				}
			}
		}
	}

	/**
	 * Associated the reports with the given team
	 * @param reportId The Long array of reportIds which are to be associated
	 * 				   with the given team
	 * @param teamId   The team id of the team to which the reports are to be
	 * 				   assigned
	 */
	public static void addTeamReports(Long reportId[],Long teamId) {
		Session session = null;
		Transaction tx = null;

		try {
			session = PersistenceManager.getSession();
			tx = session.beginTransaction();

			String queryString = "select tm from "
				+ AmpTeam.class.getName()
				+ " tm where (tm.ampTeamId=:teamId)";

			Query qry = session.createQuery(queryString);
			qry.setParameter("teamId", teamId, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			AmpTeam team = null;
			if (itr.hasNext()) {
				team = (AmpTeam) itr.next();
			}
			if (team != null) {
				if (reportId != null && reportId.length > 0) {
					queryString = "select rep from "
						+ AmpReports.class.getName()
						+ " rep where rep.ampReportId in (";
					StringBuffer temp = new StringBuffer();
					for (int i = 0;i < reportId.length;i ++) {
						temp.append(reportId[i]);
						if ((i+1) != reportId.length) {
							temp.append(",");
						}
					}
					temp.append(")");
					queryString += temp;
					qry = session.createQuery(queryString);
					logger.debug("Query :" + qry.getQueryString());
					itr = qry.list().iterator();
					while (itr.hasNext()) {
						AmpReports report = (AmpReports) itr.next();
						if (report != null) {
							String tempQry = "select teamRep from "
								+ AmpTeamReports.class.getName()
								+ " teamRep where (teamRep.team=:tId) and "
								+ " (teamRep.report=:rId)";
							Query tmpQry = session.createQuery(tempQry);
							tmpQry.setParameter("tId",team.getAmpTeamId(),Hibernate.LONG);
							tmpQry.setParameter("rId",report.getAmpReportId(),Hibernate.LONG);
							Iterator tmpItr = tmpQry.list().iterator();
							if (!tmpItr.hasNext()) {
								AmpTeamReports tr = new AmpTeamReports();
								tr.setTeam(team);
								tr.setReport(report);
								tr.setTeamView(false);
								session.save(tr);
							}
						}
					}
				}
			}
			tx.commit();
		} catch (Exception e) {
			logger.error("Exception from addTeamReports()");
			logger.error(e.getMessage());
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception rbf) {
					logger.error("Rollback failed");
					logger.error(rbf.getMessage());
				}
			}
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed");
					logger.error(rsf.getMessage());
				}
			}
		}
	}



    public static AmpReports getAmpReports (Long id) {
		Session session		= null;
		AmpReports report	= null;
		try{
			session		= PersistenceManager.getSession();
			report		= (AmpReports)session.get(AmpReports.class, id);
		} catch (Exception ex) {
			logger.error("Unable to get AmpReports by Id :" + ex);
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed :" + ex2);
			}
		}
		return report;
	}

    public static Collection getFundingDetails(Long fundId) {
        Session session = null;
        Collection fundingDetails = new ArrayList();

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select f from "
                + AmpFundingDetail.class.getName()
                + " f "
                + "where (f.ampFundingId=:fundId) order by f.transactionDate";
            Query qry = session.createQuery(queryString);
            qry.setParameter("fundId", fundId, Hibernate.LONG);
            fundingDetails = qry.list();

        } catch (Exception ex) {
            logger.error("Unable to get fundingDetails :" + ex);
        }
        return fundingDetails;
    }
    
    public static Collection getDisbursementsFundingOfIPAContract(IPAContract c) {
        Session session = null;
        Collection<AmpFundingDetail> fundingDetails = new ArrayList<AmpFundingDetail>();

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select f from "
                + AmpFundingDetail.class.getName()
                + " f "
                + "where (f.contract=:cId) and f.transactionType=1";
            Query qry = session.createQuery(queryString);
            qry.setParameter("cId", c.getId(), Hibernate.LONG);
            fundingDetails = qry.list();

        } catch (Exception ex) {
            logger.error("Unable to get fundingDetails (disbursements) of an IPA contract:" + ex);
        }
        return fundingDetails;
    }

    public static Collection getFundingByActivity(Long actId) {
        Session session = null;
        Collection funding = new ArrayList();

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select f from "
                + AmpFunding.class.getName()
                + " f "
                + "where (f.ampActivityId=:actId)";
            Query qry = session.createQuery(queryString);
            qry.setParameter("actId", actId, Hibernate.LONG);
            funding = qry.list();

        } catch (Exception ex) {
            logger.error("Unable to get funding :" + ex);
        }
        return funding;
    }

    
    public static int countActivitiesByQuery(String sQuery, ArrayList<FilterParam> params) {
    	
    	Session sess=null;
    	Connection conn=null;
    		CellColumn cc=null;
    		try {
    			sess= PersistenceManager.getSession();
    		
    			conn=sess.connection();
    		} catch (HibernateException e) {
    			logger.error(e);
    			e.printStackTrace();
    		} catch (SQLException e) {
    			logger.error(e);
    			e.printStackTrace();
    		}
    		
    		int ii=0;

    		String query = "SELECT count(*) FROM amp_activity WHERE amp_activity_id IN ("
    				+ sQuery + " ) ";
    		//System.out.println("MASTER query count activities::: " + query);
    		PreparedStatement ps;
    		
    		
    		try {
    			ps = conn.prepareStatement(query);
    			if (params!=null){
        			
        			//add params if exist
        			for (int i = 0; i < params.size(); i++) {
        				ps.setObject(i+1, params.get(i).getValue(),params.get(i).getSqlType());	
        			}
        		
        		}	
    			ResultSet rs = ps.executeQuery();
    			ResultSetMetaData rsmd;
    			rsmd=rs.getMetaData();
    			rs.first();
    			ii=rs.getInt(1);
    			rs.close();

    		} catch (SQLException e) {
    			logger.error(e);
    			e.printStackTrace();
    		} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
    		finally {
    			try {
    				if (sess != null) {
    					PersistenceManager.releaseSession(sess);
    				}
    			} catch (Exception e) {
    				logger.error("Error parsing date filters");
    			}
    		}
    		//System.out.println("--------------------- "+ii);
    	return ii;
    }
    
    public static String getTrnMessage(String keyTrn) {
        Session session = null;
        Collection funding = new ArrayList();
        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select f.message from "
                + Message.class.getName()
                + " f "
                + "where (f.key=:keyTrn)";
            Query qry = session.createQuery(queryString);
            qry.setParameter("keyTrn", keyTrn, Hibernate.STRING);
            funding = qry.list();

        } catch (Exception ex) {
            logger.error("Unable to get funding :" + ex);
        }
        String s = null;
        if (funding != null)
            if (!funding.isEmpty())
                s = ( (String) (funding.iterator().next()));
        //////System.out.println("aaaaaaaaaaaaa"+s);
        return s;
    }

    public static AmpActivityInternalId getActivityInternalId(Long actId,
        Long orgId) {
        Session session = null;
        AmpActivityInternalId internalId = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select a from "
                + AmpActivityInternalId.class.getName() + " a "
                + "where (a.ampActivityId=:actId) and (a.ampOrgId=:orgId)";
            Query qry = session.createQuery(queryString);
            qry.setParameter("actId", actId, Hibernate.LONG);
            qry.setParameter("orgId", orgId, Hibernate.LONG);
            Iterator itr = qry.list().iterator();

            if (itr.hasNext()) {
                internalId = (AmpActivityInternalId) itr.next();
            }
        } catch (Exception ex) {
            logger.error("Unable to get Activity Internal Id :" + ex);
        }
        return internalId;
    }

    public static Collection getOrganizations(Long actId, String orgCode) {
        Session session = null;
        Collection orgs = new ArrayList();

        try {
            session = PersistenceManager.getRequestDBSession();
            AmpActivity activity = (AmpActivity) session.load(AmpActivity.class, actId);
            Set set = activity.getOrgrole();
            Iterator itr1 = set.iterator();
            while (itr1.hasNext()) {
                AmpOrgRole orgRole = (AmpOrgRole) itr1.next();
                if (orgRole.getRole().getRoleCode().equals(orgCode)) {
                    if (!orgs.contains(orgRole.getOrganisation())) {
                        orgs.add(orgRole.getOrganisation());
                    }
                }
            }
        } catch (Exception ex) {
            logger.error("Unable to get Organizations :" + ex);
        }
        return orgs;
    }

    public static Collection getActivityDocuments(Long id) {
        Session session = null;
        Collection docs = new ArrayList();

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select a from " + AmpActivity.class.getName()
                + " a " + "where (a.ampActivityId=:id)";
            Query qry = session.createQuery(queryString);
            qry.setParameter("id", id, Hibernate.LONG);
            Iterator itr = qry.list().iterator();
            if (itr.hasNext()) {
                AmpActivity activity = (AmpActivity) itr.next();
                Set set = activity.getDocuments();
                Iterator itr1 = set.iterator();
                while (itr1.hasNext()) {
                    CMSContentItem cmsItem = (CMSContentItem) itr1.next();
                    docs.add(cmsItem);
                }
            }
        } catch (Exception ex) {
            logger.error("Unable to get ActivityDocuments :" + ex);
        }
        return docs;
    }

    public static Collection getKnowledgeDocuments(Long id) {
        Session session = null;
        Collection docs = new ArrayList();

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select a from " + AmpActivity.class.getName()
                + " a " + "where (a.ampActivityId=:id)";
            Query qry = session.createQuery(queryString);
            qry.setParameter("id", id, Hibernate.LONG);
            Iterator itr = qry.list().iterator();
            if (itr.hasNext()) {
                AmpActivity activity = (AmpActivity) itr.next();
                Set set = activity.getDocuments();
                Iterator itr1 = set.iterator();
                while (itr1.hasNext()) {
                    CMSContentItem cmsItem = (CMSContentItem) itr1.next();
                    Documents document = new Documents();
                    document.setActivityId(activity.getAmpActivityId());
                    document.setActivityName(activity.getName());
                    document.setDocId(new Long(cmsItem.getId()));
                    document.setTitle(cmsItem.getTitle());
                    document.setIsFile(cmsItem.getIsFile());
                    document.setFileName(cmsItem.getFileName());
                    document.setUrl(cmsItem.getUrl());
                    document.setDocDescription(cmsItem.getDescription());
                    document.setDate(cmsItem.getDate());
                    if (cmsItem.getDocType() != null)
                    	document.setDocType(cmsItem.getDocType().getValue());

                    if (cmsItem.getDocLanguage() != null)
                    	document.setDocLanguage( cmsItem.getDocLanguage().getValue() );
                    document.setDocComment( cmsItem.getDocComment() );

                    logger.debug("Doc Desc :" + document.getDocDescription());
                    docs.add(document);
                }
            }
        } catch (Exception ex) {
            logger.error("Unable to get ActivityDocuments :" + ex);
        }
        return docs;
    }

    public static Collection getAllDocuments(Long teamId) {
        Session session = null;
        Collection col = new ArrayList();

        try {
            session = PersistenceManager.getRequestDBSession();
            String qryStr = "select act from " + AmpActivity.class.getName()
                + " act";
            qryStr += " where (act.team=:team)";
            Query qry = session.createQuery(qryStr);
            qry.setParameter("team", teamId, Hibernate.LONG);
            Iterator itr1 = qry.list().iterator();
            while (itr1.hasNext()) {
                AmpActivity act = (AmpActivity) itr1.next();
                Set docs = act.getDocuments();
                if (docs != null) {
                    Iterator itr2 = docs.iterator();
                    while (itr2.hasNext()) {
                        CMSContentItem cmsItem = (CMSContentItem) itr2.next();
                        Documents document = new Documents();
                        document.setActivityId(act.getAmpActivityId());
                        document.setActivityName(act.getName());
                        document.setDocId(new Long(cmsItem.getId()));
                        document.setTitle(cmsItem.getTitle());
                        document.setIsFile(cmsItem.getIsFile());
                        document.setFileName(cmsItem.getFileName());
                        document.setUrl(cmsItem.getUrl());
                        document.setDocDescription(cmsItem.getDescription());
                        document.setDate(cmsItem.getDate());
                        col.add(document);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Cannot get All documents :" + e);
        }

        return col;
    }

    public static AmpRole getAmpRole(String roleCode) {
        Session session = null;
        AmpRole role = null;

        try {
            session = PersistenceManager.getRequestDBSession();

            String queryString = "select r from " + AmpRole.class.getName()
                + " r " + "where (r.roleCode=:code)";
            Query qry = session.createQuery(queryString);
            qry.setParameter("code", roleCode, Hibernate.STRING);
            Iterator itr = qry.list().iterator();
            while (itr.hasNext())
                role = (AmpRole) itr.next();

        } catch (Exception e) {
            logger.error("Uanble to get role :" + e);
        }
        return role;
    }

    public static Object get(Class c, Long id) {
        Session session = null;
        Object o = null;

        try {
            session = PersistenceManager.getSession();
            o = session.load(c, id);

        } catch (Exception e) {
            logger.error("Uanble to get object of class " + c.getName() + " width id=" + id + ". Error was:" + e);
        } finally {

            try {
                PersistenceManager.releaseSession(session);
            } catch (Exception ex) {
                logger.error("releaseSession() failed " + ex);
            }
        }
        return o;
    }


    public static ArrayList getOrgRole(Long id) {
        ArrayList list = new ArrayList();
        StringBuffer RAOrg = new StringBuffer();
        StringBuffer DNOrg = new StringBuffer();
        StringBuffer IAOrg = new StringBuffer();
        StringBuffer RLOrg = new StringBuffer();
        Iterator iter = null;

        Session session = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            // modified by Priyajith
            // desc:used select query instead of session.load
            // start
            String queryString = "select a from " + AmpActivity.class.getName()
                + " a " + "where (a.ampActivityId=:id)";
            Query qry = session.createQuery(queryString);
            qry.setParameter("id", id, Hibernate.LONG);
            Iterator itr = qry.list().iterator();
            AmpActivity ampActivity = null;
            while (itr.hasNext()) {
                ampActivity = (AmpActivity) itr.next();
            }
            // end
            iter = ampActivity.getOrgrole().iterator();
            while (iter.hasNext()) {
                AmpOrgRole ampOrgRole = (AmpOrgRole) iter.next();
                if (ampOrgRole.getRole().getRoleCode().equals(
                    Constants.REPORTING_AGENCY)) {
                    if (RAOrg.length() == 0)
                        RAOrg.append(ampOrgRole.getOrganisation().getName());
                    else
                        RAOrg.append(",").append(
                            ampOrgRole.getOrganisation().getName());
                }
                if (ampOrgRole.getRole().getRoleCode().equals(
                    Constants.FUNDING_AGENCY)) {
                    if (DNOrg.length() == 0)
                        DNOrg.append(ampOrgRole.getOrganisation().getName());
                    else
                        DNOrg.append(",").append(
                            ampOrgRole.getOrganisation().getName());
                }
                if (ampOrgRole.getRole().getRoleCode().equals(
                    Constants.IMPLEMENTING_AGENCY)) {
                    if (IAOrg.length() == 0)
                        IAOrg.append(ampOrgRole.getOrganisation().getName());
                    else
                        IAOrg.append(",").append(
                            ampOrgRole.getOrganisation().getName());
                }
                if (ampOrgRole.getRole().getRoleCode().equals(
                    Constants.RELATED_INSTITUTIONS)) {
                    if (RLOrg.length() == 0)
                        RLOrg.append(ampOrgRole.getOrganisation().getName());
                    else
                        RLOrg.append(",").append(
                            ampOrgRole.getOrganisation().getName());
                }
                logger.debug("Organisation :"
                             + ampOrgRole.getOrganisation().getName());
                logger.debug("Role Code : "
                             + ampOrgRole.getRole().getRoleCode());
            }
            list.add(RAOrg.toString());
            list.add(DNOrg.toString());
            list.add(IAOrg.toString());
            list.add(RLOrg.toString());

            logger.debug("Funding Country/Agency" + RAOrg.toString());
            logger.debug("Reporting Country/Agency" + DNOrg.toString());
            logger.debug("Implementing Agency" + IAOrg.toString());
            logger.debug("Related Institution" + RLOrg.toString());

        } catch (Exception ex) {
            logger.error("Unable to get Amp Org Role " + ex.getMessage());
        }
        return list;
    }

    public static AmpOrganisation getOrganisation(Long id) {
        Session session = null;
        AmpOrganisation org = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            // modified by Priyajith
            // desc:used select query instead of session.load
            // start
            String queryString = "select o from "
                + AmpOrganisation.class.getName() + " o "
                + "where (o.ampOrgId=:id)";
            Query qry = session.createQuery(queryString);
            qry.setParameter("id", id, Hibernate.LONG);
            Iterator itr = qry.list().iterator();
            while (itr.hasNext()) {
                org = (AmpOrganisation) itr.next();
            }
            // end

        } catch (Exception ex) {
            logger.error("Unable to get organisation from database", ex);
        }
        logger.debug("Getting organisation successfully ");
        return org;
    }

    public static ArrayList getAmpComponent(Long ampActivityId) {
        ArrayList component = new ArrayList();
        Query q = null;
        Session session = null;
        String queryString = null;
        AmpComponent ampComponent = null;
        Iterator iter = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            queryString = " select c from " + AmpComponent.class.getName()
                + " c where (c.activity.ampActivityId=:ampActivityId )";
            q = session.createQuery(queryString);
            q.setParameter("ampActivityId", ampActivityId, Hibernate.LONG);
            iter = q.list().iterator();

            while (iter.hasNext()) {
                ampComponent = (AmpComponent) iter.next();
                component.add(ampComponent);
            }
        } catch (Exception ex) {
            logger.error("Unable to get Amp PhysicalPerformance", ex);
            //////System.out.println(ex.toString()) ;
        }
        logger.debug("Getting components executed successfully "
                     + component.size());
        return component;
    }

    public static ArrayList getAmpComponents() {
        ArrayList component = new ArrayList();
        Query q = null;
        Session session = null;
        String queryString = null;
        AmpComponent ampComponent = null;
        Iterator iter = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            queryString = "FROM " + AmpComponent.class.getName();

            q = session.createQuery(queryString);
            iter = q.list().iterator();

            while (iter.hasNext()) {
                ampComponent = (AmpComponent) iter.next();
                component.add(ampComponent);
            }
        } catch (Exception ex) {
            logger.error("Unable to get Amp Components", ex);
        }
        logger.debug("Getting components executed successfully "
                     + component.size());
        return component;
    }

    public static ArrayList getAmpPhysicalProgress(Long ampActivityId,Long componentId) {
        ArrayList progress = new ArrayList();
        Query q = null;
        Session session = null;
        String queryString = null;
        AmpPhysicalPerformance ampPhysicalPerformance = null;
        Iterator iter = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            queryString = " select Progress from "
                + AmpPhysicalPerformance.class.getName()
                + " Progress where (Progress.ampActivityId=:ampActivityId )";
                if(componentId!=null){
              queryString+= " and (Progress.component=:componentId) ";
            }
            q = session.createQuery(queryString);
            q.setParameter("ampActivityId", ampActivityId, Hibernate.LONG);
                if(componentId!=null){
                  q.setLong("componentId", componentId);
                }
            iter = q.list().iterator();

            while (iter.hasNext()) {

                ampPhysicalPerformance = (AmpPhysicalPerformance) iter.next();
                logger.debug("Title :"
                             + (String) ampPhysicalPerformance.getTitle());
                logger.debug("DESCRIPTION :"
                             + (String) ampPhysicalPerformance.getDescription());
                progress.add(ampPhysicalPerformance);
            }
        } catch (Exception ex) {
            logger.error("Unable to get Amp PhysicalPerformance", ex);
            //////System.out.println(ex.toString()) ;
        }
        logger
            .debug("Getting funding Executed successfully "
                   + progress.size());
        return progress;
    }

    public static Collection getAmpFunding(Long ampActivityId, Long ampFundingId) {
        Session session = null;
        Query q = null;
        Collection ampFundings = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = new String();
            queryString = "select f from "
                + AmpFunding.class.getName()
                + " f where (f.ampActivityId=:ampActivityId ) and (f.ampFundingId=:ampFundingId)";
            q = session.createQuery(queryString);
            q.setParameter("ampActivityId", ampActivityId, Hibernate.LONG);
            q.setParameter("ampFundingId", ampFundingId, Hibernate.LONG);
            ampFundings = q.list();
            logger
                .debug("DbUtil : getAmpFunding() returning collection of size  "
                       + ampFundings.size());

        } catch (Exception ex) {
            logger.error("Unable to get AmpFunding collection from database",
                         ex);
        }

        return ampFundings;
    }

    public static List<AmpFunding> getAmpFunding(Long ampActivityId) {
        logger.debug("getAmpFunding() with ampActivityId=" + ampActivityId);
        Session session = null;
        Query q = null;
        List<AmpFunding> ampFundings = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = new String();
            queryString = "select f from " + AmpFunding.class.getName()
                + " f where (f.ampActivityId=:ampActivityId) ";
            q = session.createQuery(queryString);
            q.setParameter("ampActivityId", ampActivityId, Hibernate.LONG);
            ampFundings = q.list();
        } catch (Exception ex) {
            logger.error("Unable to get AmpFunding collection from database",
                         ex);
        }
        logger
            .debug("DbUtil : getAmpFunding(ampActivityId) returning collection of size  "
                   + (ampFundings != null ? ampFundings.size() : 0));
        return ampFundings;
    }

    /**
     *  Return total amount using the exchange for each funding according with funding date
     * @param ampFundingId
     * @param transactionType
     * @param adjustmentType
     * @param currcode
     * @return
     */
    public static DecimalWraper getTotalDonorFunding(Long ampFundingId,
           Integer transactionType, Integer adjustmentType, String currcode) {
    	   Session session = null;
           Query q = null;
           List list = null;
           Iterator iter = null;
           double fromrate;
           double torate;
           DecimalWraper total = new DecimalWraper();
           try {
               session = PersistenceManager.getRequestDBSession();
               String queryString = new String();
               queryString = "select from "
                   + AmpFundingDetail.class.getName()
                   + " f where (f.ampFundingId=:ampFundingId) "
                   + " and (f.transactionType=:transactionType) "
                   + " and (f.adjustmentType=:adjustmentType)";
               q = session.createQuery(queryString);
               q.setParameter("ampFundingId", ampFundingId, Hibernate.LONG);
               q.setParameter("transactionType", transactionType,
                              Hibernate.INTEGER);
               q.setParameter("adjustmentType", adjustmentType, Hibernate.INTEGER);
               list = q.list();
               if (list.size() != 0) {
                   iter = list.iterator();
                   while (iter.hasNext()) {
                       AmpFundingDetail fundingdetails = (AmpFundingDetail)iter.next();
                       fromrate = Util.getExchange(fundingdetails.getAmpCurrencyId().getCurrencyCode(),new java.sql.Date(fundingdetails.getTransactionDate().getTime()));
                       torate = Util.getExchange(currcode,new java.sql.Date(fundingdetails.getTransactionDate().getTime()));
                       if (total.getValue() == null){
                    	   total = CurrencyWorker.convertWrapper(fundingdetails.getTransactionAmount(),fromrate, torate,new java.sql.Date(fundingdetails.getTransactionDate().getTime()));
                       }
                       else
                       {
                    	  DecimalWraper tmp = CurrencyWorker.convertWrapper(fundingdetails.getTransactionAmount(),fromrate, torate,new java.sql.Date(fundingdetails.getTransactionDate().getTime()));
                    	  total.setValue(tmp.getValue().add(total.getValue()));
                    	  total.setCalculations(tmp.getCalculations()+ " + " + total.getCalculations());
                       }
                      }
               }
           } catch (Exception ex) {
               logger.error("Unable to get sum of funds from database", ex);
           }
				return total;
    }

    public static double getTotalDonorFund(Long ampFundingId,
                                           Integer transactionType, Integer adjustmentType) {

        logger.debug("getTotalDonorFund() with ampFundingId " + ampFundingId
                     + " transactionType " + transactionType + " adjustmentType "
                     + adjustmentType);

        Session session = null;
        Query q = null;
        List list = null;
        Iterator iter = null;
        Double total = new Double(0.0);
        ;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = new String();
            queryString = "select (sum(f.thousandsTransactionAmount) from "
                + AmpFundingDetail.class.getName()
                + " f where (f.ampFundingId=:ampFundingId) "
                + " and (f.transactionType=:transactionType) "
                + " and (f.adjustmentType=:adjustmentType) group by f.ampFundingId";
            q = session.createQuery(queryString);
            q.setParameter("ampFundingId", ampFundingId, Hibernate.LONG);
            q.setParameter("transactionType", transactionType,
                           Hibernate.INTEGER);
            q.setParameter("adjustmentType", adjustmentType, Hibernate.INTEGER);
            list = q.list();
            if (list.size() != 0) {
                iter = list.iterator();
                while (iter.hasNext()) {
                    total = (Double) iter.next();
                }
            }
        } catch (Exception ex) {
            logger.error("Unable to get sum of funds from database", ex);
        }

        logger.debug("getTotalDonorFund() total : " + total);
        return total.doubleValue();
    }

    public static Collection getYearlySum(Long ampFundingId,
                                          Integer transactionType, Integer adjustmentType) {
        logger.debug("getYearlySum() with ampFundingId="
                     + ampFundingId.longValue() + " transactionType="
                     + transactionType.longValue());
        Session session = null;
        Query q = null;
        Collection c = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = new String();
            queryString = "select sum(f.thousandsTransactionAmount) from "
                + AmpFundingDetail.class.getName()
                + " f where (f.ampFundingId=:ampFundingId) "
                + " and (f.transactionType=:transactionType) "
                + " and (f.adjustmentType=:adjustmentType) group by f.fiscalYear";
            q = session.createQuery(queryString);
            q.setParameter("ampFundingId", ampFundingId, Hibernate.LONG);
            q.setParameter("transactionType", transactionType,
                           Hibernate.INTEGER);
            q.setParameter("adjustmentType", adjustmentType, Hibernate.INTEGER);

            c = q.list();
        } catch (Exception ex) {
            logger
                .error(
                    "Unable to get planned commitment by fiscal year from database",
                    ex);
        }

        logger.debug("getYearlySum() collection size returned : "
                     + (c != null ? c.size() : 0));
        return c;
    }

    public static Collection getFiscalYears(Long ampFundingId,
                                            Integer transactionType) {
        logger.debug("getFiscalYears() with ampFundingId="
                     + ampFundingId.longValue() + " transactionType="
                     + transactionType.intValue());

        Session session = null;
        Query q = null;
        Collection c = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = new String();
            queryString = "select f.fiscalYear from "
                + AmpFundingDetail.class.getName()
                + " f where (f.ampFundingId=:ampFundingId) "
                + " and (f.transactionType=:transactionType) "
                + " group by f.fiscalYear";
            q = session.createQuery(queryString);
            q.setParameter("ampFundingId", ampFundingId, Hibernate.LONG);
            q.setParameter("transactionType", transactionType,
                           Hibernate.INTEGER);

            c = q.list();
        } catch (Exception ex) {
            logger.error("Unable to get  fiscal years from database", ex);
        }

        logger.debug("getFiscalYears() collection size returned : "
                     + (c != null ? c.size() : 0));
        return c;
    }

    public static AmpComponent getAmpComponentDescription(Long cid) {
        Query q = null;
        Session session = null;
        String queryString = null;
        AmpComponent comp = null;
        Iterator iter = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            queryString = " select c from " + AmpComponent.class.getName()
                + " c where (c.ampComponentId=:cid )";
            q = session.createQuery(queryString);
            q.setParameter("cid", cid, Hibernate.LONG);
            iter = q.list().iterator();

            if (iter.hasNext()) {

                comp = (AmpComponent) iter.next();
                logger.debug("Title :" + (String) comp.getTitle());
                logger.debug("DESCRIPTION :" + (String) comp.getDescription());
            }
        } catch (Exception ex) {
            logger.error("Unable to get Amp Component", ex);
            //////System.out.println(ex.toString()) ;
        }
        logger.debug("Getting Amp Component Executed successfully ");
        return comp;
    }

    public static AmpPhysicalPerformance getAmpPhysicalProgressDescription(
        Long ampPpId) {
        Query q = null;
        Session session = null;
        String queryString = null;
        AmpPhysicalPerformance ampPhysicalPerformance = null;
        Iterator iter = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            queryString = " select Progress from "
                + AmpPhysicalPerformance.class.getName()
                + " Progress where (Progress.ampPpId=:ampPpId )";
            q = session.createQuery(queryString);
            q.setParameter("ampPpId", ampPpId, Hibernate.LONG);
            iter = q.list().iterator();

            if (iter.hasNext()) {

                ampPhysicalPerformance = (AmpPhysicalPerformance) iter.next();
                logger.debug("Title :"
                             + (String) ampPhysicalPerformance.getTitle());
                logger.debug("DESCRIPTION :"
                             + (String) ampPhysicalPerformance.getDescription());
                //			progress.add(ampPhysicalPerformance);
            }
        } catch (Exception ex) {
            logger.error("Unable to get Amp PhysicalPerformance", ex);
            //////System.out.println(ex.toString()) ;
        }
        logger.debug("Getting funding Executed successfully ");
        return ampPhysicalPerformance;
    }

    public static Collection getCreatedOrEditedActivities(Long ampTeamId) {
        Collection actList = new ArrayList();
        Session session = null;
        Query q = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select act from " + AmpActivity.class.getName()
                + " act where (act.team=:ampTeamId)"
                + " and (act.approvalStatus in ("+ Constants.ACTIVITY_NEEDS_APPROVAL_STATUS +") ) "
                + " and act.draft != :draftValue" ;
            q = session.createQuery(queryString);
            q.setParameter("ampTeamId", ampTeamId, Hibernate.LONG);
            q.setBoolean("draftValue", true);

            actList = q.list();

        } catch (Exception ex) {
            logger.error("Unable to get AmpActivity [getCreatedOrEditedActivities()]", ex);
        }
        logger.debug("Getting CreatedOrEdited activities Executed successfully ");
        return actList;
    }

    public static String getActivityApprovalStatus(Long actId) {
        Session session = null;
        Query q = null;
        String ans = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            String qry = "select act from " + AmpActivity.class.getName() + " act where act.ampActivityId=:actId";
            q = session.createQuery(qry);
            q.setParameter("actId", actId, Hibernate.LONG);
            Iterator itr = q.list().iterator();
            while (itr.hasNext()) {
                AmpActivity act = (AmpActivity) itr.next();
                ans = act.getApprovalStatus();
            }
        } catch (Exception ex) {
            logger.error("Unable to get AmpActivity [getActivityApprovalStatus()]", ex);
        }
        logger.debug("getActivityApprovalStatus Executed successfully ");
        return ans;
    }

    public static Collection getApprovedOrCreatorActivities(Long ampTeamId, Long ampTeamMemId) {
        Collection actList = new ArrayList();
        Session session = null;
        Query q = null;
        String queryString;
        try {
            session = PersistenceManager.getRequestDBSession();
            /*
                if (new Long(0).equals(ampTeamId)) {  // for management workspace
             queryString = "select act.ampActivityId from " + AmpActivity.class.getName()
               + " act where (act.approvalStatus=:status1 or act.approvalStatus=:status2)";
             q = session.createQuery(queryString);
                }
                else {								// for regular working team
             */
            queryString = "select act from " + AmpActivity.class.getName()
                + " act where (act.team=:ampTeamId)"
                + " and ( act.activityCreator=:ampTeamMemId "
                + " or act.approvalStatus=:status1 or act.approvalStatus=:status2)";
            q = session.createQuery(queryString);
            q.setParameter("ampTeamId", ampTeamId, Hibernate.LONG);
            q.setParameter("ampTeamMemId", ampTeamMemId, Hibernate.LONG);
            /*	} */
            q.setParameter("status1", "approved", Hibernate.STRING);
            q.setParameter("status2", "edited", Hibernate.STRING);
            actList = q.list();

        } catch (Exception ex) {
            logger.error("Unable to get AmpActivity [getApprovedOrCreatorActivities()]", ex);
        }
        logger.debug("Getting ApprovedOrCreator activities Executed successfully ");
        return actList;
    }

    /**
     * get AmpFunding by ampFundingId
     *
     * @param ampFundingId
     * @return
     */
    public static AmpFunding getAmpFundingById(Long ampFundingId) {
        logger.debug("getAmpFundingById() with ampFundingId="
                     + ampFundingId.longValue());
        Session session = null;
        Query q = null;
        AmpFunding ampFunding = null;
        Iterator iter = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = new String();
            queryString = "select f from " + AmpFunding.class.getName()
                + " f where (f.ampFundingId=:ampFundingId) ";
            q = session.createQuery(queryString);
            q.setParameter("ampFundingId", ampFundingId, Hibernate.LONG);
            iter = q.list().iterator();
            if (iter.hasNext()) {
                ampFunding = (AmpFunding) iter.next();
            }
        } catch (Exception ex) {
            logger.error("Unable to get AmpFunding from database", ex);
        }
        logger
            .debug("DbUtil : getAmpFundingById(ampFundingId) returning ampFunding  "
                   + (ampFunding != null ? ampFunding.getFinancingId()
                      : "null"));

        return ampFunding;
    }

    public static Collection getQuarterlyDataForProjections(Long ampFundingId, int adjustmentType) {
    	logger.debug("getQuarterlyDataForProjections with ampFundingId " + ampFundingId
    			+ " adjustmentType " + adjustmentType);

    	Session session = null;
    	Query q = null;
    	Collection c = null;
    	Integer adjType = new Integer(adjustmentType);

    	try {
    		session = PersistenceManager.getRequestDBSession();
    		String queryString = new String();
    		queryString = "select p.amount,"
    			+ "p.projectionDate, p.ampCurrency, p.projected from "
    			+ AmpFundingMTEFProjection.class.getName()
    			+ " p where (p.ampFunding=:ampFundingId) "
    			+ " and (p.projected=:adjType) order by p.projectionDate ";
    		q = session.createQuery(queryString);
    		q.setParameter("ampFundingId", ampFundingId, Hibernate.LONG);
    		q.setParameter("adjType", adjType, Hibernate.INTEGER);
    		c = q.list();
    	} catch (Exception ex) {
    		logger.error("Unable to get quarterly data from database", ex);

    	}

    	logger.debug("getQuarterlyDataForProjections() returning a list of size : "
    			+ c.size());
    	return c;
    }

    /**
     * @author jose Returns a collection of records from amp_funding_detail
     *              based on below
     * @param ampFundingId
     * @param transactionType
     *                 commitment=0,disbursement=1,expenditure=2
     * @param adjustmentType
     *                 planned=0,actual=1
     * @return Collection
     */
    public static Collection getQuarterlyData(Long ampFundingId,
                                              int transactionType, int adjustmentType) {
        logger.debug("getQuarterlyData with ampFundingId " + ampFundingId
                     + " transactionType "
                     + transactionType + " adjustmentType " + adjustmentType);

        Session session = null;
        Query q = null;
        Collection c = null;
        Integer trsType = new Integer(transactionType);
        Integer adjType = new Integer(adjustmentType);

        if (transactionType == Constants.MTEFPROJECTION ) {
        	return getQuarterlyDataForProjections(ampFundingId, adjustmentType);
        }

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = new String();
            queryString = "select f.thousandsTransactionAmount,"
                + "f.transactionDate,f.ampCurrencyId, f.fixedExchangeRate from "
                + AmpFundingDetail.class.getName()
                + " f where (f.ampFundingId=:ampFundingId) "
                + " and (f.transactionType=:trsType) "
                + " and (f.adjustmentType=:adjType) order by f.transactionDate ";
            q = session.createQuery(queryString);
            q.setParameter("ampFundingId", ampFundingId, Hibernate.LONG);
            q.setParameter("trsType", trsType, Hibernate.INTEGER);
            q.setParameter("adjType", adjType, Hibernate.INTEGER);
            c = q.list();
        } catch (Exception ex) {
            logger.error("Unable to get quarterly data from database", ex);

        }

        logger.debug("getQuarterlyData() returning a list of size : "
                     + c.size());
        return c;
    }


    /*
     * @author Priyajith C
     */
    // Retreives all organisation;
    public static Collection getAllOrganisation() {
        Session session = null;
        Query qry = null;
        Collection organisation = new ArrayList();

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select o from "
                + AmpOrganisation.class.getName() + " o order by name asc";
            qry = session.createQuery(queryString);
            organisation = qry.list();
        } catch (Exception e) {
            logger.error("Unable to get all organisations");
            logger.debug("Exceptiion " + e);
        }
        return organisation;
    }

    public static Collection getAllFisCalenders() {
        Session session = null;
        Query qry = null;
        Collection fisCals = new ArrayList();

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select f from "
                + AmpFiscalCalendar.class.getName() + " f";
            qry = session.createQuery(queryString);
            fisCals = qry.list();
        } catch (Exception e) {
            logger.error("Unable to get all fiscal calendars");
            logger.debug("Exceptiion " + e);
        }
        return fisCals;
    }

    public static Long getBaseFiscalCalendar() {
        Session session = null;
        Query qry = null;
        Long fid = new Long(4);

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select f from " + AmpFiscalCalendar.class.getName()
                + " where (f.startMonthNum=:start) and (f.startDayNum=:start) and (f.yearOffset=:offset)";
            qry = session.createQuery(queryString);
            qry.setParameter("start", new Integer(1), Hibernate.INTEGER);
            qry.setParameter("offset", new Integer(0), Hibernate.INTEGER);
            if (null != qry.list())
                fid = ( (AmpFiscalCalendar) qry.list().get(0)).getAmpFiscalCalId();
        } catch (Exception ex) {
            logger.error("Unable to get base fiscal calendar" + ex);
            ex.printStackTrace(System.out);
        }
        return fid;
    }
    public static AmpFiscalCalendar getGregorianCalendar() {
	Session session = null;
	Query qry = null;
	AmpFiscalCalendar calendar = null;

	try {
	    session = PersistenceManager.getRequestDBSession();
	    String queryString = "select f from " + AmpFiscalCalendar.class.getName() + " f " +
	    		"where f.baseCal=:baseCalParam order by f.name";
	    qry = session.createQuery(queryString);
	    qry.setParameter("baseCalParam", BaseCalendar.BASE_GREGORIAN.getValue(),Hibernate.STRING);

	    if (qry.list() != null)
		calendar = ((AmpFiscalCalendar) qry.list().get(0));
	} catch (Exception ex) {
	    logger.error("Unable to get fiscal calendar" + ex);
	}
	return calendar;
    }
    public static Collection getAllActivities() {
        Session session = null;
        Query qry = null;
        Collection activities = new ArrayList();
        try {
            session = PersistenceManager.getSession();
            String queryString = "select f from " + AmpActivity.class.getName()
                + " f";
            qry = session.createQuery(queryString);
            activities = qry.list();
        } catch (Exception e) {
            logger.error("Unable to get all activities");
            logger.debug("Exceptiion " + e);
        } finally {
            try {
                PersistenceManager.releaseSession(session);
            } catch (HibernateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        return activities;
    }

    public static Collection getAllOrgActivities(Long orgId) {
        Session session = null;
        Query qry = null;
        Collection activities = new ArrayList();
        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select f from " + AmpActivity.class.getName()
                + " f where (f.internalId=:orgId)";
            qry = session.createQuery(queryString);
            activities = qry.list();
        } catch (Exception e) {
            logger.error("Unable to get all activities");
            logger.debug("Exceptiion " + e);
        }
        return activities;
    }

    public static AmpFiscalCalendar getAmpFiscalCalendar(Long ampFisCalId) {
        Session session = null;
        Query qry = null;
        AmpFiscalCalendar ampFisCal = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select f from "
                + AmpFiscalCalendar.class.getName()
                + " f where (f.ampFiscalCalId=:ampFisCalId)";
            qry = session.createQuery(queryString);
            qry.setParameter("ampFisCalId", ampFisCalId, Hibernate.LONG);
            Iterator itr = qry.list().iterator();
            if (itr.hasNext()) {
                ampFisCal = (AmpFiscalCalendar) itr.next();
            }
        } catch (Exception e) {
            logger.error("Unable to get fiscalCalendar");
            logger.debug("Exceptiion " + e);
        }
        return ampFisCal;
    }

    public static User getUser(String email) {
        Session session = null;
        Query qry = null;
        User user = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select u from " + User.class.getName()
                + " u where (u.email=:email)";
            qry = session.createQuery(queryString);
            qry.setParameter("email", email, Hibernate.STRING);
            Iterator itr = qry.list().iterator();
            if (itr.hasNext()) {
                user = (User) itr.next();
            }
        } catch (Exception e) {
            logger.error("Unable to get user");
            logger.debug("Exceptiion " + e);
        }
        return user;
    }

    public static User getUser(Long userId) {
        Session session = null;
        Query qry = null;
        User user = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select u from " + User.class.getName()
                + " u where (u.id=:userId)";
            qry = session.createQuery(queryString);
            qry.setParameter("userId", userId, Hibernate.LONG);
            Iterator itr = qry.list().iterator();
            if (itr.hasNext()) {
                user = (User) itr.next();
            }
        } catch (Exception e) {
            logger.error("Unable to get user");
            logger.debug("Exceptiion " + e);
        }
        return user;
    }

    /**
     * @author Arty
     * @param reportId
     * Sets the the defaultTeamReport to null for all the
     * AppSettings that were referencing the
     */
    public static void updateAppSettingsReportDeleted(Long reportId) {
        Session session = null;
        Query qry = null;
        AmpApplicationSettings ampAppSettings = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select a from "
                + AmpApplicationSettings.class.getName()
                + " a where (a.defaultTeamReport=:repId)";
            qry = session.createQuery(queryString);
            qry.setParameter("repId", reportId, Hibernate.LONG);
            Iterator itr = qry.list().iterator();
            while (itr.hasNext()) {
                ampAppSettings = (AmpApplicationSettings) itr.next();
                ampAppSettings.setDefaultTeamReport(null);
                update(ampAppSettings);
                //////System.out.println("Am updatat: " + ampAppSettings.getAmpAppSettingsId());
            }
        } catch (Exception e) {
            logger.error("Unable to get TeamAppSettings");
            logger.debug("Exceptiion " + e);
        }
    }

    public static AmpApplicationSettings getTeamAppSettings(Long teamId) {
        Session session = null;
        Query qry = null;
        AmpApplicationSettings ampAppSettings = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select a from "
                + AmpApplicationSettings.class.getName()
                + " a where (a.team=:teamId) and a.member is null";
            qry = session.createQuery(queryString);
            qry.setParameter("teamId", teamId, Hibernate.LONG);
            Iterator itr = qry.list().iterator();
            if (itr.hasNext()) {
                ampAppSettings = (AmpApplicationSettings) itr.next();
            }
            
        } catch (Exception e) {
        	e.printStackTrace();
            logger.error("Unable to get TeamAppSettings");
            logger.debug("Exceptiion " + e);
        }
        return ampAppSettings;
    }

    public static AmpApplicationSettings getTeamAppSettingsMemberNotNull(Long teamId) {
        Session session = null;
        Query qry = null;
        AmpApplicationSettings ampAppSettings = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select a from "
                + AmpApplicationSettings.class.getName()
                + " a where (a.team=:teamId) ";
            qry = session.createQuery(queryString);
            qry.setParameter("teamId", teamId, Hibernate.LONG);
            Iterator itr = qry.list().iterator();
            while (itr.hasNext()) {
                ampAppSettings = (AmpApplicationSettings) itr.next();
                if(ampAppSettings!=null) break;
            }
            
        } catch (Exception e) {
        	e.printStackTrace();
            logger.error("Unable to get TeamAppSettings");
            logger.debug("Exceptiion " + e);
        }
        return ampAppSettings;
    }
    
    public static boolean isUserTranslator(Long userId) {

        logger.debug("In isUserTranslator()");
        User user = null;
        Session session = null;
        boolean flag = false;
        try {
            session = PersistenceManager.getRequestDBSession();

            // modified by Priyajith
            // desc:used select query instead of session.load
            // start
            String queryString = "select u from " + User.class.getName()
                + " u " + "where (u.id=:id)";
            Query qry = session.createQuery(queryString);
            qry.setParameter("id", userId, Hibernate.LONG);
            Iterator itrTemp = qry.list().iterator();
            while (itrTemp.hasNext()) {
                user = (User) itrTemp.next();
            }
            // end

            Iterator itr = user.getGroups().iterator();
            if (!itr.hasNext()) {
                logger.debug("No groups");
            } while (itr.hasNext()) {
                Group grp = (Group) itr.next();
                logger.debug("Group key is " + grp.getKey());
                if (grp.getKey().trim().equals("TRN")) {
                    logger.debug("setting flag as true");
                    flag = true;
                    break;
                } else {
                    logger.debug("in else");
                }
            }
        } catch (Exception ex) {
            logger.error("Unable to get team member " + ex);
        }
        return flag;
    }

    public static AmpApplicationSettings getMemberAppSettings(Long memberId) {
        Session session = null;
        Query qry = null;
        AmpApplicationSettings ampAppSettings = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select a from "
                + AmpApplicationSettings.class.getName()
                + " a where (a.member=:memberId)";
            qry = session.createQuery(queryString);
            qry.setParameter("memberId", memberId, Hibernate.LONG);
            Iterator itr = qry.list().iterator();
            if (itr.hasNext()) {
                ampAppSettings = (AmpApplicationSettings) itr.next();
            }
        } catch (Exception e) {
            logger.error("Unable to get MemberAppSettings");
            logger.debug("Exceptiion " + e);
        }
        return ampAppSettings;
    }


    /*
     * Get all reports 
     * if 	tabs = null (all)
     * 		tab = true 	only get tabs
     * 		tab = false reports which aren't tabs
     */
    public static Collection getAllReports(Boolean tabs) {
        Session session = null;
        Query qry = null;
        Collection reports = new ArrayList();

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select r from " + AmpReports.class.getName() + " r";
            if (tabs != null) {
				if (tabs) {
					queryString += " where r.drilldownTab=true ";
				} else {
					queryString += " where r.drilldownTab=false ";
				}
			}            
            qry = session.createQuery(queryString);
            reports = qry.list();
        } catch (Exception e) {
            logger.error("Unable to get all reports");
            logger.debug("Exceptiion " + e);
        }
        return reports;
    }

    public static AmpReports getAmpReport(Long id) {
        AmpReports ampReports = null;
        Session session = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            // modified by Priyajith
            // desc:used select query instead of session.load
            // start
            String queryString = "select r from " + AmpReports.class.getName()
                + " r " + "where (r.ampReportId=:id)";
            Query qry = session.createQuery(queryString);
            qry.setParameter("id", id, Hibernate.LONG);
            Iterator itr = qry.list().iterator();
            while (itr.hasNext()) {
                ampReports = (AmpReports) itr.next();
            }
            // end
        } catch (Exception ex) {
            logger.error("Unable to get reports " + ex);
        }
        return ampReports;
    }
    
    public static AmpReportLog getAmpReportLog(Long report_id, Long member_id) {
    	AmpReportLog ampReportMemberLog = null;
        Session session = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select r from " + AmpReportLog.class.getName()
                + " r " + "where (r.report=:id and r.member=:member)";
            Query qry = session.createQuery(queryString);
            qry.setParameter("id", report_id, Hibernate.LONG);
            qry.setParameter("member", member_id, Hibernate.LONG);
            Iterator itr = qry.list().iterator();
            while (itr.hasNext()) {
            	ampReportMemberLog = (AmpReportLog) itr.next();
            }
            // end
        } catch (Exception ex) {
            logger.error("Unable to get reportmemberlog " + ex);
        }
        return ampReportMemberLog;
    }    

    public static Collection getMembersUsingReport(Long id) {

        Session session = null;
        Collection col = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            // modified by Priyajith
            // desc:used select query instead of session.load
            // start
            String queryString = "select r from " + AmpReports.class.getName()
                + " r " + "where (r.ampReportId=:id)";
            Query qry = session.createQuery(queryString);
            qry.setParameter("id", id, Hibernate.LONG);
            Iterator itrTemp = qry.list().iterator();
            AmpReports ampReports = null;
            while (itrTemp.hasNext()) {
                ampReports = (AmpReports) itrTemp.next();
            }
            // end
            Iterator itr = ampReports.getMembers().iterator();
            col = new ArrayList();
            while (itr.hasNext()) {
                col.add(itr.next());
            }
        } catch (Exception e) {
            logger.debug("Exception from getMembersUsingReport()");
            logger.debug(e.toString());
        }
        return col;
    }

    public static Collection getAllConfigurablePages(Long teamId) {
        Session session = null;
        Query qry = null;
        Collection pages = new ArrayList();
        String qryStr = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            qryStr = "select p from " + AmpPages.class.getName()
                + " p where p.pageCode in ('" + Constants.DESKTOP_PG_CODE
                + "','" + Constants.FINANCIAL_PG_CODE + "')";
            qry = session.createQuery(qryStr);
            pages = qry.list();

            qryStr = "select tr from " + AmpTeamReports.class.getName() + " tr " +
                "where (tr.team=:tId)";
            qry = session.createQuery(qryStr);
            qry.setParameter("tId", teamId, Hibernate.LONG);
            Iterator tempItr = qry.list().iterator();
            String params = "";
            while (tempItr.hasNext()) {
                AmpTeamReports tr = (AmpTeamReports) tempItr.next();
                if (params.length() > 0) {
                    params += ",";
                }
                params += "'" + tr.getReport().getName().replaceAll("'", "''") + "'";
            }
            if (params.length() > 0) {
                logger.info("Params :" + params);

                qryStr = "select p from " + AmpPages.class.getName() + " p " +
                    "where p.pageName in (" + params + ")";
                Iterator itr = session.createQuery(qryStr).list().iterator();
                while (itr.hasNext()) {
                    AmpPages p = (AmpPages) itr.next();
                    pages.add(p);
                }
            }

        } catch (Exception e) {
            logger.error("Unable to get all configurable pages");
            logger.error("Exceptiion is :" + e);
            e.printStackTrace(System.out);
        }
        return pages;
    }

    public static Collection getAllPageFilters(Long id) {

        Session session = null;
        Collection col = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            // modified by Priyajith
            // desc:used select query instead of session.load
            // start
            String queryString = "select p from " + AmpPages.class.getName()
                + " p " + "where (p.ampPageId=:id)";
            Query qry = session.createQuery(queryString);
            qry.setParameter("id", id, Hibernate.LONG);
            Iterator itrTemp = qry.list().iterator();
            AmpPages ampPage = null;
            while (itrTemp.hasNext()) {
                ampPage = (AmpPages) itrTemp.next();
            }
            // end
            Iterator itr = ampPage.getFilters().iterator();
            col = new ArrayList();
            while (itr.hasNext()) {
                col.add(itr.next());
            }
        } catch (Exception e) {
            logger.debug("Exception from getAllPageFilters()");
            logger.debug(e.toString());
        }
        return col;
    }

    public static AmpPages getAmpPage(Long pageId) {
        Session session = null;
        Query qry = null;
        AmpPages page = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            // modified by Priyajith
            // desc:used select query instead of session.load
            // start
            String queryString = "select p from " + AmpPages.class.getName()
                + " p " + "where (p.ampPageId=:id)";
            qry = session.createQuery(queryString);
            qry.setParameter("id", pageId, Hibernate.LONG);
            Iterator itrTemp = qry.list().iterator();
            while (itrTemp.hasNext()) {
                page = (AmpPages) itrTemp.next();
            }
            // end

            Iterator itr = qry.list().iterator();
            if (itr.hasNext()) {
                page = (AmpPages) itr.next();
            }
        } catch (Exception e) {
            logger.error("Unable to get AmpPage");
            logger.debug("Exceptiion " + e);
        }
        return page;
    }

    public static AmpFilters getAmpFilter(Long filterId) {
        Session session = null;
        Query qry = null;
        AmpFilters filter = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            // modified by Priyajith
            // desc:used select query instead of session.load
            // start
            String queryString = "select f from " + AmpFilters.class.getName()
                + " f " + "where (f.ampFilterId=:id)";
            qry = session.createQuery(queryString);
            qry.setParameter("id", filterId, Hibernate.LONG);
            Iterator itr = qry.list().iterator();
            while (itr.hasNext()) {
                filter = (AmpFilters) itr.next();
            }
            // end
        } catch (Exception e) {
            logger.error("Unable to get AmpFilter");
            logger.debug("Exceptiion " + e);
        }
        return filter;
    }

    public static ArrayList getTeamPageFilters(Long teamId, Long pageId) {
        Session session = null;
        ArrayList col = new ArrayList();

        try {
            session = PersistenceManager.getRequestDBSession();
            String qryStr = "select tpf.filter.ampFilterId from " +
                AmpTeamPageFilters.class.getName() + " tpf " +
                "where (tpf.team=:tId) and (tpf.page=:pId)";
            Query qry = session.createQuery(qryStr);
            qry.setParameter("tId", teamId, Hibernate.LONG);
            qry.setParameter("pId", pageId, Hibernate.LONG);

            Iterator itr = qry.list().iterator();
            while (itr.hasNext()) {
                col.add( (Long) itr.next());
            }
        } catch (Exception e) {
            logger.debug("Exception from getTeamPageFilters()");
            logger.debug(e.toString());
        }
        return col;
    }

    public static Collection getFilters(Long teamId, Long pageId) {
        Session session = null;
        Collection col = new ArrayList();

        try {
            session = PersistenceManager.getRequestDBSession();
            String qryStr = "select tpf.filter.ampFilterId from " +
                AmpTeamPageFilters.class.getName() + " tpf " +
                "where (tpf.team=:tId) and (tpf.page=:pId)";
            Query qry = session.createQuery(qryStr);
            qry.setParameter("tId", teamId, Hibernate.LONG);
            qry.setParameter("pId", pageId, Hibernate.LONG);

            Iterator itr = qry.list().iterator();
            while (itr.hasNext()) {
                Long fId = (Long) itr.next();
                AmpFilters filter = (AmpFilters) session.load(AmpFilters.class, fId);
                col.add(filter);
            }
        } catch (Exception e) {
            logger.debug("Exception from getFilters()");
            logger.debug(e.toString());
        }
        return col;
    }

    /**
     * Replaced by getAllAssistanceTypesFromCM() which uses the category manager
     * public static Collection getAllAssistanceTypes() {
        Session session = null;
        Collection col = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select ta from "
                + AmpTermsAssist.class.getName() + " ta ";
            Query qry = session.createQuery(queryString);
            col = qry.list();
        } catch (Exception e) {
            logger.debug("Exception from getAllAssistanceType()");
            logger.debug(e.toString());
        }
        return col;
    }*/
    /**
     * Replaces DbUtil.getAllAssistanceTypes()
     */
    public static Collection<AmpCategoryValue> getAllAssistanceTypesFromCM() {
    	return
    		CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.TYPE_OF_ASSISTENCE_KEY, null);
    }

    public static Collection getAll(Class object) {
        Session session = null;
        Collection col = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select from " + object.getName();
            Query qry = session.createQuery(queryString);
            col = qry.list();
        } catch (Exception e) {
            logger.debug("Exception from getAll()");
            e.printStackTrace();
        }
        return col;
    }


    public static Collection getAllCountries() {
        Session session = null;
        Collection col = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select c from " + Country.class.getName()
                + " c order by c.countryName asc";
            Query qry = session.createQuery(queryString);
            col = qry.list();
        } catch (Exception e) {
            logger.debug("Exception from getAllCountries()");
            logger.debug(e.toString());
        }
        return col;
    }

    public static Country getDgCountry(String iso) {
        Session session = null;
        Country country = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select c from " + Country.class.getName()
                + " c " + "where (c.iso=:iso)";
            Query qry = session.createQuery(queryString);
            qry.setParameter("iso", iso, Hibernate.STRING);
            Iterator itr = qry.list().iterator();
            while (itr.hasNext()) {
                country = (Country) itr.next();
            }

        } catch (Exception e) {
            logger.debug("Exception from getDgCountry()");
            logger.debug(e.toString());
        }
        return country;
    }

    public static Country getCountryByName(String name) {
        Session session = null;
        Country country = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select c from " + Country.class.getName()
                + " c " + "where (c.countryName=:name)";
            Query qry = session.createQuery(queryString);
            qry.setParameter("name", name, Hibernate.STRING);
            Iterator itr = qry.list().iterator();
            while (itr.hasNext()) {
                country = (Country) itr.next();
            }

        } catch (Exception e) {
            logger.debug("Exception from getDgCountry()");
            logger.debug(e.toString());
        }
        return country;
    }

    public static Country getDgCountry(Long id) {
        Session session = null;
        Country country = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select c from " + Country.class.getName()
                + " c " + "where (c.countryId=:id)";
            Query qry = session.createQuery(queryString);
            qry.setParameter("id", id, Hibernate.LONG);
            Iterator itr = qry.list().iterator();
            while (itr.hasNext()) {
                country = (Country) itr.next();
            }

        } catch (Exception e) {
            logger.debug("Exception from getDgCountry()");
            logger.debug(e.toString());
        }
        return country;
    }

/*    public static ArrayList getAmpModality() {
        Session session = null;
        Query q = null;
        AmpModality ampModality = null;
        ArrayList modality = new ArrayList();
        String queryString = null;
        Iterator iter = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            queryString = " select Modality from "
                + AmpModality.class.getName()
                + " Modality order by Modality.name";
            q = session.createQuery(queryString);
            iter = q.list().iterator();

            while (iter.hasNext()) {

                ampModality = (AmpModality) iter.next();
                modality.add(ampModality);
            }

        } catch (Exception ex) {

            logger
                .debug("Modality : Unable to get Amp Activity names  from database "
                       + ex.getMessage());

        }
        return modality;
    }*/

    public static ArrayList<AmpCategoryValue> getAmpModality() {
    	ArrayList<AmpCategoryValue> result	= new ArrayList<AmpCategoryValue> (
    		CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.FINANCING_INSTRUMENT_KEY, true)
    		);

    	return result;
    }

    /**
     * @deprecated Use getAmpStatusFromCM instead which uses the Category Manager
     */
    public static ArrayList getAmpStatus() {
        Session session = null;
        Query q = null;
        AmpStatus ampStatus = null;
        ArrayList status = new ArrayList();
        String queryString = null;
        Iterator iter = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            queryString = " select Status from " + AmpStatus.class.getName()
                + " Status order by Status.name";
            q = session.createQuery(queryString);
            iter = q.list().iterator();
            while (iter.hasNext()) {

                ampStatus = (AmpStatus) iter.next();
                status.add(ampStatus);
            }

        } catch (Exception ex) {
            logger.error("Unable to get Amp status   from database "
                         + ex.getMessage());
        }
        return status;
    }

    public static ArrayList<AmpCategoryValue> getAmpStatusFromCM() {
    	ArrayList<AmpCategoryValue> result	=
    		new ArrayList<AmpCategoryValue> (
    				CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.ACTIVITY_STATUS_KEY, true)
    		);
    	return result;
    }

    /**
     * @deprecated Use category manager instead
     *
     * @return
     *
     */
    public static ArrayList getAmpLevels() {
        Session session = null;
        Query q = null;
        AmpLevel ampLevel = null;
        ArrayList level = new ArrayList();
        String queryString = null;
        Iterator iter = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            queryString = " select Level from " + AmpLevel.class.getName()
                + " Level";
            q = session.createQuery(queryString);
            iter = q.list().iterator();

            while (iter.hasNext()) {

                ampLevel = (AmpLevel) iter.next();
                level.add(ampLevel);
            }

        } catch (Exception ex) {
            logger.error("Unable to get Amp levels  from database "
                         + ex.getMessage());
        }
        return level;
    }

    public static Collection getFiscalCalOrgs(Long fiscalCalId) {

        Session sess = null;
        Collection col = null;
        Query qry = null;

        try {
            sess = PersistenceManager.getRequestDBSession();
            String queryString = "select o from "
                + AmpOrganisation.class.getName()
                + " o where (o.ampFiscalCalId=:ampFisCalId)";
            qry = sess.createQuery(queryString);
            qry.setParameter("ampFisCalId", fiscalCalId, Hibernate.LONG);
            Iterator itr = qry.list().iterator();
            col = new ArrayList();
            while (itr.hasNext()) {
                col.add(itr.next());
            }

        } catch (Exception e) {
            logger.debug("Exception from getFiscalCalOrgs()");
            logger.debug(e.toString());
        }
        return col;

    }

    public static Collection getFiscalCalSettings(Long fiscalCalId) {

        Session sess = null;
        Collection col = null;
        Query qry = null;

        try {
            sess = PersistenceManager.getRequestDBSession();
            String queryString = "select o from "
                + AmpApplicationSettings.class.getName()
                + " o where (o.fiscalCalendar=:ampFisCalId)";
            qry = sess.createQuery(queryString);
            qry.setParameter("ampFisCalId", fiscalCalId, Hibernate.LONG);
            Iterator itr = qry.list().iterator();
            col = new ArrayList();
            while (itr.hasNext()) {
                col.add(itr.next());
            }

        } catch (Exception e) {
            logger.debug("Exception from getFiscalCalSettings()");
            logger.debug(e.toString());
        }
        return col;

    }

    public static AmpFiscalCalendar getFiscalCalByName(String name) {
        Session session = null;
        Query qry = null;
        AmpFiscalCalendar ampFisCal = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select f from "
                + AmpFiscalCalendar.class.getName()
                + " f where (f.name=:name)";
            qry = session.createQuery(queryString);
            qry.setParameter("name", name, Hibernate.STRING);
            Iterator itr = qry.list().iterator();
            if (itr.hasNext()) {
                ampFisCal = (AmpFiscalCalendar) itr.next();
            }
        } catch (Exception e) {
            logger.error("Unable to get fiscal Calendar");
            logger.debug("Exceptiion " + e);
        }
        return ampFisCal;
    }

    public static Collection searchForOrganisation(String keyword, Long orgType) {
        Session session = null;
        Collection col = null;
        keyword = keyword.toLowerCase();

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select distinct org from "
                + AmpOrganisation.class.getName() + " org "
                + "where (lower(acronym) like '%" + keyword + "%' || lower(name) like '%"
                + keyword + "%') and org.orgTypeId=:orgType";
            Query qry = session.createQuery(queryString);
            qry.setParameter("orgType", orgType, Hibernate.LONG);
            col = qry.list();
        } catch (Exception ex) {
            logger.debug("Unable to search " + ex);
        }
        return col;
    }

    public static Collection searchForOrganisation(String keyword) {
        Session session = null;
        Collection col = null;
        keyword=keyword.toLowerCase();

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select distinct org from "
                + AmpOrganisation.class.getName() + " org "
                + "where (lower(acronym) like '%" + keyword + "%' || lower(name) like '%"
                + keyword + "%')";
            Query qry = session.createQuery(queryString);
            col = qry.list();
        } catch (Exception ex) {
            logger.debug("Unable to search " + ex);
        }
        return col;
    }
    
    /**
     * This function gets all organizations whose names begin with namesFirstLetter 
     * and name or acronym contain keyword
     * @author Dare
     */
    public static Collection searchForOrganisation(String namesFirstLetter,String keyword) {
        Session session = null;
        Collection col = null;
        if(keyword.length()!=0){
        	keyword=keyword.toLowerCase();
        }
        namesFirstLetter=namesFirstLetter.toLowerCase();

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select distinct org from " + AmpOrganisation.class.getName() + " org "
                + "where ((lower(acronym) like '%" + keyword + "%' && lower(name) like '"+namesFirstLetter+"%') || lower(name) like '"+namesFirstLetter+ "%"
                + keyword + "%')";
            Query qry = session.createQuery(queryString);
            col = qry.list();
        } catch (Exception ex) {
            logger.debug("Unable to search " + ex);
        }
        return col;
    }

    public static Collection searchForOrganisationByType(Long orgType) {
        Session session = null;
        Collection col = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select distinct org from "
                + AmpOrganisation.class.getName() + " org "
                + "where org.orgTypeId=:orgType";
            Query qry = session.createQuery(queryString);
            qry.setParameter("orgType", orgType, Hibernate.LONG);
            col = qry.list();
        } catch (Exception ex) {
            logger.debug("Unable to search " + ex);
        }
        return col;
    }

    public static ArrayList<AmpOrganisation> getAmpOrganisations(boolean includeWeirdOrgs) {
        Session session = null;
        Query q = null;
        AmpOrganisation ampOrganisation = null;
        ArrayList organisation = new ArrayList();
        String queryString = null;
        Iterator iter = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            queryString = " select org from " + AmpOrganisation.class.getName() +" org ";
            if(!includeWeirdOrgs){
               queryString +=  " where org.name not like '%x_%' and org.orgTypeId.orgTypeCode in ('BIL','MUL','Private') ";
            }     
            queryString +=  "  order by org.name";
            q = session.createQuery(queryString);
            iter = q.list().iterator();

            while (iter.hasNext()) {
                ampOrganisation = (AmpOrganisation) iter.next();
                organisation.add(ampOrganisation);
            }

        } catch (Exception ex) {
            logger.error("Unable to get Amp organisation names  from database "
                         + ex.getMessage());
        }
        return organisation;
    }

    public static ArrayList getBilMulOrganisations() {
        Session session = null;
        Query q = null;
        AmpOrganisation ampOrganisation = null;
        ArrayList organisation = new ArrayList();
        String queryString = null;
        Iterator iter = null;

        try {
            AmpOrgType tBil=getAmpOrgTypeByCode("BIL");
            AmpOrgType tMul=getAmpOrgTypeByCode("MUL");

            session = PersistenceManager.getRequestDBSession();
            queryString = " select org from " + AmpOrganisation.class.getName()
                + " org where org.orgTypeId='" + tBil.getAmpOrgTypeId() + "' or org.orgTypeId='" + tMul.getAmpOrgTypeId() + "' order by org.name";
            q = session.createQuery(queryString);
            iter = q.list().iterator();

            while (iter.hasNext()) {
                ampOrganisation = (AmpOrganisation) iter.next();
                organisation.add(ampOrganisation);
            }

        } catch (Exception ex) {
            logger.error("Unable to get Amp organisation names  from database "
                         + ex.getMessage());
            ex.printStackTrace();
        }
        return organisation;
    }
    /*
     * gets all organisation groups  excluding goverment groups
     */ 
   public static Collection<AmpOrgGroup> getAllNonGovOrgGroups() {
        Session session = null;
        Query qry = null;
        Collection<AmpOrgGroup> groups = new ArrayList<AmpOrgGroup>();

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select distinct gr from " + AmpFunding.class.getName()
                    +" f inner join f.ampDonorOrgId  org "
                    +" inner join org.orgGrpId gr "
                + " inner join gr.orgType t where t.orgTypeIsGovernmental is NULL or t.orgTypeIsGovernmental=false order by org_grp_name asc";
            qry = session.createQuery(queryString);
            groups = qry.list();
        } catch (Exception e) {
            logger.error("Unable to get all organisation groups");
            logger.debug("Exceptiion " + e);
        }
        return groups;
    }
    public static void updateIndicator(AmpAhsurveyIndicator ind) {
        AmpAhsurveyIndicator oldInd = new AmpAhsurveyIndicator();
        Session session = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            oldInd=(AmpAhsurveyIndicator)session.load(AmpAhsurveyIndicator.class, ind.getAmpIndicatorId());

            oldInd.setAmpIndicatorId(ind.getAmpIndicatorId());
            oldInd.setCalcFormulas(ind.getCalcFormulas());
            oldInd.setIndicatorCode(ind.getIndicatorCode());
            oldInd.setIndicatorNumber(ind.getIndicatorNumber());
            oldInd.setName(ind.getName());
            oldInd.setQuestions(ind.getQuestions());
            oldInd.setStatus(ind.getStatus());
            oldInd.setTotalQuestions(ind.getTotalQuestions());

            update(oldInd);
        } catch (Exception ex) {
            logger.debug("Unable to get survey indicator : " + ex.getMessage());
            ex.printStackTrace(System.out);
        }
    }

    public static AmpOrgType getAmpOrgTypeByCode(String ampOrgTypeCode) {
        Session session = null;
        Query qry = null;
        AmpOrgType ampOrgType = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select f from " + AmpOrgType.class.getName()
                + " f where (f.orgTypeCode=:ampOrgTypeCode)";
            qry = session.createQuery(queryString);
            qry.setString("ampOrgTypeCode", ampOrgTypeCode);
            Iterator itr = qry.list().iterator();
            if (itr.hasNext()) {
                ampOrgType = (AmpOrgType) itr.next();
            }
        } catch (Exception e) {
            logger.error("Unable to get Org Type");
            logger.debug("Exceptiion " + e);
        }
        return ampOrgType;
    }


    public static Collection getFundingDetWithCurrId(Long currId) {
        Session sess = null;
        Collection col = null;
        Query qry = null;

        try {
            sess = PersistenceManager.getRequestDBSession();
            String queryString = "select f from "
                + AmpFundingDetail.class.getName()
                + " f where (f.ampCurrencyId=:currId)";
            qry = sess.createQuery(queryString);
            qry.setParameter("currId", currId, Hibernate.LONG);
            Iterator itr = qry.list().iterator();
            col = new ArrayList();
            while (itr.hasNext()) {
                col.add(itr.next());
            }

        } catch (Exception e) {
            logger.debug("Exception from getFundingDetWithCurrId()");
            logger.debug(e.toString());
        }
        return col;
    }

    public static Collection getActivityTheme(Long themeId) {
        Session sess = null;
        Collection col = null;
        Query qry = null;

        try {
            sess = PersistenceManager.getRequestDBSession();
            String queryString = "select a from " + AmpActivity.class.getName()
                + " a where (a.themeId=:themeId)";
            qry = sess.createQuery(queryString);
            qry.setParameter("themeId", themeId, Hibernate.LONG);
            Iterator itr = qry.list().iterator();
            col = new ArrayList();
            while (itr.hasNext()) {
                col.add(itr.next());
            }

        } catch (Exception e) {
            logger.debug("Exception from getActivityTheme()");
            logger.debug(e.toString());
        }
        return col;
    }

    public static Collection getActivityThemeFromAAT(Long themeId) {
        Session sess = null;
        Collection col = null;
        try {
            //		PersistenceManager.gets
            sess = PersistenceManager.getRequestDBSession();
            /*String queryString = "select a from " + AmpTheme.class.getName()
              + " a where (a.themeId=:themeId)";*/

            //AmpTheme themeToBeDeleted = (AmpTheme) sess.get(AmpTheme.class, themeId);
            AmpTheme themeToBeDeleted = (AmpTheme) sess.load(AmpTheme.class, themeId);

            //qry = sess.createQuery(queryString);
            //qry.setParameter("themeId", themeId, Hibernate.LONG);
            Iterator itr = themeToBeDeleted.getActivities().iterator();

            col = new ArrayList();
            while (itr.hasNext()) {
                col.add(itr.next());
            }

        } catch (Exception e) {
            logger.debug("Exception from getActivityTheme()");
            logger.debug(e.toString());
        }
        return col;
    }

    public static void add(Object object) {
        logger.debug("In add " + object.getClass().getName());
        Session sess = null;
        Transaction tx = null;

        try {
            sess = PersistenceManager.getRequestDBSession();
            tx = sess.beginTransaction();
            sess.save(object);
            tx.commit();
        } catch (Exception e) {
            logger.error("Unable to add");
            e.printStackTrace(System.out);
            if (tx != null) {
                try {
                    tx.rollback();
                } catch (HibernateException ex) {
                    logger.debug("rollback() failed");
                    logger.debug(ex.toString());
                }
            }
        }
    }

    public static void update(Object object) {
        Session sess = null;
        Transaction tx = null;

        try {
            sess = PersistenceManager.getRequestDBSession();
            tx = sess.beginTransaction();
            sess.update(object);
            tx.commit();
        } catch (Exception e) {
            logger.error("Unable to update");
            logger.debug(e.toString());
            if (tx != null) {
                try {
                    tx.rollback();
                } catch (HibernateException ex) {
                    logger.debug("rollback() failed");
                    logger.debug(ex.toString());
                }
            }
        }
    }

    public static void updateOrg(Object object) {
        Session sess = null;
        Transaction tx = null;

        try {
            sess = PersistenceManager.getRequestDBSession();
            tx = sess.beginTransaction();
            AmpOrganisation org = (AmpOrganisation) object;
            HashSet sect = new HashSet();
            Iterator i = org.getSectors().iterator();
            while (i.hasNext()) {
                AmpSector e = (AmpSector) i.next();
                sect.add(sess.load(AmpSector.class, e.getAmpSectorId()));

            }
            org.setSectors(sect);

            sess.update(org);
            tx.commit();
        } catch (Exception e) {
            logger.error("Unable to update");
            logger.debug(e.toString());
            if (tx != null) {
                try {
                    tx.rollback();
                } catch (HibernateException ex) {
                    logger.debug("rollback() failed");
                    logger.debug(ex.toString());
                }
            }
        }
    }

    public static void delete(Object object) throws JDBCException {
        Session sess = null;
        Transaction tx = null;

        try {
            sess = PersistenceManager.getRequestDBSession();
            tx = sess.beginTransaction();
            logger.debug("before delete");
            sess.delete(object);
            //sess.flush();
            tx.commit();
        } catch (Exception e) {
            if (e instanceof JDBCException)
                throw (JDBCException) e;
            logger.error("Exception " + e.toString());
            try {
                tx.rollback();
            } catch (HibernateException ex) {
                logger.error("rollback() failed");
                logger.error(ex.toString());
            }
        }
    }

    public static void deleteStatus(Long id) {
        AmpStatus oldStatusItem = new AmpStatus();
        Session sess = null;
        Transaction tx = null;

        try {
            sess = PersistenceManager.getRequestDBSession();
            tx = sess.beginTransaction();
            logger.debug("BEFORE SESS.SAVE()");
            oldStatusItem = (AmpStatus) sess.load(AmpStatus.class, id);

            if (sess != null) {
                logger.debug("DbUtil session is not null");

                logger.debug("DbUtil deleteStatus id : "
                             + oldStatusItem.getAmpStatusId());

                sess.delete(oldStatusItem);

                logger.debug("AFTER SESS.SAVE()");

                tx.commit();
            } else
                logger.debug("DbUtil session is null");
        } catch (Exception ex) {
            logger.error("Unable to Delete Amp status record");
            logger.debug(ex.toString());

            if (tx != null) {
                try {
                    tx.rollback();
                } catch (HibernateException ex1) {
                    logger.debug("rollback() failed ");
                }
            }
        }

    }

    public static Collection getQuarters(Long ampFundingId,
                                         Integer transactionType, Integer adjustmentType, Integer fiscalYear) {
        logger.debug("getQuarters() with ampFundingId="
                     + ampFundingId.longValue() + " fiscalYear=" + fiscalYear);

        Session session = null;
        Query q = null;
        Collection c = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = new String();
            queryString = "select f.fiscalQuarter from "
                + AmpFundingDetail.class.getName()
                + " f where (f.ampFundingId=:ampFundingId) "
                + " and (f.transactionType=:transactionType) "
                + " and (f.adjustmentType=:adjustmentType) "
                + " and (f.fiscalYear=:fiscalYear) "
                + " group by f.fiscalQuarter";

            q = session.createQuery(queryString);
            q.setParameter("ampFundingId", ampFundingId, Hibernate.LONG);
            q.setParameter("transactionType", transactionType,
                           Hibernate.INTEGER);
            q.setParameter("adjustmentType", adjustmentType, Hibernate.INTEGER);
            q.setParameter("fiscalYear", fiscalYear, Hibernate.INTEGER);
            c = q.list();
            logger.debug("No of Quarters : " + q.list().size());
        } catch (Exception ex) {
            logger.error("Unable to get  Quarters from database", ex);
        }

        logger.debug("getQuarters() collection size returned : "
                     + (c != null ? c.size() : 0));
        return c;
    }

    
    /**
     * 
     * @param fiscalCalId
     * @return
     */
    public static AmpFiscalCalendar getFiscalCalendar(Long fiscalCalId) {
    	logger.debug("getFiscalCalendar with fiscalCalId" + fiscalCalId);
    	Session session = null;
        Query q = null;
        Collection collection = null;
        Iterator iter = null;
        AmpFiscalCalendar fc=null;
        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = new String();
            queryString = "select f from "
                + AmpFiscalCalendar.class.getName()
                + " f where (f.ampFiscalCalId=:fiscalCalId) ";
            q = session.createQuery(queryString);
            q.setParameter("fiscalCalId", fiscalCalId, Hibernate.LONG);
            collection = q.list();
            if (collection.size() > 0){
            	fc= (AmpFiscalCalendar) collection.toArray()[0];
            }
        } catch (Exception ex) {
            logger.error("Unable to get fiscal calendar from database", ex);
        }
        return fc;	
    }

    
    

    public static Collection getMaxFiscalYears() {
        Session session = null;
        Query q = null;
        Collection c = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select Max(f.fiscalYear), Min(f.fiscalYear) from "
                + AmpFundingDetail.class.getName() + " f ";

            q = session.createQuery(queryString);

            c = q.list();

        } catch (Exception ex) {
            logger.debug("Unable to get  Max fiscal years from database"
                         + ex.getMessage());
        }
        //logger.debug("getFiscalYears() collection size returned : " + ( c !=
        // null ? c.size() : 0 ) ) ;
        return c;
    }

    public static List getAmpAssistanceType(Long ampActivityId) {
        Session session = null;
        Query q = null;
        List c = new ArrayList();

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = new String();
            queryString = "select distinct f.ampTermsAssistId.termsAssistName from "
                + AmpFunding.class.getName()
                + " f where f.ampActivityId=:ampActivityId";

            q = session.createQuery(queryString);
            q.setParameter("ampActivityId", ampActivityId, Hibernate.LONG);
            int i = 0;
            Iterator iter = q.list().iterator();
            while (iter.hasNext()) {
                Assistance assistance = new Assistance();
                logger.debug("Assistance type: " + q.list().get(i));
                if (q.list().get(i) != null) {
                    String code = (String) iter.next();
                    assistance.setAssistanceType(code);
                    i++;
                }
                c.add(assistance);
            }
        } catch (Exception ex) {
            logger.error("Unable to get  Max fiscal years from database", ex);
        }
        //logger.debug("getFiscalYears() collection size returned : " + ( c !=
        // null ? c.size() : 0 ) ) ;
        return c;
    }

    public static List getAmpModalityNames(Long ampActivityId) {
        Session session = null;
        Query q = null;
        List c = new ArrayList();

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = new String();
            queryString = "select distinct f.modalityId.name from "
                + AmpFunding.class.getName()
                + " f where f.ampActivityId=:ampActivityId";

            q = session.createQuery(queryString);
            q.setParameter("ampActivityId", ampActivityId, Hibernate.LONG);

            Iterator iter = q.list().iterator();
            while (iter.hasNext()) {
                String modalityName = (String) iter.next();
                c.add(modalityName);

            }
        } catch (Exception ex) {
            logger.error(ex);
        }

        return c;
    }

    public static Date getClosingDate(Long ampFundingId, Integer type) {

        logger.debug("getClosingDate() with ampFundingId=" + ampFundingId
                     + " type=" + type);

        Session session = null;
        Query q = null;
        Collection c = null;
        Iterator iter = null;
        Date d = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = new String();
            queryString = "select a.closingDate from "
                + AmpClosingDateHistory.class.getName()
                + " a where (a.ampFundingId=:ampFundingId) "
                + " and (a.type=:type)";
            q = session.createQuery(queryString);
            q.setParameter("ampFundingId", ampFundingId, Hibernate.LONG);
            q.setParameter("type", type, Hibernate.INTEGER);
            c = q.list();
            if (c.size() != 0) {
                iter = c.iterator();
                if (iter.hasNext()) {
                    d = (Date) iter.next();
                }
            }
        } catch (Exception ex) {
            logger.error("Unable to get closing date from database", ex);
        }
        logger.debug("getClosingDate() returning date:" + d);
        return d;
    }

    public static String getGoeId(Long ampActivityId) {

        logger.debug("getGoeId() with ampActivityId=" + ampActivityId);
        Long ampDonorOrgId = Constants.MOFED_ORG_ID;
        Session session = null;
        Query q = null;
        Collection c = null;
        Iterator iter = null;
        String s = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = new String();
            queryString = "select a.internalId from "
                + AmpActivityInternalId.class.getName()
                + " a where (a.id=:ampActivityId) "
                + " and (a.organisation=:ampDonorOrgId)"
                + " a inner join a.ampActivityId ampAct"  
                + " inner join a.ampOrgId ampOrg "  
                + " where (ampAct.ampActivityId=:ampActivityId) "
                + " and (ampOrg.ampOrgId=:ampDonorOrgId)";
    
            q = session.createQuery(queryString);
            q.setParameter("ampActivityId", ampActivityId, Hibernate.LONG);
            q.setParameter("ampDonorOrgId", ampDonorOrgId, Hibernate.LONG);
            if (q.list() != null) {
                iter = q.list().iterator();
                if (iter.hasNext())
                    s = (String) iter.next();
            } else
                s = "NA";
        } catch (Exception ex) {
            logger.error("Unable to get GOE ID from database", ex);
        }
        logger.debug("getGoeId() returning s:" + s);
        return s;
    }

    public static Collection getFundingIdforG(Long ampActivityId,
                                              Long ampDonorOrgId, Long ampTermsAssistId) {
        logger.debug(" Funding Term Code passed is " + ampTermsAssistId);
        Session session = null;
        Query q = null;
        Collection funding = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = new String();
            queryString = "select f from "
                + AmpFunding.class.getName()
                + " f where (f.ampDonorOrgId =:ampDonorOrgId) and (f.ampTermsAssistId =:ampTermsAssistId) and (f.ampActivityId =:ampActivityId)";
            logger.debug("querystring " + queryString);
            q = session.createQuery(queryString);
            q.setParameter("ampActivityId", ampActivityId, Hibernate.LONG);
            q.setParameter("ampDonorOrgId", ampDonorOrgId, Hibernate.LONG);
            q
                .setParameter("ampTermsAssistId", ampTermsAssistId,
                              Hibernate.LONG);
            funding = q.list();
        } catch (Exception ex) {
            logger.error("Unable to get Funding records from database", ex);
        }
        logger.debug("Returning Funding Grant : "
                     + (funding != null ? funding.size() : 0));
        return funding;

    }

    public static ArrayList getAmpDonorsByFunding(Long ampTeamId) {
        Session session = null;
        Query q = null;
        ArrayList donors = new ArrayList();
        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = new String();
            queryString = "select f from "
                + AmpFunding.class.getName() + " f";
            q = session.createQuery(queryString);
            //logger.debug("No of Donors : " + q.list().size());
            Iterator it = q.list().iterator();
            while (it.hasNext()) {
                AmpFunding el = (AmpFunding) it.next();
                if (el.getAmpActivityId().getTeam().getAmpTeamId().equals(ampTeamId)) {
                    AmpOrganisation org = el.getAmpDonorOrgId();
                    if (donors.indexOf(org) == -1)
                        donors.add(org);
                }
            }
        } catch (Exception ex) {
            logger.debug("Unable to get Donors from database", ex);
        }
        return donors;
    }

    public static ArrayList getAmpDonors(Long ampTeamId) {
        ArrayList donor = new ArrayList();
        StringBuffer DNOrg = new StringBuffer();
        Session session = null;
        Query q = null;
        Iterator iterActivity = null;
        Iterator iter = null;
        String inClause = null;

        try {
            ArrayList dbReturnSet = (ArrayList) TeamUtil
                .getAmpLevel0TeamIds(ampTeamId);
            if (dbReturnSet.size() == 0)
                inClause = "'" + ampTeamId + "'";
            else {
                iter = dbReturnSet.iterator();
                while (iter.hasNext()) {
                    Long teamId = (Long) iter.next();
                    if (inClause == null)
                        inClause = "'" + teamId + "'";
                    else
                        inClause = inClause + ",'" + teamId + "'";
                }
            }
            session = PersistenceManager.getRequestDBSession();
            String queryString = new String();
            queryString = "select activity from " + AmpActivity.class.getName()
                + " activity where activity.team.ampTeamId in(" + inClause
                + ")";
            q = session.createQuery(queryString);
            logger.debug("Activity List: " + q.list().size());
            iterActivity = q.list().iterator();
            while (iterActivity.hasNext()) {
                AmpActivity ampActivity = (AmpActivity) iterActivity.next();

//				logger.debug("Org Role List: " + ampActivity.getOrgrole().size());
                iter = ampActivity.getOrgrole().iterator();
                while (iter.hasNext()) {
                    AmpOrgRole ampOrgRole = (AmpOrgRole) iter.next();
                    if (ampOrgRole.getRole().getRoleCode().equals(
                        Constants.FUNDING_AGENCY)) {
                        if (donor.indexOf(ampOrgRole.getOrganisation()) == -1)
                            donor.add(ampOrgRole.getOrganisation());
                    }
                }
            }
            logger.debug("Donors: " + donor.size());
            int n = donor.size();
            for (int i = 0; i < n - 1; i++) {
                for (int j = 0; j < n - 1 - i; j++) {
                    AmpOrganisation firstOrg = (AmpOrganisation) donor.get(j);
                    AmpOrganisation secondOrg = (AmpOrganisation) donor
                        .get(j + 1);
                    if (firstOrg.getAcronym().compareToIgnoreCase(
                        secondOrg.getAcronym()) > 0) {
                        AmpOrganisation tempOrg = firstOrg;
                        donor.set(j, secondOrg);
                        donor.set(j + 1, tempOrg);
                    }
                }
            }

        } catch (Exception ex) {
            logger.debug("Unable to get Donor " + ex.getMessage());
        }
        return donor;
    }

    public static Collection getDonorFund1(Long ampFundingId,
                                           Integer transactionType, Integer adjustmentType) {
        logger.debug("getTotalDonorFund() with ampFundingId " + ampFundingId
                     + " transactionType " + transactionType + " adjustmentType "
                     + adjustmentType);
        Session session = null;
        Query q = null;
        List list = null;
        Iterator iter = null;
        double total = 0.0;
        Collection ampFundings = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = queryString = "select f from "
                + AmpFundingDetail.class.getName()
                + " f where (f.ampFundingId=:ampFundingId) "
                + " and (f.transactionType=:transactionType) "
                + " and (f.adjustmentType=:adjustmentType) ";

            q = session.createQuery(queryString);
            q.setParameter("ampFundingId", ampFundingId, Hibernate.LONG);
            q.setParameter("transactionType", transactionType,
                           Hibernate.INTEGER);
            q.setParameter("adjustmentType", adjustmentType, Hibernate.INTEGER);
            ampFundings = q.list();
            logger.debug("size of result " + ampFundings.size());
            /*
             * iter = list.iterator() ; while ( iter.hasNext() ) {
             * AmpFundingDetail fundDetails = new AmpFundingDetail();
             * fundDetails = (AmpFundingDetail)iter.next();
             * if(fundDetails.getAmpCurrencyId().getCurrencyCode().equals("USD")) {
             * //logger.debug("equals USD"); total = total +
             * fundDetails.getTransactionAmount().doubleValue() ; } else {
             * //logger.debug(" not equal to USD ") ; total = total +
             * CurrencyWorker.convert(fundDetails.getTransactionAmount().doubleValue(),"USD") ;
             * //logger.debug("AFTER conversion total is " + total); }
             *
             *  }
             */
            //logger.debug("Final Total is " + total);
        } catch (Exception ex) {
            logger.debug("Unable to get sum of funds from database"
                         + ex.getMessage());
        }
        return ampFundings;
    }

    /**
     *
     * @param ampActivityId
     * @param transactionType
     * @param adjustmentType
     * @param ampCurrencyCode
     * @return
     */
    public static DecimalWraper getAmpFundingAmount(Long ampActivityId,
                                             Integer transactionType, Integer adjustmentType,
                                             String ampCurrencyCode) {
        Session session = null;
        Query q = null;
        Iterator iter = null;
        String inClause = null;
        double fromCurrency = 0.0;
        double toCurrency = 1.0;
        DecimalWraper amount = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = queryString = "select f from "
                + AmpFunding.class.getName()
                + " f where (f.ampActivityId=:ampActivityId) ";
            q = session.createQuery(queryString);
            q.setParameter("ampActivityId", ampActivityId, Hibernate.LONG);
            iter = q.list().iterator();
            while (iter.hasNext()) {
                AmpFunding ampFunding = (AmpFunding) iter.next();
                if (inClause == null)
                    inClause = "'" + ampFunding.getAmpFundingId() + "'";
                else
                    inClause = inClause + ",'" + ampFunding.getAmpFundingId()
                        + "'";
            }
            logger.debug(" transactionType " + transactionType
                         + " adjustmentType " + adjustmentType
                         + " ampCurrencyCode" + ampCurrencyCode);
            queryString = queryString = "select fd from "
                + AmpFundingDetail.class.getName()
                + " fd where (fd.transactionType=:transactionType) "
                + " and (fd.adjustmentType=:adjustmentType) "
                + " and (fd.ampFundingId in(" + inClause + "))";
            logger.debug("queryString :" + queryString);
            q = session.createQuery(queryString);
            q.setParameter("transactionType", transactionType,
                           Hibernate.INTEGER);
            q.setParameter("adjustmentType", adjustmentType, Hibernate.INTEGER);
            iter = q.list().iterator();
            logger.debug("Size: " + q.list().size());
            amount = new DecimalWraper();
            while (iter.hasNext()) {
                AmpFundingDetail ampFundingDetail = (AmpFundingDetail) iter.next();
                Double fixedRateToUSD = ampFundingDetail.getFixedExchangeRate();
                if (fixedRateToUSD!=null && fixedRateToUSD.doubleValue()!=1){
                	 fromCurrency=fixedRateToUSD.doubleValue();

                	 toCurrency = Util.getExchange(ampCurrencyCode,
 							new java.sql.Date(ampFundingDetail
 									.getTransactionDate().getTime()));

                	 DecimalWraper tmpamount =CurrencyWorker.convertWrapper(ampFundingDetail
							.getTransactionAmount().doubleValue(),
							fromCurrency, toCurrency, new java.sql.Date(
									ampFundingDetail.getTransactionDate()
											.getTime()));
                	if (amount.getValue()!=null){
                		amount.setValue(amount.getValue().add(tmpamount.getValue()));
                		amount.setCalculations(amount.getCalculations() + " +" + tmpamount.getCalculations()+"<BR>");
                	}
                	else{
                		amount.setValue(tmpamount.getValue());
                		amount.setCalculations(tmpamount.getCalculations());
                	}
                }else{
                    toCurrency = Util.getExchange(ampCurrencyCode,
							new java.sql.Date(ampFundingDetail
									.getTransactionDate().getTime()));

					fromCurrency = Util.getExchange(ampFundingDetail
							.getAmpCurrencyId().getCurrencyCode(),
							new java.sql.Date(ampFundingDetail
									.getTransactionDate().getTime()));

					logger.debug("to Currency: " + toCurrency);
					logger.debug("From Currency: " + fromCurrency);

					DecimalWraper tmpamount =CurrencyWorker.convertWrapper(ampFundingDetail
							.getTransactionAmount().doubleValue(),
							fromCurrency, toCurrency, new java.sql.Date(
									ampFundingDetail.getTransactionDate()
											.getTime()));

					if (amount.getValue()!=null){
						amount.setCalculations(amount.getCalculations() + " + " + tmpamount.getCalculations());
						BigDecimal tmp = amount.getValue().add(tmpamount.getValue());
						amount.setValue(tmp);
					}
					else{
						amount.setCalculations(tmpamount.getCalculations());
						amount.setValue(tmpamount.getValue());
					}
				}

            }
            logger.debug("Amount: " + amount);

        } catch (Exception ex) {
            logger.error("Unable to get sum of funds from database", ex);
        }

        return amount;
    }

    /**
     * Given a page code like 'DTP' for Desktop,'FP' - Financial Progress
     * returns the page id
     *
     * @param pageCode
     * @return page id
     */
    public static Long getPageId(String pageCode) {

        Session session = null;
        Query q = null;
        Collection c = null;
        Long id = null;
        Iterator iter = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = new String();
            queryString = "select a.ampPageId from " + AmpPages.class.getName()
                + " a where (a.pageCode=:pageCode) ";
            q = session.createQuery(queryString);
            q.setParameter("pageCode", pageCode, Hibernate.STRING);
            c = q.list();
            if (c.size() != 0) {
                iter = c.iterator();
                if (iter.hasNext()) {
                    id = (Long) iter.next();
                }
            } else {
                if (logger.isDebugEnabled())
                    logger.debug("No page with corresponding name");
            }
        } catch (Exception ex) {
            if (logger.isDebugEnabled())
                logger.error("Unable to get page id  from database", ex);
        }
        if (logger.isDebugEnabled())
            logger.debug("getPageId() returning page id:" + id);
        return id;
    }

    public static Long getAmpPageId(String pageName) {

        Session session = null;
        Query q = null;
        Collection c = null;
        Long id = null;
        Iterator iter = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = new String();
            queryString = "select a.ampPageId from " + AmpPages.class.getName()
                + " a where (a.pageName=:pageName) ";
            q = session.createQuery(queryString);
            q.setParameter("pageName", pageName, Hibernate.STRING);
            c = q.list();
            if (c.size() != 0) {
                iter = c.iterator();
                if (iter.hasNext()) {
                    id = (Long) iter.next();
                }
            } else {
                if (logger.isDebugEnabled())
                    logger.debug("No page with corresponding name");
            }
        } catch (Exception ex) {
            if (logger.isDebugEnabled())
                logger.error("Unable to get page id  from database", ex);
        }
        if (logger.isDebugEnabled())
            logger.debug("getPageId() returning page id:" + id);
        return id;
    }

    public static Collection getOrgId(Long ampSecSchemeId) {
        Session session = null;
        Query q = null;
        Collection ampOrgs = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = new String();
            queryString = "select o from " + AmpOrganisation.class.getName()
                + " o where (o.ampSecSchemeId=:ampSecSchemeId ) ";
            q = session.createQuery(queryString);
            q.setParameter("ampSecSchemeId", ampSecSchemeId, Hibernate.LONG);

            ampOrgs = q.list();
            logger.debug("DbUtil : getOrgId() returning collection of size  "
                         + ampOrgs.size());

        } catch (Exception ex) {
            logger.error("Unable to get AmpFunding collection from database",
                         ex);
        }

        return ampOrgs;
    }

    // ----------------

    public static ArrayList getAmpStatusList() {
        Session session = null;
        Query q = null;
        AmpStatus ampStatus = null;
        ArrayList ampStatusList = new ArrayList();
        String queryString = null;
        Iterator iter = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            queryString = " select s from " + AmpStatus.class.getName() + " s ";
            q = session.createQuery(queryString);
            iter = q.list().iterator();

            while (iter.hasNext()) {
                ampStatus = (AmpStatus) iter.next();
                logger.debug("Amp Status Id :"
                             + (Long) ampStatus.getAmpStatusId());
                logger.debug("Amp Status Code :"
                             + (String) ampStatus.getStatusCode());
                logger
                    .debug("Amp Status Name :"
                           + (String) ampStatus.getName());
                ampStatusList.add(ampStatus);
            }

        } catch (Exception ex) {
            logger.debug("Unable to get Amp Status records  from database "
                         + ex.getMessage());
        }
        return ampStatusList;
    }

    public static AmpStatus getAmpStatus(Long id) {
        AmpStatus statusItem = new AmpStatus();
        Session session = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            statusItem = (AmpStatus) session.load(AmpStatus.class, id);
        } catch (Exception ex) {
            logger.error("DbUtil:getAmpStatus: Unable to get Amp Status ", ex);
            //////System.out.println(ex.toString()) ;
        }
        logger.debug("DbUtil: getAmpStatus(id) executed successfully ");
        return statusItem;
    }

    public static double getFundDetails(Long ampFundingId,
                                        Integer transactionType, Integer adjustmentType, Integer forcastYear) {
        logger.debug("inside fund details for yr " + forcastYear);
        Session session = null;
        Query q = null;
        Iterator iter = null;
        Double amount = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = new String();
            queryString = " select sum(fd.thousandsTransactionAmount) from "
                + AmpFundingDetail.class.getName()
                + " fd where (fd.ampFundingId = :ampFundingId ) and (fd.fiscalYear = :forcastYear) and (fd.transactionType=:transactionType) and (fd.adjustmentType=:adjustmentType) group by fd.fiscalYear ";
            q = session.createQuery(queryString);
            q.setParameter("ampFundingId", ampFundingId, Hibernate.LONG);
            q.setParameter("forcastYear", forcastYear, Hibernate.INTEGER);
            q.setParameter("transactionType", transactionType,
                           Hibernate.INTEGER);
            q.setParameter("adjustmentType", adjustmentType, Hibernate.INTEGER);

            iter = q.list().iterator();
            while (iter.hasNext()) {
                amount = (Double) iter.next();
            }
        } catch (Exception ex) {
            logger.debug("Unable to get REcords names  from database "
                         + ex.getMessage());
        }
        if (amount == null) {
            logger.debug("RETURNING ZERO");
            return 0;
        } else {
            return amount.doubleValue();
        }
    }

    public static double getFundDetails(Long ampFundingId,
                                        Integer transactionType, Integer adjustmentType) {
        Session session = null;
        Query q = null;
        Iterator iter = null;
        Double amount = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = new String();
            queryString = " select sum(fd.thousandsTransactionAmount) from "
                + AmpFundingDetail.class.getName()
                + " fd where (fd.ampFundingId = :ampFundingId ) and (fd.transactionType=:transactionType) and (fd.adjustmentType=:adjustmentType) group by fd.ampFundingId ";
            q = session.createQuery(queryString);
            q.setParameter("ampFundingId", ampFundingId, Hibernate.LONG);
            q.setParameter("transactionType", transactionType,
                           Hibernate.INTEGER);
            q.setParameter("adjustmentType", adjustmentType, Hibernate.INTEGER);

            iter = q.list().iterator();
            while (iter.hasNext()) {
                amount = (Double) iter.next();
            }

        } catch (Exception ex) {
            logger.debug("Unable to get REcords names  from database "
                         + ex.getMessage());
        }
        if (amount == null) {
            logger.debug("RETURNING ZERO");
            return 0;
        } else {
            return amount.doubleValue();
        }
    }

    public static ArrayList getProjects() {
        Session session = null;
        Query q = null;
        AmpActivity ampActivity = new AmpActivity();
        ArrayList project = new ArrayList();
        List list = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = new String();

            queryString = " select a from " + AmpActivity.class.getName()
                + " a ";
            //logger.debug("querystring " + queryString);
            q = session.createQuery(queryString);
            Iterator iter = q.list().iterator();

            while (iter.hasNext()) {
                ampActivity = (AmpActivity) iter.next();
                project.add(ampActivity);
            }

        } catch (Exception ex) {

            logger
                .debug("Projects : Unable to get Amp Activity names from database "
                       + ex.getMessage());

        }
        return project;
    }

    public static double getDonorFund(Long ampFundingId,
                                      Integer transactionType, Integer adjustmentType) {
        logger.debug("getTotalDonorFund() with ampFundingId " + ampFundingId
                     + " transactionType " + transactionType + " adjustmentType "
                     + adjustmentType);
        Session session = null;
        Query q = null;
        List list = null;
        Iterator iter = null;
        double total = 0.0;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = new String();

            queryString = "select f from " + AmpFundingDetail.class.getName()
                + " f where (f.ampFundingId=:ampFundingId) "
                + " and (f.transactionType=:transactionType) "
                + " and (f.adjustmentType=:adjustmentType) ";

            q = session.createQuery(queryString);
            q.setParameter("ampFundingId", ampFundingId, Hibernate.LONG);
            q.setParameter("transactionType", transactionType,
                           Hibernate.INTEGER);
            q.setParameter("adjustmentType", adjustmentType, Hibernate.INTEGER);
            list = q.list();
            logger.debug("size of result " + list.size());
            iter = list.iterator();
            while (iter.hasNext()) {
                AmpFundingDetail fundDetails = new AmpFundingDetail();
                fundDetails = (AmpFundingDetail) iter.next();
                if (fundDetails.getAmpCurrencyId().getCurrencyCode().equals(
                    "USD")) { //logger.debug("equals USD");
                    total = total
                        + fundDetails.getTransactionAmount().doubleValue();
                } else { //logger.debug(" not equal to USD ") ;
                    double fromCurrency = Util.getExchange(fundDetails
                        .getAmpCurrencyId().getCurrencyCode(),new java.sql.Date(fundDetails.getTransactionDate().getTime()));
                    double toCurrency = CurrencyUtil.getExchangeRate("USD");
                    //total = total +
                    // CurrencyWorker.convert(fundDetails.getTransactionAmount().doubleValue(),"USD")
                    // ;
                    total = total
                        + CurrencyWorker.convert1(fundDetails
                                                  .getTransactionAmount().doubleValue(),
                                                  fromCurrency, toCurrency);
                    //logger.debug("AFTER conversion total is " + total);
                }

            }
        } catch (Exception ex) {
            logger.debug("Unable to get sum of funds from database", ex);
        }
        return total;
    }

    public static double getDonorFundbyFiscalYear(Long ampFundingId,
                                                  Integer transactionType, Integer adjustmentType,
                                                  Integer fiscalYear) {
        Session session = null;
        Query q = null;
        List list = null;
        Iterator iter = null;
        double total = 0.0;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = new String();

            queryString = "select f from " + AmpFundingDetail.class.getName()
                + " f where (f.ampFundingId=:ampFundingId) "
                + " and (f.transactionType=:transactionType) "
                + " and (f.adjustmentType=:adjustmentType) "
                + " and (f.fiscalYear=:fiscalYear) ";

            q = session.createQuery(queryString);
            q.setParameter("ampFundingId", ampFundingId, Hibernate.LONG);
            q.setParameter("transactionType", transactionType,
                           Hibernate.INTEGER);
            q.setParameter("adjustmentType", adjustmentType, Hibernate.INTEGER);
            q.setParameter("fiscalYear", fiscalYear, Hibernate.INTEGER);
            list = q.list();

            iter = list.iterator();
            logger.debug("size of resultset " + q.list().size());

            while (iter.hasNext()) {
                AmpFundingDetail fundDetails = new AmpFundingDetail();
                fundDetails = (AmpFundingDetail) iter.next();

                double fromCurrency = CurrencyUtil.getExchangeRate(fundDetails
                    .getAmpCurrencyId().getCurrencyCode());
                double toCurrency = CurrencyUtil.getExchangeRate("USD");

                if (fundDetails.getAmpCurrencyId().getCurrencyCode().equals(
                    "USD")) {
                    total = total
                        + fundDetails.getTransactionAmount().doubleValue();
                    logger.debug("if total " + total);
                } else {
                    total = total
                        + CurrencyWorker.convert1(fundDetails
                                                  .getTransactionAmount().doubleValue(),
                                                  fromCurrency, toCurrency);
                    logger.debug(" else total " + total);
                }

            }
            logger.debug("Total K " + total);
        } catch (Exception ex) {
            logger.debug("Unable to get sum of funds from database", ex);
        }
        return total;
    }

    public static double getDonorFundbyFiscalYear(Long ampFundingId,
                                                  Integer transactionType, Integer adjustmentType,
                                                  Integer fiscalYear, Integer fiscalQuarter) {
        /*
         * logger.debug("getTotalDonorFundbyFiscalYear() with ampFundingId " +
         * ampFundingId + " transactionType " + transactionType + "
         * adjustmentType " + adjustmentType + " perspective " + perspective + "
         * fiscal year " + fiscalYear + " quarter " + fiscalQuarter) ;
         */
        Session session = null;
        Query q = null;
        List list = null;
        Iterator iter = null;
        double total = 0.0;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = new String();

            queryString = "select f from " + AmpFundingDetail.class.getName()
                + " f where (f.ampFundingId=:ampFundingId) "
                + " and (f.transactionType=:transactionType) "
                + " and (f.adjustmentType=:adjustmentType) "
                + " and (f.fiscalYear=:fiscalYear) "
                + " and (f.fiscalQuarter=:fiscalQuarter) ";
            q = session.createQuery(queryString);
            q.setParameter("ampFundingId", ampFundingId, Hibernate.LONG);
            q.setParameter("transactionType", transactionType,
                           Hibernate.INTEGER);
            q.setParameter("adjustmentType", adjustmentType, Hibernate.INTEGER);
            q.setParameter("fiscalYear", fiscalYear, Hibernate.INTEGER);
            q.setParameter("fiscalQuarter", fiscalQuarter, Hibernate.INTEGER);
            list = q.list();

            iter = list.iterator();
            while (iter.hasNext()) {
                AmpFundingDetail fundDetails = new AmpFundingDetail();
                fundDetails = (AmpFundingDetail) iter.next();
                if (fundDetails.getAmpCurrencyId().getCurrencyCode().equals(
                    "USD")) {
                    total = total
                        + fundDetails.getTransactionAmount().doubleValue();
                } else {
                    total = total
                        + CurrencyWorker.convert(fundDetails
                                                 .getTransactionAmount().doubleValue(),
                                                 "USD");

                }

            }

        } catch (Exception ex) {
            logger.debug("Unable to get sum of funds from database", ex);
        }
        return total;
    }

    public static Collection<AmpOrganisation> getDonors() {
        Session session = null;
        Query q = null;
        Collection<AmpOrganisation> donors = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = new String();
            queryString = "select distinct f.ampDonorOrgId from "
                + AmpFunding.class.getName() + " f";
            q = session.createQuery(queryString);
            logger.debug("No of Donors : " + q.list().size());
            donors = q.list();
        } catch (Exception ex) {
            logger.debug("Unable to get Donors from database", ex);
        }
        return donors;
    }

    public static Collection getOrganisations() {
        Session session = null;
        Query q = null;
        Collection donors = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = new String();
            queryString = "select distinct org from "
                + AmpOrganisation.class.getName() + " org  join  org.calendar  cal";
            q = session.createQuery(queryString);
            //logger.debug("No of Donors : " + q.list().size());
            donors = q.list();
        } catch (Exception ex) {
            logger.debug("Unable to get Donors from database", ex);
            ex.printStackTrace();
        }
        return donors;

    }


    public static Collection getFundingIdbyDonor(Long ampDonorOrgId) {
        Session session = null;
        Query q = null;
        Collection fundingIds = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = new String();
            queryString = "select f from " + AmpFunding.class.getName()
                + " f where (f.ampDonorOrgId=:ampDonorOrgId)";
            q = session.createQuery(queryString);
            q.setParameter("ampDonorOrgId", ampDonorOrgId, Hibernate.LONG);
            //logger.debug("No of funding Id for each donor : " +
            // q.list().size());
            fundingIds = q.list();
        } catch (Exception ex) {
            logger.debug("Unable to get Donors from database", ex);
        }
        logger.debug("Returning fundingIDs : "
                     + (fundingIds != null ? fundingIds.size() : 0));
        return fundingIds;

    }

    public static Collection getProjectsbyModality(Long ampModalityId) {
        //logger.debug("Modality Id : " + ampModalityId);
        Session session = null;
        Query q = null;
        Collection projects = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = new String();
            queryString = " select activity from "
                + AmpActivity.class.getName()
                + " activity where activity.modality.ampModalityId = :ampModalityId";
            q = session.createQuery(queryString);
            q.setParameter("ampModalityId", ampModalityId, Hibernate.LONG);
            //				logger.debug("No of projects for each Modality : " +
            // q.list().size());
            projects = q.list();
        } catch (Exception ex) {
            logger.debug("Unable to get Donors from database", ex);
        }
        logger.debug("Returning Projects : "
                     + (projects != null ? projects.size() : 0));
        return projects;

    }

    public static Collection getDonorAgencies() {
        Session session = null;
        Query q = null;
        Collection donorGroups = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = new String();
            queryString = "select distinct o.orgType from "
                + AmpOrganisation.class.getName() + " o";
            q = session.createQuery(queryString);
            logger.debug("No of Donors Agencies : " + q.list().size());
            donorGroups = q.list();
        } catch (Exception ex) {
            logger.debug("Unable to get Donor Agencies from database", ex);
        }
        return donorGroups;

    }

    public static Collection getDonorbyAgency(String orgType) {
        logger.debug(" Donor Agency name passed is " + orgType);
        Session session = null;
        Query q = null;
        Collection donorGroups = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = new String();
            queryString = "select o from " + AmpOrganisation.class.getName()
                + " o where (o.orgType =:orgType)";
            q = session.createQuery(queryString);
            q.setParameter("orgType", orgType, Hibernate.STRING);
            //logger.debug("No of Org records : " + q.list().size());
            donorGroups = q.list();
        } catch (Exception ex) {
            logger
                .debug("Unable to get Organisation records from database",
                       ex);
        }
        return donorGroups;

    }

    public static Collection getFundingIdbyOrgId(Long ampDonorOrgId,
                                                 String fundingTermsCode) {
        logger.debug(" Funding Term Assit Id passed is " + fundingTermsCode);
        Session session = null;
        Query q = null;
        Collection funding = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = new String();
            queryString = "select f from "
                + AmpFunding.class.getName()
                + " f where (f.ampDonorOrgId =:ampDonorOrgId) and (f.fundingTermsCode =:fundingTermsCode ) ";
            logger.debug("querystring " + queryString);
            q = session.createQuery(queryString);
            q.setParameter("ampDonorOrgId", ampDonorOrgId, Hibernate.LONG);
            q.setParameter("fundingTermsCode", fundingTermsCode,
                           Hibernate.STRING);
            logger.debug("No of funding records : " + q.list().size());
            funding = q.list();
        } catch (Exception ex) {
            logger.debug("Unable to get Funding records from database", ex);
        }
        logger.debug("Returning Funding Loan : "
                     + (funding != null ? funding.size() : 0));
        return funding;

    }

    public static Collection getFundingIdforL(Long ampDonorOrgId,
                                              Long ampTermsAssistId) {
        logger.debug(" Funding Term Code passed is " + ampTermsAssistId);
        Session session = null;
        Query q = null;
        Collection funding = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = new String();
            //queryString = "select f from " + AmpFunding.class.getName() + " f
            // , " + AmpTermsAssist.class.getName() + " t where (f.ampDonorOrgId
            // =:ampDonorOrgId) and (t.termsAssistName = termsAssistName ) ";
            queryString = "select f from "
                + AmpFunding.class.getName()
                + " f where (f.ampDonorOrgId =:ampDonorOrgId) and (f.ampTermsAssistId =:ampTermsAssistId)";
            q = session.createQuery(queryString);
            q.setParameter("ampDonorOrgId", ampDonorOrgId, Hibernate.LONG);
            q
                .setParameter("ampTermsAssistId", ampTermsAssistId,
                              Hibernate.LONG);
            logger.debug("querystring " + queryString);
            funding = q.list();
        } catch (Exception ex) {
            logger.debug("Unable to get Funding records from database", ex);
        }
        logger.debug("Returning Funding L : "
                     + (funding != null ? funding.size() : 0));
        return funding;

    }

    public static Collection getFiscalYears(Long ampFundingId,
                                            String orgRoleCode) {
        Session session = null;
        Query q = null;
        Collection c = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = new String();
            queryString = "select distinct f.fiscalYear from "
                + AmpFundingDetail.class.getName()
                + " f where (f.ampFundingId=:ampFundingId) "
                + " and (f.orgRoleCode=:orgRoleCode) ";

            q = session.createQuery(queryString);
            q.setParameter("ampFundingId", ampFundingId, Hibernate.LONG);
            q.setParameter("orgRoleCode", orgRoleCode, Hibernate.STRING);

            c = q.list();
        } catch (Exception ex) {
            logger.debug("Unable to get  fiscal years from database", ex);
        }
        logger.debug("getFiscalYears() collection size returned : "
                     + (c != null ? c.size() : 0));
        return c;
    }

    public static Collection getFundingId(Long ampActivityId, Long ampDonorOrgId) {
        Session session = null;
        Query q = null;
        Collection ampFundings = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = new String();
            queryString = "select f from "
                + AmpFunding.class.getName()
                + " f where (f.ampActivityId=:ampActivityId ) and (f.ampDonorOrgId=:ampDonorOrgId)";
            q = session.createQuery(queryString);
            q.setParameter("ampActivityId", ampActivityId, Hibernate.LONG);
            q.setParameter("ampDonorOrgId", ampDonorOrgId, Hibernate.LONG);
            ampFundings = q.list();
            logger
                .debug("DbUtil : getFundingId() returning collection of size  "
                       + ampFundings.size());

        } catch (Exception ex) {
            logger.error("Unable to get AmpFunding collection from database",
                         ex);
        }

        return ampFundings;
    }

    public static Collection getActivityId(Long ampOrgId) {
        Session session = null;
        Query q = null;
        ArrayList list = new ArrayList();
        String queryString = null;
        Iterator iter = null;
        Collection act1 = new ArrayList();

        try {
            session = PersistenceManager.getRequestDBSession();
            queryString = "select activity from " + AmpActivity.class.getName()
                + " activity";
            q = session.createQuery(queryString);
            //q.setParameter("activity.getOrgrole().getOrganisation().getAmpOrgId",ampOrgId,Hibernate.LONG)
            // ;
            Collection act = q.list();

            Iterator actItr = act.iterator();
            while (actItr.hasNext()) {
                AmpActivity ampActivity = (AmpActivity) actItr.next();
                Iterator iter1 = ampActivity.getOrgrole().iterator();

                while (iter1.hasNext()) {
                    AmpOrgRole ampOrg = (AmpOrgRole) iter1.next();
                    if (ampOrg.getOrganisation().getAmpOrgId().intValue() == ampOrgId
                        .intValue()) {
                        act1.add(ampActivity);
                    } //if
                } //while
            } //while
        } catch (Exception ex) {
            logger.debug("Unable to get activty names  from database "
                         + ex.getMessage());
        }
        return act1;
    }

    public static AmpTermsAssist getAssistanceType(Long ampTermsAssistId) {

        Session session = null;
        Query q = null;
        AmpTermsAssist ampTermsAssist = null;
        Collection c = null;
        Iterator iter = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = new String();
            queryString = "select a from " + AmpTermsAssist.class.getName()
                + " a where (a.ampTermsAssistId=:ampTermsAssistId) ";
            q = session.createQuery(queryString);
            q
                .setParameter("ampTermsAssistId", ampTermsAssistId,
                              Hibernate.LONG);
            c = q.list();
            if (c.size() != 0) {
                iter = c.iterator();
                if (iter.hasNext()) {
                    ampTermsAssist = (AmpTermsAssist) iter.next();
                }
            } else {
                if (logger.isDebugEnabled())
                    logger.debug("Unable to get type of assistance for id "
                                 + ampTermsAssistId);
            }
        } catch (Exception ex) {
            logger.error("Unable to get type of assistance from database", ex);
        }
        if (logger.isDebugEnabled()) {
            if (ampTermsAssist != null)
                logger
                    .debug("getAssistanceType() returning type of assistance :"
                           + ampTermsAssist.getTermsAssistName());
        }
        return ampTermsAssist;
    }

    public static Collection getAmpReportSector(Long ampActivityId) {
        Session session = null;
        ArrayList sectors = new ArrayList();

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select a from "
                + AmpReportSector.class.getName() + " a "
                + "where (a.ampActivityId=:ampActivityId)";
            Query qry = session.createQuery(queryString);
            qry.setParameter("ampActivityId", ampActivityId, Hibernate.LONG);
            Iterator itr = qry.list().iterator();
            while (itr.hasNext()) {
                AmpReportSector act = (AmpReportSector) itr.next();
                if (sectors.indexOf(act.getSectorName()) == -1)
                    sectors.add(act.getSectorName());
            }

        } catch (Exception ex) {
            logger.error("Unable to get activity sectors" + ex);
        }
        return sectors;
    }

    public static Collection getAmpReportSectorId(Long ampActivityId) {
        Session session = null;
        Collection sectors = new ArrayList();

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select a from "
                + AmpReportSector.class.getName() + " a "
                + "where (a.ampActivityId=:ampActivityId)";
            Query qry = session.createQuery(queryString);
            qry.setParameter("ampActivityId", ampActivityId, Hibernate.LONG);
            Iterator itr = qry.list().iterator();
            while (itr.hasNext()) {
                AmpReportSector act = (AmpReportSector) itr.next();
                //	AmpSector
                // ampSector=DbUtil.getAmpParentSector(act.getAmpSectorId());
                sectors.add(act);
            }

        } catch (Exception ex) {
            logger.warn("Unable to get activity sectors" + ex);
        }
        return sectors;
    }

    public static Collection getAmpReportSectors(String inClause) {
        Session session = null;
        ArrayList sectors = new ArrayList();
        ArrayList activityId = new ArrayList();

        try {
            logger.debug("Team Id:" + inClause);
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select report from "
                + AmpReportCache.class.getName()
                + " report where (report.ampTeamId in(" + inClause
                + ")) and (report.reportType='1') group by report.ampActivityId";
            Query qry = session.createQuery(queryString);
            Iterator itr = qry.list().iterator();
            inClause = null;
            while (itr.hasNext()) {
                AmpReportCache ampReportCache = (AmpReportCache) itr.next();
                if (inClause == null)
                    inClause = "'" + ampReportCache.getAmpActivityId() + "'";
                else
                    inClause = inClause + ",'"
                        + ampReportCache.getAmpActivityId() + "'";
            }
            logger.debug("Activity Id:" + inClause);
            queryString = "select sector from "
                + AmpReportSector.class.getName()
                + " sector where (sector.ampActivityId in(" + inClause
                + ")) order by sector.sectorName,sector.ampActivityId";
            AmpProjectBySector ampProjectBySector = null;
            //			logger.debug("Query String: " + queryString);
            qry = session.createQuery(queryString);
            //			qry.setParameter("ampTeamId",ampTeamId,Hibernate.LONG);
            //			logger.debug("Size: " + qry.list().size());
            itr = qry.list().iterator();
            int flag = 0;
            while (itr.hasNext()) {
                AmpReportSector ampReportSector = (AmpReportSector) itr.next();
                if (ampProjectBySector == null) {
//					logger.debug("Start: ");
                    ampProjectBySector = new AmpProjectBySector();
                    ampProjectBySector.setAmpActivityId(new ArrayList());
                } else if (! (ampProjectBySector.getSector().getAmpSectorId()
                              .equals(ampReportSector.getAmpSectorId()))) {
                    ampProjectBySector.getAmpActivityId().addAll(activityId);
                    sectors.add(ampProjectBySector);
                    ampProjectBySector = new AmpProjectBySector();
                    ampProjectBySector.setAmpActivityId(new ArrayList());
                    activityId.clear();
                    flag = 0;
                }
                if (flag == 0) {
                    ampProjectBySector.setSector(ampReportSector);
                    flag = 1;
                }
                if (activityId.indexOf(ampReportSector.getAmpActivityId()) == -1) {
//					logger.debug("Id: " + ampReportSector.getAmpActivityId());
                    activityId.add(ampReportSector.getAmpActivityId());
                }
                if (!itr.hasNext()) {
                    ampProjectBySector.getAmpActivityId().addAll(activityId);
                    sectors.add(ampProjectBySector);
                }
            }

            logger.debug("Sectors size: " + sectors.size());

        } catch (Exception ex) {
            logger.error("Unable to get activity sectors" + ex.getMessage());
        }
        return sectors;
    }

    public static Collection getAmpReportLocation(Long ampActivityId) {
        Session session = null;
        Collection regions = new ArrayList();

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select a from "
                + AmpReportLocation.class.getName() + " a "
                + "where (a.ampActivityId=:ampActivityId)";
            Query qry = session.createQuery(queryString);
            qry.setParameter("ampActivityId", ampActivityId, Hibernate.LONG);
            Iterator itr = qry.list().iterator();
            while (itr.hasNext()) {
                AmpReportLocation act = (AmpReportLocation) itr.next();
                if (act.getRegion() != null)
                    regions.add(act.getRegion());
            }

        } catch (Exception ex) {
            logger.error("Unable to get activity sectors" + ex);
        }
        return regions;
    }

    public static AmpLevel getAmpLevel(Long id) {
        Session session = null;
        AmpLevel level = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select l from " + AmpLevel.class.getName()
                + " l " + "where (l.ampLevelId=:id)";
            Query qry = session.createQuery(queryString);
            qry.setParameter("id", id, Hibernate.LONG);
            Iterator itr = qry.list().iterator();
            if (itr.hasNext()) {
                level = (AmpLevel) itr.next();
            }

        } catch (Exception ex) {
            logger.error("Unable to get Level" + ex);
        }
        return level;
    }

    // filterFlag, adjustmentFlag, CurrencyCode, calendarId, region,
    // modalityId,donorId(orgId)
    // statusId, sectorId
    /*public static String[] setFilterDetails(FilterProperties filter) {
        logger.debug("In setFilterDetails(FilterProperties filter) Function");
        Session session = null;
        String names = "";
        String name[] = new String[2];
        try {
            Query q = null;
            session = PersistenceManager.getRequestDBSession();

            logger.debug("In setFilterDetails()");
            logger
                .info(filter.getAmpTeamId() + " : "
                      + filter.getCurrencyCode() + " :"
                      + filter.getPerspective());
            logger.debug(filter.getCalendarId() + ", " + filter.getRegionId()
                         + " : " + filter.getModalityId());
            logger.debug(filter.getOrgId() + ": " + filter.getStatusId() + ": "
                         + filter.getSectorId());
            String currQ = "", q2 = "", flag = "";
            String regionName = "Region(All) - ", currName = "Currency(All) - ", calName = "Calendar(All) - ", perspective = "";
            String modName = " Financing Instrument(All) -  ", statusName = "Status(All) - ", sectorName = "Sector(All) - ", orgName = "Donor(All) - ";
            String fromYear = "", toYear = "", startDate = "", closeDate = "";
            Iterator iter = null;
            //AmpModality mod;
            AmpFiscalCalendar fisCal;
            AmpRegion region;
            AmpSector sector;
            AmpStatus status;
            AmpOrganisation org;
            AmpCurrency curr;
            if (filter.getCurrencyCode().length() == 1) {
                currName = "Currency(All) - ";
                logger.debug("Currency is 0");
            } else {
                currName = "Currency(" + filter.getCurrencyCode() + ") - ";
                logger.debug("Currency is NOt 0 : "
                             + filter.getCurrencyCode().length());
            }

            //			 Gets the Organisation name corresponding to the Modality Id
            currQ = "select report from " + AmpOrganisation.class.getName()
                + " report where (report.ampOrgId=:orgId)";
            q = session.createQuery(currQ);
            q.setParameter("orgId", filter.getOrgId(), Hibernate.LONG);
            if (q != null) {
                iter = q.list().iterator();
                while (iter.hasNext()) {
                    org = (AmpOrganisation) iter.next();
                    logger.debug(" Organiation Name : " + org.getOrgCode());
                    orgName = "Donor(" + org.getOrgCode() + ") - ";
                    flag = "found";
                    break;
                }
            }

            // Gets the Modality name corresponding to the Modality Id
            currQ = "select report from " + AmpModality.class.getName()
                + " report where (report.ampModalityId=:modalityId)";
            q = session.createQuery(currQ);
            q
                .setParameter("modalityId", filter.getModalityId(),
                              Hibernate.LONG);
            if (q != null) {
                iter = q.list().iterator();
                while (iter.hasNext()) {
                    mod = (AmpModality) iter.next();
                    logger.debug(" Modality Name : " + mod.getName());
                    modName = " Financing Instrument(" + mod.getName() + ") - ";
                    flag = "found";
                    break;
                }
            }
            //			 Gets the Status Name corresponding to the Region Id
            currQ = "select report from " + AmpStatus.class.getName()
                + " report where (report.ampStatusId=:statusId)";
            q = session.createQuery(currQ);
            q.setParameter("statusId", filter.getStatusId(), Hibernate.LONG);
            if (q != null) {
                iter = q.list().iterator();
                while (iter.hasNext()) {
                    status = (AmpStatus) iter.next();
                    logger.debug(" Status Name : " + status.getName());
                    statusName = "Status(" + status.getName() + ") - ";
                    flag = "found";
                    break;
                }
            }
            //			 Gets the Sector Name corresponding to the Region Id
            currQ = "select report from " + AmpSector.class.getName()
                + " report where (report.ampSectorId=:sectorId)";
            q = session.createQuery(currQ);
            q.setParameter("sectorId", filter.getSectorId(), Hibernate.LONG);
            if (q != null) {
                iter = q.list().iterator();
                while (iter.hasNext()) {
                    sector = (AmpSector) iter.next();
                    logger.debug(" Sector Name : " + sector.getName());
                    sectorName = "Sector(" + sector.getName() + ") - ";
                    flag = "found";
                    break;
                }
            }

            //			 Gets the Calendar Name corresponding to the Calendar Id
            currQ = "select report from " + AmpFiscalCalendar.class.getName()
                + " report where (report.ampFiscalCalId=:calendarId)";
            q = session.createQuery(currQ);
            q.setParameter("calendarId", filter.getCalendarId(),
                           Hibernate.INTEGER);
            if (q != null) {
                iter = q.list().iterator();
                while (iter.hasNext()) {
                    fisCal = (AmpFiscalCalendar) iter.next();
                    logger.debug(" Calendar Name : " + fisCal.getName());
                    calName = "Calendar(" + fisCal.getName() + ") - ";
                    flag = "found";
                    break;
                }
            }

            regionName = "Region(" + filter.getRegionId() + ")";
            perspective = "Perspective(" + filter.getPerspective() + ") - ";
            fromYear = "FromYear(" + filter.getFromYear() + ") - ";
            toYear = "ToYear(" + filter.getToYear() + ") - ";
            if (filter.getStartDate() == null)
                startDate = "StartDate(Not Selected) - ";
            else
                startDate = "StartDate(" + filter.getStartDate() + ") - ";
            if (filter.getCloseDate() == null)
                closeDate = "CloseDate(Not Selected)";
            else
                closeDate = "CloseDate(" + filter.getCloseDate() + ") - ";

            name[0] = perspective + currName + calName + fromYear + toYear
                + orgName + regionName;
            name[1] = modName + statusName + sectorName + startDate + closeDate;
            //names = perspective + currName + calName + fromYear + toYear +
            // orgName + regionName + modName + statusName + sectorName
            // +startDate + closeDate;

        } catch (Exception e) {
            logger.debug("Exception in filterDetails : " + e);
            e.printStackTrace(System.out);
        }
        logger.debug("Before Return " + name);
        logger.debug("End of setFilterDetails()");
        return (name);
    } // End of SetFilterDetails Function
*/
    public static Collection getAllLevels() {
        Session session = null;
        Collection col = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select c from " + AmpLevel.class.getName()
                + " c";
            Query qry = session.createQuery(queryString);
            col = qry.list();
        } catch (Exception e) {
            logger.debug("Exception from getAllLevels()");
            logger.debug(e.toString());
        }
        return col;
    }

    public static Collection getAllActivityStatus() {
        Session session = null;
        Collection col = new ArrayList();

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select c from " + AmpStatus.class.getName() + " c";
            Query qry = session.createQuery(queryString);
            col = qry.list();
        } catch (Exception e) {
            logger.debug("Exception from getAllActivityStatus()");
            logger.debug(e.toString());
        }
        return col;
    }

    public static Collection getAllTermAssist() {
        Session session = null;
        Collection col = new ArrayList();

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select c from " + AmpTermsAssist.class.getName() + " c";
            Query qry = session.createQuery(queryString);
            col = qry.list();
        } catch (Exception e) {
            logger.debug("Exception from getAllTermAssist()");
            logger.debug(e.toString());
        }
        return col;
    }

    /*public static Collection getAllFinancingInstruments() {
        Session session = null;
        Collection col = new ArrayList();

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select c from " + AmpModality.class.getName() + " c";
            Query qry = session.createQuery(queryString);
            col = qry.list();
        } catch (Exception e) {
            logger.debug("Exception from getAllFinancingInstruments() : " + e);
            e.printStackTrace(System.out);
        }
        return col;
    }*/

    public static Collection<AmpCategoryValue> getAllFinancingInstruments() {
    	return CategoryManagerUtil.getAmpCategoryValueCollectionByKey(
    				CategoryConstants.FINANCING_INSTRUMENT_KEY, null);
    }

    public static Collection getAllDonorOrgs() {
        Session session = null;
        Collection col = new ArrayList();

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select distinct org.ampDonorOrgId from " + AmpFunding.class.getName()
                + " org order by org.ampDonorOrgId.acronym asc";
            Query qry = session.createQuery(queryString);
            col = qry.list();
        } catch (Exception ex) {
            logger.debug("Exception from getAllDonorOrgs() : " + ex);
            ex.printStackTrace(System.out);
        }
        return col;
    }

    public static Collection getAllOrgGrpBeeingUsed(){
        Session session = null;
        Collection col = new ArrayList();

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select distinct c.orgGrpId from " + AmpOrganisation.class.getName()
                + " c order by c.orgGrpId.orgGrpName";
            Query qry = session.createQuery(queryString);
            col = qry.list();
        } catch (Exception e) {
            logger.debug("Exception from getAllOrgGrpBeeingUsed()");
            logger.debug(e.toString());
        }
        return col;

    }

    public static Collection getAllOrgGroups() {
        Session session = null;
        Collection col = new ArrayList();

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select c from " + AmpOrgGroup.class.getName()
                + " c order by org_grp_name asc";
            Query qry = session.createQuery(queryString);
            col = qry.list();
        } catch (Exception e) {
            logger.debug("Exception from getAllOrgGroups()");
            logger.debug(e.toString());
        }
        return col;
    }

    public static Collection getAllOrgTypes() {
        Session session = null;
        Collection col = new ArrayList();

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select c from " + AmpOrgType.class.getName()
                + " c order by org_type asc";
            Query qry = session.createQuery(queryString);
            col = qry.list();
        } catch (Exception e) {
            logger.debug("Exception from getAllOrgTypes()");
            logger.debug(e.toString());
        }
        return col;
    }

    public static AmpOrgType getAmpOrgType(Long ampOrgTypeId) {
        Session session = null;
        Query qry = null;
        AmpOrgType ampOrgType = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select f from " + AmpOrgType.class.getName()
                + " f where (f.ampOrgTypeId=:ampOrgTypeId)";
            qry = session.createQuery(queryString);
            qry.setParameter("ampOrgTypeId", ampOrgTypeId, Hibernate.LONG);
            Iterator itr = qry.list().iterator();
            if (itr.hasNext()) {
                ampOrgType = (AmpOrgType) itr.next();
            }
        } catch (Exception e) {
            logger.error("Unable to get Org Type");
            logger.debug("Exceptiion " + e);
        }
        return ampOrgType;
    }

    public static AmpOrgGroup getAmpOrgGroup(Long id) {
        Session session = null;
        AmpOrgGroup grp = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select l from " + AmpOrgGroup.class.getName()
                + " l " + "where (l.ampOrgGrpId=:id)";
            Query qry = session.createQuery(queryString);
            qry.setParameter("id", id, Hibernate.LONG);
            Iterator itr = qry.list().iterator();
            if (itr.hasNext()) {
                grp = (AmpOrgGroup) itr.next();
            }

        } catch (Exception ex) {
            logger.error("Unable to get Org Group" + ex);
        }
        return grp;
    }

    public static Collection<AmpOrgGroup> searchForOrganisationGroupByType(Long orgType) {
        Session session = null;
        Collection col = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select distinct org from "
                + AmpOrgGroup.class.getName() + " org "
                + " where org.orgType=:orgType";
            Query qry = session.createQuery(queryString);
            qry.setParameter("orgType", orgType, Hibernate.LONG);
            col = qry.list();
        } catch (Exception ex) {
            logger.debug("Unable to search " + ex);
        }
        return col;
    }
    
    public static Collection<AmpOrgGroup> searchForOrganisationGroup(String keyword, Long orgType) {
        Session session = null;
        Collection col = null;
        keyword = keyword.toLowerCase();

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select distinct org from "
                + AmpOrgGroup.class.getName() + " org "
                + " where (lower(org.orgGrpName) like '%" + keyword + "%') and org.orgType=:orgType";
            Query qry = session.createQuery(queryString);
            qry.setParameter("orgType", orgType, Hibernate.LONG);
            col = qry.list();
        } catch (Exception ex) {
            logger.debug("Unable to search " + ex);
        }
        return col;
    }
    public static Collection<AmpOrgGroup> searchForOrganisationGroup(String keyword) {
        Session session = null;
        Collection col = null;
        keyword=keyword.toLowerCase();

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select distinct org from "
                + AmpOrgGroup.class.getName() + " org "
                + " where lower(org.orgGrpName) like '%" + keyword + "%'";
            Query qry = session.createQuery(queryString);
            col = qry.list();
        } catch (Exception ex) {
            logger.debug("Unable to search " + ex);
        }
        return col;
    }
    
    public static Collection<AmpOrgGroup> getAllOrganisationGroup() {
        Session session = null;
        Query qry = null;
        Collection organisation = new ArrayList();

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select o from " + AmpOrgGroup.class.getName()
                + " o order by org_grp_name asc";
            qry = session.createQuery(queryString);
            organisation = qry.list();
        } catch (Exception e) {
            logger.error("Unable to get all organisation groups");
            logger.debug("Exceptiion " + e);
        }
        return organisation;
    }

    public static Collection getOrgByGroup(Long Id) {

        Session sess = null;
        Collection col = new ArrayList();
        Query qry = null;

        try {
            sess = PersistenceManager.getRequestDBSession();
            String queryString = "select o from "
                + AmpOrganisation.class.getName()
                + " o where (o.orgGrpId=:orgGrpId)";
            qry = sess.createQuery(queryString);
            qry.setParameter("orgGrpId", Id, Hibernate.LONG);
            col = qry.list();
        } catch (Exception e) {
            logger.debug("Exception from getOrgByGroup(): " + e);
            e.printStackTrace(System.out);
        }
        return col;
    }

    public static boolean chkOrgTypeReferneces(Long Id) {

        Session sess = null;
        Query qry = null;
        String queryString = null;

        try {
            sess = PersistenceManager.getRequestDBSession();
            queryString = "select o from " + AmpOrganisation.class.getName()
                + " o where (o.orgTypeId=:orgTypeId)";
            qry = sess.createQuery(queryString);
            qry.setParameter("orgTypeId", Id, Hibernate.LONG);
            if (null != qry.list() && qry.list().size() > 0)
                return true;
            else {
                queryString = "select o from " + AmpOrgGroup.class.getName()
                    + " o where (o.orgType=:orgTypeId)";
                qry = sess.createQuery(queryString);
                qry.setParameter("orgTypeId", Id, Hibernate.LONG);
                if (null != qry.list() && qry.list().size() > 0)
                    return true;
            }
        } catch (Exception e) {
            logger.debug("Exception from chkOrgTypeReferneces(): " + e);
            e.printStackTrace(System.out);
        }
        return false;
    }

    public static AmpOrgType getOrgType(Long typeId) {

        Session sess = null;
        Query qry = null;
        Collection col = new ArrayList();
        AmpOrgType ot = new AmpOrgType();

        try {
            sess = PersistenceManager.getRequestDBSession();
            String queryString = "select o from " + AmpOrgType.class.getName()
                + " o where (o.ampOrgTypeId=:typeId)";
            qry = sess.createQuery(queryString);
            qry.setParameter("typeId", typeId, Hibernate.LONG);
            col = qry.list();
            Iterator itr = col.iterator();
            while (itr.hasNext()) {
                ot = (AmpOrgType) itr.next();
            }
        } catch (Exception e) {
            logger.debug("Exception from getOrgType() : " + e.getMessage());
        }
        return ot;
    }

    public static Collection getOrgByCode(String action, String code, Long id) {

        Session sess = null;
        Collection col = new ArrayList();
        Query qry = null;
        String queryString;

        try {
            sess = PersistenceManager.getRequestDBSession();
            if ("create".equals(action)) {
                queryString = "select o from "
                    + AmpOrganisation.class.getName()
                    + " o where (o.orgCode=:code)";
                qry = sess.createQuery(queryString);
                qry.setParameter("code", code, Hibernate.STRING);
            } else if ("edit".equals(action)) {
                queryString = "select o from "
                    + AmpOrganisation.class.getName()
                    + " o where (o.orgCode=:code) and (o.ampOrgId!=:id)";
                qry = sess.createQuery(queryString);
                qry.setParameter("code", code, Hibernate.STRING);
                qry.setParameter("id", id, Hibernate.LONG);
            }
            col = qry.list();
        } catch (Exception e) {
            logger.debug("Exception from getOrgByCode()");
            logger.debug(e.toString());
        }
        return col;
    }

    public static Collection getOrganisationAsCollection(Long id) {
        Session session = null;
        Collection org = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select o from "
                + AmpOrganisation.class.getName() + " o "
                + "where (o.ampOrgId=:id)";
            Query qry = session.createQuery(queryString);
            qry.setParameter("id", id, Hibernate.LONG);
            org = qry.list();
        } catch (Exception ex) {
            logger.error("Unable to get organisation from database", ex);
        }
        logger.debug("Getting organisation successfully ");
        return org;
    }

    public static AmpField getAmpFieldByName(String com) {
        Session session = null;
        Query qry = null;
        AmpField comments = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select o from " + AmpField.class.getName()
                + " o " + "where (o.fieldName=:com)";
            qry = session.createQuery(queryString);
            qry.setParameter("com", com, Hibernate.STRING);
            Iterator itr = qry.list().iterator();
            if (itr.hasNext()) {
                comments = (AmpField) itr.next();
            }
        } catch (Exception e) {
            logger.error("Unable to get all comments");
            logger.debug("Exceptiion " + e);
        }
        return comments;
    }

    public static AmpField getAmpFieldById(Long fieldId) {
        Session session = null;
        Query qry = null;
        AmpField field = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select o from " + AmpField.class.getName()
                + " o " + "where (o.ampFieldId=:id)";
            qry = session.createQuery(queryString);
            qry.setParameter("id", fieldId, Hibernate.STRING);
            Iterator itr = qry.list().iterator();
            if (itr.hasNext()) {
                field = (AmpField) itr.next();
            }
        } catch (Exception e) {
            logger.error("Unable to get field");
            logger.debug("Exceptiion " + e);
        }
        return field;
    }

    public static Collection getAmpFields() {
        Session session = null;
        Query qry = null;
        Collection colAux = new ArrayList();

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select o from " + AmpField.class.getName()
                + " o ";
            qry = session.createQuery(queryString);
            //colAux = qry.list();
            Iterator itr = qry.list().iterator();
            while (itr.hasNext()) {
                AmpField af = (AmpField) itr.next();
                colAux.add(af);
            }
        } catch (Exception e) {
            logger.error("Unable to get all comments");
            logger.debug("Exceptiion " + e);
        }
        return colAux;
    }

    public static ArrayList getAllCommentsByField(Long fid, Long aid) {
        Session session = null;
        Query qry = null;
        ArrayList comments = new ArrayList();

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select o from " + AmpComments.class.getName()
                + " o "
                + "where (o.ampFieldId=:fid) and (o.ampActivityId=:aid)";
            qry = session.createQuery(queryString);
            qry.setParameter("fid", fid, Hibernate.LONG);
            qry.setParameter("aid", aid, Hibernate.LONG);
            Iterator itr = qry.list().iterator();
            while (itr.hasNext()) {
                AmpComments com = (AmpComments) itr.next();
                comments.add(com);
            }
        } catch (Exception e) {
            logger.error("Unable to get all comments");
            logger.debug("Exceptiion " + e);
        }
        return comments;
    }

    public static ArrayList getAllCommentsByActivityId(Long aid) {
        Session session = null;
        Query qry = null;
        ArrayList comments = new ArrayList();

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select o from " + AmpComments.class.getName()
                + " o "
                + "where (o.ampActivityId=:aid)";
            qry = session.createQuery(queryString);
            qry.setParameter("aid", aid, Hibernate.LONG);
            Iterator itr = qry.list().iterator();
            while (itr.hasNext()) {
                AmpComments com = (AmpComments) itr.next();
                comments.add(com);
            }
        } catch (Exception e) {
            logger.error("Unable to get all comments");
            logger.debug("Exceptiion " + e);
        }
        return comments;
    }
    
    public static ArrayList getAllIPAContractsByActivityId(Long aid) {
        Session session = null;
        Query qry = null;
        ArrayList contracts = new ArrayList();

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select o from " + IPAContract.class.getName()
                + " o "
                + "where (o.activity=:aid)";
            qry = session.createQuery(queryString);
            qry.setParameter("aid", aid, Hibernate.LONG);
            Iterator itr = qry.list().iterator();
            while (itr.hasNext()) {
            	IPAContract com = (IPAContract) itr.next();
            	contracts.add(com);
            }
        } catch (Exception e) {
            logger.error("Unable to get all contracts");
            logger.debug("Exceptiion " + e);
        }
        return contracts;
    }
    

    public static AmpComments getAmpComment(Long id) {
        Session session = null;
        AmpComments comment = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select l from " + AmpComments.class.getName()
                + " l " + "where (l.ampCommentId=:id)";
            Query qry = session.createQuery(queryString);
            qry.setParameter("id", id, Hibernate.LONG);
            Iterator itr = qry.list().iterator();
            if (itr.hasNext()) {
                comment = (AmpComments) itr.next();
            }

        } catch (Exception ex) {
            logger.error("Unable to get comment" + ex);
        }
        return comment;
    }

    public static int getAmpMaxToYear(Long ampTeamId) {
        Session session = null;
        Query q = null;
        Integer year = null;
        String queryString = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            queryString = " select extract(YEAR from max(afd.transactionDate)) from " + AmpFundingDetail.class.getName()
                + " afd," + AmpFunding.class.getName() + " af," + AmpActivity.class.getName() + " aa where (afd.ampFundingId=af.ampFundingId) and af.ampActivityId=aa.ampActivityId and aa.team.ampTeamId='" + ampTeamId + "'";
            q = session.createQuery(queryString);
            year = (Integer) q.list().get(0);

        } catch (Exception ex) {
            logger.error("Unable to get Amp status   from database "
                         + ex.getMessage());
        }
        return year.intValue();
    }

    public static ArrayList getAmpReportPhysicalPerformance(Long ampActivityId) {
        Session session = null;
        ArrayList progress = new ArrayList();

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select a from "
                + AmpReportPhysicalPerformance.class.getName() + " a "
                + "where (a.ampActivityId=:ampActivityId)";
            Query qry = session.createQuery(queryString);
            qry.setParameter("ampActivityId", ampActivityId, Hibernate.LONG);
            Iterator itr = qry.list().iterator();
            while (itr.hasNext()) {
                AmpReportPhysicalPerformance act = (AmpReportPhysicalPerformance) itr.next();
                progress.add(act.getTitle() + " : " + act.getDescription());
            }

        } catch (Exception ex) {
            logger.error("Unable to get activity sectors" + ex);
        }
        return progress;
    }

    public static Group getGroup(String key, Long siteId) {
        Session session = null;
        Group group = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            String qryStr = "select grp from " + Group.class.getName() + " grp " +
                "where (grp.key=:key) and (grp.site=:sid)";
            Query qry = session.createQuery(qryStr);
            qry.setParameter("key", key, Hibernate.STRING);
            qry.setParameter("sid", siteId, Hibernate.LONG);
            Iterator itr = qry.list().iterator();
            if (itr.hasNext()) {
                group = (Group) itr.next();
            }
        } catch (Exception ex) {
            logger.error("Unable to get Group "
                         + ex.getMessage());
        }
        return group;
    }

    public static ArrayList getApprovedActivities(String inClause) {
        ArrayList actList = new ArrayList();
        Session session = null;
        Query q = null;
        String queryString;
        try {

            session = PersistenceManager.getRequestDBSession();

            queryString = "select act.ampActivityId from " + AmpActivity.class.getName()
                + " act where (act.team.ampTeamId in(" + inClause + ")) and (act.approvalStatus=:status)";
            q = session.createQuery(queryString);
            q.setParameter("status", "approved", Hibernate.STRING);
            actList = (ArrayList) q.list();
            //logger.debug("Approved Activity List Size: " + actList.size());

        } catch (Exception ex) {
            logger.error("Unable to get AmpActivity [getApprovedActivities()]", ex);
        }
        logger.debug("Getting Approved activities Executed successfully ");
        return actList;
    }

    public static ArrayList getAmpDonors(String inClause) {
        ArrayList donor = new ArrayList();
        Session session = null;
        Query q = null;
        Iterator iterActivity = null;
        Iterator iter = null;

        try {

            session = PersistenceManager.getRequestDBSession();
            String queryString = new String();
            queryString = "select activity from " + AmpActivity.class.getName()
                + " activity where activity.team.ampTeamId in(" + inClause
                + ")";
            q = session.createQuery(queryString);
            //logger.debug("Activity List: " + q.list().size());
            iterActivity = q.list().iterator();
            while (iterActivity.hasNext()) {
                AmpActivity ampActivity = (AmpActivity) iterActivity.next();

//				logger.debug("Org Role List: " + ampActivity.getOrgrole().size());
                iter = ampActivity.getOrgrole().iterator();
                while (iter.hasNext()) {
                    AmpOrgRole ampOrgRole = (AmpOrgRole) iter.next();
                    if (ampOrgRole.getRole().getRoleCode().equals(
                        Constants.FUNDING_AGENCY)) {
                        if (donor.indexOf(ampOrgRole.getOrganisation()) == -1)
                            donor.add(ampOrgRole.getOrganisation());
                    }
                }
            }
            logger.debug("Donors: " + donor.size());
            int n = donor.size();
            for (int i = 0; i < n - 1; i++) {
                for (int j = 0; j < n - 1 - i; j++) {
                    AmpOrganisation firstOrg = (AmpOrganisation) donor.get(j);
                    AmpOrganisation secondOrg = (AmpOrganisation) donor
                        .get(j + 1);
                    if (firstOrg.getAcronym().compareToIgnoreCase(
                        secondOrg.getAcronym()) > 0) {
                        AmpOrganisation tempOrg = firstOrg;
                        donor.set(j, secondOrg);
                        donor.set(j + 1, tempOrg);
                    }
                }
            }

        } catch (Exception ex) {
            logger.debug("Unable to get Donor " + ex.getMessage());
        }
        return donor;
    }

    public static ArrayList getAmpDonorsForActivity(Long id) {
        ArrayList donor = new ArrayList();
        Session session = null;
        Query q = null;
        Iterator iterActivity = null;
        Iterator iter = null;

        try {

            session = PersistenceManager.getRequestDBSession();
            String queryString = new String();
            queryString = "select activity from " + AmpActivity.class.getName()
                + " activity where (activity.ampActivityId=:id)";

            q = session.createQuery(queryString);
            q.setParameter("id", id, Hibernate.LONG);
            //logger.debug("Activity List: " + q.list().size());
            iterActivity = q.list().iterator();
            while (iterActivity.hasNext()) {
                AmpActivity ampActivity = (AmpActivity) iterActivity.next();

                // logger.debug("Org Role List: " + ampActivity.getOrgrole().size());
                iter = ampActivity.getOrgrole().iterator();
                while (iter.hasNext()) {
                    AmpOrgRole ampOrgRole = (AmpOrgRole) iter.next();
                    if (ampOrgRole.getRole().getRoleCode().equals(
                        Constants.FUNDING_AGENCY)) {
                        if (donor.indexOf(ampOrgRole.getOrganisation()) == -1)
                            donor.add(ampOrgRole.getOrganisation());
                    }
                }
            }
            logger.debug("Donors: " + donor.size());
            int n = donor.size();
            for (int i = 0; i < n - 1; i++) {
                for (int j = 0; j < n - 1 - i; j++) {
                    AmpOrganisation firstOrg = (AmpOrganisation) donor.get(j);
                    AmpOrganisation secondOrg = (AmpOrganisation) donor
                        .get(j + 1);
                    if (firstOrg.getAcronym().compareToIgnoreCase(
                        secondOrg.getAcronym()) > 0) {
                        AmpOrganisation tempOrg = firstOrg;
                        donor.set(j, secondOrg);
                        donor.set(j + 1, tempOrg);
                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            logger.debug("Unable to get Donor " + ex.getMessage());
        }
        //return donor;

        ArrayList donorString = new ArrayList();
        Iterator i = donor.iterator();
        while (i.hasNext()) {
            AmpOrganisation element = (AmpOrganisation) i.next();
            donorString.add(element.getName());
        }

        return donorString;
    }

    public static Collection getAllAhSurveys() {
        Session session = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String qry = "select svy from " + AmpAhsurvey.class.getName() + " svy";
            Query q = session.createQuery(qry);
            return q.list();
        } catch (Exception ex) {
            logger.debug("Unable to get survey : " + ex.getMessage());
            ex.printStackTrace(System.out);
        }
        return null;
    }
    public static Collection getAllSurveysByActivity(Long activityId) {
        ArrayList survey = new ArrayList();
        Set fundingSet = new HashSet();
        Set surveySet = new HashSet();
        ArrayList donorOrgs = new ArrayList();
        Session session = null;
        Iterator iter1 = null;
        Iterator iter2 = null;
        Transaction tx = null;
        boolean flag1 = false;

        try {
        	if(activityId==null) return survey;
            session = PersistenceManager.getRequestDBSession();

            if(activityId==null || activityId.longValue()==0) return survey;
            AmpActivity activity = (AmpActivity) session.load(AmpActivity.class, activityId);
            fundingSet = activity.getFunding();
            surveySet = activity.getSurvey();
            //logger.debug("fundingSet.size() : " + fundingSet.size());
            //logger.debug("surveySet.size() : " + surveySet.size());
            if (surveySet.size() < 1)
                flag1 = true;
            if (fundingSet.size() < 1)
                return survey;
            else {
                // adding a survey per donor, having at least one funding for this activity, if there is none
                // or if a new donor with funding is added.
                boolean flag2 = true;
                tx = session.beginTransaction();
                iter1 = fundingSet.iterator();
                while (iter1.hasNext()) {
                    AmpFunding ampFund = (AmpFunding) iter1.next();
                    donorOrgs.add(ampFund.getAmpDonorOrgId());
                    if (!flag1) {
                        iter2 = surveySet.iterator();
                        while (iter2.hasNext()) {
                            AmpAhsurvey ahs = (AmpAhsurvey) iter2.next();
                            if (ahs.getAmpDonorOrgId().equals(ampFund.getAmpDonorOrgId())) {
                                flag2 = false;
                                break;
                            }
                        }
                    }
                    if (flag1 || flag2) {
                        AmpAhsurvey ahsvy = new AmpAhsurvey();
                        ahsvy.setAmpActivityId(activity);
                        ahsvy.setAmpDonorOrgId(ampFund.getAmpDonorOrgId());
                        activity.getSurvey().add(ahsvy);
                        flag1 = false;
                    }
                    flag2 = true;
                }

                iter2 = surveySet.iterator();
                while (iter2.hasNext()) {
                    AmpAhsurvey ahs = (AmpAhsurvey) iter2.next();
                    if (ahs.getPointOfDeliveryDonor() == null) {
                        ahs.setPointOfDeliveryDonor(ahs.getAmpDonorOrgId());
                    }
                }
                session.update(activity);
                tx.commit();

                if (activity.getSurvey().isEmpty())
                    logger.debug("activity.getSurvey() is empty.");
                else {
                    //logger.debug("activity.getSurvey().size() : " + activity.getSurvey().size());
                    //logger.debug("donorOrgs.size() : " + donorOrgs.size());
                    iter2 = activity.getSurvey().iterator();
                    while (iter2.hasNext()) {
                        AmpAhsurvey svy = (AmpAhsurvey) iter2.next();
                        // getting only those survey records where donor-org is in current funding list
                        if (donorOrgs.indexOf(svy.getAmpDonorOrgId()) != -1) {
                            SurveyFunding svfund = new SurveyFunding();
                            svfund.setSurveyId(svy.getAmpAHSurveyId());
                            svfund.setFundingOrgName(svy.getAmpDonorOrgId().getName());
                            if (svy.getPointOfDeliveryDonor() != null) {
                                svfund.setDeliveryDonorName(svy.getPointOfDeliveryDonor().getName());
                            } else {
                                svfund.setDeliveryDonorName(svy.getAmpDonorOrgId().getName());
                            }

                            survey.add(svfund);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            if (tx != null) {
                try {
                    tx.rollback();
                } catch (HibernateException e) {
                    logger.debug("rollback() failed : " + e.getMessage());
                }
            }
            logger.debug("Unable to get survey : " + ex.getMessage());
            ex.printStackTrace(System.out);
        }
        logger.debug("survey.size() : " + survey.size());
        return survey;
    }

    public static List getResposesBySurvey(Long surveyId, Long activityId) {
        ArrayList responses = new ArrayList();
        Set response = new HashSet();
        Collection fundingSet = new ArrayList();
        Session session = null;
        Iterator iter1 = null;
        boolean flag = true;

        try {
            session = PersistenceManager.getRequestDBSession();
            String qry = "select indc from " + AmpAhsurveyIndicator.class.getName()
                + " indc order by indicator_number asc";
            Collection indicatorColl = session.createQuery(qry).list();
            //logger.debug("indicatorColl.size() : " + indicatorColl.size());

            AmpAhsurvey svy = (AmpAhsurvey) session.get(AmpAhsurvey.class, surveyId);
            //response = svy.getResponses();
            qry = "select res from " + AmpAhsurvey.class.getName()
                + " res left join fetch res.responses where (res.ampAHSurveyId=:surveyId)";
            Query query = session.createQuery(qry);
            query.setParameter("surveyId", surveyId, Hibernate.LONG);
            response = ( (AmpAhsurvey) query.list().get(0)).getResponses();
            //logger.debug("response.size() : " + response.size());

            qry = "select fund from " + AmpFunding.class.getName()
                + " fund where (fund.ampDonorOrgId=:donorId) and (fund.ampActivityId=:activityId)";
            query = session.createQuery(qry);
            query.setParameter("donorId", svy.getAmpDonorOrgId().getAmpOrgId(), Hibernate.LONG);
            query.setParameter("activityId", svy.getAmpActivityId().getAmpActivityId(), Hibernate.LONG);
            fundingSet = query.list();
            //logger.debug("fundingSet.size() : " + fundingSet.size());

            if (response.size() < 1) // new survey
                flag = false;
            iter1 = indicatorColl.iterator();
            Iterator iter2 = null;
            boolean ansFlag = false;
            while (iter1.hasNext()) {
                AmpAhsurveyIndicator indc = (AmpAhsurveyIndicator) iter1.next();
                Indicator ind = new Indicator();
                ind.setIndicatorCode(indc.getIndicatorCode());
                ind.setName(indc.getName());
                ind.setQuestion(new ArrayList());
                iter2 = session.createFilter(indc.getQuestions(), "order by this.questionNumber asc").list().iterator();
                //iter2 = session.createFilter(((AmpAhsurveyIndicator) session.load(AmpAhsurveyIndicator.class, indc.getAmpIndicatorId())).getQuestions(),
                //			"order by this.questionNumber asc").list().iterator();
                Iterator iter3 = null;
                while (iter2.hasNext()) {
                    AmpAhsurveyQuestion q = (AmpAhsurveyQuestion) iter2.next();
                    Question ques = new Question();
                    ques.setQuestionType(q.getAmpTypeId().getName());
                    ques.setQuestionId(q.getAmpQuestionId());
                    ques.setQuestionText(q.getQuestionText());
                    if (flag) { // response is blank in case of new survey
                        iter3 = response.iterator();
                        while (iter3.hasNext()) {
                            AmpAhsurveyResponse res = (AmpAhsurveyResponse) iter3.next();
                            if (res.getAmpQuestionId().getAmpQuestionId().equals(q.getAmpQuestionId())) {
                                if (q.getQuestionNumber().intValue() == 1) {
                                    if ("yes".equalsIgnoreCase(res.getResponse()))
                                        ansFlag = true;
                                }

                                /* -------------------------------- Defunct now ------------------------------------- */
                                // if answer to question #1 of survey is yes then calculate
                                // difference(%) between planned & actual disbursement(s)
                                if ("calculated".equalsIgnoreCase(q.getAmpTypeId().getName())) {
                                    if (q.getQuestionNumber().intValue() == 10) {
                                        if (ansFlag) {
                                            Iterator itr4 = fundingSet.iterator();
                                            Iterator itr5 = null;
                                            double actual = 0.0;
                                            double planned = 0.0;
                                            AmpFundingDetail fd = null;
                                            while (itr4.hasNext()) {
                                                AmpFunding ampf = (AmpFunding) itr4.next();
                                                itr5 = ampf.getFundingDetails().iterator();
                                                while (itr5.hasNext()) {
                                                    fd = (AmpFundingDetail) itr5.next();
                                                    if (fd.getTransactionType().intValue() == 1) {
                                                        if (fd.getAdjustmentType().intValue() == 0)
                                                            planned += fd.getTransactionAmount().floatValue();
                                                        else if (fd.getAdjustmentType().intValue() == 1)
                                                            actual += fd.getTransactionAmount().floatValue();
                                                    }
                                                }
                                            }
                                            //logger.debug("actual = " + actual + "  planned = " + planned);
                                            if (planned == 0.0)
                                                res.setResponse("nil");
                                            else {
                                                NumberFormat formatter = new DecimalFormat("#.##");
                                                Double percent = new Double( (actual * 100) / planned);
                                                //logger.debug("percent = " + percent + " format(percent) : " + formatter.format(percent));
                                                res.setResponse(formatter.format(percent));
                                            }
                                        } else
                                            res.setResponse(null);
                                    }
                                }
                                /* -------------------------------- Defunct now ------------------------------------- */

                                ques.setResponse(res.getResponse());
                                ques.setResponseId(res.getAmpReponseId());
                                break;
                            }
                        }
                    }
                    ind.getQuestion().add(ques);
                }
                responses.add(ind);
            }
        } catch (Exception ex) {
            logger.debug("Unable to get survey responses : " + ex.getMessage());
            ex.printStackTrace(System.out);
        }
        logger.debug("responses.size() : " + responses.size());
        return responses;
    }

    public static AmpAhsurvey getAhSurvey(Long surveyId) {
        AmpAhsurvey survey = new AmpAhsurvey();
        Session session = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String qry = "select svy from " + AmpAhsurvey.class.getName()
                + " svy where (svy.ampAHSurveyId=:surveyId)";
            Query q = session.createQuery(qry);
            q.setParameter("surveyId", surveyId, Hibernate.LONG);
            survey = (AmpAhsurvey) q.list().get(0);
        } catch (Exception ex) {
            logger.debug("Unable to get survey : " + ex.getMessage());
            ex.printStackTrace(System.out);
        }
        return survey;
    }

    public static void updateSurvey(AmpAhsurvey survey) {
        Session session = null;
        Transaction tx = null;

        try {
        	if (survey == null || survey.getAmpAHSurveyId()==null)
        	{
        		logger.debug("The survey or AHSurvey is null ... no update for Survey");
        		return;
        	}
            session = PersistenceManager.getRequestDBSession();
            tx = session.beginTransaction();

            AmpAhsurvey oldSurvey ;
            oldSurvey = (AmpAhsurvey) session.load(AmpAhsurvey.class, survey.getAmpAHSurveyId());
            oldSurvey.setAmpActivityId(survey.getAmpActivityId());
            oldSurvey.setAmpDonorOrgId(survey.getAmpDonorOrgId());
            oldSurvey.setPointOfDeliveryDonor(survey.getPointOfDeliveryDonor());
            oldSurvey.setResponses(survey.getResponses());

            session.update(oldSurvey);
            tx.commit();
        } catch (Exception ex) {
            if (tx != null) {
                try {
                    tx.rollback();
                } catch (HibernateException e) {
                    logger.debug("rollback() failed : " + e.getMessage());
                }
            }
            logger.debug("Unable to save survey response : " + ex.getMessage());
            ex.printStackTrace(System.out);
        }
    }

    public static void saveSurveyResponses(Long surveyId, Collection indicator) {
        Session session = null;
        Transaction tx = null;
        Iterator itr1 = null;
        Iterator itr2 = null;
        boolean flag = true;
        if (surveyId == null)
    	{
    		logger.debug("The survey id is null ... no save survey response");
    		return;
    	}
        try {
            session = PersistenceManager.getRequestDBSession();
            tx = session.beginTransaction();

            AmpAhsurvey survey = (AmpAhsurvey) session.get(AmpAhsurvey.class, surveyId);
            String qry = "select count(*) from " + AmpAhsurveyResponse.class.getName()
                + " res where (res.ampAHSurveyId=:surveyId)";
            Integer resposeSize = (Integer) session.createQuery(qry)
                .setParameter("surveyId", surveyId, Hibernate.LONG)
                .uniqueResult();
            //logger.debug("Response size : " + resposeSize.intValue());
            if (resposeSize.intValue() < 1) {
                flag = false;
                logger.debug("Response set is empty");
            }

            itr1 = indicator.iterator();
            while (itr1.hasNext()) {
                itr2 = ( (Indicator) itr1.next()).getQuestion().iterator();
                while (itr2.hasNext()) {
                    Question q = (Question) itr2.next();
                    AmpAhsurveyResponse res = new AmpAhsurveyResponse();
                    if (flag)
                        // res.setAmpReponseId(q.getResponseId());
                        res = (AmpAhsurveyResponse) session.load(AmpAhsurveyResponse.class, q.getResponseId());
                    res.setAmpAHSurveyId(survey);
                    AmpAhsurveyQuestion ques = (AmpAhsurveyQuestion) session.load(AmpAhsurveyQuestion.class, q.getQuestionId());
                    res.setAmpQuestionId(ques);
                    res.setResponse(q.getResponse());
                    session.saveOrUpdate(res);
                }
            }
            tx.commit();
        } catch (Exception ex) {
            if (tx != null) {
                try {
                    tx.rollback();
                } catch (HibernateException e) {
                    logger.debug("rollback() failed : " + e.getMessage());
                }
            }
            logger.debug("Unable to save survey response : " + ex.getMessage());
            ex.printStackTrace(System.out);
        }
    }

    public static Collection getAllAhSurveyIndicators() {
        Collection responses = new ArrayList();
        Session session = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String qry = "select indc from " + AmpAhsurveyIndicator.class.getName()
                + " indc order by indicator_number asc";
            responses = session.createQuery(qry).list();

        } catch (Exception ex) {
            logger.debug("Unable to get survey indicators : " + ex.getMessage());
            ex.printStackTrace(System.out);
        }
        return responses;
    }

    public static AmpAhsurveyIndicator getIndicatorById(Long id) {
        AmpAhsurveyIndicator indc = new AmpAhsurveyIndicator();
        Session session = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String qry = "select indc from " + AmpAhsurveyIndicator.class.getName()
                + " indc where (indc.ampIndicatorId=:id)";
            indc = (AmpAhsurveyIndicator) session.createQuery(qry)
                .setParameter("id", id, Hibernate.LONG)
                .list().get(0);

        } catch (Exception ex) {
            logger.debug("Unable to get survey indicator : " + ex.getMessage());
            ex.printStackTrace(System.out);
        }
        return indc;
    }

    public static Collection getSurveyQuestionsByIndicator(Long indcId) {
        Collection responses = new ArrayList();
        Session session = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String qry = "select qs from " + AmpAhsurveyQuestion.class.getName()
                + " qs where (qs.ampIndicatorId=:indcId) order by questionNumber asc";
            responses = session.createQuery(qry)
                .setParameter("indcId", indcId, Hibernate.LONG)
                .list();

        } catch (Exception ex) {
            logger.debug("Unable to get survey indicator questions : " + ex.getMessage());
            ex.printStackTrace(System.out);
        }
        return responses;
    }

    public static Collection getAidSurveyReportByIndicator(String indcCode, String donor, String orgGroup,
        AmpCategoryValue statusCM, int startYear, int closeYear, String currency, String termAssist, AmpCategoryValue financingInstr,
        String sector, String calendar) {

        Session session = null;
        ArrayList responses = new ArrayList();
        Collection surveyDonors = new ArrayList();
        Set surveySet = new HashSet();
        boolean orgGroupFlag = false;
        int NUM_ANSWER_COLUMNS = 4;
        int YEAR_RANGE = (closeYear - startYear + 1);
        int indcFlag = 0, index = 0, j = 0, convYr = 0;
        double sum = 0.0, fromExchangeRate = 0.0, toExchangeRate = 0.0, ansToQues4 = 0.0;
        Date startDates[] = null;
        Date endDates[] = null;
        double answersRow[] = null;
        double allDnRow[] = null;
        boolean answers[] = null;
        Double percent = null;
        NumberFormat formatter = new DecimalFormat("#.##");
        String date = null;
        Iterator itr1 = null, itr2 = null, itr3 = null, itr4 = null;

        try {
            //logger.debug("indcCode[inside getAidSurveyReportByIndicator] : " + indcCode);
            session = PersistenceManager.getRequestDBSession();
            String qry = "select distinct dn.ampDonorOrgId from " + AmpAhsurvey.class.getName() + " dn";
            //String qry = "select distinct dn.pointOfDeliveryDonor from " + AmpAhsurvey.class.getName() + " dn";
            surveyDonors.addAll(session.createQuery(qry).list());
            //logger.debug("total donors from AmpOrganisation[surveyDonors] : " + surveyDonors.size());
            if (surveyDonors.size() > 0) {
                if (null != orgGroup && orgGroup.trim().length() > 1 && !"all".equalsIgnoreCase(orgGroup))
                    orgGroupFlag = true;
                if ("5a".equalsIgnoreCase(indcCode)) {
                    NUM_ANSWER_COLUMNS = 8;
                    indcFlag = 5;
                } else if ("6".equalsIgnoreCase(indcCode)) {
                    NUM_ANSWER_COLUMNS = YEAR_RANGE;
                    indcFlag = 6;
                } else if ("7".equalsIgnoreCase(indcCode))
                    indcFlag = 7;
                else if ("9".equalsIgnoreCase(indcCode)) {
                    NUM_ANSWER_COLUMNS = 5;
                    indcFlag = 9;
                }
                startDates = new Date[YEAR_RANGE];
                endDates = new Date[YEAR_RANGE];
                //logger.debug("calendar: " + calendar);
                Comparator dnComp = new Comparator() {
                    public int compare(Object o1, Object o2) {
                        AmpOrganisation r1 = (AmpOrganisation) o1;
                        AmpOrganisation r2 = (AmpOrganisation) o2;
                        AmpOrgGroup og1 = r1.getOrgGrpId();
                        AmpOrgGroup og2 = r2.getOrgGrpId();
                        if (og1 != null && og2 != null) {
                            if (og1.getOrgGrpName() != null && og2.getOrgGrpName() != null) {
                                og1.getOrgGrpName().compareTo(og2.getOrgGrpName());
                            } else {
                                return 0;
                            }
                        } else {
                            return 0;
                        }
                        return r1.getAcronym().trim().toLowerCase().compareTo(r2.getAcronym().trim().toLowerCase());
                    }
                };
                Collections.sort( (List) surveyDonors, dnComp);
                // Creating first row for all-donors in indicator report.
                ParisIndicator all = new ParisIndicator();
                all.setDonor("All Donors");
                all.setAnswers(new ArrayList());
                responses.add(all);
                if (indcFlag != 6) {
                    for (int i = 0; i < YEAR_RANGE; i++) {
                        answersRow = new double[NUM_ANSWER_COLUMNS];
                        answersRow[0] = startYear + i;
                        ( (ParisIndicator) responses.get(0)).getAnswers().add(answersRow);
                    }
                } else
                    ( (ParisIndicator) responses.get(0)).getAnswers().add(new double[NUM_ANSWER_COLUMNS]);

                itr1 = surveyDonors.iterator();
                while (itr1.hasNext()) {
                    AmpOrganisation dnOrg = (AmpOrganisation) itr1.next();
                    // Filtering by donor-organisation here
                    if (dnOrg.getOrgTypeId() != null) {
                        if (!dnOrg.getOrgTypeId().getOrgTypeCode().equalsIgnoreCase("bil") &&
                            !dnOrg.getOrgTypeId().getOrgTypeCode().equalsIgnoreCase("mul")) {
                            continue;
                        }
                    }

                    if (null != donor && donor.trim().length() > 1 && !"all".equalsIgnoreCase(donor)) {
                        if (!donor.equals(dnOrg.getAmpOrgId().toString()))
                            continue;
                    }
                    surveySet.addAll(dnOrg.getSurvey());
                    //logger.debug("dnOrg.getAmpOrgId() : " + dnOrg.getAmpOrgId() + "  dnOrg.getAcronym() : " +dnOrg.getAcronym());
                    //logger.debug("----------------------------------------------------------------------------------------------");
                    // Filtering by org-group here
                    if (orgGroupFlag) {
                        if (!orgGroup.equalsIgnoreCase(dnOrg.getOrgGrpId().getOrgGrpCode()))
                            continue;
                    }
                    ParisIndicator pi = new ParisIndicator(); // represents one row of indicator report.
                    if (dnOrg.getOrgGrpId() != null && dnOrg.getOrgGrpId().getOrgGrpName() != null) {
                        pi.setDonor(dnOrg.getOrgGrpId().getOrgGrpName());
                    } else {
                        pi.setDonor("N/A");
                    }

                    pi.setAnswers(new ArrayList());
                    if (indcFlag == 6)
                        answersRow = new double[NUM_ANSWER_COLUMNS];
                    //logger.debug("surveySet.size() : " + surveySet.size());
                    boolean[][] answersColl = getSurveyReportAnswer(indcCode, surveySet);
                    for (int i = 0; i < YEAR_RANGE; i++) {
                        if (indcFlag != 6) {
                            // answersRow will represent row for one disbursement year inside answer-collection of pi helper object.
                            answersRow = new double[NUM_ANSWER_COLUMNS];
                            answersRow[0] = (startYear + i);
                        }
                        AmpFiscalCalendar fCalendar = FiscalCalendarUtil.getAmpFiscalCalendar(Long.parseLong(calendar));

                        if (startDates[i] == null || endDates[i] == null) {
                            if (! (fCalendar.getBaseCal().equalsIgnoreCase(BaseCalendar.BASE_ETHIOPIAN.getValue()) )) {
                                startDates[i] = FiscalCalendarUtil.getCalendarStartDate(new Long(calendar), startYear + i);
                                endDates[i] = FiscalCalendarUtil.getCalendarEndDate(new Long(calendar), startYear + i);
                            }
                        }
                        //logger.debug("year: " + (startYear+i) + " startDates[" + i + "]: " + startDates[i]
                        //               + " endDates[" + i + "]: " + endDates[i]);
                        itr2 = surveySet.iterator();
                        index = 0;
                        while (itr2.hasNext()) {
                            AmpAhsurvey svy = (AmpAhsurvey) itr2.next();
                            // Filtering by activity-status here
                            if (null != statusCM) {
                                AmpCategoryValue statusValue = CategoryManagerUtil.getAmpCategoryValueFromListByKey(CategoryConstants.ACTIVITY_STATUS_KEY, svy.getAmpActivityId().getCategories());

                                if (statusValue == null || (!statusCM.getId().equals(statusValue.getId())))
                                    continue;
                                /*if (statusValue != null && !status.equalsIgnoreCase(statusValue.getValue()))
                                    ;
                                                                 continue;*/
                            }
                            // Filtering by activity-sector here
                            if (null != sector && sector.trim().length() > 1 && !"all".equalsIgnoreCase(sector)) {
                                if (null != svy.getAmpActivityId().getSectors()) {
                                    Iterator secItr = svy.getAmpActivityId().getSectors().iterator();
                                    //AmpSector sec = (AmpSector) secItr.next();
                                    AmpSector sec = ( (AmpActivitySector) secItr.next()).getSectorId();
                                    if (!sector.equals(sec.getAmpSectorId().toString()))
                                        continue;
                                }
                            }
                            if ("4".equalsIgnoreCase(indcCode)) {
                                Iterator iter = svy.getResponses().iterator();
                                while (iter.hasNext()) {
                                    AmpAhsurveyResponse resp = (AmpAhsurveyResponse) iter.next();
                                    if (4 == resp.getAmpQuestionId().getQuestionNumber().intValue()) {
                                        try {
                                            ansToQues4 = Double.parseDouble(resp.getResponse());
                                            ansToQues4 /= 100;
                                            //logger.debug("ansToQues4 : " + ansToQues4);
                                        } catch (NumberFormatException nex) {
                                            logger.debug("response to question-4 is not a number.");
                                            ansToQues4 = 0.0;
                                        }
                                        break ;
                                    }
                                }
                            }
                            answers = answersColl[index++];
                            ////System.out.println(svy.getAmpActivityId().getName());

                            AmpOrganisation pdOrg=svy.getPointOfDeliveryDonor();
                            if (pdOrg!=null && pdOrg.getOrgGrpId() != null && pdOrg.getOrgGrpId().getOrgGrpName() != null) {
                                pi.setDonor(pdOrg.getOrgGrpId().getOrgGrpName());
                            } else {
                                pi.setDonor("N/A");
                            }

                            if (null != answers) {
                                indc6Break:
                                    for (j = 0; j < answers.length; j++) {
                                    sum = 0.0;
                                    if (answers[j]) {
                                        /*
                                         if (indcFlag == 6) {
                                         convYr = (startYear + i);
                                         if (convYr == DateConversion.getYear(DateConversion.ConvertDateToString(svy.getAmpActivityId().getActualStartDate()))
                                           || convYr == DateConversion.getYear(DateConversion.ConvertDateToString(svy.getAmpActivityId().getActualCompletionDate())))
                                          answersRow[i] += 1;
                                         break;
                                                  }
                                         */
                                        itr3 = svy.getAmpActivityId().getFunding().iterator();
                                        while (itr3.hasNext()) {
                                            AmpFunding fund = (AmpFunding) itr3.next();
                                            // Only those donors are considered who have funding for the activity/project
                                            if (0 == dnOrg.getAmpOrgId().compareTo(fund.getAmpDonorOrgId().getAmpOrgId())) {
                                                // Filtering by financing-instrument here
                                                if (null != financingInstr) {
                                                    if (!financingInstr.getId().equals(fund.getFinancingInstrument().getId()))
                                                        continue;
                                                }
                                                if ("9".equalsIgnoreCase(indcCode)) {
                                                    if (j == 0)
                                                        if (!"Direct Budget Support".equalsIgnoreCase(fund.getFinancingInstrument().getValue())) {
                                                            //logger.debug("continue[indcCode=9]: because of !Direct Budget Suppor");
                                                            continue;
                                                        }
                                                    if (j == 1)
                                                        if ("Direct Budget Support".equalsIgnoreCase(fund.getFinancingInstrument().getValue())) {
                                                            //logger.debug("continue[indcCode=9]: because of Direct Budget Suppor");
                                                            continue;
                                                        }
                                                }
                                                /* Filtering by term-assist here
                                                            if (null != termAssist && termAssist.trim().length() > 1 && !"all".equalsIgnoreCase(termAssist))
                                                 if (!termAssist.equalsIgnoreCase(fund.getAmpTermsAssistId().getTermsAssistName()))
                                                  continue;
                                                 */
                                                itr4 = fund.getFundingDetails().iterator();
                                                while (itr4.hasNext()) {
                                                    AmpFundingDetail fundtl = (AmpFundingDetail) itr4.next();
                                                    // Filtering by perspective here
                                                    //if (perspective.equalsIgnoreCase(fundtl.getPerspectiveId().getCode())) {
                                                        /*
                                                                      date = DateConversion.ConvertDateToString(fundtl.getTransactionDate());
                                                                      convYr = DateConversion.getYear(date);
                                                                      // Filtering by disbursement-year here
                                                                      if (convYr == (startYear + i)) {
                                                         */
                                                        if (isValidTransactionDate(startYear + i, fundtl.getTransactionDate(), startDates[i], endDates[i])) {
                                                            // Filtering by AdjustmentType & TransactionType here -
                                                            // only 'Actual Disbursement' is considered except for indicator-7.
                                                            if ( (indcFlag != 7 && fundtl.getAdjustmentType().intValue() != Constants.ACTUAL)
                                                                || (indcFlag == 7 && j == 0 && fundtl.getAdjustmentType().intValue() != Constants.PLANNED)
                                                                || (indcFlag == 7 && j == 1 && fundtl.getAdjustmentType().intValue() != Constants.ACTUAL)
                                                                || fundtl.getTransactionType().intValue() != Constants.DISBURSEMENT) {
                                                                continue;
                                                            }
                                                            // For indc-6: (Q9 = yes) & there is an actual-disb in the year
                                                            if (indcFlag == 6) {
                                                                answersRow[i] += 1;
                                                                break indc6Break;
                                                            }
                                                            // Filtering by currency here
                                                            if ("USD".equalsIgnoreCase(fundtl.getAmpCurrencyId().getCurrencyCode()))
                                                                fromExchangeRate = 1.0;
                                                            else if (indcFlag == 7 && j == 0)
                                                                fromExchangeRate = Util.getExchange(fundtl.getAmpCurrencyId().getCurrencyCode(),
                                                                    new java.sql.Date(fundtl.getTransactionDate().getTime()));
                                                            else
                                                                fromExchangeRate = Util.getExchange(fundtl.getAmpCurrencyId().getCurrencyCode(),
                                                                    new java.sql.Date(fundtl.getTransactionDate().getTime()));
                                                            if (null != currency && currency.trim().length() > 1) {
                                                                if ("USD".equalsIgnoreCase(currency))
                                                                    toExchangeRate = 1.0;
                                                                else if (indcFlag == 7 && j == 0)
                                                                    toExchangeRate = Util.getExchange(currency, new java.sql.Date(fundtl.getTransactionDate().getTime()));
                                                                else
                                                                    toExchangeRate = Util.getExchange(currency, new java.sql.Date(fundtl.getTransactionDate().getTime()));
                                                            }
                                                            sum += CurrencyWorker.convert1(fundtl.getTransactionAmount().doubleValue(),
                                                                fromExchangeRate, toExchangeRate);
                                                        }
                                                    //}
                                                }
                                            }
                                        }
                                    }
                                    if (indcFlag != 6) {
                                        if ("4".equalsIgnoreCase(indcCode)) {
                                            percent = new Double(sum * ansToQues4);
                                            sum = Double.parseDouble(formatter.format(percent));
                                        }
                                        answersRow[j + 1] += sum;
                                    }
                                }
                            } else
                                logger.debug("[inside getAidSurveyReportByIndicator()]- answers array is NULL !");
                        }

                        if (indcFlag == 6)
                            continue;

                        // computing last two columns of indicator-5a report
                        if (indcFlag == 5) {
                            //calculating percentage for second-last column here
                            sum = answersRow[NUM_ANSWER_COLUMNS - 7] + answersRow[NUM_ANSWER_COLUMNS - 6]
                                + answersRow[NUM_ANSWER_COLUMNS - 5];
                            if (answersRow[NUM_ANSWER_COLUMNS - 3] == 0.0)
                                answersRow[NUM_ANSWER_COLUMNS - 2] = -1.0;
                            else {
                                sum /= 3;
                                percent = new Double( (sum * 100) / answersRow[NUM_ANSWER_COLUMNS - 3]);
                                answersRow[NUM_ANSWER_COLUMNS - 2] = Double.parseDouble(formatter.format(percent));
                            }
                        }
                        // calculating final percentage here
                        if ( (indcFlag == 5 || indcFlag == 7) && answersRow[NUM_ANSWER_COLUMNS - 3] == 0.0)
                            answersRow[NUM_ANSWER_COLUMNS - 1] = -1.0;
                        else if ( (indcFlag == 0 || indcFlag == 9) && answersRow[NUM_ANSWER_COLUMNS - 2] == 0.0)
                            answersRow[NUM_ANSWER_COLUMNS - 1] = -1.0;
                        else {
                            try {
                                if (indcFlag == 5)
                                    percent = new Double( (100 * answersRow[NUM_ANSWER_COLUMNS - 4]) / answersRow[NUM_ANSWER_COLUMNS - 3]);
                                else if (indcFlag == 7)
                                    percent = new Double( (100 * answersRow[NUM_ANSWER_COLUMNS - 2]) / answersRow[NUM_ANSWER_COLUMNS - 3]);
                                else if (indcFlag == 9) {
                                    sum = answersRow[NUM_ANSWER_COLUMNS - 4] + answersRow[NUM_ANSWER_COLUMNS - 3];
                                    percent = new Double( (100 * sum) / answersRow[NUM_ANSWER_COLUMNS - 2]);
                                } else
                                    percent = new Double( (100 * answersRow[NUM_ANSWER_COLUMNS - 3]) / answersRow[NUM_ANSWER_COLUMNS - 2]);
                                answersRow[NUM_ANSWER_COLUMNS - 1] = Double.parseDouble(formatter.format(percent));
                                //logger.debug("final-% : " + answersRow[NUM_ANSWER_COLUMNS - 1]);
                            } catch (NumberFormatException nex) {
                                logger.debug("percentage is NaN");
                                answersRow[NUM_ANSWER_COLUMNS - 1] = 0.0;
                            }
                        }
                        pi.getAnswers().add(answersRow);

                        // getting results year-wise for all-donor row
                        allDnRow = (double[]) ( ( (ParisIndicator) responses.get(0)).getAnswers().get(i));
                        for (j = 1; j < (NUM_ANSWER_COLUMNS - 1); j++) {
                            if (indcFlag == 5 && j == (NUM_ANSWER_COLUMNS - 2))
                                break;
                            allDnRow[j] += answersRow[j];
                        }
                    }
                    if (indcFlag == 6) {
                        pi.getAnswers().add(answersRow);
                        responses.add(pi);
                        allDnRow = (double[]) ( ( (ParisIndicator) responses.get(0)).getAnswers().get(0));
                        for (j = 0; j < NUM_ANSWER_COLUMNS; j++) {
                            allDnRow[j] += answersRow[j];
                        }
                    } else
                        responses.add(pi);
                    surveySet.clear();
                }
                if (indcFlag != 6) {
                    // calculating final percentage for all-donors row
                    for (j = 0; j < YEAR_RANGE; j++) {
                        allDnRow = (double[]) ( ( (ParisIndicator) responses.get(0)).getAnswers().get(j));
                        if (indcFlag == 5) {
                            //calculating percentage for second-last column here
                            sum = allDnRow[NUM_ANSWER_COLUMNS - 7] + allDnRow[NUM_ANSWER_COLUMNS - 6]
                                + allDnRow[NUM_ANSWER_COLUMNS - 5];
                            if (allDnRow[NUM_ANSWER_COLUMNS - 3] == 0.0)
                                allDnRow[NUM_ANSWER_COLUMNS - 2] = -1.0;
                            else {
                                sum /= 3;
                                percent = new Double( (sum * 100) / allDnRow[NUM_ANSWER_COLUMNS - 3]);
                                allDnRow[NUM_ANSWER_COLUMNS - 2] = Double.parseDouble(formatter.format(percent));
                            }
                        }
                        // calculating final percentage here
                        if ( (indcFlag == 5 || indcFlag == 7) && allDnRow[NUM_ANSWER_COLUMNS - 3] == 0.0)
                            allDnRow[NUM_ANSWER_COLUMNS - 1] = -1.0;
                        else if ( (indcFlag == 0 || indcFlag == 9) && allDnRow[NUM_ANSWER_COLUMNS - 2] == 0.0)
                            allDnRow[NUM_ANSWER_COLUMNS - 1] = -1.0;
                        else {
                            try {
                                if (indcFlag == 5)
                                    percent = new Double( (100 * allDnRow[NUM_ANSWER_COLUMNS - 4]) / allDnRow[NUM_ANSWER_COLUMNS - 3]);
                                else if (indcFlag == 7)
                                    percent = new Double( (100 * allDnRow[NUM_ANSWER_COLUMNS - 2]) / allDnRow[NUM_ANSWER_COLUMNS - 3]);
                                else if (indcFlag == 9) {
                                    sum = allDnRow[NUM_ANSWER_COLUMNS - 4] + allDnRow[NUM_ANSWER_COLUMNS - 3];
                                    percent = new Double( (100 * sum) / allDnRow[NUM_ANSWER_COLUMNS - 2]);
                                } else
                                    percent = new Double( (100 * allDnRow[NUM_ANSWER_COLUMNS - 3]) / allDnRow[NUM_ANSWER_COLUMNS - 2]);
                                allDnRow[NUM_ANSWER_COLUMNS - 1] = Double.parseDouble(formatter.format(percent));
                                //logger.debug("final-%[all-donors row] : " + allDnRow[NUM_ANSWER_COLUMNS - 1]);
                            } catch (NumberFormatException nex) {
                                logger.debug("percentage[all-donors row] is NaN");
                                allDnRow[NUM_ANSWER_COLUMNS - 1] = 0.0;
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        //logger.debug("responses.size[getAidSurveyReportByIndicator()] : " + responses.size());
        return responses;
    }

    public static boolean isValidTransactionDate(int year, Date transactionDate, Date startDate, Date endDate) {
        boolean result = false;
        //logger.debug("year: " + year + " transactionDate: " + transactionDate);
        if (startDate == null || endDate == null) {
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTime(transactionDate);
            EthiopianCalendar ethCal = (new EthiopianCalendar()).getEthiopianDate(gc);
            if (ethCal.ethFiscalYear == year)
                result = true;
        } else {
            if ( (transactionDate.after(startDate) || chkEqualDates(transactionDate, startDate))
                && (transactionDate.before(endDate) || chkEqualDates(transactionDate, endDate))) {
                result = true;
                /*if (year == DateConversion.getYear(DateConversion.ConvertDateToString(transactionDate)))
                 result = true;*/
            }
            //else
            //logger.debug("[isValidTransactionDate] date-range mismatch !");
        }
        return result;
    }

    public static boolean chkEqualDates(Date d1, Date d2) {
        boolean result = false;
        result = (DateTimeUtil.formatDate(d1).equalsIgnoreCase(DateTimeUtil.formatDate(d2))) ? true : false;
        //logger.debug("[chkEqualDates] date1: " + d1 + " date2:" + d2 + " result: " + result);
        return result;
    }

    public static Collection getAidSurveyReportByIndicator10a(String orgGroup, String donor, int startYear, int closeYear) {
        Session session = null;
        String qry = null;
        ArrayList responses = new ArrayList();
        Collection calDonorsList = new ArrayList();
        Set donors = new HashSet();
        List sortedDonors = new LinkedList();
        boolean orgGroupFlag = false;
        int NUM_ANSWER_COLUMNS = 4, i = 0, j = 0;
        int YEAR_RANGE = (closeYear - startYear + 1);
        double answersRow[] = null;
        double allDnRow[] = null;
        Iterator itr1 = null, itr2 = null;
        Double percent = null;
        NumberFormat formatter = new DecimalFormat("#.##");
        SimpleDateFormat year = new SimpleDateFormat("yyyy");

        try {
            session = PersistenceManager.getRequestDBSession();
            qry = "select cal from " + AmpCalendar.class.getName() + " cal";
            calDonorsList.addAll(session.createQuery(qry).list());
            if (calDonorsList.size() > 0) {
                itr1 = calDonorsList.iterator();
                while (itr1.hasNext()) {
                    AmpCalendar cal = (AmpCalendar) itr1.next();
                    if (cal.getOrganisations() != null)
                        donors.addAll(cal.getOrganisations());
                }
            }
            if (donors.size() > 0) {
                Comparator dnComp = new Comparator() {
                    public int compare(Object o1, Object o2) {
                        AmpOrganisation r1 = (AmpOrganisation) o1;
                        AmpOrganisation r2 = (AmpOrganisation) o2;
                        AmpOrgGroup og1=r1.getOrgGrpId();
                        AmpOrgGroup og2=r2.getOrgGrpId();
                        if(og1!=null && og2!=null){
                            if(og1.getOrgGrpName()!=null && og2.getOrgGrpName()!=null){
                                return og2.getOrgGrpName().compareTo(og2.getOrgGrpName());
                            }else{
                                return 0;
                            }
                        }else{
                            return 0;
                        }
                    }
                };
                sortedDonors.addAll(donors);
                Collections.sort(sortedDonors, dnComp);
                if (null != orgGroup && orgGroup.trim().length() > 1 && !"all".equalsIgnoreCase(orgGroup))
                    orgGroupFlag = true;
                // Creating first row for 'all-donors' in indicator report.
                ParisIndicator all = new ParisIndicator();
                all.setDonor("All Donors");
                all.setAnswers(new ArrayList());
                responses.add(all);
                for (i = 0; i < YEAR_RANGE; i++) {
                    answersRow = new double[NUM_ANSWER_COLUMNS];
                    answersRow[0] = startYear + i;
                    ( (ParisIndicator) responses.get(0)).getAnswers().add(answersRow);
                }
                itr1 = sortedDonors.iterator();
                while (itr1.hasNext()) {
                    AmpOrganisation dnOrg = (AmpOrganisation) itr1.next();
                    // Filtering by donor-organisation here
                    if (dnOrg.getOrgTypeId() != null) {
                        if (!dnOrg.getOrgTypeId().getOrgTypeCode().equalsIgnoreCase("bil") &&
                            !dnOrg.getOrgTypeId().getOrgTypeCode().equalsIgnoreCase("mul")) {
                            continue;
                        }
                    }

                    if (null != donor && donor.trim().length() > 1 && !"all".equalsIgnoreCase(donor)) {
                        if (!donor.equals(dnOrg.getAmpOrgId().toString()))
                            continue;
                    }
                    if (orgGroupFlag) {
                        if (!orgGroup.equalsIgnoreCase(dnOrg.getOrgGrpId().getOrgGrpCode()))
                            continue;
                    }
                    if (null == dnOrg.getCalendar())
                        continue;

                    ParisIndicator pi = new ParisIndicator(); // represents one row of indicator report.
                    if(dnOrg.getOrgGrpId()!=null && dnOrg.getOrgGrpId().getOrgGrpName()!=null){
                        pi.setDonor(dnOrg.getOrgGrpId().getOrgGrpName());
                    }else{
                        pi.setDonor("N/A");
                    }
                    pi.setAnswers(new ArrayList());
                    for (i = 0; i < YEAR_RANGE; i++) {
                        allDnRow = (double[]) ( ( (ParisIndicator) responses.get(0)).getAnswers().get(i));
                        // answersRow will represent row for one disbursement year inside answer-collection of pi helper object.
                        answersRow = new double[NUM_ANSWER_COLUMNS];
                        answersRow[0] = (startYear + i);
                        itr2 = dnOrg.getCalendar().iterator();
                        while (itr2.hasNext()) {
                            AmpCalendar ampCal = (AmpCalendar) itr2.next();
                            if ("Mission".equalsIgnoreCase(ampCal.getEventType().getName())) {
                                Calendar cal = (Calendar) ampCal.getCalendarPK().getCalendar();
                                if (answersRow[0] == Double.parseDouble(year.format(cal.getStartDate())) ||
                                    answersRow[0] == Double.parseDouble(year.format(cal.getEndDate()))) {
                                    // checking if the Mission is 'joint'
                                    if (null != ampCal.getOrganisations() && ampCal.getOrganisations().size() > 1) {
                                        answersRow[1] += 1;
                                        //allDnRow[1] += 1;
                                    }
                                    // total number of Missions
                                    answersRow[2] += 1;
                                    //allDnRow[2] += 1;
                                }
                            }
                        }
                        // calculating final percentage here
                        if (answersRow[2] == 0)
                            answersRow[NUM_ANSWER_COLUMNS - 1] = -1.0;
                        else {
                            percent = new Double( (100 * answersRow[1]) / answersRow[2]);
                            answersRow[NUM_ANSWER_COLUMNS - 1] = Double.parseDouble(formatter.format(percent));
                        }
                        pi.getAnswers().add(answersRow);
                    }
                    responses.add(pi);
                }
                // calculating total joint missions & all missions for 'all-donors' row
                for (j = 0; j < YEAR_RANGE; j++) {
                    allDnRow = (double[]) ( ( (ParisIndicator) responses.get(0)).getAnswers().get(j));
                    Iterator itr = calDonorsList.iterator();
                    while (itr.hasNext()) {
                        AmpCalendar acal = (AmpCalendar) itr.next();
                        if ("Mission".equalsIgnoreCase(acal.getEventType().getName())) {
                            Calendar cal = (Calendar) acal.getCalendarPK().getCalendar();
                            if (allDnRow[0] == Double.parseDouble(year.format(cal.getStartDate())) ||
                                allDnRow[0] == Double.parseDouble(year.format(cal.getEndDate()))) {
                                if (null != acal.getOrganisations()) {
                                    if (acal.getOrganisations().size() > 1) // checking if the Mission is 'joint'
                                        allDnRow[1] += 1;
                                    if (!acal.getOrganisations().isEmpty())
                                        allDnRow[2] += 1; // total number of Missions
                                }
                            }
                        }
                    }
                }
                // calculating final percentage for all-donors row
                for (j = 0; j < YEAR_RANGE; j++) {
                    allDnRow = (double[]) ( ( (ParisIndicator) responses.get(0)).getAnswers().get(j));
                    if (allDnRow[2] == 0)
                        allDnRow[NUM_ANSWER_COLUMNS - 1] = -1.0;
                    else {
                        percent = new Double( (100 * allDnRow[1]) / allDnRow[2]);
                        allDnRow[NUM_ANSWER_COLUMNS - 1] = Double.parseDouble(formatter.format(percent));
                    }
                }
            } else
                logger.debug("[getAidSurveyReportByIndicator10a()] No donor org found");
        } catch (Exception ex) {
            logger.debug("Unable to get AidSurveyReportByIndicator10a: " + ex);
            ex.printStackTrace(System.out);
        }
        return responses;
    }

    /* returns a 2-D array whose each element is an array consisting of results after matching
     * survey-responses with required report-answers for the aid-effectiveness-indicator
     * except for those indicators with code '10a', '10b'.
     */
    public static boolean[][] getSurveyReportAnswer(String indCode, Set surveys) {
        boolean answersColl[][] = new boolean[surveys.size()][];
        boolean answers[] = null;
        boolean flag[] = null;
        int NUM_COLUMNS_CALCULATED = 2;
        int index = 0;
        int quesNum = 0;
        Iterator itr1 = null;
        Iterator itr2 = null;

        if ("5a".equalsIgnoreCase(indCode))
            NUM_COLUMNS_CALCULATED = 5;
        else if ("6".equalsIgnoreCase(indCode))
            NUM_COLUMNS_CALCULATED = 1;
        else if ("9".equalsIgnoreCase(indCode))
            NUM_COLUMNS_CALCULATED = 3;
        else
            NUM_COLUMNS_CALCULATED = 2;
        //logger.debug("indCode[inside getSurveyReportAnswer] : " + indCode);
        //logger.debug("NUM_ANSWERS_CALCULATED[inside getSurveyReportAnswer] : " + NUM_COLUMNS_CALCULATED);
        flag = new boolean[NUM_COLUMNS_CALCULATED];

        itr1 = surveys.iterator();
        while (itr1.hasNext()) {
            AmpAhsurvey ahs = (AmpAhsurvey) itr1.next();
            //logger.debug("[inside iterator]-SurveyId: " + ahs.getAmpAHSurveyId() + " AmpOrgId: " + ahs.getAmpDonorOrgId().getAmpOrgId()
            //			+ " Acronym: " + ahs.getAmpDonorOrgId().getAcronym());
            answers = new boolean[NUM_COLUMNS_CALCULATED];
            if (null != ahs.getResponses() && ahs.getResponses().size() > 0) {
                //logger.debug("ahs.getResponses().size() : " + ahs.getResponses().size());
                for (int i = 0; i < NUM_COLUMNS_CALCULATED; i++)
                    flag[i] = false;
                itr2 = ahs.getResponses().iterator();
                while (itr2.hasNext()) {
                    AmpAhsurveyResponse resp = (AmpAhsurveyResponse) itr2.next();
                    quesNum = resp.getAmpQuestionId().getQuestionNumber().intValue();
                    //logger.debug("quesNum : " + quesNum);
                    if ("3".equalsIgnoreCase(indCode)) {
                        if (quesNum == 2) {
                            flag[0] = true;
                            answers[0] = ("Yes".equalsIgnoreCase(resp.getResponse())) ? true : false;
                            //logger.debug("indCode: " + indCode + " q#: " + 2 + " - answers[0] : " + answers[0]);
                        }
                        if (quesNum == 1) {
                            flag[1] = true;
                            answers[1] = ("Yes".equalsIgnoreCase(resp.getResponse())) ? true : false;
                            //logger.debug("indCode: " + indCode + " q#: " + 1 + " - answers[1] : " + answers[1]);
                        }
                        if (flag[0] && flag[1]) {
                            answers[0] = (answers[0] && answers[1]) ? true : false;
                            break;
                        } else
                            continue;
                    }
                    if ("4".equalsIgnoreCase(indCode)) {
                        if (quesNum == 3) {
                            answers[0] = ("Yes".equalsIgnoreCase(resp.getResponse())) ? true : false;
                            answers[1] = true;
                            break;
                        }
                    }
                    if ("5a".equalsIgnoreCase(indCode)) {
                        if (quesNum == 1) {
                            flag[0] = true;
                            answers[4] = ("Yes".equalsIgnoreCase(resp.getResponse())) ? true : false;
                            //logger.debug("indCode: " + indCode + " q#: " + 1 + " - answers[4] : " + answers[4]);
                        }
                        if (quesNum == 5) {
                            flag[1] = true;
                            answers[0] = ("Yes".equalsIgnoreCase(resp.getResponse())) ? true : false;
                            //logger.debug("indCode: " + indCode + " q#: " + 5 + " - answers[0] : " + answers[0]);
                        }
                        if (quesNum == 6) {
                            flag[2] = true;
                            answers[1] = ("Yes".equalsIgnoreCase(resp.getResponse())) ? true : false;
                            //logger.debug("indCode: " + indCode + " q#: " + 6 + " - answers[1] : " + answers[1]);
                        }
                        if (quesNum == 7) {
                            flag[3] = true;
                            answers[2] = ("Yes".equalsIgnoreCase(resp.getResponse())) ? true : false;
                            //logger.debug("indCode: " + indCode + " q#: " + 7 + " - answers[2] : " + answers[2]);
                        }
                        if (flag[0] && flag[1] && flag[2] && flag[3]) {
                            answers[0] = (answers[0] && answers[4]) ? true : false;
                            answers[1] = (answers[1] && answers[4]) ? true : false;
                            answers[2] = (answers[2] && answers[4]) ? true : false;
                            answers[3] = (answers[0] && answers[1] && answers[2] && answers[4]) ? true : false;
                            break;
                        } else
                            continue;
                    }
                    if ("5b".equalsIgnoreCase(indCode)) {
                        if (quesNum == 8) {
                            flag[0] = true;
                            answers[0] = ("Yes".equalsIgnoreCase(resp.getResponse())) ? true : false;
                            //logger.debug("indCode: " + indCode + " q#: " + 8 + " - answers[0] : " + answers[0]);
                        }
                        if (quesNum == 1) {
                            flag[1] = true;
                            answers[1] = ("Yes".equalsIgnoreCase(resp.getResponse())) ? true : false;
                            //logger.debug("indCode: " + indCode + " q#: " + 1 + " - answers[1] : " + answers[1]);
                        }
                        if (flag[0] && flag[1])
                            break;
                        else
                            continue;
                    }
                    if ("6".equalsIgnoreCase(indCode)) {
                        if (quesNum == 9) {
                            answers[0] = ("Yes".equalsIgnoreCase(resp.getResponse())) ? true : false;
                            //logger.debug("indCode: " + indCode + " q#: " + 9 + " - answers[0] : " + answers[0]);
                            break;
                        } else
                            continue;
                    }
                    if ("7".equalsIgnoreCase(indCode)) {
                        if (quesNum == 1) {
                            answers[0] = answers[1] = ("Yes".equalsIgnoreCase(resp.getResponse())) ? true : false;
                            //logger.debug("indCode: " + indCode + " q#: " + 1 + " - answers[0]=answers[1] : " + answers[0]);
                            break;
                        } else
                            continue;
                    }
                    if ("9".equalsIgnoreCase(indCode)) {
                        if (quesNum == 11) {
                            answers[0] = answers[1] = ("Yes".equalsIgnoreCase(resp.getResponse())) ? true : false;
                            answers[2] = true;
                            //logger.debug("indCode: " + indCode + " q#: " + 11 + " - answers[0]=answers[1] : " + answers[0]);
                            //logger.debug("indCode: " + indCode + " q#: " + 11 + " - answers[2] : " + answers[2]);
                            break;
                        }
                    }
                }
                answersColl[index++] = answers;
            } else {
                //logger.debug("No response set found for this survey");
                for (int b = 0; b < NUM_COLUMNS_CALCULATED; b++)
                    answers[b] = false;
                answersColl[index++] = answers;
            }
        }
        //logger.debug("answersColl.length : " + answersColl.length);
        return answersColl;
    }

    /*
     * Methods called to retrieve data
     * that have to be deleted
     * while an activity is deleted by Admin
     * start here
     */
    /* get amp comments of a particular activity specified by ampActId */
    public static Collection getActivityAmpComments(Long ampActId) {
        Session session = null;
        Collection col = null;
        Query qry = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select com from " + AmpComments.class.getName()
                + " com " + " where (com.ampActivityId=:ampActId)";
            qry = session.createQuery(queryString);
            qry.setParameter("ampActId", ampActId, Hibernate.LONG);
            col = qry.list();
        } catch (Exception e1) {
            logger.error("could not retrieve AmpComments " + e1.getMessage());
            e1.printStackTrace(System.out);
        }
        return col;
    }

    /* get ampActivity physical component report of a particular activity specified by ampActId */
    public static Collection getActivityPhysicalComponentReport(Long ampActId) {
        Session session = null;
        Collection col = null;
        Query qry = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select phyCompReport from "
                + AmpPhysicalComponentReport.class.getName()
                + " phyCompReport "
                + " where (phyCompReport.ampActivityId=:ampActId)";
            qry = session.createQuery(queryString);
            qry.setParameter("ampActId", ampActId, Hibernate.LONG);
            col = qry.list();
        } catch (Exception e1) {
            logger.error("could not retrieve AmpPhysicalComponentReport " + e1.getMessage());
            e1.printStackTrace(System.out);
        }
        return col;
    }

    /* get amp report cache of a particular activity specified by ampActId */
    public static Collection getActivityReportCache(Long ampActId) {
        Session session = null;
        Collection col = null;
        Query qry = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select repCache from "
                + AmpReportCache.class.getName()
                + " repCache "
                + " where (repCache.ampActivityId=:ampActId)";
            qry = session.createQuery(queryString);
            qry.setParameter("ampActId", ampActId, Hibernate.LONG);
            col = qry.list();
        } catch (Exception e1) {
            logger.error("could not retrieve AmpReportCache " + e1.getMessage());
            e1.printStackTrace(System.out);
        }
        return col;
    }

    /* get amp report location of a particular activity specified by ampActId */
    public static Collection getActivityReportLocation(Long ampActId) {
        Session session = null;
        Collection col = null;
        Query qry = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select repLoc from "
                + AmpReportLocation.class.getName()
                + " repLoc "
                + " where (repLoc.ampActivityId=:ampActId)";
            qry = session.createQuery(queryString);
            qry.setParameter("ampActId", ampActId, Hibernate.LONG);
            col = qry.list();
        } catch (Exception e1) {
            logger.error("could not retrieve AmpReportLocation " + e1.getMessage());
            e1.printStackTrace(System.out);
        }
        return col;
    }

    /* get amp activity report physical performance of a particular activity specified by ampActId */
    public static Collection getActivityRepPhyPerformance(Long ampActId) {
        Session session = null;
        Collection col = null;
        Query qry = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select phyPer from "
                + AmpReportPhysicalPerformance.class.getName()
                + " phyPer "
                + " where (phyPer.ampActivityId=:ampActId)";
            qry = session.createQuery(queryString);
            qry.setParameter("ampActId", ampActId, Hibernate.LONG);
            col = qry.list();
        } catch (Exception e1) {
            logger.error("could not retrieve AmpReportPhysicalPerformance " + e1.getMessage());
            e1.printStackTrace(System.out);
        }
        return col;
    }

    /* get amp activity report sector of a particular activity specified by ampActId */
    public static Collection getActivityReportSector(Long ampActId) {
        Session session = null;
        Collection col = null;
        Query qry = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select repSector from "
                + AmpReportSector.class.getName()
                + " repSector "
                + " where (repSector.ampActivityId=:ampActId)";
            qry = session.createQuery(queryString);
            qry.setParameter("ampActId", ampActId, Hibernate.LONG);
            col = qry.list();
        } catch (Exception e1) {
            logger.error("could not retrieve AmpReportSector " + e1.getMessage());
            e1.printStackTrace(System.out);
        }
        return col;
    }

    /* get amp ME indicator value of a particular activity specified by ampActId */
    public static Collection getActivityMEIndValue(Long ampActId) {
		try {

			Collection<IndicatorActivity> activityInd = null;
			AmpActivity activity = ActivityUtil.loadActivity(ampActId);
			if (activity != null) {
				activityInd = activity.getIndicators();
			}
			return activityInd;
		} catch (Exception e) {
			logger.info("Couldn't get activity to delete indicators...");
			return null;
		}

	}

    /*
	 * Methods called to retrieve data that have to be deleted while an activity
	 * is deleted by Admin end here
	 */

    /* To check for Status code
     * modified by Govind
     */
    public static Collection getStatusCodes() {
        logger.info(" in getting the Status codes...");
        Session session = null;
        Collection col = null;
        Query qry = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select st from " + AmpStatus.class.getName() + " st ";
            qry = session.createQuery(queryString);
            col = qry.list();
        } catch (Exception e1) {
            logger.error("could not retrieve Statuc codes... " + e1.getMessage());
            e1.printStackTrace(System.out);
        }
        return col;
    }



    /**
     * Returns LabelValueBean's for all AmpStatus entities.
     * Status code is used as value and Status name as Label.
     * Used in HTML select dropdowns and lists.
     * @return List of LabelValeBean objects
     * @throws AimException if any error happens.
     * @author Irakli Kobiashvili
     */
    public static List getAllAmpStatusesLVB() throws AimException {
        try {
            Session session = PersistenceManager.getRequestDBSession();
            String queryString = "from " + AmpStatus.class.getName();
            Query qry = session.createQuery(queryString);
            Collection col = qry.list();
            List result = new ArrayList(col.size());
            for (Iterator iter = col.iterator(); iter.hasNext(); ) {
                AmpStatus status = (AmpStatus) iter.next();
                LabelValueBean lvb = new LabelValueBean(status.getName(), status.getStatusCode());
                result.add(lvb);
            }
            return result;
        } catch (Exception e1) {
            logger.error("could not retrieve Statuses " + e1.getMessage());
            throw new AimException(e1);
        }
    }

    public static Collection<CountryBean> getTranlatedCountries(HttpServletRequest request) {
        Collection<CountryBean> trnCnCol = null;
        org.digijava.kernel.entity.Locale navLang = RequestUtils.getNavigationLanguage(request);

        try {
            trnCnCol = new ArrayList<CountryBean>();
            Collection<Country> cnCol=FeaturesUtil.getAllDgCountries();
            if(cnCol!=null && cnCol.size()!=0){
                for (Iterator cnIter = cnCol.iterator(); cnIter.hasNext(); ) {
                    Country cn = (Country) cnIter.next();
                    cn=getTranlatedCountry(request,cn);

                    CountryBean trnCn=new CountryBean();
                    trnCn.setId(cn.getCountryId());
                    trnCn.setIso(cn.getIso());
                    trnCn.setIso3(cn.getIso3());
                    trnCn.setName(cn.getCountryName());
                    trnCnCol.add(trnCn);
                }

            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        if(trnCnCol!=null && trnCnCol.size()!=0){
            List<CountryBean> sortedCountrieList = new ArrayList<CountryBean>(trnCnCol);
            Collections.sort(sortedCountrieList, new HelperTrnCountryNameComparator(navLang.getCode()));
            return sortedCountrieList;
        }else{
            return null;
        }
    }

    public static Country getTranlatedCountry(HttpServletRequest request, Country country) {
        Session session = null;
        Collection msgCol = null;
        Query qry = null;

        org.digijava.kernel.entity.Locale navLang = RequestUtils.getNavigationLanguage(request);
        Site site = RequestUtils.getSite(request);

        try {
            if (country != null) {
                session = PersistenceManager.getRequestDBSession();
                String queryString = "select msg " +
                    " from " + Message.class.getName() + " msg" +
                    " where (msg.key=:msgLangKey) and (msg.siteId=:siteId) and (msg.locale=:locale)";

                qry = session.createQuery(queryString);
                qry.setParameter("siteId", site.getId(), Hibernate.LONG);
                qry.setParameter("locale", navLang.getCode(), Hibernate.STRING);
                qry.setParameter("msgLangKey", country.getMessageLangKey(), Hibernate.STRING);
                msgCol = qry.list();

                if (msgCol != null && msgCol.size() != 0) {
                    for (Iterator msgIter = msgCol.iterator(); msgIter.hasNext(); ) {
                        Message msg = (Message) msgIter.next();
                        if (msg != null) {
                            country.setCountryName(msg.getMessage());
                            break;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return country;
    }




    public static class HelperUserNameComparator implements Comparator {
        public int compare(Object obj1, Object obj2) {
            User user1 = (User) obj1;
            User user2 = (User) obj2;
            return user1.getName().compareTo(user2.getName());
        }
    }
    /**
     * //for sorting users by Email
     * @author dare
     *
     */
    public static class HelperEmailComparator implements Comparator {
        public int compare(Object obj1, Object obj2) {
            User user1 = (User) obj1;
            User user2 = (User) obj2;
            return user1.getEmail().compareTo(user2.getEmail());
        }
    }

    public static class HelperTrnCountryNameComparator implements Comparator<CountryBean> {
        Locale locale;
        Collator collator;

        public HelperTrnCountryNameComparator(){
            this.locale=new Locale("en", "EN");
        }

        public HelperTrnCountryNameComparator(String iso) {
            this.locale = new Locale(iso.toLowerCase(), iso.toUpperCase());
        }

        public int compare(CountryBean o1, CountryBean o2) {
            collator = Collator.getInstance(locale);
            collator.setStrength(Collator.TERTIARY);

            int result = collator.compare(o1.getName(), o2.getName());
            return result;
        }
    }

    public static class HelperAmpOrgGroupNameComparator implements Comparator<AmpOrgGroup> {
        Locale locale;
        Collator collator;

        public HelperAmpOrgGroupNameComparator(){
            this.locale=new Locale("en", "EN");
        }

        public HelperAmpOrgGroupNameComparator(String iso) {
            this.locale = new Locale(iso.toLowerCase(), iso.toUpperCase());
        }

        public int compare(AmpOrgGroup o1, AmpOrgGroup o2) {
            collator = Collator.getInstance(locale);
            collator.setStrength(Collator.TERTIARY);

            int result = collator.compare(o1.getOrgGrpName(), o2.getOrgGrpName());
            return result;
        }
    }
    /**
     * This class is used for sorting AmpOrgGroup by code.
     * @author Dare Roinishvili
     *
     */
    public static class HelperAmpOrgGroupCodeComparator implements Comparator<AmpOrgGroup> {
        Locale locale;
        Collator collator;

        public HelperAmpOrgGroupCodeComparator(){
            this.locale=new Locale("en", "EN");
        }

        public HelperAmpOrgGroupCodeComparator(String iso) {
            this.locale = new Locale(iso.toLowerCase(), iso.toUpperCase());
        }

        public int compare(AmpOrgGroup o1, AmpOrgGroup o2) {
            collator = Collator.getInstance(locale);
            collator.setStrength(Collator.TERTIARY);

            int result = (o1.getOrgGrpCode()!=null && o2.getOrgGrpCode()!=null)?collator.compare(o1.getOrgGrpCode(), o2.getOrgGrpCode()):0;
            return result;
        }
    }

    /**
     * This class is used for sorting AmpOrgGroup by Type.
     * @author Dare Roinishvili
     *
     */
    public static class HelperAmpOrgGroupTypeComparator implements Comparator<AmpOrgGroup>{
    	public int compare(AmpOrgGroup o1,AmpOrgGroup o2){
    		AmpOrgType o1Type=o1.getOrgType();
    		AmpOrgType o2Type=o2.getOrgType();
    		return new HelperAmpOrgTypeNameComparator().compare(o1Type, o2Type);
    	}
    }

    public static class HelperAmpOrgTypeNameComparator implements Comparator<AmpOrgType> {
        Locale locale;
        Collator collator;

        public HelperAmpOrgTypeNameComparator(){
            this.locale=new Locale("en", "EN");
        }

        public HelperAmpOrgTypeNameComparator(String iso) {
            this.locale = new Locale(iso.toLowerCase(), iso.toUpperCase());
        }

        public int compare(AmpOrgType o1, AmpOrgType o2) {
            collator = Collator.getInstance(locale);
            collator.setStrength(Collator.TERTIARY);

            int result = collator.compare(o1.getOrgType(), o2.getOrgType());
            return result;
        }
    }

    /**
     * This class is used for sorting organisations by name.
     * @author Dare Roinishvili
     *
     */
    public static class HelperAmpOrganisationNameComparator implements Comparator<AmpOrganisation> {
    	Locale locale;
        Collator collator;

        public HelperAmpOrganisationNameComparator(){
            this.locale=new Locale("en", "EN");
        }

        public HelperAmpOrganisationNameComparator(String iso) {
            this.locale = new Locale(iso.toLowerCase(), iso.toUpperCase());
        }

        public int compare(AmpOrganisation o1, AmpOrganisation o2) {
            collator = Collator.getInstance(locale);
            collator.setStrength(Collator.TERTIARY);


            int result = (o1.getName()==null || o2.getName()==null)?0:collator.compare(o1.getName().toLowerCase(), o2.getName().toLowerCase());
            return result;
        }
    }

    /**
     * This class is used for soring organisations by acronym.
     * @author Dare Roinishvili
     *
     */
    public static class HelperAmpOrganisatonAcronymComparator implements Comparator<AmpOrganisation>{
    	Locale locale;
        Collator collator;

        public HelperAmpOrganisatonAcronymComparator(){
            this.locale=new Locale("en", "EN");
        }

        public HelperAmpOrganisatonAcronymComparator(String iso) {
            this.locale = new Locale(iso.toLowerCase(), iso.toUpperCase());
        }

        public int compare(AmpOrganisation o1, AmpOrganisation o2) {

        	int result=0;
        	collator = Collator.getInstance(locale);
            collator.setStrength(Collator.TERTIARY);

            if (o1.getAcronym()!=null && o2.getAcronym()!=null) {
            	result=collator.compare(o1.getAcronym(), o2.getAcronym());
            }else if(o1.getAcronym()==null && o2.getAcronym()==null){
            	result=0;
            }else if(o1.getAcronym()==null){
            	result=collator.compare("",o2.getAcronym());
            }else if (o2.getAcronym()==null){
            	result=collator.compare(o1.getAcronym(),"");
            }
            return result;
        }

   }

    /**
     * This class is used for sorting organisation by group.
     * such long and complicated case is necessary because orgGroup maybe empty for organisation
     * @author Dare Roinishvili
     *
     */
    public static class HelperAmpOrganisationGroupComparator implements Comparator<AmpOrganisation> {
    	Locale locale;
    	Collator collator;

    	public HelperAmpOrganisationGroupComparator(){
            this.locale=new Locale("en", "EN");
        }

        public HelperAmpOrganisationGroupComparator(String iso) {
            this.locale = new Locale(iso.toLowerCase(), iso.toUpperCase());
        }


    	public int compare (AmpOrganisation o1,AmpOrganisation o2){
    		collator = Collator.getInstance(locale);
            collator.setStrength(Collator.TERTIARY);
    		int result=0;
    		//such long and complicated case is necessary because orgGroup maybe empty for organisation
    		if (o1.getOrgGrpId()!=null && o2.getOrgGrpId()!=null) {
    			AmpOrgGroup orggrp1=o1.getOrgGrpId();
    			AmpOrgGroup orggrp2=o2.getOrgGrpId();
    			result=new HelperAmpOrgGroupNameComparator().compare(orggrp1, orggrp2);
    		} else if (o2.getOrgGrpId()==null){
    			result=collator.compare(o1.getOrgGrpId().getOrgGrpName(), "");
    		}else if (o1.getOrgGrpId()==null){
    			result= collator.compare("", o2.getOrgGrpId().getOrgGrpName());
    		}
    		return result;
    	}
    }

    /**
     * This class is used for sorting organisation by Type.
     * such long and complicated case is necessary because orgType maybe empty for organisation
     * @author Dare Roinisvili
     *
     */
    public static class HelperAmpOrganisationTypeComparator implements Comparator<AmpOrganisation> {
    	Locale locale;
    	Collator collator;

    	public HelperAmpOrganisationTypeComparator(){
            this.locale=new Locale("en", "EN");
        }

        public HelperAmpOrganisationTypeComparator(String iso) {
            this.locale = new Locale(iso.toLowerCase(), iso.toUpperCase());
        }

    	public int compare (AmpOrganisation o1,AmpOrganisation o2){
    		collator = Collator.getInstance(locale);
            collator.setStrength(Collator.TERTIARY);
    		int result=0;
    		//such long and complicated case is necessary because orgType maybe empty for organisation
    		if (o1.getOrgTypeId()!=null && o2.getOrgTypeId()!=null) {
    			AmpOrgType orgType1=o1.getOrgTypeId();
    			AmpOrgType orgType2=o2.getOrgTypeId();
    			result=new HelperAmpOrgTypeNameComparator().compare(orgType1, orgType2);
    		} else if (o2.getOrgTypeId()==null){
    			result=collator.compare(o1.getOrgTypeId().getOrgType(), "");
    		}else if (o1.getOrgTypeId()==null){
    			result=collator.compare("", o2.getOrgTypeId().getOrgType());
    		}
    		return result;

    	}
    }

	public static AmpOrganisation getOrganisationByName(String name) {
		AmpOrganisation obResult=null;
        Session sess = null;
        Query qry = null;
        String queryString = null;

        try {
            sess = PersistenceManager.getRequestDBSession();
            queryString = "select o from " + AmpOrganisation.class.getName()
                + " o where (TRIM(o.name)=:orgName)";
            qry = sess.createQuery(queryString);
            qry.setParameter("orgName", name, Hibernate.STRING);

            List  result=qry.list();
            if (result.size() > 0){
            	obResult= (AmpOrganisation) result.get(0);
            }
            //System.out.println("DBUTIL.GETORGANISATIONBYNAME() : " + qry.getQueryString());
        } catch (Exception e) {
            logger.debug("Exception from getOrganisationByName(): " + e);
            e.printStackTrace(System.out);
        }
        return obResult;
	}

	/**
	 * Compares Values by type(actual,base,target)
	 * Used in Multi Program Manager to sort them in order: base,actual,target  of the same year
	 * @author dare
	 *
	 */
	public static class IndicatorValuesComparatorByTypeAndYear implements Comparator<AmpPrgIndicatorValue>{
		public int compare(AmpPrgIndicatorValue o1, AmpPrgIndicatorValue o2) {
			int retValue=0;
			String  o1Year="";
			String o2Year="";
			//getting  year from creation date
			if(o1.getCreationDate()!=null){
				int length=o1.getCreationDate().length();
				o1Year=o1.getCreationDate().substring(length-4, length);
			}
			if(o2.getCreationDate()!=null){
				int length=o2.getCreationDate().length();
				o2Year=o2.getCreationDate().substring(length-4, length);
			}
			//o1's creation year is greater than o2's
			if(o1Year.compareTo(o2Year)==1){
				retValue= 1;
			}else if(o1Year.compareTo(o2Year)==-1){//creation year of o1 is less than o2's
				retValue=-1;
			}else if(o1Year.compareTo(o2Year)==0){ //creation years are equal. So we have to sort them in order:base actual target
				retValue= -(new Integer(o1.getValueType()).compareTo(new Integer(o2.getValueType())));
			}
			return retValue;
		}
	}

	public static class AmpIndicatorValuesComparatorByTypeAndYear implements Comparator<AmpIndicatorValue>{

		public int compare(AmpIndicatorValue o1, AmpIndicatorValue o2) {
			AmpPrgIndicatorValue val1=new AmpPrgIndicatorValue();
			AmpPrgIndicatorValue val2=new AmpPrgIndicatorValue();

			val1.setValueType(o1.getValueType());
			val1.setCreationDate(DateConversion.ConvertDateToString(o1.getValueDate()));

			val2.setValueType(o2.getValueType());
			val2.setCreationDate(DateConversion.ConvertDateToString(o2.getValueDate()));
			return new IndicatorValuesComparatorByTypeAndYear().compare(val1, val2) ;
		}

	}

	/**
	 * @param id
	 * @return
	 * @throws CMSException
	 */
	public static CMSContentItem getCMSContentItem(Long id) throws AimException {

		CMSContentItem item = null;
		Session session = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			item = (CMSContentItem) session.load(CMSContentItem.class, id);
		}
		catch (Exception ex) {
			logger.debug("Unable to get CMS Content Item from database", ex);
			throw new AimException("Unable to get CMS Content Item from database", ex);
		}
		return item;
	}
}
