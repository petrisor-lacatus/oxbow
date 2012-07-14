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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.event.PopupMenuEvent;

public abstract class PopupWindow {

	private final JPopupMenu menu;
	private Dimension defaultSize = new Dimension(100,100);

	public PopupWindow( boolean resizable ) {
		menu = new ResizablePopupMenu( resizable ) {

			private static final long serialVersionUID = 1L;

			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
				if ( menu.getComponentCount() == 0 ) {
					JComponent content = buildContent();
					defaultSize = content.getPreferredSize();
					
					menu.add( content );

				}
				beforeShow();
			}

			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
				beforeHide();
			}

		};
	}

	public final Dimension getDefaultSize() {
		return defaultSize;
	}

	public final Dimension getPreferredSize() {
		return menu.getPreferredSize();
	}

	public final void setPreferredSize( Dimension preferredSize ) {
		menu.setPreferredSize(preferredSize);
	}

	/**
	 * Override this method to add content yo the owner.
	 * This method is only executed when owner has no subcomponents
	 * @param owner
	 */
	protected abstract JComponent buildContent();

	/**
	 * Shows Popup in predefined location
	 * @param invoker
	 * @param x
	 * @param y
	 */
	public void show( Component invoker, int x, int y ) {
		menu.show( invoker, x, y );
	}

	/**
	 * Shows popup in predefined location
	 * @param invoker
	 * @param location
	 */
	public void show( Component invoker, Point location ) {
		show( invoker, location.x, location.y );
	}

	/**
	 * Hides popup
	 */
	public final void hide() {
		menu.setVisible(false);
	}

	protected void beforeShow() {}

	protected void beforeHide() {}
	
	
	/**
	 * Simple action to for the popup window.
	 * To use - override perform method. 
	 * 
	 * Created on Feb 4, 2011
	 * @author Eugene Ryzhikov
	 *
	 */
	public class CommandAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public CommandAction(String name, Icon icon) {
			super(name, icon);
			
			if ( icon != null ) {
				putValue(Action.SHORT_DESCRIPTION, name);
				putValue(Action.NAME, null);
			}
			
		}

		public CommandAction( String name ) {
			super(name);
		}

		@Override
		public final void actionPerformed(ActionEvent e) {
			if ( perform() ) hide();
		}

		/**
		 * Preforms action
		 * @return true if popup should be closed
		 */
		protected boolean perform(){
			return true;
		}
	}

}
