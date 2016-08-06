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

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.Action;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import org.glasser.sql.TableInfo;
import org.glasser.swing.ButtonClicker;
import org.glasser.swing.EnterEscapeKeyHandler;
import org.glasser.swing.GUIHelper;
import org.glasser.swing.KeySelectableJList;

public class TableSelector extends JDialog implements ActionListener {

	private static boolean debug = false;

	public class DataSourceListItem implements Comparable<DataSourceListItem> {

		private String displayName = null;

		private Integer sourceId = null;

		public DataSourceListItem(Integer sourceId, String displayName) {
			this.sourceId = sourceId;
			this.displayName = "(" + sourceId + ") " + displayName;
		}

		public String toString() {
			return displayName;
		}

		public Integer getSourceId() {
			return sourceId;
		}

		public boolean equals(DataSourceListItem other) {
			try {
				return other.getSourceId().equals(sourceId);
			} catch (Exception ex) {
				return this == other;
			}
		}

		public int hashCode() {
			return sourceId.intValue();
		}

		public int compareTo(DataSourceListItem other) {
			try {
				return sourceId.compareTo(other.getSourceId());
			} catch (Exception ex) {
				return 0;
			}
		}
	}

	protected JComboBox<DataSourceListItem> sourceList = new JComboBox<>();

	protected Vector<DataSourceListItem> sourceVector = new Vector<>();

	protected DefaultComboBoxModel<DataSourceListItem> sourceModel = new DefaultComboBoxModel<>(sourceVector);

	JComboBox<String> schemaList = new JComboBox<>();

	private ComboBoxModel<String> emptySchemaListModel = schemaList.getModel();

	private Vector<TableInfo> emptyVector = new Vector<>();

	JList<TableInfo> tableList = new KeySelectableJList<>();

	JButton btnOK = new JButton("OK");

	JButton btnCancel = new JButton("Cancel");

	HashMap<Integer, DefaultComboBoxModel<String>> schemaModelMap = new HashMap<>();

	HashMap<Integer, HashMap<String, Vector<TableInfo>>> tableListMap = new HashMap<>();

	private Object[] selections = null;

	private TableInfo selectedTableInfo = null;

	private Integer selectedSourceId = null;

	public void addDataSource(Integer sourceId, String sourceName, HashMap<String, Vector<TableInfo>> tables) {

		DataSourceListItem item = new DataSourceListItem(sourceId, sourceName);
		sourceVector.add(item);
		Collections.sort(sourceVector);
		sourceList.setModel(sourceModel);

		Vector<String> schemas = new Vector<>();
		for (Iterator<String> i = tables.keySet().iterator(); i.hasNext();) {
			schemas.add(i.next());
		}

		Collections.sort(schemas);

		schemaModelMap.put(sourceId, new DefaultComboBoxModel<String>(schemas));

		tableListMap.put(sourceId, tables);
		sourceList.setSelectedItem(item);

	}

	public void removeDataSource(Integer sourceId) {

		for (int j = sourceVector.size() - 1; j >= 0; j--) {
			DataSourceListItem li = sourceVector.get(j);
			if (sourceId.equals(li.getSourceId())) {
				if (debug)
					System.out.println("GOT IT.");
				sourceVector.remove(j);
			}
		}

		if (debug)
			System.out.println("new sourceModel size is " + sourceModel.getSize());

		Object o = schemaModelMap.remove(sourceId);
		if (debug)
			System.out.println("schemaModel removed: " + o);
		o = tableListMap.remove(sourceId);
		if (debug)
			System.out.println("tableList removed : " + (o != null));

		sourceList.setModel(sourceModel);
		try {
			sourceList.setSelectedIndex(-1);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		schemaList.setModel(this.emptySchemaListModel);
		tableList.setListData(emptyVector);
		if (sourceList.getSelectedIndex() < 0 && sourceModel.getSize() > 0) {
			sourceList.setSelectedIndex(0);
		}
	}

	public TableSelector() {
		this(null);
	}

	public TableSelector(Frame parent) {

		super(parent);

		setTitle("Select a table");

		sourceList.setModel(sourceModel);

		JPanel p1 = new JPanel();
		p1.setLayout(new BorderLayout());
		p1.add(new JLabel("Data Source"), BorderLayout.NORTH);
		p1.add(sourceList, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		JPanel topPanel = new JPanel();
		JPanel centerPanel = new JPanel();
		topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		centerPanel.setBorder(new EmptyBorder(0, 10, 10, 10));
		panel.setLayout(new BorderLayout());
		topPanel.setLayout(new BorderLayout());
		centerPanel.setLayout(new BorderLayout());

		JPanel p2 = new JPanel();
		p2.setLayout(new BorderLayout());
		p2.add(new JLabel("Table Owner"), BorderLayout.NORTH);
		p2.add(schemaList, BorderLayout.CENTER);
		p2.setBorder(new EmptyBorder(10, 0, 0, 0));

		topPanel.add(p1, BorderLayout.NORTH);
		topPanel.add(p2, BorderLayout.CENTER);

		panel.add(topPanel, BorderLayout.NORTH);
		centerPanel.add(new JScrollPane(tableList), BorderLayout.CENTER);
		centerPanel.add(new JLabel("Tables/Views (Views in ITALICS)"), BorderLayout.NORTH);
		panel.add(centerPanel, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();

		GUIHelper.enterPressesWhenFocused(btnOK);
		GUIHelper.enterPressesWhenFocused(btnCancel);

		buttonPanel.add(btnOK);
		buttonPanel.add(btnCancel);
		btnOK.setPreferredSize(btnCancel.getPreferredSize());
		buttonPanel.setBorder(new EmptyBorder(0, 10, 10, 10));
		panel.add(buttonPanel, BorderLayout.SOUTH);

		btnOK.addActionListener(this);
		btnCancel.addActionListener(this);
		this.addKeyListener(new EnterEscapeKeyHandler(btnOK, btnCancel));

		sourceList.addActionListener(this);
		schemaList.addActionListener(this);
		tableList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					setSelections();
					if (selections != null) {
						setVisible(false);
					}
				}
			}
		});
		tableList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		tableList.setCellRenderer(new TableInfoListCellRenderer<TableInfo>());

