package kp;

import arces.unibo.SEPA.application.Producer;
import arces.unibo.SEPA.commons.Bindings;
import arces.unibo.SEPA.commons.RDFTermLiteral;
import arces.unibo.SEPA.commons.RDFTermURI;
import arces.unibo.SEPA.application.ApplicationProfile;

public class PositionUpdater extends Producer {
	private String id = "";
	
	public PositionUpdater(ApplicationProfile appProfile,String id) {
		super(appProfile,"UPDATE_USER_POSITION");
		this.id = id;
	}
	
	public void updatePosition(int x,int y) {
		Bindings bindings = new Bindings();
		bindings.addBinding("id", new RDFTermURI(id));
		bindings.addBinding("x", new RDFTermLiteral(String.format("%d", x)));
		bindings.addBinding("y", new RDFTermLiteral(String.format("%d", y)));
		update(bindings);
	}
}
