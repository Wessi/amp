package org.digijava.kernel.ampapi.endpoints.datafreeze;

import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.dbentity.AmpDataFreezeSettings;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.List;

import org.apache.log4j.Logger;

public final class DataFreezeUtil {
    private static Logger logger = Logger.getLogger(DataFreezeUtil.class);

    private DataFreezeUtil() {
    }

    public static AmpDataFreezeSettings getDataFreezeEventById(Long id) {
        return (AmpDataFreezeSettings) PersistenceManager.getSession().get(AmpDataFreezeSettings.class, id);
    }

    public static void saveDataFreezeEvent(AmpDataFreezeSettings dataFreezeEvent) {
        Session session = null;
        try {
            session = PersistenceManager.getSession();
            session.saveOrUpdate(dataFreezeEvent);
        } catch (Exception e) {
            logger.error("Exception from saveDataFreezeEvent: " + e.getMessage());
        }
    }

    public static void deleteDataFreezeEvent(Long id) {
        AmpDataFreezeSettings dataFreezeEvent = getDataFreezeEventById(id);
        Session session = null;
        try {
            session = PersistenceManager.getSession();
            session.delete(dataFreezeEvent);
        } catch (Exception e) {
            logger.error("Exception from deleteDataFreezeEvent: " + e.getMessage());
        }
    }

    public static Integer getFreezeEventsTotalCount() {
        Session dbSession = PersistenceManager.getSession();
        String queryString = "select count(*) from " + AmpDataFreezeSettings.class.getName();
        Query query = dbSession.createQuery(queryString);
        return (Integer) query.uniqueResult();
    }

    public static List<AmpDataFreezeSettings> getDataFreeEventsList(Integer offset, Integer count, String orderBy,
            String sort, Integer total) {
        Integer maxResults = count == null ? DataFreezeConstants.DEFAULT_RECORDS_PER_PAGE : count;
        Integer startAt = (offset == null || offset > total) ? DataFreezeConstants.DEFAULT_OFFSET : offset;
        String orderByColumn = (orderBy == null) ? DataFreezeConstants.DEFAULT_SORT_COLUMN : orderBy;
        String sortOrder = (sort == null) ? DataFreezeConstants.ORDER_DESC : sort;

        Session dbSession = PersistenceManager.getSession();
        String queryString = "select dataFreezeEvents from " + AmpDataFreezeSettings.class.getName()
                + " dataFreezeEvents order by " + orderByColumn + " " + sortOrder;
        Query query = dbSession.createQuery(queryString);
        query.setFirstResult(startAt);
        query.setMaxResults(maxResults);

        return query.list();
    }

    public static void unfreezeAll() {
        try {
            Session dbSession = PersistenceManager.getSession();
            String queryString = "update " + AmpDataFreezeSettings.class.getName() + " d set d.enabled = false";
            dbSession.createQuery(queryString).executeUpdate();
        } catch (Exception e) {
            logger.error("Exception from unfreezeAll: " + e.getMessage());
        }
    }

    public static List<AmpDataFreezeSettings> getEnabledDataFreezeEvents(
            AmpDataFreezeSettings.FreezeOptions freezeOption) {
        Session dbSession = PersistenceManager.getSession();
        String queryString = "select dataFreezeEvent from " + AmpDataFreezeSettings.class.getName()
                + " dataFreezeEvent where dataFreezeEvent.enabled = true";
        if(freezeOption != null) {
            queryString += " and dataFreezeEvent.freezeOption = :freezeOption";
        }        
                
        Query query = dbSession.createQuery(queryString);
        if(freezeOption != null) {
           query.setParameter("freezeOption", freezeOption);
        }
        return query.list();
    }
    
    public static List<User> getUsers(){       
          Session session = PersistenceManager.getRequestDBSession();
          String queryString = "from " + User.class.getName();
          Query query = session.createQuery(queryString);
          return query.list();        
    }
}
