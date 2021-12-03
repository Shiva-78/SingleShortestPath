package com.Algo.SingleShortestPath.bellman_ford;

class NodeWrapper<N> {
  private final N node;
  private int Cost;
  private NodeWrapper<N> predecessor;

  NodeWrapper(N node, int Cost, NodeWrapper<N> predecessor) {
    this.node = node;
    this.Cost = Cost;
    this.predecessor = predecessor;
  }

  N getNode() {
    return node;
  }

  void setTotalCostFromStart(int Cost) {
    this.Cost = Cost;
  }

  public int getTotalCostFromStart() {
    return Cost;
  }

  public void setPredecessor(NodeWrapper<N> predecessor) {
    this.predecessor = predecessor;
  }

  public NodeWrapper<N> getPredecessor() {
    return predecessor;
  }


  @Override
  public boolean equals(Object other) {
    return super.equals(other);
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }
}
