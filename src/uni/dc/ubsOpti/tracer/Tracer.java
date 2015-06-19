package uni.dc.ubsOpti.tracer;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public abstract class Tracer implements Serializable {
	private static final long serialVersionUID = 1L;

	public abstract void update(final TracerStat stat);
}
