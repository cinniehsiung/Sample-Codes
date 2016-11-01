package ca.ubc.ece.cpen221.mp3.graph;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ca.ubc.ece.cpen221.mp3.staff.Graph;
import ca.ubc.ece.cpen221.mp3.staff.Vertex;
/**
 * @author Cinnie Hsiung, Yuqing Du
 */
public class AdjacencyListGraph implements Graph {
    private Map<Vertex, List<Vertex>> adjacencyList;

    public AdjacencyListGraph() {
        // create the adjacency list!
        adjacencyList = new HashMap<Vertex, List<Vertex>>();
    }

    /**
     * Adds a vertex to the graph if the vertex was not already in the graph If
     * the vertex was already in the graph, does nothing
     * 
     * @param v
     *            the vertex to add to the graph
     */
    public void addVertex(Vertex v) {
        // if the vertex is already in the graph, break
        if (adjacencyList.containsKey(v))
            return;

        // otherwise defensively clone the vertex
        Vertex vClone = new Vertex(v.getLabel());

        // and add it to the adjacency list
        List<Vertex> edgeList = new LinkedList<Vertex>();
        adjacencyList.put(vClone, edgeList);
    }

    /**
     * Adds an edge from v1 to v2 if the edge does not already exist If the edge
     * exists, does nothing
     *
     * @requires v1 and v2 are vertices in the graph
     * 
     * @param v1
     *            the vertex the edge starts from
     * @param v2
     *            the vertex the edge ends
     */
    public void addEdge(Vertex v1, Vertex v2) {
        // if the edge already exists, do nothing
        if (edgeExists(v1, v2)) {
            return;
        }

        // defensively clone the the vertex
        Vertex v2Clone = new Vertex(v2.getLabel());
        // add the edge
        adjacencyList.get(v1).add(v2Clone);
    }

    /**
     * Check if there is an edge from v1 to v2.
     * 
     * @requires v1 and v2 be vertices in the graph
     * 
     * @return true if an edge from v1 connects to v2
     * @return false otherwise
     */
    public boolean edgeExists(Vertex v1, Vertex v2) {
        return adjacencyList.get(v1).contains(v2);

    }

    /**
     * Get an array containing all downstream vertices adjacent to v.
     *
     * @requires v is a vertex in the graph
     * 
     * @return a list containing each vertex w such that there is an edge from v
     *         to w, the size of the list will be the number of upstream
     *         vertices
     * 
     * @return a list of size 0 if v has no downstream neighbors.
     */
    public List<Vertex> getDownstreamNeighbors(Vertex v) {
        return Collections.unmodifiableList(adjacencyList.get(v));
    }

    /**
     * Get an array containing all upstream vertices adjacent to v.
     *
     * @requires v is a vertex in the graph
     * 
     * @return a list containing each vertex u such that there is an edge from u
     *         to v, the size of the list will be the number of upstream
     *         vertices
     * 
     * @return a list of size 0 if v has no upstream neighbors.
     */
    public List<Vertex> getUpstreamNeighbors(Vertex v) {
        // create an empty list for the upstream neighbors
        List<Vertex> neighbors = new LinkedList<Vertex>();

        // for every vertex in the adjacency list
        for (Vertex currentVertex : adjacencyList.keySet()) {
            // if that vertex has and edge to vertex v
            if (adjacencyList.get(currentVertex).contains(v)) {

                // add that vertex to the list of upstream neighbors
                Vertex vClone = new Vertex(currentVertex.getLabel());
                neighbors.add(vClone);
            }
        }

        return neighbors;
    }

    /**
     * Get all vertices in the graph.
     *
     * @return a list containing all vertices in the graph
     * @return a list of size 0 if the graph has no vertices.
     */
    public List<Vertex> getVertices() {
        List<Vertex> Vertices = new LinkedList<Vertex>();
        Vertices.addAll(adjacencyList.keySet());
        return Collections.unmodifiableList(Vertices);
    }

}
