package de.mkrane.mse_blatt02_a04;

import android.app.Application;

public class A04 extends Application {

	private int count = 0;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void incCount() {
		count++;
	}

	public void decCount() {
		count--;
	}

}
