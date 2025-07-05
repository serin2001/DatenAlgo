package ex123;

import java.util.ArrayList;
import java.util.List;

class Edge {
    int u, v;
    double weight;

    Edge(int u, int v, double weight) {
        this.u = u;
        this.v = v;
        this.weight = weight;
    }
}

public class NegativeCycleFinder {
    private List<Edge> edges; // Liste der Kanten
    private int numVertices; // Anzahl der Knoten

    public NegativeCycleFinder(int numVertices) {
        this.numVertices = numVertices;
        this.edges = new ArrayList<>();
    }

    public void addEdge(int u, int v, double weight) {
        edges.add(new Edge(u, v, weight));
    }

    // Methode zur Erkennung negativer Kreise
    public void findNegativeCycle() {
        double[] dist = new double[numVertices];
        int[] predecessor = new int[numVertices];

        // Initialisierung
        for (int i = 0; i < numVertices; i++) {
            dist[i] = Double.POSITIVE_INFINITY; // Setze alle Distanzen auf unendlich
            predecessor[i] = -1; // Vorgänger auf -1 setzen
        }

        // Setze die Distanz des Startknotens auf 0
        dist[0] = 0;

        // Entspanne alle Kanten |V| - 1 Mal
        for (int i = 1; i < numVertices; i++) {
            for (Edge edge : edges) {
                if (dist[edge.u] != Double.POSITIVE_INFINITY && dist[edge.u] + edge.weight < dist[edge.v]) {
                    dist[edge.v] = dist[edge.u] + edge.weight;
                    predecessor[edge.v] = edge.u;
                }
            }
        }

        // Überprüfe auf negative Kreise
        for (Edge edge : edges) {
            if (dist[edge.u] != Double.POSITIVE_INFINITY && dist[edge.u] + edge.weight < dist[edge.v]) {
                // Ein negativer Kreis wurde gefunden
                printNegativeCycle(predecessor, edge.v);
                return;
            }
        }

        System.out.println("Kein negativer Kreis gefunden.");
    }

    private void printNegativeCycle(int[] predecessor, int start) {
        List<Integer> cycle = new ArrayList<>();
        boolean[] visited = new boolean[numVertices];

        // Finde den Startpunkt des Kreises
        int current = start;
        for (int i = 0; i < numVertices; i++) {
            current = predecessor[current]; // Gehe zurück, um den Kreis zu finden
        }

        // Speichere den Startpunkt des Kreises
        int cycleStart = current;
        cycle.add(cycleStart);

        // Gehe weiter, um den gesamten Kreis zu extrahieren
        current = predecessor[cycleStart];
        while (current != cycleStart) {
            cycle.add(current);
            current = predecessor[current];
        }
        cycle.add(cycleStart); // Füge den Startpunkt des Kreises hinzu

        // Ausgabe des negativen Kreises
        System.out.println("Negativer Kreis gefunden:");
        for (int i = cycle.size() - 1; i >= 0; i--) {
            System.out.print(cycle.get(i) + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        NegativeCycleFinder graph = new NegativeCycleFinder(5); // Beispiel mit 5 Knoten
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, -1);
        graph.addEdge(2, 0, -1);
        graph.addEdge(1, 3, 2);
        graph.addEdge(3, 4, 1);
        graph.addEdge(4, 1, -3);

        graph.findNegativeCycle(); // Suche nach negativen Kreisen
    }
}
