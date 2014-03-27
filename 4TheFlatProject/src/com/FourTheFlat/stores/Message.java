package com.FourTheFlat.stores;

import java.util.UUID;

public class Message 
{
	private UUID messageID;
	private String message; 
	private int type;
	private String receiver;

	public UUID getMessageID() 
	{
		return messageID;
	}

	public void setMessageID(UUID messageID) 
	{
		this.messageID = messageID;
	}

	public String getMessage() 
	{
		return message;
	}

	public void setMessage(String message) 
	{
		this.message = message;
	}

	public int getType() 
	{
		return type;
	}

	public void setType(int type) 
	{
		this.type = type;
	}

	public String getReceiver() 
	{
		return receiver;
	}

	public void setReceiver(String receiver) 
	{
		this.receiver = receiver;
	}
}