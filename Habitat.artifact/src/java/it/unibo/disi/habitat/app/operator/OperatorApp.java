package it.unibo.disi.habitat.app.operator;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import arces.unibo.SEPA.application.ApplicationProfile;
import it.unibo.disi.habitat.ai.entities.AlarmSeverity;
import it.unibo.disi.habitat.app.operator.AlarmMonitor.AlarmListener;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;

public class OperatorApp implements Runnable {

	private JFrame frame;
	private JTabbedPane tabbedPane;

	private AlarmMonitor alarmMonitor;
	//private PositionAndLocationMonitor positionMonitor;
	private JTextField txtAlice;
	private JButton btnGo;

	private static String operatorName;
	private int tabCounter=0;

	private class AlarmForOperatorListener implements AlarmListener  {
		//private String operatorId;
		private String opLabel;

		private AlarmForOperatorListener(String opLabel){
			this.opLabel=opLabel;
		}

		//public void newAlarm(String alarmId, String patId, String opId, String locationURI, String detail,String answer) {
		public void newAlarm(String alarmId, String patLabel, String opLabel, AlarmSeverity as, String detail,
				String answer) {
			if(opLabel.equals(this.opLabel)){
				newTab( as, detail );
				//canvas.setPositionAndLocation(id, label, role,x, y,location);
				//canvas.repaint();
			}
		}

		/*public void newPositionAndLocation(String id, String label, Role role, int x, int y,String location) {
			if(label.equals(this.opLabel)){
				this.operatorId=id;
				System.out.println("My name is "+label+" and my id is:"+id);
			}
		}*/
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		if (args.length == 0) operatorName="Alice";
		else if(args[0].equals("")) operatorName="Alice";
		else operatorName=args[0];
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					OperatorApp window = new OperatorApp();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void run() {
		try {
			this.frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the application.
	 */
	public OperatorApp(String operatorName) {
		initialize();
	}

	private OperatorApp() {
		initialize();
	}

	private void newTab(AlarmSeverity as, String labelMessage ){
		Color color = null;
		if (as==AlarmSeverity.YELLOW) color=Color.YELLOW;
		else if (as==AlarmSeverity.RED) color = Color.RED;//new Color(255, 0, 0,50);
		
		JPanel internalSplitPane = new JPanel();
		//internalSplitPane.setBackground(color);
		tabbedPane.addTab(as.toString()+ " Alarm", null, internalSplitPane, null);
		tabbedPane.setBackgroundAt(tabCounter, color);
		
		JLabel lblNewLabel = new JLabel(labelMessage);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		internalSplitPane.add(lblNewLabel, BorderLayout.NORTH);
		
		JPanel panel1 = new JPanel();
		//panel1.setBackground(color);
		internalSplitPane.add(panel1, BorderLayout.SOUTH);
		
		JButton btnOk = new JButton("Ok");
		panel1.add(btnOk);
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// update the SIB with the answer
				tabbedPane.removeTabAt(tabbedPane.getSelectedIndex());
				tabCounter--;
				System.out.println("I'll manage this alarm");
			}
		});

		JButton btnCancel = new JButton("Cancel");
		panel1.add(btnCancel);
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// update the SIB with the answer
				tabbedPane.removeTabAt(tabbedPane.getSelectedIndex());
				tabCounter--;
				System.out.println("I won't manage this alarm");
			}
		});
		tabbedPane.setEnabled(true);
		tabbedPane.repaint();
		tabCounter++;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(675, 0, 387, 239);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Simulatore App Operatore");
		
		//JSplitPane splitPane = new JSplitPane();
		//splitPane.setResizeWeight(0.01);
		//splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		//frame.getContentPane().add(splitPane, BorderLayout.CENTER);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		//splitPane.setRightComponent(tabbedPane);
		frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		//splitPane.setLeftComponent(panel);
		frame.getContentPane().add(panel, BorderLayout.NORTH);
		
		txtAlice = new JTextField();
		txtAlice.setText(operatorName);
		panel.add(txtAlice);
		txtAlice.setColumns(10);

		btnGo = new JButton("Go!");
		btnGo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AlarmListener listener = new AlarmForOperatorListener(txtAlice.getText());
				//((AlarmForOperatorListener)listener).opLabel=txtAlice.getText();
				ApplicationProfile appProfile = new ApplicationProfile();
				appProfile.load("Habitat.sap");
				alarmMonitor = new AlarmMonitor(appProfile, listener);
				if(!alarmMonitor.join()) return;
				if (alarmMonitor.subscribe(null) == null) return;
				/*positionMonitor = new PositionAndLocationMonitor("localhost", 10123, "Alice", (PositionListener)listener);
				if(!positionMonitor.join()) return;
				if (positionMonitor.subscribe(null) == null) return;
				 */
				txtAlice.setEnabled(false);
				btnGo.setEnabled(false);
			}
		});
		panel.add(btnGo);

		/**TESTING SECTION**
		JPanel internalSplitPane = new JPanel();
		FlowLayout flowLayout = (FlowLayout) internalSplitPane.getLayout();
		internalSplitPane.setBackground(Color.ORANGE);
		tabbedPane.addTab("Alarm - YELLOW", null, internalSplitPane, null);

		JLabel lblNewLabel = new JLabel("A sample message");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		internalSplitPane.add(lblNewLabel, BorderLayout.NORTH);
		
		JPanel panel1 = new JPanel();
		panel1.setBackground(Color.ORANGE);
		internalSplitPane.add(panel1, BorderLayout.SOUTH);
		
		JButton btnOk = new JButton("Ok");
		panel1.add(btnOk);
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// update the SIB with the answer
				System.out.println("I'll manage this alarm");
			}
		});

		JButton btnCancel = new JButton("Cancel");
		panel1.add(btnCancel);
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// update the SIB with the answer
				System.out.println("I won't manage this alarm");
			}
		});
		tabbedPane.repaint();
		/*****END TESTING SECTION****/
	}

}
