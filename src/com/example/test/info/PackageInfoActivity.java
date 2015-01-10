package com.example.test.info;

import java.lang.reflect.Method;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageStats;

import com.example.test.R;

public class PackageInfoActivity extends Activity implements OnClickListener {
	private final static String TAG = "PackageInfoActivity";  
	ApplicationInfo item;
	PackageManager pm;
	TextView nameView,sizeView, permissionView;
	View permissionsControl, componentContainer, componentControl;
	ListView activityView;
	ActivityInfo[] activities;
	TextView componentOther;

	Button appStop, appUninstall, appClean, appPull;

	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		
		this.setContentView(R.layout.package_info_activity);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.screen_custom_title); 	
		
		pm = getPackageManager();
		Intent intent = this.getIntent();
		item = intent.getParcelableExtra("AppInfo");
		
		Log.d(TAG,"onCreate");
		initViews();
		
		
		try {
			queryPacakgeSize(item.packageName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

	}

	private void initViews() {
		ImageView mIcon = (ImageView) this.findViewById(R.id.icon);
		TextView mTitle = (TextView) this.findViewById(R.id.title);
		mIcon.setImageDrawable(item.loadIcon(pm));
		mTitle.setText(item.loadLabel(pm));
		
		nameView = (TextView) this.findViewById(R.id.package_name);
		sizeView = (TextView) this.findViewById(R.id.package_size);
		permissionView = (TextView) this.findViewById(R.id.permission);
		permissionsControl = this.findViewById(R.id.permissions_control);
		permissionsControl.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (permissionView.getVisibility() == View.VISIBLE) {
					permissionsControl
							.setBackgroundResource(R.drawable.arrow_g);
					permissionView.setVisibility(View.GONE);
				} else {

					permissionView.setVisibility(View.VISIBLE);
					permissionsControl
							.setBackgroundResource(R.drawable.arrow_g_up);
				}
			}
		});

		activityView = (ListView) this.findViewById(R.id.component_activity);
		componentOther = (TextView) this.findViewById(R.id.component_other);
		componentContainer = this.findViewById(R.id.component_container);
		componentControl = this.findViewById(R.id.component_control);
		componentControl.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (componentContainer.getVisibility() == View.VISIBLE) {
					componentControl.setBackgroundResource(R.drawable.arrow_g);
					componentContainer.setVisibility(View.GONE);
				} else {

					componentContainer.setVisibility(View.VISIBLE);
					componentControl
							.setBackgroundResource(R.drawable.arrow_g_up);
				}
			}
		});

		appStop = (Button) this.findViewById(R.id.app_stop);
		appClean = (Button) this.findViewById(R.id.app_clean);
		appPull = (Button) this.findViewById(R.id.app_pull);
		appUninstall = (Button) this.findViewById(R.id.app_uninstall);

		appStop.setOnClickListener(this);
		appClean.setOnClickListener(this);
		appPull.setOnClickListener(this);
		appUninstall.setOnClickListener(this);
	

	}

	 public void  queryPacakgeSize(String pkgName) throws Exception{  
	        if ( pkgName != null){  
	            //使用放射机制得到PackageManager类的隐藏函数getPackageSizeInfo   
	            PackageManager pm = getPackageManager();  //得到pm对象   
	            try {  
	                //通过反射机制获得该隐藏函数   
	                Method getPackageSizeInfo = pm.getClass().getDeclaredMethod("getPackageSizeInfo", String.class,IPackageStatsObserver.class);  
	                //调用该函数，并且给其分配参数 ，待调用流程完成后会回调PkgSizeObserver类的函数   
	                getPackageSizeInfo.invoke(pm, pkgName,new PkgSizeObserver());  
	            }   
	            catch(Exception ex){  
	                Log.e(TAG, "NoSuchMethodException") ;  
	                ex.printStackTrace() ;  
	                throw ex ;  // 抛出异常   
	            }   
	        }  
	    }  
	   
	
	    public class PkgSizeObserver extends IPackageStatsObserver.Stub{  
	        /*** 回调函数， 
	         * @param pStatus ,返回数据封装在PackageStats对象中 
	         * @param succeeded  代表回调成功 
	         */   

			@Override
			public void onGetStatsCompleted(PackageStats pStats,
					boolean succeeded) throws RemoteException {
				// TODO Auto-generated method stub
		            long cachesize = pStats.cacheSize  ; //缓存大小   
		            long datasize = pStats.dataSize  ;  //数据大小    
		            long codesize = pStats.codeSize  ;  //应用程序大小   
		            long totalsize = cachesize + datasize + codesize ; 
		            
		    		StringBuilder sb = new StringBuilder();
		    		sb.append("TotalSize: "+ formateFileSize(totalsize)+"\n");
		    		sb.append("\t CodeSize: "+formateFileSize(codesize)+"\n");
		    		sb.append("\t DataSize: "+formateFileSize(datasize)+"\n");
		    		sb.append("\t CacheSize: "+formateFileSize(cachesize)+"\n");
		    		sizeView.setVisibility(View.VISIBLE);
		    		sizeView.setText(sb.toString());
			}  
	    }  
	    //系统函数，字符串转换 long -String (kb)   
	    private String formateFileSize(long size){  
	        return Formatter.formatFileSize(PackageInfoActivity.this, size);   
	    }  
