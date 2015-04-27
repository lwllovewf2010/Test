package com.allen.test.tool.flashlight;

import com.allen.test.R;

import android.app.Service;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class FlashLightService extends Service {
	
	final static String TAG = "TEST/FlashLightService";
	private Camera mCameraDevices;
	private Camera.Parameters mParameters; 
	boolean ison = false;
	private Handler mHandler = new Handler();
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		Log.d(TAG,"onCreate");
		super.onCreate();
		initCamera();
	}

	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		
		Log.d(TAG,"onDestroy");
		if(mCameraDevices!=null){
			mCameraDevices.release();
		}
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.d(TAG,"onStartCommand");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return new LocalBinder();
	}
	
   public class LocalBinder extends Binder {  
		 FlashLightService getService() {  
	            // Return this instance of LocalService so clients can call public methods  
	        return FlashLightService.this;  
	    }     
	} 
	 
	void initCamera(){
        try {  
            mCameraDevices = Camera.open();  
            mParameters = mCameraDevices.getParameters();
            //mCameraDevices.setPreviewDisplay(mHolder);  
        } catch (Exception e) {  
            if(mCameraDevices != null) { 
                mCameraDevices.release(); 
                mCameraDevices = null;
                Log.d(TAG,"mParameters get faild");
            }else{ 
            
            	Log.d(TAG,"mCameraDevices open faild");
            }
              
        }
	}
	
	public boolean isOn(){
		return ison;
	}
	
	public  void turnOn(boolean on){
		if(on){
			mHandler.post(onRunnable);
		}else{
			mHandler.post(offRunnable);
		}
	}
	
	private Runnable onRunnable = new Runnable(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(mCameraDevices != null && mParameters!=null){
				mParameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
				mCameraDevices.setParameters(mParameters); 

				ison = true;
			}else{
				Log.d(TAG,"mParameters is null");
				ison = true;
			}
		}
		
	};
	private Runnable offRunnable = new Runnable(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(mCameraDevices != null && mParameters!=null){
				mParameters.setFlashMode(Parameters.FLASH_MODE_OFF);
				mCameraDevices.setParameters(mParameters);
				ison = false;
			}else{
				Log.d(TAG,"mParameters is null");
				ison = true;
			}
		}
		
	};

}
