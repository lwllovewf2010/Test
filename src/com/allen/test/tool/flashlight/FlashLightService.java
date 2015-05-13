package com.allen.test.tool.flashlight;

import com.allen.test.R;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.ImageView;

public class FlashLightService extends Service {
	
	final static String TAG = "TEST/FlashLightService";
	private Camera mCameraDevices;
	private Camera.Parameters mParameters; 
	private boolean ison = false;
	private Handler mHandler = new Handler();
	private FlashLightCallback mCallback;
	private WindowManager mWindowManager;
	private WindowManager.LayoutParams mWindowParams;
	private int screenWidth;
	private int screenHeight;
	private int statusBarHeight;
	static final int CUSTOM_LOCATION_X = 300;
	static final int CUSTOM_LOCATION_Y = 800;
	private float mLastX = CUSTOM_LOCATION_X;
	private float mLastY = CUSTOM_LOCATION_Y;
	boolean isDraging = true;
	private ImageView floatView;
	private int touchSlop;
	private  boolean isShow=false;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		Log.d(TAG,"onCreate");
		super.onCreate();
		initCamera();
		initFloatTool();
	}

	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		
		Log.d(TAG,"onDestroy");
		if(mCameraDevices!=null){
			mCameraDevices.release();
		}
		showFloatTool(false);
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
	public boolean isFloatShow(){
		return isShow;
	}
	
	public  void turnOn(boolean on){
		if(on){
			mHandler.post(onRunnable);
		}else{
			mHandler.post(offRunnable);
		}
	}
	
	public void showFloatTool(boolean show){
		if(show && ! isShow){
			addFloatTool(CUSTOM_LOCATION_X,CUSTOM_LOCATION_Y);
		}else if(!show && isShow){
			hideFloatTool();
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
			mCallback.onLightStateChange(ison);
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
			mCallback.onLightStateChange(ison);
		}
		
	};
	
	interface FlashLightCallback{
		void onLightStateChange( boolean on);
	}
	public void setCallBack(FlashLightCallback callback){
		mCallback = callback;
	}
	
	void initFloatTool(){
		mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		mWindowParams = new WindowManager.LayoutParams();
		
		screenWidth = getResources().getDisplayMetrics().widthPixels;
		screenHeight = getResources().getDisplayMetrics().heightPixels;
		statusBarHeight = getStatusBarHeight();
		
		final ViewConfiguration configuration = ViewConfiguration.get(this);
		touchSlop = configuration.getScaledTouchSlop();
		
		
		floatView = (ImageView) LayoutInflater.from(this).inflate(
				R.layout.flashlight_float, null);
		floatView.setOnTouchListener(floatViewListener);
		

		
	}
	
	private void addFloatTool(int x, int y) {
		Log.d(TAG,"addFloatTool");
		mWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		mWindowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		mWindowParams.flags = 40;
		mWindowParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;

		mWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
		mWindowParams.x = x - floatView.getWidth() / 2;
		mWindowParams.y = y - statusBarHeight - floatView.getHeight() / 2;
		mWindowParams.format = PixelFormat.RGBA_8888;
		mWindowManager.addView(floatView, mWindowParams);
		isShow = true;
	}
	
	private void hideFloatTool(){
		Log.d(TAG,"hideFloatTool");
		if (mWindowManager != null) {
			mWindowManager.removeView(floatView);
			isShow = false;
		}
	}
	
	private void updateFloatView(int x, int y) {
		mWindowManager = (WindowManager) getApplicationContext()
				.getSystemService("window");

		mWindowParams.x = x - floatView.getWidth() / 2;
		mWindowParams.y = y - statusBarHeight - floatView.getHeight() / 2;
		mWindowManager.updateViewLayout(floatView, mWindowParams);
	}
	
	View.OnTouchListener floatViewListener = new View.OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			int mRawX = (int) event.getRawX();
			int mRawY = (int) event.getRawY();

			switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN:
				mLastX = mRawX;
				mLastY = mRawY;
				isDraging = false;
				break;
			case MotionEvent.ACTION_MOVE:
				if (isDraging || Math.abs(mRawX - mLastX) > touchSlop
						|| Math.abs(mRawY - mLastY) > touchSlop) {
					mLastX = mRawX;
					mLastY = mRawY;
					isDraging = true;
					updateFloatView(mRawX, mRawY);
				}
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				if (!isDraging) {
					turnOn(! ison);
				}
				isDraging = false;
				break;
			default:
				;
			}

			return true;
		}
		
	};
	
	public int getStatusBarHeight() {
		Class<?> c = null;
		Object obj = null;
		java.lang.reflect.Field field = null;
		int x = 0;
		int statusBarHeight = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			statusBarHeight = getResources().getDimensionPixelSize(x);
			return statusBarHeight;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return statusBarHeight;
	}
}
