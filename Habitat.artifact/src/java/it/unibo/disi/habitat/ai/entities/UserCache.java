package it.unibo.disi.habitat.ai.entities;

import java.util.Collection;
import java.util.HashMap;


public class UserCache {

	private static HashMap<String,User> userList = new HashMap<String,User>();  //id and user object
	
	public User getNearestUser(int x, int y, Role role){   //get the nearest user with the provided role
		int minSquareDistance=Integer.MAX_VALUE;
		User nearestUser=null;
		for (User user : userList.values()) {
			if(role==user.getRole()){
				int a = x - user.getCurrentPosition().getX();  //x-distance
				int b = y - user.getCurrentPosition().getY();  //y-distance
				int cSquare = a*a+b*b;
				System.err.println("(x,y)=("+x+","+y+") dist. from "+user.getLabel()+" a="+a+" b="+b+" cSquare="+cSquare);
				if(cSquare<minSquareDistance){
					minSquareDistance = cSquare;
					nearestUser=user;
				}
			}
		}
		return nearestUser;
	}

	public void put(String id, String label, Role role, int x, int y,String location){
		if (userList.containsKey(id)){
			userList.get(id).setCurrentPosition(new PositionAndLocation(x, y, location));
		}else {
			userList.put(id, new User(id,label,role, new PositionAndLocation(x, y, location)));
		}
		
	}
	
	public void updatePosition(String id, int x, int y, String location){
		if (userList.containsKey(id)){
			userList.get(id).setCurrentPosition(new PositionAndLocation(x, y, location));
		}else {
			//System.err.println("ERROR");
		}
		
	}
	
	public Collection<User> values(){
		return userList.values();
	}
	
	public User getUser(String id){
		return userList.get(id);
	}
}
