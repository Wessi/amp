package org.digijava.module.gis.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.utils.AmpCollectionUtils.KeyResolver;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.util.collections.CollectionSynchronizer;
import org.digijava.module.gis.dbentity.AmpDaColumn;
import org.digijava.module.gis.dbentity.AmpDaTable;
import org.digijava.module.gis.dbentity.AmpDaValue;
import org.digijava.module.gis.dbentity.AmpDaWidgetPlace;
import org.digijava.module.gis.widget.table.DaCell;
import org.digijava.module.gis.widget.table.DaRow;
import org.digijava.module.gis.widget.table.DaTable;

/**
 * Utilities for table widgets,
 * @author Irakli Kobiashvili
 *
 */
public class TableWidgetUtil {

    private static Logger logger = Logger.getLogger(TableWidgetUtil.class);
	
    /**
     * Returns ALL tables widgets.
     * @return
     * @throws DgException
     */
    @SuppressWarnings("unchecked")
	public static Collection<AmpDaTable> getAllTableWidgets() throws DgException{
    	Session session=PersistenceManager.getRequestDBSession();
    	Collection<AmpDaTable> result=null;
    	try {
			Query query=session.createQuery("from "+AmpDaTable.class.getName());
			List list=query.list();
			result=list;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
    	return result;
    }
    
    /**
     * Lads table widget by ID.
     * @param id
     * @return
     * @throws DgException
     */
    public static AmpDaTable getTableWidget(Long id) throws DgException{
		Session session=PersistenceManager.getRequestDBSession();
		AmpDaTable result=null;
		try {
			result=(AmpDaTable)session.load(AmpDaTable.class, id);
		} catch (Exception e) {
			throw new DgException("Cannot get Table Widget!",e);
		}
    	return result;
    }
    
    /**
     * Creates new widget in db.
     * @param widget
     * @throws DgException
     */
	public static void createWidget(AmpDaTable widget) throws DgException{
		Session session=PersistenceManager.getRequestDBSession();
		Transaction tx=null;
		try {
			tx=session.beginTransaction();
			session.save(widget);
			tx.commit();
		} catch (Exception e) {
			if (tx!=null){
				try {
					tx.rollback();
				} catch (Exception e1) {
					throw new DgException("Cannot rallback Table Widget creation!",e1);
				}
			}
			throw new DgException("Cannot create Table Widget!",e);
		}
	}
	
	/**
	 * Updates already existing widget in db.
	 * @param widget
	 * @throws DgException
	 */
	public static void updateWidget(AmpDaTable widget) throws DgException{
		Session session=PersistenceManager.getRequestDBSession();
		Transaction tx=null;
		try {
			tx=session.beginTransaction();
			session.update(widget);
			tx.commit();
		} catch (Exception e) {
			if (tx!=null){
				try {
					tx.rollback();
				} catch (Exception e1) {
					throw new DgException("Cannot rallback Table Widget update!",e1);
				}
			}
			throw new DgException("Cannot update Table Widget!",e);
		}
	}

	/**
	 * Saves or updates widget and also place if specified.
	 * @param widget table widget together with columns. 
	 * @param place this can be null. if not null than save is attempted in same transaction. 
	 * @throws DgException
	 */
	public static void saveOrUpdateWidget(AmpDaTable widget, AmpDaWidgetPlace place) throws DgException{
		Session session=PersistenceManager.getRequestDBSession();
		Transaction tx=null;
		try {
			tx=session.beginTransaction();
			if (place != null){
				session.update(place);
			}
			session.saveOrUpdate(widget);
			tx.commit();
		} catch (Exception e) {
			if (tx!=null){
				try {
					tx.rollback();
				} catch (Exception e1) {
					throw new DgException("Cannot rallback Table Widget save or update!",e1);
				}
			}
			throw new DgException("Cannot save or update Table Widget!",e);
		}
	}
	
	/**
	 * Removes widget from db.
	 * @param widget
	 * @throws DgException
	 */
	public static void deleteWidget(AmpDaTable widget) throws DgException{
		Session session=PersistenceManager.getRequestDBSession();
		Transaction tx=null;
		try {
			tx=session.beginTransaction();
			session.delete(widget);
			tx.commit();
		} catch (Exception e) {
			if (tx!=null){
				try {
					tx.rollback();
				} catch (Exception e1) {
					throw new DgException("Cannot rallback Table Widget delete!",e1);
				}
			}
			throw new DgException("Cannot delete Table Widget!",e);
		}
		
	}
	
	/**
	 * Removes widget from db.
	 * @param id
	 * @throws DgException
	 */
	public static void deleteWidget(Long id) throws DgException{
		AmpDaTable widget=getTableWidget(id);
		deleteWidget(widget);
	}

	//=======Data=======================
	
	@SuppressWarnings("unchecked")
	public static List<AmpDaValue> getTableData(Long tableId)throws DgException{
		Session session = PersistenceManager.getRequestDBSession();
		List<AmpDaValue> results=null;
		String oql="select v from ";
		oql += AmpDaValue.class.getName()+" as v, ";
		oql += AmpDaColumn.class.getName()+ " as c, ";
		oql += AmpDaTable.class.getName()+ " as t ";
		oql += " where ";
		oql += " v.column = c and c.widget = t and t.id=:tableId";
		oql += " order by v.pk";
		try {
			Query query=session.createQuery(oql);
			query.setLong("tableId", tableId);
			results = query.list();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DgException("Error loading table data for id=",e);
		}
		return results;
	}
	
	public static List<DaRow> dataToHelpers(Collection<AmpDaValue> data)throws DgException{
		List<DaRow> rows=null;
		Map<Long, DaRow> rowsByPk=new HashMap<Long, DaRow>();
		for (AmpDaValue value : data) {
			DaRow row=rowsByPk.get(value.getPk());
			if (null == row){
				row = new DaRow();
				rowsByPk.put(value.getPk(), row);
				row.setPk(value.getPk().toString());
				row.setCells(new ArrayList<DaCell>(10));
			}
			DaCell cell = new DaCell(value);
			row.getCells().add(cell);
		}
		rows = new ArrayList<DaRow>(rowsByPk.values());
		for (DaRow daRow : rows) {
			Collections.sort(daRow.getCells(),new CellOrderNoComparator());
		}
		return rows;
	}
	//=======PLACES=====================
	public static AmpDaWidgetPlace getPlace(String code) throws DgException{
		AmpDaWidgetPlace result=null;
		Session session = PersistenceManager.getRequestDBSession();
		String oql="from "+AmpDaWidgetPlace.class.getName()+" as p ";
		oql += " where p.code like :placeCode";
		try {
			Query q=session.createQuery(oql);
			q.setString("placeCode", code);
			result = (AmpDaWidgetPlace)q.uniqueResult();
		} catch (Exception e) {
			logger.error(e);
			throw new DgException("Cannot load widget place object from db",e);
		}
		return result;
	}
	
	public static void savePlace(AmpDaWidgetPlace place) throws DgException{
		Session session=PersistenceManager.getRequestDBSession();
		Transaction tx=null;
		try {
			tx=session.beginTransaction();
			session.saveOrUpdate(place);
			tx.commit();
		} catch (Exception e) {
			if (tx!=null){
				try {
					tx.rollback();
				} catch (Exception e1) {
					throw new DgException("Cannot rallback Widget place save or update!",e1);
				}
			}
			throw new DgException("Cannot save or update Widget place!",e);
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public static List<AmpDaWidgetPlace> getAllPlaces() throws DgException{
		List<AmpDaWidgetPlace> result=null;
		Session session = PersistenceManager.getRequestDBSession();
		String oql="from "+AmpDaWidgetPlace.class.getName()+" as p ";
		try {
			Query q=session.createQuery(oql);
			result = q.list();
		} catch (Exception e) {
			logger.error(e);
			throw new DgException("Cannot load all widget place object from db",e);
		}
		return result;
	}
	
	
	//=======session====================

	
	public static DaTable getTableHelperFromSession(HttpServletRequest request)throws DgException{
		
		return null;
	}
	
	
	//=======COLUMNS====================
	
	
	//=======key resolvers==============
	public static class TableWidgetKeyResolver implements KeyResolver<Long, AmpDaTable>{
		public Long resolveKey(AmpDaTable element) {
			return element.getId();
		}
	}
	public static class TableWidgetColumnKeyResolver implements KeyResolver<Long, AmpDaColumn>{
		public Long resolveKey(AmpDaColumn element) {
			return element.getId();
		}
	}
	
	public static class WidgetPlaceKeyResolver implements KeyResolver<Long, AmpDaWidgetPlace>{
		public Long resolveKey(AmpDaWidgetPlace element) {
			return element.getId();
		}
		
	}
	
	//========comparators=============
	/**
	 * Compares {@link AmpDaColumn} with its orderNo field.
	 *
	 */
	public static class ColumnOrderNoComparator implements Comparator<AmpDaColumn>{

		public int compare(AmpDaColumn col1, AmpDaColumn col2) {
			return col1.getOrderNo().compareTo(col2.getOrderNo());
		}
	}
	
	public static class RowPkComparator implements Comparator<DaRow>{
		public int compare(DaRow r1, DaRow r2) {
			return r1.getPk().compareTo(r2.getPk());
		}
	}
	
	/**
	 * Compares DaCell helpers by its column order number.
	 *
	 */
	public static class CellOrderNoComparator implements Comparator<DaCell>{
		public int compare(DaCell c1, DaCell c2) {
			return c1.getColumnOrderNo().compareTo(c2.getColumnOrderNo());
		}
	}
	
	
	//not used yet. will reimplement in AmpCollectionUtils with java generics.
	public static class ColumnSynzchronizer implements CollectionSynchronizer{

		public boolean removeEvent(Object arg0) {
			return false;
		}

		public boolean synchronizeEvent(Object arg0, Object arg1) {
			return false;
		}

		public int compare(Object arg0, Object arg1) {
			return 0;
		}
		
	}

	public static DaTable widgetToHelper(AmpDaTable widget) throws DgException{
		DaTable table=null;
		table = new DaTable(widget);
		return table;
	}
	
}
