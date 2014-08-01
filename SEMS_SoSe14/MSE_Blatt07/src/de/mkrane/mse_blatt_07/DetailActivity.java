package de.mkrane.mse_blatt_07;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import de.mkrane.blatt_07.R;

public class DetailActivity extends Activity {

	// Lokale Variable
	private Uri uri;

	/*
	 * OnClickListener f�r den Button zum �ffnen der URL in einem
	 * App-externen Browser. Dies wird mittels Intent umgesetzt, dem die URI zur
	 * Webseite �bergeben wird.
	 */
	private View.OnClickListener buttonBrowserListener = new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {

			if (uri != null) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(uri);
				startActivity(intent);
			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);

		// Abruf des �bergebenen Intents
		Intent intent = getIntent();

		// Abruf der im Intent gespeicherten Daten und anschlie�ende F�llung
		// Elemente im Layout mit Daten
		((EditText) findViewById(R.id.editText_detail_description))
				.setText(intent
						.getStringExtra(getString(R.string.string_intent_summary)));
		((TextView) findViewById(de.mkrane.blatt_07.R.id.textView_detail_title))
				.setText(intent
						.getStringExtra(getString(R.string.string_intent_title)));
		((TextView) findViewById(R.id.textView_wikiurl)).setText(intent
				.getStringExtra(getString(R.string.string_intent_wikiurl)));

		this.uri = Uri.parse(intent
				.getStringExtra(getString(R.string.string_intent_wikiurl)));

		((ImageView) findViewById(R.id.imageView_detail_image))
				.setImageBitmap((Bitmap) intent
						.getParcelableExtra(getString(R.string.string_intent_image)));

		findViewById(R.id.button_openbrowser).setOnClickListener(
				buttonBrowserListener);

	}
}
