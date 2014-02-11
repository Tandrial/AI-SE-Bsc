public class Graph {
	Knoten kopf, fuss;
	boolean flag = false;

	public void fuegeEin(Knoten k) {
		if (kopf == null) {
			this.kopf = this.fuss = k;
		} else {
			this.fuss.nf = k;
			this.fuss = this.fuss.nf;
		}
	}

	public void gibAus() {
		Knoten tmp = this.kopf;
		while (tmp != null) {
			tmp.gibAus();
			tmp = tmp.nf;
		}
	}

	public boolean gleicheKanten() {
		Knoten tmp = this.kopf;
		while (tmp != null) {
			Kante tmpK = tmp.kopf;
			while (tmpK != null) {
				if (exists(tmpK))
					return true;
				tmpK = tmpK.nf;
			}
			tmp = tmp.nf;
		}
		return false;
	}

	private boolean exists(Kante k) {
		Knoten tmp = this.kopf;
		while (tmp != null) {
			Kante tmpK = tmp.kopf;
			while (tmpK != null) {
				if (k.bez.equals(tmpK.bez) && k != tmpK)
					return true;
				tmpK = tmpK.nf;
			}
			tmp = tmp.nf;
		}
		return false;
	}

	private boolean sucheWegDFS(Knoten von, Knoten zu) {
		boolean vorhanden = false;
		von.marker = !von.marker;
		Kante kante = von.kopf;

		while (kante != null && !vorhanden) {
			if (kante.ziel == zu) {
				vorhanden = true;
			}
			if (!kante.ziel.marker ^ flag) {
				vorhanden = vorhanden || sucheWegDFS(kante.ziel, zu);
			}
			kante = kante.nf;
		}
		return vorhanden;
	}

	private void loescheMarker(Knoten k1, Knoten k2) {
		sucheWegDFS(k1, k2);
		flag = !flag;
	}

	public boolean sucheWeg(Knoten k1, Knoten k2) {
		boolean vorhanden = sucheWegDFS(k1, k2);
		flag = !flag;
		loescheMarker(k1, k2);
		return vorhanden;
	}

	public boolean istZyklenfrei() {
		Knoten tmp = this.kopf;
		while (tmp != null) {
			if (sucheWeg(tmp, tmp))
				return false;
			tmp = tmp.nf;
		}
		return true;
	}
}