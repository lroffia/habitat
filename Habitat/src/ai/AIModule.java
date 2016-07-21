package ai;
import java.util.ArrayList;

import arces.unibo.SEPA.Aggregator;
import arces.unibo.SEPA.BindingURIValue;
import arces.unibo.SEPA.Bindings;
import arces.unibo.SEPA.BindingsResults;

public class AIModule extends Aggregator {

	private static String IP ="mml.arces.unibo.it";
	private static int PORT = 10123;
	private static String NAME = "Habitat";
	
	private static String UPDATE ="DELETE { ?id hbt:hasLocation ?oldLocation } "
			+ "INSERT { ?id hbt:hasLocation ?location } "
			+ "WHERE { ?id hbt:hasLocation ?location }";
	private static String SUBSCRIBE ="SELECT ?id ?x ?y "
			+ "WHERE { ?id rdf:type hbt:ID . ?id hbt:hasPosition ?pos . ?pos hbt:hasCoordinateX ?x . ?pos hbt:hasCoordinateY ?y }";
	
	public AIModule() {
		super(SUBSCRIBE, UPDATE,IP,PORT,NAME);
		addNamespace("hbt","http://www.unibo.it/Habitat#");
		addNamespace("rdf","http://www.w3.org/1999/02/22-rdf-syntax-ns#");
		addNamespace("rdfs","http://www.w3.org/2000/01/rdf-schema#");
	}
	
	//TODO: AP2) da invocare per mandare alla SIB l'URI della location di "id"
	//Esempi di URI: hbt:Bagno, hbt:Room1, hbt:Sala, hbt:RedZone
	public boolean SendLocation(String id,String locationURI) {
		Bindings bindings = new Bindings();
		bindings.addBinding("?id", new BindingURIValue(id));
		bindings.addBinding("?location", new BindingURIValue(locationURI));
		return update(bindings);
	}

	@Override
	public void notifyAdded(ArrayList<Bindings> arg0) {
		for (Bindings bindings : arg0) {
			String id = bindings.getBindingValue("?id").getValue();
			String x = bindings.getBindingValue("?x").getValue();
			String y = bindings.getBindingValue("?y").getValue();
			
			//TODO: AP1) stream di dati da mandare a DROOLS (id,x,y)
			
		}
	}

	@Override
	public void notifyFirst(ArrayList<Bindings> arg0) {
		notifyAdded(arg0);
	}
	
	@Override
	public void notify(BindingsResults arg0) {}

	@Override
	public void notifyRemoved(ArrayList<Bindings> arg0) {}

}
