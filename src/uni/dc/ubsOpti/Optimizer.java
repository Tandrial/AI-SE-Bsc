package uni.dc.ubsOpti;

import java.util.List;

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
import org.goataa.spec.IUnarySearchOperation;

import uni.dc.model.PriorityConfiguration;
import uni.dc.ubsOpti.DelayCalc.UbsDelayCalc;
import uni.dc.util.NetworkParser;

public class Optimizer {

	private UbsDelayCalc delayCalc;

	private INullarySearchOperation<int[]> create;
	private IUnarySearchOperation<int[]> mutate;

	private int maxPrio;
	private int dim;
	private int maxSteps = 10000;
	private int runs = 20;

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

	public PriorityConfiguration optimize(NetworkParser parser,
			UbsDelayCalc delayCalc, String selectedAlgo) {
		this.delayCalc = delayCalc;
		PriorityConfiguration config = parser.resetPriorityConfig();
		maxPrio = 2;
		dim = config.toIntArray().length;
		create = new IntArrayAllOnesCreation(dim, 1, maxPrio);
		mutate = new IntArrayAllNormalMutation(1, maxPrio);

		if (selectedAlgo.equals("Brute Force")) {
			config = optimizeBruteForce(parser);
		} else if (selectedAlgo.equals("Simulated Annealing")) {
			config.fromIntArray(optimizeSimulatedAnnealing(parser));
		} else if (selectedAlgo.equals("HillClimbing")) {
			config.fromIntArray(optimizeHillClimbing(parser));
		} else if (selectedAlgo.equals("Random Walks")) {
			config.fromIntArray(optimizeRandomWalks(parser));
		} else if (selectedAlgo.equals("SimpleGenerationalEA")) {
			config.fromIntArray(optimizeSimpleGenerationalEA(parser));
		}

		System.out.println(config);
		delayCalc.calculateDelays(config);
		delayCalc.printDelays();
		System.out.println("delays okay = " + delayCalc.checkDelays());
		return (PriorityConfiguration) config.clone();
	}

	public PriorityConfiguration optimizeBruteForce(NetworkParser parser) {
		BruteForce BF = new BruteForce(parser.getTraffic().getPortFlowMap());
		BF.optimize(parser.getPriorityConfig(), maxPrio);
		return BF.getBestConfig();
	}

	public int[] optimizeHillClimbing(NetworkParser parser) {
		HillClimbing<int[], int[]> HC = new HillClimbing<int[], int[]>();
		HC.setObjectiveFunction(delayCalc);
		HC.setNullarySearchOperation(create);
		HC.setUnarySearchOperation(mutate);
		return testRuns(HC, runs, maxSteps);
	}

	public int[] optimizeSimulatedAnnealing(NetworkParser parser) {
		SimulatedAnnealing<int[], int[]> SA = new SimulatedAnnealing<int[], int[]>();
		SA.setObjectiveFunction(delayCalc);
		SA.setNullarySearchOperation(create);
		SA.setTemperatureSchedule(new Logarithmic(1d));
		SA.setUnarySearchOperation(mutate);
		return testRuns(SA, runs, maxSteps);
	}

	public int[] optimizeSimpleGenerationalEA(NetworkParser parser) {
		SimpleGenerationalEA<int[], int[]> GA = new SimpleGenerationalEA<int[], int[]>();
		GA.setBinarySearchOperation(IntArrayWeightedMeanCrossover.INT_ARRAY_WEIGHTED_MEAN_CROSSOVER);
		GA.setObjectiveFunction(delayCalc);
		GA.setNullarySearchOperation(create);
		GA.setSelectionAlgorithm(new TournamentSelection(2));
		GA.setUnarySearchOperation(mutate);
		return testRuns(GA, runs, maxSteps);
	}

	public int[] optimizeRandomWalks(NetworkParser parser) {
		RandomWalk<int[], int[]> RW = new RandomWalk<int[], int[]>();
		RW.setObjectiveFunction(delayCalc);
		RW.setNullarySearchOperation(create);
		RW.setUnarySearchOperation(mutate);
		return testRuns(RW, runs, maxSteps);
	}
}
