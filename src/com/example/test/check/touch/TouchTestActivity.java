package com.example.test.check.touch;

import java.util.ArrayList;
import java.util.Iterator;

import com.example.test.R;
import com.example.test.R.layout;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;


public class TouchTestActivity extends Activity {

	private final static String TAG = "SamsungFactoryTestActivity";
	
//	final static int GRID=42;
//	final static int SCREEN_WIDTH=540;
//	final static int SCREEN_HEIGHT=960;

	final static int GRID=57;
	final static int SCREEN_WIDTH=720;
	final static int SCREEN_HEIGHT=1280;
	
	final static int VERTICAL_LINE1_LEFT = 0;
	final static int VERTICAL_LINE1_RIGHT = GRID-1;
	final static int VERTICAL_LINE2_LEFT = (SCREEN_WIDTH-GRID)/2;
	final static int VERTICAL_LINE2_RIGHT = (SCREEN_WIDTH+GRID)/2;
	final static int VERTICAL_LINE3_LEFT = SCREEN_WIDTH-GRID;
	final static int VERTICAL_LINE3_RIGHT = SCREEN_WIDTH-1;

	final static int HORIZONTAL_LINE1_TOP = 0;
	final static int HORIZONTAL_LINE1_BOTTOM = GRID-1;
	final static int HORIZONTAL_LINE2_TOP = (SCREEN_HEIGHT-GRID)/2;
	final static int HORIZONTAL_LINE2_BOTTOM = (SCREEN_HEIGHT+GRID)/2;;
	final static int HORIZONTAL_LINE3_TOP = SCREEN_HEIGHT-GRID;
	final static int HORIZONTAL_LINE3_BOTTOM = SCREEN_HEIGHT-1;
	
    private int mRectSize = 0;
    public ArrayList<RectFill> mRect = new ArrayList<RectFill>();
    private PopupWindow mPromptWindow;
    private TextView mTextPass;
    Handler mHandler = new Handler();
    MyView v;
    
	
    protected void onCreate(Bundle paramBundle)
    {
      super.onCreate(paramBundle);
      Log.d(this.TAG, "onCreate start");
      this.requestWindowFeature(Window.FEATURE_NO_TITLE);
      this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
              WindowManager.LayoutParams.FLAG_FULLSCREEN);
      this.getWindow().getDecorView().setBackgroundColor(0xffa9a9a9);
      
