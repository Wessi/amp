package org.digijava.module.orgProfile.util;

import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.rtf.table.RtfCell;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;


import java.util.ListIterator;
import java.util.Set;
import java.util.TreeMap;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpAhsurvey;
import org.digijava.module.aim.dbentity.AmpAhsurveyResponse;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.logic.FundingCalculationsHelper;
import org.digijava.module.aim.util.DecimalWraper;
import org.digijava.module.calendar.dbentity.AmpCalendar;
import org.digijava.module.widget.util.ChartWidgetUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.apache.log4j.Logger;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityGroup;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.orgProfile.helper.Project;
import org.digijava.module.orgProfile.helper.FilterHelper;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.FiscalCalendarUtil;
import org.digijava.module.widget.dbentity.AmpWidgetOrgProfile;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.orgProfile.helper.NameValueYearHelper;
import org.digijava.module.widget.util.WidgetUtil;

/**
 *
 * @author medea
 */
public class OrgProfileUtil {

    private static Logger logger = Logger.getLogger(OrgProfileUtil.class);

    public static final Color TITLECOLOR = new Color(34, 46, 93);
    public static final Color BORDERCOLOR = new Color(255, 255, 255);
    public static final Color CELLCOLOR=new Color(219, 229, 241);
    public static final Font PLAINFONT = new Font(Font.TIMES_ROMAN, 10);
    public static final Font HEADERFONT = new Font(Font.TIMES_ROMAN, 12, Font.BOLD, new Color(255, 255, 255));

