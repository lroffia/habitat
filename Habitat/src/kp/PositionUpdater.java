package kp;

import arces.unibo.SEPA.BindingLiteralValue;
import arces.unibo.SEPA.BindingURIValue;
import arces.unibo.SEPA.Bindings;
import arces.unibo.SEPA.Producer;

public class PositionUpdater extends Producer {
	private static final String UPDATE ="DELETE { ?pos hbt:hasCoordinateX ?oldX .  ?pos hbt:hasCoordinateY ?oldY } "
			+ "INSERT { ?pos hbt:hasCoordinateX ?x . ?pos hbt:hasCoordinateY ?y } "
			+ "WHERE { ?id hbt:hasPosition ?pos . ?pos hbt:hasCoordinateX ?oldX .  ?pos hbt:hasCoordinateY ?oldY }";
	
	private String id = "";
	
	public PositionUpdater(String id,String IP,int PORT,String NAME) {
		super(UPDATE,IP,PORT,NAME);
		addNamespace("hbt","http://www.unibo.it/Habitat#");
		this.id = id;
	}
	
	public PositionUpdater(String id) {
		super("UPDATE_USER_POSITION");
		this.id = id;
	}
	
	public void updatePosition(int x,int y) {
		Bindings bindings = new Bindings();
		bindings.addBinding("?id", new BindingURIValue(id));
		bindings.addBinding("?x", new BindingLiteralValue(String.format("%d", x)));
		bindings.addBinding("?y", new BindingLiteralValue(String.format("%d", y)));
		update(bindings);
	}
}
