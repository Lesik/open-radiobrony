package com.lesikapk.openradiobrony;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

public class PlayButton extends ImageButton implements OnClickListener, OnLongClickListener {

	private static PlayButton mThis;
	
    public PlayButton(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(this);
        setOnLongClickListener(this);
        mThis = this;
        enableButton();
    }

    @Override
    public void onClick(final View v) {
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
    		iconPlay();
    	}
    }

    @Override
    public boolean onLongClick(final View view) {
        Toast.makeText(getContext(), "Press this button to play or pause the stream.", Toast.LENGTH_SHORT).show();
        return true;
    }
    
    public static PlayButton getThis() {
    	return mThis;
    }
    
    public void enableButton() {
    	setEnabled(true);
    }
    
    public void disableButton() {
    	setEnabled(false);
    }

    public void iconPlay() {
        setContentDescription("Play");
        setImageResource(R.drawable.btn_playback_play);
    }
    
    public void iconPause() {
        setContentDescription("Pause");
        setImageResource(R.drawable.btn_playback_pause);
    }
}