   /**
    *
    * @param indCode
    * @param currCode
    * @param orgId
    * @param orgGroupId
    * @param startDate
    * @param endDate
    * @param teamMember
    * @return
    * @throws org.digijava.kernel.exception.DgException
    */
    public static Long getValue(String indCode,  String currCode, Long[] orgIds, Long orgGroupId, Date startDate, Date endDate, TeamMember teamMember) throws DgException {
        Long total = 0l;
        try {
            // questions that must be answered with 'yes' in the survey, nominator column
            int nominator[] = null;
             // questions that must be answered with 'yes' in the survey, denominator column
            int denominator[] = null;
            int adjustmentType=Constants.ACTUAL;
            if (indCode.equals("3")) {
                nominator = new int[]{2, 1};
                denominator = new int[]{1};
            } else {
                if (indCode.equals("4")) {
                    nominator = new int[]{3};
                    denominator = new int[]{0};
                } else {
                    if (indCode.equals("5a")) {
                        nominator = new int[]{1, 5, 6, 7};
                        denominator = new int[]{1};

                    } else {
                        if (indCode.equals("5b")) {
                            nominator = new int[]{8, 1};
                            denominator = new int[]{1};

                        } else {
                            if (indCode.equals("6")) {
                                total = getPIUValue(orgIds, orgGroupId, startDate, endDate, teamMember);
                                return total;
                            } else {
                                if (indCode.equals("7")) {
                                    nominator = new int[]{1};
                                    denominator = new int[]{1};

                                } else {
                                    if (indCode.equals("9")) {
                                        nominator = new int[]{10};
                                        denominator = new int[]{0};

                                    } else {
                                        if (indCode.equals("10a")) {
                                           total=getIndicator10aValue(startDate, endDate, orgIds, orgGroupId);
                                           return total;
                                        } else {
                                            if (indCode.equals("5aii")) {
                                                nominator = new int[]{1, 5, 6, 7};
                                                total = getDonorsCount(nominator, orgIds, orgGroupId, startDate, endDate, teamMember);
                                                return total;
                                            } else {
                                                if (indCode.equals("5bii")) {
                                                    nominator = new int[]{8, 1};
                                                    total = getDonorsCount(nominator, orgIds, orgGroupId, startDate, endDate, teamMember);
                                                    return total;
                                                }
                                                else{
                                                     if (indCode.equals("10b")||indCode.equals("8")) {
                                                         return total;
                                                     }

                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                    }

                }
            }
            String nominatorCondition = getAmpAhsurveyCondition(nominator);
            String denominatorCondition = getAmpAhsurveyCondition(denominator);
            Double nominatorValue = null;
            Double denominatorValue = null;
            if (nominatorCondition.length() > 0) {
                if (indCode.equals("7")) {
                    // in that case we are calculating  planned disb.
                    nominatorValue = getValue(indCode, Constants.PLANNED, currCode, orgIds, orgGroupId, startDate, endDate, teamMember, nominatorCondition);
                }
                else{
                    //calculating  actual disb.
                      nominatorValue = getValue(indCode, adjustmentType, currCode, orgIds, orgGroupId, startDate, endDate, teamMember, nominatorCondition);
                }
            }
            if (denominatorCondition.length() > 0) {
                //calculating denominator value
                denominatorValue = getValue(indCode, adjustmentType, currCode, orgIds, orgGroupId, startDate, endDate, teamMember, denominatorCondition);
            }
            if (denominatorValue != null && denominatorValue != 0 && nominatorValue != null) {
                total = Math.round(nominatorValue / denominatorValue * 100);
            }



        } catch (Exception e) {
            logger.error("Unable get value ", e);

        }

        return total;
    }

    /**
     *
     * @param questionNumber
     * @param indId
     * @param adjustmentType
     * @param currCode
     * @param orgId
     * @param year
     * @return
     */
    public static Long getDonorsCount(int questionNumber[], Long[] orgIds, Long orgGroupId, Date startDate, Date endDate, TeamMember member) {
        long size = 0;
        try {
            Session session = PersistenceManager.getRequestDBSession();
            String queryString = "";
            String whereCondition = getAmpAhsurveyCondition(questionNumber);
            Query qry = null;
            if (whereCondition.length() > 0) {
                queryString = "select  distinct f.ampDonorOrgId from " + AmpAhsurvey.class.getName() + " ah inner join ah.responses res  " + " inner join res.ampQuestionId  q  " +
                        " inner join ah.ampActivityId act   " + " inner join act.funding f   " + " inner join  f.fundingDetails fd   ";

                queryString += "  where fd.transactionType =1 and  fd.adjustmentType =1" +
                        " and fd.transactionDate>=:startDate and  fd.transactionDate<=:endDate ";
                queryString += " and ah.ampAHSurveyId in (" + whereCondition + ")";
                if (orgIds == null) {
                   if(orgGroupId !=null&&orgGroupId!=-1){
                       queryString += " and ah.ampDonorOrgId.orgGrpId=:orgGroupId ";
                   }
                } else {
                    queryString += " and ah.ampDonorOrgId in ("+ChartWidgetUtil.getInStatment(orgIds)+")";
                }
                queryString += ChartWidgetUtil.getTeamQuery(member);
                qry = session.createQuery(queryString);
                qry.setDate("startDate", startDate);
                qry.setDate("endDate", endDate);
                if (member != null) {
                    qry.setLong("teamId", member.getTeamId());

                }
                if (orgIds == null&&orgGroupId!=null&&orgGroupId!=-1 ) {
                    qry.setLong("orgGroupId", orgGroupId);
                }
                size = qry.list().size();
            }

        } catch (Exception e) {
            logger.error("Unable get value ", e);

        }

        return size;
    }

    /**
     *
     * @param questionNumber
     * @param indId
     * @param adjustmentType
     * @param currCode
     * @param orgId
     * @param year
     * @return
     */
    public static long getPIUValue(Long[] orgIds, Long orgGroupId, Date startDate, Date endDate, TeamMember member) {
        long size = 0;
        try {
            Session session = PersistenceManager.getRequestDBSession();
            String queryString = "select  distinct act ";

            queryString += " from " + AmpAhsurvey.class.getName() + " ah inner join ah.responses res  " + " inner join res.ampQuestionId  q  " +
                    " inner join ah.ampActivityId act   " + " inner join act.funding f   " + " inner join  f.fundingDetails fd   ";

            queryString += " where " + " fd.transactionType =1 and  fd.adjustmentType =1" +
                    " and act.actualStartDate>=:startDate and  act.actualCompletionDate<=:endDate  ";
            queryString += ChartWidgetUtil.getTeamQuery(member);
            queryString += " and res.response='Yes' and q.questionNumber=9";

            if (orgIds == null) {
                if(orgGroupId!=null&&orgGroupId!=-1){
                      queryString += " and ah.ampDonorOrgId.orgGrpId=:orgGroupId ";
                }
              
            } else {
                queryString += " and ah.ampDonorOrgId in ("+ChartWidgetUtil.getInStatment(orgIds)+")";
            }
            Query qry = session.createQuery(queryString);
            qry.setDate("startDate", startDate);
            qry.setDate("endDate", endDate);
            if (member != null) {
                qry.setLong("teamId", member.getTeamId());

            }
            if (orgIds ==null&&orgGroupId!=null&&orgGroupId!=-1) {
             qry.setLong("orgGroupId", orgGroupId);
            }
            size = qry.list().size();
        } catch (Exception e) {
            logger.error("Unable get value ", e);

        }

        return size;
    }

    /**
     *
     * @param questionNumber
     * @param indId
     * @param adjustmentType
     * @param currCode
     * @param orgId
     * @param year
     * @return
     */
    public static Double getQ4Value(Long ampAhsurveyId) {
        Double value = null;
        try {
            Session session = PersistenceManager.getRequestDBSession();
            String queryString = "select  res.response" + " from " + AmpAhsurveyResponse.class.getName() + " res inner join res.ampQuestionId  q  ";


            queryString += " where q.questionNumber=4 and res.ampAHSurveyId=:ampAHSurveyId";

            Query qry = session.createQuery(queryString);
            qry.setLong("ampAHSurveyId", ampAhsurveyId);
            String val = (String) qry.uniqueResult();
            if (val != null && !val.equals("")) {
                value = Double.parseDouble(val);
            } else {
                value = new Double(0);
            }


        } catch (Exception e) {
            logger.error("Unable get value ", e);

        }

        return value;
    }

    /**
     *
     * @param questionNumber
     * @param indId
     * @param adjustmentType
     * @param currCode
     * @param orgId
     * @param year
     * @return
     */
    public static long getIndicator10aValue(Date startDate, Date endDate, Long[] orgIds, Long orgGroupId) {
        long value = 0;
        try {
            Session session = PersistenceManager.getRequestDBSession();
            String queryString = "select  distinct cal  from " + AmpCalendar.class.getName() + " cal inner join cal.eventsType  type " + " left join cal.organisations org " +
                    " where (cal.calendarPK.calendar.startDate>=:startDate and cal.calendarPK.calendar.endDate<=:endDate) " + " and type.value='Mission'  "; //I think we need made changes in db structure
            if (orgIds != null) {
                queryString += " and (";
                for (Long orgId : orgIds) {
                    queryString += " ( " + orgId + " in elements(cal.organisations)) or";
                }
                // cutting last or
                queryString = queryString.substring(0, queryString.length() - 2);
                queryString += ")";
            } else {
                if (orgGroupId !=null&&orgGroupId != -1) {
                    queryString += " and org.orgGrpId=:orgGroupId";
                }
            }
            Query qry = session.createQuery(queryString + " and size(cal.organisations)>1 "); //joint
            qry.setDate("startDate", startDate);
            qry.setDate("endDate", endDate);
            if (orgIds == null&& orgGroupId!=null && orgGroupId != -1) {
                qry.setLong("orgGroupId", orgGroupId);
            }
            long jointMisssion = qry.list().size();
            qry = session.createQuery(queryString); // all missions
            qry.setDate("startDate", startDate);
            qry.setDate("endDate", endDate);
            if (orgIds == null && orgGroupId!=null&& orgGroupId != -1) {
                qry.setLong("orgGroupId", orgGroupId);
            }
            long allMisssion = qry.list().size();
            if (allMisssion > 0) {
                value = jointMisssion / allMisssion;
            }


        } catch (Exception e) {
            logger.error("Unable get value ", e);
            e.printStackTrace();
        }

        return value;
    }

	  /**
     * Returns list of 5 (or less) largest projects
     * TODO review this method
     * @param filter
     * @return
     * @throws org.digijava.kernel.exception.DgException
     */
    public static List<Project> getOrganisationLargestProjects(FilterHelper filter) throws DgException {
        Session session = null;
        String queryString = null;
        TeamMember teamMember = filter.getTeamMember();
        List<Project> projects = new ArrayList<Project>();
        Long year = filter.getYear();
        if (year == null || year == -1) {
            year = Long.parseLong(FeaturesUtil.getGlobalSettingValue("Current Fiscal Year"));
        }
        year -= 1; // previous fiscal year
        Long currId = filter.getCurrId();
        String currCode;
        Long orgGroupId = filter.getOrgGroupId();
        if (currId == null) {
            currCode = "USD";
        } else {
            currCode = CurrencyUtil.getCurrency(currId).getCurrencyCode();
        }
        Long[] orgIds = filter.getOrgIds();
        Long fiscalCalendarId = filter.getFiscalCalendarId();
        // apply calendar filter
        Date startDate = getStartDate(fiscalCalendarId, year.intValue());
        Date endDate = getEndDate(fiscalCalendarId, year.intValue());
        try {
            session = PersistenceManager.getRequestDBSession();
            /* pick all activities of the organization in the selected year ordered
            by their amounts in USD
            alas that "Limit" does not work in the query...  */
            queryString = " select act from " + AmpActivityGroup.class.getName() + " actGrp  ";

            queryString += " inner join actGrp.ampActivityLastVersion act " ;

            queryString += " inner join act.funding f " +
                    " inner join f.fundingDetails fd ";

            queryString += "  where " +
                    " fd.transactionType = 0 and  fd.adjustmentType = 1";

            if (orgIds == null) {
                if(orgGroupId!=-1){
                  queryString += ChartWidgetUtil.getOrganizationQuery(true,orgIds);
                }
            } else {
               queryString += ChartWidgetUtil.getOrganizationQuery(false,orgIds);
            }
            queryString += " and fd.transactionDate>=:startDate and  fd.transactionDate<=:endDate  ";
            queryString+=ChartWidgetUtil.getTeamQuery(teamMember);
            queryString +=" group by act order by sum(fd.transactionAmountInBaseCurrency) desc ";

            Query query = session.createQuery(queryString);
            query.setDate("startDate", startDate);
            query.setDate("endDate", endDate);
            if (orgIds == null && orgGroupId!=-1) {
                 query.setLong("orgGroupId", orgGroupId);
             }
            if (teamMember != null) {
                query.setLong("teamId", teamMember.getTeamId());

            }
            List result = query.list();
            if (result.size() > 5) {
                result = result.subList(0, 5);//pick 5 largest projects
            }


            Iterator<AmpActivity> activityIter = result.iterator();
            // converting funding to selected currency amount and creating projects
            while (activityIter.hasNext()) {
                AmpActivity activity=activityIter.next();
                queryString = "select fd from " + AmpFundingDetail.class.getName() + " fd  inner join fd.ampFundingId f ";
                queryString += "   inner join f.ampActivityId act  where   fd.transactionType = 0 and  fd.adjustmentType = 1  ";
                if (orgIds == null) {
                    if(orgGroupId != -1){
                       queryString += ChartWidgetUtil.getOrganizationQuery(true,orgIds);
                    }
                } else {
                    queryString += ChartWidgetUtil.getOrganizationQuery(false, orgIds);
                }
                queryString += " and fd.transactionDate>=:startDate and  fd.transactionDate<=:endDate  and act=" + activity.getAmpActivityId();
                query = session.createQuery(queryString);
                query.setDate("startDate", startDate);
                query.setDate("endDate", endDate);
                if(orgIds==null&& orgGroupId!=-1){
                      query.setLong("orgGroupId",orgGroupId);
                }
                List<AmpFundingDetail> details = query.list();
                Project project = new Project();
                Set<AmpActivitySector> sectors = activity.getSectors();
                Iterator<AmpActivitySector> sectorIter = sectors.iterator();
                String sectorsName = "";
                while (sectorIter.hasNext()) {
                    sectorsName += " " + sectorIter.next().getSectorId().getName() + ",";
                }
                if (sectorsName.length() > 0) {
                    sectorsName = sectorsName.substring(0, sectorsName.length() - 1);
                }
                project.setSectorNames(sectorsName);
                FundingCalculationsHelper cal = new FundingCalculationsHelper();
                cal.doCalculations(details, currCode);

                Double amount = cal.getTotActualComm().doubleValue();

                project.setAmount(FormatHelper.formatNumber(amount));
                String title = activity.getName();
                if (title.length() > 15) {
                    project.setFullTitle(title);
                    title = title.substring(0, 14) + "...";
                }
                project.setTitle(title);
                project.setActivityId(activity.getAmpActivityId());
                projects.add(project);

            }
        } catch (Exception e) {
            logger.error("error",e);
            throw new DgException(
                    "Cannot load sector fundings by donors from db", e);
        }


        return projects;
    }

    public static Date getStartDate(Long fiscalCalendarId, int year) {
        Date startDate = null;
        if (fiscalCalendarId != null && fiscalCalendarId != -1) {
            AmpFiscalCalendar calendar = FiscalCalendarUtil.getAmpFiscalCalendar(fiscalCalendarId);
            startDate = getStartOfYear(year, calendar.getStartMonthNum() - 1, calendar.getStartDayNum());
        } else {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MONTH, Calendar.JANUARY);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.set(Calendar.YEAR, year);
            startDate = cal.getTime();
        }
        return startDate;
    }

    public static Date getEndDate(Long fiscalCalendarId, int year) {
        Date endDate = null;
        if (fiscalCalendarId != null && fiscalCalendarId != -1) {
            AmpFiscalCalendar calendar = FiscalCalendarUtil.getAmpFiscalCalendar(fiscalCalendarId);
            //we need data including the last day of toYear,this is till the first day of toYear+1
            int MILLISECONDS_IN_DAY = 1000 * 60 * 60 * 24;
            endDate = new Date(getStartOfYear(year + 1, calendar.getStartMonthNum() - 1, calendar.getStartDayNum()).getTime() - MILLISECONDS_IN_DAY);

        } else {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MONTH, Calendar.DECEMBER);
            cal.set(Calendar.DAY_OF_MONTH, 31);
            cal.set(Calendar.YEAR, year);
            endDate = cal.getTime();
        }
        return endDate;
        }

    public static Date getStartOfYear(int year, int month, int day) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.set(year, month, day, 0, 0, 0);
        return cal.getTime();
    }

    /**
     * validation method that only allows you to add a chart or table type once
     * @param type
     * @param widgetOrgId
     * @return
     */
    public static boolean widgetTypeExists(Long type, Long widgetOrgId) {
        boolean exists = true;
        try {
            Session session = PersistenceManager.getRequestDBSession();
            String queryString = "select  widOrg from " + AmpWidgetOrgProfile.class.getName() + " widOrg where widOrg.type=:type ";
            if (widgetOrgId != null) {
                queryString += " and widOrg.id!=:widgetOrgId";
            }
            Query qry = session.createQuery(queryString);
            qry.setLong("type", type);
            if (widgetOrgId != null) {
                qry.setLong("widgetOrgId", widgetOrgId);
            }
            if (qry.list().size() == 0) {
                exists = false;
            }
        } catch (DgException ex) {
            logger.error("Unable get value ", ex);

        }


        return exists;
    }
    /**
     * Creates string consisting of surveys ids
     * @param questionNumbers
     * @return
     * @throws org.digijava.kernel.exception.DgException
     */
    public static String getAmpAhsurveyCondition(int[] questionNumbers) throws DgException{
            Session session = PersistenceManager.getRequestDBSession();
            String queryString = "";
            String whereCondition = "";
            Query qry = null;
            List<AmpAhsurvey> surveys = new ArrayList<AmpAhsurvey>();
            List<AmpAhsurvey> selectedSurveys = new ArrayList<AmpAhsurvey>();
            for (int i = 0; i < questionNumbers.length; i++) {
                queryString = " select ah from " + AmpAhsurvey.class.getName()+" ah ";
                // not all  actual disbursement
                if (questionNumbers[0] != 0) {
                    queryString += " inner join ah.responses res  " + " inner join res.ampQuestionId  q  ";
                    queryString += " where  res.response='Yes'" + " and q.questionNumber=" + questionNumbers[i] + ") ";
                }
                qry = session.createQuery(queryString);
                List surveyList = qry.list();
                if (questionNumbers.length == 1) {
                    surveys.addAll(surveyList);

                } else {
                    if (surveyList.size() > 0) {
                        if (i == 0) {
                            surveys.addAll(surveyList);
                        } else {
                            Iterator<AmpAhsurvey> iter = surveyList.iterator();
                            while (iter.hasNext()) {
                                AmpAhsurvey survey = iter.next();
                                if (surveys.contains(survey)) {
                                    selectedSurveys.add(survey);
                                }
                            }
                            surveys.clear();
                            surveys.addAll(selectedSurveys);
                            selectedSurveys.clear();

                        }

                    } else {
                        surveys.clear();
                    }

                }

            }
            Iterator<AmpAhsurvey> iter = surveys.iterator();
            while (iter.hasNext()) {
                if (whereCondition.length() != 0) {
                    whereCondition += ",";
                }
                AmpAhsurvey survey = iter.next();
                whereCondition += survey.getAmpAHSurveyId();
            }
            return whereCondition ;
    }
    /**
     * Returns funding amount for the specified surveys
     * @param indCode
     * @param adjustmentType
     * @param currCode
     * @param orgId
     * @param orgGroupId
     * @param startDate
     * @param endDate
     * @param teamMember
     * @param condition
     * @return
     * @throws org.digijava.kernel.exception.DgException
     */
    public static Double getValue(String indCode, int adjustmentType, String currCode, Long[] orgIds, Long orgGroupId, Date startDate, Date endDate, TeamMember teamMember, String condition) throws DgException {
        Session session = PersistenceManager.getRequestDBSession();
        String queryString = "";
        Double total = new Double(0);
        Query qry = null;
        queryString = "select new AmpFundingDetail(fd.transactionType,fd.adjustmentType,";
        queryString += "fd.transactionAmount,fd.transactionDate,fd.ampCurrencyId,fd.fixedExchangeRate";
        if (indCode.equals("4")) {
            queryString += ", ah.ampAHSurveyId";
        }
        queryString += ") from " +
                AmpAhsurvey.class.getName() +
                " ah inner join ah.responses res  " + " inner join ah.ampActivityId act   " + " inner join act.funding f   " + " inner join  f.fundingDetails fd   ";
        queryString += " where  fd.transactionType =1 and  fd.adjustmentType =:adjustmentType" +
                "  and fd.transactionDate>=:startDate and  fd.transactionDate<=:endDate ";
        queryString += ChartWidgetUtil.getTeamQuery(teamMember);

        if (orgIds == null) {
            if (orgGroupId!=null&&orgGroupId != -1) {
                queryString += " and ah.ampDonorOrgId.orgGrpId=:orgGroupId ";
            }
        } else {
            queryString += " and ah.ampDonorOrgId in (" + ChartWidgetUtil.getInStatment(orgIds) + ") ";
        }

        // specified survyes
        queryString += " and ah.ampAHSurveyId in (" + condition + ")";

        qry = session.createQuery(queryString);
        qry.setDate("startDate", startDate);
        qry.setDate("endDate", endDate);
        if (teamMember != null) {
            qry.setLong("teamId", teamMember.getTeamId());

        }
        qry.setInteger("adjustmentType", adjustmentType);
        if (orgIds == null&&orgGroupId!=null&&orgGroupId!=-1) {
           qry.setLong("orgGroupId", orgGroupId);
        }
        List<AmpFundingDetail> fundingDets = qry.list();
        FundingCalculationsHelper cal = new FundingCalculationsHelper();
        cal.doCalculations(fundingDets, currCode);
        DecimalWraper tot = null;
        if (adjustmentType == 1) {
            tot = cal.getTotActualDisb();
        } else {
            tot = cal.getTotPlanDisb();
        }
        total = tot.doubleValue();
        return total;
    }
    public static List<NameValueYearHelper> getData(FilterHelper filter,int type) throws DgException {
        List<NameValueYearHelper> result = new ArrayList<NameValueYearHelper>();
        TreeMap<Long,String> totalValues=new TreeMap<Long,String>();
        Long year = filter.getYear();
        if (year == null || year == -1) {
            year = Long.parseLong(FeaturesUtil.getGlobalSettingValue("Current Fiscal Year"));
        }

        Long currId = filter.getCurrId();
        String currCode;
        if (currId == null) {
            currCode = "USD";
        } else {
            currCode = CurrencyUtil.getCurrency(currId).getCurrencyCode();
        }
        Long fiscalCalendarId = filter.getFiscalCalendarId();
        Collection<AmpCategoryValue>  categoryValues=null;
        if(type==WidgetUtil.ORG_PROFILE_ODA_PROFILE){
            categoryValues=CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.FINANCING_INSTRUMENT_KEY);
        }
        else{
            categoryValues = CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.TYPE_OF_ASSISTENCE_KEY);
        }

        for (AmpCategoryValue categoryValue: categoryValues) {
            NameValueYearHelper nameValueYearHelper = new NameValueYearHelper();
            nameValueYearHelper.setName(categoryValue.getValue());
            for (Long i = year - 4; i <= year; i++) {
                // apply calendar filter
                Date startDate = OrgProfileUtil.getStartDate(fiscalCalendarId, i.intValue());
                Date endDate = OrgProfileUtil.getEndDate(fiscalCalendarId, i.intValue());
                DecimalWraper funding =null;
                if (type == WidgetUtil.ORG_PROFILE_ODA_PROFILE) {
                   funding = ChartWidgetUtil.getFundingByFinancingInstrument(filter.getOrgIds(), filter.getOrgGroupId(), startDate, endDate, categoryValue.getId(), currCode, filter.getTransactionType(), filter.getTeamMember());
                } else {
                   funding = ChartWidgetUtil.getFunding(filter.getOrgIds(), filter.getOrgGroupId(), startDate, endDate, categoryValue.getId(), currCode, filter.getTransactionType(), filter.getTeamMember());
                }
                if(nameValueYearHelper.getYearValues()==null){
                    nameValueYearHelper.setYearValues(new TreeMap<Long,String>());
                }
                if(totalValues.containsKey(i)){
                    String value=(String)totalValues.get(i);
                    Double newValue=funding.doubleValue()+FormatHelper.parseDouble(value);
                    totalValues.remove(i);
                    totalValues.put(i, FormatHelper.formatNumber(newValue));
                }
                else{
                    totalValues.put(i, FormatHelper.formatNumber(funding.doubleValue()));
                }
                nameValueYearHelper.getYearValues().put(i, FormatHelper.formatNumber(funding.doubleValue()));

            }
            result.add(nameValueYearHelper);

        }

        NameValueYearHelper nameValueYearHelper = new NameValueYearHelper();
        nameValueYearHelper.setName("TOTAL");
        nameValueYearHelper.setYearValues(totalValues);
        result.add(nameValueYearHelper);
        return result;
    }
    public static void getDataTable(PdfPTable table,FilterHelper filter,Long siteId,String langCode,int type) throws Exception{
        for (int i = 4; i >= 0; i--) {
            PdfPCell cell=new PdfPCell(new Paragraph(""+(filter.getYear() - i),HEADERFONT));
            cell.setBackgroundColor(TITLECOLOR);
            table.addCell(cell);
        }
        List<NameValueYearHelper> values = OrgProfileUtil.getData(filter,type);
        ListIterator<NameValueYearHelper> valuesIter = values.listIterator();
        while (valuesIter.hasNext()) {
            NameValueYearHelper value = valuesIter.next();
            int index=valuesIter.nextIndex();
            PdfPCell cellCatValue=new PdfPCell(new Paragraph(TranslatorWorker.translateText(value.getName(), langCode, siteId),PLAINFONT ));
            if(index%2==0){
                    cellCatValue.setBackgroundColor(CELLCOLOR);
            }
            table.addCell(cellCatValue);
            Collection<String> yearValues = value.getYearValues().values();
            Iterator<String> yearValuesIter = yearValues.iterator();
            while (yearValuesIter.hasNext()) {
                PdfPCell cell=new PdfPCell(new Paragraph(yearValuesIter.next(),PLAINFONT));
                if(index%2==0){
                    cell.setBackgroundColor(CELLCOLOR);
                }
                table.addCell(cell);
            }
        }

    }

    public static void getDataTable(Table table,FilterHelper filter,Long siteId,String langCode,int type) throws Exception{
        for (int i = 4; i >= 0; i--) {
            RtfCell cell=new RtfCell(new Paragraph(""+(filter.getYear() - i),HEADERFONT));
            cell.setBackgroundColor(TITLECOLOR);
            table.addCell(cell);
        }
        List<NameValueYearHelper> values = OrgProfileUtil.getData(filter,type);
        ListIterator<NameValueYearHelper> valuesIter = values.listIterator();
        while (valuesIter.hasNext()) {
            NameValueYearHelper value = valuesIter.next();
            int index=valuesIter.nextIndex();
            RtfCell cellCatValue=new RtfCell(new Paragraph(TranslatorWorker.translateText(value.getName(), langCode, siteId),PLAINFONT ));
            if(index%2==0){
                    cellCatValue.setBackgroundColor(CELLCOLOR);
            }
            table.addCell(cellCatValue);
            Collection<String> yearValues = value.getYearValues().values();
            Iterator<String> yearValuesIter = yearValues.iterator();
            while (yearValuesIter.hasNext()) {
                RtfCell cell=new RtfCell(new Paragraph(yearValuesIter.next(),PLAINFONT));
                if(index%2==0){
                    cell.setBackgroundColor(CELLCOLOR);
                }
                table.addCell(cell);
            }
        }

    }
    public static String getFooterText(String langCode, Long siteId, FilterHelper filter) throws WorkerException {
        String footerText = TranslatorWorker.translateText("Selected organizations", langCode, siteId) + ": ";
        if (filter.getOrgIds() != null) {
            for (Long id : filter.getOrgIds()) {
                AmpOrganisation org = DbUtil.getOrganisation(id);
                footerText += org.getName() + ", ";
            }
            if (footerText.length() > 0) {
                footerText = footerText.substring(0, footerText.length() - 2);
            }
        } else {
            footerText += TranslatorWorker.translateText("All", langCode, siteId);
        }
        if (filter.getOrgGroupId() != null && filter.getOrgGroupId() != -1) {
            footerText += " " + TranslatorWorker.translateText("from", langCode, siteId) + " "+filter.getOrgGroup().getOrgGrpName();
        }
        return footerText;
    }

}
