package it.unibo.disi.ai.app.sim;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import arces.unibo.SEPA.application.ApplicationProfile;
import net.miginfocom.swing.MigLayout;

public class MapMultiUserPositionSimulator{

	private static MapCanvas canvas;
	HashMap<String,Position> positions = new HashMap<String,Position>();  //name and position
	UserRoleList urList = new UserRoleList();
	
	private class Position {
		private int x;
		private int y;
		//private Role r;

		public Position(int x, int y){
			this.x = x;
			this.y = y;
			//this.r = r;
		}

		public int getX(){return x;}
		public void setX(int x){this.x=x;}

		public int getY(){return y;}
		public void setY(int y){this.y=y;}

		//public Role getRole(){return r;}
	}


	private JFrame frmHabitatSimulatore;
	private JTextField textfield_IP;
	private JTextField textField_UPORT;
	private JButton btnNewButton;

	private JRadioButton rdbtnPat1;
	private JRadioButton rdbtnPat2;
	private JRadioButton rdbtnPat3;
	private JRadioButton rdbtnOp1;
	private JRadioButton rdbtnOp2;
	private ExtButtonGroup extButtonGroup = new ExtButtonGroup();
	String currentUser;

	@SuppressWarnings("serial")
	private class ExtButtonGroup extends ButtonGroup{
		public String getSelectedButtonText() {
			for (Enumeration<AbstractButton> buttons = this.getElements(); buttons.hasMoreElements();) {
				AbstractButton button = buttons.nextElement();
				if (button.isSelected()) {
					return button.getText();
				}
			}
			return null;
		}
	}

	UserIDManager userIdManager;
	//private HashMap<String,UserIDManager> userIDsOLD = new HashMap<String,UserIDManager>();  //name and id
	private HashMap<String,PositionUpdater> posUpdaters = new HashMap<String,PositionUpdater>();  //name and position
	private JTextField textField_SPORT;
	private JLabel lblSport;

	//private UserIDManager userIDs = null;
	//private PositionUpdater positionUpdater = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MapMultiUserPositionSimulator window = new MapMultiUserPositionSimulator();
					window.frmHabitatSimulatore.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/*public void run() {
		try {
			this.frmHabitatSimulatore.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
	

	/**
	 * Create the application.
	 */
	public MapMultiUserPositionSimulator() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		final ApplicationProfile appProfile = new ApplicationProfile();
		appProfile.load("Habitat.sap");
		
