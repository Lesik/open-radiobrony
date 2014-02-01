package com.lesikapk.openradiobrony;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.Toast;

public class PlayButton extends Button implements OnClickListener, OnLongClickListener {

	private static PlayButton mThis;
	
    public PlayButton(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(this);
        setOnLongClickListener(this);
        mThis = this;
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
    
    public void iconPlay() {
    	setText(getResources().getString(R.string.btn_playpause_play));
        setContentDescription(getResources().getString(R.string.btn_playpause_play));
        setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.btn_playback_play), null, null, null);
//        setImageResource(R.drawable.btn_playback_play);
    }
    
    public void iconPause() {
    	setText(getResources().getString(R.string.btn_playpause_pause));
        setContentDescription(getResources().getString(R.string.btn_playpause_pause));
        setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.btn_playback_pause), null, null, null);
//        setImageResource(R.drawable.btn_playback_pause);
    }
}
