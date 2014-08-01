package de.mkrane.mse_blatt08_a13a;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class DetailActivity extends Activity {

	private MySensor s = null;
	private TextView v1;
	private TextView v2;
	private TextView v3;
	private SensorManager sManager;

	private SensorEventListener listener = new SensorEventListener() {

		@Override
		public void onSensorChanged(SensorEvent event) {
			if (event != null) {
				if (event.values.length > 0)
					v1.setText(String.valueOf(event.values[0]));
				if (event.values.length > 1)
					v2.setText(String.valueOf(event.values[1]));
				if (event.values.length > 2)
					v3.setText(String.valueOf(event.values[2]));
			}
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_details);

		s = (MySensor) getIntent().getSerializableExtra("SENSOR");

		((TextView) findViewById(R.id.textView_name_value))
				.setText(s.getName());
		((TextView) findViewById(R.id.textView_vendor_value)).setText(s
				.getVendor());
		((TextView) findViewById(R.id.textView_type_value)).setText(String
				.valueOf(s.getType()));
		((TextView) findViewById(R.id.textView_version_value)).setText(String
				.valueOf(s.getVersion()));
		((TextView) findViewById(R.id.textView_maxRange_value)).setText(String
				.valueOf(s.getMaxRange()));

		v1 = (TextView) findViewById(R.id.textView_value1_value);
		v2 = (TextView) findViewById(R.id.textView_value2_value);
		v3 = (TextView) findViewById(R.id.textView_value3_value);

		sManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	}

	@Override
	protected void onResume() {
		sManager.registerListener(listener,
				sManager.getDefaultSensor(s.getType()),
				SensorManager.SENSOR_DELAY_UI);
		super.onResume();
	}

	@Override
	protected void onPause() {
		sManager.unregisterListener(listener);
		super.onPause();
	}

}
