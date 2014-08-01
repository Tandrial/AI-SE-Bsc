package de.mkrane.mse_blatt04_a09;

import java.util.ArrayList;
import java.util.List;

import android.content.AsyncTaskLoader;
import android.content.Context;

public class CustomAsyncTaskLoader extends AsyncTaskLoader<List<String>> {

	private static String[] countryArray;

	public CustomAsyncTaskLoader(Context context) {
		super(context);
	}

	public List<String> loadInBackground() {

		ArrayList<String> result = new ArrayList<>();

		for (int i = 0; i < CustomAsyncTaskLoader.countryArray.length; i++) {
			result.add(CustomAsyncTaskLoader.countryArray[i]);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	public void loadCustomArray(String[] array) {
		CustomAsyncTaskLoader.countryArray = array;
	}

}
