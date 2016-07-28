package ai;

import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class AIModuleApplication {

	private AIModule ai;
	private JFrame frame;

	public AIModuleApplication()
	{
		ai = new AIModule();
		
		if (ai.join() == false)
		{
			System.out.println("JOIN FAILED");
			System.exit(0);
		}
		
		if (ai.subscribe(null) == null)
		{
			System.out.println("SUBSCRIBE FAILED");
			System.exit(0);
		}
		
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
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AIModuleApplication aiModuleApplication = new AIModuleApplication();
					aiModuleApplication.frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
