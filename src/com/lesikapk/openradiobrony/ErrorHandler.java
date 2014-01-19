package com.lesikapk.openradiobrony;

import java.net.HttpURLConnection;
import java.net.URL;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.webkit.URLUtil;
import android.widget.Toast;

public class ErrorHandler {
	
	public static Context myContext;
	public static int code;
	
	public static void checkForError(Context context) {
		checkConnection(context);
		checkUrl(context);
	}
	
	private static void checkUrl(Context context) {
		myContext = context;
		new Thread(new Runnable() {
	        public void run() {
	        	try {
	        		String url = PlayerActivity.streamUrl;
	        		URL u = new URL (url);
	        		HttpURLConnection huc = (HttpURLConnection)u.openConnection (); 
	    			huc.setRequestMethod("GET"); 
	    			huc.connect(); 
	    			code = huc.getResponseCode();
	    			if (code == 404) {
	    				PlayerActivity.handler.post(new Runnable() { // This thread runs in the UI
	                        @Override
	                        public void run() {
	                        	showFatalDialog("URL not valid",
	                    				"The stream URL appears not to be valid."+"\n"+"This is not our fault"+"\n"+"Most likely, BronyRadio changed their stream URL. Try updating this app to a newer version or contact me.",
	                    				"Okay", myContext);
	                        }
	                    });
	    			}
	    	    }
	    		catch (Exception e) {
	    			e.printStackTrace();
	    		}
	        }
	    }).start();
	}
	
	private static void checkConnection(Context context) {
	    boolean haveConnectedWifi = false;
	    boolean haveConnectedMobile = false;
	    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo[] netInfo = cm.getAllNetworkInfo();
	    for (NetworkInfo ni : netInfo) {
	        if (ni.getTypeName().equalsIgnoreCase("WIFI"))
	            if (ni.isConnected())
	                haveConnectedWifi = true;
	        if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
	            if (ni.isConnected())
	                haveConnectedMobile = true;
	    }
	    if (!haveConnectedWifi && !haveConnectedMobile) {
	    	showFatalDialog("No network connectivity",
	    			"It appears that your device doesn't have a working internet connecting, which is necessary to run this app." + "\n" + "The application will now close.",
	    			"Okay", context);
	    }
	}
	private static void showFatalDialog(String title, String message, String buttontext, Context context) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
	    dialog.setTitle(title)
		    .setMessage(message)
		    .setCancelable(false)
		    .setNegativeButton(buttontext, new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		            dialog.dismiss();
		            android.os.Process.killProcess(android.os.Process.myPid());
		        }
		     })
		    .show();
	}
}
