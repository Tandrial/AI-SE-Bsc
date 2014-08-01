package de.mkrane.mse_blatt08_a13a;

import java.io.Serializable;

import android.hardware.Sensor;

public class MySensor implements Serializable {

	private static final long serialVersionUID = -679702602295335472L;

	private float maxRange;
	private String name;
	private float power;
	private float res;
	private int type;
	private String vendor;
	private int version;

	public MySensor(Sensor s) {

		maxRange = s.getMaximumRange();
		name = s.getName();
		power = s.getPower();
		res = s.getResolution();
		type = s.getType();
		vendor = s.getVendor();
		version = s.getVersion();

	}

	public float getMaxRange() {
		return maxRange;
	}

	public void setMaxRange(float maxRange) {
		this.maxRange = maxRange;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getPower() {
		return power;
	}

	public void setPower(float power) {
		this.power = power;
	}

	public float getRes() {
		return res;
	}

	public void setRes(float res) {
		this.res = res;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

}
