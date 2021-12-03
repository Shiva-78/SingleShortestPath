package com.Algo.SingleShortestPath.bellman_ford;

import com.google.common.graph.EndpointPair;
import com.google.common.graph.ValueGraph;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bellman_Ford {

  public static <N> List<N> findPath(ValueGraph<N, Integer> graph, N source, N target) {
    Map<N, NodeWrapper<N>> nodeWrappers = new HashMap<>();
    for (N node : graph.nodes()) {
      int cost = node.equals(source) ? 0 : Integer.MAX_VALUE;
      NodeWrapper<N> nodeWrapper = new NodeWrapper<>(node, cost, null);
      nodeWrappers.put(node, nodeWrapper);
    }
    int n = graph.nodes().size();
    for (int i = 0; i < n; i++) {
      boolean lastIteration = i == n - 1;

      boolean atLeastOneChange = false;

      for (EndpointPair<N> edge : graph.edges()) {
        NodeWrapper<N> edgeWrapper = nodeWrappers.get(edge.source());
        int totalCost = edgeWrapper.getTotalCostFromStart();
        if (totalCost == Integer.MAX_VALUE) continue;
        int cost = graph.edgeValue(edge).orElseThrow(IllegalStateException::new);
        int totalCostToEdgeTarget = totalCost + cost;
        NodeWrapper edgeTargetWrapper = nodeWrappers.get(edge.target());
        if (totalCostToEdgeTarget < edgeTargetWrapper.getTotalCostFromStart()) {
          if (lastIteration) {
            throw new IllegalArgumentException("Negative cycle detected");
          }

          edgeTargetWrapper.setTotalCostFromStart(totalCostToEdgeTarget);
          edgeTargetWrapper.setPredecessor(edgeWrapper);
          atLeastOneChange = true;
        }
      }

      // Optimization: terminate if nothing was changed
      if (!atLeastOneChange) break;
    }

    // Path found?
    NodeWrapper<N> targetWrapper = nodeWrappers.get(target);
    if (targetWrapper.getPredecessor() != null) {
      return createPath(targetWrapper);
    } else {
      return null;
    }
  }

  private static <N> List<N> createPath(NodeWrapper<N> nodeWrapper) {
    List<N> path = new ArrayList<>();
    while (nodeWrapper != null) {
      path.add(nodeWrapper.getNode());
      nodeWrapper = nodeWrapper.getPredecessor();
    }
    Collections.reverse(path);
    return path;
  }
}
