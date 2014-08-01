package de.mkrane.mse_blatt04_a09;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment implements
			LoaderManager.LoaderCallbacks<List<String>> {

		private ArrayAdapter<String> adapter;
		private ArrayList<String> stringList;
		private ArrayList<String[]> listOfStringArrays;
		private int counter = 0;

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);

			this.listOfStringArrays = ((CustomApplication) getActivity()
					.getApplication()).createCountryArrayListIDs(getActivity());

			((Button) rootView.findViewById(R.id.button_get_countries))
					.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							// ... zu programmieren ...
							getLoaderManager().getLoader(0).forceLoad();
						}
					});

			// Erzeugung des Adapters für die Liste
			// ... zu programmieren ...
			adapter = new ArrayAdapter<>(getActivity(),
					android.R.layout.simple_list_item_1, this.stringList);

			((ListView) rootView.findViewById(R.id.listViewCountry))
					.setAdapter(adapter);

			// init Loader
			// ... zu programmieren ...
			getLoaderManager().initLoader(0, null, this);
			return rootView;
		}

		@Override
		public Loader<List<String>> onCreateLoader(int id, Bundle args) {
			CustomAsyncTaskLoader loader = new CustomAsyncTaskLoader(
					getActivity());
			loader.loadCustomArray(this.listOfStringArrays.get(this.counter++));
			return loader;
		}

		@Override
		public void onLoadFinished(Loader<List<String>> loader,
				List<String> data) {
			this.stringList.addAll(data);
			this.adapter.notifyDataSetChanged();
			getLoaderManager().restartLoader(0, null, this);
		}

		@Override
		public void onLoaderReset(Loader<List<String>> loader) {

		}
	}

}
