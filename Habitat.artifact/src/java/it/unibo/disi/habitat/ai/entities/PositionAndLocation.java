package it.unibo.disi.habitat.ai.entities;

public class PositionAndLocation {
	private int x;
	private int y;
	private String location;

	public PositionAndLocation(int x, int y,String location){
		this.x = x;
		this.y = y;
		this.location = location;
	}

	public int getX(){return x;}
	public int getY(){return y;}
	public String getLocation(){return location;}

}
