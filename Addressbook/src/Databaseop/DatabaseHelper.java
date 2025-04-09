package Databaseop;

import java.sql.*;
import javax.swing.JOptionPane;

public class DatabaseHelper {
	Connection con = null;
	PreparedStatement pst = null;
	ResultSet rst = null;

	public Connection getConnection() {
		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:addressbook.db");
			return con;
		} catch (Exception e) {
			System.err.println("Connection error");
			return null;
		}
	}

	public ResultSet GetData(String Sql)// for select
	{
		try {
			con = getConnection();
			pst = con.prepareStatement(Sql);
			rst = pst.executeQuery();
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, ex + "\nDbConnection Error");
		}
		return rst;
	}

	public int Insupdel(String Sql) // for insert, update, delete
	{
		int flag = 0;
		try {
			con = getConnection();
			pst = con.prepareStatement(Sql);
			flag = pst.executeUpdate();
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, ex + "\n Insupdel hiba !");
			System.out.println(ex);
			ex.printStackTrace();			
		}
		CloseConnection();
		return flag;
	}

	public void CloseConnection() {
		if (rst != null) {
			try {
				rst.close();
			} catch (SQLException e) {
				/* ignored */}
		}
		if (pst != null) {
			try {
				pst.close();
			} catch (SQLException e) {
				/* ignored */}
		}
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				/* ignored */}
		}
	}
}
