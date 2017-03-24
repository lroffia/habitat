package it.unibo.disi.habitat.rid.simulator;

import java.util.HashMap;
import java.util.UUID;

import arces.unibo.SEPA.application.Aggregator;
import arces.unibo.SEPA.application.ApplicationProfile;
import arces.unibo.SEPA.commons.SPARQL.ARBindingsResults;
import arces.unibo.SEPA.commons.SPARQL.Bindings;
import arces.unibo.SEPA.commons.SPARQL.BindingsResults;
import arces.unibo.SEPA.commons.SPARQL.RDFTermLiteral;
import arces.unibo.SEPA.commons.SPARQL.RDFTermURI;
import it.unibo.disi.habitat.ai.entities.Role;

public class UserIDManager extends Aggregator {
	private HashMap<String,String> userIDs = new HashMap<String,String>();
	
	public UserIDManager(ApplicationProfile appProfile) {
		super(appProfile,"ID","INSERT_USER");
	}
	
	public String newID(String label, Role role) {
		if (userIDs.containsKey(label)) return userIDs.get(label);
		
		String id = "hbt:ID_"+UUID.randomUUID().toString();
		String pos = "hbt:POS_"+UUID.randomUUID().toString();
		
		Bindings bindings = new Bindings();
		bindings.addBinding("id", new RDFTermURI(id));
		bindings.addBinding("pos", new RDFTermURI(pos));
		bindings.addBinding("label", new RDFTermLiteral(label));
		bindings.addBinding("role", new RDFTermLiteral(role.toString()));
		
		update(bindings);
		
		return id;
	}

	public void notify(ARBindingsResults arg0, String arg1, Integer arg2) {
		// TODO Auto-generated method stub
		
	}

	public void notifyAdded(BindingsResults arg0, String arg1, Integer arg2) {
		for(Bindings bindings : arg0.getBindings()) {
			userIDs.put(bindings.getBindingValue("label"), bindings.getBindingValue("id"));
		}
		
	}

	public void notifyRemoved(BindingsResults arg0, String arg1, Integer arg2) {
		for(Bindings bindings : arg0.getBindings()) {
			userIDs.remove(bindings.getBindingValue("label"));
		}	
		
	}

	public void onSubscribe(BindingsResults arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	public void brokenSubscription() {
		// TODO Auto-generated method stub
		
	}
}
