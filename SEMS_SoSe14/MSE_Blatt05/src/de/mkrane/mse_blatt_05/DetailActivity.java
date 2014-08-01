package de.mkrane.mse_blatt_05;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailActivity extends Activity {

	// Lokale Variable
	private Uri uri;

	/*
	 * OnClickListener für den Button zum Öffnen der URL in einem App-externen
	 * Browser. Dies wird mittels Intent umgesetzt, dem die URI zur Webseite
	 * übergeben wird.
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

		// Abruf des übergebenen Intents
		Intent intent = getIntent();

		// Abruf der im Intent gespeicherten Daten und anschließende Füllung
		// Elemente im Layout mit Daten
		((EditText) findViewById(R.id.editText_detail_description))
				.setText(intent
						.getStringExtra(getString(R.string.string_intent_summary)));
		((TextView) findViewById(R.id.textView_detail_title)).setText(intent
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.master, menu);
		return true;
	}

}
