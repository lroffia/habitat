package it.unibo.disi.ai.app.sim;

import java.util.HashMap;

import it.unibo.disi.ai.entities.Role;

/*
 * Hard coded user-role association. Just for demo purposes.
 * This information will flow through the SIB module in the future.
 * */

public class UserRoleList {
	private static final HashMap<String,Role> urList = new HashMap<String,Role>();  //name and role
	
	public UserRoleList(){
		init();
	}
	
	private void init(){
		urList.put("Adele", Role.PATIENT);
		urList.put("Piero", Role.PATIENT);
		urList.put("Mario", Role.PATIENT);
		urList.put("Luca", Role.OPERATOR);
		urList.put("Alice", Role.OPERATOR);
	}
	
	public Role get(String userName){
		return urList.get(userName);
	}
}
