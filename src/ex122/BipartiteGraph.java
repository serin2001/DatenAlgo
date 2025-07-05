package ex122;

import java.util.ArrayList;
import java.util.List;

public class BipartiteGraph {
    private List<List<Integer>> adj; // Adjacency list representation of the graph
    private int[] color; // Array to store colors of nodes

    public BipartiteGraph(int numVertices) {
        adj = new ArrayList<>(numVertices);
        for (int i = 0; i < numVertices; i++) {
            adj.add(new ArrayList<>());
        }
        color = new int[numVertices];
        for (int i = 0; i < numVertices; i++) {
            color[i] = -1; // Initialize all nodes as unvisited
        }
    }

    // Method to add an edge between two vertices
    public void addEdge(int u, int v) {
        adj.get(u).add(v);
        adj.get(v).add(u); // Since the graph is undirected
    }

    // DFS method to check bipartiteness and color the graph
    private boolean dfs(int node, int c, List<Integer> group1, List<Integer> group2) {
        color[node] = c; // Color the node
        if (c == 0) {
            group1.add(node); // Add to group 1
        } else {
            group2.add(node); // Add to group 2
        }

        // Explore all adjacent nodes
        for (int neighbor : adj.get(node)) {
            if (color[neighbor] == -1) { // If the neighbor is unvisited
                if (!dfs(neighbor, 1 - c, group1, group2)) {
                    return false; // Conflict found
                }
            } else if (color[neighbor] == c) { // If the neighbor has the same color
                return false; // Conflict found
            }
        }
        return true; // Successful coloring
    }

    // Method to check if the graph is bipartite and return the groups
    public String checkBipartite() {
        List<Integer> group1 = new ArrayList<>();
        List<Integer> group2 = new ArrayList<>();

        for (int v = 0; v < adj.size(); v++) {
            if (color[v] == -1) { // If the node is unvisited
                if (!dfs(v, 0, group1, group2)) {
                    return "Error: The graph is not bipartite";
                }
            }
        }
        return "Group 1: " + group1 + "\nGroup 2: " + group2;
    }

    public static void main(String[] args) {
        BipartiteGraph graph = new BipartiteGraph(6); // Example with 6 students
        graph.addEdge(0, 1);
        graph.addEdge(0, 3);
        graph.addEdge(1, 2);
        graph.addEdge(1, 4);
        graph.addEdge(2, 5);
        graph.addEdge(3, 4);

        String result = graph.checkBipartite();
        System.out.println(result);
    }
}