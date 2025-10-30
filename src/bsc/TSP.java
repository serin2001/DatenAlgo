package bsc;

import com.google.ortools.Loader;
//import com.google.ortools.constraintsolver.FirstSolutionStrategy;
//import com.google.ortools.constraintsolver.LocalSearchMetaheuristic;
//import com.google.ortools.constraintsolver.RoutingIndexManager;
//import com.google.ortools.constraintsolver.RoutingModel;
//import com.google.ortools.constraintsolver.RoutingSearchParameters;
//import com.google.ortools.constraintsolver.main;
//import com.google.ortools.constraintsolver.Assignment;
import com.google.ortools.constraintsolver.*;

import java.util.concurrent.*;
import com.google.protobuf.Duration;

import java.util.logging.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.Arrays;
import java.util.Collections;

public class TSP {

    private int n;
    private static long[][] distanceMatrix;
    private long[][] dp;
    private static final Logger logger = Logger.getLogger(TSP.class.getName());
    private static final String FILE_PATH = "gr17.tsp";
    
    // Klasse zur Speicherung der Lösung
    static class TSPSolution {
        long objectiveValue;
        String route;
        String strategy;

        public TSPSolution(long objectiveValue, String route, String strategy) {
            this.objectiveValue = objectiveValue;
            this.route = route;
            this.strategy = strategy;
        }
    }
    
    public TSP(long[][] distanceMatrix) {
        this.distanceMatrix = distanceMatrix;
        this.n = distanceMatrix.length;  // Number of cities
        this.dp = new long[1 << n][n];     // DP table
        for (long[] row : dp) {
            Arrays.fill(row, (long) -1);         // Initialize DP table with -1 (not visited)
        }
    }

    public long tsp(int mask, int pos) {
        // If all cities are visited, return to the starting city
        if (mask == (1 << n) - 1) {
            return distanceMatrix[pos][0]; // Return to the starting city
        }

        if (dp[mask][pos] != -1) {
            return dp[mask][pos]; // Return memoized result
        }

        long ans = Long.MAX_VALUE;

        // Try to go to every other city
        for (int city = 0; city < n; city++) {
            // If the city has not been visited yet
            if ((mask & (1 << city)) == 0) {
                // Calculate the new answer
                long newAns = distanceMatrix[pos][city] + tsp(mask | (1 << city), city);
                ans = Math.min(ans, newAns); // Choose the minimum cost
            }
        }
        return dp[mask][pos] = ans; // Memoize result
    }

