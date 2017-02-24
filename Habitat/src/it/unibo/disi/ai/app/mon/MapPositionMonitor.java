package it.unibo.disi.ai.app.mon;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import arces.unibo.SEPA.application.ApplicationProfile;
import it.unibo.disi.ai.app.mon.PositionAndLocationMonitor.PositionListener;
import it.unibo.disi.ai.app.opapp.AlarmMonitor;
import it.unibo.disi.ai.app.opapp.AlarmMonitor.AlarmListener;
import it.unibo.disi.ai.entities.AlarmSeverity;
import it.unibo.disi.ai.entities.PositionAndLocation;
import it.unibo.disi.ai.entities.Role;
import it.unibo.disi.ai.entities.User;
import it.unibo.disi.ai.entities.UserCache;

public class MapPositionMonitor implements Runnable{
	static MapCanvas canvas;

	private JFrame frmHabitatSimulatore;
	private JTextField txtMmlarcesuniboit;
	private JTextField textField_PORT;
	private JButton btnNewButton;

	private PositionAndLocationMonitor monitor;
	private AlarmMonitor alarmMonitor;

	private ApplicationProfile appProfile = new ApplicationProfile();
	private JLabel lblUport;
	private JTextField textField_UPORT;
	
	private class MapPositionListener implements PositionListener {
		//@Override
		public void newPositionAndLocation(String id, String label, Role role, int x, int y,String location) {
			canvas.setPositionAndLocation(id, label, role,x, y,location);
			canvas.repaint();
		}

	}
	private class AlarmPatientListener implements AlarmListener {

