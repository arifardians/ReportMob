package com.arif.httphelper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;


import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;


public class CustomHttpClient {
	/* the time it takes for our client to timeout */
	public static final int HTTP_TIMEOUT = 30*1000;  // in millisecond
	
	/* single instance of our httpClient */
	private static HttpClient mHttpClient;
	
	
	/**
	 * Get our single instance of our HttpClient object 
	 * 
	 * @return an Http client object with connection parameter set 
	 */	
	private static HttpClient getHttpClient(){
		if(mHttpClient == null){
			mHttpClient = new DefaultHttpClient(); 
			final HttpParams params = mHttpClient.getParams(); 
			HttpConnectionParams.setConnectionTimeout(params, HTTP_TIMEOUT);
			HttpConnectionParams.setSoTimeout(params, HTTP_TIMEOUT);
			ConnManagerParams.setTimeout(params, HTTP_TIMEOUT);
		}
		
		return mHttpClient;
		
	}
	
	/**
	 * Performs an Http Post request to the specified url with the specified
	 * parameters. 
	 * 
	 * @param url the web address to post the request to 
	 * @param postParameters The parameters to send via the request 
	 * @return The result of the request
	 * @throws Exception
	 */
	
	public static String executeHttpPost(String url, ArrayList<NameValuePair> postParameters) throws Exception{
		BufferedReader in = null; 
			
		try {
			HttpClient client = getHttpClient(); 
			HttpPost request = new HttpPost(url);
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters); 
			
			request.setEntity(formEntity); 
			request.getParams().setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, Boolean.FALSE);
			HttpResponse response = client.execute(request); 
			
			in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			
			StringBuffer sb = new StringBuffer("");
			String line = ""; 
			String NL = System.getProperty("line.separator"); 
			while ((line = in.readLine()) != null) {
				sb.append(line+NL);
			}
			in.close();
			
			String result = sb.toString(); 
			
			return result; 
		}finally{
			if(in != null){
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} 
		
	}
	
	public static String executeHttpPost(String url, ArrayList<NameValuePair> postParameters, NameValuePair header) throws Exception{
		BufferedReader in = null; 
			
		try {
			HttpClient client = getHttpClient(); 
			HttpPost request = new HttpPost(url);
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters); 
			
			request.setEntity(formEntity); 
			request.getParams().setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, Boolean.FALSE);
			request.addHeader(header.getName(), header.getValue());
			HttpResponse response = client.execute(request); 
			
			in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			
			StringBuffer sb = new StringBuffer("");
			String line = ""; 
			String NL = System.getProperty("line.separator"); 
			while ((line = in.readLine()) != null) {
				sb.append(line+NL);
			}
			in.close();
			
			String result = sb.toString(); 
			
			return result; 
		}finally{
			if(in != null){
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} 
		
	}
	
	/**
	 * performs an HTTP GET req uest to specified url. 
	 * 
	 * @param url The web address to post the request to. 
	 * @return The result of the request 
	 * @throws Exception
	 */
	
	public static String executeHttpGet(String url, NameValuePair header)throws Exception{
		BufferedReader in = null; 
		try {
			HttpClient client  = getHttpClient(); 
			HttpGet request = new HttpGet(); 
			request.setURI(new URI(url)); 
			request.setHeader(header.getName(), header.getValue());
			HttpResponse response = client.execute(request); 
			String html = EntityUtils.toString(response.getEntity());
//			in = new BufferedReader(new InputStreamReader(response.getEntity().getContent())); 
			InputStream stream = new ByteArrayInputStream(html.getBytes(Charset.defaultCharset()));
			in = new BufferedReader(new InputStreamReader(stream));
			StringBuffer sb = new StringBuffer("");
			String line = ""; 
			String NL = System.getProperty("line.separator");
			while ((line = in.readLine()) !=null) {
				sb.append(line+NL);
			}
			in.close();
			
			String result = sb.toString(); 
			return result; 
			
		}finally{
			if(in !=null){
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} 
		
		
	}
}
