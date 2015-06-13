package uni.dc.ubsOpti.Tracer;

import uni.dc.ubsOpti.UbsOptiConfig;

public interface Tracable {
	public void setUpTrace(UbsOptiConfig config);
	public DelayTrace getTrace();		
}
