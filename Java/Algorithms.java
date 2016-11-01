package ca.ubc.ece.cpen221.mp3.graph;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import ca.ubc.ece.cpen221.mp3.staff.Graph;
import ca.ubc.ece.cpen221.mp3.staff.Vertex;
/**
 * @author Cinnie Hsiung, Yuqing Du
 */
public class Algorithms {

    /**
     * Breadth first search algorithm to traverse a graph.
     * 
     * @param Graph
     *            g - graph to search through with breadth first search
     *            algorithm
     * 
     * @param Vertex
     *            v - source vertex for the search algorithm. Must already be in
     *            the graph.
     * 
     * @return Set<List<Vertex>> searchResult a set of the paths (in lists)
     *         searched using the breadth first search method
     * 
     */

    public static Set<List<Vertex>> breadthFirstSearch(Graph g) {
        Set<List<Vertex>> searchResult = new HashSet<List<Vertex>>();
        List<Vertex> allVertices = new LinkedList<Vertex>();
        List<Vertex> visitedVertices;

        // get every starting vertex
        allVertices = g.getVertices();

        // make a queue to keep track of the vertex we still need to visit
        Queue<Vertex> toVisit = new LinkedList<Vertex>();

        // for every start vertex
        for (Vertex startVertex : allVertices) {
            // make a list to keep track of the vertices already visited
            visitedVertices = new LinkedList<Vertex>();
            // add the current start vertex to the toVisit queue
            toVisit.add(startVertex);

            // while there are still vertices to visit
            while (!toVisit.isEmpty()) {
                // visit the current vertex, and document that by adding it to
                // the visitedVertices list
                Vertex currentVertex = toVisit.remove();
                visitedVertices.add(currentVertex);

                // get all the downstream neighbors for the current vertex
                for (Vertex downstairsVertex : g.getDownstreamNeighbors(currentVertex)) {
                    // for only add to the toVisit queue if we haven't already
                    // visited them and they aren't already in the toVisit queue
                    if (!visitedVertices.contains(downstairsVertex) && !toVisit.contains(downstairsVertex)) {
                        toVisit.add(downstairsVertex);
                    }
                }

            }
            // add the list visited vertices to the set of paths
            searchResult.add(Collections.unmodifiableList(visitedVertices));
        }

        // return the set of paths
        return Collections.unmodifiableSet(searchResult);
    }

    /**
     * Depth first search algorithm to traverse a graph. Searches through a
     * graph structure by starting at an arbitrary node and explore as far as
     * possible along each branch before backtracking.
     * 
     * 
     * @param Graph
     *            g - graph to search through using depth first search algorithm
     * 
     * @return Set<List<Vertex>> searchResult a Set of all the paths (in lists)
     *         down the graph using the depth first search method.
     * 
     * 
     */

    public static Set<List<Vertex>> depthFirstSearch(Graph g) {
        Set<List<Vertex>> searchResult = new HashSet<List<Vertex>>();
        List<Vertex> visitedVertices;

        // make a list of all the start vertices
        List<Vertex> allVertices = new LinkedList<Vertex>();
        allVertices = g.getVertices();

        // make a stack to keep track of the order we need to visit vertices
        Stack<Vertex> toVisit = new Stack<Vertex>();

        // for every start vertex
        for (Vertex startVertex : allVertices) {
            // make a list to keep track of vertices we've already visited
            visitedVertices = new LinkedList<Vertex>();
            // add the start vertex to the stack of vertices we need to visit
            toVisit.push(startVertex);

            // while there are still vertices to visit
            while (!toVisit.isEmpty()) {
                // visit the current vertex and add it to the visistedVertices
                // list
                Vertex currentVertex = toVisit.pop();
                visitedVertices.add(currentVertex);

                // for every downstream neighbor of the current vertex
                for (Vertex downstairsVertex : g.getDownstreamNeighbors(currentVertex)) {
                    // if we haven't already visited that vertex/aren't already going to visit it
                    if (!visitedVertices.contains(downstairsVertex) && !toVisit.contains(downstairsVertex)) {
                        // add it to the list of vertices we need to visit
                        toVisit.push(downstairsVertex);
                    }
                }

            }
            // add the visitedVertices list to the set of paths
            searchResult.add(Collections.unmodifiableList(visitedVertices));
        }

        return Collections.unmodifiableSet(searchResult);
    }

