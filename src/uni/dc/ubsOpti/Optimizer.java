package uni.dc.ubsOpti;

import java.util.List;

import org.goataa.impl.algorithms.ea.selection.TournamentSelection;
import org.goataa.impl.algorithms.sa.temperatureSchedules.Logarithmic;
import org.goataa.impl.utils.Individual;
import org.goataa.spec.INullarySearchOperation;
import org.goataa.spec.ISOOptimizationAlgorithm;
import org.goataa.spec.IUnarySearchOperation;

import uni.dc.ubsOpti.goataaExt.algorithms.BruteForceTrace;
import uni.dc.ubsOpti.goataaExt.algorithms.HillClimbingTrace;
import uni.dc.ubsOpti.goataaExt.algorithms.RandomWalkTrace;
import uni.dc.ubsOpti.goataaExt.algorithms.SimpleGenerationalTrace;
import uni.dc.ubsOpti.goataaExt.algorithms.SimulatedAnnealingTrace;
import uni.dc.ubsOpti.goataaExt.searchOperations.strings.integer.binary.IntArrayWeightedMeanCrossover;
import uni.dc.ubsOpti.goataaExt.searchOperations.strings.integer.nullary.IntArrayAllOnesCreation;
import uni.dc.ubsOpti.goataaExt.searchOperations.strings.integer.unary.IntArrayAllNormalMutation;
import uni.dc.ubsOpti.goataaExt.termination.UbsDelayTermination;
import uni.dc.ubsOpti.tracer.DelayTrace;

public class Optimizer {

	private static Optimizer optimizer = new Optimizer();

	public static Optimizer getOptimizer() {
		return Optimizer.optimizer;
	}

	private INullarySearchOperation<int[]> create = null;
	private IUnarySearchOperation<int[]> mutate = null;
	private UbsOptiConfig config = null;

	private Optimizer() {

	}

	public boolean optimize(UbsOptiConfig config, String selectedAlgo) {
		this.config = config;
		create = new IntArrayAllOnesCreation(config.getDim(), 1,
				config.getMaxPrio());
		mutate = new IntArrayAllNormalMutation(1, config.getMaxPrio());

		DelayTrace trace = null;
		if (selectedAlgo.equals("BruteForce")) {
			trace = optimizeBruteForce();
		} else if (selectedAlgo.equals("SimulatedAnnealing")) {
			trace = optimizeSimulatedAnnealing();
		} else if (selectedAlgo.equals("HillClimbing")) {
			trace = optimizeHillClimbing();
		} else if (selectedAlgo.equals("SimpleGenerationalEA")) {
			trace = optimizeSimpleGenerationalEA();
		} else if (selectedAlgo.equals("RandomWalk")) {
			trace = optimizeRandomWalk();
		}
		trace.setName(config.getTraces().size() + "_" + trace.getName());
		config.getTraces().add(trace);
		config.getDelayCalc().calculateDelays(trace.getBestConfig());
		return config.getDelayCalc().checkDelays();
	}

	private DelayTrace optimizeBruteForce() {
		BruteForceTrace BF = new BruteForceTrace(config.getDelayCalc());
		BF.setUpTrace(config);
		BF.optimize(config.getPriorityConfig(), config.getMaxPrio());
		return BF.getTrace();
	}

	private DelayTrace optimizeHillClimbing() {
		HillClimbingTrace<int[], int[]> HC = new HillClimbingTrace<int[], int[]>();
		HC.setUpTrace(config);
		HC.setObjectiveFunction(config.getDelayCalc());
		HC.setNullarySearchOperation(create);
		HC.setUnarySearchOperation(mutate);
		run(HC);
		return HC.getTrace();
	}

	private DelayTrace optimizeSimulatedAnnealing() {
		SimulatedAnnealingTrace<int[], int[]> SA = new SimulatedAnnealingTrace<int[], int[]>();
		SA.setUpTrace(config);
		SA.setObjectiveFunction(config.getDelayCalc());
		SA.setNullarySearchOperation(create);
		SA.setTemperatureSchedule(new Logarithmic(1d));
		SA.setUnarySearchOperation(mutate);
		run(SA);
		return SA.getTrace();
	}

	private DelayTrace optimizeSimpleGenerationalEA() {
		SimpleGenerationalTrace<int[], int[]> GA = new SimpleGenerationalTrace<int[], int[]>();
		GA.setUpTrace(config);
		GA.setBinarySearchOperation(IntArrayWeightedMeanCrossover.INT_ARRAY_WEIGHTED_MEAN_CROSSOVER);
		GA.setObjectiveFunction(config.getDelayCalc());
		GA.setNullarySearchOperation(create);
		GA.setSelectionAlgorithm(new TournamentSelection(2));
		GA.setUnarySearchOperation(mutate);
		run(GA);
		return GA.getTrace();
	}

	private DelayTrace optimizeRandomWalk() {
		RandomWalkTrace<int[], int[]> RW = new RandomWalkTrace<int[], int[]>();
		RW.setUpTrace(config);
		RW.setObjectiveFunction(config.getDelayCalc());
		RW.setNullarySearchOperation(create);
		RW.setUnarySearchOperation(mutate);
		run(RW);
		return RW.getTrace();
	}

	@SuppressWarnings("unchecked")
	private final int[] run(
			final ISOOptimizationAlgorithm<?, int[], ?> algorithm) {
		List<Individual<?, int[]>> solutions;
		Individual<?, int[]> individual = null;
		double bestValue = Double.MAX_VALUE;

		UbsDelayTermination term = new UbsDelayTermination(config);
		algorithm.setTerminationCriterion(term);
		
		for (int i = 0; i < config.getRuns(); i++) {
			algorithm.setRandSeed(i + System.currentTimeMillis());
			solutions = ((List<Individual<?, int[]>>) (algorithm.call()));
			if (solutions.get(0).v < bestValue) {
				individual = solutions.get(0);
			}
			if (term.foundDelay())
				return individual.x;
			term.reset();
		}
		return individual.x;
	}
}
