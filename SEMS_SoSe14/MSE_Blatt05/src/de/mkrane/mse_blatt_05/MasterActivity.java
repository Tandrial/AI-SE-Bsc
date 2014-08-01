package de.mkrane.mse_blatt_05;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import de.mkrane.mse_blatt_05.GeoWikiService.GeoWikiServiceBinder;

public class MasterActivity extends Activity implements IGeoWikiServiceClient {

	// Lokale Variablen
	private GeoWikiService geoWikiService;
	private boolean isBoundToGeoWikiService = false;
	private Handler handler = new Handler();

	// OnClickListener f�r die Listenzeilen. Hier werden f�r die Detailansicht
	// ben�tigte Daten in ein Intent verpackt und anschlie�end wird die neue
	// Activity mittels des �bergebenen Intents aufgerufen.
	private AdapterView.OnItemClickListener listViewItemClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int index,
				long arg3) {
			if (isBoundToGeoWikiService) {

				// TODO implementieren Sie Teilaufgabe 10d
				WikipediaArticle article = geoWikiService.getArticles().get(
						index);

				Intent intent = new Intent(MasterActivity.this,
						DetailActivity.class);

				intent.putExtra(getString(R.string.string_intent_title),
						article.getTitle());
				intent.putExtra(getString(R.string.string_intent_wikiurl),
						article.getWikipediaUrl().toString());
				intent.putExtra(getString(R.string.string_intent_summary),
						article.getSummary());
				intent.putExtra(getString(R.string.string_intent_image),
						article.getThumbnailImg());

				startActivity(intent);
			}
		}
	};

	// OnClickListener f�r Suchanfrage-Button. Sobald der Button bet�tigt wird,
	// wird der im EditText vorhandene Text ausgelesen und bei gestarteten
	// Service an den Service �bergeben, der daraufhin einen GeoCoder-Thread zur
	// Umwandlung des Request-Strings startet.
	private View.OnClickListener buttonCommitSearchRequestListener = new View.OnClickListener() {

		@Override
		public void onClick(View view) {

			String request = ((EditText) findViewById(R.id.editText_searchrequest))
					.getEditableText().toString();

			if (isBoundToGeoWikiService) {

				geoWikiService.geoCode(request);

				((ListView) findViewById(R.id.listView_searchresults))
						.setAdapter(null);

			}
		}
	};

	// Erstellung einer Service-Verbindung
	private ServiceConnection serviceConnection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {

			geoWikiService = null;

			isBoundToGeoWikiService = false;

		}

		// Bindung des Service sowie Registierung dieser Activity am Client
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {

			GeoWikiService.GeoWikiServiceBinder binder = (GeoWikiServiceBinder) service;

			geoWikiService = binder.getService();

			geoWikiService.registerClient(MasterActivity.this);

			isBoundToGeoWikiService = true;

			if (isBoundToGeoWikiService) {

				// Falls das article-Objekt nicht leer ist, werden bereits
				// vorhandene Daten in die Liste geladen.
				if (geoWikiService.getArticles() != null) {

					((ListView) findViewById(R.id.listView_searchresults))
							.setAdapter(new WikipediaArticleArrayAdapter(
									MasterActivity.this,
									android.R.layout.simple_list_item_1,
									geoWikiService.getArticles()));

				}

			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_master);

		// Start des Services
		Intent intent = new Intent(this, GeoWikiService.class);
		startService(intent);

		// Zuweisung der OnClickListener auf die entsprechenden Elemente
		findViewById(R.id.button_commitsearchrequest).setOnClickListener(
				buttonCommitSearchRequestListener);

		((ListView) findViewById(R.id.listView_searchresults))
				.setOnItemClickListener(listViewItemClickListener);

	}

	@Override
	protected void onStart() {

		super.onStart();

		// Bindung des Services
		Intent intent = new Intent(this, GeoWikiService.class);
		bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
	}

	@Override
	protected void onStop() {

		if (isBoundToGeoWikiService) {

			// Deregistrierung am Client sowie Entbindung des Services
			geoWikiService.deregisterClient(this);
			unbindService(serviceConnection);

		}

		super.onStop();
	}

	// Getter f�r den Handler
	public Handler getHandler() {
		return this.handler;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.master, menu);
		return true;
	}

	// Methode, die nach erfolgreicher Adress-Geokoordinaten-Konvertierung das
	// Koordinatenpaar in ein vorgesehenes Textfeld schreibt. Anschlie�end wird
	// der Service zum Download von GeoNames angesto�en.
	@Override
	public void didConvertToLatitudeLongitude(double latitude, double longitude) {

		((TextView) findViewById(R.id.textView_locationinfo)).setText(String
				.format("%f; %f", latitude, longitude));

		if (isBoundToGeoWikiService) {
			geoWikiService.downloadFomGeoNames(latitude, longitude);
		}

	}

	// Methode, die nach fehlgeschlagener Adress-Geokoordinaten-Konvertierung
	// einen Hinweis mittels Toast ausgibt.
	@Override
	public void failedConvertToLatitudeLongitude() {

		Toast.makeText(this,
				getResources().getString(R.string.string_error_geocoder),
				Toast.LENGTH_LONG).show();

	}

	// Methode, die nach fehlgeschlagenem Download von GeoNames einen Hinweis
	// mittels Toast ausgibt.
	@Override
	public void failedDownloadGeoNames() {

		Toast.makeText(this,
				getResources().getString(R.string.string_error_geonames),
				Toast.LENGTH_LONG).show();

	}

	// Methode, die nach erfolgreichem Download von GeoNames die
	// zur�ckgelieferten Wikieintr�ge in die Liste �bertr�gt und anzeigt.
	@Override
	public void didDownloadWikiData() {

		if (isBoundToGeoWikiService) {

			((ListView) findViewById(R.id.listView_searchresults))
					.setAdapter(new WikipediaArticleArrayAdapter(this,
							android.R.layout.simple_list_item_1, geoWikiService
									.getArticles()));

		}

	}

	// Methode zur Informierung der Liste, dass sich deren Dateninhalt ge�ndert
	// hat und diese ihren angezeigten Datenbestand aktualisieren muss.
	@Override
	public void updateArticles() {

		((WikipediaArticleArrayAdapter) ((ListView) findViewById(R.id.listView_searchresults))
				.getAdapter()).notifyDataSetChanged();

	}

}
