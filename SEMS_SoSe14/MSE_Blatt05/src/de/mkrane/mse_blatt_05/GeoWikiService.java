package de.mkrane.mse_blatt_05;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.app.Service;
import android.content.Intent;
import android.location.Geocoder;
import android.os.Binder;
import android.os.IBinder;

public class GeoWikiService extends Service {

	// Lokale Variablen
	private ArrayList<IGeoWikiServiceClient> clients = new ArrayList<IGeoWikiServiceClient>();
	private ArrayList<WikipediaArticle> articles = null;
	private BlockingQueue<Runnable> blockingQueue = null;
	private ThreadPoolExecutor threadPoolExecutor = null;
	private IBinder binder = new GeoWikiServiceBinder();

	/*
	 * Private Klasse eines ThreadPoolExecutors, der die verschiedenen Runnables
	 * verarbeitet, die ihm �bergeben werden.
	 */
	private class GeoWikiServiceThreadPoolExecutor extends ThreadPoolExecutor {

		/*
		 * Konstruktor
		 */
		public GeoWikiServiceThreadPoolExecutor(int corePoolSize,
				int maximumPoolSize, long keepAliveTime, TimeUnit unit,
				BlockingQueue<Runnable> workQueue) {
			super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
		}

		// Methode beforeExecute, um im Falle des ThumbnailImageRunnable vor der
		// Ausf�hrung die Progressbar zu starten, als Indikator f�r das
		// Herunterladen des aktuellen Bildes.
		@Override
		protected void beforeExecute(Thread t, Runnable r) {

			super.beforeExecute(t, r);

			if (r instanceof ThumbnailImageRunnable) {

				ThumbnailImageRunnable tir = (ThumbnailImageRunnable) r;

				if (tir.getArticle().getThumbnailImgUrl() != null) {
					tir.getArticle().setLoading(true);
				}

				for (final IGeoWikiServiceClient client : clients) {
					client.getHandler().post(new Runnable() {
						public void run() {
							client.updateArticles();
						}
					});

				}
			}
		}

		// Methode afterExecute, die nach Beenden des Threads ausgef�hrt wird.
		// Unterscheidung an dieser Stelle nach mehreren F�llen.
		@Override
		protected void afterExecute(Runnable r, Throwable t) {

			super.afterExecute(r, t);

			// Im Falle des ThumbnailImageRunnable nach dem
			// Herunterladen des Bildes die Progressbar anzuhalten, damit
			// anstelle der Progressbar das Bild angezeigt werden kann.
			if (r instanceof ThumbnailImageRunnable) {

				ThumbnailImageRunnable tir = (ThumbnailImageRunnable) r;

				tir.getArticle().setLoading(false);

				for (final IGeoWikiServiceClient client : clients) {
					client.getHandler().post(new Runnable() {
						public void run() {
							client.updateArticles();
						}
					});

				}

			}

			// Im Falle von GeoNamesRunnable wird nach erfolgreichem Download
			// der anschlie�ende Download der einzelnen Thumbnails angesto�en.
			if (r instanceof GeoNamesRunnable) {

				GeoNamesRunnable gnr = (GeoNamesRunnable) r;

				if (gnr.getResult() != null) {

					articles = new ArrayList<WikipediaArticle>(gnr.getResult());

					for (final IGeoWikiServiceClient client : clients) {
						client.getHandler().post(new Runnable() {
							public void run() {
								client.didDownloadWikiData();
							}
						});

					}

					for (WikipediaArticle article : articles) {

						threadPoolExecutor.execute(new ThumbnailImageRunnable(
								article));

					}

				} else {

					for (final IGeoWikiServiceClient client : clients) {
						client.getHandler().post(new Runnable() {
							public void run() {
								client.failedDownloadGeoNames();
							}
						});

					}
				}
			}

			// Im Falle von GeoCodeRunnable wird nach erfolgreicher Umwandlung
			// von Adresse zu Geokoordinaten die Methode
			// didConvertToLatitudeLongitude mit �bergabe der Werte gestartet.
			if (r instanceof GeoCodeRunnable) {

				final GeoCodeRunnable gcr = (GeoCodeRunnable) r;

				if (gcr.getAddress() == null) {
					for (final IGeoWikiServiceClient client : clients) {
						client.getHandler().post(new Runnable() {
							public void run() {
								client.failedConvertToLatitudeLongitude();
							}
						});

					}
				} else {

					for (final IGeoWikiServiceClient client : clients) {
						client.getHandler().post(new Runnable() {
							public void run() {
								client.didConvertToLatitudeLongitude(gcr
										.getAddress().getLatitude(), gcr
										.getAddress().getLongitude());
							}
						});

					}
				}
			}
		}
	}

	// �ffentliche Methode geoCode, die mit �bermittlung eines Request-Strings
	// einen Geocode-Thread startet und versucht von Adresse auf Geokoordinaten
	// zu mappen.
	public void geoCode(String request) {

		if (Geocoder.isPresent()) {
			this.threadPoolExecutor.execute(new GeoCodeRunnable(this, request));
		} else {
			for (IGeoWikiServiceClient client : clients) {
				client.failedConvertToLatitudeLongitude();
			}
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();

		// Erzeugung einer BlockingQueue
		this.blockingQueue = new LinkedBlockingQueue<Runnable>();
		// Erzeugung eines ThreadPoolExecutors mit eine genau 4 parallelen
		// Threads
		this.threadPoolExecutor = new GeoWikiServiceThreadPoolExecutor(4, 4,
				Long.MAX_VALUE, TimeUnit.NANOSECONDS, this.blockingQueue);
	}

	// Methode onBind mit R�ckgabe des Binders
	@Override
	public IBinder onBind(Intent arg0) {
		return binder;
	}

	/*
	 * Private Binder-Klasse
	 */
	public class GeoWikiServiceBinder extends Binder {

		public GeoWikiService getService() {
			return GeoWikiService.this;
		}

	}

	// Methode zum Regestrieren am Client
	public void registerClient(IGeoWikiServiceClient client) {
		this.clients.add(client);
	}

	// Methode zum Deregistrieren vom Client
	public void deregisterClient(IGeoWikiServiceClient client) {
		this.clients.remove(client);
	}

	// Getter f�r articles-Objekt
	public ArrayList<WikipediaArticle> getArticles() {
		return this.articles;
	}

	// �ffentliche Methode zum Start des Threads zum Download von GeoNames
	public void downloadFomGeoNames(double latitude, double longitude) {

		this.threadPoolExecutor.execute(new GeoNamesRunnable(this, latitude,
				longitude));

	}

}
