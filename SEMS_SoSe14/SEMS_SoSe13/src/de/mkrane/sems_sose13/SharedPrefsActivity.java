package de.mkrane.sems_sose13;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;

public class SharedPrefsActivity extends Activity {

	public static final String PREFS = "KLAUSUR_PREFS";
	public static final String NAME = "NAME_KEY";
	public static final String VORNAME = "VORNAME_KEY";
	public static final String MATRIKEL = "MATRIKEL_KEY";
	public static final String PUSH = "PUSH_KEY";

	EditText etName;
	EditText etVorname;
	EditText etMatrikel;
	CheckBox cbPush;

	SharedPreferences settings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sharedprefs);

		etName = (EditText) findViewById(R.id.etName);
		etVorname = (EditText) findViewById(R.id.etVorname);
		etMatrikel = (EditText) findViewById(R.id.etMatrikel);
		cbPush = (CheckBox) findViewById(R.id.cbPush);

		settings = getSharedPreferences(PREFS, MODE_PRIVATE);

		etName.setText(settings.getString(NAME, ""));
		etVorname.setText(settings.getString(VORNAME, ""));
		etMatrikel.setText(String.valueOf(settings.getInt(MATRIKEL, 0)));
		cbPush.setChecked(settings.getBoolean(PUSH, false));

		findViewById(R.id.btnAbbrechen).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});

		findViewById(R.id.btnSpeichern).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						Editor editor = settings.edit();

						editor.putString(NAME, etName.getText().toString());
						editor.putString(VORNAME, etVorname.getText()
								.toString());
						String mat = etMatrikel.getText().toString();
						
						if (mat.length() > 0)
							editor.putInt(MATRIKEL, Integer.valueOf(etMatrikel
									.getText().toString()));
						else
							editor.remove(MATRIKEL);

						editor.putBoolean(PUSH, cbPush.isChecked());
						editor.commit();
						finish();

					}
				});
	}
}
