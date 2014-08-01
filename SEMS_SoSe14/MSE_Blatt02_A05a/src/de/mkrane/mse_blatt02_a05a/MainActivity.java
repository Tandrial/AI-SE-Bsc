package de.mkrane.mse_blatt02_a05a;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {

	private BroadcastReceiver receiver;
	private int min = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		((TextView) findViewById(R.id.txtTick)).setText(String.valueOf(min));

		receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				((TextView) findViewById(R.id.txtTick)).setText(String
						.valueOf(++min));
			}

		};
		registerReceiver(receiver, new IntentFilter(Intent.ACTION_TIME_TICK));
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(receiver);
		super.onDestroy();
	}
}
