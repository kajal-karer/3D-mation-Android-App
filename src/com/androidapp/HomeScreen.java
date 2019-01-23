package com.androidapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class HomeScreen extends Activity {

	private Button captureImageBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_screen);

		captureImageBtn = (Button) findViewById(R.id.captureImage);
		captureImageBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(HomeScreen.this,
						CaptureImageActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				// SplashScreen.this.finish();
			}
		});

		Button libraryImageBtn = (Button) findViewById(R.id.libraryImage);
		libraryImageBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(HomeScreen.this,
						LibraryActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				// SplashScreen.this.finish();
			}
		});
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

		AlertDialog.Builder alert1 = new AlertDialog.Builder(this);
		alert1.setTitle("Alert");
		alert1.setMessage("Are you sure want to quit?");

		alert1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
				HomeScreen.this.finish();
			}
		});

		alert1.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});

		alert1.show();
	}
}
