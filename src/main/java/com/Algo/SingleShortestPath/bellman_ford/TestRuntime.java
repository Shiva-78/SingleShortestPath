package com.Algo.SingleShortestPath.bellman_ford;

import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import com.Algo.SingleShortestPath.common.Stats;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class TestRuntime {
	private static final int MinNodes = 88;
	private static final int MaxNodes = 160_000;

	private static final int EdgesPerNode = 4;

	private static final int MaxWarmups = 1;
	private static final int MaxIterations = 50;

	private static final Map<Integer, List<Long>> TIMES = new HashMap<>();

	private static int blackhole = 0;

	public static void main(String[] args) {
		for (int i = 0; i < MaxWarmups; i++) {
			runTest(i, true);
		}
		for (int i = 0; i < MaxIterations; i++) {
			runTest(i, false);
		}
	}

	private static void runTest(int iteration, boolean warmup) {
		System.out.printf("%n%sIteration %d:%n", warmup ? "Warmup - " : "Test - ", iteration + 1);
		for (int numOfNodes = MinNodes; numOfNodes <= MaxNodes; numOfNodes = Math.min((int) (numOfNodes * 1.5),
				numOfNodes + 25_000)) {
			int numOfEdges = numOfNodes * EdgesPerNode;
			long time = runTestForGraph(numOfNodes, numOfEdges);
			System.out.printf(Locale.US, "Time for graph with %,7d nodes and %,9d edges = %,8.1f ms", numOfNodes,
					numOfEdges, time / 1_000_000.0);

			if (!warmup) {
				List<Long> times = TIMES.computeIfAbsent(numOfNodes, k -> new ArrayList<>());
				times.add(time);
				long median = Stats.median(times);
				System.out.printf(Locale.US, "  -->  Median after %2d iterations = %,8.1f ms", iteration + 1,
						median / 1_000_000.0);
			}
			System.out.println();
		}
		System.out.println("blackhole = " + blackhole);
	}

	private static long runTestForGraph(int numOfNodes, int numOfEdges) {
		List<String> shortestPath = null;
		long time = 0;
		while (shortestPath == null) {
			ValueGraph<String, Integer> graph = createGraph(numOfNodes, numOfEdges);
			String source = nodeName(0);
			String target = nodeName(numOfNodes - 1);

			time = System.nanoTime();
			try {
				shortestPath = Bellman_Ford.findPath(graph, source, target);
			} catch (IllegalArgumentException ex) {
				System.out.println("*** Negative cycle detected - repeating test ***");
			}
			time = System.nanoTime() - time;
		}
		blackhole += shortestPath.size();
		return time;
	}

	private static ValueGraph<String, Integer> createGraph(int numOfNodes, int numOfEdges) {
		MutableValueGraph<String, Integer> graph = ValueGraphBuilder.directed().build();

		for (int i = 0; i < numOfNodes; i++) {
			graph.addNode(nodeName(i));
		}

		ThreadLocalRandom random = ThreadLocalRandom.current();
		for (int i = 0; i < numOfEdges; i++) {
			String node1;
			String node2;
			do {
				node1 = nodeName(random.nextInt(numOfNodes));
				node2 = nodeName(random.nextInt(numOfNodes));
			} while (node1.equals(node2) || graph.hasEdgeConnecting(node1, node2));
			boolean negativeWeight = random.nextDouble() < 0.02;
			int weight = negativeWeight ? random.nextInt(-10, 0) : random.nextInt(0, 100);

			graph.putEdgeValue(node1, node2, weight);
		}
		return graph;
	}

	private static String nodeName(int i) {
		return "Node" + i;
	}
}
