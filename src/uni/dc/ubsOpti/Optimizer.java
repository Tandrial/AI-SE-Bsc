package uni.dc.ubsOpti;

import java.util.List;

import org.goataa.impl.algorithms.ea.selection.TournamentSelection;
import org.goataa.impl.algorithms.sa.temperatureSchedules.Logarithmic;
import org.goataa.impl.utils.Individual;

import uni.dc.model.PriorityConfiguration;
import uni.dc.ubsOpti.goataaExt.algorithms.BackTrackingTraceable;
import uni.dc.ubsOpti.goataaExt.algorithms.BruteForceTraceable;
import uni.dc.ubsOpti.goataaExt.algorithms.HillClimbingTraceable;
import uni.dc.ubsOpti.goataaExt.algorithms.LocalSearchAlgorithmTraceable;
import uni.dc.ubsOpti.goataaExt.algorithms.SimpleGenerationalTraceable;
import uni.dc.ubsOpti.goataaExt.algorithms.SimulatedAnnealingTraceable;
import uni.dc.ubsOpti.goataaExt.searchOperations.strings.integer.IntVectorCreation;
import uni.dc.ubsOpti.goataaExt.searchOperations.strings.integer.IntVectorMutation;
import uni.dc.ubsOpti.goataaExt.searchOperations.strings.integer.binary.IntArrayWeightedMeanCrossover;
import uni.dc.ubsOpti.goataaExt.searchOperations.strings.integer.nullary.IntArrayAllOnesCreation;
import uni.dc.ubsOpti.goataaExt.searchOperations.strings.integer.unary.IntArrayAllNormalMutation;
import uni.dc.ubsOpti.goataaExt.searchOperations.strings.integer.unary.IntArrayBestMutation;
import uni.dc.ubsOpti.goataaExt.termination.UbsStepDelayTermination;
import uni.dc.ubsOpti.tracer.DelayTrace;

public class Optimizer {

	private static Optimizer optimizer = new Optimizer();

	public static Optimizer getOptimizer() {
		return Optimizer.optimizer;
	}

	private IntVectorCreation create = null;
	private IntVectorMutation mutate = null;
	private UbsOptiConfig config = null;

	private Optimizer() {

	}

	public boolean optimize(UbsOptiConfig config, String selectedAlgo) {
		this.config = config;
		create = new IntArrayAllOnesCreation(config.getDim(), 1, config.getMaxPrio());
		mutate = new IntArrayBestMutation(1, config.getMaxPrio(), config.getDelayCalc());

		DelayTrace trace = null;
		if (selectedAlgo.equals("BruteForce")) {
			trace = optimizeBruteForce();
		} else if (selectedAlgo.equals("BackTrack")) {
			trace = optimizeBackTrack();
		} else if (selectedAlgo.equals("SimulatedAnnealing")) {
			trace = optimizeSimulatedAnnealing();
		} else if (selectedAlgo.equals("HillClimbing")) {
			trace = optimizeHillClimbing();
		} else if (selectedAlgo.equals("SimpleGenerationalEA")) {
			trace = optimizeSimpleGenerationalEA();
		}

		trace.setAlgoName(config.getTraces().size() + "_" + trace.getAlgoName());
		config.getTraces().add(trace);
		PriorityConfiguration bestConfig = trace.getBestConfig();
		if (bestConfig != null) {
			config.getDelayCalc().calculateDelays(bestConfig);
			return config.getDelayCalc().checkDelays();
		} else {
			return false;
		}
	}

	private DelayTrace optimizeBruteForce() {
		BruteForceTraceable BF = new BruteForceTraceable();
		BF.setDim(create.n);
		BF.setMaxPrio(mutate.max);
		run(BF);
		return BF.getTrace();
	}

	private DelayTrace optimizeBackTrack() {
		BackTrackingTraceable BT = new BackTrackingTraceable();
		BT.setConfig(config);
		BT.setMaxPrio(mutate.max);
		run(BT);
		return BT.getTrace();

	}

	private DelayTrace optimizeHillClimbing() {
		HillClimbingTraceable<int[], int[]> HC = new HillClimbingTraceable<int[], int[]>();
		mutate = new IntArrayAllNormalMutation(1, config.getMaxPrio());
		run(HC);
		return HC.getTrace();
	}

	private DelayTrace optimizeSimulatedAnnealing() {
		SimulatedAnnealingTraceable<int[], int[]> SA = new SimulatedAnnealingTraceable<int[], int[]>();
		SA.setTemperatureSchedule(new Logarithmic(1d));
		run(SA);
		return SA.getTrace();
	}

	private DelayTrace optimizeSimpleGenerationalEA() {
		SimpleGenerationalTraceable<int[], int[]> GA = new SimpleGenerationalTraceable<int[], int[]>();
		GA.setBinarySearchOperation(IntArrayWeightedMeanCrossover.INT_ARRAY_WEIGHTED_MEAN_CROSSOVER);
		GA.setSelectionAlgorithm(new TournamentSelection(2));
		run(GA);
		return GA.getTrace();
	}

	@SuppressWarnings("unchecked")
	private final int[] run(final LocalSearchAlgorithmTraceable<int[], int[], ?> algorithm) {
		List<Individual<?, int[]>> solutions;
		Individual<?, int[]> individual = null;
		double bestValue = Double.MAX_VALUE;

		UbsStepDelayTermination term = new UbsStepDelayTermination(config);
		algorithm.setObjectiveFunction(config.getDelayCalc());
		algorithm.setTerminationCriterion(term);
		algorithm.setUpTrace(config);
		algorithm.setNullarySearchOperation(create);
		algorithm.setUnarySearchOperation(mutate);

		for (int i = 0; i < config.getRuns(); i++) {
			algorithm.setRandSeed(i + System.currentTimeMillis());
			solutions = ((List<Individual<?, int[]>>) (algorithm.call()));
			if (solutions.get(0).v < bestValue) {
				individual = solutions.get(0);
				bestValue = individual.v;
			}
			if (term.foundDelay())
				return individual.x;
			term.reset();
		}
		return individual.x;
	}
}
