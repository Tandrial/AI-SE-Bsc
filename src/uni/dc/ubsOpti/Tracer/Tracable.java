package uni.dc.ubsOpti.Tracer;

import uni.dc.ubsOpti.OptimizerConfig;

public interface Tracable {
	public void setUpTrace(OptimizerConfig config);
	public DelayTrace getTrace();		
}
