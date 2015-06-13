package uni.dc.ubsOpti.tracer;

import uni.dc.ubsOpti.UbsOptiConfig;

public interface Tracable {
	public void setUpTrace(UbsOptiConfig config);
	public DelayTrace getTrace();		
}
