package com.lesikapk.openradiobrony;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class PlayerActivity extends Activity implements OnErrorListener, OnSeekBarChangeListener {

	private static boolean isPlaying = false;
	private static boolean isPrepared = false;
	private boolean isVisible = false;
	private Context context;
	private int currentVolume;
	private SeekBar seekBar;
	private AudioManager audioManager;
	private static PlayerActivity mThis;
	private static MediaPlayer mediaPlayer;
	private ScheduledExecutorService scheduleTaskExecutor;
	private TextView musicTitle;
	private TextView musicArtist;
	private PendingIntent playerActivityIntent;
	private PendingIntent errorActivityIntent;
	private NotificationManager notificationManager;
	private NotificationCompat.Builder notificationBuilder;
	private final int notId = 63;
	private int i = 0;
//	private final MusicInformation musicInformation = new MusicInformation();
	public static Handler handler;
	private String title;
	public static final String streamUrl = "http://www.radiobrony.fr:8000/live";
	public static final String jsonUrl = "http://radiobrony.fr/wp-content/plugins/radiobrony/ajax/API/music_info.json"; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		playerActivityIntent = PendingIntent.getActivity(this, 0, new Intent(this, PlayerActivity.class), 0);
		errorActivityIntent = PendingIntent.getActivity(this, 0, new Intent(this, ErrorActivity.class), 0);
		checkInternetAndClose();
		// Background stuff
	    View view = this.getWindow().getDecorView();
	    view.setBackgroundColor(getResources().getColor(R.color.activity_background));
		isVisible = true;
//		ErrorHandler.checkForError(this);
		setContentView(R.layout.activity_player);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);

		// Card stuff
		musicTitle = (TextView)findViewById(R.id.music_title);
		musicArtist = (TextView)findViewById(R.id.music_artist);
		
		// Volume stuff
		seekBar = (SeekBar) findViewById(R.id.volume_seekbar);
		audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		seekBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
		seekBar.setOnSeekBarChangeListener(this);
    	seekBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));

		mThis = this;
		handler = new Handler();
		
		// Music information stuff
		scheduleTaskExecutor = Executors.newScheduledThreadPool(5);
	    // This schedules a task to run every 10 seconds
	    scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
	    	public void run() {
	    		// Parsing JSON
	    		MusicInformation.getMusicInformation(getApplicationContext(), jsonUrl);
	    		// Needed to update stuff in UI, otherwise exception
	    		runOnUiThread(new Runnable() {
	    			@Override
	    			public void run() {
	    				// Check internet connection
	    				checkInternetAndClose();
	    				// Check if title changed since last check
	    				if(i == 0 || title != MusicInformation.getTitle(context)) {
		    				musicTitle.setText(MusicInformation.getTitle(context));
		    				musicArtist.setText(MusicInformation.getArtist(context));
		    				if(notificationBuilder != null) {
			    				notificationBuilder.setContentTitle(MusicInformation.getTitle(context));
			    				notificationBuilder.setContentText(MusicInformation.getArtist(context));
			    				notificationBuilder.setContentInfo(MusicInformation.getListeners() + " " + getResources().getString(R.string.notific_listeners));
			    				notificationBuilder.setContentIntent(playerActivityIntent);
			    				notificationManager.notify(notId, notificationBuilder.build());
		    				}
		    				title = MusicInformation.getTitle(context);
	    				}
	    				if(i == 0) {
	    					i++;
	    				}
	    			}
	    		});
	    	}
	    }, 0, 10, TimeUnit.SECONDS);
	}

	public void cancelSchedule() {
		scheduleTaskExecutor.shutdown();
	}
	
	public void jsonParseError() {
		if(notificationBuilder != null) {
			notificationBuilder.setContentTitle(getResources().getString(R.string.nojson));
			notificationBuilder.setContentText(getResources().getString(R.string.nojson_descr));
			notificationBuilder.setContentInfo("");
			notificationBuilder.setContentIntent(playerActivityIntent);
			notificationManager.notify(notId, notificationBuilder.build());
		}
	}
	
	private void checkInternetAndClose() {
		if(!Utils.internetAvailable(getApplicationContext())) {
			if(isPlaying() || isPrepared()) {
				stopPlaying();
			}
			if(notificationBuilder != null) {
				notificationBuilder.setContentTitle(getResources().getString(R.string.notific_noconnection));
				notificationBuilder.setContentText(getResources().getString(R.string.notific_noconnection_descr));
				notificationBuilder.setContentInfo("");
				notificationBuilder.setContentIntent(errorActivityIntent);
				notificationManager.notify(notId, notificationBuilder.build());
			}
			if(isVisible) {
				Intent errorIntent = new Intent(getApplicationContext(), ErrorActivity.class);
				startActivity(errorIntent);
				overridePendingTransition(0, 0);
			}
			destroyMe();
			overridePendingTransition(0, 0);
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		isVisible = false;
	};
	
    private void displayNotification() {
//    	PendingIntent pauseIntent = PendingIntent.getActivity(this, 0, new Intent(this, onPausePressed.class), 0);
//    	PendingIntent stopIntent = PendingIntent.getActivity(this, 0, new Intent(this, onStopPressed.class), 0);
		
    	notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
    	
    	notificationBuilder = new NotificationCompat.Builder(this)
		        .setContentTitle(getResources().getString(R.string.notific_title))
		        .setContentText(getResources().getString(R.string.notific_artist))
		        .setSmallIcon(R.drawable.ic_stat_playing)
		        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.radiobrony_squarecover_small_transparent))
		        .setContentIntent(playerActivityIntent)
		        .setAutoCancel(true)
		        .setOngoing(true);
//		        .addAction(R.drawable.btn_playback_play_pressed, "Play", playIntent)
//		        .addAction(R.drawable.btn_playback_pause_pressed, "Pause", pauseIntent)
//		        .addAction(R.drawable.btn_playback_stop_pressed, "Stop", stopIntent);
		  
		notificationManager.notify(notId, notificationBuilder.build());
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    }
    
    public void destroyMe() {
    	onDestroy();
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
		displayNotification();
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
		}
		catch (IllegalArgumentException e) {
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
        if(notificationBuilder != null) {
	        notificationBuilder.setSmallIcon(R.drawable.ic_stat_playing);
	        notificationManager.notify(notId, notificationBuilder.build());
        }
        isPrepared = true;
		isPlaying = true;
	}
	
	public void pausePlaying() {
		if (isPrepared) {
			if(notificationBuilder != null) {
				notificationBuilder.setSmallIcon(R.drawable.ic_stat_pause);
				notificationManager.notify(notId, notificationBuilder.build());
			}
			mediaPlayer.pause();
			isPlaying = false;
		}
	}
	
	public void stopPlaying() {
		if (isPrepared) {
			mediaPlayer.stop();
			if(notificationBuilder != null) {
				notificationManager.cancel(notId);
			}
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
	
	public class onPlayPressed {

		public onPlayPressed() {
			if (!PlayerActivity.isPlaying()) {
	    		if (PlayerActivity.isPrepared()) {
	    			PlayerActivity.getThis().resumePlaying();
	    		}
	    		else {
	    			PlayerActivity.getThis().startPlaying();
	    		}
	    	}
	    	else {
	    		PlayerActivity.getThis().pausePlaying();
	    	}
		}
	}
	
	public class onPausePressed {
	
	}
	
	public class onStopPressed {
		public onStopPressed() {
			if(PlayerActivity.isPrepared() && PlayerActivity.isPlaying()) {
    			PlayerActivity.getThis().stopPlaying();
			}
		}
	}

}
