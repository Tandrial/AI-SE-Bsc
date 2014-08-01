package de.mkrane.mse_blatt04_a09;

import java.util.ArrayList;

import android.app.Application;
import android.content.Context;

public class CustomApplication extends Application {

	public ArrayList<String[]> createCountryArrayListIDs(Context context) {

		ArrayList<String[]> listOfStringArrays = new ArrayList<String[]>();

		listOfStringArrays.add(context.getResources().getStringArray(
				R.array.country_array_a));
		listOfStringArrays.add(context.getResources().getStringArray(
				R.array.country_array_b));
		listOfStringArrays.add(context.getResources().getStringArray(
				R.array.country_array_c));
		listOfStringArrays.add(context.getResources().getStringArray(
				R.array.country_array_d));
		listOfStringArrays.add(context.getResources().getStringArray(
				R.array.country_array_e));
		listOfStringArrays.add(context.getResources().getStringArray(
				R.array.country_array_f));
		listOfStringArrays.add(context.getResources().getStringArray(
				R.array.country_array_g));
		listOfStringArrays.add(context.getResources().getStringArray(
				R.array.country_array_h));
		listOfStringArrays.add(context.getResources().getStringArray(
				R.array.country_array_i));
		listOfStringArrays.add(context.getResources().getStringArray(
				R.array.country_array_j));
		listOfStringArrays.add(context.getResources().getStringArray(
				R.array.country_array_k));
		listOfStringArrays.add(context.getResources().getStringArray(
				R.array.country_array_l));
		listOfStringArrays.add(context.getResources().getStringArray(
				R.array.country_array_m));
		listOfStringArrays.add(context.getResources().getStringArray(
				R.array.country_array_n));
		listOfStringArrays.add(context.getResources().getStringArray(
				R.array.country_array_o));
		listOfStringArrays.add(context.getResources().getStringArray(
				R.array.country_array_p));
		listOfStringArrays.add(context.getResources().getStringArray(
				R.array.country_array_r));
		listOfStringArrays.add(context.getResources().getStringArray(
				R.array.country_array_s));
		listOfStringArrays.add(context.getResources().getStringArray(
				R.array.country_array_t));
		listOfStringArrays.add(context.getResources().getStringArray(
				R.array.country_array_u));
		listOfStringArrays.add(context.getResources().getStringArray(
				R.array.country_array_v));
		listOfStringArrays.add(context.getResources().getStringArray(
				R.array.country_array_w));
		listOfStringArrays.add(context.getResources().getStringArray(
				R.array.country_array_z));

		return listOfStringArrays;
	}
}
