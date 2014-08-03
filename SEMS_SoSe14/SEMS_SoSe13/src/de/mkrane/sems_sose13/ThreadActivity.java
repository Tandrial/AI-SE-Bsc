package de.mkrane.sems_sose13;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

public class ThreadActivity extends Activity {
	Thread t = null;

	int current = 0;
	int MIN_VALUE = 0;
	int MAX_VALUE = 300;
	int direction = 1;
	boolean running = false;

	ProgressBar pBar;
	TextView tView;

	RadioButton rbAb;
	RadioButton rbAuf;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_threads);

		pBar = (ProgressBar) findViewById(R.id.progressBar1);
		tView = (TextView) findViewById(R.id.tvValue);

		rbAb = (RadioButton) findViewById(R.id.rbAb);
		rbAuf = (RadioButton) findViewById(R.id.rbAuf);

		rbAuf.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				rbAb.setChecked(false);
				rbAuf.setChecked(true);
				direction = 1;
				checkThread();
			}
		});
		rbAb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				rbAb.setChecked(true);
				rbAuf.setChecked(false);
				direction = -1;
				checkThread();
			}
		});

		findViewById(R.id.btnStart).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (t != null && t.isAlive()) {
					running = false;
					try {
						t.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else {
					running = true;
					checkThread();
					t.start();
				}
			}
		});
	}

	public void checkThread() {
		if (t == null || !t.isAlive()) {
			t = new Thread() {
				@Override
				public void run() {
					while (running) {
						current += direction;
						if (current > MAX_VALUE || current < MIN_VALUE) {
							current -= direction;
						} else {
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									pBar.setProgress(current);
									tView.setText(String.valueOf(current));
								}
							});
						}
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

				}
			};
		}
	}
}
