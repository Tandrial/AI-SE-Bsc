package de.mkrane.mse_blatt_07;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import de.mkrane.blatt_07.R;
import de.mkrane.mse_blatt_07.GeoWikiService.GeoWikiServiceBinder;

public class MasterActivity extends Activity implements IGeoWikiServiceClient {

	// Lokale Variablen
	private GeoWikiService geoWikiService;
	private boolean isBoundToGeoWikiService = false;
	private Handler handler = new Handler();
	private LocationManager locationManager;
	private ToggleButton toggleButtonGPS;
	private ToggleButton toggleButtonNetwork;

	private static final long minTime = 0;
	private static final float minDistGPS = 200;
	private static final float minDistNet = 1000;

	// ToggleButton Listener für GPS- und Netzwerk-ToggleButton, ob dieser
	// aktiviert oder deaktiviert ist.
	private OnCheckedChangeListener toggleButtonListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {

			if (isChecked) {
				if (buttonView.getId() == R.id.toggleButtonGPS)
					startPositionListening(LocationManager.GPS_PROVIDER,
							minTime, minDistGPS);
				if (buttonView.getId() == R.id.toggleButtonNetwork)
					startPositionListening(LocationManager.NETWORK_PROVIDER,
							minTime, minDistNet);
			} else {
				stopPositionListening();
			}
		}
	};

	// LocationListener zum Handhabung von LocationUpdates
	private LocationListener locationListener = new LocationListener() {

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onLocationChanged(Location location) {
			didConvertToLatitudeLongitude(location.getLatitude(),
					location.getLongitude());
			toggleButtonGPS.setChecked(false);
			toggleButtonNetwork.setChecked(false);
			locationManager.removeUpdates(locationListener);
		}
	};

	// OnClickListener für die Listenzeilen. Hier werden für die Detailansicht
	// benötigte Daten in ein Intent verpackt und anschließend wird die neue
	// Activity mittels des übergebenen Intents aufgerufen.
	private AdapterView.OnItemClickListener listViewItemClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {

			if (isBoundToGeoWikiService) {

				Intent intent = new Intent(MasterActivity.this,
						DetailActivity.class);

				WikipediaArticle article = geoWikiService.getArticles().get(
						arg2);

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

	// OnClickListener für Suchanfrage-Button. Sobald der Button betätigt
	// wird,
	// wird der im EditText vorhandene Text ausgelesen und bei gestarteten
	// Service an den Service übergeben, der daraufhin einen GeoCoder-Thread
	// zur
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

		toggleButtonGPS = (ToggleButton) findViewById(R.id.toggleButtonGPS);
		toggleButtonGPS.setOnCheckedChangeListener(toggleButtonListener);
		toggleButtonNetwork = (ToggleButton) findViewById(R.id.toggleButtonNetwork);
		toggleButtonNetwork.setOnCheckedChangeListener(toggleButtonListener);

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

	}

	@Override
	protected void onStart() {

		super.onStart();

		// Bindung des Services
		Intent intent = new Intent(this, GeoWikiService.class);
		bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
	}

	@Override
	protected void onResume() {

		if (toggleButtonGPS.isChecked())
			startPositionListening(LocationManager.GPS_PROVIDER, minTime,
					minDistGPS);
		if (toggleButtonNetwork.isChecked())
			startPositionListening(LocationManager.NETWORK_PROVIDER, minTime,
					minDistNet);
		super.onResume();
	}

	@Override
	protected void onPause() {

		stopPositionListening();

		super.onPause();
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

	// Getter für den Handler
	public Handler getHandler() {
		return this.handler;
	}

	// Methode, die nach erfolgreicher Adress-Geokoordinaten-Konvertierung das
	// Koordinatenpaar in ein vorgesehenes Textfeld schreibt. Anschließend wird
	// der Service zum Download von GeoNames angestoßen.
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
	// zurückgelieferten Wikieinträge in die Liste überträgt und anzeigt.
	@Override
	public void didDownloadWikiData() {

		if (isBoundToGeoWikiService) {

			((ListView) findViewById(R.id.listView_searchresults))
					.setAdapter(new WikipediaArticleArrayAdapter(this,
							android.R.layout.simple_list_item_1, geoWikiService
									.getArticles()));

		}

	}

	// Methode zur Informierung der Liste, dass sich deren Dateninhalt geändert
	// hat und diese ihren angezeigten Datenbestand aktualisieren muss.
	@Override
	public void updateArticles() {

		((WikipediaArticleArrayAdapter) ((ListView) findViewById(R.id.listView_searchresults))
				.getAdapter()).notifyDataSetChanged();

	}

	// Startet Positionsabfragen
	private void startPositionListening(String provider, long minTime,
			float minDistance) {
		locationManager.requestLocationUpdates(provider, minTime, minDistance,
				locationListener);
	}

	// Stoppt Positionsabfragen
	private void stopPositionListening() {
		if (locationManager != null)
			locationManager.removeUpdates(locationListener);
	}
}
