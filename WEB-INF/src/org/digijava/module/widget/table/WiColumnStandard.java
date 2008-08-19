package org.digijava.module.widget.table;

import java.util.List;
import java.util.Set;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import org.digijava.kernel.exception.DgException;
import org.digijava.module.widget.dbentity.AmpDaColumn;
import org.digijava.module.widget.dbentity.AmpDaValue;
import org.digijava.module.widget.table.util.TableWidgetUtil;

public class WiColumnStandard extends WiColumn {

	public WiColumnStandard(){
		
	}
	
	public WiColumnStandard(AmpDaColumn dbColumn) {
		super(dbColumn);
		Set<AmpDaValue> values = dbColumn.getValues();
		if (values!=null){
			for (AmpDaValue value : values) {
				WiCell cell = TableWidgetUtil.newCell(value);
				cell.setColumn(this);
				setCell(cell);
			}
		}
	}

	@Override
	public void saveData(Session dbSession) throws DgException {
		try {
			AmpDaColumn dbColumn = (AmpDaColumn) dbSession.load(AmpDaColumn.class, this.getId());
			List<WiCell> myCells = this.getAllCells();
			for (WiCell cell : myCells) {
				cell.saveData(dbSession, dbColumn);
			}
		} catch (HibernateException e) {
			throw new DgException("cannot save column, ID="+getId(),e);
		}
	}


}
