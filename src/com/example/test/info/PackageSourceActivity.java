package com.example.test.info;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.example.test.R;
import com.example.test.util.ImageItem;

public class PackageSourceActivity extends Activity implements OnClickListener {
	private final static String TAG = "PackageSourceActivity";  
	private final static String ROOT_DIR = "PullResource";  
	private final static int DRAWABLE_FIRST = 0x7f020000; 
	ApplicationInfo mApp;
	PackageManager pm;
	Button allButton;
	Button pullButton;
	Resources mRes;
	ListView imageContainer;
	TextView imageInfo;
	ArrayList<ImageItem> items;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.package_source_activity);
		Log.d(TAG,"onCreate");
		pm = getPackageManager();
		items = new ArrayList<ImageItem>();
		
		Intent intent = this.getIntent();
		//item = intent.getParcelableExtra("AppInfo");
		mApp = this.getApplicationInfo();
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
		
		imageContainer.setAdapter(new ImageAdapter());

	}

	private void init(){
		
		int i=0;
		for( i = 0; i < 0x10000;i++){
			try{
				Drawable draw = mRes.getDrawable(DRAWABLE_FIRST+i);
				String name = mRes.getResourceName(DRAWABLE_FIRST+i);
				
				if(name != null && !name.isEmpty()){
					int start = name.indexOf("/");
					name = name.substring(start+1);
				}else{
					name = Integer.toHexString(DRAWABLE_FIRST+i);
				}
				
				ImageItem item = new ImageItem(DRAWABLE_FIRST+i,draw,name);
				items.add(item);
			}catch(NotFoundException e){
				break;
			}

		}	
		imageInfo.setText("Total Number:"+ items.size());
	}
	

	
	private void saveImages(){

//		File card = Environment.getExternalStorageDirectory();
		File card = this.getExternalFilesDir(null);
		
		Log.d(TAG,"ExternalStorageState:"+Environment.getExternalStorageState());
		File rootDir = new File(card, ROOT_DIR);
		Log.d(TAG,"rootDir:"+rootDir.getAbsolutePath());
		File appDir = new File(rootDir, mApp.packageName);
		Log.d(TAG,"appDir:"+appDir.getAbsolutePath());
		
		if(!rootDir.exists()){
			rootDir.mkdirs();
		}else{
			Log.d(TAG,rootDir.getAbsolutePath()+" is exists !");
			rootDir.delete();
			rootDir.mkdir();
			
		}
		
		
		
		int i=0;
		for( i = 0; i < 0x10000;i++){
			
			String name = Integer.toHexString(DRAWABLE_FIRST+i);
					
			try{
				Drawable draw = mRes.getDrawable(DRAWABLE_FIRST+i);
				saveImage(draw, name);
				
			}catch(NotFoundException e){
				break;
			}

		}
		
		
		
	}
	private void addImageView(Drawable drawable){
		if(drawable==null) return;
		
		ImageView imageview = new ImageView(this);
		LinearLayout.LayoutParams lp;
		if(drawable instanceof NinePatchDrawable){
			lp = new LayoutParams(120, 120);
			imageview.setPadding(5, 5, 5, 5);
			imageview.setBackgroundResource(R.drawable.ic_image_bg_nine);
			
		}else{
			lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			imageview.setPadding(5, 5, 5, 5);
			imageview.setBackgroundResource(R.drawable.ic_image_bg);
		}
		imageview.setLayoutParams(lp);
		imageview.setImageDrawable(drawable);
		imageContainer.addView(imageview);
	}
	
	private void saveImage(Drawable drawable, String name){
		
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
			if (convertView == null) {
				convertView = View.inflate(getApplicationContext(),
						R.layout.image_item, null);
				new ViewHolder(convertView);
			}
			ViewHolder holder = (ViewHolder) convertView.getTag();
			ImageItem item = getItem(position);
			holder.iconView.setImageDrawable(item.getDrawable());
			holder.nameView.setText(item.getName());
			
			if(item.isSelect()){
				holder.selectView.setVisibility(View.VISIBLE);
			}else{
				holder.selectView.setVisibility(View.GONE);
			}
			return convertView;
		}

		class ViewHolder {
			ImageView iconView;
			TextView  nameView;
			View      selectView;

			public ViewHolder(View view) {
				iconView = (ImageView) view.findViewById(R.id.id_icon);
				nameView = (TextView) view.findViewById(R.id.id_name);
				selectView=view.findViewById(R.id.id_select);
				view.setTag(this);
			}
		}
	}
	

}
