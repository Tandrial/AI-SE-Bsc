package de.mkrane.mse_blatt02_a04;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Dec_Activity extends Activity {

	A04 app;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dec_layout);

		app = (A04) this.getApplication();

		TextView tView = (TextView) findViewById(R.id.txtCount);
		tView.setText(String.valueOf(app.getCount()));

		findViewById(R.id.btnDec).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						app.decCount();

						TextView tView = (TextView) findViewById(R.id.txtCount);
						tView.setText(String.valueOf(app.getCount()));
					}
				});

		findViewById(R.id.btnSwitchDec).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(Dec_Activity.this,
								Inc_Activity.class);
						startActivity(intent);
						finish();
					}
				});
	}
}
