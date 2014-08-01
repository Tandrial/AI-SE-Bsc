package de.mkrane.mse_blatt03;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

public class ListViewActivity extends Activity {

	private ArrayAdapter<String> ap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ap = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, getResources()
						.getStringArray(R.array.items));

		if (Resources.getSystem().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			setContentView(R.layout.listview_vert);
			GridView gView = (GridView) findViewById(R.id.gridView1);
			gView.setAdapter(ap);

		} else {
			setContentView(R.layout.listview_land);
			ListView lView = (ListView) findViewById(R.id.listView);
			lView.setAdapter(ap);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, R.menu.gotomain, 0,
				getResources().getString(R.string.gomain));
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.menu.gotomain) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
			setContentView(R.layout.listview_vert);
			GridView gView = (GridView) findViewById(R.id.gridView1);
			gView.setAdapter(ap);
		} else {
			setContentView(R.layout.listview_land);
			ListView lView = (ListView) findViewById(R.id.listView);
			lView.setAdapter(ap);
		}
	}
}
