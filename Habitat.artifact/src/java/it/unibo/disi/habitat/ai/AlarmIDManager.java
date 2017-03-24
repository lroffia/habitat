package it.unibo.disi.habitat.ai;

import java.util.UUID;

import arces.unibo.SEPA.application.ApplicationProfile;
import arces.unibo.SEPA.application.Producer;
import arces.unibo.SEPA.commons.SPARQL.Bindings;
import arces.unibo.SEPA.commons.SPARQL.RDFTermLiteral;
import arces.unibo.SEPA.commons.SPARQL.RDFTermURI;
import it.unibo.disi.habitat.ai.entities.AlarmSeverity;

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
}
