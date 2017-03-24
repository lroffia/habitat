package it.unibo.disi.habitat.ai.entities;

import org.kie.api.definition.type.PropertyReactive;

import it.unibo.disi.habitat.ai.AIModule;

@PropertyReactive
public class LocationEvent {

	private String id;
	private Role role;
	//private String label; //name of the user
	private String locationURI;
	private Integer x;
	private Integer y;
	private AIModule aiModule;
	public long timestamp;
	
	public LocationEvent(String id, String role, String locationURI, String x, String y, AIModule aiModule)
	{
		this.id = id;
		if(!role.equals("")) 
			this.setRole(Enum.valueOf(Role.class, role));
		this.locationURI = locationURI;
		this.x = Integer.parseInt(x);
		this.y = Integer.parseInt(y);
		this.aiModule = aiModule;
		this.timestamp = System.currentTimeMillis();
	}
	
	public void setId(String i) { id = i; }
	public void setLocationURI(String loc) { locationURI = loc; }
	public void setRole(Role role) {this.role = role;}
	public void setX(String x1) { x = Integer.parseInt(x1); }
	public void setY(String y1) { y = Integer.parseInt(y1); }
	public void setAIModule(AIModule ai) { aiModule = ai; }
	//public void setLabel(String label) {this.label = label;}
	
	public String getId() { return id; }
	public String getLocationURI() { return locationURI; }
	public Role getRole() {return role;}
	public Integer getX() { return x; }
	public Integer getY() { return y; }
	public AIModule getAIModule() { return aiModule; }
	//public String getLabel() {return label;}


	

}
