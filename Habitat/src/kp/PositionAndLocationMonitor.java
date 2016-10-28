package kp;

import java.util.ArrayList;

import arces.unibo.SEPA.Bindings;
import arces.unibo.SEPA.BindingsResults;
import arces.unibo.SEPA.Consumer;

public class PositionAndLocationMonitor extends Consumer {
	public interface PositionListener {
		public void newPositionAndLocation(String id,String label,int x,int y,String locationURI);
	}
	
	PositionListener mListener = null;
	
	private static final String SUBSCRIBE = "SELECT ?id ?label ?x ?y ?loc WHERE { ?id rdf:type hbt:ID . ?id rdfs:label ?label ."
			+ " ?id hbt:hasPosition ?pos . ?pos hbt:hasCoordinateX ?x . ?pos hbt:hasCoordinateY ?y . ?id hbt:hasLocation ?loc }";
	
	public PositionAndLocationMonitor(String SIB_IP, int SIB_PORT, String SIB_NAME,PositionListener listener) {
		super(SUBSCRIBE, SIB_IP, SIB_PORT, SIB_NAME);
		addNamespace("hbt","http://www.unibo.it/Habitat#");
		addNamespace("rdf","http://www.w3.org/1999/02/22-rdf-syntax-ns#");
		addNamespace("rdfs","http://www.w3.org/2000/01/rdf-schema#");
		mListener = listener;
	}

	public PositionAndLocationMonitor(PositionListener listener) {
		super("USER_POSITION");
		mListener = listener;
	}
	
	@Override
	public void notify(BindingsResults notify) {
		
	}

	@Override
	public void notifyAdded(ArrayList<Bindings> bindingsResults) {
		for (Bindings bindings : bindingsResults){
			if (bindings.getBindingValue("?id") == null) return;
			if (bindings.getBindingValue("?label") == null) return;
			if (bindings.getBindingValue("?loc") == null) return;
			if (bindings.getBindingValue("?x") == null) return;
			if (bindings.getBindingValue("?y") == null) return;
			
			String id = bindings.getBindingValue("?id").getValue();
			String label = bindings.getBindingValue("?label").getValue();
			String location = bindings.getBindingValue("?loc").getValue();
			int x = Integer.parseInt(bindings.getBindingValue("?x").getValue());
			int y = Integer.parseInt(bindings.getBindingValue("?y").getValue());
			
			if (mListener != null) {mListener.newPositionAndLocation(id, label,x, y,location);}
		}	
	}

	@Override
	public void notifyRemoved(ArrayList<Bindings> bindingsResults) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void notifyFirst(ArrayList<Bindings> bindingsResults) {
		notifyAdded(bindingsResults);
	}


}
