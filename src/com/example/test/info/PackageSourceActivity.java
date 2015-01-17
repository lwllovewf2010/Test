package com.example.test.info;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.R;
import com.example.test.util.ImageItem;
import com.example.test.util.ImageUtil;
import com.example.test.view.ImageItemView;

public class PackageSourceActivity extends Activity implements OnClickListener, OnItemClickListener {
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
	File appDir;
	int selectNum = 0;

	
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
		
		imageContainer=(ListView) this.findViewById(R.id.image_container);
		imageInfo=(TextView) this.findViewById(R.id.image_info);
		selectInfo=(TextView) this.findViewById(R.id.select_info);
		
		mAdapter = new ImageAdapter();
		imageContainer.setAdapter(mAdapter);
		imageContainer.setOnItemClickListener(this);

	}

	private void init(){
		
		int i=0;
		for( i = 0x0; i < 0x1000;i++){
			try{
				String name = mRes.getResourceName(DRAWABLE_FIRST+i);
				if(name != null && !name.isEmpty()){
					int start = name.indexOf("/");
					name = name.substring(start+1);
				}else{
					name = Integer.toHexString(DRAWABLE_FIRST+i);
				}
				
				
				ImageItem item = new ImageItem(DRAWABLE_FIRST+i,null,name);
				items.add(item);
			}catch(NotFoundException e){
				continue;
			}

		}	
		imageInfo.setText("Total Number:"+ items.size());
		
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
	
	private void saveImages(){
		
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
		
		
		for(ImageItem data:items){
			if(data.isSelect()){
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
	
	private void saveImage(Drawable drawable, String  name){
//		Log.d(TAG,"save image:" + name);
		if(drawable==null){
			Log.w(TAG,"save image:" + name+" is null");
			return;
		}
		Bitmap bitmap;
		bitmap = ImageUtil.drawableToBitmap(drawable);
		
		File file = new File(appDir,name);
		if(bitmap != null){
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
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
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
		switch(id){
		case R.id.select_all:
			if(selectNum == 0){
				for(ImageItem data: items){
					data.setSeleted(true);
				}
			}else{
				for(ImageItem data: items){
					data.setSeleted(false);
				}
			}
			mAdapter.notifyDataSetChanged();
			updateSelect();
			
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
	

}
