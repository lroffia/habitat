package it.unibo.disi.ai;

public class LocationData {

	private String id;
	private String locationURI;
	private Integer x;
	private Integer y;
	private AIModule aiModule;
	
	public LocationData(String i, String loc, String x1, String y1, AIModule ai)
	{
		id = i;
		locationURI = loc;
		x = Integer.parseInt(x1);
		y = Integer.parseInt(y1);
		aiModule = ai;
	}
	
	public void setId(String i) { id = i; }
	public void setLocationURI(String loc) { locationURI = loc; }
	public void setX(String x1) { x = Integer.parseInt(x1); }
	public void setY(String y1) { y = Integer.parseInt(y1); }
	public void setAIModule(AIModule ai) { aiModule = ai; }
	
	public String getId() { return id; }
	public String getLocationURI() { return locationURI; }
	public Integer getX() { return x; }
	public Integer getY() { return y; }
	public AIModule getAIModule() { return aiModule; }
}
