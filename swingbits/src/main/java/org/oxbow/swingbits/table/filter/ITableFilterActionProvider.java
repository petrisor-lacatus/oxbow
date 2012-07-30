package org.oxbow.swingbits.table.filter;

import java.util.Collection;

public interface ITableFilterActionProvider {

	Collection<? extends AbstractTableFilterAction> getActions( int column );
	
}
