package uni.dc.ubsOpti;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

import uni.dc.model.EgressTopology;
import uni.dc.model.PriorityConfiguration;
import uni.dc.model.Traffic;
import uni.dc.ubsOpti.DelayCalc.UbsDelayCalc;
import uni.dc.ubsOpti.DelayCalc.UbsV0DelayCalc;
import uni.dc.ubsOpti.Tracer.DelayTrace;
import uni.dc.ubsOpti.Tracer.TraceCollection;

public class OptimizerConfig implements Serializable {

	private static final long serialVersionUID = 1L;
	private int maxPrio = 2;
	private int maxSteps = 3000000;
	private int runs = 2;

	private Traffic traffic;
	private EgressTopology topology;
	private PriorityConfiguration prio;
	private UbsDelayCalc delayCalc;
	private TraceCollection traces;

	public OptimizerConfig(EgressTopology topology, Traffic traffic,
			PriorityConfiguration prio, UbsDelayCalc delayCalc,
			TraceCollection traces) {
		this.topology = topology;
		this.traffic = traffic;
		this.prio = prio;
		this.delayCalc = delayCalc;
		this.traces = traces;
	}

	public int getMaxPrio() {
		return maxPrio;
	}

	public void setMaxPrio(int maxPrio) {
		this.maxPrio = maxPrio;
	}

	public int getMaxSteps() {
		return maxSteps;
	}

	public void setMaxSteps(int maxSteps) {
		this.maxSteps = maxSteps;
	}

	public int getRuns() {
		return runs;
	}

	public void setRuns(int runs) {
		this.runs = runs;
	}

	public Traffic getTraffic() {
		return traffic;
	}

	public void setTraffic(Traffic traffic) {
		this.traffic = traffic;
	}

	public EgressTopology getTopology() {
		return topology;
	}

	public void setTopology(EgressTopology topology) {
		this.topology = topology;
	}

	public PriorityConfiguration getPriorityConfig() {
		return prio;
	}

	public void setPriorityConfig(PriorityConfiguration prio) {
		this.prio = prio;
	}

	public UbsDelayCalc getDelayCalc() {
		return delayCalc;
	}

	public void setDelayCalc(UbsDelayCalc delayCalc) {
		this.delayCalc = delayCalc;
	}

	public TraceCollection getTraces() {
		return traces;
	}

	public void setTraces(TraceCollection traces) {
		this.traces = traces;
	}

	public static void saveToFile(File file, OptimizerConfig config) {
		try {
			FileOutputStream fileOut = new FileOutputStream(file);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(config.getTopology());
			out.writeObject(config.getTraffic());
			out.writeObject(config.getPriorityConfig());
			out.writeObject(config.getDelayCalc());
			out.writeObject(config.getTraces());
			out.close();
			fileOut.close();
		} catch (IOException i) {
			i.printStackTrace();
		}
	}

	public static OptimizerConfig loadFromFile(File file) {
		try {
			FileInputStream fileIn = new FileInputStream(file);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			EgressTopology topo = (EgressTopology) in.readObject();
			Traffic traffic = (Traffic) in.readObject();
			PriorityConfiguration prio = (PriorityConfiguration) in
					.readObject();
			TraceCollection traces = (TraceCollection) in.readObject();
			UbsDelayCalc delayCalc = new UbsV0DelayCalc(traffic);
			OptimizerConfig config = new OptimizerConfig(topo, traffic, prio,
					delayCalc, traces);
			in.close();
			fileIn.close();
			return config;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
