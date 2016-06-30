package application;
import java.awt.EventQueue;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JFrame;

import kp.PositionAndLocationMonitor;
import kp.PositionAndLocationMonitor.PositionListener;
import kp.PositionUpdater;
import kp.UserIDManager;

import javax.swing.JTable;
import javax.swing.JLabel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import arces.unibo.tools.Logging;
import arces.unibo.tools.Logging.VERBOSITY;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RadarSimulator implements PositionListener {
	private static final String IP ="127.0.0.1";
	private static final int PORT = 10111;
	private static final String NAME = "Habitat";
	
	private static UserIDManager idManager = null;
	private static HashMap<String,RadarSimulatorThread> runningThreads = new HashMap<String,RadarSimulatorThread>();
	private static HashMap<String,Integer> tableRows = new HashMap<String,Integer>();
	private static PositionAndLocationMonitor positionMonitor = null;
	
	private String idTableHeader[] = new String[] {"URI", "ID","X","Y","LOCATION"};
	private DefaultTableModel idDM;
	
	private class RadarSimulatorThread extends Thread {
		boolean running = true;
		private PositionUpdater positionUpdater = null;
		private long delay = 1000;
		private int MAX_X = 10;
		private int MAX_Y = 10;
		
		public RadarSimulatorThread(String id) {
			positionUpdater = new PositionUpdater(id,IP,PORT,NAME);
		}
		
		public void stopRunning() {
			running = false;
		}
		
		public void run() {
			if(!positionUpdater.join()) return ;
			running = true;
			while(running){
				try {
					sleep(delay);
				} catch (InterruptedException e) {
					positionUpdater.leave();
					return;
				}
				
				int x = (int) (MAX_X * Math.random());
				int y = (int) (MAX_Y * Math.random());
				
				if (positionUpdater != null) positionUpdater.updatePosition(x, y);
			}
			positionUpdater.leave();
		}
		
	}
	
	private JFrame frmHabitatSimulatoreDi;
	private JTable table;
	private JTextField textField_ID;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RadarSimulator window = new RadarSimulator();
					window.frmHabitatSimulatoreDi.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public RadarSimulator() {
		initialize();
		
		idManager = new UserIDManager(IP,PORT,NAME);
		positionMonitor = new PositionAndLocationMonitor(IP,PORT,NAME,this);
		
		Logging.setVerbosityLevel(VERBOSITY.DEBUG);
		
		if (!idManager.join()) return;
		idManager.subscribe(null);
		
		if (!positionMonitor.join()) return;
		positionMonitor.subscribe(null);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmHabitatSimulatoreDi = new JFrame();
		frmHabitatSimulatoreDi.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				for(RadarSimulatorThread thread : runningThreads.values()) thread.stopRunning();
				idManager.unsubscribe();
				idManager.leave();
			}
		});
		frmHabitatSimulatoreDi.setTitle("Habitat - Simulatore di posizione");
		frmHabitatSimulatoreDi.setBounds(100, 100, 612, 440);
		frmHabitatSimulatoreDi.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{292, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{278, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		frmHabitatSimulatoreDi.getContentPane().setLayout(gridBagLayout);
		
		idDM = new DefaultTableModel(0, 0){
			/**
			 * 
			 */
			private static final long serialVersionUID = 6231730058550334611L;

			@Override
			public boolean isCellEditable(int row, int column)
		    {
		        return false;
		    }
		};
		idDM.setColumnIdentifiers(idTableHeader);
		
		table = new JTable(idDM);
		GridBagConstraints gbc_table = new GridBagConstraints();
		gbc_table.gridwidth = 3;
		gbc_table.fill = GridBagConstraints.BOTH;
		gbc_table.insets = new Insets(0, 0, 5, 5);
		gbc_table.gridx = 0;
		gbc_table.gridy = 0;
		frmHabitatSimulatoreDi.getContentPane().add(table, gbc_table);
		
		JLabel lblId = new JLabel("ID");
		GridBagConstraints gbc_lblId = new GridBagConstraints();
		gbc_lblId.anchor = GridBagConstraints.EAST;
		gbc_lblId.insets = new Insets(0, 0, 0, 5);
		gbc_lblId.gridx = 0;
		gbc_lblId.gridy = 1;
		frmHabitatSimulatoreDi.getContentPane().add(lblId, gbc_lblId);
		
		textField_ID = new JTextField();
		GridBagConstraints gbc_textField_ID = new GridBagConstraints();
		gbc_textField_ID.insets = new Insets(0, 0, 0, 5);
		gbc_textField_ID.fill = GridBagConstraints.BOTH;
		gbc_textField_ID.gridx = 1;
		gbc_textField_ID.gridy = 1;
		frmHabitatSimulatoreDi.getContentPane().add(textField_ID, gbc_textField_ID);
		textField_ID.setColumns(10);
		
		JButton btnAggiungi = new JButton("Aggiungi");
		btnAggiungi.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (idManager != null) idManager.newID(textField_ID.getText());
			}
		});

		GridBagConstraints gbc_btnAggiungi = new GridBagConstraints();
		gbc_btnAggiungi.gridx = 2;
		gbc_btnAggiungi.gridy = 1;
		frmHabitatSimulatoreDi.getContentPane().add(btnAggiungi, gbc_btnAggiungi);
	}

	@Override
	public void newPositionAndLocation(String id, String label, int x, int y,String location) {
		if (!runningThreads.containsKey(id)){
			RadarSimulatorThread thread = new RadarSimulatorThread(id);
			runningThreads.put(id, thread);
			tableRows.put(id, idDM.getRowCount());
			thread.start();
			
			Vector<Object> data = new Vector<Object>();
	        data.add(id);
	        data.add(label);
	        data.add(x);
	        data.add(y);
	        data.add(location);
			idDM.addRow(data);
		}
		else {
			int row = tableRows.get(id);
			idDM.setValueAt(x, row, 2);
			idDM.setValueAt(y, row, 3);
		}
		
	}

}
