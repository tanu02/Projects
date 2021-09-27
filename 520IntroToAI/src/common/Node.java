package common;

import java.util.List;

public class Node {

    public int r, c, w;

    public List<Node> parList;

    public Node(int r, int c, int w, List<Node> par) {
        this.r = r;
        this.c = c;
        this.parList = par;
        this.w = w;
    }

}
