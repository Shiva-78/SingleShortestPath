package com.Algo.SingleShortestPath.floyd_warshall;

import com.google.common.graph.ValueGraph;
import java.util.Optional;

public class Floyd_Warshall {

  public static Matrices findPaths(
      ValueGraph<String, Integer> graph, boolean printDebugOutput) {
    int n = graph.nodes().size();
    String[] nodes = graph.nodes().toArray(new String[n]);

    Matrices m = new Matrices(nodes);

    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        Optional<Integer> edgeValue = graph.edgeValue(nodes[i], nodes[j]);
        m.costs[i][j] = i == j ? 0 : edgeValue.orElse(Integer.MAX_VALUE);
        m.successors[i][j] = edgeValue.isPresent() ? j : -1;
      }
    }

    if (printDebugOutput) {
      System.out.println("\nMatrix after preparation:");
      m.print();
    }

    for (int k = 0; k < n; k++) {
      for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
          int costViaNodeK = addCosts(m.costs[i][k], m.costs[k][j]);
          if (costViaNodeK < m.costs[i][j]) {
            m.costs[i][j] = costViaNodeK;
            m.successors[i][j] = m.successors[i][k];
          }
        }
      }

      if (printDebugOutput) {
        System.out.println("\nMatrices after k = " + k + ":");
        m.print();
      }
    }

    for (int i = 0; i < n; i++) {
      if (m.costs[i][i] < 0) {
        throw new IllegalArgumentException("Graph has a negative cycle");
      }
    }

    return m;
  }

  private static int addCosts(int a, int b) {
    if (a == Integer.MAX_VALUE || b == Integer.MAX_VALUE) {
      return Integer.MAX_VALUE;
    }
    return a + b;
  }
}
