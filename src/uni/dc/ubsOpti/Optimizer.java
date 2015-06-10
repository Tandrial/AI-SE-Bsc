package uni.dc.ubsOpti;

import java.util.List;

import org.goataa.impl.algorithms.ea.TraceSimpleGenerationalEA;
import org.goataa.impl.algorithms.ea.selection.TournamentSelection;
import org.goataa.impl.algorithms.hc.TraceHillClimbing;
import org.goataa.impl.algorithms.sa.TraceSimulatedAnnealing;
import org.goataa.impl.algorithms.sa.temperatureSchedules.Logarithmic;
import org.goataa.impl.searchOperations.strings.integer.binary.IntArrayWeightedMeanCrossover;
import org.goataa.impl.searchOperations.strings.integer.nullary.IntArrayAllOnesCreation;
import org.goataa.impl.searchOperations.strings.integer.unary.IntArrayAllNormalMutation;
import org.goataa.impl.termination.UbsDelayTermination;
import org.goataa.impl.utils.Individual;
import org.goataa.spec.INullarySearchOperation;
import org.goataa.spec.ISOOptimizationAlgorithm;
import org.goataa.spec.IUnarySearchOperation;

import uni.dc.model.PriorityConfiguration;
import uni.dc.ubsOpti.DelayCalc.UbsDelayCalc;
import uni.dc.ubsOpti.Tracer.DelayTrace;

public class Optimizer {
	private INullarySearchOperation<int[]> create;
	private IUnarySearchOperation<int[]> mutate;
	private OptimizerConfig optiConfig;

	public DelayTrace optimize(OptimizerConfig optiConfig, String selectedAlgo) {
		this.optiConfig = optiConfig;
		UbsDelayCalc delayCalc = optiConfig.getDelayCalc();
		PriorityConfiguration config = optiConfig.getPriorityConfig();
		DelayTrace trace = null;
		int dim = config.toIntArray().length;
		create = new IntArrayAllOnesCreation(dim, 1, optiConfig.getMaxPrio());
		mutate = new IntArrayAllNormalMutation(1, optiConfig.getMaxPrio());

		if (selectedAlgo.equals("BruteForce")) {
			trace = optimizeBruteForce(config, delayCalc);
		} else if (selectedAlgo.equals("SimulatedAnnealing")) {
			trace = optimizeSimulatedAnnealing(delayCalc);
		} else if (selectedAlgo.equals("HillClimbing")) {
			trace = optimizeHillClimbing(delayCalc);
		} else if (selectedAlgo.equals("SimpleGenerationalEA")) {
			trace = optimizeSimpleGenerationalEA(delayCalc);
		}

		delayCalc.calculateDelays(trace.getBestConfig());
		System.out.println(trace.getBestConfig());

		delayCalc.printDelays();
		System.out.println("delays okay = " + delayCalc.checkDelays());
		return trace;
	}

	private DelayTrace optimizeBruteForce(PriorityConfiguration prio,
			UbsDelayCalc delayCalc) {
		BruteForce BF = new BruteForce(delayCalc);
		BF.setUpTrace(optiConfig);
		BF.optimize(prio, optiConfig.getMaxPrio());
		return BF.getTrace();
	}

	private DelayTrace optimizeHillClimbing(UbsDelayCalc delayCalc) {
		TraceHillClimbing<int[], int[]> HC = new TraceHillClimbing<int[], int[]>();
		HC.setUpTrace(optiConfig);
		HC.setObjectiveFunction(delayCalc);
		HC.setNullarySearchOperation(create);
		HC.setUnarySearchOperation(mutate);
		testRuns(HC);
		return HC.getTrace();
	}

	private DelayTrace optimizeSimulatedAnnealing(UbsDelayCalc delayCalc) {
		TraceSimulatedAnnealing<int[], int[]> SA = new TraceSimulatedAnnealing<int[], int[]>();
		SA.setUpTrace(optiConfig);
		SA.setObjectiveFunction(delayCalc);
		SA.setNullarySearchOperation(create);
		SA.setTemperatureSchedule(new Logarithmic(1d));
		SA.setUnarySearchOperation(mutate);
		testRuns(SA);
		return SA.getTrace();
	}

	private DelayTrace optimizeSimpleGenerationalEA(UbsDelayCalc delayCalc) {
		TraceSimpleGenerationalEA<int[], int[]> GA = new TraceSimpleGenerationalEA<int[], int[]>();
		GA.setUpTrace(optiConfig);
		GA.setBinarySearchOperation(IntArrayWeightedMeanCrossover.INT_ARRAY_WEIGHTED_MEAN_CROSSOVER);
		GA.setObjectiveFunction(delayCalc);
		GA.setNullarySearchOperation(create);
		GA.setSelectionAlgorithm(new TournamentSelection(2));
		GA.setUnarySearchOperation(mutate);
		testRuns(GA);
		return GA.getTrace();
	}

	@SuppressWarnings("unchecked")
	private final int[] testRuns(
			final ISOOptimizationAlgorithm<?, int[], ?> algorithm) {
		List<Individual<?, int[]>> solutions;
		Individual<?, int[]> individual = null;
		double bestValue = Double.MAX_VALUE;

		UbsDelayTermination term = new UbsDelayTermination(optiConfig);

		algorithm.setTerminationCriterion(term);
		for (int i = 0; i < optiConfig.getRuns(); i++) {
			algorithm.setRandSeed(i);
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
