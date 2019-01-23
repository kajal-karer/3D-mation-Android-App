package com.androidapp;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import android.graphics.Point;

import android.graphics.drawable.Drawable;

public class CaptureImageViewer extends Activity implements OnClickListener {

	private ImageView _mainImageView = null;
	private Button panImage;
	private ArrayList<Drawable> imageArray = null;
	// private int imageCount = 0;
	private int currentX = 0;
	private File temp;
	private File parent;
	private Button img_Save, img_Share, img_Delete, btn_Back, zoom;
	// private boolean bottonFlag = false;

	public static boolean loginCheck = false;

	private boolean imageTime = false;

	private long disWidth = 0;
	private ProgressDialog pd;
	private int valX = 0;

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.auto_capture_spin_view);

		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		disWidth = size.x;
		// **************
		pd = new ProgressDialog(this);
		pd.setCancelable(false);
		pd.setMessage("Please wait..");

		_mainImageView = (ImageView) findViewById(R.id.imageviewspin);
		_mainImageView.setScaleType(ScaleType.FIT_XY);

		zoom = (Button) findViewById(R.id.img_zoom);
		zoom.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				File imageList[] = temp.listFiles();
				String path = imageList[valX].getAbsolutePath();

				Intent intent = new Intent(CaptureImageViewer.this,
						ImageActivity.class);
				intent.putExtra("picture", path);
				startActivity(intent);
			}
		});

		panImage = (Button) findViewById(R.id.imageview_pan);

		imageArray = new ArrayList<Drawable>();

		temp = new File(Environment.getExternalStorageDirectory()
				+ "/3DMation/Temp/");
		temp.mkdir();

		parent = new File(Environment.getExternalStorageDirectory()
				+ "/3DMation/parent/");
		parent.mkdir();

		/*
		 * img_Arrow = (Button) findViewById(R.id.img_arrow);
		 * img_Arrow.setOnClickListener(this);
		 */

		img_Save = (Button) findViewById(R.id.img_save);
		img_Save.setOnClickListener(this);

		img_Share = (Button) findViewById(R.id.img_share);
		img_Share.setOnClickListener(this);

		img_Delete = (Button) findViewById(R.id.img_delete);
		img_Delete.setOnClickListener(this);

		btn_Back = (Button) findViewById(R.id.btn_Back_Camera);
		btn_Back.setOnClickListener(this);

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
		File imageList[] = temp.listFiles();

		for (int i = 0; i < imageList.length; i++) {

			Drawable image = Drawable.createFromPath(imageList[i]
					.getAbsolutePath());

			if (image != null) {
				imageArray.add(image);
			}
		}
		pd.dismiss();
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				_mainImageView.setImageDrawable(imageArray.get(0));
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
				_mainImageView.setImageDrawable(imageArray.get(valX));

			}
			break;
		}

		return false;
	}

	/*
	 * private void setImageInImageView(Boolean isForwaward) { if (isForwaward)
	 * { imageCount++; if (imageCount >= imageArray.size()) { imageCount = 0; }
	 * _mainImageView.setImageDrawable(imageArray.get(imageCount));
	 * 
	 * } else { imageCount--; if (imageCount < 0) { imageCount =
	 * imageArray.size() - 1; }
	 * _mainImageView.setImageDrawable(imageArray.get(imageCount)); } }
	 */

	public void copy(File src, File dst) throws IOException {
		InputStream in = new FileInputStream(src);
		OutputStream out = new FileOutputStream(dst);

		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		in.close();
		out.close();
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {

		/*
		 * case R.id.img_arrow: if (!bottonFlag) { bottonFlag = true; Animation
		 * animation = AnimationUtils.loadAnimation(this,
		 * R.anim.rotate_around_center_point);
		 * 
		 * animation.setFillAfter(true); img_Arrow.startAnimation(animation);
		 * 
		 * TranslateAnimation slide = new TranslateAnimation(0, 0, -50, 0);
		 * slide.setDuration(500); slide.setFillAfter(true);
		 * img_Save.startAnimation(slide); img_Save.setVisibility(View.VISIBLE);
		 * img_Share.startAnimation(slide);
		 * img_Share.setVisibility(View.VISIBLE);
		 * img_Delete.startAnimation(slide);
		 * img_Delete.setVisibility(View.VISIBLE); } else { bottonFlag = false;
		 * Animation animation1 = AnimationUtils.loadAnimation(this,
		 * R.anim.rotate_around_center_point_first);
		 * 
		 * animation1.setFillAfter(true); img_Arrow.startAnimation(animation1);
		 * TranslateAnimation slide = new TranslateAnimation(0, 0, 0, 0);
		 * slide.setDuration(500); slide.setFillAfter(true);
		 * img_Save.startAnimation(slide); img_Save.setVisibility(View.GONE);
		 * img_Share.startAnimation(slide); img_Share.setVisibility(View.GONE);
		 * img_Delete.startAnimation(slide);
		 * img_Delete.setVisibility(View.GONE);
		 * img_Arrow.setVisibility(View.VISIBLE); } break;
		 */

		case R.id.img_save:

			showSingleBtnDialog();
			break;
		case R.id.btn_Back_Camera:

			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setMessage("All Captured Images will be lost.");
			alert.setTitle("Message");

			alert.setPositiveButton("Ok",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							backHome();
						}
					});

			alert.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
						}
					});

			alert.show();

			break;

		case R.id.img_share:

			break;
		case R.id.img_delete:

			AlertDialog.Builder alert1 = new AlertDialog.Builder(this);
			alert1.setMessage("Are you sure want to delete the spin?");
			alert1.setTitle("Message");

			alert1.setPositiveButton("Delete",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							backHome();
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
		}
	}

	public void saveImage() {

		File imageList[] = temp.listFiles();
		File parentList[] = parent.listFiles();

		for (int i = 0; i < imageList.length; i++) {

			try {
				File newFile = new File(
						Environment.getExternalStorageDirectory()
								+ "/3DMation/children/imageMedia"
								+ parentList.length + "_" + i + ".jpg");
				if (i == 0) {
					File parentFile = new File(
							Environment.getExternalStorageDirectory()
									+ "/3DMation/parent/imageMedia"
									+ parentList.length + "_"
									+ imageList.length + ".jpg");

					copy(imageList[i], parentFile);
				}
				copy(imageList[i], newFile);
			} catch (IOException e) {

				e.printStackTrace();
			}
		}

	}

	public void arrayFileClean(ArrayList<Drawable> arrayName) {
		if (arrayName.size() > 0) {
			for (int i = arrayName.size() - 1; i >= 0; i--) {
				arrayName.remove(i);
			}
		}
	}

	public void backHome() {
		if (temp.isDirectory()) {
			String[] children = temp.list();
			for (int i = 0; i < children.length; i++) {
				new File(temp, children[i]).delete();
			}
		}
		arrayFileClean(imageArray);

		CaptureImageViewer.this.finish();
	}

	private void showSingleBtnDialog() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setMessage("Spin saved to library.");
		alert.setTitle("Message");

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				saveImage();
				backHome();

			}
		});

		alert.show();
	}

	protected void onStop() {
		super.onStop();
	}

	@Override
	public void onBackPressed() { // disable back button in this activity
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setMessage("All Captured Images will be lost.");
		alert.setTitle("Message");

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				backHome();
			}
		});

		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
					}
				});

		alert.show();
	}
}
