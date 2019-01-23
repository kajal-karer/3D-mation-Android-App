package com.androidapp;

import java.io.File;
import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class SpinViewAllCapture extends Activity implements OnClickListener {

	private int currentX = 0;
	private ImageView imageView = null;
	private Button panImage;
	private ArrayList<Drawable> imageArray = null;
	private ArrayList<String> dltArray = null;
	private File chidren = null;
	private Button img_Share, img_Delete, back_home,zoom;
	public static boolean loginCheckSpinViewAll = false;
	private int count;
	private String filname = null;
	private boolean imageTime = false;

	// ************************send videoSpin**************************
	// private String imageSubString;

	// **********************************************************************
	// private String nameChck = null;
	private ProgressDialog pd;
	private long disWidth = 0;
	private int valX = 0;

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.spin_view_all_capture);

		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		disWidth = size.x;
		// **************
		pd = new ProgressDialog(this);
		pd.setCancelable(false);
		pd.setMessage("Please wait..");

		imageView = (ImageView) findViewById(R.id.full_image_view);
		imageView.setScaleType(ScaleType.FIT_XY);
		
		zoom = (Button)findViewById(R.id.image_zoom);
		zoom.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				File imageList = new File(Environment
						.getExternalStorageDirectory()
						+ "/3DMation/children/"
						+ filname
						+ "_"
						+ valX
						+ ".jpg");
				String path = imageList.getAbsolutePath();

				Intent intent = new Intent(SpinViewAllCapture.this,
						ImageActivity.class);
				intent.putExtra("picture", path);
				startActivity(intent);
			}
		});
		panImage = (Button) findViewById(R.id.imageview_pan_all_spin);

		imageArray = new ArrayList<Drawable>();

		dltArray = new ArrayList<String>();

		img_Share = (Button) findViewById(R.id.img_share_spin_view);
		img_Share.setOnClickListener(this);

		img_Delete = (Button) findViewById(R.id.img_delete_spin_view);
		img_Delete.setOnClickListener(this);

		back_home = (Button) findViewById(R.id.back_home);
		back_home.setOnClickListener(this);

		Intent i = getIntent();
		String name = i.getExtras().getString("id");
		// nameChck = name.substring(0, 1);
		String[] nameid = name.split("_");
		String suffix = nameid[1].replaceAll(".jpg", "");

		count = Integer.parseInt(suffix);
		filname = nameid[0];

		pd.show();
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				showImageMethod();
			}
		});
		t.start();
	}

	private void showImageMethod() {
		File image_Parent = new File(Environment.getExternalStorageDirectory()
				+ "/3DMation/parent/" + filname + "_" + count + ".jpg");

		for (int j = 0; j < count; j++) {
			chidren = new File(Environment.getExternalStorageDirectory()
					+ "/3DMation/children/" + filname + "_" + j + ".jpg");

			Drawable image = Drawable.createFromPath(chidren.getAbsolutePath());
			imageArray.add(image);

			String path = chidren.getAbsolutePath();
			String pathImageParent = image_Parent.getAbsolutePath();

			dltArray.add(path);
			dltArray.add(pathImageParent);

		}
		pd.dismiss();
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				imageView.setImageDrawable(imageArray.get(0));
			}
		});

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		panImage.setVisibility(View.INVISIBLE);
		if (imageTime == true) {
			imageTime = false;
		}
		int value1 = (int) (disWidth / imageArray.size());
		int x = (int) event.getX();
		currentX = x;
		valX = currentX / value1;
		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE:
			if (imageArray.size() > valX) {
				imageView.setImageDrawable(imageArray.get(valX));

			}
			break;
		}

		return false;
	}

	/*
	 * private void setImageInImageView(Boolean isForwaward) { if (isForwaward)
	 * { imageCount++; if (imageCount >= imageArray.size()) { imageCount = 0;
	 * 
	 * } imageView.setImageDrawable(imageArray.get(imageCount));
	 * 
	 * } else { imageCount--; if (imageCount < 0) { imageCount =
	 * imageArray.size() - 1;
	 * 
	 * } imageView.setImageDrawable(imageArray.get(imageCount)); } }
	 */

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {

		case R.id.back_home:
			arrayFileClean(imageArray);
			Intent intent = new Intent(getApplicationContext(),
					LibraryActivity.class);
			startActivity(intent);
			this.finish();
			break;
		case R.id.img_share_spin_view:

			break;
		case R.id.img_delete_spin_view:

			AlertDialog.Builder alert1 = new AlertDialog.Builder(this);
			alert1.setMessage("Are you sure want to delete the spin?");
			alert1.setTitle("Message");

			alert1.setPositiveButton("Delete",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							for (String strPath : dltArray) {
								File delFile = new File(strPath);
								delFile.delete();
							}
							arrayFileClean(imageArray);
							Intent intent = new Intent(getApplicationContext(),
									LibraryActivity.class);
							startActivity(intent);
							finish();
						}
					});

			alert1.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
						}
					});

			alert1.show();
			break;
		default:
			break;

		}

	}

	public void arrayFileClean(ArrayList<Drawable> arrayName) {
		if (arrayName.size() > 0) {
			for (int i = arrayName.size() - 1; i >= 0; i--) {
				arrayName.remove(i);
			}
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		arrayFileClean(imageArray);
		Intent intent = new Intent(getApplicationContext(),
				LibraryActivity.class);
		startActivity(intent);
		this.finish();
	}
}