/**
 * GroupColumn.java
 * (c) 2005 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.dgfoundation.amp.ar.cell.AmountCell;
import org.dgfoundation.amp.ar.cell.Cell;
import org.digijava.module.aim.dbentity.AmpMeasures;
import org.digijava.module.aim.dbentity.AmpReportMeasures;
import org.digijava.module.aim.dbentity.AmpReports;

/**
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org Column that is built
 *         for grouping other ColumnS. This Column can hold only items of type
 *         Column and is basically a list of ColumnS.
 * @since Jun 22, 2006
 * 
 */
public class GroupColumn extends Column {

    	@Override
	public int getVisibleRows() {
    	 Iterator i=items.iterator();
 	    int ret=0;
 	    while (i.hasNext()) {
		Column element = (Column) i.next();
		int visCol=element.getVisibleRows();
		if(visCol>ret) ret=visCol;
	    }
 	    return ret+1; //one is for the title
	}
    
	public int getWidth() {
		int ret=0;
		Iterator i=items.iterator();
		while (i.hasNext()) {
			Column element = (Column) i.next();
			ret+=element.getWidth();
		}
		return ret;
	}
	
	public static Column verticalSplitByCategs(CellColumn src,
            List categories, boolean generateTotalCols,AmpReports reportMetadata) {
		
		
		return verticalSplitByCategs(src,categories,null,generateTotalCols, reportMetadata);
	}
	
	/**
	 * Split a column holding CategAmountCellS into several subcolumns based on the categorized amount data and some
	 * categories given as reference. The result will be a categorized column tree of GroupColumnS and CellColumnS as leafs. 
	 * Each category will create another level on the tree while on the same level we will find several GroupColumnS that 
	 * share the same metainfo category but not the same value.
	 * @param src The source CellColumn to be categorized
	 * @param categories the list of categories to be applied to the src column
	 * @param generateTotalCols true when creating TotalAmountColumnS instead of CellColumnS 
	 * @return a GroupColumn that holds the categorized data
	 * @see MetaInfo, TotalAmountColumn, CategAmountCell
	 */
    private static Column verticalSplitByCategs(Column src,
            List categories, Set ids,boolean generateTotalCols,AmpReports reportMetadata) {
        String cat = (String) categories.remove(0);
        if (categories.size() > 0)
            return verticalSplitByCategs(verticalSplitByCateg(src, cat,ids,
                    false,reportMetadata), categories, ids, generateTotalCols,reportMetadata);
        else
            return verticalSplitByCateg(src, cat, ids, generateTotalCols, reportMetadata);
    }

    /**
     * Helper method that only uses one category to create a categorized tree. This method is internally used and should not
     * be invoked by the developer directly.
     * @param src The source column to be categorized
     * @param category the category to categorize the data with
     * @param generateTotalCols true when creating TotalAmountColumnS instead of CellColumnS
     * @return a GroupColumn that holds the categorized Data
     * @see verticalSplitByCategs
     */
    private static Column verticalSplitByCateg(Column src, 
            String category,Set ids, boolean generateTotalCols,AmpReports reportMetadata) {    
    	if(src instanceof CellColumn) 
    		return verticalSplitByCateg((CellColumn)src,category,ids,generateTotalCols,reportMetadata);
    	else {
    		GroupColumn srcG=(GroupColumn) src;
    		GroupColumn dest=null;
   			dest=new GroupColumn(src);
    		Iterator i=srcG.iterator();
    		while (i.hasNext()) {
				Column element = (Column) i.next();
				Column splitted=verticalSplitByCateg(element,category,ids,generateTotalCols,reportMetadata);
				
				if(splitted!=null) {
					dest.addColumn(splitted);
					splitted.setContentCategory(category);
				}
				else dest.addColumn(element);
			}
    		return dest;
    	}
    }
    
