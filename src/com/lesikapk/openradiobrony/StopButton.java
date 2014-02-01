package com.lesikapk.openradiobrony;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.Toast;

public class StopButton extends Button implements OnClickListener, OnLongClickListener {

	private static StopButton mThis;
	
    public StopButton(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(this);
        setOnLongClickListener(this);
        mThis = this;
        disableButton();
    }

    @Override
    public void onClick(final View v) {
		if(PlayerActivity.isPrepared() && PlayerActivity.isPlaying()) {
    			PlayerActivity.getThis().stopPlaying();
    	}
    }

    @Override
    public boolean onLongClick(final View view) {
        Toast.makeText(getContext(), "Press this button to stop the stream.", Toast.LENGTH_SHORT).show();
        return true;
    }
    
    public static StopButton getThis() {
    	return mThis;
    }
    
    public void enableButton() {
    	setEnabled(true);
    }
    
    public void disableButton() {
    	setEnabled(false);
    }
}
