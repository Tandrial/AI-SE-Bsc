package uni.dc.ubsOpti;

import java.util.Arrays;
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

import uni.dc.model.Flow;
import uni.dc.model.PriorityConfiguration;
import uni.dc.ubsOpti.DelayCalc.UbsDelayCalc;
import uni.dc.util.NetworkParser;
import uni.dc.util.OptimizerConfig;

public class Optimizer {
	private INullarySearchOperation<int[]> create;
	private IUnarySearchOperation<int[]> mutate;

	private OptimizerConfig optiConfig;
	private int dim;

	public PriorityConfiguration optimize(OptimizerConfig optiConfig,
			String selectedAlgo) {
		this.optiConfig = optiConfig;
		PriorityConfiguration config = optiConfig.getParser()
				.getPriorityConfig();
		int[] prio = config.toIntArray();
		int[] bestPrio = prio;
		dim = config.toIntArray().length;
		create = new IntArrayAllOnesCreation(dim, 1, optiConfig.getMaxPrio());
		mutate = new IntArrayAllNormalMutation(1, optiConfig.getMaxPrio());
		boolean firstRun = true;
		NetworkParser parser = optiConfig.getParser();
		UbsDelayCalc delayCalc = optiConfig.getDelayCalc();

		while (firstRun || delayCalc.checkDelays()) {
			firstRun = false;
			bestPrio = Arrays.copyOf(prio, prio.length);
			for (Flow f : parser.getTraffic()) {
				if (optiConfig.getFlows().contains(f)) {
					f.speedUp(optiConfig.getSpeedFactor());
				} else {
					f.slowDown(2 * optiConfig.getSpeedFactor());
				}
			}
			if (selectedAlgo.equals("BruteForce")) {
				prio = Arrays
						.copyOf(optimizeBruteForce(parser, delayCalc), dim);
			} else if (selectedAlgo.equals("SimulatedAnnealing")) {
				prio = Arrays.copyOf(
						optimizeSimulatedAnnealing(parser, delayCalc), dim);
			} else if (selectedAlgo.equals("HillClimbing")) {
				prio = Arrays.copyOf(optimizeHillClimbing(parser, delayCalc),
						dim);
			} else if (selectedAlgo.equals("RandomWalks")) {
				prio = Arrays.copyOf(optimizeRandomWalks(parser, delayCalc),
						dim);
			} else if (selectedAlgo.equals("SimpleGenerationalEA")) {
				prio = Arrays.copyOf(
						optimizeSimpleGenerationalEA(parser, delayCalc), dim);
			}

			config.fromIntArray(prio);
			delayCalc.calculateDelays(config);
		}
		for (Flow f : parser.getTraffic()) {
			if (optiConfig.getFlows().contains(f)) {
				f.slowDown(optiConfig.getSpeedFactor());
			} else {
				f.speedUp(2 * optiConfig.getSpeedFactor());
			}
		}
		config.fromIntArray(bestPrio);
		System.out.println(config);
		delayCalc.calculateDelays(config);
		delayCalc.printDelays();
		System.out.println("delays okay = " + delayCalc.checkDelays());
		return (PriorityConfiguration) config.clone();
	}

	private int[] optimizeBruteForce(NetworkParser parser,
			UbsDelayCalc delayCalc) {
		BruteForce BF = new BruteForce(delayCalc);
		return BF.optimize(parser.getPriorityConfig(), optiConfig.getMaxPrio());
	}

	private int[] optimizeHillClimbing(NetworkParser parser,
			UbsDelayCalc delayCalc) {
		HillClimbing<int[], int[]> HC = new HillClimbing<int[], int[]>();
		HC.setObjectiveFunction(delayCalc);
		HC.setNullarySearchOperation(create);
		HC.setUnarySearchOperation(mutate);
		return testRuns(HC);
	}

	private int[] optimizeSimulatedAnnealing(NetworkParser parser,
			UbsDelayCalc delayCalc) {
		SimulatedAnnealing<int[], int[]> SA = new SimulatedAnnealing<int[], int[]>();
		SA.setObjectiveFunction(delayCalc);
		SA.setNullarySearchOperation(create);
		SA.setTemperatureSchedule(new Logarithmic(1d));
		SA.setUnarySearchOperation(mutate);
		return testRuns(SA);
	}

	private int[] optimizeSimpleGenerationalEA(NetworkParser parser,
			UbsDelayCalc delayCalc) {
		SimpleGenerationalEA<int[], int[]> GA = new SimpleGenerationalEA<int[], int[]>();
		GA.setBinarySearchOperation(IntArrayWeightedMeanCrossover.INT_ARRAY_WEIGHTED_MEAN_CROSSOVER);
		GA.setObjectiveFunction(delayCalc);
		GA.setNullarySearchOperation(create);
		GA.setSelectionAlgorithm(new TournamentSelection(2));
		GA.setUnarySearchOperation(mutate);
		return testRuns(GA);
	}

	private int[] optimizeRandomWalks(NetworkParser parser,
			UbsDelayCalc delayCalc) {
		RandomWalk<int[], int[]> RW = new RandomWalk<int[], int[]>();
		RW.setObjectiveFunction(delayCalc);
		RW.setNullarySearchOperation(create);
		RW.setUnarySearchOperation(mutate);
		return testRuns(RW);
	}

	@SuppressWarnings("unchecked")
	private final int[] testRuns(
			final ISOOptimizationAlgorithm<?, int[], ?> algorithm) {
		BufferedStatistics stat;
		List<Individual<?, int[]>> solutions;
		Individual<?, int[]> individual = null;
		double bestValue = Double.MAX_VALUE;

		stat = new BufferedStatistics();
		algorithm.setTerminationCriterion(new StepLimit(optiConfig
				.getMaxSteps()));
		for (int i = 0; i < optiConfig.getRuns(); i++) {
			algorithm.setRandSeed(i);
			solutions = ((List<Individual<?, int[]>>) (algorithm.call()));
			if (solutions.get(0).v < bestValue) {
				individual = solutions.get(0);
				stat.add(individual.v);
			}
		}
		return individual.x;
	}
}