    /**
     * Helper method that only uses one category to create a categorized tree. This method is internally used and should not
     * be invoked by the developer directly.
     * @param src the CellColumn source
     * @param category category the category to categorize the data with 
     * @param generateTotalCols true when creating TotalAmountColumnS instead of CellColumnS
     * @return a GroupColumn that holds the categorized Data
     */
    private static Column verticalSplitByCateg(CellColumn src,
            String category, Set ids, boolean generateTotalCols,AmpReports reportMetadata) {
        Column ret = new GroupColumn(src);
        Set<MetaInfo> metaSet = new TreeSet<MetaInfo>();
        Iterator i = src.iterator();
        while (i.hasNext()) {
            Categorizable element = (Categorizable) i.next();
            if(!element.isShow()) continue;
            MetaInfo minfo=MetaInfo.getMetaInfo(element.getMetaData(),category);
            if(minfo==null || minfo.getValue()==null) return null;
            	//if the year is not renderizable just not add it to minfo
        	if (element.isRenderizable()) {
        	    metaSet.add(minfo);
        	}
        }
        
        //TODO: ugly stuff... i have no choice
        //manually add all quarters
       if(category.equals(ArConstants.QUARTER)) {
        	metaSet.add(new MetaInfo<String>(ArConstants.QUARTER,"Q1"));
        	metaSet.add(new MetaInfo<String>(ArConstants.QUARTER,"Q2"));
        	metaSet.add(new MetaInfo<String>(ArConstants.QUARTER,"Q3"));
        	metaSet.add(new MetaInfo<String>(ArConstants.QUARTER,"Q4"));
        }
   
       //manually add at least one term :(
       
       if(category.equals(ArConstants.TERMS_OF_ASSISTANCE) && ARUtil.containsMeasure(ArConstants.UNDISBURSED_BALANCE,reportMetadata.getMeasures())) {
    	   //Commented for Bolivia
	   if (metaSet.size()==0)
	   metaSet.add(new MetaInfo<String>(ArConstants.TERMS_OF_ASSISTANCE,"Grant"));
    	//   metaSet.add(new MetaInfo(ArConstants.TERMS_OF_ASSISTANCE,"Loan"));
    	//metaSet.add(new MetaInfo(ArConstants.TERMS_OF_ASSISTANCE,"In Kind"));
       }
       
       //manually add measures selected
       if(category.equals(ArConstants.FUNDING_TYPE)) {
    	   metaSet.clear();
    	   Set<AmpReportMeasures> measures=reportMetadata.getMeasures();
    	   Iterator<AmpReportMeasures> ii = measures.iterator();
    	   while (ii.hasNext()) {
	    		AmpReportMeasures ampReportMeasurement = ii.next();
				AmpMeasures element = ampReportMeasurement.getMeasure();
				if (element.getMeasureName().equals(
						ArConstants.UNDISBURSED_BALANCE)
						|| element.getMeasureName().equals(ArConstants.TOTAL_COMMITMENTS) 
							|| element.getMeasureName().equals(ArConstants.UNCOMMITTED_BALANCE)
					) continue;
				
				MetaInfo<FundingTypeSortedString> metaInfo = new MetaInfo<FundingTypeSortedString>(
						ArConstants.FUNDING_TYPE, new FundingTypeSortedString(
						element.getMeasureName(), reportMetadata.getMeasureOrder(element.getMeasureName())));
				metaSet.add(metaInfo);
    	   }
    	   /*
    	    * if there isn't a measure selected and TOTAL_COMMITMENTS isn't selected.
    	    * We add at least one measure.
    	    */    	   
    	   if(metaSet.isEmpty() && !ARUtil.containsMeasure(ArConstants.TOTAL_COMMITMENTS,reportMetadata.getMeasures())){
				MetaInfo<FundingTypeSortedString> metaInfo = new MetaInfo<FundingTypeSortedString>(ArConstants.FUNDING_TYPE,new FundingTypeSortedString(ArConstants.PLANNED + " " + ArConstants.COMMITMENT, 0));
				metaSet.add(metaInfo);    		   
    	   }
       }
        
        

        // iterate the set and create a subColumn for each of the metainfo
        i = metaSet.iterator();
        while (i.hasNext()) {
            MetaInfo element = (MetaInfo) i.next();
            CellColumn cc = null;
            if (generateTotalCols)
                cc = new TotalAmountColumn(element.getValue().toString(),true);
            else
                cc = new AmountCellColumn( element.getValue().toString());
            ret.getItems().add(cc);
            cc.setParent(ret);
            
            cc.setContentCategory(category);
            //iterate the src column and add the items with same MetaInfo
            Iterator ii=src.iterator();
            while (ii.hasNext()) {
    			Categorizable item = (Categorizable) ii.next();
    			if(item.hasMetaInfo(element)) cc.addCell(item);
    		}
        }
        
        
        // Start AMP-2724
        if(category.equals(ArConstants.FUNDING_TYPE)) {
			if (ARUtil.containsMeasure(ArConstants.TOTAL_COMMITMENTS,reportMetadata.getMeasures())) {
	
				TotalCommitmentsAmountColumn tac = new TotalCommitmentsAmountColumn(
						ArConstants.TOTAL_COMMITMENTS);
				
	            List theItems = ret.getItems();
	            
	            int index = reportMetadata.getMeasureOrder(ArConstants.TOTAL_COMMITMENTS) - 1;
	            
	            theItems.add(index <  0  || index > theItems.size() ?  theItems.size() : index, tac);
	            
	            tac.setParent(ret);
	            
	            tac.setContentCategory(category);
	            
				Iterator it = src.iterator();
				while (it.hasNext()) {
					AmountCell element = (AmountCell) it.next();
					tac.addCell(element);
				}
	
			}  
        }
        // End AMP-2724
        
        
        
        if(ret.getItems().size()==0) {
        	AmountCellColumn acc=new AmountCellColumn(ret);
        	Iterator ii=src.iterator();
        	while (ii.hasNext()) {
				AmountCell element = (AmountCell) ii.next();
				acc.addCell(element);
			}
        	
        	//fixed problem when there is only  TOTAL_COMMITMENTS  or UNDISBURSED_BALANCE selected
        	// ret=acc;
        	 ret.getItems().add(acc);
        	 acc.setParent(ret);
        }
        
        return ret;
        
       
    }

