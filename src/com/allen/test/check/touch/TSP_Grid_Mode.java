package com.allen.test.check.touch;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;


import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class TSP_Grid_Mode extends Activity {
	private final static String TAG = "TSP_Grid_Mode";
	private final static int CLOUMN = 9;
	private final static int ROW = 13;
	private final static float LINE_WEIGHT = 1.0f;
	private final static float TOUCH_WEIGHT = 4.0f;
	
	View contentView;
	int width,height;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate start");
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
		          WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.getWindow().getDecorView().setBackgroundColor(0xfff5f5f5);
	    
		Display  disply = getWindowManager().getDefaultDisplay();
		
		DisplayMetrics outMetrics= new DisplayMetrics();
		disply.getMetrics(outMetrics);
		width = outMetrics.widthPixels;
		height = outMetrics.heightPixels;
		
		disableStatusBar();
		contentView = new DrawView(this);
		this.setContentView(contentView);
		

		
		
	}

	private void disableStatusBar(){
		Object service = getSystemService("statusbar");
		try {
			Class<?> statusBarManager = Class
					.forName("android.app.StatusBarManager");
			Method expand = statusBarManager.getMethod("disable", int.class); // getMethod("collapse")似乎有点效果
			expand.invoke(service, 0x00000001);
		} catch (Exception e) {
			e.printStackTrace();
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
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	class DrawView extends View{
		Grid[][] Grids;
		Rect[] Lines;
		ArrayList<ArrayList<Point>> TouchLines = null;
		ArrayList<Point> touchLine;
		
		private final Paint mLinePaint = new Paint();
		private final Paint mRectPaint = new Paint();
		private final Paint mTouchPaint = new Paint();
		
		public DrawView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
//			ViewGroup.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
//			this.setLayoutParams(params);
			mLinePaint.setARGB(150, 80, 80, 80);
			mLinePaint.setStrokeWidth(LINE_WEIGHT);
			
			mRectPaint.setARGB(255, 0, 255, 0);
			
			mTouchPaint.setARGB(255, 0, 0, 0);
			mTouchPaint.setAntiAlias(false);
			mTouchPaint.setStyle(Paint.Style.STROKE);
			mTouchPaint.setStrokeWidth(TOUCH_WEIGHT);
			
			TouchLines =new ArrayList<ArrayList<Point>>();
			initGrids();
			initLines();
		}
		
		void initGrids(){
			Grids = new Grid[ROW][CLOUMN];
			
			for(int i=0; i<ROW; i++){
				for(int j=0; j<CLOUMN; j++){
					Grid grid = new Grid(); 
					grid.rect.left= j*width/CLOUMN;
					grid.rect.top = i*height/ROW;
					grid.rect.right =(j+1)*width/CLOUMN;
					grid.rect.bottom=(i+1)*height/ROW;
					Grids[i][j]=grid;
				}
			}
		}		
		
		void initLines(){
			Lines = new Rect[ROW+CLOUMN+2];
			
			for(int i=0;i < ROW+1;i++){
				Rect rect = new Rect();
				rect.left = 0;
				rect.top=i*height/ROW;
				rect.right=width;
				rect.bottom=i*height/ROW;
				
				Lines[i]=rect;
			}
			
			for(int i=0;i < CLOUMN+1;i++){
				Rect rect = new Rect();
				rect.left = i*width/CLOUMN;
				rect.top=0;
				rect.right=i*width/CLOUMN;
				rect.bottom=height;
				Lines[i+ROW+1]=rect;
			}
		}
		
		void touchRect(float x,float y){
			
			float gridwidth = 1.0f*width/CLOUMN;
			float gridheight =  1.0f*height/ROW;
			int cloumn = (int) (x / gridwidth);
			int row = (int) (y / gridheight);
			
			Grids[row][cloumn].touched=true;
		}
		
		@Override
		protected void onDraw(Canvas canvas) {
			// TODO Auto-generated method stub
			super.onDraw(canvas);
			
			for(int i=0; i<ROW; i++){
				for(int j=0; j<CLOUMN; j++){
					Grid grid = Grids[i][j];
					if(grid.touched){
						canvas.drawRect(grid.rect, mRectPaint);
					}
				}
			}
			
			for(Rect line: Lines){
				canvas.drawLine(line.left, line.top, line.right, line.bottom, mLinePaint);
			}
			
			for(ArrayList<Point> touchline : TouchLines){
				
				Iterator<Point> localIterator = touchline.iterator();
				Point start = null;
				if(localIterator.hasNext()){
					start=localIterator.next();
				}
				while(localIterator.hasNext()){
					Point cur = localIterator.next();
					canvas.drawLine(start.x,start.y,cur.x,cur.y,mTouchPaint);
					start=cur;
				}
				
			}
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			// TODO Auto-generated method stub
		      int action = event.getAction();
		      Log.d(TAG,"onTouchEvent" );
		      if (action == MotionEvent.ACTION_DOWN)
		      {
		    	  Log.d(TAG,"ACTION_DOWN" );
		    	  touchLine = new ArrayList<Point>();
		    	  TouchLines.add(touchLine);
		      }
		      
		      int j = event.getHistorySize();
		      for (int k = 0; k < j; ++k){
		    	  float x = event.getHistoricalX(k);
		    	  float y = event.getHistoricalY(k);
		    	  touchRect(x,y);
		    	  touchLine.add(new Point(x, y));
		      }
	    	  touchLine.add(new Point(event.getX(), event.getY()));
		      
	    	  touchRect(event.getX(),event.getY());
	    	  
		      invalidate(); 
			
			
			
			return true;
		}
		
		
		
		
		
		

	}
	
	class Grid {
		Rect rect;
		boolean touched;
		
		Grid(){
			rect=new Rect();
			touched = false;
		}

	}
	
	class Point{
		public Point(Float fx, Float fy) {
			// TODO Auto-generated constructor stub
			x=fx;
			y=fy;
		}
		float x;
		float y;
	}
	
}
