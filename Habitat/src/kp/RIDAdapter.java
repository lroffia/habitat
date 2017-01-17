package kp;

import java.io.IOException;

import arces.unibo.SEPA.application.Producer;
import arces.unibo.SEPA.application.ApplicationProfile;
import labid.comm.SerialStream;

public class RIDAdapter extends Producer {
	private SerialStream port = new SerialStream();
	
	public RIDAdapter(ApplicationProfile appProfile,String updateID) {
		super(appProfile,updateID);
		// TODO Auto-generated constructor stub
		try {
			port.Open("/dev/tty.usbserial-FTHC1DUG", 115200);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		SerialStream port = new SerialStream();
		// TODO Auto-generated constructor stub
		
		try {
			port.Open("/dev/tty.usbserial-FTHC1DUG", 115200);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		byte[] reset ={'+','\n'};
		try {
			port.Write(reset);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		byte[] cmd ={'<','\n'};
		try {
			port.Write(cmd);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		
		byte[] input = new byte[100];
		int nRead = 0;
		try {
			nRead = port.Read(input,0,6); //2 + 1 + 2*Nid
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.printf("[");
		for (int index=0;index<nRead;index++){
			System.out.printf("%d ",input[index]);
		}
		System.out.printf("]\n");
		
		long start = System.currentTimeMillis();
		for (int i=0; i < 40; i++) {
			try {
				port.Write(cmd);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				nRead = port.Read(input,0,3); //2xnID + 1
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			/*try {
				Thread.sleep(10);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			*/
			
			/*System.out.printf("%d [",i);
			for (int index=0;index<nRead;index++){
				System.out.printf("%d ",input[index]);
			}
			System.out.printf("]\n");*/
		}
	
		long stop = System.currentTimeMillis();
		
		System.out.printf("Tempo: %d ms", stop-start);
		
		try {
			port.Close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
