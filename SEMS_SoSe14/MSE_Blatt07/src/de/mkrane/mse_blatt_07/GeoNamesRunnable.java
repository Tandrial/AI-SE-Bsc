package de.mkrane.mse_blatt_07;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
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

	// Getter f�r die Liste vom Typ WikipediaArticle
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
				// Download der verf�gbaren Daten
				HttpURLConnection connection = (HttpURLConnection) url
						.openConnection();

				InputStream ins = connection.getInputStream();

				InputStreamReader reader = new InputStreamReader(ins);

				int r = 0;
				StringBuffer sBuf = new StringBuffer();
				char[] buffer = new char[1024];

				while ((r = reader.read(buffer)) > -1) {
					sBuf.append(buffer, 0, r);
				}
				String response = sBuf.toString();

				if (response != null && response.length() > 0) {

					// Falls die Webantwort erfolgreich ist, wird das
					// JSON-Objekt geparst und in ein WikipediaArticle-Objekt
					// umgewandelt. Jedes WikipediaArticle-Objekt wird einer
					// Liste hinzugef�gt.
					JSONObject json = new JSONObject(response);

					JSONArray entries = json.getJSONArray("geonames");

					for (int i = 0; i < entries.length(); i++) {

						if (entries.isNull(i))
							continue;

						if (result == null)
							result = new ArrayList<WikipediaArticle>();

						WikipediaArticle wikiArticle = new WikipediaArticle(
								entries.getJSONObject(i).isNull("summary") ? null
										: entries.getJSONObject(i).getString(
												"summary"),
								entries.getJSONObject(i).isNull("distance") ? null
										: Float.valueOf((float) entries
												.getJSONObject(i).getDouble(
														"distance")),
								entries.getJSONObject(i).isNull("rank") ? null
										: Integer.valueOf(entries
												.getJSONObject(i)
												.getInt("rank")),
								entries.getJSONObject(i).isNull("title") ? null
										: entries.getJSONObject(i).getString(
												"title"),
								entries.getJSONObject(i).isNull("wikipediaUrl") ? null
										: new URL("https://"
												+ entries.getJSONObject(i)
														.getString(
																"wikipediaUrl")),
								entries.getJSONObject(i).isNull("elevation") ? null
										: Float.valueOf((float) entries
												.getJSONObject(i).getDouble(
														"elevation")),
								entries.getJSONObject(i).isNull("countryCode") ? null
										: entries.getJSONObject(i).getString(
												"countryCode"),
								entries.getJSONObject(i).isNull("lng") ? null
										: Float.valueOf((float) entries
												.getJSONObject(i).getDouble(
														"lng")),
								entries.getJSONObject(i).isNull("lat") ? null
										: Float.valueOf((float) entries
												.getJSONObject(i).getDouble(
														"lat")),
								entries.getJSONObject(i).isNull("lang") ? null
										: entries.getJSONObject(i).getString(
												"lang"),
								entries.getJSONObject(i).isNull("feature") ? null
										: entries.getJSONObject(i).getString(
												"feature"),
								entries.getJSONObject(i).isNull("thumbnailImg") ? null
										: new URL(entries.getJSONObject(i)
												.getString("thumbnailImg")));

						result.add(wikiArticle);

					}

				}

			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

	}

}