/* ====================================================================
 * Copyright (c) 1998 - 2003 David F. Glasser.  All rights
 * reserved.
 *
 * This file is part of the QueryForm Database Tool.
 *
 * The QueryForm Database Tool is free software; you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * The QueryForm Database Tool is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with the QueryForm Database Tool; if not, write to:
 *
 *      The Free Software Foundation, Inc.,
 *      59 Temple Place, Suite 330
 *      Boston, MA  02111-1307  USA
 *
 * or visit http://www.gnu.org.
 *
 * ====================================================================
 *
 * This product includes software developed by the
 * Apache Software Foundation (http://www.apache.org/).
 *
 * ==================================================================== 
 */
package org.glasser.qform;

import java.awt.Component;
import java.awt.Font;
import java.util.HashMap;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import org.glasser.sql.TableInfo;

public class TableInfoListCellRenderer<V> extends DefaultListCellRenderer {

	HashMap<Font, Font> italicFontMap = new HashMap<>();

	public Component getListCellRendererComponent(JList<?> list, TableInfo value, int index, boolean isSelected,
			boolean cellHasFocus) {

		JLabel component = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

		TableInfo ti = value;
		if ("VIEW".equals(ti.getTableType())) {
			Font font = component.getFont();
			Font viewFont = italicFontMap.get(font);
			if (viewFont == null) {
				viewFont = font.deriveFont(Font.ITALIC);
				italicFontMap.put(font, viewFont);
			}
			component.setFont(viewFont);
		}

		return component;

	}

}
