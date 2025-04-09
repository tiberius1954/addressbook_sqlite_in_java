import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.print.DocFlavor.URL;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.ColorUIResource;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import Classes.Grlib;
import Classes.Hhelper;
import Databaseop.DatabaseHelper;
import Databaseop.Databaseop;
import Databaseop.Databaseop.Groups;

public class Addressbook extends JFrame implements ActionListener {
	ResultSet rs;
	Connection con = null;
	Grlib gr = new Grlib();
	DatabaseHelper dh = new DatabaseHelper();
	Databaseop dd = new Databaseop();
	Hhelper hh = new Hhelper();
	String rowid = "";
	int myrow = 0;
	Array gArray;
	JRadioButton[] gbuttons;
	Groups groupss = (dd.new Groups());
	

	Addressbook() {
		init();
		hh.iconhere(this);
		dd.countrycombofill(cmbcountries);
		dd.cmbgroupscombofill(cmbgroups);	
		radiofilling(gPanel);
		dd.atable_update(atable, "","");
	}

	private void init() {
		UIManager.put("ComboBox.selectionBackground", hh.lpiros);
		UIManager.put("ComboBox.selectionForeground", hh.feher);
		UIManager.put("ComboBox.background", new ColorUIResource(hh.homok));
		UIManager.put("ComboBox.foreground", Color.BLACK);
		UIManager.put("ComboBox.border", new LineBorder(Color.green, 1));		
		setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				if (hh.whichpanel(cardPanel) == "tabla") {
					dispose();
				} else if (hh.whichpanel(cardPanel) == "edit") {
					cards.show(cardPanel, "tabla");
				} else {
					cards.show(cardPanel, "tabla");
				}
			}
		});	

	
		setTitle("Addressbook");
		getContentPane().setBackground(hh.cian);
		setSize(1230, 680);
		setLayout(null);
		setLocationRelativeTo(null);
		setVisible(true);
		cards = new CardLayout();
		cardPanel = new JPanel();	
		cardPanel.setLayout(cards);
		cardPanel.setBounds(10, 10, 1192, 620);
		tPanel = maketpanel();
		tPanel.setName("tabla");
		ePanel = makeepanel();
		ePanel.setName("edit");
		cardPanel.add(tPanel, "tabla");
		cardPanel.add(ePanel, "edit");
		add(cardPanel);
		cards.show(cardPanel, "tabla");
		// cards.show(cardPanel, "edit");
		
	}

	private JPanel maketpanel() {
		JPanel ttpanel = new JPanel(null);
		ttpanel.setBorder(hh.ztroundborder(Color.yellow));
		ttpanel.setBackground(hh.cian);
		ttpanel.setBounds(20, 20, 1100, 430);
		lbheader = new JLabel("ADDRESBOOK");
		lbheader.setBounds(30, 15, 200, 40);
		lbheader.setForeground(Color.yellow);
		lbheader.setFont(new Font("tahoma", Font.BOLD, 24));
		ttpanel.add(lbheader);

		lbsearch = hh.clabel("Search:");
		lbsearch.setBounds(250, 25, 70, 25);
		ttpanel.add(lbsearch);

		txsearch = cTextField(25);
		txsearch.setBounds(330, 25, 200, 25);
		ttpanel.add(txsearch);

		btnclear = new JButton();
		btnclear.setFont(new java.awt.Font("Tahoma", 1, 16));
		btnclear.setMargin(new Insets(0, 0, 0, 0));
		btnclear.setBounds(530, 25, 25, 25);
		btnclear.setBorder(hh.borderf);
		btnclear.setText("x");
		ttpanel.add(btnclear);
		btnclear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				txsearch.setText("");
				txsearch.requestFocus();
				dd.atable_update(atable, "","");
			}
		});
		cmbsearch = hh.cbcombo();
		cmbsearch.setFocusable(true);
		cmbsearch.setBounds(560, 25, 150, 29);
		cmbsearch.setFont(new java.awt.Font("Tahoma", 1, 16));
		cmbsearch.setBorder(BorderFactory.createMatteBorder(1, 1, 3, 3, Color.DARK_GRAY));
		cmbsearch.setBackground(Color.ORANGE);
		cmbsearch.addItem("Name");
		cmbsearch.addItem("Mobile");	
		ttpanel.add(cmbsearch);
	
		btnsearch = hh.cbutton("");
		btnsearch.setForeground(Color.black);
		btnsearch.setBackground(Color.ORANGE);
		// btnsearch.setBorder(BorderFactory.createEmptyBorder());
		btnsearch.setIcon(new ImageIcon(getClass().getResource("images/search5.png")));
		btnsearch.setBounds(715, 25, 50, 27);
		btnsearch.setMargin(new Insets(0, 0, 0, 0));
		btnsearch.setBorder(BorderFactory.createMatteBorder(1, 1, 2, 2, Color.DARK_GRAY));
		btnsearch.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		btnsearch.setToolTipText("Searching in addresses.");
		ttpanel.add(btnsearch);
		btnsearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sqlgyart();
			}
		});

		lbgroups = hh.clabel("Groups:");
		lbgroups.setBounds(800, 25, 70, 25);
		ttpanel.add(lbgroups);

		cmbgroups = hh.cbcombo();
		cmbgroups.setFocusable(true);
		cmbgroups.setBounds(880, 25, 150, 29);
		cmbgroups.setFont(new java.awt.Font("Tahoma", 1, 16));
		cmbgroups.setBorder(BorderFactory.createMatteBorder(1, 1, 3, 3, Color.DARK_GRAY));
		cmbgroups.setBackground(Color.ORANGE);
		ttpanel.add(cmbgroups);
		
		btnclearf = new JButton();
		btnclearf.setFont(new java.awt.Font("Tahoma", 1, 16));
		btnclearf.setMargin(new Insets(0, 0, 0, 0));
		btnclearf.setBounds(1028, 26, 25, 27);
		btnclearf.setBorder(hh.borderf);
		btnclearf.setText("x");
		ttpanel.add(btnclearf);
		btnclearf.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {				
			   cmbgroups.setSelectedIndex(0);
				dd.atable_update(atable, "","");
			}
		});

		btnfilter = hh.cbutton("");
		btnfilter.setBounds(1059, 25, 50, 29);
		btnfilter.setBackground(Color.ORANGE);
		btnfilter.setIcon(new ImageIcon(getClass().getResource("images/filter2.png")));
		btnfilter.setToolTipText("Filter on groups");
		btnfilter.setMargin(new Insets(0, 0, 0, 0));
		ttpanel.add(btnfilter);
		
		btnfilter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sqlfilter();
			}
		});

		atable = hh.ztable();
		DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) atable.getDefaultRenderer(Object.class);
		renderer.setHorizontalAlignment(SwingConstants.LEFT);
		atable.setTableHeader(new JTableHeader(atable.getColumnModel()) {
			@Override
			public Dimension getPreferredSize() {
				Dimension d = super.getPreferredSize();
				d.height = 25;
				return d;
			}
		});
		atable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent event) {
				DefaultTableModel model = (DefaultTableModel) atable.getModel();
				try {
					int row = atable.getSelectedRow();
					if (row > -1) {
						String myaid = model.getValueAt(row, 0).toString();
						moredata(myaid, 1);
					}
				} catch (Exception e) {
					System.out.println("sql error!!!");
				}
			}
		});

		hh.madeheader(atable);
		atable.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		aPane = new JScrollPane(atable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		atable.setModel(new javax.swing.table.DefaultTableModel(
				new Object[][] { { null, null, null, null, null, null, null, null, null, null, null }, },
				new String[] { "aid", "First name", "Last name", "Nickname", "Mobile", "Email" }));
		hh.setJTableColumnsWidth(atable, 1135, 0, 25, 25, 10, 15, 25);
		aPane.setViewportView(atable);
		aPane.setBounds(30, 110, 1135, 260);
		ttpanel.add(aPane);

		btnnew = hh.cbutton("");
		btnnew.setBounds(40, 72, 50, 33);
		btnnew.setBackground(hh.vpiros);
		btnnew.setIcon(new ImageIcon(getClass().getResource("images/newbtn2.png")));
		btnnew.setToolTipText("Add new address");
		btnnew.setMargin(new Insets(0, 0, 0, 0));
		ttpanel.add(btnnew);
		btnnew.addActionListener(e -> data_new());

		btnupdate = hh.cbutton("");
		btnupdate.setBounds(95, 72, 50, 33);
		btnupdate.setBackground(hh.vvzold);
		btnupdate.setToolTipText("Update record");
		btnupdate.setIcon(new ImageIcon(getClass().getResource("images/edit3.png")));
		btnupdate.setMargin(new Insets(0, 0, 0, 0));
		ttpanel.add(btnupdate);		
		btnupdate.addActionListener(e -> data_update());	

		btndelete = hh.cbutton("");
		btndelete.setBounds(150, 72, 50, 33);
		btndelete.setBackground(hh.sarga);
		btndelete.setIcon(new ImageIcon(getClass().getResource("images/delete4.png")));
		btndelete.setToolTipText("Delete record");
		btndelete.setMargin(new Insets(0, 0, 0, 0));
		ttpanel.add(btndelete);
		btndelete.addActionListener(e -> data_delete());	
	
		lbccountry = hh.clabel("Country");
		lbccountry.setBounds(15, 400, 80, 25);
		ttpanel.add(lbccountry);

		lbtcountry = blabel("");
		lbtcountry.setBounds(105, 400, 300, 25);
		ttpanel.add(lbtcountry);

		lbccity = hh.clabel("Town");
		lbccity.setBounds(15, 440, 80, 25);
		ttpanel.add(lbccity);

		lbtcity = blabel("");
		lbtcity.setBounds(105, 440, 300, 25);
		ttpanel.add(lbtcity);

		lbcstreet = hh.clabel("Street");
		lbcstreet.setBounds(15, 480, 80, 25);
		ttpanel.add(lbcstreet);

		lbtstreet = blabel("");
		lbtstreet.setBounds(105, 480, 300, 25);
		ttpanel.add(lbtstreet);

		lbcpostcode = hh.clabel("Postcode");
		lbcpostcode.setBounds(15, 520, 80, 25);
		ttpanel.add(lbcpostcode);

		lbtpostcode = blabel("");
		lbtpostcode.setBounds(105, 520, 150, 25);
		ttpanel.add(lbtpostcode);

		lbcwebpage = hh.clabel("Webpage");
		lbcwebpage.setBounds(15, 560, 80, 25);
		ttpanel.add(lbcwebpage);

		lbtwebpage = blabel("");
		lbtwebpage.setBounds(105, 560, 300, 25);
		ttpanel.add(lbtwebpage);

		lbcgroup = hh.clabel("Groups");
		lbcgroup.setBounds(400, 400, 80, 25);
		ttpanel.add(lbcgroup);

		txagroup = new JTextArea();
		txagroup.setBackground(hh.tcolor);
		txagroup.setFont(hh.textf5);
		txagroup.setEditable(false);
		txagroup.setFocusable(false);
		jsp = new JScrollPane(txagroup, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jsp.setBounds(490, 400, 250, 200);
		jsp.setViewportView(txagroup);
		jsp.setBorder(hh.borderf);
		ttpanel.add(jsp);

		lbcnote = hh.clabel("Note");
		lbcnote.setBounds(720, 400, 80, 25);
		ttpanel.add(lbcnote);

		ttanote = new JTextArea();
		ttanote.setBackground(hh.tcolor);
		// ttanote.setBorder(hh.borderf);
		ttanote.setFont(hh.textf5);
		ttanote.setEditable(false);
		ttanote.setFocusable(false);
		jspn = new JScrollPane(ttanote, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jspn.setBounds(810, 400, 350, 200);
		jspn.setViewportView(ttanote);
		jspn.setBorder(hh.borderf);
		ttpanel.add(jspn);
		return ttpanel;
	}

	private JPanel makeepanel() {
		JPanel eepanel = new JPanel(null);
		eepanel.setBorder(hh.ztroundborder(Color.yellow));
		eepanel.setBackground(hh.cian);
		eepanel.setBounds(20, 20, 1100, 430);
		lbeheader = new JLabel("ADDRESBOOK");
		lbeheader.setBounds(30, 15, 200, 40);
		lbeheader.setForeground(Color.yellow);
		lbeheader.setFont(new Font("tahoma", Font.BOLD, 24));
		eepanel.add(lbeheader);
		lPanel = new JPanel(null);
		lPanel.setBounds(10, 60, 582, 530);
		lPanel.setBorder(hh.ztroundborder(Color.yellow));
		lPanel.setBackground(hh.cian);
		eepanel.add(lPanel);

		rPanel = new JPanel(null);
		rPanel.setBounds(600, 60, 582, 530);
		rPanel.setBorder(hh.ztroundborder(Color.yellow));
		rPanel.setBackground(hh.cian);
		eepanel.add(rPanel);

		lbefname = hh.clabel("First name");
		lbefname.setBounds(10, 20, 150, 25);
		lPanel.add(lbefname);

		txfname = cTextField(30);
		txfname.setBounds(170, 20, 250, 25);
		lPanel.add(txfname);
		txfname.addKeyListener(hh.MUpper());

		lbelname = hh.clabel("Last Name");
		lbelname.setBounds(10, 70, 150, 25);
		lPanel.add(lbelname);

		txlname = cTextField(30);
		txlname.setBounds(170, 70, 250, 25);
		lPanel.add(txlname);
		txlname.addKeyListener(hh.MUpper());

		lbenickname = hh.clabel("Nickname");
		lbenickname.setBounds(10, 120, 150, 25);
		lPanel.add(lbenickname);

		txnickname = cTextField(30);
		txnickname.setBounds(170, 120, 250, 25);
		lPanel.add(txnickname);
		txnickname.addKeyListener(hh.MUpper());

		lbephones = hh.clabel("Phones");
		lbephones.setBounds(10, 170, 150, 25);
		lPanel.add(lbephones);

		txphones = cTextField(30);
		txphones.setBounds(170, 170, 250, 25);
		lPanel.add(txphones);

		lbemobile = hh.clabel("Mobile");
		lbemobile.setBounds(10, 220, 150, 25);
		lPanel.add(lbemobile);

		txmobile = cTextField(30);
		txmobile.setBounds(170, 220, 250, 25);
		lPanel.add(txmobile);

		lbeemail = hh.clabel("Email");
		lbeemail.setBounds(10, 270, 150, 25);
		lPanel.add(lbeemail);

		txemail = cTextField(30);
		txemail.setBounds(170, 270, 250, 25);
		lPanel.add(txemail);

		lbewebpage = hh.clabel("Webpage");
		lbewebpage.setBounds(10, 320, 150, 25);
		lPanel.add(lbewebpage);

		txwebpage = cTextField(30);
		txwebpage.setBounds(170, 320, 250, 25);
		lPanel.add(txwebpage);

		lbecountry = hh.clabel("Countries");
		lbecountry.setBounds(10, 370, 150, 25);
		lPanel.add(lbecountry);

		cmbcountries = hh.cbcombo();
		cmbcountries.setBounds(170, 370, 250, 27);
		lPanel.add(cmbcountries);

		lbecity = hh.clabel("Town");
		lbecity.setBounds(10, 420, 150, 25);
		lPanel.add(lbecity);

		txcity = cTextField(30);
		txcity.setBounds(170, 420, 250, 25);
		lPanel.add(txcity);
		txcity.addKeyListener(hh.MUpper());

		lbestreet = hh.clabel("Street");
		lbestreet.setBounds(10, 470, 150, 25);
		lPanel.add(lbestreet);

		txstreet = cTextField(30);
		txstreet.setBounds(170, 470, 250, 25);
		lPanel.add(txstreet);
		txstreet.addKeyListener(hh.MUpper());

		lbepostcode = hh.clabel("Postcode");
		lbepostcode.setBounds(10, 20, 150, 25);
		rPanel.add(lbepostcode);

		txpostcode = cTextField(30);
		txpostcode.setBounds(170, 20, 250, 25);
		rPanel.add(txpostcode);

		lbepostcode = hh.clabel("Mailbox");
		lbepostcode.setBounds(10, 70, 150, 25);
		rPanel.add(lbepostcode);

		txmailbox = cTextField(30);
		txmailbox.setBounds(170, 70, 250, 25);
		rPanel.add(txmailbox);

		lbenote = hh.clabel("Note");
		lbenote.setBounds(10, 100, 150, 25);
		rPanel.add(lbenote);

		txanote = new JTextArea();
		txanote.setBackground(hh.tcolor);
		txanote.setBorder(hh.borderf);
		txanote.setFont(hh.textf5);
		// txanote.setEditable(false);
		jspe = new JScrollPane(txanote, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jspe.setBounds(170, 100, 350, 180);
		jspe.setViewportView(txanote);
		rPanel.add(jspe);

		lbegroup = hh.clabel("Groups");
		lbegroup.setBounds(10, 290, 150, 25);
		rPanel.add(lbegroup);

		gPanel = new JPanel();
		gPanel.setLayout(new BoxLayout(gPanel, BoxLayout.Y_AXIS));
		gPanel.setBorder(hh.borderf);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(170, 290, 250, 150);
		scrollPane.setViewportView(gPanel);
		rPanel.add(scrollPane);

		btnsave = gr.sbcs("Save");
		btnsave.setBounds(180, 470, 120, 35);
		btnsave.setBackground(hh.lpiros);
		rPanel.add(btnsave);
		btnsave.addActionListener(e -> savebuttrun());

		btncancel = gr.sbcs("Cancel");
		btncancel.setBounds(310, 470, 120, 35);
		btncancel.setBackground(Color.green);
		rPanel.add(btncancel);
       btncancel.addActionListener(e -> cancelbutt());
   
		return eepanel;
	}

	public JLabel blabel(String string) {
		JLabel llabel = new JLabel(string);
		llabel.setFont(new Font("Tahoma", 1, 16));
		llabel.setBackground(new Color(255, 255, 255));
		llabel.setOpaque(true);
		llabel.setForeground(Color.black);
		llabel.setPreferredSize(new Dimension(120, 30));
		llabel.setHorizontalAlignment(JLabel.LEFT);
		return llabel;
	}

	void radiofilling(JPanel gpanel) {
		String[][] gArray = dd.gArrayfill();
		gbuttons = new JRadioButton[gArray.length];
		for (int i = 0; i < gbuttons.length; i++) {
			gbuttons[i] = new JRadioButton();
			gbuttons[i].setName(gArray[i][0]);
			gbuttons[i].setText(gArray[i][1]);
			gbuttons[i].setSelected(false);
			gbuttons[i].addActionListener(this);
			gpanel.add(gbuttons[i]);
		}
	}

	private void moredata(String myaid, int ihow) {
		String txt = "";
		String Sql = "select  *  from addresses where aid ='" + myaid + "'";
		ArrayList<String> gidlist = new ArrayList<>();
		try {
			rs = dh.GetData(Sql);
			while (rs.next()) {
				String aid = rs.getString("aid");
				String fname = rs.getString("fname");
				String lname = rs.getString("lname");
				String nickname = rs.getString("nickname");
				String mobile = rs.getString("mobile");
				String email = rs.getString("email");
				String phones = rs.getString("phones");
				String country = rs.getString("country");
				String city = rs.getString("city");
				String street = rs.getString("street");
				String postcode = rs.getString("postcode");
				String mailbox = rs.getString("mailbox");
				String note = rs.getString("note");
				String webpage = rs.getString("webpage");
				if (ihow == 1) {
					lbtcountry.setText(country);
					lbtcity.setText(city);
					lbtstreet.setText(street);
					lbtpostcode.setText(postcode);
					lbtwebpage.setText(webpage);
					ttanote.setText(note);					
				} else {
					txfname.setText(fname);
					txlname.setText(lname);
					txnickname.setText(nickname);
					txphones.setText(phones);
					txmobile.setText(mobile);
					txcity.setText(city);
					txstreet.setText(street);
					txpostcode.setText(postcode);
					txmailbox.setText(mailbox);
					txwebpage.setText(webpage);
					txemail.setText(email);
					cmbcountries.setSelectedItem(country);
					txanote.setText(note);
				}
			}
			dh.CloseConnection();
			Sql = "select  c.gid, g.name  from choosedgroup c   join groups  g  on c.gid = g.gid " + " where c.aid ='"
					+ myaid + "' order by name";
			rs = dh.GetData(Sql);
			while (rs.next()) {
				txt = txt + rs.getString("name") + "\n";
				gidlist.add(rs.getString("gid"));
			}
			if (ihow == 1) {
				txagroup.setText(txt);			
			}else {					
				for( int i = 0; i < gbuttons.length; i++ ){
					if (gidlist.contains(gbuttons[i].getName()) ) {
						gbuttons[i].setSelected(true);
					}					
			    }				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dh.CloseConnection();
		}
	}
	
	private void data_new(){
		clearFields();
		cards.show(cardPanel, "edit");
		txfname.requestFocus();
	}
	
	private void data_update(){
		clearFields();
		DefaultTableModel d1 = (DefaultTableModel) atable.getModel();
		int row = atable.getSelectedRow();
		myrow = 0;
		if (row < 0) {
			return;		
		} else {
			myrow = row;
			rowid = d1.getValueAt(row, 0).toString();
			moredata(rowid, 2);
			 cards.show(cardPanel, "edit");
		}
		}
	private void cancelbutt() {
		clearFields();
	  	cards.show(cardPanel, "tabla");		
	}

	private void savebuttrun() {
		String sql = "";
		String jel = "";	
		DefaultTableModel d1 = (DefaultTableModel) atable.getModel();
		String fname = txfname.getText();
		String lname = txlname.getText();
		String nickname = txnickname.getText();
		String email = txemail.getText();
		String phones = txphones.getText();
		String mobile = txmobile.getText();
		String country ="";
		if (cmbcountries.getSelectedItem() !=null) {
			country = (String) cmbcountries.getSelectedItem();
		}    
		String  city = txcity.getText();	
		String street = txstreet.getText();
		String postcode = txpostcode.getText();
		String mailbox = txmailbox.getText();
		String webpage = txwebpage.getText();
		String note = txanote.getText();
		
		 if (hh.zempty(lname) && hh.zempty(fname) == true) {
				return;
		  	}
		 if (rowid != "") {
				jel = "UP";
				sql = "update  addresses set fname= '" + fname + "', lname= '" + lname + "'," + "nickname = '"
						+ nickname + "' ,country = '" + country +  "', city = '" + city + "', street ='" + street
						+ "', mobile='" +mobile + "',  phones= '" + phones + "', email= '" + email + "', postcode ='" + postcode
						+ "', mailbox= '" + mailbox +"', webpage ='" + webpage +"', note='" + note +"' where aid = " + rowid;
			} else {
				sql = "insert into addresses (fname, lname, nickname, country, city, street, mobile, phones,"
						+ " email, postcode, mailbox, webpage, note) " + "values ('"
						+ fname + "','" + lname  +  "','" + nickname + "','" + country + "','" + city + "','"+street +"','"+
						mobile+ "','"+ phones + "','" + email +"','"+postcode+"','"+mailbox +"','"+webpage+"','"+
						note +"')";
			}
			try {
				int flag = dh.Insupdel(sql);
				if (flag == 1) {
					hh.ztmessage("Success", "Message");				
					if (rowid == "") {
						int myid = dd.table_maxid("SELECT MAX(aid) AS max_id FROM addresses");
						d1.insertRow(d1.getRowCount(), new Object[] { myid, fname, lname, nickname, mobile,
								email });
				       hh.gotolastrow(atable);
						 rowid = hh.itos(myid);
				        dd.groupsave(rowid, gbuttons);				  
					} else {					
						d1.setValueAt(fname, myrow, 1);
						d1.setValueAt(lname, myrow, 2);
						d1.setValueAt(nickname, myrow, 3);
						d1.setValueAt(mobile, myrow, 4);
						d1.setValueAt(email, myrow, 5);	
						dd.groupsave(rowid, gbuttons);						
					}
				} else {
					JOptionPane.showMessageDialog(null, "sql error !");
				}
			} catch (Exception e) {
				System.err.println("SQLException: " + e.getMessage());
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "sql insert hiba");
			}
		
			moredata(rowid, 1);
			clearFields();
			cards.show(cardPanel, "tabla");		
	}
	
	private void data_delete() {
		int sIndex = atable.getSelectedRow();
		if (sIndex < 0) {
			return;
		}
		DefaultTableModel d1 = (DefaultTableModel) atable.getModel();
		String aid = d1.getValueAt(sIndex, 0).toString();
		if (aid.equals("")) {
			return;
		}	
		int a = JOptionPane.showConfirmDialog(null, "Do you really want to delete ?");
		if (a == JOptionPane.YES_OPTION) {
	   clearlabels();
		dd.rtable_delete(atable, aid);	
		}
	}

	private void clearFields() {
	txfname.setText("");
	txlname.setText("");
	txnickname.setText("");
	txphones.setText("");
	txmobile.setText("");
	txcity.setText("");
	txstreet.setText("");
	txpostcode.setText("");
	txmailbox.setText("");
	txwebpage.setText("");
	txemail.setText("");
	cmbcountries.setSelectedItem("");
	txanote.setText("");
	rowid = "";
	myrow = 0;
	for (int i = 0; i < gbuttons.length; i++) {	
		gbuttons[i].setSelected(false);
	}
	}
	private void clearlabels() {
		lbtcountry.setText("");
		lbtcity.setText("");
		lbtstreet.setText("");
		lbtpostcode.setText("");
		lbtwebpage.setText("");
		ttanote.setText("");
		 txagroup.setText("");
	}

	public void actionPerformed(ActionEvent e) {
		JRadioButton button = (JRadioButton) e.getSource();
		String gid = button.getName();
		String name = button.getText();
//		if (button.isSelected() == true) {
//			System.out.println(gid);
//		} else {
//		System.out.println("Egyéb");
//		}
	}

	private void sqlgyart() {
		String sql = "";
		String ss = txsearch.getText().trim().toLowerCase();	
		String scmbtxt = String.valueOf(cmbsearch.getSelectedItem());
		if (scmbtxt =="Name") {
			sql = "lower(lname) LIKE '%" + ss + "%' or lower(fname) LIKE '%" + ss + "%' order by lname "
					+ " COLLATE NOCASE ASC";
		} else {
			sql = "mobile LIKE '%" + ss + "%' order by mobile COLLATE NOCASE ASC";
		}
		dd.atable_update(atable,sql,"");
	}
	private void sqlfilter() {
	    String scmbtxt = String.valueOf(cmbgroups.getSelectedItem());		
	    if (hh.zempty(scmbtxt)) {
	    	dd.atable_update(atable, "","");
	    	return;
	    }
		int igid  = (int) ((Groups) cmbgroups.getSelectedItem()).getGid();
		String sql = "select  a.aid, a.fname, a.lname, a.nickname, a.mobile, a.email from addresses a "
       + "  join choosedgroup c on a.aid = c.aid where c.gid="+ igid;
		dd.atable_update(atable,sql, "F");
	}

	public static void main(String args[]) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Addressbook adr = new Addressbook();
					adr.setSize(1230, 680);
					adr.setLayout(null);
					adr.setLocationRelativeTo(null);
					adr.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public JTextField cTextField(int hossz) {
		JTextField textField = new JTextField(hossz);
		textField.setFont(hh.textf);
		textField.setBorder(hh.borderf);
		textField.setBackground(hh.feher);
		textField.setPreferredSize(new Dimension(250, 30));
		textField.setCaretColor(Color.RED);
		textField.putClientProperty("caretAspectRatio", 0.1);
		// textField.setHorizontalAlignment(JTextField.RIGHT)
		// textField.addFocusListener(dFocusListener);
		textField.setText("");
		textField.setDisabledTextColor(Color.magenta);
		return textField;
	}
	
	

	JLabel lbheader, lbeheader, lbsearch, lbgroups;
	JLabel lbtcountry, lbtcity, lbtpostcode, lbtstreet, lbtgroup, lbtnote, lbtwebpage;
	JLabel lbccountry, lbccity, lbcpostcode, lbcstreet, lbcgroup, lbcnote, lbcwebpage;
	JLabel lbecountry, lbecity, lbepostcode, lbestreet, lbegroup, lbenote, lbewebpage;
	JLabel lbefname, lbelname, lbenickname, lbephones, lbemobile, lbemailbox, lbeemail;
	JTable atable;
	JScrollPane aPane, jsp, jspn, jspe;
	JTextField txfname, txlname, txnickname, txphones, txmobile, txcity, txstreet, txpostcode, txmailbox, txwebpage,
			txemail, txsearch;
	JTextArea txanote, ttanote, txagroup;
	JPanel mPanel, lPanel, rPanel;
	DefaultTableModel model;
	JButton btnsave, btncancel, btndelete, btnclear, btnsearch, btnnew, btnupdate, btnfilter, btnclearf;
	JComboBox cmbcountries, cmbsearch, cmbgroups;
	JPanel cardPanel, tPanel, ePanel, gPanel;
	CardLayout cards;

}
