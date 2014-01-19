package com.lesikapk.openradiobrony;

import java.io.IOException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;

public class PlayerActivity extends Activity implements OnErrorListener  {

	private static boolean isPlaying = false;
	private static boolean isPrepared = false;
	private static PlayerActivity mThis;
	private static MediaPlayer mediaPlayer;
	public static final String streamUrl = "http://www.radiobrony.fr:8000/live";
	public static Handler handler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ErrorHandler.checkForError(this);
		setContentView(R.layout.activity_player);
		mThis = this;
		handler = new Handler();
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
		PlayButton.getThis().disableButton();
		StopButton.getThis().disableButton();
		final ProgressDialog progress = new ProgressDialog(this);
    	progress.setTitle("Loading");
    	progress.setMessage("Preparing stream...");
    	progress.setCancelable(false);
    	progress.show();
		try {
			mediaPlayer.setDataSource(streamUrl);
			mediaPlayer.setOnErrorListener(this);
			mediaPlayer.setOnPreparedListener(new OnPreparedListener() {
			    @Override
			    public void onPrepared(MediaPlayer mp) {
			    	progress.dismiss();
					mediaPlayer.start();
			        PlayButton.getThis().iconPause();
			        PlayButton.getThis().enableButton();
			        StopButton.getThis().enableButton();
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
	
	public static boolean isPrepared() {
		return isPrepared;
	}
	
	public void resumePlaying() {
		mediaPlayer.start();
        PlayButton.getThis().iconPause();
		isPrepared = true;
		isPlaying = true;
	}
	
	public void pausePlaying() {
		if (isPrepared) {
			mediaPlayer.pause();
			isPlaying = false;
		}
	}
	
	public void stopPlaying() {
		if (isPrepared) {
			mediaPlayer.stop();
			PlayButton.getThis().iconPlay();
			isPlaying = false;
			isPrepared = false;
		}
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		return false;
	}
	
}
