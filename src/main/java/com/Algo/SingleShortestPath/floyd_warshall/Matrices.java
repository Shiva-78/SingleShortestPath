package com.Algo.SingleShortestPath.floyd_warshall;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Matrices {

  private final int n;
  private final String[] nodes;
  private final Map<String, Integer> mappingNodeIndex;

  final int[][] costs;
  final int[][] successors;

  
  public Matrices(String[] nodes) {
    this.n = nodes.length;
    this.nodes = nodes;

    Map<String, Integer> temp = new HashMap<>();
    for (int i = 0; i < n; i++) {
      temp.put(nodes[i], i);
    }
    this.mappingNodeIndex = Collections.unmodifiableMap(temp);

    this.costs = new int[n][n];
    this.successors = new int[n][n];
  }

 
  public int getCost(String source, String target) {
    return costs[mappingNodeIndex.get(source)][mappingNodeIndex.get(target)];
  }


  public Optional<List<String>> getPath(String source, String target) {
    int i = mappingNodeIndex.get(source);
    int j = mappingNodeIndex.get(target);

    // Check for -1 in case there's no path from source to dest
    if (successors[i][j] == -1) {
      return Optional.empty();
    }

    List<String> path = new ArrayList<>();
    path.add(nodes[i]);

    while (i != j) {
      i = successors[i][j];
      path.add(nodes[i]);
    }

    return Optional.of(List.copyOf(path));
  }

  /** Prints the cost and successor matrices to the console. */
  public void print() {
    printCosts();
    printSuccessors();
  }

  private void printCosts() {
    System.out.println("Costs:");

    printHeader();

    for (int rowNo = 0; rowNo < n; rowNo++) {
      System.out.printf("%5s", nodes[rowNo]);

      for (int colNo = 0; colNo < n; colNo++) {
        int cost = costs[rowNo][colNo];
        if (cost == Integer.MAX_VALUE) System.out.print("    ∞");
        else System.out.printf("%5d", cost);
      }
      System.out.println();
    }
  }

  private void printSuccessors() {
    System.out.println("Successors:");

    printHeader();

    for (int rowNo = 0; rowNo < n; rowNo++) {
      System.out.printf("%5s", nodes[rowNo]);

      for (int colNo = 0; colNo < n; colNo++) {
        int successor = successors[rowNo][colNo];
        String nextNode = successor != -1 ? nodes[successor] : "-";
        System.out.printf("%5s", nextNode);
      }
      System.out.println();
    }
  }

  private void printHeader() {
    System.out.print("     ");
    for (int colNo = 0; colNo < n; colNo++) {
      System.out.printf("%5s", nodes[colNo]);
    }
    System.out.println();
  }
}
