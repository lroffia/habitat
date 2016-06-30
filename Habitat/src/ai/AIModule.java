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
			+ "WHERE { ?id rdf:type hat:ID . ?id hbt:hasPosition ?pos . ?pos hbt:hasCoordinateX ?x . ?pos hbt:hasCoordinateY ?y }";
	
	public AIModule() {
		super(SUBSCRIBE, UPDATE,IP,PORT,NAME);
		addNamespace("hbt","http://www.unibo.it/Habitat#");
		addNamespace("rdf","http://www.w3.org/1999/02/22-rdf-syntax-ns#");
		addNamespace("rdfs","http://www.w3.org/2000/01/rdf-schema#");
	}
	
	private String AILocation(String id,String x,String y) {
		//TODO: qui occorre usare DROOLS per capire l'URI della location in base a X,Y (e forse ID?)
		
		return "hbt:DummyLocation_"+id+"_"+x+"_"+y;
	}

	@Override
	public void notifyAdded(ArrayList<Bindings> arg0) {
		//Viene chiamata ogni volta che X e/o Y cambiano
		for (Bindings bindings : arg0) {
			String id = bindings.getBindingValue("?id").getValue();
			String x = bindings.getBindingValue("?x").getValue();
			String y = bindings.getBindingValue("?x").getValue();
			
			String locationURI = AILocation(id,x,y);
			
			bindings.addBinding("?location", new BindingURIValue(locationURI));
			update(bindings);
		}
	}

	@Override
	public void notifyFirst(ArrayList<Bindings> arg0) {
		//TODO: capire se in base alla condizione iniziale è possibile stabilire già qual'è la location
		notifyAdded(arg0);
	}
	
	@Override
	public void notify(BindingsResults arg0) {}

	@Override
	public void notifyRemoved(ArrayList<Bindings> arg0) {}

}
