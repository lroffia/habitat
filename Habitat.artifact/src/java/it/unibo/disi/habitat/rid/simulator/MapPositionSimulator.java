package it.unibo.disi.habitat.rid.simulator;

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

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import arces.unibo.SEPA.application.ApplicationProfile;
import it.unibo.disi.habitat.ai.entities.Role;

public class MapPositionSimulator {

	private static int posX = -1;
	private static int posY = -1;
	private static MapCanvas canvas;
	
	private JFrame frmHabitatSimulatore;
	private JTextField txtMmlarcesuniboit;
	private JTextField textField_PORT;
	private JTextField txtHabitante;
	private JButton btnNewButton;
	
	private UserIDManager userIDs = null;
	private PositionUpdater positionUpdater = null;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MapPositionSimulator window = new MapPositionSimulator();
					window.frmHabitatSimulatore.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MapPositionSimulator() {
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
				if (positionUpdater != null) positionUpdater.leave();
				if (userIDs != null) {
					userIDs.unsubscribe();
					userIDs.leave();
				}
			}
		});
		frmHabitatSimulatore.setResizable(false);
		frmHabitatSimulatore.setTitle("HABITAT - Simulatore di posizione");
		frmHabitatSimulatore.setBounds(100, 100, 670, 650);
		frmHabitatSimulatore.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//JPanel panel_Map = new JPanel();
		//frmHabitatSimulatore.getContentPane().add(panel_Map, BorderLayout.CENTER);
		
		JPanel panel_Args = new JPanel();
		frmHabitatSimulatore.getContentPane().add(panel_Args, BorderLayout.NORTH);
		
		JLabel lblIp = new JLabel("IP");
		panel_Args.add(lblIp);
		
		txtMmlarcesuniboit = new JTextField();
		txtMmlarcesuniboit.setText("localhost"); //txtMmlarcesuniboit.setText("mml.arces.unibo.it");
		panel_Args.add(txtMmlarcesuniboit);
		txtMmlarcesuniboit.setColumns(10);
		
		JLabel lblPort = new JLabel("PORT");
		panel_Args.add(lblPort);
		
		textField_PORT = new JTextField();
		textField_PORT.setText("10123");
		panel_Args.add(textField_PORT);
		textField_PORT.setColumns(10);
		
		JLabel lblIdUtente = new JLabel("ID UTENTE");
		panel_Args.add(lblIdUtente);
		
		txtHabitante = new JTextField();
		txtHabitante.setText("Habitante");
		panel_Args.add(txtHabitante);
		txtHabitante.setColumns(10);
		
		btnNewButton = new JButton("Go!");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ApplicationProfile appProfile = new ApplicationProfile();
				appProfile.load("Habitat.sap");
				txtMmlarcesuniboit.setText(appProfile.getParameters().getUrl());
				userIDs = new UserIDManager(appProfile);
				if (!userIDs.join()) return;
				if (userIDs.subscribe(null) == null) return;
				String id = userIDs.newID(txtHabitante.getText(), Role.OPERATOR);
				//String id = userIDs.newID(txtHabitante.getText()); //ORIGINAL
				
				positionUpdater = new PositionUpdater(appProfile,id);
				if (!positionUpdater.join()) return;
				
				btnNewButton.setEnabled(false);
				txtMmlarcesuniboit.setEnabled(false);
				textField_PORT.setEnabled(false);
				txtHabitante.setEnabled(false);
			}
		});
		panel_Args.add(btnNewButton);
		
		BufferedImage map = null;
		try {
			map = ImageIO.read(new File(getClass().getResource("/ArcesMap.bmp").getFile()));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		canvas = new MapCanvas();
		canvas.setImage(map);
		canvas.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				posX = e.getX();
				posY = e.getY();
				canvas.repaint();
				positionUpdater.updatePosition(posX, posY);
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
			
			if (posX != -1) {
				g.setColor(Color.RED);
				g.drawString(String.format("(%d,%d)", posX, posY), posX, posY);
				g.drawOval(posX, posY, 5, 5);
			}
		}
	}
}