		frmHabitatSimulatore = new JFrame();
		frmHabitatSimulatore.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				for (String userName : posUpdaters.keySet()) {
					PositionUpdater positionUpdater = posUpdaters.get(userName);
					/*UserIDManager userID = userIDsOLD.get(userName);
					if (positionUpdater != null) positionUpdater.leave();
					if (userID != null) {
						userID.unsubscribe();
						userID.leave();
					}*/
					if (positionUpdater != null) positionUpdater.leave();
					if (userIdManager != null) {
						userIdManager.unsubscribe();
						userIdManager.leave();
					}
				}

			}
		});
		frmHabitatSimulatore.setResizable(false);
		frmHabitatSimulatore.setTitle("HABITAT - Simulatore di posizione");
		frmHabitatSimulatore.setBounds(600, 600, 818, 611);
		frmHabitatSimulatore.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//		JPanel panel_Map = new JPanel();
		//		frmHabitatSimulatore.getContentPane().add(panel_Map, BorderLayout.CENTER);
		//
		//		JPanel panel_Args = new JPanel();
		//		frmHabitatSimulatore.getContentPane().add(panel_Args, BorderLayout.NORTH);

		JPanel panel_Map = new JPanel();
		frmHabitatSimulatore.getContentPane().add(panel_Map, BorderLayout.WEST);

		JPanel panel_Args = new JPanel();
		frmHabitatSimulatore.getContentPane().add(panel_Args, BorderLayout.EAST);
		panel_Args.setLayout(new MigLayout("", "[100px,grow]", "[][][][][][][][][][][][][][][]"));

		JLabel lblIp = new JLabel("IP");
		panel_Args.add(lblIp, "flowx,cell 0 0,alignx left");
		
				textfield_IP = new JTextField();
				textfield_IP.setEnabled(false);
				textfield_IP.setText("localhost"); //txtMmlarcesuniboit.setText("mml.arces.unibo.it");
				panel_Args.add(textfield_IP, "cell 0 1,alignx left,aligny top");
				textfield_IP.setColumns(10);

		JLabel lblPort = new JLabel("UPORT");
		panel_Args.add(lblPort, "flowx,cell 0 2,alignx left");
		
				btnNewButton = new JButton("Go!");
				btnNewButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {	
						
						userIdManager = new UserIDManager(appProfile);
						if (!userIdManager.join()) return;
						if (userIdManager.subscribe(null) == null) return;
						for (Enumeration<AbstractButton> buttons = extButtonGroup.getElements(); buttons.hasMoreElements();) {
							AbstractButton button = buttons.nextElement();	    
							String id = userIdManager.newID(button.getText(), urList.get(button.getText()));
							PositionUpdater positionUpdater = new PositionUpdater(appProfile,id);
							if (!positionUpdater.join()) return;
							posUpdaters.put(button.getText(), positionUpdater);
							
							btnNewButton.setEnabled(false);
							textfield_IP.setEnabled(false);
							textField_UPORT.setEnabled(false);
						}
						/*for (Enumeration<AbstractButton> buttons = extButtonGroup.getElements(); buttons.hasMoreElements();) {
							AbstractButton button = buttons.nextElement();	    
							UserIDManager userID = new UserIDManager(txtMmlarcesuniboit.getText(),
									Integer.parseInt(textField_PORT.getText()),
									"Habitat");
							if (!userID.join()) return;
							if (userID.subscribe(null) == null) return;
							String id = userID.newID(button.getText(), urList.get(button.getText()));

							PositionUpdater positionUpdater = new PositionUpdater(id,
									txtMmlarcesuniboit.getText(),
									Integer.parseInt(textField_PORT.getText()),
									"Habitat");
							if (!positionUpdater.join()) return;
							//positionUpdater.updatePosition(-10, -10);  //to avoid position indicator in the first screen
							
							userIDsOLD.put(button.getText(), userID);
							posUpdaters.put(button.getText(), positionUpdater);
							
							btnNewButton.setEnabled(false);
							txtMmlarcesuniboit.setEnabled(false);
							textField_PORT.setEnabled(false);
							//txtHabitante.setEnabled(false);
						}*/
					}
				});
				
						textField_UPORT = new JTextField();
						textField_UPORT.setText(""+appProfile.getParameters().getUpdatePort());
						panel_Args.add(textField_UPORT, "cell 0 3,alignx left");
						textField_UPORT.setColumns(10);
						textField_UPORT.setEnabled(false);
				
				lblSport = new JLabel("SPORT");
				panel_Args.add(lblSport, "cell 0 4");
				
				textField_SPORT = new JTextField();
				textField_SPORT.setEnabled(false);
				textField_SPORT.setText(""+appProfile.getParameters().getSubscribePort());
				panel_Args.add(textField_SPORT, "cell 0 5,growx");
				textField_SPORT.setColumns(10);
				textField_SPORT.setText(""+appProfile.getParameters().getSubscribePort());
				panel_Args.add(btnNewButton, "cell 0 6,alignx center,growy");

		JLabel lblPatients = new JLabel("Patients");
		panel_Args.add(lblPatients, "cell 0 8");

		rdbtnPat1 = new JRadioButton("Mario");
		panel_Args.add(rdbtnPat1, "cell 0 9,alignx left,aligny top");
		//positions.put(rdbtnPat1.getText(), new Position(-1,-1, Role.PATIENT));
		positions.put(rdbtnPat1.getText(), new Position(-1,-1));
		extButtonGroup.add(rdbtnPat1);

		rdbtnPat2 = new JRadioButton("Piero");
		panel_Args.add(rdbtnPat2, "cell 0 10");
		positions.put(rdbtnPat2.getText(), new Position(-1,-1));
		extButtonGroup.add(rdbtnPat2);

		rdbtnPat3 = new JRadioButton("Adele");
		panel_Args.add(rdbtnPat3, "cell 0 11");
		positions.put(rdbtnPat3.getText(), new Position(-1,-1));
		extButtonGroup.add(rdbtnPat3);
		
		JLabel lblOperators = new JLabel("Operators");
		panel_Args.add(lblOperators, "cell 0 12");

		rdbtnOp1 = new JRadioButton("Luca");
		panel_Args.add(rdbtnOp1, "cell 0 13");
		positions.put(rdbtnOp1.getText(), new Position(-1,-1));
		extButtonGroup.add(rdbtnOp1);

		rdbtnOp2 = new JRadioButton("Alice");
		panel_Args.add(rdbtnOp2, "cell 0 14");
		positions.put(rdbtnOp2.getText(), new Position(-1,-1));
		extButtonGroup.add(rdbtnOp2);

		textfield_IP.setText(appProfile.getParameters().getUrl());
		textField_UPORT.setText(""+appProfile.getParameters().getUpdatePort());
		textField_SPORT.setText(""+appProfile.getParameters().getSubscribePort());
		BufferedImage map = null;
		try {
			map = ImageIO.read(new File(getClass().getResource("/resources/ArcesMap.bmp").getFile()));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		canvas = new MapCanvas();
		canvas.setImage(map);
		canvas.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String userName = extButtonGroup.getSelectedButtonText();
				positions.get(userName).setX(e.getX());
				positions.get(userName).setY(e.getY());
				//posX = e.getX();
				//posY = e.getY();
				canvas.repaint();
				posUpdaters.get(userName).updatePosition(e.getX(), e.getY());
			}
		});
		frmHabitatSimulatore.getContentPane().add(canvas);	
	}

	private class MapCanvas extends Canvas {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		BufferedImage img;
		

		public void setImage(BufferedImage img){
			this.img = img;
		}

		@Override
		public void paint(Graphics g){
			super.paint(g);
			g.drawImage(img,0,0,null);
			
			for (String userName : positions.keySet()) {
				int posX = positions.get(userName).getX();
				int posY = positions.get(userName).getY();
				//Role role = urList.get(userName); //Role role = positions.get(userName).getRole();
				if (posX != -1) {
					//if (role == Role.PATIENT) g.setColor(Color.BLUE) ;
					//else 
						g.setColor(Color.BLACK) ;
					g.drawString(String.format("%s (%d,%d)", userName, posX, posY), posX, posY);
					g.drawOval(posX, posY, 5, 5);
				}
			}
		}
	}
}
