package it.unibo.disi.ai.app.mon;

import arces.unibo.SEPA.application.ApplicationProfile;
import arces.unibo.SEPA.application.Consumer;
import arces.unibo.SEPA.commons.SPARQL.ARBindingsResults;
import arces.unibo.SEPA.commons.SPARQL.Bindings;
import arces.unibo.SEPA.commons.SPARQL.BindingsResults;
import it.unibo.disi.ai.entities.Role;

public class PositionAndLocationMonitor extends Consumer {
	public interface PositionListener {
		public void newPositionAndLocation(String id,String label, Role role, int x,int y,String locationURI);
	}
	
	PositionListener mListener = null;
	
	public PositionAndLocationMonitor(ApplicationProfile appProfile,PositionListener listener) {
		super(appProfile,"POSITION_AND_ALARM");
		mListener = listener;
	}

	@Override
	public void notifyAdded(BindingsResults arg0, String arg1, Integer arg2) {
		for (Bindings bindings : arg0.getBindings()){
			if (bindings.getBindingValue("id") == null) return;
			if (bindings.getBindingValue("label") == null) return;
			if (bindings.getBindingValue("role") == null) return;
			if (bindings.getBindingValue("loc") == null) return;
			if (bindings.getBindingValue("x") == null) return;
			if (bindings.getBindingValue("y") == null) return;
			
			
			String id = bindings.getBindingValue("id");
			String label = bindings.getBindingValue("label");
			String rolename = bindings.getBindingValue("role");
			Role role = Enum.valueOf(Role.class, rolename);
			String location = bindings.getBindingValue("loc");
			int x = Integer.parseInt(bindings.getBindingValue("x"));
			int y = Integer.parseInt(bindings.getBindingValue("y"));
			
			String alarmId = "";
			if (bindings.getBindingValue("alarmId") != null) {
				alarmId = bindings.getBindingValue("alarmId");
				System.err.println("There is an alarm now! "+alarmId);
			}
			//if(!alarmId.equals("")) System.err.println("There is an alarm now! "+alarmId);
			
			if (mListener != null) {mListener.newPositionAndLocation(id, label,role,x, y,location);}
		}	
		
	}


	@Override
	public void notifyRemoved(BindingsResults arg0, String arg1, Integer arg2) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onSubscribe(BindingsResults arg0, String arg1) {
		notifyAdded(arg0,arg1,0);
		
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
