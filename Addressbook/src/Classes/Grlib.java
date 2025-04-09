package Classes;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import javax.imageio.ImageIO;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class Grlib {
	Border borderw = BorderFactory.createLineBorder(Color.WHITE, 1);
	JButton exitbutton, minbutton, maxbutton, btnnew, btnupdate, btndelete;
	Font textf3 =new Font(Font.MONOSPACED, Font.BOLD, 18);
	private Color lpiros = new Color(255, 102, 75);

	public JButton sbcs(String string) {
		JButton bbutton = new SGomb(string);
		return bbutton;
	}

	class SGomb extends JButton {
		private boolean mouseOver = false;
		private boolean mousePressed = false;
		private String nev;
		SGomb(String label) {
			super(label);
			setOpaque(false);
			setContentAreaFilled(false);
			setBorderPainted(false);
			setFocusPainted(false);
			this.setFont(new Font("Arial", Font.BOLD, 16));
			MouseAdapter mouseListener = new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent me) {
					if (contains(me.getX(), me.getY())) {
						mousePressed = true;
						repaint();
					}
				}

				@Override
				public void mouseReleased(MouseEvent me) {
					mousePressed = false;
					repaint();
				}

				@Override
				public void mouseExited(MouseEvent me) {
					mouseOver = false;
					mousePressed = false;
					repaint();
				}

				@Override
				public void mouseMoved(MouseEvent me) {
					mouseOver = contains(me.getX(), me.getY());
					repaint();
				}
			};
			addMouseListener(mouseListener);
			addMouseMotionListener(mouseListener);
		}

		public void setNev(String nev) {
			this.nev = nev;
		}

		public String getNev() {
			return nev;
		}
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			int width = getWidth();
			int height = getHeight();
			int shadowGap = 5;
			int shadowOffset = 4;
			int shadowAlpha = 150;
			Color shadowColor = Color.black;
			Color shadowColorA = new Color(shadowColor.getRed(), shadowColor.getGreen(), shadowColor.getBlue(),
					shadowAlpha);
			int strokeSize = 1;
			Dimension arcs = new Dimension(20, 20);
			g2.setColor(shadowColorA);
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			if (mousePressed) {
				g2.setColor(Color.LIGHT_GRAY);
			} else if (mouseOver) {
				g2.setColor(Color.BLUE);
			} else {
				g2.setColor(Color.black);
			}
			g2.fillRect(shadowOffset, shadowOffset, width - strokeSize - shadowOffset,
					height - strokeSize - shadowOffset);
			g2.setColor(getBackground());
			g2.fillRect(0, 0, width - shadowGap, height - shadowGap);
			g2.setColor(getForeground());
			g2.setStroke(new BasicStroke(strokeSize));
			g2.drawRect(0, 0, width - shadowGap, height - shadowGap);
			g2.setStroke(new BasicStroke());
