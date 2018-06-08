package junit;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.junit.Test;

import graph.GraphFactories;
import graph.IGraph;
import graph.INode;
import graph.NodeVisitor;

public class TestGraphAdvanced
{
    static String root="tests/";
    
    /**
     * Create a file in the tests folder for breadth-first search (BFS).
     * 
     * Put the filename into the FileInputStream constructor in the place indicated. 
     * 
     * Choose a clear, descriptive filename so that it's obvious what the file is used for.
     * Remember we are evaluating your test cases, so they should be correct, and also 
     * easy to understand.
     * 
     * You can certainly have multiple test cases for each method!
     * 
     * BFS/DFS do not care about edge weights, so use the createUndirectedGraphFromAdjacencyList
     * method.
     * 
     * The format of the file should be something like:
     * 
     * A B
     * B C
     * A D
     * D E
     * 
     */
    @Test
    public void testBFS() throws Exception
    {
        IGraph g=GraphFactories.createUndirectedGraphFromAdjacencyList(new FileInputStream("tests/BFStestGraph"));
        LinkedList<String> list = new LinkedList<String>();
        g.breadthFirstSearch("A", new NodeVisitor() {
            @Override
            public void visit(INode n) {
                System.out.println("BFS Visiting node: " + n.getName());
                list.add(n.getName());
            }
        });
        assertEquals("A",list.get(0));
        assertTrue(list.indexOf("B")>0 && list.indexOf("B")<3 ); 
        assertTrue(list.indexOf("C")>0 && list.indexOf("C")<3 );
//        assertEquals("E",list.get(3)); 
//        assertEquals("D",list.get(4)); 
//        assertEquals("G",list.get(5));   
        assertEquals("F",list.get(6));     
    }
    
    /**
     * Create a file in the tests folder for breadth-first search (BFS).
     * 
     * Put the filename into the FileInputStream constructor in the place indicated. 
     * 
     * Choose a clear, descriptive filename so that it's obvious what the file is used for.
     * Remember we are evaluating your test cases, so they should be correct, and also 
     * easy to understand.
     * 
     * You can certainly have multiple test cases for each method!
     * 
     * BFS/DFS do not care about edge weights, so use the createUndirectedGraphFromAdjacencyList
     * method.
     * 
     * The format of the file should be something like:
     * 
     * A B
     * B C
     * A D
     * D E
     * 
     */
    @Test
    public void testDFS() throws Exception
    {
        IGraph g=GraphFactories.createUndirectedGraphFromAdjacencyList(new FileInputStream("tests/BFStestGraph"));
        LinkedList<String> list = new LinkedList<String>();
        g.depthFirstSearch("A", new NodeVisitor() {
            @Override
            public void visit(INode n) {
                System.out.println("DFS Visiting node: " + n.getName());
                list.add(n.getName());
            }
        });
        assertEquals("A",list.get(0));
        assertEquals("C",list.get(1)); 
        assertEquals("G",list.get(2));  
        assertEquals("D",list.get(3)); 
        assertEquals("B",list.get(4)); 
        assertEquals("E",list.get(5));   
        assertEquals("F",list.get(6));     
    }
    
    
    
    /**
     * Create a file in the tests folder for Dijkstra.
     * 
     * Put the filename into the FileInputStream constructor in the place indicated. Choose
     * a good, descriptive filename so that it's clear what the file is used for.
     * 
     * You need multiple test cases for this method.
     * 
     * Dijkstra cares about the weights, so note that we are using the
     * createUndirectedWeightedGraphFromEdgeList method.
     * 
     * The format should be something like:
     * 
     * A B 4
     * B C 6
     * A D 2
     * 
     * This means that there is an undirected edge from A to B with a weight of 4,
     * B to C with a weight of 6, and A to D with a weight of 2.
     * 
     */
    @Test
    public void testDijkstra1() throws Exception
    { 
        IGraph g=GraphFactories.createUndirectedWeightedGraphFromEdgeList(new FileInputStream("tests/graph1.txt"));
        Map<INode, Integer> shortPaths = g.dijkstra("A");
//        for(INode n: shortPaths.keySet()){
//        	System.out.printf("%s = %d\n",n.getName(), shortPaths.get(n));
//        }
        assertEquals(37, (int)shortPaths.get(g.getOrCreateNode("E")));
        assertEquals(30, (int)shortPaths.get(g.getOrCreateNode("P")));
        assertEquals(62, (int)shortPaths.get(g.getOrCreateNode("T")));
        assertEquals(49, (int)shortPaths.get(g.getOrCreateNode("R")));
        assertEquals(27, (int)shortPaths.get(g.getOrCreateNode("H")));
    }
    
    /**
     * Create a file that defines a graph for testing Prim-Jarnik. Put this file in 
     * the tests folder. Pick a clear and descriptive name for the file.
     * 
     * Prim-Jarnik cares about edge weights, so use the
     * createUndirectedWeightedGraphFromEdgeList static factor method to create the graph.
     * 
     * The file will look something like this:
     * 
     * A B 6
     * B C 4
     * A D 2
     * 
     * Remember that Prim-Jarnik can have multiple correct solutions, so make sure your
     * test cases have only one correct solution.
     * 
     */
    @Test
    public void testPrimJarnik1() throws Exception
    {
        IGraph graph = GraphFactories.createUndirectedWeightedGraphFromEdgeList
        		(new FileInputStream("tests/PrimJarnikTestGraph.txt"));
        IGraph g2 = graph.primJarnik();
        
        System.out.println(GraphFactories.toUndirectedWeightedDotFile(g2, "PrimJarnikTest"));
        
        assertTrue(g2.containsNode("A"));
        assertTrue(g2.containsNode("B"));
        assertTrue(g2.containsNode("C"));
        
        INode A = g2.getOrCreateNode("A");
        INode B = g2.getOrCreateNode("B");
        INode C = g2.getOrCreateNode("C");
        
        assertTrue(A.hasEdge(C));
        assertTrue(C.hasEdge(B));
        assertTrue(!B.hasEdge(A));
    }

}
