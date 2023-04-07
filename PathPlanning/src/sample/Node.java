package sample;

import java.util.ArrayList;
import java.util.List;

public class Node {
    public int x;
    public int y;
    public List<Node> neighbors;
    public double g; // cost of getting from the start node to this node
    public double h; // estimated cost of getting from this node to the goal node
    public Node parent; // the node from which this node was discovered

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
        this.neighbors = new ArrayList<>();
        this.g = Double.POSITIVE_INFINITY; // initialize to "infinity" to indicate unknown cost
        this.h = 0; // initialize to 0 since we don't know the goal node yet
        this.parent = null; // initialize to null since we haven't discovered this node yet
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void addNeighbor(Node neighbor) {
        this.neighbors.add(neighbor);
    }

    public double getGScore() {
        return this.g;
    }

    public double getFScore() {
        return this.g + this.h;
    }

    public Node getParent() {
        return this.parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public void setGScore(double g) {
        this.g = g;
    }

    public void setFScore(double f) {
        this.g = f - this.h;
        this.h = f - this.g;
    }
}
