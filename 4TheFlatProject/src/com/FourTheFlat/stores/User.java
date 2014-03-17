package com.FourTheFlat.stores;

import java.util.Set;
import java.util.UUID;

public class User {
	private String username;
	private UUID groupID;
	private boolean isShopping;
	private Set<String> pendingApproval;
	private Set<Integer> moneyToGet;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Set<String> getPendingApproval() {
		return pendingApproval;
	}

	public void setPendingApproval(Set<String> pendingApproval) {
		this.pendingApproval = pendingApproval;
	}

	public Set<Integer> getMoneyToGet() {
		return moneyToGet;
	}

	public void setMoneyToGet(Set<Integer> moneyToGet) {
		this.moneyToGet = moneyToGet;
	}

	public boolean getIsShopping() {
		return isShopping;
	}

	public void setIsShopping(boolean isShopping) {
		this.isShopping = isShopping;
	}

	public UUID getGroupID() {
		return groupID;
	}

	public void setGroupID(UUID groupID) {
		this.groupID = groupID;
	}

}