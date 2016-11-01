package ca.ubc.ece.cpen221.mp3.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import ca.ubc.ece.cpen221.mp3.staff.Graph;
import ca.ubc.ece.cpen221.mp3.staff.Vertex;
/**
 * @author Cinnie Hsiung, Yuqing Du
 */
public class AdjacencyMatrixGraph implements Graph {

    // adjacency matrix as a list of lists
    private List<ArrayList<Integer>> adjacencyMatrix;
    // list of vertices that correspond to the adjacency matrix
    private List<Vertex> vertices = new ArrayList<Vertex>();

    /**
     * Constructor for the AdjacencyMatrixGraph.
     */
    public AdjacencyMatrixGraph() {
        adjacencyMatrix = new ArrayList<ArrayList<Integer>>();
    }

    /**
     * This method adds a vertex to the adjacency matrix graph
     * 
     * @param v
     *            - Vertex to add to the adjacency matrix graph.
     */
    public void addVertex(Vertex v) {
        if (vertices.contains(v)) {
            return;
        }

        else {

            // defensively copy vertex
            Vertex cloneVertex = new Vertex(v.getLabel());
            vertices.add(cloneVertex);

            // get index of newly added vertex
            int newVertexIndex = vertices.indexOf(v);

            // add a new row (List) to the matrix, at index representing newly
            // added vertex
            adjacencyMatrix.add(newVertexIndex, new ArrayList<Integer>());

            for (int indexA = 0; indexA < vertices.size() - 1; indexA++) {
                adjacencyMatrix.get(newVertexIndex).add(0);
            }

            // add 0 to the end of each row for the new vertex
            for (int index = 0; index < vertices.size(); index++) {
                adjacencyMatrix.get(index).add(0);
            }
        }

    }

    /**
     * Method to add an edge from v1 to v2 (two vertices) in the adjacency
     * matrix graph.
     * 
     * @param v1
     *            - Vertex from which the edge begins (must already be in the
     *            matrix)
     * @param v2
     *            - Vertex that the edge connects to (must already be in the
     *            matrix)
     */
    public void addEdge(Vertex v1, Vertex v2) {
        int indexOfV1 = vertices.indexOf(v1);
        int indexOfV2 = vertices.indexOf(v2);

        adjacencyMatrix.get(indexOfV1).remove(indexOfV2);
        adjacencyMatrix.get(indexOfV1).add(indexOfV2, 1);

    }

    /**
     * Method to check whether or not there's an edge between v1 and v2 (two
     * vertices)
     * 
     * @param v1
     *            - Vertex from which the edge begins (must already be in the
     *            adjacency matrix graph)
     * @param v2
     *            - Vertex at which the edge ends (must already be in the
     *            adjacency matrix graph)
     * @return - true iff the edge exists from v1 to v2, false if not.
     */
    public boolean edgeExists(Vertex v1, Vertex v2) {
        int indexOfV1 = vertices.indexOf(v1);
        int indexOfV2 = vertices.indexOf(v2);

        if (adjacencyMatrix.get(indexOfV1).get(indexOfV2) == 1) {
            return true;
        }

        return false;
    }

    /**
     * Returns a list of the downstream neighbours of given Vertex v (ex. if
     * there is an edge from v to w, w would be on the list of returned
     * vertices)
     * 
     * @param v
     *            - Vertex we are getting downstream neighbours of (must be in
     *            the adjacency matrix graph)
     * @return List<Vertex> - list containing all the vertices that are
     *         downstream from v (there exists an edge between v and every
     *         element on this returned list)
     * 
     *         - If there are no downstream neighbours, returns an empty list.
     */
    public List<Vertex> getDownstreamNeighbors(Vertex v) {

        // new linkedlist to return
        List<Vertex> downstreamNeighbours = new LinkedList<Vertex>();

        // index of vertex we are currently checking (v)
        int vertexIndex = vertices.indexOf(v);

        for (int index = 0; index < vertices.size(); index++) {
            // get edge from v to w
            int edge = adjacencyMatrix.get(vertexIndex).get(index);

            // get w vertex
            Vertex w = vertices.get(index);

            // if edge exists, add w to neighbours
            if (edge == 1) {
                downstreamNeighbours.add(w);
            }
        }

        return Collections.unmodifiableList(downstreamNeighbours);
    }

    /**
     * Method that returns the upstream neighbours of a given vertex v (ex. if
     * there is an edge from vertex u to v, u would be on the list of returned
     * vertices)
     * 
     * @param v
     *            - Vertex from which we get the upstream neighbours (must be in
     *            the adjacency matrix graph)
     * @return List<Vertex> - all the upstream neighbours of v (there exists an
     *         edge from each element on the returned list to v). Returns an
     *         empty list if no such vertices exist.
     */
    public List<Vertex> getUpstreamNeighbors(Vertex v) {

        // new linkedlist to return
        List<Vertex> upstreamNeighbours = new LinkedList<Vertex>();

        // index of vertex we are currently checking (v)
        int vertexIndex = vertices.indexOf(v);

        int index = 0;

        // iterate through the adjacencymatrix
        for (ArrayList<Integer> currentRow : adjacencyMatrix) {

            Vertex u = vertices.get(index);

            if (currentRow.get(vertexIndex) == 1) { // if edge exists, add w to
                                                    // neighbours
                upstreamNeighbours.add(u);
            }

            index++;
        }

        return Collections.unmodifiableList(upstreamNeighbours);

    }

    /**
     * Method that returns a list of the vertices in the adjacency matrix graph
     * 
     * @return List<Vertex> of all the vertices in the adjacency matrix graph.
     *         Returns a list of size 0 if there are no vertices in the graph.
     */
    public List<Vertex> getVertices() {
        return Collections.unmodifiableList(vertices);
    }

    /**
     * Method that returns a copy of the current adjacency matrix graph.
     * 
     * @return List<ArrayList<Integer>> - a copy of the adjacency matrix graph.
     *         Returns an empty list if there are no vertices in the graph.
     */

    public List<ArrayList<Integer>> getMatrixCopy() {
        return Collections.unmodifiableList(adjacencyMatrix);
    }

}
