package it.unibo.disi.ai.app.sim;

import arces.unibo.SEPA.application.ApplicationProfile;
import arces.unibo.SEPA.application.Producer;
import arces.unibo.SEPA.commons.SPARQL.Bindings;
import arces.unibo.SEPA.commons.SPARQL.RDFTermLiteral;
import arces.unibo.SEPA.commons.SPARQL.RDFTermURI;

public class PositionUpdater extends Producer {
	
	private String id = "";
	
	public PositionUpdater(ApplicationProfile appProfile,String id) {
		super(appProfile,"UPDATE_USER_POSITION");
		addNamespace("hbt","http://www.unibo.it/Habitat#");
		this.id = id;
	}
	
	public void updatePosition(int x,int y) {
		System.out.println("Updated position: x="+x+" y="+y);
		Bindings bindings = new Bindings();
		bindings.addBinding("id", new RDFTermURI(id));
		bindings.addBinding("x", new RDFTermLiteral(String.format("%d", x)));
		bindings.addBinding("y", new RDFTermLiteral(String.format("%d", y)));
		update(bindings);
	}
}
