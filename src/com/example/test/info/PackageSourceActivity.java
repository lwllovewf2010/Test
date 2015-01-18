package com.example.test.info;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.R;
import com.example.test.util.ImageItem;
import com.example.test.util.ImageUtil;
import com.example.test.view.ImageItemView;

public class PackageSourceActivity extends Activity implements OnClickListener, OnItemClickListener, OnScrollListener {
	private final static String TAG = "PackageSourceActivity";  
	private final static String ROOT_DIR = "PullRes/";  
	private final static int DRAWABLE_FIRST = 0x7f020000; 
	ApplicationInfo mApp;
	PackageManager pm;
	Button allButton;
	Button pullButton;
	Resources mRes;
	ListView imageContainer;
	TextView imageInfo;
	TextView selectInfo;
	ArrayList<ImageItem> items;
	ImageAdapter mAdapter;
	ProgressBar  progressBar;
	File appDir;
	int selectNum = 0;
	SaveTask saveTask;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.package_source_activity);
		Log.d(TAG,"onCreate");
		pm = getPackageManager();
		items = new ArrayList<ImageItem>();
		
		Intent intent = this.getIntent();
		mApp = intent.getParcelableExtra("AppInfo");
//		mApp = this.getApplicationInfo();
		this.setTitle(mApp.loadLabel(pm));
		this.setTitleColor(Color.CYAN);
		
		try {
			mRes = pm.getResourcesForApplication(mApp);
			
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		allButton=(Button) this.findViewById(R.id.select_all);
		allButton.setOnClickListener(this);
		
		pullButton=(Button) this.findViewById(R.id.bt_pull);
		pullButton.setOnClickListener(this);

		imageContainer = (ListView) this.findViewById(R.id.image_container);
		imageInfo = (TextView) this.findViewById(R.id.image_info);
		selectInfo = (TextView) this.findViewById(R.id.select_info);
		progressBar = (ProgressBar) this.findViewById(R.id.progressBar);
	
				
		mAdapter = new ImageAdapter();
		imageContainer.setAdapter(mAdapter);
		imageContainer.setOnItemClickListener(this);
		imageContainer.setOnScrollListener(this);
		
		saveTask = new SaveTask();
		

	}


	
	private void init() {

		int id = 0;
		items.clear();
		for (id = 0; id < 0x1000; id++) {
			try{
				String name = mRes.getResourceName(DRAWABLE_FIRST+id);
				if(name != null && !name.isEmpty()){
					int start = name.indexOf("/");
					name = name.substring(start + 1);
				} else {
					name = Integer.toHexString(DRAWABLE_FIRST + id);
				}

				ImageItem item = new ImageItem(DRAWABLE_FIRST + id, null, name);
				items.add(item);
			}catch(NotFoundException e){
				continue;
			}

		}

		imageInfo.setText("Total Number:" + items.size());

	}
	

	void selectAll(boolean selectall) {
		
		if (selectall) {
			
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
	}
	void updateSelect(){
		selectNum=0;
		for(ImageItem data: items){
			if(data.isSelect()){
				selectNum++;
			}
		}
		selectInfo.setText("Select Number:"+ selectNum);
	}

	private void scanSdCard(File file){ 
 
		Uri contentUri = Uri.fromFile(file);
		Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,contentUri);
		this.sendBroadcast(mediaScanIntent);
	}
	
	@SuppressWarnings("unchecked")
	@SuppressLint("ShowToast")
	private void saveImages(){
		if(selectNum < 1){
			return;
		}
		if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			Toast.makeText(this, "Can't found storage", 500).show();
			return;
		}
		
		File card =Environment.getExternalStorageDirectory();
		
		appDir = new File(card, ROOT_DIR+mApp.packageName);
		
		
		Log.d(TAG,"appDir:"+appDir.getAbsolutePath());
		
		if(!appDir.exists()){
			appDir.mkdirs();
		}else{
			Log.d(TAG, appDir.getAbsolutePath()+" is exists !");
			appDir.delete();
			appDir.mkdir();
		}
		
		if(!appDir.exists()){
			return;
		}
		
		if(saveTask.getStatus() != AsyncTask.Status .RUNNING){
			saveTask.execute(items);
		}
		

	}

	@SuppressLint("ShowToast")
	private void saveImage(Drawable drawable, String name) {
//		Log.d(TAG, "save image:" + name);

		Bitmap bitmap;
		bitmap = ImageUtil.drawableToBitmap(drawable);
		File file = null;
		
		if(drawable instanceof NinePatchDrawable){
			file = new File(appDir, name+".9.png");
		}else if(drawable instanceof BitmapDrawable){
			file = new File(appDir, name+".png");
		}else{
			return;
		}
		
		
		if (bitmap != null) {
			try {
				ImageUtil.saveBitmap(bitmap, file);
				scanSdCard(file);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(this, "save:"+name+"failed !", 500).show();
			}finally{
				bitmap.recycle();
			}
		}
		
		
		
	}
	
	void releaseDrawable(Drawable drawable){
		drawable.setCallback(null);
		if(drawable instanceof BitmapDrawable){
			((BitmapDrawable)drawable).getBitmap().recycle();
			drawable = null;
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
		selectAll(false);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if(saveTask.getStatus() == AsyncTask.Status .RUNNING){
			saveTask.cancel(true);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch(id){
		case R.id.select_all:
			selectAll(selectNum==0);
			break;
		case R.id.bt_pull:
			pullButton.post(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					saveImages();
				}
				
			});
			break;
			
		}
	}
	
	class SaveTask extends AsyncTask<ArrayList<ImageItem>, Integer, Integer>{

		@Override
		protected Integer doInBackground(ArrayList<ImageItem>... params) {
			// TODO Auto-generated method stub
			int num=0;
			for (ImageItem data : params[0]) {
				if (data.isSelect()) {
					num++;
					Drawable draw= data.getDrawable();
					if(draw == null){
						try{
							draw = mRes.getDrawable(data.getid());
						} catch (NotFoundException e) {
							continue;
						}
					}
					saveImage(draw, data.getName());
					publishProgress(num);
					releaseDrawable(draw);
				}
			}
			return num;
		}
		@Override  
	    protected void onPreExecute() {  
			allButton.setClickable(false);
			imageContainer.setEnabled(false);
			progressBar.setProgress(0);
			progressBar.setVisibility(View.VISIBLE);
	    }
		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
//			Log.d(TAG, "onProgressUpdate "+values[0]);
			int progress= (int) (1.0f*values[0]/selectNum*100);
			progressBar.setProgress(progress);
			selectInfo.setText(progress+"%");
		} 
		
		@Override
	     protected void onPostExecute(Integer result) {
			allButton.setClickable(true);
			progressBar.setVisibility(View.INVISIBLE);
			imageContainer.setEnabled(true);
			selectAll(false);
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
		}


	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		ImageItemView view=(ImageItemView) arg1;
		ImageItem data= mAdapter.getItem(arg2);
		if(data.isSelect()){
			data.setSeleted(false);
			view.setSelect(false);
		}else{
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
