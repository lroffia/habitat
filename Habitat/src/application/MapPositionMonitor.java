package application;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

import kp.PositionAndLocationMonitor;
import kp.PositionAndLocationMonitor.PositionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MapPositionMonitor {
	static MapCanvas canvas;
	
	private JFrame frmHabitatSimulatore;
	private JTextField txtMmlarcesuniboit;
	private JTextField textField_PORT;
	private JButton btnNewButton;
	
	private PositionAndLocationMonitor monitor;
	
	private class MapPositionListener implements PositionListener {

		@Override
		public void newPositionAndLocation(String id, String label, int x, int y,String location) {
			canvas.setPositionAndLocation(id, label, x, y,location);
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
		frmHabitatSimulatore.setBounds(100, 100, 670, 650);
		frmHabitatSimulatore.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel_Map = new JPanel();
		frmHabitatSimulatore.getContentPane().add(panel_Map, BorderLayout.CENTER);
		
		JPanel panel_Args = new JPanel();
		frmHabitatSimulatore.getContentPane().add(panel_Args, BorderLayout.NORTH);
		
		JLabel lblIp = new JLabel("IP");
		panel_Args.add(lblIp);
		
		txtMmlarcesuniboit = new JTextField();
		txtMmlarcesuniboit.setText("mml.arces.unibo.it");
		panel_Args.add(txtMmlarcesuniboit);
		txtMmlarcesuniboit.setColumns(10);
		
		JLabel lblPort = new JLabel("PORT");
		panel_Args.add(lblPort);
		
		textField_PORT = new JTextField();
		textField_PORT.setText("77123");
		panel_Args.add(textField_PORT);
		textField_PORT.setColumns(10);
		
		btnNewButton = new JButton("Go!");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				MapPositionListener listener = new MapPositionListener();
				monitor = new PositionAndLocationMonitor(txtMmlarcesuniboit.getText(),
						Integer.parseInt(textField_PORT.getText()),"Habitat",listener);
				if(!monitor.join()) return;
				if (monitor.subscribe(null) == null) return;
				
				btnNewButton.setEnabled(false);
				txtMmlarcesuniboit.setEnabled(false);
				textField_PORT.setEnabled(false);
				
			}
		});
		panel_Args.add(btnNewButton);
		
		BufferedImage map = null;
		try {
			map = ImageIO.read(new File("ArcesMap.bmp"));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		canvas = new MapCanvas();
		canvas.setImage(map);
		frmHabitatSimulatore.getContentPane().add(canvas);	
	}
	
	private class MapCanvas extends Canvas {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		BufferedImage img;
		HashMap<String,PositionAndLocation> positions = new HashMap<String,PositionAndLocation>();
		
		private class PositionAndLocation {
			private String label;
			private int x;
			private int y;
			private String location;
			
			public PositionAndLocation(String label, int x, int y,String location){
				this.label = label;
				this.x = x;
				this.y = y;
				this.location = location;
			}
			
			public String getLabel() {
				return label;
			}
			
			public int getX(){
				return x;
			}
			
			public int getY(){
				return y;
			}
			
			public String getLocation(){
				return location;
			}
		}
		
		public void setImage(BufferedImage img){
			this.img = img;
		}
		
		public void setPositionAndLocation(String id, String label, int x, int y,String location){
			positions.put(id, new PositionAndLocation(label,x,y,location));
		}
		
		@Override
		public void paint(Graphics g){
			super.paint(g);
			g.drawImage(img,0,0,null);
			
			for (PositionAndLocation pos : positions.values()) {
				g.drawString(
						String.format("%s (%s)", pos.getLabel(),pos.getLocation()),pos.getX(), pos.getY());
				g.drawOval(pos.getX(), pos.getY(), 5, 5);	
			}
		}
	}
}
