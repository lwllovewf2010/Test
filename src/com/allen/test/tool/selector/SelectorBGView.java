package com.allen.test.tool.selector;

import java.util.ArrayList;
import java.util.Random;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.allen.test.R;

@SuppressLint("DrawAllocation")
public class SelectorBGView extends ImageView {
	final static String TAG = "SelectorView";
	final static int BASIC_R = 160;
	final static int BASIC_G = 160;
	final static int BASIC_B = 80;

	int height = 0;
	int width = 0;
	int textSize = 0;
	int smallCircle = 0;

	int genR = 160;
	int genG = 160;
	int genB = 80;

	Drawable mDrawable = null;
	Drawable mSelectDrawable = null;

	float degrees = -180.0f;
	float tarDegrees = -180.0f;
	float accelete = 10;
	
	Paint backPaint = null;
	Paint textPaint = null;
	ArrayList<Selection> Selections = null;
//	String[] items =  { "123", "345", "567", "789", "234", "208" };

	class Selection {
		int ColorR = 0;
		int ColorG = 0;
		int ColorB = 0;
		String Label = null;

		Selection(String name, int colorR, int colorG, int colorB) {
			Label = name;
			ColorR = colorR;
			ColorR = colorR;
			ColorR = colorR;
		}

	}

	public SelectorBGView(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public SelectorBGView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public SelectorBGView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub

		backPaint = new Paint();
		backPaint.setAntiAlias(false);
		backPaint.setStyle(Paint.Style.FILL_AND_STROKE);

		textPaint = new Paint();
		textPaint.setColor(0xffffffff);

		textSize = this.getResources().getDimensionPixelSize(
				R.dimen.menu_textsize);
		textPaint.setTextSize(textSize);

		smallCircle = this.getResources().getDimensionPixelSize(
				R.dimen.small_circle);
		
		mDrawable = this.getDrawable();
		mSelectDrawable = this.getResources().getDrawable(R.drawable.selector);
//		initSelection(null);
	}
	
	public void setData(String[] items){
		Selections = new ArrayList<Selection>();

		for (String item : items) {
			Selection selec = new Selection(item, BASIC_R, BASIC_G, BASIC_B);
			Selections.add(selec);
		}

		resetColor();
	}
	public void Launch(int degree){
		tarDegrees = degree;
		this.invalidate();
	}
	
	
	
//	private void initSelection(List<String> list) {
//		Selections = new ArrayList<Selection>();
//
//		for (String item : items) {
//			Selection selec = new Selection(item, BASIC_R, BASIC_G, BASIC_B);
//			Selections.add(selec);
//		}
//
//		resetColor();
//	}

	private void resetColor() {
		if (Selections.size() < 2) {
			return;
		}
		for (int i = 0; i < Selections.size(); i++) {
			getColor(i);
		}
	}

	private void getColor(int index) {
		Random radom = new Random();
		Selection select = Selections.get(index);

		while (true) {
			int r = radom.nextInt(0xff);
			int g = radom.nextInt(0xff);
			int b = radom.nextInt(0xff);

			if (Math.abs(r - b) > 100) {
				continue;
			}

			if (index == 0) {
				select.ColorR = r;
				select.ColorG = g;
				select.ColorB = b;
				break;
			}
			Selection last = Selections.get(index - 1);
			int total = r + g + b;
			int ltotal = last.ColorB + last.ColorG + last.ColorR;
			if (index == Selections.size() - 1) {
				Selection first = Selections.get(0);
				int ftotal = first.ColorB + first.ColorG + first.ColorR;
				if (Math.abs(total - ltotal) > 40
						&& Math.abs(total - ftotal) > 40) {
					select.ColorR = r;
					select.ColorG = g;
					select.ColorB = b;
					break;
				}
			} else if (Math.abs(total - ltotal) > 40) {
				select.ColorR = r;
				select.ColorG = g;
				select.ColorB = b;
				break;
			}

			continue;
		}

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		width = MeasureSpec.getSize(widthMeasureSpec);

		Drawable drawable = this.getDrawable();

		int srcWith = drawable.getIntrinsicWidth();
		int srcHeight = drawable.getIntrinsicHeight();

		height = width * srcHeight / srcWith;

		this.setMeasuredDimension(width, height);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// // TODO Auto-generated method stub
		if (Selections != null) {
			float arc = 360.0f / Selections.size();
			RectF oval = new RectF(0, 0, width, height);

			for (int i = 0; i < Selections.size(); i++) {
				Selection selection = Selections.get(i);
				backPaint.setARGB(0xff, selection.ColorR, selection.ColorG,
						selection.ColorB);
				canvas.drawArc(oval, i * arc, arc, true, backPaint);

				canvas.save();
				canvas.rotate((i + 0.5f) * arc, width / 2, height / 2);

				textPaint.setARGB(0xff, 0xff - selection.ColorR,
						0xff - selection.ColorG, 0xff - selection.ColorB);
				canvas.drawText(selection.Label, (width + smallCircle) / 2,
						(height + textSize) / 2, textPaint);
				canvas.restore();
			}
		}

		mDrawable.setBounds(0, 0, width, height);

		this.getDrawable().draw(canvas);

		canvas.save();

		
		canvas.rotate(degrees,width/2,height/2);
		mSelectDrawable.setBounds((width - mSelectDrawable.getIntrinsicWidth())/2,
				(height - mSelectDrawable.getIntrinsicHeight())/2,
				(width + mSelectDrawable.getIntrinsicWidth())/2,
				(height + mSelectDrawable.getIntrinsicHeight())/2
				);
		mSelectDrawable.draw(canvas);
		canvas.restore();
		
		boolean needInvalidate = false;
		
		if(tarDegrees-degrees > 1000.0f){
			accelete = 10;
			needInvalidate = true;
		}else if(tarDegrees - degrees > 100.0f){
			accelete = (tarDegrees-degrees)/100;
			needInvalidate = true;
		}else if(tarDegrees > degrees ){
			accelete=1;
			needInvalidate = true;
		}
		
		if(needInvalidate){
			degrees += accelete;
			this.invalidate();
		}else{
			degrees=degrees%360;
			tarDegrees = degrees;
		}
		
		
	}

}
