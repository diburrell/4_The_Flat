package com.FourTheFlat;

import java.io.IOException;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

public class HttpRequests extends AsyncTask<String, Void, String>{

	
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		String url = params[0];
		String requestType = params[1];
		
		if(requestType.toLowerCase().equals("get"))
		{
			return httpGetRequest(url);
		}
		else if(requestType.toLowerCase().equals("post"))
		{
			return httpPostRequest(url);
		}
		else if(requestType.toLowerCase().equals("put"))
		{
			return httpPutRequest(url);
		}
		else if(requestType.toLowerCase().equals("delete"))
		{
			return httpDeleteRequest(url);
		}
		else
		{
			return null;
		}
	}
	private static String httpGetRequest(String url)
	{
		try
		{
			
			 HttpClient httpclient = new DefaultHttpClient();
			 HttpGet httpGet = new HttpGet(url);
			 ResponseHandler<String> responseHandler = new BasicResponseHandler();
			 String response = httpclient.execute(httpGet, responseHandler);
			 Log.w("succeed!!!","http response: "+response);
			 return response;
			}
			catch(IOException e)
			{
				e.printStackTrace();
					return null;
			}
	}
	
	private static String httpPutRequest(String url)
	{
		try
		{
			 HttpClient httpclient = new DefaultHttpClient();
			 HttpDelete httpDelete = new HttpDelete(url);
			 ResponseHandler<String> responseHandler = new BasicResponseHandler();
			 String response = httpclient.execute(httpDelete, responseHandler);
			 Log.w("succeed!!!","http response: "+response);
			 return response;
			}
			catch(IOException e)
			{
				e.printStackTrace();
					return null;
			}
	}
	
	private static String httpPostRequest(String url)
	{
		try
		{
			 HttpClient httpclient = new DefaultHttpClient();
			 HttpPost httpPost = new HttpPost(url);
			 ResponseHandler<String> responseHandler = new BasicResponseHandler();
			 String response = httpclient.execute(httpPost, responseHandler);
			 Log.w("succeed!!!","http response: "+response);
			 return response;
			}
			catch(IOException e)
			{
				e.printStackTrace();
					return null;
			}
	}
	
	private static String httpDeleteRequest(String url)
	{
		try
		{
			 HttpClient httpclient = new DefaultHttpClient();
			 HttpDelete httpDelete = new HttpDelete(url);
			 ResponseHandler<String> responseHandler = new BasicResponseHandler();
			 String response = httpclient.execute(httpDelete, responseHandler);
			 Log.w("succeed!!!","http delete response: "+response);
			 return response;
			}
			catch(IOException e)
			{
				e.printStackTrace();
					return null;
			}
	}


}
