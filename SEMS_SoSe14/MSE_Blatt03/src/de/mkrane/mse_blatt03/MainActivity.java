package de.mkrane.mse_blatt03;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class MainActivity extends Activity {
	private int k = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Resources r = getResources();
		menu.add(0, R.menu.linlayout, 0, r.getString(R.string.linlayout));
		menu.add(0, R.menu.rellayout, 0, r.getString(R.string.rellayout));
		menu.add(0, R.menu.listview, 0, r.getString(R.string.listview));

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		case R.menu.linlayout:
			exchangeLinearLayoutOrientation();
			return true;
		case R.menu.rellayout:
			exchangeTextviewPosition();
			return true;
		case R.menu.listview:
			Intent intent = new Intent(MainActivity.this,
					ListViewActivity.class);
			startActivity(intent);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void exchangeTextviewPosition() {
		LinearLayout lin = (LinearLayout) findViewById(R.id.uilinlayout);
		RelativeLayout.LayoutParams lp = (LayoutParams) lin.getLayoutParams();
		if (k % 2 == 0)
			lp.addRule(RelativeLayout.BELOW, R.id.container);
		else
			lp.addRule(RelativeLayout.BELOW, R.id.textView1);

		lin.setLayoutParams(lp);

		TextView tView = (TextView) findViewById(R.id.textView1);
		lp = (LayoutParams) tView.getLayoutParams();
		if (k++ % 2 == 0)
			lp.addRule(RelativeLayout.BELOW, R.id.uilinlayout);
		else
			lp.addRule(RelativeLayout.BELOW, R.id.container);
		tView.setLayoutParams(lp);
	}

	private void exchangeLinearLayoutOrientation() {
		LinearLayout layout = (LinearLayout) findViewById(R.id.uilinlayout);
		if (layout.getOrientation() == LinearLayout.VERTICAL)
			layout.setOrientation(LinearLayout.HORIZONTAL);
		else
			layout.setOrientation(LinearLayout.VERTICAL);
	}
}