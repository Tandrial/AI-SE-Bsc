package de.mkrane.mse_blatt_05;

import java.io.IOException;
import java.util.List;

import android.location.Address;
import android.location.Geocoder;

class GeoCodeRunnable implements Runnable {

	// Lokale Variablen
	private final GeoWikiService geoWikiService;
	private String request;
	private Address address;

	// Getter für Request-String
	public String getRequest() {
		return request;
	}

	// Getter für Address-Objekt
	public Address getAddress() {
		return address;
	}

	/*
	 * Konstruktor
	 */
	public GeoCodeRunnable(GeoWikiService geoWikiService, String request) {
		this.geoWikiService = geoWikiService;
		this.request = request;
	}

	@Override
	public void run() {

		Geocoder coder = new Geocoder(this.geoWikiService);

		// Umwandlung des Request-Strings in ein gültiges Geokoordinatenpaar
		try {
			List<Address> addresses = coder.getFromLocationName(request, 1);

			if (addresses != null && addresses.size() != 0) {
				address = addresses.get(0);
			} else {

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}