//		g2.setColor(Color.green);
//		g2.drawString(getText(), 60, 20);
			super.paintComponent(g);
			g2.dispose();
		}
	}

	public JTextField gTextField(int hossz) {
		JTextField textField = new JTextField(hossz);
		textField.setMargin(new Insets(2, 4, 2, 4));
		textField.setBorder(BorderFactory.createMatteBorder(1, 1, 4, 4, Color.DARK_GRAY));
		textField.setFont(new Font("Arial", Font.BOLD, 16));
		// textField.setBackground(ph.feher);
		// textField.setBackground(vvkek);
		textField.setPreferredSize(new Dimension(250, 30));
		textField.setCaretColor(Color.RED);
		textField.putClientProperty("caretAspectRatio", 0.2);
		// textField.setHorizontalAlignment(JTextField.RIGHT)
		// textField.addFocusListener(dFocusListener);
		return textField;
	}
	public JTextArea gTextArea() {
		JTextArea txarea= new JTextArea();
		txarea.setMargin(new Insets(2, 4, 2, 4));
		txarea.setBorder(BorderFactory.createMatteBorder(1, 1, 4, 4, Color.DARK_GRAY));
		txarea.setFont(textf3);
		// textField.setBackground(ph.feher);
		// textField.setBackground(vvkek);
	//	txarea.setPreferredSize(new Dimension(250, 90));
		txarea.setCaretColor(Color.RED);
		txarea.putClientProperty("caretAspectRatio", 0.1);
		txarea.setLineWrap(true);
		txarea.setWrapStyleWord(true);

		// textField.setHorizontalAlignment(JTextField.RIGHT)
		// textField.addFocusListener(dFocusListener);
		return txarea;
	}


	public JPasswordField gpasstField(int hossz) {
		JPasswordField ptextField = new JPasswordField(hossz);
		ptextField.setMargin(new Insets(2, 4, 2, 4));
		ptextField.setBorder(BorderFactory.createMatteBorder(1, 1, 4, 4, Color.DARK_GRAY));
		ptextField.setFont(new Font("Arial", Font.BOLD, 16));
		// textField.setBackground(ph.feher);
		// textField.setBackground(vvkek);
		ptextField.setPreferredSize(new Dimension(250, 30));
		ptextField.setCaretColor(Color.RED);
		ptextField.putClientProperty("caretAspectRatio", 0.2);
		// textField.setHorizontalAlignment(JTextField.RIGHT)
		// textField.addFocusListener(dFocusListener);
		return ptextField;
	}
	public JComboBox grcombo() {
		JComboBox ccombo = new JComboBox();		
		ccombo.setBackground(Color.WHITE);
		ccombo.setPreferredSize(new Dimension(250, 30));
		ccombo.setCursor(new Cursor(Cursor.HAND_CURSOR));
		ccombo.setFont(new java.awt.Font("Tahoma", 1, 16));
		ccombo.setBorder(BorderFactory.createMatteBorder(1, 1, 3, 3, Color.DARK_GRAY));
	 	ccombo.setSelectedItem("");
		return ccombo;
	}

	public JPanel makeexitpanel(JFrame frame) {		
		JPanel mpanel = new JPanel(null);
		mpanel.setSize(100, 35);
		exitbutton = xbutton("X");
		exitbutton.setBounds(65, 5, 25, 25);
		mpanel.add(exitbutton);
		exitbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {			
				 //  frame.dispose();		
				   frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));				
			}
		});

		minbutton = xbutton("");
		minbutton.setBounds(5, 5, 25, 25);
		mpanel.add(minbutton);
		try {
			Image imgm = Toolkit.getDefaultToolkit().createImage(this.getClass().getResource("/images/min.png"));
			minbutton.setIcon(new ImageIcon(imgm));
		} catch (Exception ex) {
			System.out.println(ex);
		}
		minbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.setExtendedState(JFrame.ICONIFIED);
			}
		});

		maxbutton = xbutton("");
		try {
			maxbutton.setBounds(35, 5, 25, 25);
			try {
				Image img = Toolkit.getDefaultToolkit().createImage(this.getClass().getResource("/images/max.png"));
				maxbutton.setIcon(new ImageIcon(img));
			} catch (Exception ex) {
				System.out.println(ex);
			}

		} catch (Exception ex) {
			System.out.println(ex);
		}
		mpanel.add(maxbutton);

		maxbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (frame.getExtendedState() == JFrame.NORMAL)
					frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
				else
					frame.setExtendedState(JFrame.NORMAL);
			}
		});
		return mpanel;
	}
	
	public JPanel makenewpanel(JFrame frame) {		
		JPanel mpanel = new JPanel(null);
		mpanel.setSize(200, 150);
		btnnew = sbcs("New");
		btnnew.setBounds(10, 10, 100, 30);
		btnnew.setForeground(Color.black);
		btnnew.setBackground(lpiros);
		mpanel.add(btnnew);	
		btnnew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {		
				

							
			}
		});
		btnupdate = sbcs("Update");
		btnupdate.setBounds(10, 50, 100, 30);
		btnupdate.setForeground(Color.black);
		btnupdate.setBackground(Color.green);
		mpanel.add(btnupdate);
		btnupdate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				
				
			}
		});
		btndelete = sbcs("Delete");	
		btndelete.setBounds(10, 90, 100, 30);
		btndelete.setForeground(Color.black);
		btndelete.setBackground(Color.yellow);

		mpanel.add(btndelete);
		btndelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				//data_delete();
			}
		});

		return mpanel;
	}


	public JButton xbutton(String string) {
		JButton bbutton = new JButton(string);
		bbutton.setBackground(new java.awt.Color(0, 0, 0));
		bbutton.setFont(new java.awt.Font("Tahoma", 1, 16));
		bbutton.setForeground(new java.awt.Color(255, 255, 255));
		bbutton.setPreferredSize(new Dimension(25, 25));
		bbutton.setMargin(new Insets(2, 2, 2, 2));
		bbutton.setBorder(borderw);
		bbutton.setFocusable(false);
		return bbutton;
	}

}
