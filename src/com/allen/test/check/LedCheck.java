package com.allen.test.check;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;

public class LedCheck extends Activity implements OnClickListener {
	private View contentView;
	private int[] COLOR={0xffff0000,0xff00ff00,0xff0000ff,0xffffff00,0xffff00ff
			
	};
	int curColor = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		contentView = new View(this);
		setContentView(contentView);
		setColor(curColor);
		contentView.setOnClickListener(this);
	}
	
	private void setColor(int index){
		contentView.setBackgroundColor(COLOR[index]);
		ledcolor(COLOR[index]);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		ledcolor(0xff000000);
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
	public void onClick(View v) {
		// TODO Auto-generated method stub
		curColor++;
		if(curColor > COLOR.length-1){
			curColor = 0;
		}
		setColor(curColor);
		
	}

	private void ledcolor(int color){
		final int ID_LED=19871103; 
		NotificationManager nm=(NotificationManager)getSystemService(NOTIFICATION_SERVICE); 
		Notification n = new Notification();
        n.flags |= Notification.FLAG_SHOW_LIGHTS;
        n.ledARGB = color;
        n.ledOnMS = 1;
        n.ledOffMS = 0;
        nm.notify(1, n);
	}
	
}
