package NegativeCycleFinder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;

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
	private HashSet<Edge> edges; // Liste der Kanten
	private HashSet<Integer> vertices; // Anzahl der Knoten
	private int numVertices;

	public NegativeCycleFinder(int numVertices) {
		this.vertices = new HashSet<>();
		this.edges = new HashSet<>();
		this.numVertices = numVertices;
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
			predecessor[i] = -1; // Vorgaenger auf -1 setzen
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

		// Ueberpruefe auf negative Kreise
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
			current = predecessor[current]; // Gehe zurueck im Kreis
		}

		// Speichere den Startpunkt des Kreises
		int cycleStart = current;
		cycle.add(cycleStart);

		// Gehe weiter im Kreis
		current = predecessor[cycleStart];
		while (current != cycleStart) {
			cycle.add(current);
			current = predecessor[current];
		}
		cycle.add(cycleStart); // Fuege den Startpunkt des Kreises hinzu

		// Ausgabe des negativen Kreises
		System.out.println("Negativer Kreis gefunden:");
		for (int i = cycle.size() - 1; i >= 0; i--) {
			System.out.print(cycle.get(i) + " ");
		}
		System.out.println();
	}

	public static void main(String[] args) {
		NegativeCycleFinder graph = new NegativeCycleFinder(1258); // Beispiel mit 5 Knoten

		// Pfad zur CSV-Datei anpassen
		String csvFile = "/home/andreas/git/CBAG/Data/mahindas.csv";
		String line = "";
		String csvSplitBy = " ";

		// CSV-Datei lesen und addEdge() für Werte ab der dritten Zeile ausführen
		try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
			int lineNumber = 0;
			while ((line = br.readLine()) != null) {
				lineNumber++;
				if (lineNumber < 4)
					continue; // Überspringen der ersten beiden Zeilen

				String[] values = line.split(csvSplitBy);
				int u = Integer.parseInt(values[0].trim());
				int v = Integer.parseInt(values[1].trim());
				double weight = Double.parseDouble(values[2].trim());
				if (u != v) {
					graph.vertices.add(u); // Knoten u zur Menge hinzufügen
					graph.vertices.add(v); // Knoten v zur Menge hinzufügen
					if (u < v)
						graph.addEdge(u, v, weight);
					else
						graph.addEdge(v, u, weight);
				}
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		}

		graph.findNegativeCycle(); // Suche nach negativen Kreisen
		System.out.println(graph.edges.size());
		
		System.out.println(graph.vertices.size());
	}
}
