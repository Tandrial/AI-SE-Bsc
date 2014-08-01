package de.mkrane.mse_blatt02_a04;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Inc_Activity extends Activity {

	A04 app;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inc_layout);
		app = (A04) this.getApplication();

		TextView tView = (TextView) findViewById(R.id.txtCount);
		tView.setText(String.valueOf(app.getCount()));

		findViewById(R.id.btnInc).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						app.incCount();

						TextView tView = (TextView) findViewById(R.id.txtCount);
						tView.setText(String.valueOf(app.getCount()));
					}
				});

		findViewById(R.id.btnSwitchInc).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(Inc_Activity.this,
								Dec_Activity.class);
						startActivity(intent);
						finish();
					}
				});
	}
}
