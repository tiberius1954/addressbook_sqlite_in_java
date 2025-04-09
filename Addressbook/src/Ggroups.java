import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import Classes.Grlib;
import Classes.Hhelper;
import Databaseop.DatabaseHelper;
import Databaseop.Databaseop;

public class Ggroups extends JFrame {
	ResultSet rs;
	Connection con = null;
	Grlib gr = new Grlib();
	DatabaseHelper dh = new DatabaseHelper();
	Databaseop dd = new Databaseop();
	Hhelper hh = new Hhelper();
	String rowid = "";
	int myrow = 0;

	Ggroups() {
		init();
		hh.iconhere(this);
		gtable_update(gtable, "");

	}

	private void init() {
		UIManager.put("ComboBox.selectionBackground", hh.piros);
		UIManager.put("ComboBox.selectionForeground", hh.feher);
		UIManager.put("ComboBox.background", new ColorUIResource(hh.homok));
		UIManager.put("ComboBox.foreground", Color.BLACK);
		UIManager.put("ComboBox.border", new LineBorder(Color.green, 1));
		UIManager.put("ComboBox.disabledForeground", Color.magenta);
		setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				dispose();
			}
		});
		setTitle("Groups");
		setSize(480, 475);
		setLayout(null);
		setLocationRelativeTo(null);
		getContentPane().setBackground(hh.cian);

		mPanel = new JPanel(null);
		mPanel.setBounds(10, 10, 440, 410);
		mPanel.setBorder(hh.ztroundborder(Color.yellow));

		mPanel.setBackground(hh.cian);
		add(mPanel);

		lbheader = new JLabel("G R O U P S");
		lbheader.setForeground(Color.yellow);
		lbheader.setFont(new Font("tahoma", Font.BOLD, 24));
		lbheader.setBounds(180, 20, 350, 30);
		mPanel.add(lbheader);

		lbname = hh.clabel("Group name");
		lbname.setBounds(10, 70, 100, 30);
		mPanel.add(lbname);

		txname = hh.cTextField(40);
		txname.setBounds(120, 70, 280, 30);
		mPanel.add(txname);
		txname.addKeyListener(hh.MUpper());

		btnsave = gr.sbcs("Save");
		btnsave.setBounds(120, 125, 90, 35);
		btnsave.setBackground(hh.lpiros);
		mPanel.add(btnsave);
		btnsave.addActionListener(e -> savebutt());

		btndelete = gr.sbcs("Delete");
		btndelete.setBounds(215, 125, 90, 35);		
		btndelete.setBackground(Color.yellow);
		mPanel.add(btndelete);
		btndelete.addActionListener(e -> data_delete());

		btncancel = gr.sbcs("Cancel");
		btncancel.setBounds(310, 125, 90, 35);		
		btncancel.setBackground(Color.green);
		mPanel.add(btncancel);
		btncancel.addActionListener(e -> data_cancel());

		gtable = hh.ztable();
		gtable.setTableHeader(new JTableHeader(gtable.getColumnModel()) {
			@Override
			public Dimension getPreferredSize() {
				Dimension d = super.getPreferredSize();
				d.height = 25;
				return d;
			}
		});

		gtable.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				int row = gtable.getSelectedRow();
				if (row >= 0) {
					rowid = gtable.getValueAt(row, 0).toString();
					txname.setText(gtable.getValueAt(row, 1).toString());
					myrow = row;
				}
			}
		});

		hh.madeheader(gtable);
		gtable.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				gtable.scrollRectToVisible(gtable.getCellRect(gtable.getRowCount() - 1, 0, true));
			}
		});

		jScrollPane1 = new JScrollPane(gtable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		gtable.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {}, new String[] { "gid", "Name" }));
		hh.setJTableColumnsWidth(gtable, 360, 0, 100);
		jScrollPane1.setViewportView(gtable);
		jScrollPane1.setBounds(50, 180, 360, 200);
		// jScrollPane1.setBorder(hh.borderf);
		mPanel.add(jScrollPane1);
		setVisible(true);
	}

	private int data_delete() {
		String sql = " delete from groups where gid=";
		int flag = 0;
		DefaultTableModel d1 = (DefaultTableModel) gtable.getModel();
		int sIndex = gtable.getSelectedRow();
		if (sIndex < 0) {
			return flag;
		}
		String gid = d1.getValueAt(sIndex, 0).toString();
		if (gid.equals("")) {
			return flag;
		}
		int a = JOptionPane.showConfirmDialog(null, "Do you really want to delete ?");
		if (a == JOptionPane.YES_OPTION) {
			int selections[] = gtable.getSelectedRows();
			int lastIndex = selections[0];
			String vsql = sql + "'" + gid + "'";
			try {
			flag = dh.Insupdel(vsql);
			d1.removeRow(sIndex);
			if (gtable.getRowCount() > 0) {
				gtable.setRowSelectionInterval(lastIndex - 1, lastIndex - 1);
			}
			} catch (Exception e1) {
				if (gtable.getRowCount() > 0)
					gtable.setRowSelectionInterval(0, 0);
			}
			clearFields();
		}
		return flag;
	}

	private void savebutt() {
		DefaultTableModel d1 = (DefaultTableModel) gtable.getModel();
		String sql = "";
		int gid = 0;
		String name = txname.getText();
		if (hh.zempty(name) == true) {
			return;
		}
		try {
			if (rowid != "") {
				sql = "update  groups set name= '" + name + "' where gid = " + rowid;
			} else {
				sql = "insert into groups (name) values" + " ('" + name + "')";
			}
			int flag = dh.Insupdel(sql);
			if (flag > 0) {
				hh.ztmessage("Success", "Message");
				if (rowid == "") {
					int myid = dd.table_maxid("SELECT MAX(gid) AS max_id from groups");
					d1.insertRow(d1.getRowCount(), new Object[] { myid, name });
					hh.gotolastrow(gtable);
					if (gtable.getRowCount() > 0) {
						int row = gtable.getRowCount() - 1;
						gtable.setRowSelectionInterval(row, row);
					}

				} else {
					table_rowrefresh(name);
				}
			} else {
				JOptionPane.showMessageDialog(null, "sql error !");
			}
		} catch (Exception e) {
			System.err.println("SQLException: " + e.getMessage());
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "sql insert hiba");
		}
		clearFields();
	}

	private void table_rowrefresh(String name) {
		DefaultTableModel d1 = (DefaultTableModel) gtable.getModel();
		d1.setValueAt(name, myrow, 1);
	}

	private void clearFields() {
		txname.setText("");
		myrow = 0;
		rowid = "";
	}

	private void data_cancel() {
		clearFields();
		txname.requestFocus(true);
	}

	private void gtable_update(JTable dtable, String what) {
		DefaultTableModel m1 = (DefaultTableModel) dtable.getModel();
		m1.setRowCount(0);
		String Sql = "";
		if (what == "") {
			Sql = "select  gid, name from groups";
		} else {
			Sql = "select  gid, name  from groups where " + what;
		}
		try {
			rs = dh.GetData(Sql);
			while (rs.next()) {
				String gid = rs.getString("gid");
				String name = rs.getString("name");
				m1.addRow(new Object[] { gid, name });
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dh.CloseConnection();
		}
		String[] fej = { "gid", "Name" };
		((DefaultTableModel) dtable.getModel()).setColumnIdentifiers(fej);
		hh.setJTableColumnsWidth(gtable, 360, 0, 100);
//			DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) dtable.getDefaultRenderer(Object.class);
		// renderer.setHorizontalAlignment(SwingConstants.LEFT);

		dtable.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				dtable.scrollRectToVisible(dtable.getCellRect(dtable.getRowCount() - 1, 0, true));
			}
		});
		if (dtable.getRowCount() > 0) {
			int row = dtable.getRowCount() - 1;
			dtable.setRowSelectionInterval(row, row);
		}
	}

	public static void main(String args[]) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new Ggroups();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	JPanel mPanel;
	JLabel lbheader, lbname;
	JTextField txname;
	JButton btnsave, btndelete, btncancel;
	JTable gtable;
	JScrollPane jScrollPane1;
}
