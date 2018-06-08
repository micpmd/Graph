package graph.impl;

public class Edge implements Comparable<Edge> {

	private int weight;
	private String source;
	private String destination;

	public Edge(int w, String s, String d) {
		this.weight = w;
		this.source = s;
		this.destination = d;
	}

	public int getWeight() {
		return this.weight;
	}

	public String getSource() {
		return this.source;
	}

	public String getDestination() {
		return this.destination;
	}

	@Override
	public int compareTo(Edge e) {
		return this.weight - e.getWeight();
	}

	@Override
	public String toString() {
		return "Weight: " + this.weight + " Source: " + this.source	+ " Destination: " + this.destination;
	}
}
