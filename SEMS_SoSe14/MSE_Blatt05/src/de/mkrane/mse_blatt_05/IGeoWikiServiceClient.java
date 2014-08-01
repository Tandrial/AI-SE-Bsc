package de.mkrane.mse_blatt_05;

import android.os.Handler;

public interface IGeoWikiServiceClient {

	public void didConvertToLatitudeLongitude(double latitue, double longitude);

	public void failedConvertToLatitudeLongitude();

	public Handler getHandler();

	public void failedDownloadGeoNames();

	public void didDownloadWikiData();

	public void updateArticles();

}
