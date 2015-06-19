package uni.dc.ubsOpti.tracer;

import java.util.Collection;

import org.goataa.impl.utils.Individual;

public interface Traceable {
	public void attach(Tracer tracer);

	public void attach(Collection<Tracer> tracer);

	public void detach(Tracer tracer);

	public void notifyTracer(Individual<?, ?> p, Individual<?, ?>... parents);
}
