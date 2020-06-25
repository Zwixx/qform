package org.glasser.qform;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import org.glasser.sql.Column;
import org.glasser.sql.TableInfo;

public class ImportPanel extends JFrame {

	private JTable table;
	private TableInfo tableInfo;

	public ImportPanel(TableInfo tableInfo) {
		super("Import Data from CSV");

		this.tableInfo = tableInfo;
		setSize(668, 345);
		setLocation(500, 280);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);

		// Label Result
		final JLabel lblResult = new JLabel("Result", JLabel.CENTER);
		lblResult.setBounds(150, 22, 370, 14);
		getContentPane().add(lblResult);

		// Table
		table = new JTable();
		getContentPane().add(table);

		// Table Model
		final DefaultTableModel model = (DefaultTableModel) table.getModel();
		for (Column col : tableInfo.getColumns()) {
			model.addColumn(col.getColumnName());
		}

		// ScrollPane
		JScrollPane scroll = new JScrollPane(table);
		scroll.setBounds(84, 98, 506, 79);
		getContentPane().add(scroll);

		// Create Button Open JFileChooser
		JButton btnButton = new JButton("Open File Chooser");
		btnButton.setBounds(268, 47, 135, 23);
		btnButton.addActionListener(e -> {
			File file = openCsv(model);
			lblResult.setText(file.toString());
		});
		getContentPane().add(btnButton);

		// Button Save
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(e -> {
			SaveData();
		});
		btnSave.setBounds(292, 228, 89, 23);
		getContentPane().add(btnSave);

	}

	private File openCsv(final DefaultTableModel model) {
		JFileChooser fileopen = new JFileChooser();
		fileopen.addChoosableFileFilter(new FileNameExtensionFilter("Text/CSV file", "txt", "csv"));

		int ret = fileopen.showDialog(null, "Choose file");

		if (ret == JFileChooser.APPROVE_OPTION) {

			// Read Text file
			File file = fileopen.getSelectedFile();

			try (BufferedReader br = new BufferedReader(new FileReader(file))) {
				String line;
				int row = 0;
				while ((line = br.readLine()) != null) {
					String[] arr = line.split(",");
					model.addRow(new Object[0]);
					for (int col = 0; col < arr.length; col++) {
						model.setValueAt(arr[0], row, col);
					}
					row++;
				}
			} catch (IOException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}

			return fileopen.getSelectedFile();
		} else {
			return null;
		}
	}

	private void SaveData() {
		Connection connect = ;
		try {

			String sql = "INSERT INTO " + tableInfo.getTableName() + " (";
			for (int j = 0; j < table.getColumnCount(); j++) {
				sql += table.getModel().getColumnName(j) + ", ";
			}
			sql += ") VALUES (";
			for (int j = 0; j < table.getColumnCount(); j++) {
				sql += "?, ";
			}
			sql += ");";
			
			PreparedStatement s = connect.prepareStatement(sql);
			for (int i = 0; i < table.getRowCount(); i++) {
				for (int j = 0; j < table.getColumnCount(); j++) {
					s.setString(j, table.getValueAt(i, j).toString());
				}
				s.execute(sql);
			}

			JOptionPane.showMessageDialog(null, "Import Data Successfully");

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage());
			ex.printStackTrace();
		}
	}
}
