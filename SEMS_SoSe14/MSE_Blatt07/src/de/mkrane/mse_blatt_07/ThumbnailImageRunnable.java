package de.mkrane.mse_blatt_07;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ThumbnailImageRunnable implements Runnable {

	// Lokale Variable
	private WikipediaArticle article;

	// Lokale Variable
	public WikipediaArticle getArticle() {
		return article;
	}

	/*
	 * Konstruktor
	 */
	ThumbnailImageRunnable(WikipediaArticle article) {
		this.article = article;
	}

	@Override
	public void run() {

		if (article.getThumbnailImgUrl() != null) {

			URL url = article.getThumbnailImgUrl();
			// Erstellung einer HttpURLConnection
			HttpURLConnection connection;

			// �ffnen einer Verbindung zum Download des Bildes und
			// anschlie�ender Umwandlung des Bildes mittels InputStream in ein
			// Bitmap, welches nach erfolgreicher Umwandlung im article-Objekt
			// gespeichert wird.
			try {
				connection = (HttpURLConnection) url.openConnection();
				InputStream input = connection.getInputStream();
				Bitmap theBitmap = BitmapFactory.decodeStream(input);
				input.close();

				if (theBitmap != null) {
					article.setThumbnailImg(theBitmap);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
}
