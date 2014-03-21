package com.FourTheFlat.stores;

import java.util.UUID;

public class User {
	
	private String username;
	private UUID groupID;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public UUID getGroupID() {
		return groupID;
	}
	public void setGroupID(UUID groupID) {
		this.groupID = groupID;
	}

}