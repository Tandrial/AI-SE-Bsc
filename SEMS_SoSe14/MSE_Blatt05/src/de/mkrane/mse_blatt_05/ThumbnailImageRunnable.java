package de.mkrane.mse_blatt_05;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

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

			// ÷ffnen einer Verbindung zum Download des Bildes und
			// anschlieﬂender Umwandlung des Bildes mittels InputStream in ein
			// Bitmap, welches nach erfolgreicher Umwandlung im article-Objekt
			// gespeichert wird.
			try {

				// TODO implementieren Sie Teilaufgabe 10c
				Log.i("down", "start " + article.toString());
				// 10c
				HttpURLConnection con = (HttpURLConnection) url
						.openConnection();
				InputStream is = con.getInputStream();
				Bitmap bmp = BitmapFactory.decodeStream(is);
				is.close();

				if (bmp != null) {
					article.setThumbnailImg(bmp);
				}
				Log.i("down", "end " + article.toString());

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
}
