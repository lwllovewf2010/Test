package com.allen.test;

import android.annotation.TargetApi;
import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.Face;
import android.hardware.Camera.FaceDetectionListener;
import android.hardware.Camera.Parameters;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class FaceDetect extends Activity {
	  static final int CAMERA_ID = 1;
	  static final String TAG = "FaceDetection" ;
	  private Camera camera;
	  
	  private Button button;
	  private TextView mTextView;
	  int state = 0;
	  Handler handler = new Handler();
	  
	  
	  Runnable stopRun = new Runnable(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
			refreshState(0);
			reStartCamera();
		}
		  
	  };
	  
	  void reStartCamera(){
			if(camera != null){
				Log.d(TAG, "Detection restart");
				camera.stopFaceDetection();
				camera.stopPreview();
				camera.release();
				camera = null;
			}
		    camera = Camera.open(CAMERA_ID);
		    if (camera != null){
		    	Log.d(TAG, "Camera opened");
				Parameters param = camera.getParameters();
				int num = param.getMaxNumDetectedFaces();
				
				Log.d(TAG, "Camera MaxNumDetectedFaces ="+ num);
				
			    camera.setFaceDetectionListener(faceDetectionListener);
			    camera.startPreview();
			    handler.post(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						camera.startFaceDetection();
					}
			    	
			    });
			    
		    }
	  }
	  
	  void refreshState(int st){
		  if(state != st){
			  state = st;
			  if(state == 1){
				  mTextView.setText("Play");
			  }else{
				  mTextView.setText("Stop");
			  }
		  }
	  }
	  Camera.FaceDetectionListener faceDetectionListener=new MyFaceDetectionListener();
	  
	

	class MyFaceDetectionListener implements FaceDetectionListener{

		@Override
		public void onFaceDetection(Face[] faces, Camera camera) {
			// TODO Auto-generated method stub
			Log.d(TAG, "Camera onFaceDetection");
			if(state != 1){
				refreshState(1);
			}
			handler.removeCallbacks(stopRun);
			handler.postDelayed(stopRun, 500);		
		}
  	 
   }
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.face);
		
		mTextView = (TextView)this.findViewById(R.id.state);
		refreshState(1);
		
		button = (Button)this.findViewById(R.id.restart);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				reStartCamera();
			}
		});
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
	    if (this.camera != null)
	    {
	      Log.d(TAG, "Camera cancel");
	      this.camera.stopFaceDetection();
	      this.camera.stopPreview();
	      this.camera.release();
	      this.camera = null;
	    }
	    handler.removeCallbacks(stopRun);
		super.onPause();

	}
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		    this.camera = Camera.open(CAMERA_ID);
		    if (this.camera != null){
		    	Log.d(TAG, "Camera opened");
			    this.camera.setFaceDetectionListener(this.faceDetectionListener);
			    this.camera.startPreview();
			    this.camera.startFaceDetection();
		    }
	}
	
	

}
