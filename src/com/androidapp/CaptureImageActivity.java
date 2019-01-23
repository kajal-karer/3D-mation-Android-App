package com.androidapp;

import java.io.BufferedOutputStream;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CaptureImageActivity extends Activity implements
		SurfaceHolder.Callback, OnClickListener {

	private LayoutInflater controlInflater = null;
	private Camera mCamera = null;
	private SurfaceHolder mHolder;
	private SurfaceView mSurfaceView;
	private Button mbtnCapture;
	private Button mbtnBack;
	private Button mbtnStop;
	ImageView flash_View;
	private ShutterCallback shutter;
	private PictureCallback raw;
	private final int FOTO_MODE = 0;
	private boolean mPreviewRunning = false;
	private int captureCount;
	private int captureStart = 0;

	private Animation flashAnimation = null;

	private TextView txtpicCounter;
	private Handler handler;
	private Runnable captureRunnable = null;
	private boolean touch = false;
	private Bitmap myImage = null;

	private long disHeight = 0;
	private long disWidth = 0;

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_camera);

		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		disWidth = size.x;
		disHeight = size.y;

		getWindow().setFormat(PixelFormat.UNKNOWN);
		mSurfaceView = (SurfaceView) findViewById(R.id.camerapreview);
		mSurfaceView.setClickable(true);
		mSurfaceView.setOnClickListener(this);
		mHolder = mSurfaceView.getHolder();
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		controlInflater = LayoutInflater.from(getBaseContext());
		View viewControl = controlInflater.inflate(R.layout.auto_capture, null);
		LayoutParams layoutParamsControl = new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		this.addContentView(viewControl, layoutParamsControl);

		mbtnBack = (Button) findViewById(R.id.backBtn);
		mbtnBack.setClickable(true);
		mbtnBack.setOnClickListener(this);

		mbtnCapture = (Button) findViewById(R.id.auto_capture_image);
		mbtnCapture.setOnClickListener(this);

		mbtnStop = (Button) findViewById(R.id.stopBtn);
		mbtnStop.setOnClickListener(this);
		mbtnStop.setClickable(false);

		captureCount = 48;

		txtpicCounter = (TextView) findViewById(R.id.txtCounter);
		txtpicCounter.setText(captureStart + "/" + captureCount);

		flash_View = (ImageView) findViewById(R.id.alpha_view);

		flashAnimation = AnimationUtils.loadAnimation(
				CaptureImageActivity.this, R.anim.alpha);
		flash_View.setAnimation(flashAnimation);
		flash_View.setVisibility(View.GONE);

		String root = Environment.getExternalStorageDirectory().toString();
		File myDir = new File(root + "/3DMation");
		myDir.mkdirs();

		captureRunnable = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

			}
		};

	}

	protected void onResume() {

		super.onResume();
	}

	protected void onStop() {

		super.onStop();

	}

	@Override
	public void onClick(View vw) {
		// TODO Auto-generated method stub
		Handler hand;
		switch (vw.getId()) {
		case R.id.camerapreview:
			mbtnCapture.setVisibility(View.GONE);

			hand = new Handler();
			hand.postDelayed(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (!touch) {
						touch = true;
						mCamera.takePicture(shutter, raw, jpeg);
						captureImage();
						captureStart++;
						if (captureStart <= captureCount) {
							txtpicCounter.setText(captureStart + "/"
									+ captureCount);
						}
					}
				}
			}, 0);

			break;
		case R.id.auto_capture_image:
			mbtnCapture.setVisibility(View.GONE);

			hand = new Handler();
			hand.postDelayed(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (!touch) {
						touch = true;
						mCamera.takePicture(shutter, raw, jpeg);
						captureImage();
						captureStart++;
						if (captureStart <= captureCount) {
							txtpicCounter.setText(captureStart + "/"
									+ captureCount);
						}
					}
				}
			}, 0);

			break;
		case R.id.backBtn:
			if (myImage != null) {
				myImage.recycle();
				myImage = null;
			}
			mbtnBack.setClickable(false);
			File temp = new File(Environment.getExternalStorageDirectory()
					+ "/3DMation/Temp/");
			temp.mkdir();
			if (temp.isDirectory()) {
				String[] children = temp.list();
				for (int i = 0; i < children.length; i++) {
					new File(temp, children[i]).delete();
				}
			}
			if (mPreviewRunning == false) {
				if (mCamera != null) {
					handler.removeCallbacks(captureRunnable);
					mCamera.stopPreview();
					mCamera.release();
					mCamera = null;
					flashAnimation.cancel();

					finish();
				}
			} else if (mCamera != null) {
				mCamera.release();

				finish();
			}
			break;

		case R.id.stopBtn:
			if (myImage != null) {
				myImage.recycle();
				myImage = null;
			}

			mbtnStop.setClickable(false);

			if (mCamera != null) {
				handler.removeCallbacks(captureRunnable);
				mCamera.stopPreview();
				mCamera.release();
				mCamera = null;
				flashAnimation.cancel();

				Intent intent = new Intent(CaptureImageActivity.this,
						CaptureImageViewer.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(intent);
				overridePendingTransition(0, 0);
				finish();
			}

			break;

		default:
			break;
		}
	}

	PictureCallback jpeg = new PictureCallback() {
		@Override
		public void onPictureTaken(final byte[] data, Camera camera) {
			// TODO Auto-generated method stub

			if (data != null) {
				Intent mIntent = new Intent();
				mCamera.startPreview();
				setResult(FOTO_MODE, mIntent);
				if (captureStart <= captureCount) {
					Thread t = new Thread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							File file = new File(Environment
									.getExternalStorageDirectory()
									.getAbsolutePath()
									+ "/3DMation/Temp/", "myImage"
									+ captureStart + ".jpg");
							if (file.exists())
								file.delete();
							try {
								BitmapFactory.Options options = new BitmapFactory.Options();
								options.inSampleSize = 1;

								myImage = BitmapFactory.decodeByteArray(data,
										0, data.length, options);
								myImage = Bitmap.createScaledBitmap(myImage,
										(int) disHeight, (int) disWidth, false);

								myImage = rotate(myImage, 90);

								FileOutputStream out = new FileOutputStream(
										file);
								BufferedOutputStream bos = new BufferedOutputStream(
										out);
								if (myImage != null) {
									myImage.compress(CompressFormat.JPEG, 100,
											bos);
								}
								if (out != null) {
									out.close();
								}
								if (bos != null) {
									bos.flush();
									bos.close();
								}

								myImage.recycle();
							} catch (Exception e) {

								e.printStackTrace();
							}
						}
					});
					t.start();

					touch = false;

					// *************************************
					if (captureStart >= 2) {
						mbtnStop.setClickable(true);

					}

					handler = new Handler();
					handler.postDelayed(captureRunnable, 0);

				} else {

					Intent intent = new Intent(CaptureImageActivity.this,
							CaptureImageViewer.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
					startActivity(intent);
					overridePendingTransition(0, 0);
					CaptureImageActivity.this.finish();
				}

			}
		}
	};

	public void surfaceCreated(SurfaceHolder holder) {

		mCamera = Camera.open();

		mCamera.setDisplayOrientation(90);
		Camera.Parameters p = mCamera.getParameters();

		if (getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA_FLASH)) {

			p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);

		} else {
			p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
		}

		p.setPictureFormat(ImageFormat.JPEG);

		List<Camera.Size> previewSizes = p.getSupportedPictureSizes();
		Camera.Size previewsize = previewSizes.get(0);

		p.setPictureSize(previewsize.width, previewsize.height);

		mCamera.setParameters(p);

	}

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {

		if (mPreviewRunning) {
			mCamera.stopPreview();
		}

		try {
			mCamera.setPreviewDisplay(holder);

		} catch (IOException e) {

			System.out.println("Caught exception in surface chagned");
			e.printStackTrace();
		}
		mCamera.startPreview();
		mPreviewRunning = true;

	}

	public void surfaceDestroyed(SurfaceHolder holder) {

		if (mPreviewRunning == false) {
			if (mCamera != null) {
				mPreviewRunning = false;
				mCamera.stopPreview();
				mCamera.release();
			}
			if (mCamera != null) {
				mPreviewRunning = false;
				mCamera.release();
			}
		}

	}

	public static Bitmap rotate(Bitmap b, int degrees) {
		if (degrees != 0 && b != null) {

			Matrix m = new Matrix();

			m.setRotate(degrees, (float) b.getWidth() / 2,
					(float) b.getHeight() / 2);
			try {
				Bitmap b2 = Bitmap.createBitmap(b, 0, 0, b.getWidth(),
						b.getHeight(), m, true);

				if (b != b2) {
					b.recycle();
					b = b2;
				}
			} catch (OutOfMemoryError ex) {
				throw ex;
			}
		}
		return b;
	}

	public void captureImage() {
		flash_View.setVisibility(View.VISIBLE);
		flash_View.startAnimation(flashAnimation);
		flash_View.setVisibility(View.INVISIBLE);

	}

	@Override
	public void onBackPressed() { // disable back button in this activity
		if (myImage != null) {
			myImage.recycle();
			myImage = null;
		}
		mbtnBack.setClickable(false);
		File temp = new File(Environment.getExternalStorageDirectory()
				+ "/3DMation/Temp/");
		temp.mkdir();
		if (temp.isDirectory()) {
			String[] children = temp.list();
			for (int i = 0; i < children.length; i++) {
				new File(temp, children[i]).delete();
			}
		}
		if (mPreviewRunning == false) {
			if (mCamera != null) {
				handler.removeCallbacks(captureRunnable);
				mCamera.stopPreview();
				mCamera.release();
				mCamera = null;
				flashAnimation.cancel();

				finish();
			}
		} else if (mCamera != null) {
			mCamera.release();

			finish();
		}
	}

}