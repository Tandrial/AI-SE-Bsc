package de.mkrane.mse_blatt_05;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

class GeoNamesRunnable implements Runnable {

	// Lokale Variablen
	private final GeoWikiService geoWikiService;
	private double lat, lng;
	private ArrayList<WikipediaArticle> result = null;

	/*
	 * Konstruktor
	 */
	public GeoNamesRunnable(GeoWikiService geoWikiService, double latitude,
			double longitude) {
		this.geoWikiService = geoWikiService;
		this.lat = latitude;
		this.lng = longitude;
	}

	// Getter für die Liste vom Typ WikipediaArticle
	public ArrayList<WikipediaArticle> getResult() {
		return result;
	}

	@Override
	public void run() {

		// Erstellung des GeoNames-Weblinks inklusive Latitude und Longitude.
		// Sprache ist hier US, damit die Trennpunkte bei lat und lng korrekt
		// interpretiert werden.
		String urlString = String
				.format(Locale.US,
						"http://api.geonames.org/findNearbyWikipediaJSON?lat=%f&lng=%f&radius=20&maxRows=100&username=paluno",
						lat, lng);
		URL url;
		try {
			url = new URL(urlString);

			try {

				// Herstellen einer Verbindung mit der gegebenen URL sowie
				// Download der verfügbaren Daten

				// TODO implementieren Sie Teilaufgabe 10b
				HttpURLConnection con = (HttpURLConnection) url
						.openConnection();
				InputStream is = con.getInputStream();
				BufferedReader br = new BufferedReader(
						new InputStreamReader(is));

				StringBuilder sb = new StringBuilder();
				String line;
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}

				if (sb.length() > 0) {

					JSONArray entires = new JSONObject(sb.toString())
							.getJSONArray("geonames");

					result = new ArrayList<>();

					for (int i = 0; i < entires.length(); i++) {
						if (entires.isNull(i))
							continue;
						JSONObject j = entires.getJSONObject(i);
						String summary = j.isNull("summary") ? null : j
								.getString("summary");
						Float distance = j.isNull("distance") ? null
								: (float) j.getDouble("distance");
						Integer rank = j.isNull("rank") ? null : j
								.getInt("rank");
						String title = j.isNull("title") ? null : j
								.getString("title");
						URL wikipediaUrl = j.isNull("wikipediaUrl") ? null
								: new URL("https://"
										+ j.getString("wikipediaUrl"));
						Float elevation = j.isNull("elevation") ? null
								: (float) j.getDouble("elevation");
						String countryCode = j.isNull("countryCode") ? null : j
								.getString("countryCode");
						Float longitude = j.isNull("longitude") ? null
								: (float) j.getDouble("longitude");
						Float latitude = j.isNull("latitude") ? null
								: (float) j.getDouble("latitude");
						String language = j.isNull("language") ? null : j
								.getString("language");
						String feature = j.isNull("feature") ? null : j
								.getString("feature");
						URL thumbnailImgUrl = j.isNull("thumbnailImg") ? null
								: new URL(j.getString("thumbnailImg"));

						WikipediaArticle article = new WikipediaArticle(
								summary, distance, rank, title, wikipediaUrl,
								elevation, countryCode, longitude, latitude,
								language, feature, thumbnailImgUrl);
						result.add(article);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

	}

}