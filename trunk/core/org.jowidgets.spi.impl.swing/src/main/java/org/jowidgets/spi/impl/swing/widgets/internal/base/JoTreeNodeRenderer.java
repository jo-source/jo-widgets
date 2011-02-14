/*
 * Copyright (c) 2011, grossmann
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 * * Neither the name of the jo-widgets.org nor the
 *   names of its contributors may be used to endorse or promote products
 *   derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL jo-widgets.org BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */

package org.jowidgets.spi.impl.swing.widgets.internal.base;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.jowidgets.spi.impl.swing.image.SwingImageRegistry;
import org.jowidgets.spi.impl.swing.util.ColorConvert;
import org.jowidgets.spi.impl.swing.util.FontProvider;

public class JoTreeNodeRenderer extends DefaultTreeCellRenderer {

	private static final long serialVersionUID = -8485841535553785779L;

	@Override
	public Component getTreeCellRendererComponent(
		final JTree tree,
		final Object value,
		final boolean selected,
		final boolean expanded,
		final boolean leaf,
		final int row,
		final boolean hasFocus) {

		final Component result = super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

		if (value instanceof JoTreeNode) {
			final JoTreeNode joTreeNode = ((JoTreeNode) value);
			setText(joTreeNode.getText());
			setToolTipText(joTreeNode.getToolTipText());
			setIcon(SwingImageRegistry.getInstance().getImageIcon(joTreeNode.getIcon()));

			if (joTreeNode.getMarkup() != null) {
				setFont(FontProvider.deriveFont(getFont(), joTreeNode.getMarkup()));
			}

			if (!selected && joTreeNode.getBackgroundColor() != null) {
				setBackgroundNonSelectionColor(ColorConvert.convert(joTreeNode.getBackgroundColor()));
			}
			else {
				setBackgroundNonSelectionColor(null);
			}

			if (!selected && joTreeNode.getForegroundColor() != null) {
				setForeground(ColorConvert.convert(joTreeNode.getForegroundColor()));
			}
			else {
				setForeground(null);
			}
		}
		return result;
	}

}
