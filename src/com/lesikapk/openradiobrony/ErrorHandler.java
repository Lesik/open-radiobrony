package com.lesikapk.openradiobrony;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.webkit.URLUtil;

public class ErrorHandler {
	
	public static void checkForError(Context context) {
		checkConnection(context);
		checkUrl(context);
	}
	
	private static void checkUrl(Context context) {
		String url = PlayerActivity.streamUrl;
		if (!URLUtil.isValidUrl(url)) {
			showFatalDialog("URL not valid",
					"The stream URL appears not to be valid."+"\n"+"This is not our fault"+"\n"+"Most likely, BronyRadio changed their stream URL. Try updating this app to a newer version or contact me.",
					"Okay", context);
		}
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
		    .setNegativeButton(buttontext, new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		            dialog.dismiss();
		            android.os.Process.killProcess(android.os.Process.myPid());
		        }
		     })
		    .show();
	}
}
