package com.lesikapk.openradiobrony;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

public class PlayButton extends ImageButton implements OnClickListener, OnLongClickListener {

    public PlayButton(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(this);
        setOnLongClickListener(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onClick(final View v) {
    	if (!PlayerActivity.isPlaying()) {
    		PlayerActivity.getThis().startPlaying();
    	}
    	else {
    		PlayerActivity.getThis().stopPlaying();
    	}
        updateState();
    }

    @Override
    public boolean onLongClick(final View view) {
        Toast.makeText(getContext(), "Press this button to play or pause the stream.", Toast.LENGTH_SHORT).show();
        return true;
    }
    
    public void enableButton() {
    	setEnabled(true);
    }

    public void updateState() {
        if (PlayerActivity.isPlaying()) {
            setContentDescription("Pause");
            setImageResource(R.drawable.btn_playback_pause);
        } else {
            setContentDescription("Play");
            setImageResource(R.drawable.btn_playback_play);
        }
    }

}
