package de.mkrane.mse_blatt_07;

import java.io.Serializable;
import java.net.URL;

import android.graphics.Bitmap;

public class WikipediaArticle implements Comparable<WikipediaArticle>,
		Serializable {

	private static final long serialVersionUID = -142319762622193941L;

	private String summary;
	private Float distance;
	private Integer rank;
	private String title;
	private URL wikipediaUrl;
	private Float elevation;
	private String countryCode;
	private Float longitude;
	private Float latitude;
	private String language;
	private String feature;
	private Bitmap thumbnailImg;
	private URL thumbnailImgUrl;
	private boolean loading;
	
	

	public URL getThumbnailImgUrl() {
		return thumbnailImgUrl;
	}

	public void setThumbnailImgUrl(URL thumbnailImgUrl) {
		this.thumbnailImgUrl = thumbnailImgUrl;
	}

	public boolean isLoading() {
		return loading;
	}

	public void setLoading(boolean loading) {
		this.loading = loading;
	}

	public WikipediaArticle(String summary, Float distance, Integer rank,
			String title, URL wikipediaUrl, Float elevation,
			String countryCode, Float longitude, Float latitude,
			String language, String feature, URL thumbnailImgUrl) {

		super();
		
		this.summary = summary;
		this.distance = distance;
		this.rank = rank;
		this.title = title;
		this.wikipediaUrl = wikipediaUrl;
		this.elevation = elevation;
		this.countryCode = countryCode;
		this.longitude = longitude;
		this.latitude = latitude;
		this.language = language;
		this.feature = feature;
		this.thumbnailImgUrl = thumbnailImgUrl;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public Float getDistance() {
		return distance;
	}

	public void setDistance(Float distance) {
		this.distance = distance;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public URL getWikipediaUrl() {
		return wikipediaUrl;
	}

	public void setWikipediaUrl(URL wikipediaUrl) {
		this.wikipediaUrl = wikipediaUrl;
	}

	public Float getElevation() {
		return elevation;
	}

	public void setElevation(Float elevation) {
		this.elevation = elevation;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public Float getLongitude() {
		return longitude;
	}

	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}

	public Float getLatitude() {
		return latitude;
	}

	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}

	public Bitmap getThumbnailImg() {
		return thumbnailImg;
	}

	public void setThumbnailImg(Bitmap thumbnailImg) {
		this.thumbnailImg = thumbnailImg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 * 
	 * this return value is used by the standard implementation to display the
	 * content of a list row
	 */
	@Override
	public String toString() {
		// return "WikipediaArticle [summary=" + summary + ", distance="
		// + distance + ", rank=" + rank + ", title=" + title
		// + ", wikipediaUrl=" + wikipediaUrl + ", elevation=" + elevation
		// + ", countryCode=" + countryCode + ", longitude=" + longitude
		// + ", latitude=" + latitude + ", language=" + language
		// + ", feature=" + feature + ", thumbnailImg=" + thumbnailImg
		// + "]";
		return title;
	}

	@Override
	public int compareTo(WikipediaArticle another) {

		if (this.title == null || another.title == null)
			return 0;

		return this.title.compareTo(another.title);

	}

}
