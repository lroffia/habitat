package kp;

import arces.unibo.SEPA.application.Consumer;
import arces.unibo.SEPA.commons.SPARQL.ARBindingsResults;
import arces.unibo.SEPA.commons.SPARQL.Bindings;
import arces.unibo.SEPA.commons.SPARQL.BindingsResults;
import arces.unibo.SEPA.application.ApplicationProfile;

public class PositionAndLocationMonitor extends Consumer {
	public interface PositionListener {
		public void newPositionAndLocation(String id,String label,int x,int y,String locationURI);
	}
	
	PositionListener mListener = null;

	public PositionAndLocationMonitor(ApplicationProfile appProfile,PositionListener listener) {
		super(appProfile,"USER_POSITION");
		mListener = listener;
	}

	@Override
	public void notify(ARBindingsResults notify, String spuid, Integer sequence) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyAdded(BindingsResults bindingsResults, String spuid, Integer sequence) {
		for (Bindings bindings : bindingsResults.getBindings()){
			if (bindings.getBindingValue("id") == null) return;
			if (bindings.getBindingValue("label") == null) return;
			if (bindings.getBindingValue("loc") == null) return;
			if (bindings.getBindingValue("x") == null) return;
			if (bindings.getBindingValue("y") == null) return;
			
			String id = bindings.getBindingValue("id");
			String label = bindings.getBindingValue("label");
			String location = bindings.getBindingValue("loc");
			int x = Integer.parseInt(bindings.getBindingValue("x"));
			int y = Integer.parseInt(bindings.getBindingValue("y"));
			
			if (mListener != null) {mListener.newPositionAndLocation(id, label,x, y,location);}
		}
	}

	@Override
	public void notifyRemoved(BindingsResults bindingsResults, String spuid, Integer sequence) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSubscribe(BindingsResults bindingsResults, String spuid) {
		notifyAdded(bindingsResults,spuid,0);
		
	}

	@Override
	public void brokenSubscription() {
		// TODO Auto-generated method stub
		
	}


}