    /**
     * Method to find the shortest distance between two vertices in an
     * unweighted graph. (Number of edges that would have to be traversed to get
     * between two vertices). Distance between vertex and itself is 0.
     * 
     * @param graph
     *            g - the graph to search through.
     * @param a
     *            - the start vertex. Must already be in the graph.
     * @param b
     *            - the end vertex. Must already be in the graph.
     * 
     * @return the shortest distance to get from vertex a to vertex b. Returns
     *         Integer.MAX_VALUE if no path exists between a and b.
     * 
     * @throws NoPathException
     *             if no path could be found from vertex a to vertex b
     * 
     */
    public static int shortestDistance(Graph g, Vertex a, Vertex b) throws NoPathException {
        List<Vertex> visitedVertices = new LinkedList<Vertex>();
        Queue<Vertex> toVisit = new LinkedList<Vertex>();
        int shortestDistance;

        if (!g.getVertices().contains(b)) {
            throw new NoPathException();
        }

        // make a map of the distances from Vertex a to all other vertices
        Map<Vertex, Integer> distances = new HashMap<Vertex, Integer>();

        // put that the distance from a to itself is 0
        distances.put(a, 0);

        // conduct a breadth first search
        toVisit.add(a);
        outer: while (!toVisit.isEmpty()) {
            Vertex currentVertex = toVisit.remove();
            visitedVertices.add(currentVertex);

            for (Vertex downstairsVertex : g.getDownstreamNeighbors(currentVertex)) {
                // if we haven't visited the current vertex before/aren't already going to visit it
                if (!visitedVertices.contains(downstairsVertex) && !toVisit.contains(downstairsVertex)) {
                    toVisit.add(downstairsVertex);
                    // put that the distance to this downstairs vertex is one more
                    // than the distance from a to the current vertex
                    // only update the distance if there is not already a distance
                    if (!distances.containsKey(downstairsVertex)) {
                        distances.put(downstairsVertex, distances.get(currentVertex) + 1);
                    }
                }
                
                //break as soon as the distance to b is found
                if (distances.containsKey(b)) {
                    break outer;
                }
            }
        }

        // if there was no path between a and b throw an exception
        if (!distances.containsKey(b)) {
            throw new NoPathException();
        }

        // otherwise return the distance
        shortestDistance = distances.get(b);
        return shortestDistance;
    }

    /**
     * Method that returns a list of all vertices u such that there is an edge
     * from u to a and an edge from u to b in the given graph G. Returns an
     * empty list if there are no such vertices.
     * 
     * @param graph
     *            - graph to search through for common upstream vertices
     * 
     * @param a
     *            - Vertex from which we are searching for upstream vertices.
     *            Must already be in the graph.
     * 
     * @param b
     *            - Vertex from which we are searching for upstream vertices.
     *            Must already be in the graph.
     * 
     * @return a List of all vertices u, such that there exists an edge from u
     *         to vertex a and u to vertex b or an empty List if no such
     *         vertices exist.
     */

    public static List<Vertex> commonUpstreamVertices(Graph graph, Vertex a, Vertex b) {
        List<Vertex> upstreamA = new LinkedList<Vertex>();
        List<Vertex> upstreamB = new LinkedList<Vertex>();
        List<Vertex> commonUpStream = new LinkedList<Vertex>();

        // get the upstream neighbors of both a and b
        upstreamA = graph.getUpstreamNeighbors(a);
        upstreamB = graph.getUpstreamNeighbors(b);

        // compare the neighbors
        for (Vertex currentVertexA : upstreamA) {
            if (upstreamB.contains(currentVertexA)) {
                // if the neighbors match then add them to the commonUpStream
                // list
                commonUpStream.add(currentVertexA);
            }
        }

        return Collections.unmodifiableList(commonUpStream);

    }

    /**
     * Method that returns a list of all vertices v such that there is an edge
     * from a to v and an edge from b to v in the given graph G. Returns an
     * empty list if there are no such vertices.
     * 
     * @param graph
     *            - graph to search through for common downstream vertices
     * 
     * @param a
     *            - Vertex from which we are searching for downstream vertices.
     *            Must already be in the graph.
     * 
     * @param b
     *            - Vertex from which we are searching for downstream vertices.
     *            Must already be in the graph.
     * 
     * @return a List of all vertices v such that there exists an edge from a to
     *         v and from b to v. Returns an empty List if no such vertices
     *         exist.
     * 
     */

    public static List<Vertex> commonDownstreamVertices(Graph graph, Vertex a, Vertex b) {
        List<Vertex> downstreamA = new LinkedList<Vertex>();
        List<Vertex> downstreamB = new LinkedList<Vertex>();
        List<Vertex> commonDownStream = new LinkedList<Vertex>();

        // get downstream neighbors of both a and b
        downstreamA = graph.getDownstreamNeighbors(a);
        downstreamB = graph.getDownstreamNeighbors(b);

        // if the downstream neighbors exist in both and b
        for (Vertex currentVertexA : downstreamA) {
            if (downstreamB.contains(currentVertexA)) {
                // add that neighbor to the list of common downstream neighbors
                commonDownStream.add(currentVertexA);
            }
        }

        return Collections.unmodifiableList(commonDownStream);

    }

}
