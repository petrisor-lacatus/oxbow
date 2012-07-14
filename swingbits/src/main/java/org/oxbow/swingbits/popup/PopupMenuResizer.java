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

package org.oxbow.swingbits.popup;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

/**
 * Allows to resize popup with the mouse.
 *
 * Created on Aug 6, 2010
 * @author exr0bs5
 *
 */
final class PopupMenuResizer extends MouseAdapter {

	private final JPopupMenu menu;

	private static final int REZSIZE_SPOT_SIZE = 10;

	private Point mouseStart = new Point( Integer.MIN_VALUE, Integer.MIN_VALUE );

	private Dimension startSize;

	private boolean isResizing = false;


	public static void decorate( JPopupMenu menu ) {
		new PopupMenuResizer( menu );
	}

	private PopupMenuResizer( JPopupMenu menu ) {
		this.menu = menu;
		this.menu.setLightWeightPopupEnabled(true);
		menu.addMouseListener(this);
		menu.addMouseMotionListener(this);
	}

	private boolean isInResizeSpot( Point point ) {

		if ( point == null ) return false;

		Rectangle resizeSpot = new Rectangle(
			menu.getWidth()-REZSIZE_SPOT_SIZE,
			menu.getHeight()-REZSIZE_SPOT_SIZE,
			REZSIZE_SPOT_SIZE,
			REZSIZE_SPOT_SIZE );

		return resizeSpot.contains(point);

	}

	@Override
	public void mouseMoved(MouseEvent e) {

		menu.setCursor(
		   Cursor.getPredefinedCursor(
			  isInResizeSpot( e.getPoint() )? Cursor.SE_RESIZE_CURSOR: Cursor.DEFAULT_CURSOR ));
	}

	private Point toScreen( MouseEvent e ) {
		
		Point p = e.getPoint();
		SwingUtilities.convertPointToScreen(p, e.getComponent());
		return p;
		
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		mouseStart = toScreen(e);
		startSize = menu.getSize();
		isResizing = isInResizeSpot(e.getPoint());
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mouseStart = new Point( Integer.MIN_VALUE, Integer.MIN_VALUE );
		isResizing = false;
	}

	@Override
	public void mouseDragged(MouseEvent e) {

		if ( !isResizing ) return;

		Point p = toScreen(e);
		
		int dx = p.x - mouseStart.x;
		int dy = p.y - mouseStart.y;

		
		Dimension minDim = menu.getMinimumSize();
//		Dimension maxDim = menu.getMaximumSize();
		Dimension newDim = new Dimension(startSize.width + dx, startSize.height + dy);

		if ( newDim.width >= minDim.width && newDim.height >= minDim.height /*&&
		     newDim.width <= maxDim.width && newDim.height <= maxDim.height*/	) {
			menu.setPopupSize( newDim );
		}

	}
}