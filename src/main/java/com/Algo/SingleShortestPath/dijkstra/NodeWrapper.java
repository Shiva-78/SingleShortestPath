package com.Algo.SingleShortestPath.dijkstra;

class NodeWrapper<N> implements Comparable<NodeWrapper<N>> {
  private final N node;
  private int distance;
  private NodeWrapper<N> predecessor;

  NodeWrapper(N node, int distance, NodeWrapper<N> predecessor) {
    this.node = node;
    this.distance = distance;
    this.predecessor = predecessor;
  }

  N getNode() {
    return node;
  }

  void setTotalDistance(int distance) {
    this.distance = distance;
  }

  public int getTotalDistance() {
    return distance;
  }

  public void setPredecessor(NodeWrapper<N> predecessor) {
    this.predecessor = predecessor;
  }

  public NodeWrapper<N> getPredecessor() {
    return predecessor;
  }

  @Override
  public int compareTo(NodeWrapper<N> other) {
    return Integer.compare(this.distance, other.distance);
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
