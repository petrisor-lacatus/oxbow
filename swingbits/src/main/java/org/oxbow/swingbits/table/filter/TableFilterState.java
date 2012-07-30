/*
 * Copyright (c) 2009-2011, EzWare
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.Redistributions
 * in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.Neither the name of the
 * EzWare nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior
 * written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 */

package org.oxbow.swingbits.table.filter;

import static org.oxbow.swingbits.util.CollectionUtils.isEmpty;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.oxbow.swingbits.util.SetMap;

class TableFilterState implements Serializable {

	private static final long serialVersionUID = 1L;
	
	// if 'no set' - filter cleared
	// if 'set' - some kind of filtering
	private final SetMap<Integer,DistinctColumnItem> dataMap = 
			           new SetMap<Integer,DistinctColumnItem>();
	
	private final SetMap<Integer,AbstractTableFilterAction> actionMap = 
			           new SetMap<Integer,AbstractTableFilterAction>();
	
	/**
	 * Clears filtering for specific column
	 */
	public void clear( int column ) {
		dataMap.remove(column);
		actionMap.remove(column);
	}
	
	
	/**
	 * Clears all filtering
	 */
	public void clear() {
		dataMap.clear();
		actionMap.clear();
	}


	/**
	 * Resets a collection of filter values for specified column
	 * @param column
	 * @param values
	 */
	public void setValues( int column, Collection<DistinctColumnItem> values ) {
		dataMap.addAll(column, values);
	}
	
	public void setActions( int column, Collection<AbstractTableFilterAction> actions ) {
		actionMap.addAll(column,actions);
	}
	
	
	public Collection<DistinctColumnItem> getValues( int column ) {
		Set<DistinctColumnItem> vals =  dataMap.get(column);
		return vals == null? Collections.<DistinctColumnItem>emptySet(): vals;
	}
	
	
	public Collection<AbstractTableFilterAction> getActions( int column ) {
		Set<AbstractTableFilterAction> actions =  actionMap.get(column);
		return actions == null? Collections.<AbstractTableFilterAction>emptySet(): actions;
	}	
	
	/**
	 * Standard test for row inclusion using current filter values
	 * @param entry
	 * @return true if row has to be included
	 */
	public boolean include( ITableFilter.Row entry ) {
	
		for( int col=0; col< entry.getValueCount(); col++ ) {
			Collection<DistinctColumnItem> values = getValues(col);
			if ( isEmpty(values) ) continue; // no filtering for this column
			if ( !values.contains( new DistinctColumnItem( entry.getValue(col), 0))) return false;
		}
		return true;
		
	}
	
	
}
