package de.mkrane.mse_blatt04_a08;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

	private final int UPDATE_UI = 1;
	private int count = 0;

	private Handler handler = new Handler();

	private Handler handlerD = new Handler() {
		public void dispatchMessage(android.os.Message msg) {
			switch (msg.what) {
			case UPDATE_UI:
				((TextView) findViewById(R.id.textView1)).setText(String
						.valueOf(count));
				break;

			default:
				break;
			}
		}
	};

	class CountingRunnable implements java.lang.Runnable {

		@Override
		public void run() {
			for (int i = 0; i < 1000; i++) {
				System.out.println(++count);
				try {
					java.lang.Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				// c
				// runOnUiThread(new Runnable() {
				//
				// @Override
				// public void run() {
				// ((TextView) findViewById(R.id.textView1))
				// .setText(String.valueOf(count));
				// }
				// });

				// d
				// handlerD.sendEmptyMessage(UPDATE_UI);

				handler.post(new Runnable() {
					@Override
					public void run() {
						((TextView) findViewById(R.id.textView1))
								.setText(String.valueOf(count));
					}
				});

			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViewById(R.id.button1).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						count = 0;
						// a) BAD BAD BAD
						// new CountingRunnable().run();
						// b)
						new Thread(new CountingRunnable()).start();

					}
				});
	}
}
