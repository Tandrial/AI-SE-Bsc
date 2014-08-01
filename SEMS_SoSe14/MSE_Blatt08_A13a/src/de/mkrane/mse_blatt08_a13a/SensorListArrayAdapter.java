package de.mkrane.mse_blatt08_a13a;

import java.util.List;

import android.content.Context;
import android.hardware.Sensor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SensorListArrayAdapter extends ArrayAdapter<Sensor> {

	private List<Sensor> items;
	private TextView name;
	private TextView resolution;
	private TextView type;

	private Context context;

	public SensorListArrayAdapter(Context context, int textViewResourceId,
			List<Sensor> tmpList) {
		super(context, textViewResourceId, tmpList);
		this.items = tmpList;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;

		if (v == null) {
			LayoutInflater vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			v = vi.inflate(R.layout.list_row, null);
			name = (TextView) v.findViewById(R.id.textView_name);
			resolution = (TextView) v.findViewById(R.id.textView_resolution);
			type = (TextView) v.findViewById(R.id.textView_type);
		}

		Sensor currentItem = items.get(position);
		if (currentItem != null) {
			name.setText(currentItem.getName());
			resolution.setText(String.valueOf(currentItem.getResolution()));
			type.setText(String.valueOf(currentItem.getType()));
		}
		return v;

	}
}
