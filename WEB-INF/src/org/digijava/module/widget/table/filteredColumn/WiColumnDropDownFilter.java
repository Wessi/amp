package org.digijava.module.widget.table.filteredColumn;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.hibernate.Session;

import org.digijava.kernel.exception.DgException;
import org.digijava.module.widget.dbentity.AmpDaColumnFilter;
import org.digijava.module.widget.dbentity.AmpDaValue;
import org.digijava.module.widget.dbentity.AmpDaValueFiltered;
import org.digijava.module.widget.table.WiCell;
import org.digijava.module.widget.table.WiColumn;
import org.digijava.module.widget.table.util.TableWidgetUtil;

/**
 * Drop down filter column.
 * @author Irakli Kobiashvili
 *
 */
public class WiColumnDropDownFilter extends WiColumn {
	private FilterItemProvider provider;
	private Long activeItemId = new Long(0);
	private Map<Long, WiColumnFilterSubColumn> columns = new HashMap<Long, WiColumnFilterSubColumn>();

	public WiColumnDropDownFilter(AmpDaColumnFilter dbColumn) {
		super(dbColumn);
		//get provider of the drop down filter items. current donors.
		provider = TableWidgetUtil.getFilterItemProvider(dbColumn);
		//create map from filter items.
		Map<Long, WiColumnFilterSubColumn> columnMap = createColumnsFromProvider(provider, this);
		//setup currently selected item in drop down
		if (columnMap.values().size()>0){
			this.setActiveItemId(new Long(columnMap.values().iterator().next().getFilterItemId()));
		}
		//store map in this specialized filter column.
		this.columns = columnMap;
		//setup cells from db values
		Set<AmpDaValue> values = dbColumn.getValues();
		for (AmpDaValue value : values) {
			AmpDaValueFiltered valueFiltered = (AmpDaValueFiltered)value;
			//create new filter cell for each value
			WiCellFiltered cell = (WiCellFiltered)TableWidgetUtil.newCell(value);
			//search for values corresponding sub column created from filter items. 
			WiColumnFilterSubColumn col = columns.get(cell.getFilterItemId());
			//if there is no such column create new one.
			if (col == null){
				col = new WiColumnFilterSubColumn();
				FilterItem item = provider.getItem(valueFiltered.getFilterItemId());
				col.setId(dbColumn.getId());
				col.setFilterItemId(valueFiltered.getFilterItemId());//this column is for this filter item
				col.setName(item.getName());
				col.setCssClass(dbColumn.getCssClass());
				col.setOrderId(dbColumn.getOrderNo());
				col.setPattern(dbColumn.getPattern());
				columns.put(col.getFilterItemId(), col);
			}
			//set cell to the sub column
			col.setCell(cell);
			cell.setColumn(col);
		}
	}

	private Map<Long,WiColumnFilterSubColumn> createColumnsFromProvider(FilterItemProvider provider,WiColumn masterColumn){
		List<FilterItem> items = provider.getItems();
		Map<Long, WiColumnFilterSubColumn> columns = new HashMap<Long,WiColumnFilterSubColumn>(items.size());
		for (FilterItem item : items) {
			WiColumnFilterSubColumn column = new WiColumnFilterSubColumn();
			column.setId(masterColumn.getId());
			column.setFilterItemId(item.getId());
			column.setName(item.getName());
			column.setOrderId(masterColumn.getOrderId());
			column.setCssClass(masterColumn.getCssClass());
			column.setPattern(masterColumn.getPattern());
			columns.put(column.getFilterItemId(),column);
		}
		return columns;
	}
	
	@Override
	public WiCell getCell(Long rowPk) {
		return getActiveSubColumn().getCell(rowPk);
	}

	@Override
	public void setCell(WiCell cell) {
		getActiveSubColumn().setCell(cell);
	}

	@Override
	public int getType() {
		return WiColumn.FILTER;
	}

	@Override
	public void saveData(Session dbSession) throws DgException {
		Collection<WiColumnFilterSubColumn> subColumns = this.columns.values();
		for (WiColumnFilterSubColumn subColumn : subColumns) {
			subColumn.saveData(dbSession);
		}
	}
	
	public void setProvider(FilterItemProvider provider) {
		this.provider = provider;
	}

	public FilterItemProvider getProvider() {
		return provider;
	}
	
	public void setFilterOn(Long filterItemId){
		this.setActiveItemId(filterItemId);
	}

	public WiColumnFilterSubColumn getActiveSubColumn(){
		return columns.get(getActiveItemId());
	}

	public void setActiveItemId(Long activeItemId) {
		this.activeItemId = activeItemId;
	}

	public Long getActiveItemId() {
		return activeItemId;
	}
	
	
}
