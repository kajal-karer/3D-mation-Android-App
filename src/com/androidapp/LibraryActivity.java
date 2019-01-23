package com.androidapp;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class LibraryActivity extends Activity {

	private ArrayList<HashMap<String, File>> allFiles = null;
	private File parentImages[] = null;

	public class ImageAdapter extends BaseAdapter {

		private Context mContext;
		ArrayList<String> itemList = new ArrayList<String>();

		public ImageAdapter(Context c) {
			mContext = c;
		}

		void add(String path) {
			itemList.add(path);
		}

		@Override
		public int getCount() {
			return itemList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return itemList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			ImageView imageView = null;

			View gridView = convertView;
			Bitmap bm = decodeSampledBitmapFromUri(itemList.get(position), 80,
					80);
			gridView = new View(mContext);

			gridView = inflater
					.inflate(R.layout.new_grid_online, parent, false);

			imageView = (ImageView) gridView.findViewById(R.id.imageViewOnline);
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			imageView.setImageBitmap(bm);
			imageView.setTag("image resource name");

			return gridView;
		}

		public Bitmap decodeSampledBitmapFromUri(String path, int reqWidth,
				int reqHeight) {

			Bitmap bm = null;

			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path, options);

			options.inSampleSize = calculateInSampleSize(options, reqWidth,
					reqHeight);

			options.inJustDecodeBounds = false;
			bm = BitmapFactory.decodeFile(path, options);

			return bm;
		}

		public int calculateInSampleSize(

		BitmapFactory.Options options, int reqWidth, int reqHeight) {

			final int height = options.outHeight;

			final int width = options.outWidth;

			int inSampleSize = 1;

			if (height > reqHeight || width > reqWidth) {
				if (width > height) {
					inSampleSize = Math.round((float) height
							/ (float) reqHeight);
				} else {
					inSampleSize = Math.round((float) width / (float) reqWidth);
				}
			}

			return inSampleSize;
		}

	}

	private ImageAdapter myImageAdapter;
	private GridView gridview = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.library_activity);

		gridview = (GridView) findViewById(R.id.gridview);
		myImageAdapter = new ImageAdapter(this);
		allFiles = new ArrayList<HashMap<String, File>>();

		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				NewMethodImageLoad();
			}
		});
		t.start();

		gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				File file = null;
				HashMap<String, File> newFilemap = allFiles.get(position);
				if (newFilemap.containsKey("videoSpin")) {
					file = newFilemap.get("videoSpin");

				} else if (newFilemap.containsKey("imageSpin")) {

					file = newFilemap.get("imageSpin");

				}
				String name = file.getName();

				/*String[] nameSplit = name.split("_");
				String nameMedia = nameSplit[0];
				String namesub = nameMedia.substring(0, 1);*/

				Intent i = new Intent(LibraryActivity.this,
						SpinViewAllCapture.class);
				i.putExtra("id", name);
				startActivity(i);
				LibraryActivity.this.finish();

			}
		});

	}

	private void NewMethodImageLoad() {
		File targetDirector = new File(
				Environment.getExternalStorageDirectory()
						+ "/3DMation/parent");

		parentImages = targetDirector.listFiles();

		if (parentImages.length > 0) {
			for (File file : parentImages) {
				if (file.isFile()) {

					String ext = MimeTypeMap.getFileExtensionFromUrl(file
							.getName());

					if (ext.equals("jpg") || ext.equals("JPG")) {

						HashMap<String, File> hasmap = new HashMap<String, File>();
						hasmap.put("imageSpin", file);
						allFiles.add(hasmap);

					}
				}
			}
		}

		if (allFiles.size() > 0) {
			for (HashMap<String, File> fileMap : allFiles) {
				File newFile = null;

				if (fileMap.containsKey("videoSpin")) {
					newFile = fileMap.get("videoSpin");

				} else if (fileMap.containsKey("imageSpin")) {

					newFile = fileMap.get("imageSpin");

				}
				if (newFile != null && newFile.isFile()) {

					String ext = MimeTypeMap.getFileExtensionFromUrl(newFile
							.getName());

					if (ext.equals("jpg") || ext.equals("JPG")) {
						myImageAdapter.add(newFile.getAbsolutePath());

					}
				}
			}
		}
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				gridview.setAdapter(myImageAdapter);
			}
		});
	}

}
