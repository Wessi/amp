package org.digijava.module.aim.helper;

import java.util.Collection;

public class ReportSelectionCriteria {

		  private Collection columns;
		  private Collection measures;
		  private Collection hierarchy;


		  public void setColumns(Collection c) {
					 columns = c;
		  }

		  public void setMeasures(Collection c) {
					 measures = c;
		  }

		  public void setHierarchy(Collection c) {
					 hierarchy = c;
		  }

		  
		  public Collection getColumns() { return columns; }

		  public Collection getMeasures() { return measures; }

		  public Collection getHierarchy() { return hierarchy; }

		  
}
