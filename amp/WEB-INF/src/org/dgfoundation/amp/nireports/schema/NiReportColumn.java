package org.dgfoundation.amp.nireports.schema;

import java.util.Optional;

import org.dgfoundation.amp.nireports.Cell;

/**
 * a NiReports definition of a column 
 * <ol>
 * 	<li>name</li>
 * 	<li>fetching</li> 
 * 	<li>use as a hierarchy</li> 
 * 	<li>behaviour under aggregation, filters, etc</li>s
 * </ol> 
 * @author Dolghier Constantin
 *
 */
public abstract class NiReportColumn<K extends Cell> extends NiReportedEntity<K> {
	
	public final Optional<NiDimension.LevelColumn> levelColumn;
	protected boolean transactionLevelHierarchy = false;
		
	protected NiReportColumn(String name, NiDimension.LevelColumn levelColumn, Behaviour<?> behaviour, String description) {
		super(name, behaviour, description);
		this.levelColumn = Optional.ofNullable(levelColumn);
	}
		
	/** 
	 * @return whether hierarchies by these columns should filter at a transaction level
	 */
	public boolean isTransactionLevelHierarchy() {
		return this.transactionLevelHierarchy;
	}
	
	public NiReportColumn<K> setTransactionLevelHierarchy() {
		this.transactionLevelHierarchy = true;
		return this;
	}
	
	@Override public String toString() {
		return String.format("coldef: <%s>", name);
	}
}
