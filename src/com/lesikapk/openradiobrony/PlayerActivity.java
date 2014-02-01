package com.lesikapk.openradiobrony;

import java.io.IOException;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.pheelicks.visualizer.VisualizerView;

public class PlayerActivity extends Activity implements OnErrorListener, OnSeekBarChangeListener {

	private static boolean isPlaying = false;
	private static boolean isPrepared = false;
	private int currentVolume;
	private SeekBar seekBar;
	private AudioManager audioManager;
	private static PlayerActivity mThis;
	private static MediaPlayer mediaPlayer;
	private VisualizerView mVisualizerView;
	public static final String streamUrl = "http://www.radiobrony.fr:8000/live";
	public static final String jsonUrl = "http://radiobrony.fr/wp-content/plugins/radiobrony/ajax/API/music_info.json"; 
	public static Handler handler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		displayNotification();
		// Background stuff
	    View view = this.getWindow().getDecorView();
	    view.setBackgroundColor(getResources().getColor(R.color.activity_background));
		
		ErrorHandler.checkForError(this);
		setContentView(R.layout.activity_player);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);

		// Volume stuff
		seekBar = (SeekBar) findViewById(R.id.volume_seekbar);
		audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		seekBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
		seekBar.setOnSeekBarChangeListener(this);
    	seekBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));

		mThis = this;
		handler = new Handler();
	}

	public void updateInformation() {
		
	}
	
    private void displayNotification() {
		Intent intent = new Intent(this, PlayerActivity.class);
		PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
		
		// build notification
		// the addAction re-use the same intent to keep the example short
		Notification n = new NotificationCompat.Builder(this)
		        .setContentTitle(getResources().getString(R.string.notific_title))
		        .setContentText(getResources().getString(R.string.notific_artist))
		        .setSmallIcon(R.drawable.ic_stat_playing)
		        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.cover))
		        .setContentIntent(pIntent)
		        .setAutoCancel(true)
		        .addAction(R.drawable.btn_playback_play_pressed, "Play", pIntent)
		        .addAction(R.drawable.btn_playback_stop_pressed, "Pause", pIntent)
		        .build();
		  
		NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		notificationManager.notify(0, n);
    }
    
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    switch (keyCode) {
	    case KeyEvent.KEYCODE_VOLUME_UP:
	    	currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)+1;
	    	audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, AudioManager.FLAG_SHOW_UI);
	    	seekBar.setProgress(currentVolume);
	        return true;
	    case KeyEvent.KEYCODE_VOLUME_DOWN:
	    	currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)-1;
	    	audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, AudioManager.FLAG_SHOW_UI);
	    	seekBar.setProgress(currentVolume);
	        return true;
        case KeyEvent.KEYCODE_BACK:
        	this.finish();
	    default:
	        return false;
	    }
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

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
	}
}
