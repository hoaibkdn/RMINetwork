package rmi.network.server.table;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Test extends JFrame{
	JTable tbTest = new JTable(new DefaultTableModel(
			new Object[][]{
				
			},
			new String[]{
					"Name", "IP Address","Status","Time"
			}
			));
	public Test() {
		tbTest.setRowHeight(25);
		JScrollPane jScrollPane = new JScrollPane();
		jScrollPane.setViewportView(tbTest);
		this.setVisible(true);
		this.setSize(new Dimension(500, 500));
		this.add(tbTest);
		
	}
	
	public static void main(String[] args) {
		new Test();
	}
}
