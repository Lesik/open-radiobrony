package com.lesikapk.openradiobrony;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class MusicInformation {

	private String title;
	private String artist;
	private int listeners;
	
	public void getMusicInformation(String jsonUrl) {
		RequestQueue reqQueue = Volley.newRequestQueue(getActivity());
		JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET, jsonUrl, null, 
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject arg0) {
						parseJson(arg0);
						PlayerActivity.getThis().updateInformation();
					}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				//TODO LOL
			}
		});
		reqQueue.add(jsonReq);
	}
	
	public void parseJson(JSONObject jsonObject) {
		try {
			JSONObject value = jsonObject.getJSONObject("data");
			JSONArray children = value.getJSONArray("children");
			for(int i = 0; i< children.length(); i++) {
				JSONObject child = children.getJSONObject(i).getJSONObject("data");
				title = (String) child.opt("title");
				if(title != null) {
					artist = child.optString("author");
					listeners = child.optInt("listeners");
				}
				else {
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
