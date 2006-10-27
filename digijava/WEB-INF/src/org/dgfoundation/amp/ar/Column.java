/**
 * Column.java
 * (c) 2005 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.cell.Cell;
import org.dgfoundation.amp.ar.workers.ColumnWorker;

/**
 * Wraps the items that can be displayed in a report column. A Column can hold
 * CellS,other ColumnS or some more exotic structures that are yet to be defined
 * ...
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since May 31, 2006
 * 
 */
public abstract class Column extends Viewable implements ColumnIdentifiable {
	
	protected int spanCount = 0;

	protected List items;

	protected Viewable parent;

	protected int rowSpan;
	
	protected boolean visible;

	protected String contentCategory;
	
	protected int currentDepth = 0;

	protected String name;

	public abstract int getCurrentRowSpan();

	public String toString() {
		return name + " (" + items.size() + " items)";
	}

	protected static Logger logger = Logger.getLogger(CellColumn.class);

	protected ColumnWorker worker;

	public abstract int getWidth();

	public abstract Column postProcess();

	public abstract Column filterCopy(Cell filter, Set ids);

	/**
	 * Iterator for the internal list of items
	 * 
	 * @return the Iterator
	 */
	public Iterator iterator() {
		return items.iterator();
	}

	/**
	 * returns the identifier for this column
	 */
	public Object getColumnId() {
		return name;
	}

	/**
	 * gets the last Cell added to the list
	 * 
	 * @return the Cell or null if unavailable
	 */
	public Object getLastItem() {
		if (items.size() > 0)
			return getItem(items.size() - 1);
		else
			return null;
	}

	/**
	 * Returns true for ColumnS that can generate trail cells
	 * @return true if the column can generate trail cells
	 */
	public abstract boolean hasTrailCells();
	
	/**
	 * Gets the name of the current Column based on the hideContent parameter and hasTrailCells property
	 * @param hideContent true if we are hiding content rows (summary report)
	 * @return the Column name or "-"
	 */
	public String getName(Boolean hideContent) {
		if(hasTrailCells() || !hideContent.booleanValue()) return getName(); 
		return "-";
	}
	
	/**
	 * returns the cell with the specified list position
	 * 
	 * @param indexPos
	 *            the index position in the intenal items list
	 * @return the Cell, or null if unavailable
	 * @see org.dgfoundation.amp.ar.cell.Cell
	 */
	public Object getItem(int indexPos) {
		if (indexPos > items.size() - 1)
			return null;

		return items.get(indexPos);
	}

	/**
	 * Constructs a column from a given
	 * 
	 * @param parent
	 * @param name
	 */
	public Column(Viewable parent, String name) {
		super();
		this.name = name;
		items = new ArrayList();
		this.parent = parent;
	}

	public Column(ColumnWorker worker) {
		super();
		this.worker = worker;
		items = new ArrayList();
	}

	public Column(String name) {
		super();
		items = new ArrayList();
		this.name = name;
	}

	public Column() {
		super();
		items = new ArrayList();
	}

	/**
	 * @return Returns the worker.
	 */
	public ColumnWorker getWorker() {
		return worker;
	}

	/**
	 * @param worker
	 *            The worker to set.
	 */
	public void setWorker(ColumnWorker worker) {
		this.worker = worker;
	}

	/**
	 * @return Returns the parent.
	 */
	public Viewable getParent() {
		return parent;
	}

	/**
	 * @param parent
	 *            The parent to set.
	 */
	public void setParent(Column parent) {
		this.parent = parent;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Returns the items.
	 */
	public List getItems() {
		return items;
	}

	/**
	 * @param parentReport
	 *            The parentReport to set.
	 */

	public String getCurrentView() {
		return parent.getCurrentView();
	}

	/**
	 * @param parent
	 *            The parent to set.
	 */
	public void setParent(Viewable parent) {
		this.parent = parent;
	}

	public abstract int getColumnSpan();

	/**
	 * returns a list of ColumnS that can be found at the specified depth level.
	 * 
	 * @param depth
	 *            the depth level from where we want subcolumns extracted. a
	 *            CellColumn will always return itself regardless of the depth
	 *            level specified (because it has no subcolumns). A depth of 0
	 *            generally means the current column.
	 * @return a list of ColumnS on the specified depth position
	 */
	public abstract List getSubColumns(int depth);

	public List getSubColumnList() {
		return getSubColumns(currentDepth);
	}

	/**
	 * @return Returns the currentDepth.
	 */
	public int getCurrentDepth() {
		return currentDepth;
	}

	/**
	 * @param currentDepth
	 *            The currentDepth to set.
	 */
	public void setCurrentDepth(int currentDepth) {
		this.currentDepth = currentDepth;
	}

	/**
	 * @return Returns the rowSpan.
	 */
	public int getRowSpan() {
		return rowSpan;
	}

	/**
	 * @param rowSpan
	 *            The rowSpan to set.
	 */
	public void setRowSpan(int rowSpan) {
		spanCount = 0;
		this.rowSpan = rowSpan;
	}

	public abstract Set getOwnerIds();

	/**
	 * Produces a list of trail CellS. These are usually custom made cells that
	 * represent some information regarding the Column, that are usually
	 * displayed at the bottom of the column. For AmountCellColumnS this is the
	 * place to display totals. However, other types of behaviour can be
	 * implemented, like error reporting for debugging purposes
	 * @return the list of trail cells
	 */
	public abstract List getTrailCells();

	public abstract Column newInstance();
	
	
	public abstract int getColumnDepth();

	/**
	 * Counts the leafs (Cells) in the column tree
	 * @return the cell count
	 */
	public abstract int getCellCount();

	/**
	 * Counts the leafs (Cells) in the column tree that are visible (toString not empty)
	 * @param ownerId TODO
	 * @return the cell count
	 */
	public abstract int getVisibleCellCount(Long ownerId);
	
	/**
	 * Returns the full column name for this column. This means that, if this column has a parent
	 * it wil append its name to the output
	 * @return the complete column name as seen in the header
	 */
	public String getAbsoluteColumnName(){
		if (parent!=null && parent instanceof Column) return ((Column)parent).getAbsoluteColumnName()+" -- "+ this.name;
		else return this.name;
	}

	/**
	 * @param contentCategory The contentCategory to set.
	 */
	public void setContentCategory(String categ) {
		if(this.contentCategory!=null) return;
		this.contentCategory = categ;
		//logger.info("Column "+this.getAbsoluteColumnName()+" has categ="+categ);
	}

	
	
	/**
	 * 
	 * @param measures
	 * @param category
	 */
	public void applyVisibility(Set measures,String category) {
		if(!category.equals(this.contentCategory)) {visible=true;return;}
		if(!ARUtil.containsMeasure(name,measures)) visible=false;else visible=true; 
	}

	/**
	 * @return Returns the visible.
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * @param visible The visible to set.
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	/**
	 * @return Returns the contentCategory.
	 */
	public String getContentCategory() {
		return contentCategory;
	}
	
}
