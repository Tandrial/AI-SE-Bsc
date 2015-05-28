package uni.dc.ubsOpti;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.goataa.impl.algorithms.hc.HillClimbing;
import org.goataa.impl.searchOperations.strings.integer.nullary.IntArrayAllZerosCreation;
import org.goataa.impl.searchOperations.strings.integer.unary.IntArrayAllNormalMutation;
import org.goataa.impl.termination.StepLimit;
import org.goataa.impl.utils.BufferedStatistics;
import org.goataa.impl.utils.Individual;
import org.goataa.spec.INullarySearchOperation;
import org.goataa.spec.ISOOptimizationAlgorithm;
import org.goataa.spec.IUnarySearchOperation;

import uni.dc.model.EgressPort;
import uni.dc.model.Flow;
import uni.dc.model.PriorityConfiguration;
import uni.dc.model.Traffic;
import uni.dc.networkGenerator.GeneratorAPI;
import uni.dc.ubsOpti.DelayCalc.UbsV0DelayCalc;

public class Optimizer {
	public static void main(String[] args) {
		int depth = 2;
		int portCount = 4;

		GeneratorAPI.generateNetwork(depth, portCount);

		Traffic network = GeneratorAPI.getTraffic();
		Map<EgressPort, Set<Flow>> flowMap = network.getPortFlowMap();
		PriorityConfiguration prio = GeneratorAPI.getPriorityConfiguration();
		// GeneratorAPI.printGeneratedNetwork();

		UbsV0DelayCalc delays = new UbsV0DelayCalc(flowMap);
		// delays.printDelays();
		delays.calculateDelays(prio);
		delays.printDelays();
		System.out.println("delays okay = " + delays.checkDelays());
		System.out.println(prio);
		System.out.println("starting brute Force");
		BruteForce BF = new BruteForce(flowMap);
		BF.optimize(prio, 2);

		System.out.println(BF.getBestConfig());
		
//		HillClimbing<int[], int[]> HC = new HillClimbing<int[], int[]>();
//
//		int dim = prio.toIntArray().length;
//		int maxSteps = 10;
//		int runs = 10;
//
//		INullarySearchOperation<int[]> create = new IntArrayAllZerosCreation(
//				dim, 0, 5);
//		IUnarySearchOperation<int[]> mutate = new IntArrayAllNormalMutation(0,
//				5);
//
//		HC.setObjectiveFunction(delays);
//		HC.setNullarySearchOperation(create);
//		HC.setUnarySearchOperation(mutate);
//		testRuns(HC, runs, maxSteps);

	}

	@SuppressWarnings("unchecked")
	private static final void testRuns(
			final ISOOptimizationAlgorithm<?, int[], ?> algorithm,
			final int runs, final int steps) {
		int i;
		BufferedStatistics stat;
		List<Individual<?, int[]>> solutions;
		Individual<?, int[]> individual;

		stat = new BufferedStatistics();
		algorithm.setTerminationCriterion(new StepLimit(steps));
		System.out.println("starting HillClimbing!");
		for (i = 0; i < runs; i++) {
			System.out.println("run = " + i);
			algorithm.setRandSeed(i);
			solutions = ((List<Individual<?, int[]>>) (algorithm.call()));
			individual = solutions.get(0);
			stat.add(individual.v);
			System.out.println(individual);
		}
		System.out.println(stat.getConfiguration(false) + ' '
				+ algorithm.toString(false));
	}
}