		public void newAlarm(String alarmId, String patLabel, String opLabel, //String locationURI,
				AlarmSeverity as, String detail,
				String answer) {
			canvas.setPatientAlarm(patLabel,as);
			canvas.repaint();
		}

	}
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MapPositionMonitor window = new MapPositionMonitor();
					window.frmHabitatSimulatore.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void run() {
		try {
			this.frmHabitatSimulatore.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the application.
	 */
	public MapPositionMonitor() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmHabitatSimulatore = new JFrame();
		frmHabitatSimulatore.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (monitor != null) {
					monitor.unsubscribe();
					monitor.leave();
				}
			}
		});
		frmHabitatSimulatore.setResizable(false);
		frmHabitatSimulatore.setTitle("HABITAT - Monitoraggio posizioni");
		frmHabitatSimulatore.setBounds(0, 0, 670, 650);
		frmHabitatSimulatore.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		appProfile.load("Habitat.sap");
		
		JPanel panel_Map = new JPanel();
		frmHabitatSimulatore.getContentPane().add(panel_Map, BorderLayout.CENTER);

		JPanel panel_Args = new JPanel();
		frmHabitatSimulatore.getContentPane().add(panel_Args, BorderLayout.NORTH);

		JLabel lblIp = new JLabel("IP");
		panel_Args.add(lblIp);

		txtMmlarcesuniboit = new JTextField();
		txtMmlarcesuniboit.setText(appProfile.getParameters().getUrl());//txtMmlarcesuniboit.setText("mml.arces.unibo.it");
		panel_Args.add(txtMmlarcesuniboit);
		txtMmlarcesuniboit.setColumns(10);
		txtMmlarcesuniboit.setEnabled(false);
		
		lblUport = new JLabel("UPORT");
		panel_Args.add(lblUport);
		
		textField_UPORT = new JTextField();
		panel_Args.add(textField_UPORT);
		textField_UPORT.setColumns(10);
		textField_UPORT.setText(""+appProfile.getParameters().getUpdatePort());
		textField_UPORT.setEnabled(false);
		
		JLabel lblPort = new JLabel("SPORT");
		panel_Args.add(lblPort);

		textField_PORT = new JTextField();
		textField_PORT.setText(""+appProfile.getParameters().getSubscribePort());
		panel_Args.add(textField_PORT);
		textField_PORT.setColumns(10);
		textField_PORT.setEnabled(false);
		
		btnNewButton = new JButton("Go!");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				MapPositionListener mpListener = new MapPositionListener();
				
				/*monitor = new PositionAndLocationMonitor(txtMmlarcesuniboit.getText(),
						Integer.parseInt(textField_PORT.getText()),"HabitatPosMonitor",mpListener);*/
				monitor = new PositionAndLocationMonitor(appProfile,mpListener);
				txtMmlarcesuniboit.setText(appProfile.getParameters().getUrl());
				if(!monitor.join()) return;
				if (monitor.subscribe(null) == null) return;

				AlarmListener aListener = new AlarmPatientListener();
				//((AlarmForOperatorListener)listener).opLabel=txtAlice.getText();
				alarmMonitor = new AlarmMonitor(appProfile, aListener);
				if(!alarmMonitor.join()) return;
				if (alarmMonitor.subscribe(null) == null) return;


				btnNewButton.setEnabled(false);
				txtMmlarcesuniboit.setEnabled(false);
				textField_PORT.setEnabled(false);

			}
		});
		panel_Args.add(btnNewButton);

		BufferedImage map = null;
		try {
			map = ImageIO.read(new File(getClass().getResource("/resources/ArcesMap.bmp").getFile()));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		canvas = new MapCanvas();
		canvas.setImage(map);
		frmHabitatSimulatore.getContentPane().add(canvas);	
	}

	@SuppressWarnings("serial")
	private class MapCanvas extends Canvas {

		BufferedImage img;
		//HashMap<String,PositionAndLocation> positions = new HashMap<String,PositionAndLocation>();
		UserCache userCache = new UserCache();
		HashMap<String,Color> alarmList = new HashMap<String, Color>();
		
		/*public void putLocationColor(String location, Color c){
			Color oldC = alarmLocationCache.get(location);
			if (oldC==null)
				alarmLocationCache.put(location,c);
			else {
				if (c.getRGB()<oldC.getRGB())
					alarmLocationCache.put(location,c);
			}
		}*/

		public void setImage(BufferedImage img){
			this.img = img;
		}

		public void setPositionAndLocation(String id, String label, Role role, int x, int y,String location){
			//positions.put(id, new PositionAndLocation(label,role, x,y,location));
			userCache.put(id, label, role, x, y, location);
		}

		public void setPatientAlarm(String userLabel, AlarmSeverity as){
			//positions.put(id, new PositionAndLocation(label,role, x,y,location));
			Color c=null;
			if(as==AlarmSeverity.YELLOW) {
				c=new Color(255,128,0); //orange... 4 visibility
			}
			else if (as==AlarmSeverity.RED) {
				c=new Color(255,0,0); //red 
			}
			alarmList.put(userLabel,c);
		}

		@Override
		public void paint(Graphics g){
			super.paint(g);
			g.drawImage(img,0,0,null);
			g.setColor(new Color(255,0,0,95));
			g.fillRect(19, 236, 80-19, 360-236);
			
			Font defaultFont = g.getFont();
			Font boldFont= new Font(defaultFont.getFontName(), Font.BOLD, defaultFont.getSize());
			/*if(!positions.isEmpty()){
				g.setColor(new Color(176,176,176,80));
				g.fillRect(0, 0, img.getWidth(), img.getHeight());
			}*/
			for (User u : userCache.values()) {
				PositionAndLocation pos = u.getCurrentPosition();
				if (  !(pos.getX()<=0 && pos.getY()<=0) ){
					g.setColor(new Color(255,255,153,95)); //light!
					
					if (pos.getLocation().equals("hbt:Studio304")){
						g.fillRect(113, 58, 294-113, 216-58);
					}else if (pos.getLocation().equals("hbt:Studio305")){
						g.fillRect(303, 58, 482-303, 216-58);
					}else if (pos.getLocation().equals("hbt:AntiWC306")){
						g.fillRect(490, 58, 646-490, 216-58);
					}else if (pos.getLocation().equals("hbt:Corridoio303")){
						g.fillRect(81, 236, 549-81, 360-236);
					}else if (pos.getLocation().equals("hbt:Exit")){
						g.fillRect(19, 236, 80-19, 360-236);
					}else if (pos.getLocation().equals("hbt:Studio302")){
						g.fillRect(211, 379, 389-211, 535-379);
					}else if (pos.getLocation().equals("hbt:Studio301")){
						g.fillRect(398, 382, 646-398, 535-382);
					}
					
					Color c = this.alarmList.get(u.getLabel());
					
					if (u.getRole() == Role.OPERATOR) {
						g.setFont(defaultFont);
						g.setColor(Color.BLACK);
					}else if ( u.getRole() == Role.PATIENT && c !=null){
						g.setFont(boldFont);
						g.setColor(c);
					}else{
						g.setFont(defaultFont);
						g.setColor(Color.BLUE) ;
					}
					
					g.drawString(String.format("%s (%s)", u.getLabel(),pos.getLocation()),pos.getX(), pos.getY());
					g.drawOval(pos.getX(), pos.getY(), 5, 5);
				}
			}

		}
	}
}
