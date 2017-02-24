package it.unibo.disi.ai.app.aimodule;

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
import it.unibo.disi.ai.entities.AlarmEvent;
import it.unibo.disi.ai.entities.AlarmSeverity;
import it.unibo.disi.ai.entities.LocationEvent;
import it.unibo.disi.ai.entities.Role;
import it.unibo.disi.ai.entities.UserCache;

public class AIModule extends Aggregator {

	private UserCache userCache;
	private AlarmIDManager alarmIDs;
	
	private static KieServices kieServices;
	private static KieContainer kContainer;
	private static KieBase kBase;
	private static KieSession kSession;

	private EntryPoint eventStream;
	
	public AIModule(AlarmIDManager alarmIDManager,ApplicationProfile appProfile) {
		super(appProfile,"USER_POSITION", "UPDATE_USER_LOCATION");
		System.out.println("Initialization complete...");
		userCache=new UserCache();
		setAlarmIDs(alarmIDManager);
	
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
            eventStream = kSession.getEntryPoint("EventStream");
            
            new Thread() {
           	 
                @Override
                public void run() {
                    kSession.fireUntilHalt();
                }
            }.start();
			

        } catch (Throwable t) {

            t.printStackTrace();
        }
	}
	
	public void terminate()
	{
        kSession.halt();
        kSession.dispose();
	}
	
	public AlarmEvent generateAlarmAndSend( String patId, String opId, String locationURI, AlarmSeverity as, String detail){
		System.out.println("Generated alarm for "+opId);
		String alarmId=alarmIDs.newAlarmID(patId, opId, locationURI, as, detail);  //generates the Id for the alarm and sends it on the SIB
		AlarmEvent alarmEvent = new AlarmEvent(alarmId, patId, opId, locationURI, detail, "", this);
		return alarmEvent;
	}
	
	public void send(String id, String locationURI){
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

	public UserCache getUserCache() {
		return userCache;
	}

	public void setUserCache(UserCache userCache) {
		this.userCache = userCache;
	}

	public AlarmIDManager getAlarmIDs() {
		return alarmIDs;
	}

	public void setAlarmIDs(AlarmIDManager alarmIDs) {
		this.alarmIDs = alarmIDs;
	}

	@Override
	public void notify(ARBindingsResults arg0, String arg1, Integer arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyAdded(BindingsResults arg0, String arg1, Integer arg2) {
		for (Bindings bindings : arg0.getBindings()) {
			String id = bindings.getBindingValue("id");
			String label = bindings.getBindingValue("label");
			String role = bindings.getBindingValue("role");
			String x = bindings.getBindingValue("x");
			String y = bindings.getBindingValue("y");
			userCache.put(id, label, Enum.valueOf(Role.class, role), Integer.parseInt(x), Integer.parseInt(y), "");
			
			//TODO: AP1) stream di dati da mandare a DROOLS (id,x,y)
			System.out.println("New position arrived for id="+id+" label="+label+" at coordinates x="+x+" and y="+y);
			
			LocationEvent locationEvent = new LocationEvent(id,role,"",x,y,this);
			eventStream.insert(locationEvent);
		}
		
	}

	@Override
	public void notifyRemoved(BindingsResults arg0, String arg1, Integer arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSubscribe(BindingsResults arg0, String arg1) {
		for (Bindings bindings : arg0.getBindings()) {
			String id = bindings.getBindingValue("id");
			String label = bindings.getBindingValue("label");
			String x = "-1";
			String y = "-1";
			
			System.out.println("notifyFirst: New position arrived for id="+id+" label="+label+" at coordinates x="+x+" and y="+y);
			
			LocationEvent locationEvent = new LocationEvent(id,"","",x,y,this);
			eventStream.insert(locationEvent);
		}
		
	}

	@Override
	public void brokenSubscription() {
		// TODO Auto-generated method stub
		
	}
	
}
