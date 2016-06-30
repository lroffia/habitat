package kp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import arces.unibo.SEPA.Aggregator;
import arces.unibo.SEPA.BindingLiteralValue;
import arces.unibo.SEPA.BindingURIValue;
import arces.unibo.SEPA.Bindings;
import arces.unibo.SEPA.BindingsResults;

public class UserIDManager extends Aggregator {
	private HashMap<String,String> userIDs = new HashMap<String,String>();
	
	private static final String UPDATE ="INSERT DATA { "
			+ "?id rdf:type hbt:ID . "
			+ "?pos rdf:type hbt:Position . "
			+ "hbt:Unknown rdf:type hbt:Location . "
			+ "?id rdfs:label ?label . "
			+ "?id hbt:hasPosition ?pos . ?pos hbt:hasCoordinateX \"0\" . ?pos hbt:hasCoordinateY \"0\" . "
			+ "?id hbt:hasLocation hbt:Unknown . hbt:Unknown rdfs:label \"Sconosciuta\" } ";
	
	private static final String SUBSCRIBE = "SELECT ?id ?label WHERE { ?id rdf:type hbt:ID . ?id rdfs:label ?label }";
	
	public UserIDManager(String IP,int PORT,String NAME) {
		super(SUBSCRIBE,UPDATE,IP,PORT,NAME);
		addNamespace("hbt","http://www.unibo.it/Habitat#");
		addNamespace("rdf","http://www.w3.org/1999/02/22-rdf-syntax-ns#");
		addNamespace("rdfs","http://www.w3.org/2000/01/rdf-schema#");		
	}
	
	public String newID(String label) {
		if (userIDs.containsKey(label)) return userIDs.get(label);
		
		String id = "hbt:ID_"+UUID.randomUUID().toString();
		String pos = "hbt:POS_"+UUID.randomUUID().toString();
		
		Bindings bindings = new Bindings();
		bindings.addBinding("?id", new BindingURIValue(id));
		bindings.addBinding("?pos", new BindingURIValue(pos));
		bindings.addBinding("?label", new BindingLiteralValue(label));
		
		update(bindings);
		
		return id;
	}

	@Override
	public void notify(BindingsResults arg0) {
		
	}

	@Override
	public void notifyAdded(ArrayList<Bindings> arg0) {
		for(Bindings bindings : arg0) {
			userIDs.put(bindings.getBindingValue("?label").getValue(), bindings.getBindingValue("?id").getValue());
		}
	}

	@Override
	public void notifyFirst(ArrayList<Bindings> arg0) {
		for(Bindings bindings : arg0) {
			userIDs.put(bindings.getBindingValue("?label").getValue(), bindings.getBindingValue("?id").getValue());
		}		
	}

	@Override
	public void notifyRemoved(ArrayList<Bindings> arg0) {
		for(Bindings bindings : arg0) {
			userIDs.remove(bindings.getBindingValue("?label").getValue());
		}	
	}
}
