package com.FourTheFlat;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.util.Base64;
import android.util.Log;

public class Cryptography 
{
    public static String convertToHex(byte[] data) throws java.io.IOException
    {
    	StringBuffer sb = new StringBuffer();
    	String hex = null;
    	hex = Base64.encodeToString(data, 0, data.length, 0);
    	sb.append(hex);
    	return sb.toString();        	
    }
    
    public static String computeSHAHash(String input)
    {
    	StringBuffer output = new StringBuffer();
    	MessageDigest mdSha256 = null;
        try
        {
        	mdSha256 = MessageDigest.getInstance("SHA-256");
        }
        catch(NoSuchAlgorithmException e)
        {
        	Log.e("myapp", "SHA-256 ERROR!");
        }
        try
        {
        	mdSha256.update(input.getBytes("ASCII"));
        }
        catch(Exception e)
        {
        	
        }
        
        byte[] data = mdSha256.digest();
        output.append(byteArrayToString(data));
        return output.toString();
    }
    
    /**
	 * Taken from http://stackoverflow.com/questions/4895523/java-string-to-sha1
	 * Allows us to convert a SHA-1 Byte Array into a Hex String
	 */
	public static String byteArrayToString(byte[] b) 
	{
		String result = "";

		for (int i=0; i < b.length; i++) 
		{
			result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
		}

		return result;
	}
}