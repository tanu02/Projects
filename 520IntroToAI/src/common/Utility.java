package common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Utility {

    public static int BLOCKED = 2;
    public static int UNBLOCKED = 1;
    public static int UNVISITED = 0;


    public static List<Double> probListAStar = Arrays.asList(.0, .0, .05, .1, .15, .2, .25, .3, .32, .33, .35, .4);
    public static List<Double> probListRepeatedAStar = Arrays.asList(.0, .03, .06, .09, .12, .15, .18, .21, .24, .27, .3, .33);

    public static List<int[]> constructPath(Node node) {
        List<int[]> output = new ArrayList<>();
        node.parList.forEach(parNode -> {
            output.add(new int[] {parNode.r, parNode.c});
        });
        output.add(new int[] {node.r, node.c});
        return output;
    }

    public static int[][] getGrid(double prob, int m, int n) {
        int[][] grid = new int[m][n];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (generateRandom() <= prob) { // unvisited node
                    grid[i][j] = BLOCKED;
                }
            }
        }
        return grid;
    }

    public static double generateRandom() {
        double  ran = Math.random() * 1;
        return ran;
    }

    public static void printPlot(Map<Double, Map<Heuristics, long[]>> plot) {
        plot.forEach((prob, map) -> {
            System.out.println(prob);
            map.forEach((h, analysis) ->
                System.out.println(h + " time " + analysis[0] + " steps " + analysis[1]));
        });
    }
}


