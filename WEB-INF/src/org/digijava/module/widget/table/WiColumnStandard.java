package org.digijava.module.widget.table;

import java.util.List;
import java.util.Set;

import org.digijava.kernel.exception.DgException;
import org.digijava.module.widget.dbentity.AmpDaColumn;
import org.digijava.module.widget.dbentity.AmpDaValue;
import org.digijava.module.widget.table.util.TableWidgetUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;

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
            List<WiCell> trashedCells = this.getAllTrashedCells();
            for (WiCell cell : myCells) {
                cell.saveData(dbSession, dbColumn);
            }
            for (WiCell trashedCell : trashedCells) {
            	if (trashedCell.getId() !=null  && trashedCell.getId()>0){
                    trashedCell.removeData(dbSession, dbColumn);
            	}
            }
            this.clearTrashedCells();
        } catch (HibernateException e) {
			throw new DgException("cannot save column, ID="+getId(),e);
        }
    }
     
	@Override
	public WiCell createCell() {
		WiCellStandard cell = new WiCellStandard();
		cell.setColumn(this);
		return cell;
	}

	@Override
	public WiCell createCell(AmpDaValue value) {
		WiCellStandard cell = (WiCellStandard)this.createCell();
		cell.setId(value.getId());
		cell.setPk(value.getPk());
		cell.setValue(value.getValue());
		return cell;
	}


}
