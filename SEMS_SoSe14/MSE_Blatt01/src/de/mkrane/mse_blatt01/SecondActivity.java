package de.mkrane.mse_blatt01;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SecondActivity extends Activity {

	private int count;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_second);

		count = getIntent().getIntExtra("count", 0);
		updateText();

		findViewById(R.id.btnPlus).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						count++;
						updateText();
					}
				});

		findViewById(R.id.btnMinus).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						count--;
						updateText();
					}
				});

		findViewById(R.id.btnNull).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						count = 0;
						updateText();
					}
				});
	}

	public void updateText() {
		final TextView tView = (TextView) findViewById(R.id.txtCounter);
		tView.setText(String.valueOf(count));
	}

	@Override
	public void onBackPressed() {
		Intent result = new Intent();
		result.putExtra("count", count);
		setResult(RESULT_OK, result);
		super.onBackPressed();
	}
}