      MyView localMyView = new MyView(this);
      this.v = localMyView;
      setContentView(localMyView);
      Log.d(this.TAG, "onCreate success");
      initRectFill();
      this.mRectSize = this.mRect.size();
      Log.d(this.TAG, "onCreate---mRectSize=" + this.mRectSize);
      
      
      AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
      int RingerMode = audio.getRingerMode();
      if(RingerMode == AudioManager.RINGER_MODE_SILENT 
    		  || RingerMode == AudioManager.RINGER_MODE_VIBRATE){
    	  return;
      }
      
    }

    public void onPause()
    {
      Log.d(this.TAG, "-->onPause");
      if (!allRectFilled())
      {
       // MyUtil.TOUCH = 1;
        //MyUtil.sendResult(this, getString(2131165196), 2131361835, false, FactoryTestActivity.class);
      }
      super.onPause();
    }

    public void onResume()
    {
      Log.d(this.TAG, "-->onResume");
      super.onResume();
    }
    

    private void initRectFill()
    {
 
      for (int j = VERTICAL_LINE1_RIGHT; j < VERTICAL_LINE2_RIGHT; j += GRID)
      {
	       RectFill localRectFill1 = new RectFill(j, HORIZONTAL_LINE1_TOP);
	       localRectFill1.setRectFillFlag(false);
	       this.mRect.add(localRectFill1);
          
	       RectFill localRectFill2 = new RectFill(j, HORIZONTAL_LINE2_TOP);
	       localRectFill2.setRectFillFlag(false);
	       this.mRect.add(localRectFill2);
        
           RectFill localRectFill3 = new RectFill(j, HORIZONTAL_LINE3_TOP);
           localRectFill3.setRectFillFlag(false);
           this.mRect.add(localRectFill3);
      }
      
      for (int j = VERTICAL_LINE2_RIGHT; j < VERTICAL_LINE3_RIGHT; j += GRID)
      {
	       RectFill localRectFill1 = new RectFill(j, HORIZONTAL_LINE1_TOP);
	       localRectFill1.setRectFillFlag(false);
	       this.mRect.add(localRectFill1);
          
	       RectFill localRectFill2 = new RectFill(j, HORIZONTAL_LINE2_TOP);
	       localRectFill2.setRectFillFlag(false);
	       this.mRect.add(localRectFill2);
        
           RectFill localRectFill3 = new RectFill(j, HORIZONTAL_LINE3_TOP);
           localRectFill3.setRectFillFlag(false);
           this.mRect.add(localRectFill3);
      }

      
      for (int l = HORIZONTAL_LINE1_BOTTOM; l < HORIZONTAL_LINE2_TOP; l += GRID)
      {
        RectFill localRectFill4 = new RectFill(VERTICAL_LINE1_LEFT, l);
        localRectFill4.setRectFillFlag(false);
        this.mRect.add(localRectFill4);
        RectFill localRectFill5 = new RectFill(VERTICAL_LINE2_LEFT, l);
        localRectFill4.setRectFillFlag(false);
        this.mRect.add(localRectFill5);
        RectFill localRectFill6 = new RectFill(VERTICAL_LINE3_LEFT, l);
        localRectFill4.setRectFillFlag(false);
        this.mRect.add(localRectFill6);
      }
      for (int i1 = HORIZONTAL_LINE2_BOTTOM; i1 < HORIZONTAL_LINE3_TOP; i1 += GRID)
      {
        RectFill localRectFill7 = new RectFill(VERTICAL_LINE1_LEFT, i1);
        localRectFill7.setRectFillFlag(false);
        this.mRect.add(localRectFill7);
        RectFill localRectFill8 = new RectFill(VERTICAL_LINE2_LEFT, i1);
        localRectFill7.setRectFillFlag(false);
        this.mRect.add(localRectFill8);
        RectFill localRectFill9 = new RectFill(VERTICAL_LINE3_LEFT, i1);
        localRectFill7.setRectFillFlag(false);
        this.mRect.add(localRectFill9);
      }
    }

    private void ShowPassWindow()
    {
      Log.d(this.TAG, "ShowPassWindow---mPromptWindow=" + this.mPromptWindow);
      if (this.mPromptWindow == null)
      {
        PopupWindow localPopupWindow = new PopupWindow(this);
        mTextPass = ((TextView)((LayoutInflater)getSystemService("layout_inflater")).inflate(R.layout.touch_test_pass, null));
        Log.d(this.TAG, "ShowPassWindow---mTextPass=" + this.mTextPass);
        localPopupWindow.setContentView(mTextPass);
        localPopupWindow.setWidth(450);
        localPopupWindow.setHeight(225);
        this.mPromptWindow = localPopupWindow;
      }
      if (this.mPromptWindow.isShowing())
        return;
      this.mPromptWindow.showAtLocation(this.v, 17, 0, 0);
    }
    
    public boolean allRectFilled()
    {
      for (int i = 0; i < this.mRectSize; ++i)
      {
        if (((RectFill)this.mRect.get(i)).getRectFillFlag())
          continue;
        Log.d(this.TAG, "allRectFilled---false---i=" + i);
        return false;
      }
      Log.d(this.TAG, "allRectFilled---true---");
      return true;
    }
    
    public void judgeRectFill(int paramInt1, int paramInt2)
    {
      for (int i = 0; i < this.mRectSize; ++i)
      {
        RectFill localRectFill = (RectFill)this.mRect.get(i);
        if ((paramInt1 <= localRectFill.x) || (paramInt1 >= GRID + localRectFill.x) || (paramInt2 <= localRectFill.y) || (paramInt2 >= GRID + localRectFill.y))
          continue;
        localRectFill.setRectFillFlag(true);
      }
    }
    
    public class MyView extends View
	  {
	    ArrayList<TouchTestActivity.PT> curLine;
	    private boolean mCurDown;
	    private int mCurX;
	    private int mCurY;
	    private int mHeaderBottom;
	    public ArrayList<ArrayList<TouchTestActivity.PT>> mLines = new ArrayList();
	    private final Paint mPaint = new Paint();
	    private final Paint mRectPaint;
	    private final Paint mTargetPaint;

	    
	    
	    public MyView(Context arg2)
	    {
	      super(arg2);
	      this.mPaint.setARGB(255, 80, 80, 80);
	      this.mRectPaint = new Paint();
	      this.mRectPaint.setARGB(255, 0, 255, 0);
	      this.mTargetPaint = new Paint();
	      this.mTargetPaint.setAntiAlias(false);
	      this.mTargetPaint.setARGB(255, 0, 0, 0);
	      this.mTargetPaint.setStyle(Paint.Style.STROKE);
	      this.mTargetPaint.setStrokeWidth(4.0F);
	    }

	    public void Clear()
	    {
	      Iterator localIterator = this.mLines.iterator();
	      while (localIterator.hasNext())
	        ((ArrayList)localIterator.next()).clear();
	      this.mLines.clear();
	      invalidate();
	    }

	    protected void onDraw(Canvas paramCanvas)
	    {
	      for (int i = 0; i < TouchTestActivity.this.mRectSize; ++i)
	      {
	        TouchTestActivity.RectFill localRectFill = (TouchTestActivity.RectFill)TouchTestActivity.this.mRect.get(i);
	        if (!localRectFill.getRectFillFlag())
	          continue;
	        paramCanvas.drawRect(localRectFill.x, localRectFill.y, GRID + localRectFill.x, GRID + localRectFill.y, this.mRectPaint);
	      }
	      paramCanvas.drawLine(VERTICAL_LINE1_LEFT, GRID,  VERTICAL_LINE3_RIGHT,  GRID, this.mPaint);
	      paramCanvas.drawLine(VERTICAL_LINE1_LEFT, HORIZONTAL_LINE2_TOP, VERTICAL_LINE3_RIGHT, HORIZONTAL_LINE2_TOP, this.mPaint);
	      paramCanvas.drawLine(VERTICAL_LINE1_LEFT, HORIZONTAL_LINE2_BOTTOM, VERTICAL_LINE3_RIGHT, HORIZONTAL_LINE2_BOTTOM, this.mPaint);
	      paramCanvas.drawLine(VERTICAL_LINE1_LEFT, HORIZONTAL_LINE3_TOP, VERTICAL_LINE3_RIGHT, HORIZONTAL_LINE3_TOP, this.mPaint);
	      paramCanvas.drawLine(GRID,  HORIZONTAL_LINE1_TOP, GRID,  HORIZONTAL_LINE3_BOTTOM, this.mPaint);
	      paramCanvas.drawLine(VERTICAL_LINE2_LEFT, HORIZONTAL_LINE1_TOP, VERTICAL_LINE2_LEFT, HORIZONTAL_LINE3_BOTTOM, this.mPaint);
	      paramCanvas.drawLine(VERTICAL_LINE2_RIGHT, HORIZONTAL_LINE1_TOP, VERTICAL_LINE2_RIGHT, HORIZONTAL_LINE3_BOTTOM, this.mPaint);
	      paramCanvas.drawLine(VERTICAL_LINE3_LEFT, HORIZONTAL_LINE1_TOP, VERTICAL_LINE3_LEFT, HORIZONTAL_LINE3_BOTTOM, this.mPaint);
	      for (int j = HORIZONTAL_LINE1_BOTTOM; j < HORIZONTAL_LINE2_TOP; j += GRID)
	      {
	        paramCanvas.drawLine(VERTICAL_LINE1_LEFT, j, VERTICAL_LINE1_RIGHT,  j, this.mPaint);
	        paramCanvas.drawLine(VERTICAL_LINE2_LEFT, j, VERTICAL_LINE2_RIGHT, j, this.mPaint);
	        paramCanvas.drawLine(VERTICAL_LINE3_LEFT, j, VERTICAL_LINE3_RIGHT, j, this.mPaint);
	      }
	      for (int k = HORIZONTAL_LINE2_BOTTOM; k < HORIZONTAL_LINE3_TOP; k += GRID)
	      {
		        paramCanvas.drawLine(VERTICAL_LINE1_LEFT, k, VERTICAL_LINE1_RIGHT,  k, this.mPaint);
		        paramCanvas.drawLine(VERTICAL_LINE2_LEFT, k, VERTICAL_LINE2_RIGHT, k, this.mPaint);
		        paramCanvas.drawLine(VERTICAL_LINE3_LEFT, k, VERTICAL_LINE3_RIGHT, k, this.mPaint);
	      }
	      for (int l = VERTICAL_LINE1_RIGHT; l < VERTICAL_LINE2_LEFT; l += GRID)
	      {
	        paramCanvas.drawLine(l, HORIZONTAL_LINE1_TOP, l, HORIZONTAL_LINE1_BOTTOM, this.mPaint);
	        paramCanvas.drawLine(l, HORIZONTAL_LINE2_TOP, l, HORIZONTAL_LINE2_BOTTOM, this.mPaint);
	        paramCanvas.drawLine(l, HORIZONTAL_LINE3_TOP, l, HORIZONTAL_LINE3_BOTTOM, this.mPaint);
	      }
	      for (int i1 = VERTICAL_LINE2_RIGHT; i1 < VERTICAL_LINE3_LEFT; i1 += GRID)
	      {
		        paramCanvas.drawLine(i1, HORIZONTAL_LINE1_TOP, i1, HORIZONTAL_LINE1_BOTTOM, this.mPaint);
		        paramCanvas.drawLine(i1, HORIZONTAL_LINE2_TOP, i1, HORIZONTAL_LINE2_BOTTOM, this.mPaint);
		        paramCanvas.drawLine(i1, HORIZONTAL_LINE3_TOP, i1, HORIZONTAL_LINE3_BOTTOM, this.mPaint);
	      }
	      int i2 = this.mLines.size();
	      for (int i3 = 0; i3 < i2; ++i3)
	      {
	        ArrayList localArrayList = (ArrayList)this.mLines.get(i3);
	        float f1 = 0.0F;
	        float f2 = 0.0F;
	        int i4 = localArrayList.size();
	        for (int i5 = 0; i5 < i4; ++i5)
	        {
	          TouchTestActivity.PT localPT = (TouchTestActivity.PT)localArrayList.get(i5);
	          if (i5 > 0)
	            paramCanvas.drawLine(f1, f2, localPT.x.floatValue(), localPT.y.floatValue(), this.mTargetPaint);
	          f1 = localPT.x.floatValue();
	          f2 = localPT.y.floatValue();
	        }
	      }
	    }

	    public boolean onTouchEvent(MotionEvent paramMotionEvent)
	    {
	      int action = paramMotionEvent.getAction();

	      if (action == MotionEvent.ACTION_DOWN)
	      {
	        this.curLine = new ArrayList();
	        this.mLines.add(this.curLine);
	      }
	      
       
	      int j = paramMotionEvent.getHistorySize();
	      for (int k = 0; k < j; ++k){
	        curLine.add(new TouchTestActivity.PT(Float.valueOf(paramMotionEvent.getHistoricalX(k)), Float.valueOf(paramMotionEvent.getHistoricalY(k))));
	        curLine.add(new TouchTestActivity.PT(Float.valueOf(paramMotionEvent.getX()), Float.valueOf(paramMotionEvent.getY())));
	      }
	       this.mCurX = (int)paramMotionEvent.getX();
	       this.mCurY = (int)paramMotionEvent.getY();
	       judgeRectFill(this.mCurX, this.mCurY);
	       invalidate();
	        
	      if(action == MotionEvent.ACTION_UP && allRectFilled()){
	    	  ShowPassWindow();
	    	  mHandler.postDelayed(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					//Intent intent = new Intent(TouchTestActivity.this, FactoryMode.class);
					//setResult(RESULT_OK,intent);
					//finish();
				}
	    		  
	    	  }, 2000L);
	      }
	      
	      return true;
	    }
	  }
	  public class PT
	  {
	    public Float x;
	    public Float y;

	    public PT(Float paramFloat1, Float arg3)
	    {
	      this.x = paramFloat1;
	      this.y = arg3;
	    }
	  }

	  public class RectFill
	  {
	    private boolean isFillFlag = false;
	    public int x;
	    public int y;

	    public RectFill(int paramInt1, int arg3)
	    {
	      this.x = paramInt1;
	      this.y = arg3;
	    }

	    public boolean getRectFillFlag()
	    {
	      return this.isFillFlag;
	    }

	    public void setRectFillFlag(boolean paramBoolean)
	    {
	      this.isFillFlag = paramBoolean;
	    }
	  }

	  
	  
}
