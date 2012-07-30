package org.oxbow.swingbits.table.filter;

import org.oxbow.swingbits.list.ICheckListAction;
import org.oxbow.swingbits.list.ICheckListModel;

public abstract class AbstractTableFilterAction implements ICheckListAction<DistinctColumnItem> {

	private String name;

	public AbstractTableFilterAction( String name ) {
		this.name = String.format("(%s)", name);
	}
	
	@Override 
	public final String toString() {
		return name;
	}
	
	
	@Override
	public final void check(ICheckListModel<DistinctColumnItem> model, boolean value) {
	}

}
