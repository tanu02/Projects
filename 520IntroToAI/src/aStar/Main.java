package aStar;

import common.Heuristics;
import common.Utility;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Main {
    static int srcC = 0, srcR = 0, targetR = 100, targetC = 100;

    static int m = 101, n = 101;

    static int iterations = 100;

    public static void main(String[] args) {

        getTimeAndSteps();
//        getSolvability(); // q4
    }

    private static void getSolvability() {
        Map<Double, Double> solvability = new HashMap<>();

        int unsolved = 0;
        for (double prob : Utility.probListAStar) {

            for (int i = 0; i < iterations; i++) {
                int[][] grid = getGrid(prob);
                int[][] weight = getWeight();

                AStar sol = new AStar(weight, grid, Heuristics.MANHATTAN);
                long[] analysis = sol.getTimeAndSteps(srcR, srcC, targetR, targetC, new ArrayList<>());
                if (analysis[1] == 0) {
                    unsolved++;// 0 means not reachable
                }

            }
            System.out.println("Probability : " + prob + " percentage time solved: " + (iterations - unsolved) + "%");
            unsolved = 0;
        }
    }

    private static int[][] getGrid(double prob) {
        int[][] grid = Utility.getGrid(prob, m, n);
        grid[targetR][targetC] = Utility.UNBLOCKED;
        grid[srcR][srcC] = Utility.UNBLOCKED;
        return grid;
    }

    private static int[][] getWeight() {
        int[][] weight = new int[m][n];
        for (int[] w : weight) {
            Arrays.fill(w, 1);
        }
        return weight;
    }

    private static void getTimeAndSteps() {

        for (double prob : Utility.probListAStar) {
            System.out.println("Prob " + prob);
            for (Heuristics h : Heuristics.values()) {
                long time = 0, steps = 0;
                long[] analysis;
                int solvedCounter = 0;

                for (int i = 0; i < iterations; i++) {
                    int[][] grid = getGrid(prob);
                    int[][] weight = getWeight();

                    AStar sol = new AStar(weight, grid, h);
                    analysis = sol.getTimeAndSteps(srcR, srcC, targetR, targetC, new ArrayList<>());

                    if (analysis[1] != 0) {
                        time += analysis[0] / 1000;//micro sec sum,  for 100 iterations
                        steps += analysis[1];
                        solvedCounter++;
                    }
                }
                if (solvedCounter != 0) {
                    System.out.println(h + " Time " + time / solvedCounter + ", steps " + steps / solvedCounter);
                }
            }
        }
    }
}
