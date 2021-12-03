package com.Algo.SingleShortestPath.dijkstra;

import com.google.common.graph.ValueGraph;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;


public class Dijkstra {


  public static <N> List<N> findShortestPath(ValueGraph<N, Integer> graph, N source, N target) {
    Map<N, NodeWrapper<N>> nodeWrappers = new HashMap<>();
    PriorityQueue<NodeWrapper<N>> queue = new PriorityQueue<>();
    Set<N> pathFound = new HashSet<>();

    // Add source to queue
    NodeWrapper<N> sourceWrapper = new NodeWrapper<>(source, 0, null);
    nodeWrappers.put(source, sourceWrapper);
    queue.add(sourceWrapper);

    while (!queue.isEmpty()) {
      NodeWrapper<N> nodeWrapper = queue.poll();
      N node = nodeWrapper.getNode();
      pathFound.add(node);
      if (node.equals(target)) {
        return createPath(nodeWrapper);
      }

      Set<N> adjacentNodes = graph.adjacentNodes(node);
      for (N adjacentNode : adjacentNodes) {
        if (pathFound.contains(adjacentNode)) {
          continue;
        }

        int distance = graph.edgeValue(node, adjacentNode).orElseThrow(IllegalStateException::new);
        int totalDistance = nodeWrapper.getTotalDistance() + distance;

        NodeWrapper<N> adjacentNodeWrapper = nodeWrappers.get(adjacentNode);
        if (adjacentNodeWrapper == null) {
          adjacentNodeWrapper = new NodeWrapper<>(adjacentNode, totalDistance, nodeWrapper);
          nodeWrappers.put(adjacentNode, adjacentNodeWrapper);
          queue.add(adjacentNodeWrapper);
        }

        else if (totalDistance < adjacentNodeWrapper.getTotalDistance()) {
          adjacentNodeWrapper.setTotalDistance(totalDistance);
          adjacentNodeWrapper.setPredecessor(nodeWrapper);


          queue.remove(adjacentNodeWrapper);
          queue.add(adjacentNodeWrapper);
        }
      }
    }

    return null;
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
