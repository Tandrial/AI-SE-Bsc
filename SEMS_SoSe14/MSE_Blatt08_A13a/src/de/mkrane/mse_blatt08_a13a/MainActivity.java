package de.mkrane.mse_blatt08_a13a;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MainActivity extends ActionBarActivity {

	private ListView listView;
	private SensorManager mSensorManager;
	private List<Sensor> deviceSensors = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		listView = (ListView) findViewById(R.id.listView1);

		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

		deviceSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);

		listView.setAdapter(new SensorListArrayAdapter(this,
				android.R.layout.simple_list_item_1, deviceSensors));

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				MySensor s = new MySensor(deviceSensors.get(position));

				Intent intent = new Intent(getApplicationContext(),
						DetailActivity.class);
				intent.putExtra("SENSOR", s);
				startActivity(intent);

			}
		});

	}
}
