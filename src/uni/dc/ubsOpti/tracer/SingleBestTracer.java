package uni.dc.ubsOpti.tracer;

public class SingleBestTracer extends Tracer {

	private static final long serialVersionUID = 1L;
	TracerStat best = null;

	@Override
	public void update(final TracerStat stat) {
		if (stat.getPrio().length == 0)
			return;
		if (best == null)
			best = stat;
		else if (best.getDelay() > stat.getDelay())
			best = stat;
	}

	public TracerStat getBest() {
		return best;
	}
}