    /**
     * Constructs a GroupColumn from another Column. The resulting GroupColumn will inherit the parent and the name of its 
     * predecessor.
     * @param src the predecessor Column
     */
    public GroupColumn(Column src) {
       	super(src.getParent(),src.getName());
        this.setContentCategory(src.getContentCategory());
    }
    
    /**
     * Constructs a GroupColumn using a given name
     * @param name the name of the new GroupColumn
     */
    public GroupColumn(String name) {
        super(name);
        // TODO Auto-generated constructor stub
    }

    public GroupColumn() {
        super();
        // TODO Auto-generated constructor stub
    }

    
    /**
     * Adds a Column to this GroupColumn. The Parent property of the added Column will be set to this GroupColumn.
     * If the column already exists then the items are appended to the column end so we'll end up with one 
     * unique column per group
     * @param c
     */
    public void addColumn(Column c) {
        if(!items.contains(c)) {items.add(c);        
        c.setParent(this);
        } else {
            Column older=(Column) items.get(items.indexOf(c));
            older.getItems().addAll(c.getItems());
        }
    }
    
    public void addColumn(Integer idx,Column c){
    	if(items.size()<idx.intValue()|| idx.intValue()==0) addColumn(c);else {
    	if(idx.intValue()+1>items.size()) items.add(idx.intValue()-1,c);else
    			items.add(idx.intValue(),c);
    	c.setParent(this);
    	}
    }

    /**
     * Returns a specific column based on its ColumnId. The ColumnId is an arbitrary object that uniquely identifies this
     * column.  
     * @param columnId the columnId to be used to track the Column
     * @return the requested Column or null if not found
     * @see Column
     */
    public Column getColumn(Object columnId) {
        Iterator i = items.iterator();
        while (i.hasNext()) {
            Column element = (Column) i.next();
            if (element.getColumnId().equals(columnId))
                return element;
        }
        return null;
    }

    /**
     * Returns the Column occupying the specific position in the internal list of this GroupColumn 
     * @param idx the index of the requested column
     * @return the requested column
     */
    public Column getColumn(int idx) {
        return (Column) getItem(idx);
    }

