package kp;

import java.util.HashMap;
import java.util.UUID;

import arces.unibo.SEPA.application.Aggregator;
import arces.unibo.SEPA.application.ApplicationProfile;
import arces.unibo.SEPA.commons.ARBindingsResults;
import arces.unibo.SEPA.commons.Bindings;
import arces.unibo.SEPA.commons.BindingsResults;
import arces.unibo.SEPA.commons.RDFTermLiteral;
import arces.unibo.SEPA.commons.RDFTermURI;

public class UserIDManager extends Aggregator {
	private HashMap<String,String> userIDs = new HashMap<String,String>();
	
	public UserIDManager(ApplicationProfile appProfile) {
		super(appProfile,"USER_ID","INSERT_USER");		
	}
	
	public String newID(String label) {
		if (userIDs.containsKey(label)) return userIDs.get(label);
		
		String id = "hbt:ID_"+UUID.randomUUID().toString();
		String pos = "hbt:POS_"+UUID.randomUUID().toString();
		
		Bindings bindings = new Bindings();
		bindings.addBinding("id", new RDFTermURI(id));
		bindings.addBinding("pos", new RDFTermURI(pos));
		bindings.addBinding("label", new RDFTermLiteral(label));
		
		update(bindings);
		
		return id;
	}

	@Override
	public void notify(ARBindingsResults notify, String spuid, Integer sequence) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyAdded(BindingsResults bindingsResults, String spuid, Integer sequence) {
		for(Bindings bindings : bindingsResults.getBindings()) {
			userIDs.put(bindings.getBindingValue("label"), bindings.getBindingValue("id"));
		}
	}

	@Override
	public void notifyRemoved(BindingsResults bindingsResults, String spuid, Integer sequence) {
		for(Bindings bindings : bindingsResults.getBindings()) {
			userIDs.remove(bindings.getBindingValue("label"));
		}	
	}

	@Override
	public void onSubscribe(BindingsResults bindingsResults, String spuid) {
		for(Bindings bindings : bindingsResults.getBindings()) {
			userIDs.put(bindings.getBindingValue("label"), bindings.getBindingValue("id"));
		}
	}
}
