package uni.dc.ubsOpti;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.goataa.impl.algorithms.RandomWalk;
import org.goataa.impl.algorithms.ea.SimpleGenerationalEA;
import org.goataa.impl.algorithms.ea.selection.TournamentSelection;
import org.goataa.impl.algorithms.hc.HillClimbing;
import org.goataa.impl.algorithms.sa.SimulatedAnnealing;
import org.goataa.impl.algorithms.sa.temperatureSchedules.Logarithmic;
import org.goataa.impl.searchOperations.strings.integer.binary.IntArrayWeightedMeanCrossover;
import org.goataa.impl.searchOperations.strings.integer.nullary.IntArrayAllOnesCreation;
import org.goataa.impl.searchOperations.strings.integer.unary.IntArrayAllNormalMutation;
import org.goataa.impl.termination.StepLimit;
import org.goataa.impl.utils.BufferedStatistics;
import org.goataa.impl.utils.Individual;
import org.goataa.spec.INullarySearchOperation;
import org.goataa.spec.ISOOptimizationAlgorithm;
import org.goataa.spec.ISelectionAlgorithm;
import org.goataa.spec.ITemperatureSchedule;
import org.goataa.spec.IUnarySearchOperation;

import uni.dc.model.EgressPort;
import uni.dc.model.Flow;
import uni.dc.model.PriorityConfiguration;
import uni.dc.model.Traffic;
import uni.dc.ubsOpti.DelayCalc.UbsV0DelayCalc;
import uni.dc.util.NetworkParser;

public class Optimizer {

	private UbsV0DelayCalc delayCalc;

	public static void main(String[] args) {
		PriorityConfiguration prio;
		int depth = 4;
		int portCount = 6;
		Map<EgressPort, Set<Flow>> flowMap;

		// GeneratorAPI.generateNetwork(depth, portCount);
		// Traffic network = GeneratorAPI.getTraffic();
		// flowMap = network.getPortFlowMap();
		// prio = GeneratorAPI.getPriorityConfiguration();
		// GeneratorAPI.printGeneratedNetwork();

		NetworkParser parser = new NetworkParser(new File(
				"./Topologies/Linear.json"));
		Traffic traffic = parser.getTraffic();
		flowMap = traffic.getPortFlowMap();
		prio = parser.getPriorityConfig();

		UbsV0DelayCalc delays = new UbsV0DelayCalc(flowMap);
		// delays.printDelays();
		delays.calculateDelays(prio);
		delays.printDelays();
		System.out.println("delays okay = " + delays.checkDelays());
		System.out.println(prio);

		BruteForce BF = new BruteForce(flowMap);
		HillClimbing<int[], int[]> HC = new HillClimbing<int[], int[]>();
		SimulatedAnnealing<int[], int[]> SA = new SimulatedAnnealing<int[], int[]>();
		SimpleGenerationalEA<int[], int[]> GA = new SimpleGenerationalEA<int[], int[]>();
		RandomWalk<int[], int[]> RW = new RandomWalk<int[], int[]>();

		int dim = prio.toIntArray().length;
		int maxSteps = 10000;
		int runs = 20;
		int maxPrio = 2;

		System.out.println("================================================");
		// Brute Force
		BF.optimize(prio, maxPrio);
		System.out.println(BF.getBestConfig());
		delays.calculateDelays(BF.getBestConfig());
		delays.printDelays();
		System.out.println("delays okay = " + delays.checkDelays());

		maxPrio = 5;
		INullarySearchOperation<int[]> create = new IntArrayAllOnesCreation(
				dim, 1, maxPrio);
		IUnarySearchOperation<int[]> mutate = new IntArrayAllNormalMutation(1,
				maxPrio);

		GA.setBinarySearchOperation(IntArrayWeightedMeanCrossover.INT_ARRAY_WEIGHTED_MEAN_CROSSOVER);
		ISelectionAlgorithm sel = new TournamentSelection(2);

		System.out.println("================================================");
		// Generational Genetic/Evolutionary Algorithm
		// (Chapter 28, Section 28.1.4.1, Section 29.3)
		GA.setObjectiveFunction(delays);
		GA.setNullarySearchOperation(create);
		GA.setSelectionAlgorithm(sel);
		GA.setUnarySearchOperation(mutate);
		testRuns(GA, runs, maxSteps);

		System.out.println("================================================");
		// Random Walks (Section 8.2)
		RW.setObjectiveFunction(delays);
		RW.setNullarySearchOperation(create);
		RW.setUnarySearchOperation(mutate);
		testRuns(RW, runs, maxSteps);
		RW.getConfiguration(true);
	}

