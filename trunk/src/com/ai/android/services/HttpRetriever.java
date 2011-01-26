package com.ai.android.services;

import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import com.ai.android.io.FlushedInputStream;
import com.ai.android.util.Utils;

/*
 * responsible for performing all the HTTP requests and will return the responses both in text format and as a stream (for image manipulation).
 * */

public class HttpRetriever {
	
	private DefaultHttpClient client = new DefaultHttpClient();	
	
	public String retrieve(String url) {
		
        HttpGet getRequest = new HttpGet(url);                                                     // in order to represent a GET request
        
		try {
			
			HttpResponse getResponse = client.execute(getRequest);                                  // getResponse contains the actual server response along with any other information 
			final int statusCode = getResponse.getStatusLine().getStatusCode();                      //getting the response status code  
			
			if (statusCode != HttpStatus.SC_OK) {                                                    //comparing it against the code for successful HTTP requests 
	            Log.w(getClass().getSimpleName(), "Error 11 " + statusCode + " for URL " + url); 
	            return null;
	        }
			
			HttpEntity getResponseEntity = getResponse.getEntity();                                 // For successful requests, take reference of the enclosed HttpEntity object and from that get the access to the actual response data
			
			if (getResponseEntity != null) {
				return EntityUtils.toString(getResponseEntity);                                     //convert the entity to a String using the static 'toString' method of the EntityUtils class. 
			}
			
		} 
		catch (IOException e) {
			getRequest.abort();
	        Log.w(getClass().getSimpleName(), "Error for URL " + url, e);
		}
		
		return null;
		
	}
	
	public InputStream retrieveStream(String url) {                           // retrieve the data as a byte stream -for example in order to handle binary downloads.
		
		HttpGet getRequest = new HttpGet(url);
        
		try {
			
			HttpResponse getResponse = client.execute(getRequest);
			final int statusCode = getResponse.getStatusLine().getStatusCode();
			
			if (statusCode != HttpStatus.SC_OK) { 
	            Log.w(getClass().getSimpleName(), "Error " + statusCode + " for URL " + url); 
	            return null;
	        }

			HttpEntity getResponseEntity = getResponse.getEntity();
			return getResponseEntity.getContent();                                        // using the 'getContent' method of the HttpEntity class, to create a new InputStream object of the entity.
			
		} 
		catch (IOException e) {
			getRequest.abort();
	        Log.w(getClass().getSimpleName(), "Error for URL " + url, e);
		}
		
		return null;
		
	}
	
	public Bitmap retrieveBitmap(String url) throws Exception {                                        // to return Bitmap objects.
		
		InputStream inputStream = null;
        try {
            inputStream = this.retrieveStream(url);    
                                                                                                       // using the decodeStream method of the BitmapFactory class to create a new Bitmap object                       
            final Bitmap bitmap = BitmapFactory.decodeStream(new FlushedInputStream(inputStream));     //first wrap the InputStream  with a FlushedInputStream class.otherwise an exception will through over slower connection.
            return bitmap;
        } 
        finally {
            Utils.closeStreamQuietly(inputStream);                                             // closing the InputStream quietly.closeStreamQuietly class handles the exceptions thrown when closing InputStream.                                                  // 
        }
		
	}

}
