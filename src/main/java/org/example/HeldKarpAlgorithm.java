package org.example;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class HeldKarpAlgorithm {

    private static int n; // Number of cities
    private static int[][] coordinates;

    public HeldKarpAlgorithm(int[][] distances) {
        this.n = distances.length;
        this.coordinates = distances;
    }

    private static int[][] distance; // Distance matrix between cities
    private static int[][] path; // Path matrix for reconstructing the optimal solution

    public static void main(String[] args) throws IOException {
        // Read city information from text file
        readCityInfoFromFile("D:\\TSP_107.txt");

        // Calculate shortest Hamiltonian cycle using Held-Karp algorithm
        int shortestPathLength = heldKarp();
        System.out.println("Shortest Hamiltonian cycle length: " + shortestPathLength);
        System.out.println("Shortest Hamiltonian cycle: ");
        int[] shortestPath = getShortestPath();
        for (int i = 0; i < shortestPath.length; i++) {
            System.out.print(shortestPath[i] + " ");
        }
    }

    // Read city information from text file
    public static void readCityInfoFromFile(String fileName) throws IOException {

        ArrayList<int[]> cities = new ArrayList<>();
        try {
            File file = new File(fileName); // Use the fileName parameter instead of hardcoding the file path
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(" ");
                if (parts.length >= 3) { // Check if parts array has at least 3 elements
                    int[] city = new int[] {Integer.parseInt(parts[1]), Integer.parseInt(parts[2])};
                    cities.add(city);
                } else {
                    System.err.println("Invalid line format: " + line); // Optionally, you can handle invalid line formats here
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    // Compute the distances between all pairs of cities
        coordinates = new int[cities.size()][cities.size()];
        for (int i = 0; i < cities.size(); i++) {
            for (int j = i+1; j < cities.size(); j++) {
                int distance = (int) Math.round(Math.sqrt(Math.pow(cities.get(i)[0]-cities.get(j)[0], 2) + Math.pow(cities.get(i)[1]-cities.get(j)[1], 2)));
                coordinates[i][j] = distance;
                coordinates[j][i] = distance;
            }
        }
    }

    // Calculate Euclidean distance between two cities based on their coordinates
    public static int calculateDistance(int city1, int city2) {
        int x1 = coordinates[city1][0];
        int y1 = coordinates[city1][1];
        int x2 = coordinates[city2][0];
        int y2 = coordinates[city2][1];
        return (int) Math.round(Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1)));
    }

    // Held-Karp algorithm for calculating shortest Hamiltonian cycle
    public static int heldKarp() {
        distance = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                distance[i][j] = calculateDistance(i, j);
            }
        }

        int[][] dp = new int[1 << n][n];
        path = new int[1 << n][n];

        for (int i = 0; i < (1 << n); i++) {
            for (int j = 0; j < n; j++) {
                dp[i][j] = Integer.MAX_VALUE;
            }
        }

        dp[1][0] = 0;

        // Start the dynamic programming loop
        for (int mask = 1; mask < (1 << n); mask++) {
            for (int last = 0; last < n; last++) {
                // If the current city is not included in the mask, skip it
                if ((mask & (1 << last)) == 0) {
                    continue;
                }

                // For each possible next city
                for (int next = 0; next < n; next++) {
                    // If the next city is already included in the mask, skip it
                    if ((mask & (1 << next)) != 0) {
                        continue;
                    }

                    // Update the cost of reaching the next city through the current city
                    int newMask = mask | (1 << next);
                    int newDistance = dp[mask][last] + distance[last][next];
                    if (dp[newMask][next] > newDistance) {
                        dp[newMask][next] = newDistance;
                        path[newMask][next] = last;
                    }
                }
            }
        }

        // Find the shortest Hamiltonian cycle
        int shortestDistance = Integer.MAX_VALUE;
        int last = -1;
        for (int i = 0; i < n; i++) {
            int newDistance = dp[(1 << n) - 1][i] + distance[i][0];
            if (shortestDistance > newDistance) {
                shortestDistance = newDistance;
                last = i;
            }
        }

        // Construct the shortest Hamiltonian cycle
        int[] hamiltonianCycle = new int[n + 1];
        hamiltonianCycle[n] = 0;
        for (int i = n - 1; i >= 0; i--) {
            hamiltonianCycle[i] = last;
            last = path[(1 << n) - 1][last];
        }

        return shortestDistance;
    }

    // Get the shortest Hamiltonian cycle (optimal solution)
    public static int[] getShortestPath() {
        int[] shortestPath = new int[n];
        int mask = (1 << n) - 1;
        int city = 0;
        for (int i = n - 1; i >= 1; i--) {
            int prevCity = path[mask][city];
            shortestPath[i] = prevCity;
            mask = mask ^ (1 << city);
            city = prevCity;
        }
        shortestPath[0] = 0; // Ending city (same as starting city)

        return shortestPath;
    }
}
