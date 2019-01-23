package com.androidapp;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;

public class SplashScreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);

		File spinimation = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/3DMation");
		if (!spinimation.exists()) {
			spinimation.mkdir();
		}
		File temp = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/3DMation/Temp");
		if (!temp.exists()) {
			temp.mkdir();
			temp.deleteOnExit();

		}
		File parent = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/3DMation/parent");
		if (!parent.exists()) {
			parent.mkdir();

		}
		File children = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/3DMation/children");
		if (!children.exists()) {
			children.mkdir();

		}

		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			Intent intent = new Intent(SplashScreen.this, HomeScreen.class);

			@Override
			public void run() {
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				SplashScreen.this.finish();
			}
		}, 3000);
	}

}
