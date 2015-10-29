package org.dgfoundation.amp.nireports;

import java.util.List;

/**
 * 
 * @author Dolghier Constantin
 *
 */
public abstract class NiReportColumn {
	
	public final String name;
	public final NiDimension.LevelColumn levelColumn;
	
	protected NiReportColumn(String name, NiDimension.LevelColumn levelColumn) {
		this.name = name;
		this.levelColumn = levelColumn;
	}
	
	public abstract List<CellColumn> fetchColumn(NiFilters filters);
	
	@Override public int hashCode() {
		return name.hashCode();
	}
	
	@Override public boolean equals(Object oth) {
		NiReportColumn o = (NiReportColumn) oth;
		return this.name.equals(o.name);
	}
	
	@Override public String toString() {
		return String.format("coldef: <%s>", name);
	}
}
