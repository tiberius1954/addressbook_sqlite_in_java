package Databaseop;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import Classes.Hhelper;

public class Databaseop {
	Connection con;
	Statement stmt;
	PreparedStatement pst;
	ResultSet rs;
	DatabaseHelper dh = new DatabaseHelper();
	Hhelper hh = new Hhelper();

	public int data_delete(JTable dtable, String sql) {
		int flag = 0;
		DefaultTableModel d1 = (DefaultTableModel) dtable.getModel();
		int sIndex = dtable.getSelectedRow();
		if (sIndex < 0) {
			return flag;
		}
		String iid = d1.getValueAt(sIndex, 0).toString();
		if (iid.equals("")) {
			return flag;
		}
		int a = JOptionPane.showConfirmDialog(null, "Do you really want to delete ?");
		if (a == JOptionPane.YES_OPTION) {
			String vsql = sql + iid;
			flag = dh.Insupdel(vsql);
			if (flag > 0) {
				d1.removeRow(sIndex);
			}
		}
		return flag;
	}

	public void rtable_delete(JTable dtable, String aid) {
		DefaultTableModel d1 = (DefaultTableModel) dtable.getModel();
		String sql;
		int flag = 0;
		int sIndex = dtable.getSelectedRow();
		int selections[] = dtable.getSelectedRows();
		int lastIndex = selections[0];
		sql = "delete from addresses where aid='" + aid + "'";
		try {
			flag = dh.Insupdel(sql);
			d1.removeRow(sIndex);
			if (dtable.getRowCount() > 0) {
				dtable.setRowSelectionInterval(lastIndex - 1, lastIndex - 1);
			}
		} catch (Exception e1) {
			if (dtable.getRowCount() > 0)
				dtable.setRowSelectionInterval(0, 0);
		}
		sql = "delete from choosedgroup where aid='" + aid + "'";
		flag = dh.Insupdel(sql);
	}

	public int tdata_delete(JTable dtable, String sql, int row) {
		int flag = 0;
		DefaultTableModel d1 = (DefaultTableModel) dtable.getModel();
		flag = dh.Insupdel(sql);
		if (flag == 1) {
			d1.removeRow(row);
		}
		return flag;
	}

	public int table_maxid(String sql) {
		int myid = 0;
		try {

			rs = dh.GetData(sql);
			if (!rs.next()) {
				System.out.println("Error.");
			} else {
				myid = rs.getInt("max_id");
			}
			dh.CloseConnection();
		} catch (SQLException ex) {
			System.err.println("SQLException: " + ex.getMessage());
			ex.printStackTrace();
		}
		return myid;
	}

	public Boolean cannotdelete(String sql) {
		Boolean found = false;
		rs = dh.GetData(sql);
		try {
			if (rs.next()) {
				found = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		dh.CloseConnection();
		return found;
	}

	public void countrycombofill(JComboBox ccombo) {
		String Sql = " select countryname from country order by  countryname";
		try {
			ResultSet res = dh.GetData(Sql);
			while (res.next()) {
				ccombo.addItem(res.getString("countryname"));
			}
			dh.CloseConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void cmbgroupscombofill(JComboBox mycombo)  {
		mycombo.removeAllItems();
		Groups  A = new Groups (0, "");
		mycombo.addItem(A);
		String sql = "select gid, name  from groups order by name";
		ResultSet rs = dh.GetData(sql);
		try {
			while (rs.next()) {
				A = new Groups(rs.getInt("gid"), rs.getString("name"));
				mycombo.addItem(A);
			}
		} catch (SQLException e) {			
			e.printStackTrace();
		}
		dh.CloseConnection();
	}
	
	public class Groups {
		  int gid;	
		  String name;	
		  public Groups() {
			  
		  }
		  public Groups (int gid, String name) {
		    this.gid = gid;
		    this.name = name;		  
		  }
		  @Override
		  public String toString() 
		  { 	
		     	return name; 	 
		  } 
		 public int getGid() {
			  return gid;
		  }

		}

	public String[][] gArrayfill() {
		String garray[][] = null;
		int max = 0;
		int i = 0;
		try {
			String Sql = "select count() as number from groups";
			rs = dh.GetData(Sql);
			if (rs.next()) {
				max = rs.getInt("number");
			}
			garray = new String[max][2];
			dh.CloseConnection();
			Sql = "select  gid, name  from groups order by name";
			rs = dh.GetData(Sql);
			while (rs.next()) {
				garray[i][0] = rs.getString("gid");
				garray[i][1] = rs.getString("name");
				i++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dh.CloseConnection();
		}
		return garray;
	}

	public void atable_update(JTable dtable, String what, String which) {
		DefaultTableModel m1 = (DefaultTableModel) dtable.getModel();
		m1.setRowCount(0);
		String Sql = "";
		if (which =="F") {
			Sql =what;
		}else {
		if (what == "") {
			Sql = "select  aid, fname, lname,nickname,mobile, email from addresses";
		} else {
			Sql = "select  aid, fname, lname, nickname, mobile, email  from addresses  where " + what;
		}
		}
		try {
			rs = dh.GetData(Sql);
			while (rs.next()) {
				String aid = rs.getString("aid");
				String fname = rs.getString("fname");
				String lname = rs.getString("lname");
				String nickname = rs.getString("nickname");
				String mobile = rs.getString("mobile");
				String email = rs.getString("email");
				m1.addRow(new Object[] { aid, fname, lname, nickname, mobile, email });
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dh.CloseConnection();
		}
		String[] fej = { "aid", "First name", "Last name", "Nickname", "Mobile", "Email" };
		((DefaultTableModel) dtable.getModel()).setColumnIdentifiers(fej);
		hh.setJTableColumnsWidth(dtable, 1135, 0, 25, 25, 10, 15, 25);
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

	public void groupsave(String rowid, JRadioButton[] gbuttons) {
		String gid = "";
		String Sql = "delete from choosedgroup where aid ='" + rowid + "'";
		int flag = dh.Insupdel(Sql);

		for (int i = 0; i < gbuttons.length; i++) {
			if (gbuttons[i].isSelected() == true) {
				gid = gbuttons[i].getName();
				Sql = "insert into choosedgroup (aid, gid) values('" + rowid + "','" + gid + "')";
				flag = dh.Insupdel(Sql);
			}
		}

	}

}
