package com.allen.test.tool.selector;

import java.util.Random;

import com.allen.test.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class EasySelector extends Activity implements OnTouchListener {
	
	ImageView mLauncher = null;
	SelectorBGView mSelector = null;
	final static int MAX_DEGREE = 7000;
	final static int MIN_DEGREE = 1000;
	long touchTime = 0;
	
	String[] items = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.easy_selector);
		
		mLauncher = (ImageView) this.findViewById(R.id.launcher);
		mLauncher.setOnTouchListener(this);
		
		mSelector = (SelectorBGView) this.findViewById(R.id.selector);
		
		items = this.getResources().getStringArray(R.array.default_menu);
		mSelector.setData(items);
		
	}

	void launchWheel(long time){
		if(time > MAX_DEGREE){
			mSelector.Launch(getMaxDegrees());
		}else if(time < MIN_DEGREE){			
			mSelector.Launch(getMinDegrees());
		}else{
			mSelector.Launch((int) time);
		}
	};
	

	private int getMaxDegrees(){
		int degree = 0;
		Random radom = new Random();
		degree = radom.nextInt(1000);
		degree += MAX_DEGREE-1000;
		return degree;
	}
	private int getMinDegrees(){

		int degree = 0;
		Random radom = new Random();
		degree = radom.nextInt(1000);
		degree += MIN_DEGREE;
		return degree;
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
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.launcher){
			switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:
				touchTime = System.currentTimeMillis();
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				long time = System.currentTimeMillis();
				launchWheel(time-touchTime);
			}			
		}
		return true;
	}
	
}
