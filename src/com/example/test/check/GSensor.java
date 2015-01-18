package com.example.test.check;

import java.util.List;

import com.example.test.R;
import com.example.test.R.drawable;
import com.example.test.R.id;
import com.example.test.R.layout;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class GSensor extends Activity implements SensorEventListener{
	SensorManager sensorManager = null;
	Sensor orientationSensor = null;
	TextView accuracy_view= null;
	TextView value_0 = null;
	TextView value_1 = null;
	TextView value_2 = null;
	Button successButton;
	Button failButton;
	ImageView image;
	
	int[] image_res = {R.drawable.gsensor_x,R.drawable.gsensor_y,R.drawable.gsensor_z,R.drawable.gsensor_x_2};
	
	private boolean			mRegisteredSensor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gsensor);
	
		mRegisteredSensor = false;
		sensorManager = (SensorManager)getSystemService(this.SENSOR_SERVICE);
	
	
		orientationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		accuracy_view = (TextView)this.findViewById(R.id.gsensor_tv_info);
		value_0 = (TextView)this.findViewById(R.id.gsensor_xyz);
		
		accuracy_view.setText("GSensor_tips");
		value_0.setText("X:\nY:\nZ");
		successButton = (Button)this.findViewById(R.id.gsensor_bt_ok);
		failButton = (Button)this.findViewById(R.id.gsensor_bt_failed);
		image = (ImageView) this.findViewById(R.id.gsensor_image);
		
		successButton.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Intent intent = new Intent(GSensor.this, FactoryMode.class);
				//setResult(RESULT_OK,intent);
				//finish();
			}
			
		});
		failButton.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Intent intent = new Intent(GSensor.this, FactoryMode.class);
				//setResult(RESULT_CANCELED,intent);
				//finish();
			}
			
		});
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		if(sensor.getType() == Sensor.TYPE_ACCELEROMETER){
			
			}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		
		if (mRegisteredSensor)
		{
			sensorManager.unregisterListener(this);
			mRegisteredSensor = false;
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//sensorManager.registerListener(this,orientationSensor, SensorManager.SENSOR_DELAY_NORMAL);
		List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);

		if (sensors.size() > 0)
		{
			Sensor sensor = sensors.get(0);

			mRegisteredSensor = sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
		}
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
			float[] values = event.values;
			value_0.setText("X:"+values[0]+"\nY:"+values[1]+"\nZ:"+values[2]);
		}
		
		float x =  event.values[0];
		float y =  event.values[1];
		float z =  event.values[2];
		
		if(z> 7){
			image.setImageResource(image_res[2]);
		}
		
		if(y> 5){
			image.setImageResource(image_res[1]);
		}
		if(x > 5){
			image.setImageResource(image_res[0]);
		}
		if(x < -5){
			image.setImageResource(image_res[3]);
		}
		
		
	}

}
