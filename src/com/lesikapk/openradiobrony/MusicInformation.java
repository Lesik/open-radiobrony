package com.lesikapk.openradiobrony;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONObject;

import android.content.Context;

public class MusicInformation {

	private static String title;
	private static String artist;
	private static int listeners;
	
	public static void getMusicInformation(Context context, String jsonUrl) {
		DefaultHttpClient   httpclient = new DefaultHttpClient(new BasicHttpParams());
		HttpPost httppost = new HttpPost("http://radiobrony.fr/wp-content/plugins/radiobrony/ajax/API/music_info.json");
		// Depends on your web service
		httppost.setHeader("Content-type", "application/json");

		InputStream inputStream = null;
		String result = null;
		try {
		    HttpResponse response = httpclient.execute(httppost);           
		    HttpEntity entity = response.getEntity();

		    inputStream = entity.getContent();
		    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
		    StringBuilder sb = new StringBuilder();

		    String line = null;
		    while ((line = reader.readLine()) != null) {
		        sb.append(line + "\n");
		    }
		    result = sb.toString();
		    JSONObject generalJson = new JSONObject(result);
		    listeners = generalJson.getInt("listeners");
		    JSONObject nowPlayingJson = new JSONObject(result).getJSONObject("now_playing");
		    title = nowPlayingJson.getString("track");
			artist = nowPlayingJson.getString("artist");
		}
		catch (Exception e) { 
		    PlayerActivity.getThis().jsonParseError();
		}
		finally {
		    try {
		    	if(inputStream != null) {
		    		inputStream.close();
		    	}	
	    	}
		    catch(Exception e){
		    	PlayerActivity.getThis().jsonParseError();
		    }
    	}
	}
	
	public static String getTitle(Context context) {
		if(title != "" || artist != null) {
			return title;
		}
		else {
			return context.getResources().getString(R.string.nojson);
		}
	}
	
	public static String getArtist(Context context) {
		if(artist != "" || artist != null) {
			return artist;
		}
		else {
			return context.getResources().getString(R.string.nojson_descr);
		}
	}
	
	public static int getListeners() {
		return listeners;
	}
}