		setModal(true);
		this.addComponentListener(new ComponentAdapter() {
			public void componentShown(ComponentEvent e) {
				tableList.requestFocus();
				if (tableList.getSelectedIndex() < 0 && tableList.getModel().getSize() > 0) {
					try {
						tableList.setSelectedIndex(0);
					} catch (Exception ex) {
					}
				}
			}

			public void componentResized(ComponentEvent e) {
				int index = tableList.getSelectedIndex();
				if (index > -1) {
					tableList.ensureIndexIsVisible(index);
				}
			}
		});

		// make the OK button close the default so ENTER invokes it
		this.getRootPane().setDefaultButton(this.btnOK);

		// make the Escape key click the cancel button.
		KeyStroke esc = KeyStroke.getKeyStroke("ESCAPE");
		Action closer = new ButtonClicker(btnCancel);
		panel.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(esc, "_ESCAPE_");
		panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(esc, "_ESCAPE_");
		panel.getActionMap().put("_ESCAPE_", closer);

		setContentPane(panel);
		pack();

	}

	// protected void handleSchemaChange

	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();

		if (source == sourceList) {

			DataSourceListItem sourceItem = (DataSourceListItem) sourceList.getSelectedItem();
			if (sourceItem == null)
				return;
			Integer sourceId = sourceItem.getSourceId();
			DefaultComboBoxModel<String> schemaModel = schemaModelMap.get(sourceId);
			schemaList.setModel(schemaModel);
			try {
				schemaList.setSelectedIndex(0);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return;
		}

		if (source == schemaList) {
			DataSourceListItem sourceItem = sourceList.getItemAt(sourceList.getSelectedIndex());
			if (sourceItem == null)
				return; // all lists are empty.
			Integer sourceId = sourceItem.getSourceId();
			String owner = schemaList.getSelectedItem().toString();
			HashMap<String, Vector<TableInfo>> schemaTables = tableListMap.get(sourceId);

			if (owner != null) {
				Vector<TableInfo> v = schemaTables.get(owner);
				tableList.setListData(v);
				tableList.requestFocus();
				try {
					if (v.size() > 0) {
						tableList.setSelectedIndex(0);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		} else if (source == btnCancel) {
			selections = null;
			setVisible(false);
		} else if (source == btnOK) {
			setSelections();
			setVisible(false);
		}

	}

	/**
	 * If a selection was made, returns an Object[3] array. The first element is
	 * an Integer representing the DataSource id, the second element is a String
	 * representing the schema, or table owner, and the third element is the
	 * name of the table.
	 */
	protected void setSelections() {

		selections = null;
		DataSourceListItem source = (DataSourceListItem) sourceList.getSelectedItem();
		if (source == null)
			return;

		selectedTableInfo = tableList.getSelectedValue();
		selectedSourceId = source.getSourceId();
		if (selectedTableInfo == null) {
			return;
		}

		selections = new Object[3];
		selections[0] = source.getSourceId();
		selections[1] = schemaList.getSelectedItem();
		selections[2] = selectedTableInfo;

	}

	public void setVisible(boolean b) {
		if (b) {
			selections = null;
			selectedTableInfo = null;
			selectedSourceId = null;
		}
		super.setVisible(b);
	}

	public Object[] getSelection() {
		return selections;
	}

	public TableInfo getSelectedTableInfo() {
		return selectedTableInfo;
	}

	public Integer getSelectedSourceId() {
		return selectedSourceId;
	}
}
