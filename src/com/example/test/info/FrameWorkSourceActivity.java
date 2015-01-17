package com.example.test.info;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.AsyncTaskLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.R;
import com.example.test.util.ImageItem;
import com.example.test.util.ImageUtil;
import com.example.test.view.ImageItemView;

public class FrameWorkSourceActivity extends Activity implements
		OnClickListener, OnItemClickListener, OnScrollListener {
	private final static String TAG = "FrameWorkSourceActivity";
	private final static String ROOT_DIR = "PullRes/";
	private final static int DRAWABLE_FIRST = 0x01080000;

	PackageManager pm;
	Button allButton;
	Button pullButton;
	Resources mRes;
	ListView imageContainer;
	TextView imageInfo;
	TextView selectInfo;
	ArrayList<ImageItem> items;
	ImageAdapter mAdapter;
	File appDir;
	int selectNum = 0;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.package_source_activity);
		Log.d(TAG, "onCreate");
		pm = getPackageManager();
		items = new ArrayList<ImageItem>();

		this.setTitle("Framework Res");
		this.setTitleColor(Color.CYAN);

		mRes = Resources.getSystem();

		allButton = (Button) this.findViewById(R.id.select_all);
		allButton.setOnClickListener(this);

		pullButton = (Button) this.findViewById(R.id.bt_pull);
		pullButton.setOnClickListener(this);

		imageContainer = (ListView) this.findViewById(R.id.image_container);
		imageInfo = (TextView) this.findViewById(R.id.image_info);
		selectInfo = (TextView) this.findViewById(R.id.select_info);

		mAdapter = new ImageAdapter();
		imageContainer.setAdapter(mAdapter);
		imageContainer.setOnItemClickListener(this);
		imageContainer.setOnScrollListener(this);



	}


	
	private void init() {

		int id = 0;

		for (id = 0; id < 0x1000; id++) {

			try {
				String name = Resources.getSystem().getResourceName(DRAWABLE_FIRST + id);
				
				Log.d(TAG, "Drawable name:" + name);
				if (name != null && !name.isEmpty()) {
					int start = name.indexOf("/");
					name = name.substring(start + 1);
				} else {
					name = Integer.toHexString(DRAWABLE_FIRST + id);
				}

				ImageItem item = new ImageItem(DRAWABLE_FIRST + id, null, name);
				items.add(item);
				
			} catch (NotFoundException e) {
			
//				Log.d(TAG, "Drawable NotFoundException:" + " id:"
//						+ Integer.toHexString(DRAWABLE_FIRST + id));
				continue;
			}

		}
		imageInfo.setText("Total Number:" + items.size());

	}
	

	void updateSelect() {
		selectNum = 0;
		for (ImageItem data : items) {
			if (data.isSelect()) {
				selectNum++;
			}
		}
		selectInfo.setText("Select Number:" + selectNum);
	}

	private void scanSdCard(File file) {

		Uri contentUri = Uri.fromFile(file);
		Intent mediaScanIntent = new Intent(
				Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, contentUri);
		this.sendBroadcast(mediaScanIntent);
	}

	@SuppressLint("ShowToast")
	private void saveImages() {

		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			Toast.makeText(this, "Can't found storage", 500).show();
			return;
		}

		File card = Environment.getExternalStorageDirectory();

		appDir = new File(card, ROOT_DIR + "framework");

		Log.d(TAG, "appDir:" + appDir.getAbsolutePath());

		if (!appDir.exists()) {
			appDir.mkdirs();
		} else {
			Log.d(TAG, appDir.getAbsolutePath() + " is exists !");
			appDir.delete();
			appDir.mkdir();
		}

		if (!appDir.exists()) {
			return;
		}

		for (ImageItem data : items) {
			if (data.isSelect()) {
				Drawable draw= data.getDrawable();
				if(draw == null){
					try{
						draw = mRes.getDrawable(data.getid());
					} catch (NotFoundException e) {
						continue;
					}
				}
				saveImage(draw, data.getName());
			}
		}

	}

	@SuppressLint("ShowToast")
	private void saveImage(Drawable drawable, String name) {
		Log.d(TAG, "save image:" + name);

		Bitmap bitmap;
		bitmap = ImageUtil.drawableToBitmap(drawable);

		File file = new File(appDir, name);
		if (bitmap != null) {
			try {
				ImageUtil.saveBitmap(bitmap, file);
				scanSdCard(file);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(this, "save:" + name + "failed !", 500).show();
			} finally {
				bitmap.recycle();
			}
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		items.clear();
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		init();
		updateSelect();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.select_all:
			if (selectNum == 0) {
				for (ImageItem data : items) {
					data.setSeleted(true);
				}
			} else {
				for (ImageItem data : items) {
					data.setSeleted(false);
				}
			}
			mAdapter.notifyDataSetChanged();
			updateSelect();

			break;
		case R.id.bt_pull:
			pullButton.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					saveImages();
				}

			});
			break;

		}
	}

	class ImageAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return items.size();
		}

		@Override
		public ImageItem getItem(int position) {
			return items.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ImageItemView view;
			ImageItem item = getItem(position);
			if (convertView == null) {
				view = (ImageItemView) View.inflate(getApplicationContext(),
						R.layout.image_item, null);
			}else{
				view = (ImageItemView) convertView;
			}
			view.setName(item.getName());
			Drawable draw =  item.getDrawable();
			if(draw == null){
				try{
					draw = mRes.getDrawable(item.getid());
					view.setIcon(draw);
				} catch (NotFoundException e) {
					
				}
			}else{
				view.setIcon(draw);
			}
			
			view.setSelect(item.isSelect());
			return view;
			
//			ImageItem item = getItem(position);
//			ViewHolder holder;
//			if(convertView != null){
//				holder=(ViewHolder) convertView.getTag();
//			}else{
//				convertView = View.inflate(getApplicationContext(),R.layout.image_item, null);
//				holder = new ViewHolder();
//				holder.icon = (ImageView) convertView.findViewById(R.id.id_icon);
//				holder.text = (TextView) convertView.findViewById(R.id.id_name);
//				holder.selectView = convertView.findViewById(R.id.id_select);
//				convertView.setTag(holder);
//			}
//		
//			Drawable draw ;
//			try{
//				draw = mRes.getDrawable(item.getid());
//				holder.icon.setImageDrawable(draw);
//			} catch (NotFoundException e) {
//				
//			}
//			holder.text.setText(item.getName());
//			
//			
//			return convertView;
			
		}

		class ViewHolder {
			  TextView text;
			  ImageView icon;
			  View      selectView;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		ImageItemView view = (ImageItemView) arg1;
		ImageItem data = mAdapter.getItem(arg2);
		if (data.isSelect()) {
			data.setSeleted(false);
			view.setSelect(false);
		} else {
			data.setSeleted(true);
			view.setSelect(true);
		}

		updateSelect();
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onScroll->firstVisibleItem="+firstVisibleItem);

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onScrollStateChanged->scrollState="+scrollState);
		if(scrollState == SCROLL_STATE_IDLE){
			System.gc();
		}
	}

}
