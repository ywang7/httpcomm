package ee202a.yinghao.httpcomm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

public class Comms {
	
	private static final String serverURL="http://128.97.93.201:22050";
	private static final String TAG = "Comms";

	public static JSONArray sendHttpPost(String URL, JSONObject jsonObjSend) {

		try {
			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpPost httpPostRequest = new HttpPost(URL);

			StringEntity se;
			se = new StringEntity(jsonObjSend.toString());

			// Set HTTP parameters
			httpPostRequest.setEntity(se);
			httpPostRequest.setHeader("Accept", "application/json");
			httpPostRequest.setHeader("Content-type", "application/json");
			//httpPostRequest.setHeader("Accept-Encoding", "gzip"); // only set this parameter if you would like to use gzip compression

			long t = System.currentTimeMillis();
			HttpResponse response = (HttpResponse) httpclient.execute(httpPostRequest);
			Log.i(TAG, "HTTPResponse received in [" + (System.currentTimeMillis()-t) + "ms]");
			Log.i(TAG,response.getStatusLine().getStatusCode() +":"+ response.getStatusLine().getReasonPhrase());
			// Get hold of the response entity (-> the data):
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				// Read the content stream
				InputStream instream = entity.getContent();
				Header contentEncoding = response.getFirstHeader("Content-Encoding");
				Header contentType = entity.getContentType();
				Log.i(TAG, "ContentType is " + contentType.getValue());
				if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
					instream = new GZIPInputStream(instream);
				}

				// convert content stream to a String
				String resultString= convertStreamToString(instream);
				instream.close();
				resultString = resultString.substring(1,resultString.length()-1); // remove wrapping "[" and "]"

				//Transform the String into a JSONArray
				JSONArray jsonArrayRecv = new JSONArray(resultString);
				// Raw DEBUG output of our received JSON object:
				Log.i(TAG,"Response="+resultString);

				response.getStatusLine().getStatusCode();
				return jsonArrayRecv;
			} 

		}
		catch (Exception e)
		{
			// More about HTTP exception handling in another tutorial.
			// For now we just print the stack trace.
			e.printStackTrace();
		}
		return null;
		
	}

	public static JSONObject sendHttpGet(String URL) {
		try {
			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpGet httpGetRequest = new HttpGet(URL);

			//StringEntity se;
			//se = new StringEntity(jsonObjSend.toString());

			// Set HTTP parameters
			//httpGetRequest.setEntity(se);
			httpGetRequest.setHeader("Accept", "application/json");
			httpGetRequest.setHeader("Content-type", "application/json");
			//httpPostRequest.setHeader("Accept-Encoding", "gzip"); // only set this parameter if you would like to use gzip compression

			long t = System.currentTimeMillis();
			HttpResponse response = (HttpResponse) httpclient.execute(httpGetRequest);
			Log.i(TAG, "HTTPResponse received in [" + (System.currentTimeMillis()-t) + "ms]");
			Log.i(TAG, response.getStatusLine().getStatusCode() +":"+ response.getStatusLine().getReasonPhrase());
			// Get hold of the response entity (-> the data):
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				// Read the content stream
				InputStream instream = entity.getContent();
				Header contentEncoding = response.getFirstHeader("Content-Encoding");
				Header contentType = entity.getContentType();
				Log.i(TAG, "ContentType is " + contentType.getValue());
				if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
					instream = new GZIPInputStream(instream);
				}

				// convert content stream to a String
				String resultString= convertStreamToString(instream);
				instream.close();
				resultString = resultString.substring(1,resultString.length()-1); // remove wrapping "[" and "]"

				// Transform the String into a JSONObject
				JSONObject jsonObjRecv = new JSONObject(resultString);
				// Raw DEBUG output of our received JSON object:
				Log.i(TAG,"Response="+resultString);

				return jsonObjRecv;
			} 

		}
		catch (Exception e)
		{
			// More about HTTP exception handling in another tutorial.
			// For now we just print the stack trace.
			e.printStackTrace();
		}
		return null;
	}

	private static String convertStreamToString(InputStream is) {
		/*
		 * To convert the InputStream to String we use the BufferedReader.readLine()
		 * method. We iterate until the BufferedReader return null which means
		 * there's no more data to read. Each line will appended to a StringBuilder
		 * and returned as String.
		 * 
		 * (c) public domain: http://senior.ceng.metu.edu.tr/2009/praeda/2009/01/11/a-simple-restful-client-at-android/
		 */
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	
	public static String getInternetData() throws Exception{
		
		InputStream in= null;
		String data= "test";
		try{
			
			HttpClient client = new DefaultHttpClient();
			URI website = new URI("http://www.mybringback.com/");
			HttpGet request = new HttpGet();
			
			
			request.setURI(website);
			
			
			HttpResponse response = client.execute(request);
			
			
			in = response.getEntity().getContent();
			//StringBuffer sb=new StringBuffer("");
			
			
			
			data=convertStreamToString(in);
			Log.v("data in the Comms class", data);
			in.close();
			return data;
		}finally{
			if(in != null){
				try{
					in.close();
					return data;
				}catch (Exception e){
					e.printStackTrace();
				}
			}
		}
		
		
		//return data;
		
	}
	
	public static JSONArray userReg(JSONObject info) {
		Log.i(TAG+"userReg","begins");
		JSONArray finalresult = sendHttpPost(serverURL+"/userreg", info);
		if(finalresult == null)
			Log.i(TAG+"userReg","response is null");
		else 
			Log.i(TAG+"userReg",finalresult.toString());
		Log.i(TAG+"userReg","ends");
		return finalresult;
	}
	
	public static JSONArray userLogin(JSONObject info) {
		Log.i(TAG+"userLogin","begins");
		JSONArray finalresult = sendHttpPost(serverURL+"/userlogin", info);
		if(finalresult == null)
			Log.i(TAG+"userLogin","response is null");
		else 
			Log.i(TAG+"userLogin",finalresult.toString());
		Log.i(TAG+"userReg","ends");
		return finalresult;
	}
	
	public static JSONArray addFriend(JSONObject friendinfo) {
		Log.i(TAG+"addfriend","begins");
		JSONArray finalresult = sendHttpPost(serverURL+"/addfriend", friendinfo);
		if(finalresult == null)
			Log.i(TAG+"addfriend","response is null");
		else 
			Log.i(TAG+"addfriend",finalresult.toString());
		Log.i(TAG+"addfriend","ends");
		
		return finalresult;
	}
	
	public static JSONArray deleteFriend(JSONObject friendinfo) {
		Log.i(TAG+"deletefriend","begins");
		JSONArray finalresult = sendHttpPost(serverURL+"/deletefriend", friendinfo);
		if(finalresult == null)
			Log.i(TAG+"deletefriend","response is null");
		else 
			Log.i(TAG+"deletefriend",finalresult.toString());
		Log.i(TAG+"deletefriend","ends");
		
		return finalresult;
	}
	
	public static JSONArray getData(JSONObject info) {
		Log.i(TAG+"getData","begins");
		JSONArray finalresult = sendHttpPost(serverURL+"/getdata", info);
		if(finalresult == null)
			Log.i(TAG+"getdata","response is null");
		else 
			Log.i(TAG+"getdata",finalresult.toString());
		Log.i(TAG+"getData","ends");
		return finalresult;
	}
	
	public static JSONArray writeArriveData(JSONObject data) {
		Log.i(TAG+"writeArriveData","begins");
		JSONArray finalresult = sendHttpPost(serverURL+"/writearrivedata", data);
		if(finalresult == null)
			Log.i(TAG+"writeArriveData","response is null");
		else 
			Log.i(TAG+"writeArriveData",finalresult.toString());
		Log.i(TAG+"writeArriveData","ends");
		
		return finalresult;
	}
	
	public static JSONArray writeLeaveData(JSONObject data) {
		Log.i(TAG+"writeLeaveData","begins");
		JSONArray finalresult = sendHttpPost(serverURL+"/writeleavedata", data);
		if(finalresult == null)
			Log.i(TAG+"writeLeaveData","response is null");
		else 
			Log.i(TAG+"writeLeaveData",finalresult.toString());
		Log.i(TAG+"writeLeaveData","ends");
		
		return finalresult;
	}
	
	public static JSONArray deletemyData(JSONObject deleteid) {
		Log.i(TAG+"deletemyData","begins");
		JSONArray finalresult = sendHttpPost(serverURL+"/deletemydata", deleteid);
		if(finalresult == null)
			Log.i(TAG+"deletemydata","response is null");
		else 
			Log.i(TAG+"deletemydata",finalresult.toString());
		Log.i(TAG+"deletemyData","ends");
		
		return finalresult;
	}
}