	public static void main(String[] args) throws IOException {
		Loader.loadNativeLibraries();
		try {
			distanceMatrix = TSP.loadNRW1379("/home/andreas/tsplib95/archives/problems/tsp/nrw1379.tsp", 1379);
//        		distanceMatrix = TSP.loadKroA200("/home/andreas/tsplib95/archives/problems/tsp/kroA200.tsp", 200);
//        		distanceMatrix = TSP.loadKroA150("/home/andreas/tsplib95/archives/problems/tsp/kroA150.tsp", 150);
//        		distanceMatrix = TSP.loadKroA100("/home/andreas/tsplib95/archives/problems/tsp/kroA100.tsp", 100);        		
//        		distanceMatrix = TSP.loadPr76("/home/andreas/tsplib95/archives/problems/tsp/pr76.tsp", 76);
//        		distanceMatrix = TSP.loadSt70("/home/andreas/tsplib95/archives/problems/tsp/st70.tsp", 70);
//        		distanceMatrix = TSP.loadBerlin52("/home/andreas/tsplib95/archives/problems/tsp/berlin52.tsp", 52);

//        		distanceMatrix = TSP.loadDantzig42("/home/andreas/tsplib95/archives/problems/tsp/dantzig42.tsp", 42);

//        		distanceMatrix = loadBayg29("/home/andreas/tsplib95/archives/problems/tsp/bayg29.tsp", 29);
			
//        		distanceMatrix = TSP.loadGr120("/home/andreas/tsplib95/archives/problems/tsp/gr120.tsp", 120);
//        		distanceMatrix = TSP.loadGr48("/home/andreas/tsplib95/archives/problems/tsp/gr48.tsp", 48);
//        		distanceMatrix = TSP.loadGr24("/home/andreas/tsplib95/archives/problems/tsp/gr24.tsp", 24);
//        		distanceMatrix = TSP.loadGr21("/home/andreas/tsplib95/archives/problems/tsp/gr21.tsp", 21);
//        		distanceMatrix = TSP.loadGr17("/home/andreas/tsplib95/archives/problems/tsp/gr17.tsp", 17);

			// Print the full integer matrix
//			TSP.printMatrix();
		} catch (IOException e) {
			e.printStackTrace();
		}
		long startTime = System.currentTimeMillis();

		// Use an executor service with a thread pool of 8 cores
		int numCores = 7;
		ExecutorService executor = Executors.newFixedThreadPool(numCores);
		List<Future<?>> futures = new ArrayList<>();

		System.out.println("Starting parallel search with " + numCores + " workers.");

		final List<TSPSolution> allSolutions = Collections.synchronizedList(new ArrayList<>());
		TSPSolution bestSolution = null;
		
		FirstSolutionStrategy.Value[] strategies = { FirstSolutionStrategy.Value.CHRISTOFIDES,
				FirstSolutionStrategy.Value.PARALLEL_CHEAPEST_INSERTION,
				FirstSolutionStrategy.Value.LOCAL_CHEAPEST_INSERTION, FirstSolutionStrategy.Value.PATH_CHEAPEST_ARC, FirstSolutionStrategy.Value.GLOBAL_CHEAPEST_ARC,
				FirstSolutionStrategy.Value.PATH_MOST_CONSTRAINED_ARC, FirstSolutionStrategy.Value.SAVINGS
		};

		for (int i = 0; i < numCores; i++) {
			final FirstSolutionStrategy.Value strategy = strategies[i];
			futures.add(executor.submit(() -> {
				solveTSPInstance(strategy, allSolutions);
			}));
		}

		// Wait for all tasks to complete
		for (Future<?> future : futures) {
			try {
				future.get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}

		executor.shutdown();
		System.out.println("Parallel search finished.");

		
		for (TSPSolution solution : allSolutions) {
			if (bestSolution == null || solution.objectiveValue < bestSolution.objectiveValue) {
				bestSolution = solution;
			}
		}

		if (bestSolution != null) {
			System.out.println("\nOverall best solution found by strategy " + bestSolution.strategy
					+ " with objective value: " + bestSolution.objectiveValue);
			System.out.println("Route: " + bestSolution.route);
		}

		// exhausiveSearch();

		long endTime = System.currentTimeMillis();
		long elapsedTimeMillis = endTime - startTime;
		long totalSeconds = elapsedTimeMillis / 1000;
		long hours = totalSeconds / 3600;
		long minutes = (totalSeconds % 3600) / 60;
		long seconds = totalSeconds % 60;

		logger.info(String.format("Program execution time: %d hours, %d minutes, %d seconds", hours, minutes, seconds));
	}

	private static void printMatrix() {
		for (long[] row : distanceMatrix) {
			System.out.print("{");
			for (long distance : row) {
				System.out.print(String.format("%3d, ", distance));
			}
			System.out.print("},");
			System.out.println();
		}
	}
		private static long[][] loadBayg29(String filePath, int n) throws IOException {
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			String line;
			long[][] d = new long[n][n]; // Assuming 100 cities

            // Skip first few lines until we reach EDGE_WEIGHT_SECTION
            while ((line = br.readLine()) != null) {
                if (line.startsWith("EDGE_WEIGHT_SECTION")) {
                    break;
                }
            }

            // Read the distances into the distance matrix
            for (int i = 0; i < n; i++) {
                line = br.readLine();
                if (line == null || line.startsWith("DISPLAY_DATA_SECTION")) break; // Safety check

                String[] values = line.trim().split("\\s+");
                for (int j = 0; j < values.length; j++) {
                    // Only fill the upper triangle
                    d[i][j + i + 1] = (long) Double.parseDouble(values[j]);
                    // Fill the mirrored lower triangle
                    d[j + i + 1][i] = d[i][j + i + 1];
                }
            }
            br.close();            
			return d;
		}
		
		private static long[][] loadDantzig42(String filePath, int n) throws IOException {
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			String line;
			long[][] d = new long[n][n];

		    // Skip to the EDGE_WEIGHT_SECTION
		    while ((line = br.readLine()) != null) {
		        if (line.startsWith("EDGE_WEIGHT_SECTION")) {
		            break;
		        }
		    }

		    List<Long> values = new ArrayList<>();
		    // Read the distances into a list until the DISPLAY_DATA_SECTION is found
		    while ((line = br.readLine()) != null) {
		        if (line.startsWith("DISPLAY_DATA_SECTION")) {
		            break;
		        }
		        String[] parts = line.trim().split("\\s+");
		        for (String part : parts) {
		            if (!part.isEmpty()) {
		                values.add(Long.parseLong(part));
		            }
		        }
		    }
		    br.close();
		    
			int i = 0;
			int k = 0;
			for (long v : values) {
				if (i < n) { // Ensure it’s within bounds
					d[i][k] = v;
					d[k][i] = v;
					if (v == 0) {
						i++;
						k = 0;
					} else {
						k++;
					}
				}
			}
			return d;
		}
				
	    private static long[][] loadDistanceMatrix(String filePath, int numberOfCities) throws IOException {
	        BufferedReader br = new BufferedReader(new FileReader(filePath));
	        String line;
	        long[][] d = new long[numberOfCities][numberOfCities];

	        // Skip to EDGE_WEIGHT_SECTION
	        while ((line = br.readLine()) != null) {
	            if (line.startsWith("EDGE_WEIGHT_SECTION")) {
	                break;
	            }
	        }

	        List<String> lines = new ArrayList<>();
	        // Read the coordinates
	        while ((line = br.readLine()) != null) {
	            if (line.startsWith("EOF") || line.startsWith("DISPLAY_DATA_SECTION")) {
	                break;
	            }
	            lines.add(line.trim());
	        }
	        br.close();

	        // Combine all lines into a single string
	        String conc = String.join(" ", lines);
	        // Split line into values
	        String[] values = conc.trim().split("\\s+");

	        int i = 0, k = 0;
	        for (String value : values) {
	            long v = (long) Double.parseDouble(value);
	            if (i < numberOfCities) {
	                d[i][k] = v;
	                d[k][i] = v;
	                if (v == 0) {
	                    i++;
	                    k = 0; // Reset k for the next city
	                } else {
	                    k++;
	                }
	            }
	        }
	        return d;
	    }

	    public static long[][] loadGr17(String filePath, int n) throws IOException {
	    	return loadDistanceMatrix(filePath, n);
	    }

	    public static long[][] loadGr21(String filePath, int n) throws IOException {
	    	return loadDistanceMatrix(filePath, n);
	    }
	    
	    public static long[][] loadGr24(String filePath, int n) throws IOException {
	        return loadDistanceMatrix(filePath, n);
	    }
	    
	    public static long[][] loadGr48(String filePath, int n) throws IOException {
	        return loadDistanceMatrix(filePath, n);
	    }
	    
	    public static long[][] loadGr120(String filePath, int n) throws IOException {
	        return loadDistanceMatrix(filePath, n);
	    }

        private static long[][] loadCoordinates(String filePath, int numberOfCities) throws IOException {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;
            long[][] coordinates = new long[numberOfCities][2];
            
            // Skip header until "NODE_COORD_SECTION"
            while ((line = br.readLine()) != null) {
                if (line.startsWith("NODE_COORD_SECTION")) {
                    break;
                }
            }

            // Read the coordinates
            while ((line = br.readLine()) != null) {
                if (line.startsWith("EOF")) {
                    break;
                }
                String[] parts = line.trim().split("\\s+");
                if (parts.length >= 3) {
                    int cityIndex = Integer.parseInt(parts[0]) - 1; // City index from 1
//                    long x = Long.parseLong(parts[1]);
//                    long y = Long.parseLong(parts[2]);
                    long x = (long) Double.parseDouble(parts[1]);
                    long y = (long) Double.parseDouble(parts[2]);
                    coordinates[cityIndex][0] = x;
                    coordinates[cityIndex][1] = y;
                }
            }
            br.close();
            return coordinates;
        }
        
        public static long[][] loadBerlin52(String filePath, int n) throws IOException {
            return TSP.generateDistanceMatrix(loadCoordinates(filePath, n));
        }
        public static long[][] loadSt70(String filePath, int n) throws IOException {
            return TSP.generateDistanceMatrix(loadCoordinates(filePath, n));
        }
        
        public static long[][] loadPr76(String filePath, int n) throws IOException {
            return TSP.generateDistanceMatrix(loadCoordinates(filePath, n));
        }

        public static long[][] loadKroA100(String filePath, int n) throws IOException {
            return TSP.generateDistanceMatrix(loadCoordinates(filePath, n));
        }
        
        public static long[][] loadKroA150(String filePath, int n) throws IOException {
            return TSP.generateDistanceMatrix(loadCoordinates(filePath, n));
        }
        
        public static long[][] loadKroA200(String filePath, int n) throws IOException {
            return TSP.generateDistanceMatrix(loadCoordinates(filePath, n));
        }
        
        public static long[][] loadNRW1379(String filePath, int n) throws IOException {
            return TSP.generateDistanceMatrix(loadCoordinates(filePath, n));
        }
        
        private static long[][] generateDistanceMatrix(long[][] coordinates) {
            int numCities = coordinates.length;
            long[][] distanceMatrix = new long[numCities][numCities];

            for (int i = 0; i < numCities; i++) {
                for (int j = 0; j < numCities; j++) {
                    if (i != j) {
                        distanceMatrix[i][j] = calculateEuclideanDistance(coordinates[i], coordinates[j]);
                    } else {
                        distanceMatrix[i][j] = 0; // Distance to itself is 0
                    }
                }
            }
            return distanceMatrix;
        }

        private static long calculateEuclideanDistance(long[] city1, long[] city2) {
            return Math.round(Math.sqrt(Math.pow(city1[0] - city2[0], 2) + Math.pow(city1[1] - city2[1], 2)));
        }

		private static void solveTSPInstance(FirstSolutionStrategy.Value strategy, List<TSPSolution> solutions) {
			int numNodes = distanceMatrix.length;
			int numVehicles = 1;
			int depot = 0;
			// Initialize a counter for solutions
			final int[] solutionCount = { 0 };

			// Create the Routing Index Manager
			RoutingIndexManager manager = new RoutingIndexManager(numNodes, numVehicles, depot);
			// Variable to store initial solution
			Assignment previousSolution = null;
			// Create the Routing Model for this worker
			RoutingModel routing = new RoutingModel(manager);

			// Register the transit callback using a lambda expression.
			int transitCallbackIndex = routing.registerTransitCallback((long fromIndex, long toIndex) -> {
				int fromNode = manager.indexToNode(fromIndex);
				int toNode = manager.indexToNode(toIndex);
				return distanceMatrix[fromNode][toNode];
			});

			// Set the arc cost evaluator
			routing.setArcCostEvaluatorOfAllVehicles(transitCallbackIndex);

			LocalSearchMetaheuristic.Value[] meta = { LocalSearchMetaheuristic.Value.GUIDED_LOCAL_SEARCH,
					LocalSearchMetaheuristic.Value.SIMULATED_ANNEALING
					// , LocalSearchMetaheuristic.Value.TABU_SEARCH
			};
			List<Long> best = new ArrayList<>();
			// Run the solver multiple times to track different solutions
			for (int i = 0; i < 5; i++) {
				if (previousSolution != null) {
					if (best.get(best.size() - 1).equals(previousSolution.objectiveValue())) {
						System.out.println(strategy + ": Stopping with objValue " + best.get(best.size() - 1));
						break;
					}
					best.add(previousSolution.objectiveValue());
				} else
					best.add(Long.MAX_VALUE);
				System.out.println(strategy + ": Starting run " + i + " with obj=" + best.get(best.size() - 1));
				for (LocalSearchMetaheuristic.Value lsm : meta) { // Execute multiple runs

					// Define your search parameters for this solver
					RoutingSearchParameters searchParameters = main.defaultRoutingSearchParameters().toBuilder()
							.setFirstSolutionStrategy(strategy).setLocalSearchMetaheuristic(lsm)
//						.setLocalSearchMetaheuristic(LocalSearchMetaheuristic.Value.GUIDED_LOCAL_SEARCH)
							// .setLocalSearchMetaheuristic(LocalSearchMetaheuristic.Value.SIMULATED_ANNEALING)
							.setTimeLimit(Duration.newBuilder().setSeconds(360).build())
							.setLnsTimeLimit(Duration.newBuilder().setSeconds(60).build())
//						.setLogSearch(true)
//						.setSolutionLimit(200)
							/*
							 * 90: 60 Worker with strategy AUTOMATIC found solution with objVal: 26524
							 * Worker with strategy PATH_CHEAPEST_ARC found solution with objVal: 26524
							 * Worker with strategy CHRISTOFIDES found solution with objVal: 26525 Worker
							 * with strategy PATH_MOST_CONSTRAINED_ARC found solution with objVal: 26524
							 * Worker with strategy PARALLEL_CHEAPEST_INSERTION found solution with objVal:
							 * 26525 Worker with strategy LOCAL_CHEAPEST_INSERTION found solution with
							 * objVal: 26524
							 */
							.build();

					// Add a solution callback
					routing.addAtSolutionCallback(() -> {
						solutionCount[0]++;
//					if (solutionCount[0] % 100 == 0)
//						System.out.println(strategy + ": Found a new solution! Total solutions: " + solutionCount[0]);
					});

//				if (previousSolution != null) {System.out.println("Starting Worker with strategy " + strategy + " and initial solution with objVal: "
//						+ previousSolution.objectiveValue());}
					// Solve the problem for this worker
					Assignment solution = routing.solveFromAssignmentWithParameters(previousSolution, searchParameters);

					// Handle and store the solution
					if (solution != null) {
						System.out.println("Worker with strategy " + strategy + " found solution with objVal: "
								+ solution.objectiveValue());
						long index = routing.start(0);
						String route = "";
						long routeDistance = 0;
						while (!routing.isEnd(index)) {
							route += manager.indexToNode(index) + " -> ";
							long previousIndex = index;
							index = solution.value(routing.nextVar(index));
							routeDistance += routing.getArcCostForVehicle(previousIndex, index, 0);
						}
						route += manager.indexToNode(index);
//					logger.info("Route for vehicle 0: " + route);
//					logger.info("Route distance: " + routeDistance);

						previousSolution = solution;

						synchronized (solutions) {
							solutions.add(new TSPSolution(solution.objectiveValue(), route, strategy.toString()));
						}
					} else {
						logger.info("No solution found.");
					}

					System.out.println(strategy + ": Number of iterative solutions: " + solutionCount[0]);
				}
			}
		}

		public int[][] submatrix(int[][] matrix, int from, int to) {
			// Create a submatrix using Arrays.copyOf
			if (from < 0 || to >= matrix.length)
				return matrix;
			else {
				int nCities = to - from + 1;
				int[][] subMatrix = new int[nCities][nCities];
				for (int i = 0; i < nCities; i++) {
					subMatrix[i] = Arrays.copyOf(matrix[i], nCities); // Copy each row directly
				}
				return subMatrix;
			}
		}

    public static void exhausiveSearch() {
        
        Runtime runtime = Runtime.getRuntime();

        // Allocating memory details
        long maxMemory = runtime.maxMemory();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();

        // Printing memory details in MB
        System.out.println("Max Memory (MB): " + maxMemory / (1024 * 1024));
        System.out.println("Total Memory (MB): " + totalMemory / (1024 * 1024));
        System.out.println("Free Memory (MB): " + freeMemory / (1024 * 1024));
        System.out.println("Used Memory (MB): " + (totalMemory - freeMemory) / (1024 * 1024));
        
        Loader.loadNativeLibraries();
        // Your OR-Tools code here

        TSP tspSolver = new TSP(distanceMatrix);
        long result = tspSolver.tsp(1, 0); // Start with the first city visited
        System.out.println("Minimum cost of visiting all cities: " + result);
    }
}
