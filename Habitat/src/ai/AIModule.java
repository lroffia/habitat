package it.unibo.disi.ai;

import java.util.ArrayList;
import java.util.Collection;

import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.EntryPoint;

import arces.unibo.SEPA.Aggregator;
import arces.unibo.SEPA.BindingURIValue;
import arces.unibo.SEPA.Bindings;
import arces.unibo.SEPA.BindingsResults;

public class AIModule extends Aggregator {

	private static String IP ="mml.arces.unibo.it";
	private static int PORT = 10123;
	private static String NAME = "Habitat";

	private static KieServices kieServices;
	private static KieContainer kContainer;
	private static KieBase kBase;
	private static KieSession kSession;

	private EntryPoint locationDataStream;
	
	private static String UPDATE ="DELETE { ?id hbt:hasLocation ?oldLocation } "
			+ "INSERT { ?id hbt:hasLocation ?location } "
			+ "WHERE { ?id hbt:hasLocation ?oldLocation }";
	
	private static String SUBSCRIBE ="SELECT ?id ?x ?y "
			+ "WHERE { ?id rdf:type hbt:ID . ?id hbt:hasPosition ?pos . ?pos hbt:hasCoordinateX ?x . ?pos hbt:hasCoordinateY ?y }";
	
	public AIModule() {
		super(SUBSCRIBE, UPDATE,IP,PORT,NAME);
		addNamespace("hbt","http://www.unibo.it/Habitat#");
		addNamespace("rdf","http://www.w3.org/1999/02/22-rdf-syntax-ns#");
		addNamespace("rdfs","http://www.w3.org/2000/01/rdf-schema#");
		System.out.println("Initialization complete");
		run();
	}
	
	public void run()
	{
		try {

            // load up the knowledge base
            kieServices = KieServices.Factory.get();
            kContainer = kieServices.getKieClasspathContainer();

            KieBaseConfiguration kieBaseConfig = KieServices.Factory.get().newKieBaseConfiguration();
            kieBaseConfig.setOption(EventProcessingOption.STREAM);
            kBase = kContainer.newKieBase(kieBaseConfig);            
            kSession = kBase.newKieSession();

            // get a reference to the entry point
            locationDataStream = kSession.getEntryPoint("LocationDataStream");
            
        } catch (Throwable t) {

            t.printStackTrace();
        }
	}
	
	public void terminate()
	{
        kSession.halt();
        kSession.dispose();
	}
	
	public void send(String id, String locationURI)
	{
		sendLocation(id, locationURI);
	}
	
	//TODO: AP2) da invocare per mandare alla SIB l'URI della location di "id"
	//Esempi di URI: hbt:Bagno, hbt:Room1, hbt:Sala, hbt:RedZone
	private boolean sendLocation(String id, String locationURI) {
		Bindings bindings = new Bindings();
		bindings.addBinding("?id", new BindingURIValue(id));
		bindings.addBinding("?location", new BindingURIValue(locationURI));
		System.out.println("Location sent !");
		return update(bindings);
	}
	
	@Override
	public void notifyAdded(ArrayList<Bindings> arg0) {
		for (Bindings bindings : arg0) {
			String id = bindings.getBindingValue("?id").getValue();
			String x = bindings.getBindingValue("?x").getValue();
			String y = bindings.getBindingValue("?y").getValue();
			
			//TODO: AP1) stream di dati da mandare a DROOLS (id,x,y)
			System.out.println("New position arrived for id=" + id + " at coordinates x=" + x + " and y=" + y);
			
			LocationData locationData = new LocationData(id,"",x,y,this);
			locationDataStream.insert(locationData);
			kSession.fireAllRules();
		}
	}

	@Override
	public void notifyFirst(ArrayList<Bindings> arg0) {
		notifyAdded(arg0);
		for (Bindings bindings : arg0) {
			String id = bindings.getBindingValue("?id").getValue();
			String x = bindings.getBindingValue("?x").getValue();
			String y = bindings.getBindingValue("?y").getValue();
			
			System.out.println("New position arrived for id=" + id + " at coordinates x=" + x + " and y=" + y);
			
			LocationData locationData = new LocationData(id,"",x,y,this);
			locationDataStream.insert(locationData);
			kSession.fireAllRules();
		}
	}
	
	@Override
	public void notify(BindingsResults arg0) {}

	@Override
	public void notifyRemoved(ArrayList<Bindings> arg0) {}

}
