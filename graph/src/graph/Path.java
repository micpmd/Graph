package graph;

import java.util.*;

public class Path implements Comparable<Path> {

	private int cost;
	private String destination;

	public Path(String destination, int cost) {
		this.cost = cost;
		this.destination = destination;
	}

	public int getCost() {
		return this.cost;
	}

	public String getDestination() {
		return this.destination;
	}

//	@Override
//	public boolean equals(Path p) {
//		if (this.cost == p.getCost()) {
//			return true;
//		}
//		return false;
//	}
	
	@Override
    public int compareTo(Path p) {
        return this.cost - p.getCost();
    }
}
