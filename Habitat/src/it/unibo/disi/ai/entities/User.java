package it.unibo.disi.ai.entities;

public class User {
	private String id;
	private String label; //name
	private Role role;
	private PositionAndLocation currentPosition;
	
	public User(String id, String label, Role role, PositionAndLocation currentPosition) {
		super();
		this.id = id;
		this.label = label;
		this.role = role;
		this.currentPosition = currentPosition;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	public PositionAndLocation getCurrentPosition() {
		return currentPosition;
	}
	public void setCurrentPosition(PositionAndLocation currentPosition) {
		this.currentPosition = currentPosition;
	}
	
	
}
