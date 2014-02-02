package com.lesikapk.openradiobrony;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ErrorActivity extends Activity implements OnClickListener {
	
	Button retryButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_error);
		retryButton = (Button)findViewById(R.id.retry_button);
		retryButton.setOnClickListener(this);
		PlayerActivity.getThis().destroyMe();
		PlayerActivity.getThis().cancelSchedule();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.error, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		if(Utils.internetAvailable(getApplicationContext())) {
			Intent playerIntent = new Intent(getApplicationContext(), PlayerActivity.class);
			startActivity(playerIntent);
			overridePendingTransition(0, 0);
			finish();
			overridePendingTransition(0, 0);
		}
		else {
			final AlertDialog.Builder errorDialog = new AlertDialog.Builder(this);
			errorDialog
				.setTitle(R.string.error_title)
				.setMessage(R.string.error_descr_dialog)
				.setCancelable(false)
				.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						
					}
				})
				.create()
				.show();
		}
	}
	
}
