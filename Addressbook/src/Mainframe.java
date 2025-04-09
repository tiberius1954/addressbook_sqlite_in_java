import java.awt.*;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.Timer;
import Classes.Grlib;
import Classes.Hhelper;

public class Mainframe {
	static JFrame Main; 
	int x = 800;
	Color sszin = new Color(255, 255, 51);
	Font font1 = new java.awt.Font("Lucida Handwriting", 1, 50);
	Color dblue = new Color(52, 72, 95);
	Color yellow = new Color(254, 196, 1);
	Color sblue = new Color(71, 80, 111);
	Hhelper hh = new Hhelper();
	Grlib gr = new Grlib();


	void init(){
		Main.getContentPane().setBackground(Color.yellow);
		hh.iconhere(Main);
		
	
		JPanel panel = new JPanel(null);
		panel.setBounds(250, 75,500,350);
	//	panel.setBorder(BorderFactory.createLineBorder(Color.black));		
		panel.setBackground(Color.yellow);
		
	   JLabel label = new JLabel("ADDRESSBOOK");		
		label.setBounds(40, 20, 430,50);
		label.setFont(font1);		
		panel.add(label);		

		Main.add(panel);		
		
		btnaddr= gr.sbcs("Addressbook");
		btnaddr.setBounds(45, 140, 200, 35);
	    btnaddr.setBackground(hh.lpiros);
		panel.add(btnaddr);

		btnaddr.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				Addressbook  ad = new Addressbook();
			}
		});
		
		btngroup= gr.sbcs("Groups");
		btngroup.setBounds(255, 140, 200, 35);
	    btngroup.setBackground(hh.lpiros);
		panel.add(btngroup);

		btngroup.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
			Ggroups gg = new Ggroups();
			}
		});


	
	  Timer timer = new Timer(20, new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
               x = x- 20;
               panel.setLocation(250,x);
              if ( x <= 85) {
                  ((Timer) e.getSource()).stop();              
                //  mainKill();
              }
          }
      });
       timer.start();
  }
	public JButton xbutton(String string) {
		JButton bbutton = new JButton(string);
	   bbutton.setBorder(hh.myRaisedBorder);
		bbutton.setForeground(sblue);
		bbutton.setBackground(Color.yellow);
		bbutton.setFont(new Font("Tahoma", Font.BOLD, 18));
		bbutton.setPreferredSize(new Dimension(100, 30));
		bbutton.setMargin(new Insets(10, 10, 10, 10));
		bbutton.setFocusable(false);
		bbutton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		return bbutton;
	}




public void mainKill() {
    Timer timer = new Timer(500, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          //  System.exit(0);
        }
    });
   // timer.start();    
}
	

		public static void main(String[] args) 	{ 		
			Main = new JFrame(); 
			// create a object 
			Mainframe mm = new Mainframe(); 			
			Main.setSize(1000, 500);
			Main.setLayout(null);
			Main.setLocationRelativeTo(null);
			mm.init();
			Main.setVisible(true);		
			Main.addWindowListener((WindowListener) new WindowAdapter() {
				public void windowClosing(WindowEvent windowEvent) {
					int x, y, d;
					x = 1000;
					y = 500;
					d = 10;
					while (x > 0 && y > 0) {
						Main.setSize(x, y);
						x = x - 2 * d;
						y = y - d;
						Main.setVisible(true);
						try {
							Thread.sleep(10);
						} catch (Exception e) {
							System.out.println("Error:" + e);
						}
					}
					Main.dispose();
				}
			});

	}
JButton btngroup, btnaddr;
}
