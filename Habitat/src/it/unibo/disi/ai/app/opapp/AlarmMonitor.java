package it.unibo.disi.ai.app.opapp;

import arces.unibo.SEPA.application.ApplicationProfile;
import arces.unibo.SEPA.application.Consumer;
import arces.unibo.SEPA.commons.SPARQL.ARBindingsResults;
import arces.unibo.SEPA.commons.SPARQL.Bindings;
import arces.unibo.SEPA.commons.SPARQL.BindingsResults;

import it.unibo.disi.ai.entities.AlarmSeverity;

public class AlarmMonitor extends Consumer {
	public interface AlarmListener {
		public void newAlarm(String alarmId,String patLabel, String opLabel, //String locationURI, 
				AlarmSeverity as, String detail, String answer );
	}
	
	AlarmListener mListener = null;
			
	public AlarmMonitor(ApplicationProfile appProfile, AlarmListener listener) {
		super(appProfile,"PATIENT_ALARM");
		mListener = listener;
	}

	@Override
	public void notifyAdded(BindingsResults arg0, String arg1, Integer arg2) {
		int i=0;
		for (Bindings bindings : arg0.getBindings()){
			if (bindings.getBindingValue("alarmId") == null) return;
			if (bindings.getBindingValue("patId") == null) return;
			if (bindings.getBindingValue("opId") == null) return;
			
			String alarmId = bindings.getBindingValue("alarmId");
			String patId = bindings.getBindingValue("patId");
			String opId = bindings.getBindingValue("opId");
			String detail = bindings.getBindingValue("detail");
			String answer = bindings.getBindingValue("answer");
			
			String severity = bindings.getBindingValue("severity");
			AlarmSeverity as = Enum.valueOf(AlarmSeverity.class, severity);
			
			String patLabel = bindings.getBindingValue("patLabel");
			String opLabel = bindings.getBindingValue("opLabel");
			
			System.out.println("i="+i+": Detected alarm "+alarmId+" patId="+patId+" patLabel="+patLabel+"\n"
							+ "opId="+opId+" opLabel="+opLabel+//" patLoc="+patLoc+
							" severity="+as+" detail="+detail+"\n"+ "answer="+answer);
			
			if (mListener != null) {
				mListener.newAlarm( alarmId, patLabel,  opLabel, //patLoc, 
						as, detail, answer);
			}
			i++;
			
		}
		
	}


	@Override
	public void notifyRemoved(BindingsResults arg0, String arg1, Integer arg2) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onSubscribe(BindingsResults arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void brokenSubscription() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notify(ARBindingsResults arg0, String arg1, Integer arg2) {
		// TODO Auto-generated method stub
		
	}


}
