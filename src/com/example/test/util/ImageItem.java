package com.example.test.util;

import android.graphics.drawable.Drawable;

public class ImageItem {
	int Id;
	String mName;
	Drawable mDrawable;
	boolean selected=false;
	
	public ImageItem(int id,Drawable drawable,String name){	
		Id = id;
		mDrawable = drawable;
		mName = name;
	}
	
	
	public void setName(String name){
		
		mName = name;
		
	}
	
	public void setDrawable(Drawable drawable){
		
		mDrawable = drawable;
		
	}

	public void setSeleted(boolean select){
		
		selected = select;
		
	}
	
	public int getid(){
		return Id;
	}
	
	public String getName(){
		return mName;
	}
	
	public Drawable getDrawable(){
		return mDrawable;
	}
	
	public boolean isSelect(){
		return selected;
	}
	
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return mName;
	}
	
	
}
