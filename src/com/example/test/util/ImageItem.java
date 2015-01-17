package com.example.test.util;

import java.lang.ref.SoftReference;

import android.graphics.drawable.Drawable;

public class ImageItem {
	int Id;
	String mName;
	SoftReference<Drawable> mDrawable;
	boolean selected=false;
	
	public ImageItem(int id,Drawable drawable,String name){	
		Id = id;
		mDrawable = new SoftReference<Drawable>(drawable);
		mName = name;
	}
	
	
	public void setName(String name){
		
		mName = name;
		
	}
	
	public void setDrawable(Drawable drawable){
		
		mDrawable = new SoftReference<Drawable>(drawable);
		
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
		return mDrawable.get();
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
