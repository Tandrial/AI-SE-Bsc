package de.mkrane.mse_blatt_07;

import java.util.List;

import de.mkrane.blatt_07.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class WikipediaArticleArrayAdapter extends
		ArrayAdapter<WikipediaArticle> {

	// Lokale Variablen
	private Context context;
	private List<WikipediaArticle> objects;

	/*
	 * Konstruktor
	 */
	public WikipediaArticleArrayAdapter(Context context,
			int textViewResourceId, List<WikipediaArticle> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.objects = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = convertView;
		// ViewHolder zur Wiederverwendung der einmal initialisierten View
		ViewHolder viewHolder;

		if (view == null) {
			// Initialisierung der Elemente der benutzerspezifischen Listenreihe
			// und Zuweisung an den ViewHolder

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			// Zuweisung des zu verwendenden Layouts
			view = inflater.inflate(de.mkrane.blatt_07.R.layout.custom_listrow,
					null);

			viewHolder = new ViewHolder();
			viewHolder.imageViewThumbnail = (ImageView) view
					.findViewById(R.id.imageView_thumbnail);
			viewHolder.progressBarThumbnail = (ProgressBar) view
					.findViewById(R.id.progressBar_thumbnail);
			viewHolder.textViewTitle = (TextView) view
					.findViewById(R.id.textView_listrow_title);
			viewHolder.textViewDescription = (TextView) view
					.findViewById(R.id.textView_listrow_description);

			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}

		if (objects != null) {
			// Benutzerspezifische Listenelemente mit Inhalt der �bergebenen
			// Daten f�llen.

			WikipediaArticle article = objects.get(position);

			viewHolder.imageViewThumbnail
					.setVisibility(article.isLoading() ? View.INVISIBLE
							: View.VISIBLE);
			viewHolder.progressBarThumbnail
					.setVisibility(!article.isLoading() ? View.INVISIBLE
							: View.VISIBLE);

			viewHolder.textViewDescription.setText(article.getSummary());
			viewHolder.textViewTitle.setText(article.getTitle());

			if (article.getThumbnailImg() != null) {
				viewHolder.imageViewThumbnail.setImageBitmap(article
						.getThumbnailImg());
			} else {
				viewHolder.imageViewThumbnail.setImageBitmap(null);
			}

		}
		return view;
	}

	/*
	 * Private Klasse von ViewHolder mit den 4 Elementen der
	 * benutzerspezifischen Listenreihe
	 */
	static class ViewHolder {

		TextView textViewTitle;
		TextView textViewDescription;
		ProgressBar progressBarThumbnail;
		ImageView imageViewThumbnail;

	}

}
