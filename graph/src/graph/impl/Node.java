package graph.impl;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import graph.INode;
import junit.VERSION;

/**
 * Class to represent a single node (or vertex) of a graph.
 * 
 * Node can be used for either directed or undirected graphs, as well as
 * for weighted or unweighted graphs. For unweighted graphs, use something like 1 for all 
 * of the weights. For undirected graphs, add a directed edge in both directions.
 * 
 * You want to make as many operations O(1) as possible, which means you will
 * probably use a lot of Maps.
 * 
 * Side note: You can tell that I come from a networking background and not a mathematical
 * background because I almost always use the term "node" instead of "vertex".
 * 
 * @author jspacco
 *
 */
public class Node implements INode
{
    private String name;
    private Map<INode,Integer> neighbors=new HashMap<INode,Integer>();
    
    /**
     * Create a new node with the given name. The newly created node should
     * have no edges.
     * 
     * This constructor is private because we are using the Static Factory pattern
     * to construct new nodes. We use static factories so that we can test multiple
     * implementations of the same interface using one set of JUnit tests.
     * 
     * @param name
     */
    private Node(String name) {
        this.name=name;
    }
    
    public static INode createNode(String name){
        // The VERSION class knows what version to run
        int version = VERSION.version;
        if (version == 0){
            // version 0 uses the graph.impl.Node code
            return new Node(name);
        } else if (version == -1){
            // load Spacco's solution code
            try {
                Class<?> clazz = Class.forName(String.format("graph.sol.Node", version));
                Constructor<?> constructor = clazz.getConstructor(String.class);
                return (INode)constructor.newInstance(name);
            } catch (Exception e){
                throw new RuntimeException(e);
            }
        } else {
            try {
                Class<?> clazz = Class.forName(String.format("graph.broken%d.Node", version));
                Constructor<?> constructor = clazz.getConstructor(String.class);
                return (INode)constructor.newInstance(name);
            } catch (Exception e){
                throw new RuntimeException(e);
            }
        }
    }
    
    /**
     * Return the name of the node, which is a String.
     * 
     * @return
     */
    public String getName() {
        return this.name;
    }

    /**
     * Return a collection of nodes that the current node is connected to by an edge.
     * 
     * @return
     */
    public Collection<INode> getNeighbors() {
        return neighbors.keySet();
    }
    
    /**
     * Add a directed edge to the given node using the given weight.
     * 
     * @param n
     * @param weight
     */
    public void addDirectedEdgeToNode(INode n, int weight) {
        neighbors.put(n, weight);
    }
    
    /**
     * Add an undirected edge to the given node using the given weight.
     * Remember than an undirected edge can be implemented using two directed edges.
     * 
     * @param n
     * @param weight
     */
    public void addUndirectedEdgeToNode(INode n, int weight) {
        addDirectedEdgeToNode(n, weight);
        n.addDirectedEdgeToNode(this, weight);
    }

    /**
     * Remove the directed edge to the given node.
     * 
     * If there is no edge to the given node, throw
     * IllegalStateException (which is a type of runtime exception).
     * 
     * @param n
     * @throws IllegalStateException
     */
    public void removeDirectedEdgeToNode(INode n) {
        this.neighbors.remove(n);
    }
    
    /**
     * Remove an undirected edge to the given node. This means removing
     * the edge to the given node, but also any edge from the given
     * node back to this node.
     * 
     * Throw a IllegalStateException if there is no edge to the given node.
     * 
     * @param n
     * @throws IllegalStateException
     */
    public void removeUndirectedEdgeToNode(INode n) {
        removeDirectedEdgeToNode(n);
        n.removeDirectedEdgeToNode(this);
    }
    
    /**
     * Return true if there is an edge to the given node from this node,
     * and false otherwise.
     * 
     * @param other
     * @return
     */
    public boolean hasEdge(INode other) {
        return neighbors.containsKey(other);
    }
    
    /**
     * Get the weight of the edge to the given node.
     * 
     * If no such edge exists, throw {@link IllegalStateException}
     * 
     * @param n
     * @return
     * @throws IllegalStateException
     */
    public int getWeight(INode n) {
        if (neighbors.containsKey(n)) {
            return neighbors.get(n);
        }
        throw new IllegalStateException("No edge to "+n.getName());
    }
}