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

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPopupMenu;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

class ResizablePopupMenu extends JPopupMenu implements PopupMenuListener {

	private static final long serialVersionUID = 1L;

	private static final int DOT_SIZE = 2;
	private static final int DOT_START = 2;
	private static final int DOT_STEP = 4;

	private final boolean resizable;

	public ResizablePopupMenu( boolean resizable ) {
		super();
		this.resizable = resizable;
		if ( resizable ) PopupMenuResizer.decorate(this);
		addPopupMenuListener(this);
	}

	@Override
	public void popupMenuWillBecomeVisible(PopupMenuEvent e) {}

	@Override
	public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {}

	@Override
	public  void popupMenuCanceled(PopupMenuEvent e) {}

	@Override
	public void paintChildren(Graphics g) {
		super.paintChildren(g);
		if ( resizable ) drawResizer(g);
	}

	private void drawResizer(Graphics g) {

		int x = getWidth()-2;
		int y = getHeight()-2;

		Graphics g2 = g.create();
		
		try {
			for ( int dy = DOT_START, j = 2; j > 0; j--, dy += DOT_STEP ) {
				for( int dx = DOT_START, i = 0; i < j; i++, dx += DOT_STEP ) {
					drawDot( g2, x-dx, y-dy );
				}
			}
		} finally {
			g2.dispose();
		}

	};

	private void drawDot( Graphics g, int x, int y) {
		g.setColor(Color.WHITE);
		g.fillRect( x, y, DOT_SIZE, DOT_SIZE);
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect( x-1, y-1, DOT_SIZE, DOT_SIZE);
	}

}