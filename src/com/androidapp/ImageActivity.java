package com.androidapp;

import com.polites.android.GestureImageView;

import android.app.Activity;
import android.content.Intent;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

public class ImageActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zoom_image);

		Intent extras = getIntent();

		Drawable image = Drawable.createFromPath(extras
				.getStringExtra("picture"));

		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);

		GestureImageView view = new GestureImageView(this);
		view.setImageDrawable(image);
		view.setLayoutParams(params);

		ViewGroup layout = (ViewGroup) findViewById(R.id.zoomLayout);

		layout.addView(view);
	}
}
