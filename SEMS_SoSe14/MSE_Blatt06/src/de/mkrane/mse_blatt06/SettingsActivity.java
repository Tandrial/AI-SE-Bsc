package de.mkrane.mse_blatt06;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class SettingsActivity extends Activity {

	SharedPreferences settings;

	EditText etFirstname;
	EditText etLastname;
	EditText etMatrikel;
	EditText etUsername;
	TextView tvActualValue;

	SeekBar seekbar;
	Button bnSaveSettings;
	private final int maxValue = 200;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);

		settings = getSharedPreferences(Blatt06.SHARED_PREFERENCES_FILENAME,
				MODE_PRIVATE);

		etFirstname = (EditText) findViewById(R.id.edittext_firstname);
		etFirstname.setText(settings.getString(
				Blatt06.SHARED_PREFERENCES_FIRSTNAME, ""));

		etLastname = (EditText) findViewById(R.id.edittext_lastname);
		etLastname.setText(settings.getString(
				Blatt06.SHARED_PREFERENCES_LASTNAME, ""));

		etMatrikel = (EditText) findViewById(R.id.edittext_matrikel);
		etMatrikel.setText(settings.getString(
				Blatt06.SHARED_PREFERENCES_MATRIKELNUMMER, ""));

		etUsername = (EditText) findViewById(R.id.edittext_username);
		etUsername.setText(settings.getString(
				Blatt06.SHARED_PREFERENCES_USERNAME, ""));

		tvActualValue = (TextView) findViewById(R.id.textview_seekbar_actualvalue);

		tvActualValue.setText(settings.getString(
				Blatt06.SHARED_PREFERENCES_ACTUALVALUE, ""));

		seekbar = (SeekBar) findViewById(R.id.seekBar_number_articles);
		seekbar.setProgress(settings.getInt(Blatt06.SHARED_PREFERENCES_SEEKBAR,
				0));
		seekbar.setMax(maxValue);

		seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				tvActualValue.setText(String.valueOf(progress));

			}
		});

		((Button) findViewById(R.id.button_save_settings))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						SharedPreferences.Editor editor = settings.edit();

						// EditText etFirstname;
						editor.putString(Blatt06.SHARED_PREFERENCES_FIRSTNAME,
								etFirstname.getText().toString());

						// EditText etLastname;
						editor.putString(Blatt06.SHARED_PREFERENCES_LASTNAME,
								etLastname.getText().toString());
						// EditText etMatrikel;
						editor.putString(
								Blatt06.SHARED_PREFERENCES_MATRIKELNUMMER,
								etMatrikel.getText().toString());
						// EditText etUsername;
						editor.putString(Blatt06.SHARED_PREFERENCES_USERNAME,
								etUsername.getText().toString());
						// TextView tvActualValue;
						editor.putString(
								Blatt06.SHARED_PREFERENCES_ACTUALVALUE,
								tvActualValue.getText().toString());

						editor.putInt(Blatt06.SHARED_PREFERENCES_SEEKBAR,
								seekbar.getProgress());

						editor.commit();
						finish();
					}
				});

	}
}
