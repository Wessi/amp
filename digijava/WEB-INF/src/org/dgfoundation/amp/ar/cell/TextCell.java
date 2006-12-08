/**
 * TextCell.java
 * (c) 2005 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.cell;

import org.dgfoundation.amp.ar.workers.TextColWorker;

/**
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since May 29, 2006
 * regular cell holding a string
 **/
public class TextCell extends Cell {
	
	public static final int shortLength=160;
	
	protected String value;
	
	public TextCell() {
		super();
		value="";		
	}
	
	public Class getWorker() {
		return TextColWorker.class;
	}
	
	public TextCell(Long id) {
		super(id);
		value="";
	}


	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.ar.cell.Cell#getValue()
	 */
	public Object getValue() {
		return value;
	}

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.ar.cell.Cell#setValue(java.lang.Object)
	 */
	public void setValue(Object value) {
		this.value=(String) value;
		
	}

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.ar.cell.Cell#add(org.dgfoundation.amp.ar.cell.Cell)
	 */
	public Cell merge(Cell c) {
		TextCell ret=new TextCell();		
		ret.setValue((value+(String)c.getValue()).trim());
		return ret;
		
	}

	public Cell newInstance() {
		return new TextCell();
	}
	
	public int getTextLength() {
		if(value==null) return 0;
		return value.length();
	}
	
	public String getShortTextVersion() {
		 if(!getHasLongVersion()) return value;
		return value.substring(0,shortLength-1);
	}
	
	public boolean getHasLongVersion() {
		if(getTextLength()>shortLength) return true;
		return false;
	}
	
}
