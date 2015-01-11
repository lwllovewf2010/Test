package com.example.test;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.test.info.FrameWorkSourceActivity;
import com.example.test.info.LanguageInfo;
import com.example.test.info.PackageListActivity;
import com.example.test.mathod.PrintArray;
import com.example.test.tool.DPIvPX;

public class Main extends Activity {

	ListView listView = null;
	ArrayList<String> list = new ArrayList<String>();;
	
	Class[] classes = {
			GSensor.class,
			TouchTestActivity.class,
			FaceDetect.class,
			PrintArray.class,
			LanguageInfo.class,
			PackageListActivity.class,
			FrameWorkSourceActivity.class,
			DPIvPX.class
			
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.main_activity);
	
		
		for(Class cls:classes){
			list.add(cls.getSimpleName());
		}
		listView = (ListView)this.findViewById(R.id.list);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 , list);
		
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(clicklistener);

	}
	AdapterView.OnItemClickListener clicklistener = new AdapterView.OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(getApplication(), classes[arg2]);
			Main.this.startActivity(intent);
		}
		
	};
	

	
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
	
}
