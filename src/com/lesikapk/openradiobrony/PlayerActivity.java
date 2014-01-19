package com.lesikapk.openradiobrony;

import java.io.IOException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.view.Menu;

public class PlayerActivity extends Activity implements OnErrorListener  {

	private static boolean isPlaying = false;
	private static boolean isPrepared = false;
	private static PlayerActivity mThis;
	private static MediaPlayer mediaPlayer;
	public static final String streamUrl = "http://www.radiobrony.fr:8000/live";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ErrorHandler.checkForError(this);
		setContentView(R.layout.activity_player);
		mThis = this;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.player, menu);
		return true;
	}
	
	public static PlayerActivity getThis() {
		return mThis;
	}
	
	public void startPlaying() {
		mediaPlayer = new MediaPlayer();
		final ProgressDialog progress = new ProgressDialog(this);
    	progress.setTitle("Loading");
    	progress.setMessage("Preparing stream...");
    	progress.show();
		try {
			mediaPlayer.setDataSource(streamUrl);
			mediaPlayer.setOnErrorListener(this);
			mediaPlayer.setOnPreparedListener(new OnPreparedListener() {
			    @Override
			    public void onPrepared(MediaPlayer mp) {
			    	progress.dismiss();
					mediaPlayer.start();
					isPrepared = true;
					isPlaying = true;
			    }
			});
			mediaPlayer.prepareAsync();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static boolean isPlaying() {
		return isPlaying;
	}
	
	public void stopPlaying() {
		if (isPrepared) {
			mediaPlayer.stop();
			isPlaying = false;
		}
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		
		return false;
	}
	
}
