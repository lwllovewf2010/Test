package com.allen.test.tool.flashlight;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.allen.test.R;
import com.allen.test.tool.flashlight.FlashLightService.FlashLightCallback;
import com.allen.test.tool.flashlight.FlashLightService.LocalBinder;

public class FlashLight extends Activity {
	final static String TAG = "TEST/FlashLight";

	private ImageView mBg = null;
	private ServiceConnection mSc;
	private AlertDialog mPop;
	private FlashLightService mService;
	private View touchArea = null;
	private View settingBtton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");
		
		setContentView(R.layout.flashlight_activity);

		mBg = (ImageView) findViewById(R.id.light_bg);
		touchArea = findViewById(R.id.touch_area);
		touchArea.setOnTouchListener(new FlashLightListener());
		settingBtton = findViewById(R.id.setting_button);
		settingBtton.setOnTouchListener(new FlashLightListener());

		mSc = new ServiceConnection() {

			@Override
			public void onServiceConnected(ComponentName arg0, IBinder arg1) {
				// TODO Auto-generated method stub
				Log.d(TAG, "onServiceConnected");
				mService = ((LocalBinder) arg1).getService();
				mService.setCallBack(callback);
				turnOn(true);
			}

			@Override
			public void onServiceDisconnected(ComponentName arg0) {
				// TODO Auto-generated method stub
				Log.d(TAG, "onServiceDisconnected");
				mService = null;
			}
		};

		Intent service = new Intent();
		service.setClass(this, FlashLightService.class);
		this.bindService(service, mSc, Context.BIND_AUTO_CREATE);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.d(TAG, "onStart");
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Log.d(TAG, "onDestroy");
		this.unbindService(mSc);
		super.onDestroy();

	}

	FlashLightCallback callback = new FlashLightCallback() {

		@Override
		public void onLightStateChange(boolean on) {
			// TODO Auto-generated method stub
			Log.d(TAG, "onLightStateChange="+on);
			if (on) {
				mBg.setImageResource(R.drawable.shou_on);
			} else {
				mBg.setImageResource(R.drawable.shou_off);
			}
		}

	};

	private void turnOn(boolean on) {
		if (mService != null) {
			mService.turnOn(on);
			if (on) {
				mBg.setImageResource(R.drawable.shou_on);
			} else {
				mBg.setImageResource(R.drawable.shou_off);
			}
		}
	}

	private class FlashLightListener implements View.OnTouchListener {

		@Override
		public boolean onTouch(View arg0, MotionEvent arg1) {
			// TODO Auto-generated method stub

			if (arg1.getAction() != MotionEvent.ACTION_DOWN) {
				return true;
			}
			if (arg0.getId() == R.id.touch_area) {
				turnOn(!mService.isOn());
			} else if (arg0.getId() == R.id.setting_button) {
				showDialog();
			}

			return true;
		}

	}

	void showDialog() {
		String[] items = new String[1];
		boolean[] checkedItems = new boolean[1];
		items[0] = "Enable float tool";
		checkedItems[0] = mService.isFloatShow();
		if (mPop != null) {
			mPop.dismiss();
		}
		mPop = new AlertDialog.Builder(this)
				.setMultiChoiceItems(items, checkedItems, PopListener)
				.setPositiveButton("确定", null).create();

		mPop.show();
	}

	OnMultiChoiceClickListener PopListener = new OnMultiChoiceClickListener() {

		@Override
		public void onClick(DialogInterface arg0, int arg1, boolean arg2) {
			// TODO Auto-generated method stub
			Log.d(TAG, "PopListener arg1="+arg1+" arg2="+arg2);
			if(mService!=null && arg1 ==0){
				mService.showFloatTool(arg2);
			}
			mPop.dismiss();
		}

	};

}
