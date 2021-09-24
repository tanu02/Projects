import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

class GridWorld {

    int[][] w;
    int[][] grid;
    int[] rows = {0, 0, 1, -1};
    int[] cols = {1, -1, 0, 0};

    static int BLOCKER = 2;

    boolean[][] knowledgeGrid;

    public GridWorld(int[][] w, int[][] grid) {
        this.w = w;
        this.grid = grid;
        knowledgeGrid = new boolean[grid.length][grid[0].length];
    }

    public List<int[]> findPath(int r, int c, int targetR, int targetC, List<Node> par) {

        int m = grid.length, n = grid[0].length;
        boolean[][] visited = new boolean[m][n];

        int[][] minWeight = new int[m][n];
        for (int[] weight : minWeight) {
            Arrays.fill(weight, Integer.MAX_VALUE);
        }

        Queue<Node> queue =
            new PriorityQueue<>(
                (n1, n2) -> (
                    (getH(n1.r, n1.c, targetR, targetC) + n1.w) -
                        (getH(n2.r, n2.c, targetR, targetC) + n2.w)
                ));

        Node node = new Node(r, c, w[r][c], par);
        queue.offer(node);

        while (!queue.isEmpty()) {
            Node current = queue.poll();

            if (visited[current.r][current.c]) {
                continue;
            }
            if (current.r == targetR && current.c == targetC) {
                return constructPath(current);
            }

            visited[current.r][current.c] = true;
            boolean blockerSeen = false;
            for (int i = 0; i < rows.length; i++) {
                int row = current.r + rows[i];
                int col = current.c + cols[i];

                if (isValid(row, col) && !visited[row][col]) { //we need to save just the blockers globally and not all the visisted nodes
                    if (knowledgeGrid[row][col]) {
                        continue;
                    }
                    if (isBlocker(row, col)) {
                        blockerSeen = true;
                        continue;
                    }
                    int weight = current.w + w[row][col];

                    if (weight < minWeight[row][col]) {
                        minWeight[row][col] = weight;

                        List<Node> ancestors  = new ArrayList<>(current.parList);
                        ancestors.add(current);
                        Node neighbor = new Node(row, col, weight, ancestors);

                        queue.offer(neighbor);
                    }
                }
            }
            if(blockerSeen) {
                List<Node> ancestorList = current.parList; ancestorList.add(current);
                return findPath(current.r, current.c, targetR, targetC, ancestorList);
            }
        }
        return Collections.emptyList();
    }

    private boolean isBlocker(int row, int col) {
        if (grid[row][col] == BLOCKER) {
            knowledgeGrid[row][col] = true;
            return true;
        }
        return false;
    }

    private boolean isValid(int row, int col) {
        return row >= 0 && row < grid.length &&
            col >= 0 && col < grid[0].length;
    }

    private List<int[]> constructPath(Node node) {
        List<int[]> output = new ArrayList<>();
        node.parList.forEach(parNode -> {
            output.add(new int[] {parNode.r, parNode.c});
        });
        output.add(new int[] {node.r, node.c});
        return output;
    }

    private int getH(int r, int c, int targetR, int targetC) {
        return Math.abs(targetC - c) + Math.abs(targetR - r);
    }
}

class Node {
    int r, c, w;

    List<Node> parList;

    Node(int r, int c, int w, List<Node> par) {
        this.r = r;
        this.c = c;
        this.parList = par;
        this.w = w;
    }
}

class Main {
    public static void main(String[] args) {

        int[][] grid5 = {
            {0,0,2},
            {0,2,0},
            {0,0,0}
        };
        int[][] weight5 = {
            {4,1,1},
            {2,1,1},
            {2,1,1}
        };

        int[][] grid = {{0, 0}, {0, 0}};
        int[][] weight = {{1, 1}, {1, 1}};
        GridWorld gridWorld = new GridWorld(weight, grid);
        printPath(gridWorld.findPath(0, 0, 1, 1, new ArrayList<>())); // expected -> [(0,0), (0,1), (1,1)] or expected -> [(0,0), (1,0), (1,1)]

        int[][] grid2 = {{0, 0}, {0, 0}};
        int[][] weight2 = {{1, 1}, {2, 1}};
        GridWorld gridWorld2 = new GridWorld(weight2, grid2);
        printPath(gridWorld2.findPath(0, 0, 1, 1, new ArrayList<>())); // expected -> [(0,0), (0,1), (1,1)]

        int[][] grid4 = {{0, 0}, {0, 0}};
        int[][] weight4 = {{1, 2}, {1, 1}};
        GridWorld gridWorld4 = new GridWorld(weight4, grid4);
        printPath(gridWorld4.findPath(0, 0, 1, 1, new ArrayList<>())); // expected -> [(0,0), (1,0), (1,1)]


        int[][] grid3 = {{0, GridWorld.BLOCKER}, {1, 0}};
        int[][] weight3 = {{1, 1}, {1, 1}};
        GridWorld gridWorld3 = new GridWorld(weight3, grid3);
        printPath(gridWorld3.findPath(0, 0, 1, 1, new ArrayList<>())); // empty list

        int[][] grid6 = new int[101][101];
        grid6[0][100] = GridWorld.BLOCKER;
        int[][] weight6 = new int[101][101];
        GridWorld gridWorld6 = new GridWorld(weight6, grid6);
        printPath(gridWorld6.findPath(0, 0, 100, 100, new ArrayList<>())); // empty list


    }

    private static void printPath(List<int[]> pathList){
        pathList.forEach(path -> System.out.print(Arrays.toString(path)));
        System.out.println();
    }
}