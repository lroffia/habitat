package it.unibo.disi.ai.app.aimodule;

import java.util.UUID;

import arces.unibo.SEPA.application.ApplicationProfile;
import arces.unibo.SEPA.application.Producer;
import arces.unibo.SEPA.commons.SPARQL.Bindings;
import arces.unibo.SEPA.commons.SPARQL.RDFTermLiteral;
import arces.unibo.SEPA.commons.SPARQL.RDFTermURI;
import it.unibo.disi.ai.entities.AlarmSeverity;

public class AlarmIDManager extends Producer{ 
	
	public AlarmIDManager(ApplicationProfile appProfile) {
		super(appProfile,"INSERT_ALARM");	
	}
	
	public String newAlarmID(String patId, String opId, String locationURI, AlarmSeverity as, String detail) {
		
		String id = "hbt:AlarmID_"+UUID.randomUUID().toString();

		System.out.println("Generated alarm "+id);
		
		Bindings bindings = new Bindings();
		bindings.addBinding("alarmId", new RDFTermURI(id));
		bindings.addBinding("patId", new RDFTermURI(patId));
		bindings.addBinding("opId", new RDFTermURI(opId));
		bindings.addBinding("patLoc", new RDFTermURI(locationURI));
		bindings.addBinding("severity", new RDFTermLiteral(as.toString()));
		bindings.addBinding("detail", new RDFTermLiteral(detail));
		
		update(bindings);
		System.out.println("alarm inserted!");
		return id;
	}

	/*
	//@Override
	public void notify(BindingsResults arg0) {
		System.out.println("notify... ");
	}

	public void notifyAdded(ArrayList<Bindings> arg0) {
		System.out.println("notifyAdded...");
		for(Bindings bindings : arg0) {
			System.out.println("detail is: "+bindings.getBindingValue("?detail").getValue() +
					"\nalarmId is: "+ bindings.getBindingValue("?alarmId").getValue());
		}
	}

	public void notifyFirst(ArrayList<Bindings> arg0) {
		System.out.println("notifyFirst...");
		for(Bindings bindings : arg0) {
			System.out.println("detail is: "+bindings.getBindingValue("?detail").getValue() +
					"\nalarmId is: "+ bindings.getBindingValue("?alarmId").getValue());
		}
	}

	public void notifyRemoved(ArrayList<Bindings> arg0) {
		// TODO Auto-generated method stub
		
	}*/

}
