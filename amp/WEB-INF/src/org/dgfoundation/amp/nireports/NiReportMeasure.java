package org.dgfoundation.amp.nireports;

import java.util.List;
import java.util.Set;


/**
 * 
 * @author Dolghier Constantin
 *
 */
public abstract class NiReportMeasure {
		
	public final String name;
	
	protected NiReportMeasure(String name) {
		this.name = name;
	}
	
	/**
	 * selects the cells which are relevant for this measure from a combination of columns / filter data (usually just a selection of the funding column)
	 * @param source
	 * @return
	 */
	public abstract List<CategAmountCell> buildCells(NiReportContext context);
	
	/**
	 * returns the list of measures which should be fetched before this measure's {@link #buildCells(NiReportContext)} function will be called
	 * @return
	 */
	public abstract Set<String> getPrecursorMeasures();
	
	@Override public int hashCode() {
		return name.hashCode();
	}
	
	@Override public boolean equals(Object oth) {
		NiReportMeasure o = (NiReportMeasure) oth;
		return this.name.equals(o.name);
	}
	
	@Override public String toString() {
		return String.format("measdef: <%s>", name);
	}
}
