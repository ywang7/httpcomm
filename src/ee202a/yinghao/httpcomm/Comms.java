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
import org.json.JSONObject;

import android.util.Log;

public class Comms {
	
	private static final String serverURL="http://128.97.93.201:22050";
	private static final String TAG = "Comms";

	public static int sendHttpPost(String URL, JSONObject jsonObjSend) {

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

				// Transform the String into a JSONObject
				//JSONObject jsonObjRecv = new JSONObject(resultString);
				// Raw DEBUG output of our received JSON object:
				Log.i(TAG,"Response="+resultString);

				response.getStatusLine().getStatusCode();
			} 

		}
		catch (Exception e)
		{
			// More about HTTP exception handling in another tutorial.
			// For now we just print the stack trace.
			e.printStackTrace();
		}
		return 0;
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
	
	public static boolean addFriend(JSONObject friendinfo) {
		Log.i("addfriend","begins");
		boolean finalresult = false;
		int statusresult=sendHttpPost(serverURL+"/addfriend", friendinfo);
		if((statusresult>=200) & (statusresult<300))
			finalresult = true;
		
		Log.i("addfriend","ends");
		
		return finalresult;
	}
	
	public static boolean deleteFriend(JSONObject friendinfo) {
		Log.i("deletefriend","begins");
		boolean finalresult = false;
		int statusresult=sendHttpPost(serverURL+"/deletefriend", friendinfo);
		if((statusresult>=200) & (statusresult<300))
			finalresult = true;
		
		Log.i("deletefriend","ends");
		
		return finalresult;
	}
	
	public static JSONObject getData() {
		Log.i("getData","begins");
		
		JSONObject result=sendHttpGet(serverURL+"/getdata");
		if(result == null)
			Log.v("getData","Error");
		
		Log.i("getData","ends");
		
		return result;
	}
	
	public static boolean writeData(JSONObject data) {
		Log.i("writeData","begins");
		boolean finalresult = false;
		int statusresult=sendHttpPost(serverURL+"/writedata", data);
		if((statusresult>=200) & (statusresult<300))
			finalresult = true;
		
		Log.i("writeData","ends");
		
		return finalresult;
	}
	
	public static boolean deletemyData(JSONObject deleteid) {
		Log.i("deletemyData","begins");
		boolean finalresult = false;
		int statusresult=sendHttpPost(serverURL+"/deletemydata", deleteid);
		if((statusresult>=200) & (statusresult<300))
			finalresult = true;
		
		Log.i("deletemyData","ends");
		
		return finalresult;
	}
}
