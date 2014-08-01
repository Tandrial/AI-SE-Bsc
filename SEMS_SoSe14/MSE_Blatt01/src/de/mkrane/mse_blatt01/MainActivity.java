package de.mkrane.mse_blatt01;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

	final int COUNTER_RESULT = 1;

	private int flip = 0;
	private int count = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		SharedPreferences sharedPref = MainActivity.this
				.getPreferences(Context.MODE_PRIVATE);

		count = sharedPref.getInt(getString(R.string.saved_count), 0);

		findViewById(R.id.btnChange).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						final TextView tView = (TextView) findViewById(R.id.txtHello);
						tView.setText(getResources().getString(
								flip++ % 2 == 0 ? R.string.alt_test
										: R.string.hello_world));

					}
				});

		findViewById(R.id.btnSwitch).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(MainActivity.this,
								SecondActivity.class);
						intent.putExtra("count", count);
						startActivityForResult(intent, COUNTER_RESULT);
					}
				});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == RESULT_OK) {
			if (requestCode == COUNTER_RESULT && data != null) {
				count = data.getIntExtra("count", 0);
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onPause() {

		SharedPreferences sharedPref = MainActivity.this
				.getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putInt(getString(R.string.saved_count), count);
		editor.commit();

		super.onPause();
	}

}