    /**
     * Replaces the column identified by columnId with the new specified Column
     * @param columnId the identifier for the column to be replaced
     * @param newColumn the new column
     */
    public void replaceColumn(Object columnId, Column newColumn) {
        int idx = items.indexOf(getColumn(columnId));
        items.remove(idx);
        items.add(idx, newColumn);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.dgfoundation.amp.ar.Column#filterCopy(org.dgfoundation.amp.ar.cell.Cell)
     */
    public Column filterCopy(Cell filter, Set ids) {
        GroupColumn dest = new GroupColumn(this.getName());
        Iterator i = items.iterator();
        while (i.hasNext()) {
            Column element = (Column) i.next();
            dest.addColumn(element.filterCopy(filter, ids));
        }
        return dest;

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.dgfoundation.amp.ar.Column#process()
     */
    public Column postProcess() {
        GroupColumn dest = new GroupColumn(this);
        Iterator i = this.iterator();
        while (i.hasNext()) {
            Column element = (Column) i.next();
            dest.addColumn(element.postProcess());

        }
        return dest;
    }

    
    public List getHorizColumnList() {
    	ArrayList ret=new ArrayList();
    	for(int i=0;i<getColumnSpan();i++) {
    		ret.add(getSubColumns(i));
    	}
    	return ret;
    }
    
	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.ar.Column#getSubColumn(int)
	 */
	public List getSubColumns(int depth) {
		ArrayList ret=new ArrayList();
		if(getColumnSpan()<depth) return ret;
		if(depth==0) {
			ret.add(this);
			return ret;
		} else
		if(depth==1) {
			ret.addAll(items);
		} else {
			Iterator i=items.iterator();
			while (i.hasNext()) {
				Column element = (Column) i.next();
				ret.addAll(element.getSubColumns(depth-1));
			}
		}
		return ret;
	}

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.ar.Column#getColumnDepth()
	 */
	public int getColumnSpan() {
		Column c=this.getColumn(0);
		if(c==null) return 0; else;
		return 1+c.getColumnSpan();
	}

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.ar.Column#getCurrentRowSpan()
	 */
	public int getCurrentRowSpan() {
		rowSpan--;
		spanCount++;
		if(spanCount==this.getColumnSpan()+1) return rowSpan+1; else return 1;
	}

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.ar.Column#getOwnerIds()
	 */
	public Set getOwnerIds() {
		TreeSet ret=new TreeSet();
		Iterator i=items.iterator();
		while (i.hasNext()) {
			Column element = (Column) i.next();
			ret.addAll(element.getOwnerIds());
		}
		return ret;
	}

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.ar.Column#getTrailCell()
	 */
	public Cell getTrailCell() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.ar.Column#getTrailCells()
	 */
	public List getTrailCells() {
		ArrayList ret=new ArrayList();
		Iterator i=items.iterator();
		while (i.hasNext()) {
			Column element = (Column) i.next();
			ret.addAll(element.getTrailCells());
		}
		return ret;
	}

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.ar.Column#newInstance()
	 */
	public Column newInstance() {
		return new GroupColumn(this);
	}

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.ar.Column#getColumnDepth()
	 */
	public int getColumnDepth() {
		int ret=0;
		Iterator i=items.iterator();
		while (i.hasNext()) {
			Column element = (Column) i.next();
			ret+=element.getColumnDepth();			
		}
		return ret;
	}

	
	public int getRowSpan() {
		return 1+items.size();
	}

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.ar.Column#cellCount()
	 */
	public int getCellCount() {
		int count=0;
		Iterator i=items.iterator();
		while (i.hasNext()) {
			Column element = (Column) i.next();
			count+=element.getCellCount();
		}
		return count;
	}

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.ar.Column#getVisibleCellCount()
	 */
	public int getVisibleCellCount(Long ownerId) {
		int count=0;
		Iterator i=items.iterator();
		while (i.hasNext()) {
			Column element = (Column) i.next();
			count+=element.getVisibleCellCount(ownerId);
		}
		return count;
	}

	
	public boolean hasTrailCells() {
		Iterator i=items.iterator();
		while (i.hasNext()) {
			Column element = (Column) i.next();
			if(element.hasTrailCells()) return true;
		}
		return false;
	}
	
}
