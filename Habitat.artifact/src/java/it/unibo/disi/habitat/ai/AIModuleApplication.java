package it.unibo.disi.habitat.ai;

import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import arces.unibo.SEPA.application.ApplicationProfile;

public class AIModuleApplication {

	private AIModule ai;
	private JFrame frame;

	public AIModuleApplication()
	{
		ApplicationProfile appProfile = new ApplicationProfile();
		appProfile.load("Habitat.sap");
		ai = new AIModule(newAlarmIDManager(appProfile),appProfile);
		frame = new JFrame();
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (ai != null) {
					ai.unsubscribe();
					ai.leave();
					ai.terminate();
				}
			}
		});
		frame.setResizable(false);
		frame.setTitle("HABITAT - AI Module");
		frame.setBounds(300, 300, 200, 100);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        System.out.println("Ai Going to join....");
		if (ai.join() == false){
			System.out.println("JOIN FAILED");
			System.exit(0);
		}
        System.out.println("Ai JOINED");
		if (ai.subscribe(null) == null){
			System.out.println("SUBSCRIBE FAILED");
			System.exit(0);
		}
		//tester();
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					/*AlarmMonitor alarmMonitor = new AlarmMonitor("localhost", 10123, "Habitat", null);
					System.out.println("AlarmMonitor going to join...");
					if(!alarmMonitor.join()) return;
					System.out.println("AlarmMonitor JOINED");
					if (alarmMonitor.subscribe(null) == null) return;
					*/
					
					AIModuleApplication aiModuleApplication = new AIModuleApplication();
					System.out.println("setting visible....");
					aiModuleApplication.frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public AlarmIDManager newAlarmIDManager(ApplicationProfile appProfile){
		AlarmIDManager alarmIDs = new AlarmIDManager(appProfile);
		if (!alarmIDs.join()) return null;
		else System.out.println("AlarmIDManager joined");
		
		return alarmIDs;
		//String alarmId=alarmIDs.newAlarmID("hbt:ID_6f3e12aa-3267-404d-a6d5-708f5272f9d7", 
		//		"hbt:ID_6f3e12aa-3267-404d-a6d5-708f5272f9d8", "hbt:Studio301", "The patient is in danger");  //generates the Id for the alarm and sends it on the SIB
	}
	/*
	public void run() {
		try {
			this.frame.setVisible(true);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
}