//	    
	private void getBasic() {
		StringBuilder sb = new StringBuilder();
		sb.append("PackageName: ");
		sb.append(item.packageName);
		sb.append("\n");
		sb.append("ProcessName: ");
		sb.append(item.processName);
		sb.append("\n");
		sb.append("TaskAffinity:");
		sb.append(item.taskAffinity);
		sb.append("\n");

		nameView.setText(sb.toString());

	}

	private void getPermission() {
		PackageInfo tmpPermInfo = null;
		StringBuilder sb = new StringBuilder();
		try {
			tmpPermInfo = pm.getPackageInfo(item.packageName,
					PackageManager.GET_PERMISSIONS);

		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block

		}
		if (tmpPermInfo.requestedPermissions != null
				&& tmpPermInfo.requestedPermissions.length != 0) {
			for (String str : tmpPermInfo.requestedPermissions) {
				sb.append(str);
				sb.append("\n");
			}
			permissionView.setText(sb.toString());
		} else {
			permissionView.setText("Get None");
		}

	}
	
	private void getComponent() {
		PackageInfo tmpPermInfo = null;
		ActivityInfo[] receivers = null;
		ServiceInfo[] services = null;
		ProviderInfo[] providers = null;
		
		try {
			tmpPermInfo = pm.getPackageInfo(item.packageName,
					PackageManager.GET_ACTIVITIES | PackageManager.GET_SERVICES
							| PackageManager.GET_RECEIVERS
							| PackageManager.GET_PROVIDERS);
			activities = tmpPermInfo.activities;
			receivers = tmpPermInfo.receivers;
			services = tmpPermInfo.services;
			providers = tmpPermInfo.providers;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			activities = null;
		}
		if (activities == null || activities.length == 0) {

		} else {
			activityView.setAdapter(new ActivityAdapter());
		}

		StringBuilder sb = new StringBuilder();
		sb.append("\n**services**\n");
		if (services != null && services.length != 0) {

			for (ServiceInfo info : services) {
				sb.append(info.name + "\n");
			}
		} else {
			sb.append("    get none\n");
		}

		sb.append("\n**receivers**\n");
		if (receivers != null && receivers.length != 0) {

			for (ActivityInfo info : receivers) {
				sb.append(info.name + "\n");
			}
		} else {
			sb.append("    get none\n");
		}
		sb.append("\n**providers**\n");
		if (providers != null && providers.length != 0) {

			for (ProviderInfo info : providers) {
				sb.append(info.name + "\n");
			}
		} else {
			sb.append("    get none\n");
		}
		componentOther.setText(sb.toString());

	}

	class ActivityAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return activities.length;
		}

		@Override
		public ActivityInfo getItem(int position) {
			return activities[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(getApplicationContext(),
						R.layout.item_list_activity, null);
				new ViewHolder(convertView);
			}
			ViewHolder holder = (ViewHolder) convertView.getTag();
			ActivityInfo item = getItem(position);
			holder.iv_icon.setImageDrawable(item.loadIcon(getPackageManager()));
			holder.iv_name.setText(item.loadLabel(getPackageManager()));
			holder.iv_class.setText(item.name);

			return convertView;
		}

		class ViewHolder {
			ImageView iv_icon;
			TextView iv_name;
			TextView iv_class;
			Button iv_start;

			public ViewHolder(View view) {
				iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
				iv_name = (TextView) view.findViewById(R.id.iv_name);
				iv_class = (TextView) view.findViewById(R.id.iv_class);
				iv_start = (Button) view.findViewById(R.id.iv_start);

				view.setTag(this);
			}
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		getBasic();
		getPermission();
		getComponent();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
    private void uninstallPkg(String packageName, boolean allUsers, boolean andDisable) {
        // Create new intent to launch Uninstaller activity
       Uri packageURI = Uri.parse("package:"+packageName);
       Intent uninstallIntent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE, packageURI);
     
       
   }

   private void forceStopPackage(String pkgName) {
       ActivityManager am = (ActivityManager)this.getSystemService(
               Context.ACTIVITY_SERVICE);

   }
   
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.app_stop:
			
			break;
		case R.id.app_pull:
			Intent intent = this.getIntent();
			intent.setClass(this, PackageSourceActivity.class);
//			intent.putExtra("AppInfo", item);
			this.startActivity(intent);
			break;
		case R.id.app_clean:
			
			break;
		case R.id.app_uninstall:
			
			break;
		default:

		}
	}

}
