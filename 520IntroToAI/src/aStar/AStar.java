package aStar;

import common.Heuristics;
import common.Node;
import common.Utility;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Question 4, 5
 * Entire gridworld is known
 */
class AStar {

    int[][] w;

    int[][] grid;

    int[] rows = {0, 0, 1, -1};

    int[] cols = {1, -1, 0, 0};

    Heuristics heuristics;

    long[] analysis;

    public AStar(int[][] w, int[][] grid, Heuristics h) {
        this.w = w;
        this.grid = grid;
        this.heuristics = h;
    }

    /**
     * Question 5
     * Entire gridworld is known
     */
    public long[] getTimeAndSteps(int r, int c, int targetR, int targetC, List<Node> par) {
        long start = System.nanoTime();
        int m = grid.length, n = grid[0].length;
        boolean[][] visited = new boolean[m][n];

        int[][] minWeight = new int[m][n];
        for (int[] weight : minWeight) {
            Arrays.fill(weight, Integer.MAX_VALUE);
        }

        Queue<Node> queue =
            new PriorityQueue<>(
                (n1, n2) -> (
                    (n1.w + getH(n1.r, n1.c, targetR, targetC)) -
                        (n2.w + getH(n2.r, n2.c, targetR, targetC))
                ));

        Node node = new Node(r, c, w[r][c], par);
        queue.offer(node);

        while (!queue.isEmpty()) {
            Node current = queue.poll();

            if (visited[current.r][current.c]) {
                continue;
            }
            if (current.r == targetR && current.c == targetC) {
                return new long[] {System.nanoTime() - start, current.parList.size()};
            }

            visited[current.r][current.c] = true;

            for (int i = 0; i < rows.length; i++) {
                int row = current.r + rows[i];
                int col = current.c + cols[i];

                if (isValid(row, col) && !visited[row][col]) { //we need to save just the blockers globally and not all the visisted nodes

                    int weight = current.w + w[row][col];

                    if (weight < minWeight[row][col]) {
                        minWeight[row][col] = weight;

                        List<Node> ancestors = new ArrayList<>(current.parList);
                        ancestors.add(current);
                        Node neighbor = new Node(row, col, weight, ancestors);

                        queue.offer(neighbor);
                    }
                }
            }
        }
        return new long[] {System.nanoTime() - start, 0};
    }

    private int getH(int r, int c, int targetR, int targetC) {
        switch (heuristics) {
            case MANHATTAN:
                int dis = Math.abs(targetC - c) + Math.abs(targetR - r);
                return dis;
            case EUCLIDEAN:
                int c1 = Math.abs(targetC - c);
                int c2 = Math.abs(targetR - r);
                return (int) Math.sqrt(Math.pow(c1, 2) + Math.pow(c2, 2));
            case CHEBYSHEV:
                return Math.max(Math.abs(targetC - c), Math.abs(targetR - r));
            default:
                return 0;
        }
    }

    private boolean isValid(int row, int col) {
        return row >= 0 && row < grid.length &&
            col >= 0 && col < grid[0].length && grid[row][col] != Utility.BLOCKED;
    }

}