	@SuppressWarnings("unchecked")
	private static final int[] testRuns(
			final ISOOptimizationAlgorithm<?, int[], ?> algorithm,
			final int runs, final int steps) {

		BufferedStatistics stat;
		List<Individual<?, int[]>> solutions;
		Individual<?, int[]> individual = null;
		double bestValue = Double.MAX_VALUE;

		stat = new BufferedStatistics();
		algorithm.setTerminationCriterion(new StepLimit(steps));
		for (int i = 0; i < runs; i++) {
			algorithm.setRandSeed(i);
			solutions = ((List<Individual<?, int[]>>) (algorithm.call()));
			if (solutions.get(0).v < bestValue) {
				individual = solutions.get(0);
				stat.add(individual.v);
				System.out.println("found better solution " + individual.v);
			}
		}
		return individual.x;

	}

	public void optimize(NetworkParser parser, String selectedAlgo) {
		PriorityConfiguration config = (PriorityConfiguration) parser
				.getPriorityConfig();
		delayCalc = new UbsV0DelayCalc(parser.getTraffic().getPortFlowMap());
		if (selectedAlgo.equals("Brute Force")) {
			config = optimizeBruteForce(parser, 2);
		} else if (selectedAlgo.equals("SimulatedAnnealing")) {
			config.fromIntArray(optimizeSimulatedAnnealing(parser, 2, parser
					.getPriorityConfig().toIntArray().length, 100, 10000));
		} else if (selectedAlgo.equals("HillClimbing")) {
			config.fromIntArray(optimizeHillClimbing(parser, 2, parser
					.getPriorityConfig().toIntArray().length, 100, 10000));
		}
		System.out.println(config);
		delayCalc.calculateDelays(config);
		delayCalc.printDelays();
		System.out.println("delays okay = " + delayCalc.checkDelays());
	}

	public PriorityConfiguration optimizeBruteForce(NetworkParser parser,
			int maxPrio) {
		BruteForce BF = new BruteForce(parser.getTraffic().getPortFlowMap());
		BF.optimize(parser.getPriorityConfig(), maxPrio);
		return BF.getBestConfig();
	}

	public int[] optimizeHillClimbing(NetworkParser parser, int maxPrio,
			int dim, int runs, int maxSteps) {
		INullarySearchOperation<int[]> create = new IntArrayAllOnesCreation(
				dim, 1, maxPrio);
		IUnarySearchOperation<int[]> mutate = new IntArrayAllNormalMutation(1,
				maxPrio);
		HillClimbing<int[], int[]> HC = new HillClimbing<int[], int[]>();
		HC.setObjectiveFunction(delayCalc);
		HC.setNullarySearchOperation(create);
		HC.setUnarySearchOperation(mutate);
		return testRuns(HC, runs, maxSteps);
	}

	public int[] optimizeSimulatedAnnealing(NetworkParser parser, int maxPrio,
			int dim, int runs, int maxSteps) {
		INullarySearchOperation<int[]> create = new IntArrayAllOnesCreation(
				dim, 1, maxPrio);
		IUnarySearchOperation<int[]> mutate = new IntArrayAllNormalMutation(1,
				maxPrio);
		ITemperatureSchedule schedule = new Logarithmic(1d);
		SimulatedAnnealing<int[], int[]> SA = new SimulatedAnnealing<int[], int[]>();
		SA.setObjectiveFunction(delayCalc);
		SA.setNullarySearchOperation(create);
		SA.setTemperatureSchedule(schedule);
		SA.setUnarySearchOperation(mutate);
		return testRuns(SA, runs, maxSteps);
	}
}
