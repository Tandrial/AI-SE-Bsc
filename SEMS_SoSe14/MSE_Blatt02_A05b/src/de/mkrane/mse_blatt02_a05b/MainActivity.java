package de.mkrane.mse_blatt02_a05b;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import de.mkrane.mse_blatt02_a05b.R;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main_layout);

		findViewById(R.id.btnSend).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent();
						intent.setAction(".intent.action.STARTACTIVITY");
						getApplicationContext().sendBroadcast(intent);
					}
				});
	}
}
