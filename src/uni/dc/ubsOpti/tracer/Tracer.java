package uni.dc.ubsOpti.tracer;

import java.io.Serializable;

//TODO abstracte Oberklasse für die verschiedenen Tracer
public abstract class Tracer implements Serializable {
	private static final long serialVersionUID = 1L;

	public abstract void update(TracerStat stat);

	public abstract void clearData();
}
