package ai;

import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.EntryPoint;

import arces.unibo.SEPA.application.Aggregator;
import arces.unibo.SEPA.application.ApplicationProfile;
import arces.unibo.SEPA.commons.SPARQL.ARBindingsResults;
import arces.unibo.SEPA.commons.SPARQL.Bindings;
import arces.unibo.SEPA.commons.SPARQL.BindingsResults;
import arces.unibo.SEPA.commons.SPARQL.RDFTermURI;

public class AIModule extends Aggregator {

	public static void main(String[] args) {
	}

	private static KieServices kieServices;
	private static KieContainer kContainer;
	private static KieBase kBase;
	private static KieSession kSession;

	private EntryPoint locationDataStream;
	
	public AIModule(ApplicationProfile app) {
		super(app,"USER_POSITION","UPDATE_USER_LOCATION");
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
		bindings.addBinding("id", new RDFTermURI(id));
		bindings.addBinding("location", new RDFTermURI(locationURI));
		System.out.println("Location sent !");
		return update(bindings);
	}

	@Override
	public void notify(ARBindingsResults notify, String spuid, Integer sequence) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyAdded(BindingsResults bindingsResults, String spuid, Integer sequence) {
		for (Bindings bindings : bindingsResults.getBindings()) {
			String id = bindings.getBindingValue("id");
			String x = bindings.getBindingValue("x");
			String y = bindings.getBindingValue("y");
			
			//TODO: AP1) stream di dati da mandare a DROOLS (id,x,y)
			System.out.println("New position arrived for id=" + id + " at coordinates x=" + x + " and y=" + y);
			
			LocationData locationData = new LocationData(id,"",x,y,this);
			locationDataStream.insert(locationData);
			kSession.fireAllRules();
		}
	}

	@Override
	public void notifyRemoved(BindingsResults bindingsResults, String spuid, Integer sequence) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSubscribe(BindingsResults bindingsResults, String spuid) {
		notifyAdded(bindingsResults,spuid,0);
		for (Bindings bindings : bindingsResults.getBindings()) {
			String id = bindings.getBindingValue("id");
			String x = bindings.getBindingValue("x");
			String y = bindings.getBindingValue("y");
			
			System.out.println("New position arrived for id=" + id + " at coordinates x=" + x + " and y=" + y);
			
			LocationData locationData = new LocationData(id,"",x,y,this);
			locationDataStream.insert(locationData);
			kSession.fireAllRules();
		}
		
	}

	@Override
	public void brokenSubscription() {
		// TODO Auto-generated method stub
		
	}

}
