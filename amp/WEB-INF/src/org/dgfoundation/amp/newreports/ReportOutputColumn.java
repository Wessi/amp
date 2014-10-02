package org.dgfoundation.amp.newreports;

import org.digijava.kernel.translator.TranslatorWorker;

/**
 * class holding metadata about a report-output-column. It also holds  A "column" in this context might be either a Measure or a Column - anything which sits on the X axis, actually
 * @author Dolghier Constantin
 *
 */
public class ReportOutputColumn implements Comparable<ReportOutputColumn> {
	
	/**
	 * the <strong>localized</strong> name of the column
	 */
	public final String columnName;
	
	/**
	 * the parent column, if any. Might be null for top-level columns, like "Funding" or "Project Title"
	 */
	public final ReportOutputColumn parentColumn;
	
	
	/**
	 * the <strong> unlocalized</strong> name of the column
	 */
	public final String originalColumnName;
	
	//private List<ReportOutputColumn> children;
	
	public ReportOutputColumn(String columnName, ReportOutputColumn parentColumn, String originalColumnName) {
		this.columnName = columnName;
		if (columnName == null || columnName.isEmpty())
			throw new NullPointerException();
		this.parentColumn = parentColumn;
		this.originalColumnName = originalColumnName;
	}
	
	public static ReportOutputColumn buildTranslated(String originalColumnName, String locale, ReportOutputColumn parentColumn){
		return new ReportOutputColumn(TranslatorWorker.translateText(originalColumnName, locale, 3l), parentColumn, originalColumnName);
	}
	/**
	 * computes the full name of the column like, for example, [Funding][2007][Actual Commitments]
	 * <strong>unlocalized</strong>
	 * @return
	 */
	public String getHierarchicalName() {
		String res = String.format("[%s]", this.columnName);
		if (parentColumn != null)
			res = parentColumn.getHierarchicalName() + res;
		return res;
	}
	
	@Override
	public String toString() {
		return this.getHierarchicalName();
	}
	
	@Override
	public int hashCode() {
		return getHierarchicalName().hashCode();
	}
	
	@Override public boolean equals(Object oth) {
		return this.getHierarchicalName().equals(((ReportOutputColumn) oth).getHierarchicalName());
	}
	
	@Override public int compareTo(ReportOutputColumn oth) {
		return this.getHierarchicalName().compareTo(oth.getHierarchicalName());
	}
	
	/**
	 * @return {@link ReportOutputColumn} list of direct children 
	 */
	/*
	public List<ReportOutputColumn> getChildren() {
		return children;
	}
	
	public void addChild(ReportOutputColumn child) {
		if (children == null) {
			children = new ArrayList<ReportOutputColumn>();
		}
		children.add(child);
	}
	*/

}
