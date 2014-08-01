package de.mkrane.mse_blatt08_a13b;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class CompassActivity extends Activity {

	// Lokale Variablen
	private SensorManager sensorManager;
	private TextView azi, x, y;
	private long timestampNew = 0, timestampOld = 0;
	private PaintView paintView = null;

	// SensorEventListener zur Reaktion auf Sensorwertänderungen. Hier findet
	// die Berechnung der Ausrichtung der Kompassrose statt, sodass diese immer
	// nach Norden zeigt.
	private SensorEventListener sensorListener = new SensorEventListener() {

		// Arrays f�r Beschleunigungs- und Magnetfeldsensor
		float[] mAcceleration;
		float[] mGeomagnetic;

		public void onSensorChanged(SensorEvent event) {

			// TODO: Implementierung der Aufgabe 13b)

			if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
				mAcceleration = event.values;

			if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
				mGeomagnetic = event.values;

			if (mAcceleration != null && mGeomagnetic != null) {

				float Rot[] = new float[9];
				float I[] = new float[9];

				boolean succ = SensorManager.getRotationMatrix(Rot, I,
						mAcceleration, mGeomagnetic);

				timestampNew = event.timestamp;

				if (succ && Math.abs(timestampNew - timestampOld) > 100000000) {

					final float orientation[] = new float[3];
					SensorManager.getOrientation(Rot, orientation);

					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							float azimut = orientation[0];
							azimut -= Math.PI / 2;
							if (azimut < 0) {
								azimut = (float) (Math.PI - Math.abs(azimut) + Math.PI);
							}
							azimut = (float) Math.toDegrees(azimut);

							paintView.rot = -azimut + 180;
							paintView.invalidate();

							timestampOld = timestampNew;

							azi.setText("Z: " + orientation[0]);
							x.setText("X: " + orientation[1]);
							y.setText("Y: " + orientation[2]);
						}
					});
				}

			}

		}

		public void onAccuracyChanged(Sensor sensor, int accuracy) {

		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.compass);

		// Abruf des System-SensorManagers
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

		// Zuweisung der TextViews des Layouts
		azi = (TextView) findViewById(R.id.textView_azimut);
		x = (TextView) findViewById(R.id.textView_x);
		y = (TextView) findViewById(R.id.textView_y);
		// imageview = (ImageView) findViewById(R.id.imageView);

		paintView = (PaintView) findViewById(R.id.paintView);
	}

	@Override
	protected void onResume() {
		// Registrierung des SensorEventListeners für die entsprechenden
		// Sensoren Beschleunigung und Magnetfeld
		sensorManager.registerListener(sensorListener,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_FASTEST);
		sensorManager.registerListener(sensorListener,
				sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
				SensorManager.SENSOR_DELAY_FASTEST);

		super.onResume();
	}

	@Override
	protected void onPause() {
		// Deregistrierung des SensorEventListeners
		sensorManager.unregisterListener(sensorListener);

		super.onPause();
	}
}
