package uni.dc.ubsOpti.tracer;

import uni.dc.ubsOpti.UbsOptiConfig;

public interface Traceable {
	public void setUpTrace(UbsOptiConfig config);

	public DelayTrace getTrace();
}
