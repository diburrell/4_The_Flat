package com.FourTheFlat.stores;

import java.util.Set;
import java.util.UUID;

public class User 
{
	private String username;
	private UUID group;
	private boolean isShopping;
	private Set<String> pendingApproval;
	private Set<Integer> moneyToGet;
	
	public String getUsername() { return username; }
	public void setUsername(String username) { this.username = username; }
	
	public UUID getGroup() { return group; }
	public void setGroup(UUID group) { this.group = group; }
	
	public boolean isShopping() { return isShopping; }
	public void setShopping(boolean isShopping) { this.isShopping = isShopping; }
	
	public Set<String> getPendingApproval() { return pendingApproval; }
	public void setPendingApproval(Set<String> pendingApproval) { this.pendingApproval = pendingApproval; }
	
	public Set<Integer> getMoneyToGet() { return moneyToGet; }
	public void setMoneyToGet(Set<Integer> moneyToGet) { this.moneyToGet = moneyToGet; }
}