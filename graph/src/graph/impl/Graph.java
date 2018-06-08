package graph.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import graph.IGraph;
import graph.INode;
import graph.NodeVisitor;
import graph.Path;
import junit.VERSION;

import java.util.*;

/**
 * A basic representation of a graph that can perform BFS, DFS, Dijkstra, and
 * Prim-Jarnik's algorithm for a minimum spanning tree.
 * 
 * @author jspacco
 *
 */
public class Graph implements IGraph {
	private Map<String, INode> nodes = new HashMap<String, INode>();

	// private constructor to force use of the static factory pattern
	private Graph() {
	}

	/**
	 * Static factory to create a graph. This method can return different
	 * classes that implement the IGraph interface based on the version number
	 * set in a different class. This uses Reflection in Java, a way to inspect
	 * the code that is currently being executed.
	 *
	 * @return
	 */
	public static IGraph createGraph() {
		// static factory lets us load many different versinos of the code
		int version = VERSION.version;
		if (version == 0) {
			// version 0 uses the private constructor defined here, not one of
			// the broken versions
			return new Graph();
		} else if (version == -1) {
			// load Spacco's solution code (not available for students)
			// all other versions have a public constructor
			try {
				Class<?> clazz = Class.forName("graph.sol.Graph");
				return (IGraph) clazz.newInstance();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} else {
			try {
				Class<?> clazz = Class.forName(String.format(
						"graph.broken%d.Graph", version));
				return (IGraph) clazz.newInstance();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * Return the {@link Node} with the given name.
	 * 
	 * If no {@link Node} with the given name exists, create a new node with the
	 * given name and return it. Subsequent calls to this method with the same
	 * name should then return the node just created.
	 * 
	 * @param name
	 * @return
	 */
	public INode getOrCreateNode(String name) {
		// Use a map to ensure that we don't create more than one node with the
		// same name
		if (!nodes.containsKey(name)) {
			nodes.put(name, Node.createNode(name));
		}
		return nodes.get(name);
	}

	/**
	 * Return true if the graph contains a node with the given name, and false
	 * otherwise.
	 * 
	 * @param name
	 * @return
	 */
	public boolean containsNode(String name) {
		return nodes.containsKey(name);
	}

	/**
	 * Return a collection of all of the nodes in the graph.
	 * 
	 * @return
	 */
	public Collection<INode> getAllNodes() {
		// return a shallow copy
		return new LinkedList<INode>(nodes.values());
	}

	/**
	 * Perform a breadth-first search on the graph, starting at the node with
	 * the given name. The visit method of the {@link NodeVisitor} should be
	 * called on each node the first time we visit the node.
	 * 
	 * 
	 * @param startNodeName
	 * @param v
	 */
	public void breadthFirstSearch(String startNodeName, NodeVisitor v) {
		Set<String> visited = new HashSet<>();
		List<String> toVisit = new LinkedList();
		toVisit.add(startNodeName);
		while (!toVisit.isEmpty()) {
			String curr = toVisit.remove(0);
			if (!visited.contains(curr)) {
				v.visit(this.nodes.get(curr));
				visited.add(curr);
				for (INode i : this.nodes.get(curr).getNeighbors()) {
					String s = i.getName();
					if (!visited.contains(s)) {
						toVisit.add(s);
					}
				}
			}
		}
	}

	/**
	 * Perform a depth-first search on the graph, starting at the node with the
	 * given name. The visit method of the {@link NodeVisitor} should be called
	 * on each node the first time we visit the node.
	 * 
	 * 
	 * @param startNodeName
	 * @param v
	 */
	public void depthFirstSearch(String startNodeName, NodeVisitor v) {
		Set<String> visited = new HashSet<>();
		Stack toVisit = new Stack();
		toVisit.push(startNodeName);
		while (!toVisit.isEmpty()) {
			String curr = (String) toVisit.pop();
			if (!visited.contains(curr)) {
				v.visit(this.nodes.get(curr));
				visited.add(curr);
				for (INode i : this.nodes.get(curr).getNeighbors()) {
					String s = i.getName();
					if (!visited.contains(s)) {
						toVisit.add(s);
					}
				}
			}
		}
	}

	/**
	 * Perform Dijkstra's algorithm for computing the cost of the shortest path
	 * to every node in the graph starting at the node with the given name.
	 * Return a mapping from every node in the graph to the total minimum cost
	 * of reaching that node from the given start node.
	 * 
	 * <b>Hint:</b> Creating a helper class called Path, which stores a
	 * destination (String) and a cost (Integer), and making it implement
	 * Comparable, can be helpful. Well, either than or repeated linear scans.
	 * 
	 * @param startName
	 * @return
	 */
	public Map<INode, Integer> dijkstra(String startName) {
		Map<INode, Integer> res = new HashMap<INode, Integer>();
		PriorityQueue<Path> toDo = new PriorityQueue<Path>();
		toDo.add(new Path(startName, 0));
		while (res.size() < this.nodes.size()) {
			Path next = toDo.poll();
			INode node = this.nodes.get(next.getDestination());
			if (res.containsKey(node)) {
				continue;
			}
			int cost = next.getCost();
			res.put(node, cost);
			for (INode n : node.getNeighbors()) {
				toDo.add(new Path(n.getName(), cost + n.getWeight(node)));
			}
		}
		return res;
	}

	/**
	 * Perform Prim-Jarnik's algorithm to compute a Minimum Spanning Tree (MST).
	 * 
	 * The MST is itself a graph containing the same nodes and a subset of the
	 * edges from the original graph.
	 * 
	 * @return
	 */
	public IGraph primJarnik() {
		/* Declare graph to return later*/
		IGraph res = (IGraph) new Graph();
		
		/*Select random node to start with*/
		INode curr = this.nodes.values().iterator().next();
		String startName = curr.getName();
		res.getOrCreateNode(startName);
		INode start = this.nodes.get(startName);
		
		/*Put edges from startNode into PriorityQ*/
		PriorityQueue<Edge> toDo = new PriorityQueue<Edge>();
		
		Collection<INode> nb = start.getNeighbors();
		for(INode n: nb){
			int weight = start.getWeight(n);
			toDo.add(new Edge(weight, startName, n.getName()));
		}

		/*Put that random node into returning result*/
		res.getOrCreateNode(startName);
		
		while (res.getAllNodes().size() < this.nodes.size()) {
			/*Pop first node form Q*/
			Edge next = toDo.poll();
			
			//System.out.println(next.toString());
			
			String sourceName = next.getSource();		
			String destinationName = next.getDestination();
			INode destNode = this.nodes.get(destinationName);
			
			/*Check if nodes are already in results*/
			if(res.containsNode(sourceName)&&res.containsNode(destinationName)){
				continue;
			}
			
			/*Update results*/
			INode source = res.getOrCreateNode(sourceName);
			INode destination = res.getOrCreateNode(destinationName);
			source.addDirectedEdgeToNode(destination, next.getWeight());
			
			/*Put edges to neighbors into Q*/
			Collection<INode> neighbors = destNode.getNeighbors();
			for(INode n: neighbors){
				int weight = destNode.getWeight(n);
				toDo.add(new Edge(weight, destinationName, n.getName()));
			}
		}
		
		return res;
	}

	public Map<Integer, Set<String>> getMoves(String startNodeName,
			NodeVisitor v, int depth) {
		
		Map<Integer, Set<String>> result = new HashMap<Integer, Set<String>>();

		List<String> toVisit = new LinkedList();
		List<String> toVisit2 = new LinkedList();

		toVisit.add(startNodeName);

		int level = 0;

		result.put(level, new HashSet<String>());
		result.get(level).add(startNodeName);

		while (!toVisit.isEmpty() && level <= depth) {

			String curr = toVisit.remove(0);
			INode currNode = this.nodes.get(curr);
			v.visit(currNode);
			
			result.get(level).add(curr);
			
			if(toVisit.isEmpty()){
				level++;
				result.put(level, new HashSet<String>());
			}
			
			
			for (INode i : this.nodes.get(curr).getNeighbors()) {
				String s = i.getName();
				toVisit2.add(s);
			}
			
			if(toVisit.isEmpty()){
				List<String> temp = new LinkedList();
				temp = toVisit;
				toVisit = toVisit2;
				toVisit2 = temp;
			}
		}

		return result;
	}
	
	public Map<Integer, Set<String>> getMovesT(String startNodeName,
			NodeVisitor v, int depth, List<String> transportTypes) {

		Map<Integer, Set<String>> result = new HashMap<Integer, Set<String>>();
		for(int i=1; i<=5; i++){
			result.put(i, new HashSet<String>());
		}

		List<String> toVisit = new LinkedList();
		List<String> toVisit2 = new LinkedList();

		toVisit.add(startNodeName);

		int level = 0;

		result.put(level, new HashSet<String>());
		result.get(level).add(startNodeName);

		while (!toVisit.isEmpty() && level <= depth) {

			String curr = toVisit.remove(0);
			INode currNode = this.nodes.get(curr);
			v.visit(currNode);
			
			result.get(level).add(curr);
			
			if(toVisit.isEmpty()){
				level++;
			}
			
			
			for (INode i : this.nodes.get(curr).getNeighbors()) {
				if (transportTypes.contains("taxi") && currNode.getWeight(i) == 1 || 
					transportTypes.contains("bus") && currNode.getWeight(i) == 2 ||
					transportTypes.contains("underground") && currNode.getWeight(i) == 3)  {
					String s = i.getName();
					toVisit2.add(s);
				}
			}
			
			if(toVisit.isEmpty()){
				List<String> temp = new LinkedList();
				temp = toVisit;
				toVisit = toVisit2;
				toVisit2 = temp;
			}
		}

		return result;
	}
}