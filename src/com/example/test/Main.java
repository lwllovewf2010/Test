package com.example.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Main extends Activity {

	ListView listView = null;

	
	Class[] classes = {
			GSensor.class,
			TouchTestActivity.class,
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.main_activity);
	
		listView = (ListView)this.findViewById(R.id.list);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.string_ids, android.R.layout.simple_list_item_1);
		
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
