public class Kurs {
	String sprache;
	boolean[] wochentage;
	float grundkosten;

	// legt einen neuen lehren Kurs ein.
	public Kurs() {
		grundkosten = 0.0f;
		wochentage = new boolean[7];
		sprache = "";
	}

	public String getSprache() {
		return this.sprache;
	}

	public boolean[] getWochentage() {
		return this.wochentage;
	}

	public float getGrundkosten() {
		return this.grundkosten;
	}

	public void setSprache(String sprache) {
		this.sprache = sprache;
	}

	public void setWochentage(boolean[] wochentage) {
		int temp_wochentage = 7;

		if (wochentage != null && wochentage.length == temp_wochentage) {
			for (int i = 0; i < temp_wochentage; i++) {
				this.wochentage[i] = wochentage[i];
			}
		}
	}

	public void setGrundkosten(float grundkosten) {
		this.grundkosten = grundkosten;
	}
}