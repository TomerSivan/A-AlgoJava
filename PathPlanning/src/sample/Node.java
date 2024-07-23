package sample;

import java.util.ArrayList;
import java.util.List;

public class Node {
    public int x; // x-coordinate of the node
    public int y; // y-coordinate of the node
    public List<Node> neighbors; // list of neighboring nodes
    public double g; // cost of getting from the start node to this node
    public double h; // estimated cost of getting from this node to the goal node
    public Node parent; // the node from which this node was discovered

    // Constructor to initialize a node at (x, y)
    public Node(int x, int y) {
        this.x = x;
        this.y = y;
        this.neighbors = new ArrayList<>();
        this.g = Double.POSITIVE_INFINITY; // initialize unknown cost
        this.h = 0; // initialize to 0 since we don't know the goal node yet
        this.parent = null;
    }

    // Getter for x-coordinate
    public int getX() {
        return this.x;
    }

    // Getter for y-coordinate
    public int getY() {
        return this.y;
    }

    // Add a neighbor to this node's neighbors list
    public void addNeighbor(Node neighbor) {
        this.neighbors.add(neighbor);
    }

    // Getter for the cost from the start node to this node
    public double getGScore() {
        return this.g;
    }

    // Getter for the total estimated cost (f = g + h)
    public double getFScore() {
        return this.g + this.h;
    }

    // Getter for the parent node
    public Node getParent() {
        return this.parent;
    }

    // Setter for the parent node
    public void setParent(Node parent) {
        this.parent = parent;
    }

    // Setter for the cost from the start node to this node
    public void setGScore(double g) {
        this.g = g;
    }

    // Setter for the total estimated cost (f = g + h)
    public void setFScore(double f) {
        this.g = f - this.h;
        this.h = f - this.g;
    }
}