package org.dgfoundation.amp.nireports.behaviours;

import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.ImmutablePair;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.output.NiAmountCell;
import org.dgfoundation.amp.nireports.output.NiOutCell;
import org.dgfoundation.amp.nireports.output.NiTextCell;
import org.dgfoundation.amp.nireports.runtime.ColumnContents;
import org.dgfoundation.amp.nireports.schema.Behaviour;
import org.dgfoundation.amp.nireports.schema.NiDimension;
import org.dgfoundation.amp.nireports.schema.NiReportedEntity;
import org.dgfoundation.amp.nireports.schema.TimeRange;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;

/**
 * the abstract behaviour of a computed measure/column. Defines all the common behaviour so that subclasses can focus on implementing the business logic
 * @author Dolghier Constantin
 *
 */
public abstract class AbstractComputedBehaviour implements Behaviour<NiAmountCell> {
	
	protected final TimeRange timeRange;
	
	protected AbstractComputedBehaviour(TimeRange timeRange) {
		this.timeRange = timeRange;
	}
	
	@Override
	public TimeRange getTimeRange() {
		return timeRange;
	}
	
	@Override
	public Cell buildUnallocatedCell(long mainId, long entityId, LevelColumn levelColumn) {
		throw new RuntimeException("doing hierarchies by measures not supported");
	}

	@Override
	public NiAmountCell getZeroCell() {
		return NiAmountCell.ZERO;
	}

	@Override
	public NiOutCell getEmptyCell(ReportSpecification spec) {
		return NiTextCell.EMPTY;
	}

	@Override
	public boolean isKeepingSubreports() {
		return true;
	}

	@Override
	public boolean hasPercentages() {
		return false;
	}
	
	@Override
	public ImmutablePair<String, ColumnContents> getTotalCells(NiReportsEngine context, NiReportedEntity<?> entity, ColumnContents fetchedContents) {
		// trivial measures are copied verbatim to totals
		return new ImmutablePair<String, ColumnContents>(entity.name, fetchedContents);
	}
}
