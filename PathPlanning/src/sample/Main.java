package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // drawing canvas
        Canvas canvas = new Canvas(540, 270);

        // graphics context of the canvas
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // nodes representation of the map
        List<Node> nodes = createGraph();

        System.out.println(nodes.size());

        // Draws a graphic representation of the map
        drawMap(gc, nodes, 10);

        // start and end nodes of the graph for the pathfinding
        Node start = getNode(1, 10, nodes);
        Node goal = getNode(53, 25, nodes);

        // Find the path from start to goal
        List<Node> path = findPath(start, goal, nodes);

        // Draw the path on the map
        drawPath(gc, nodes, path, 10);

        // Scene display
        Scene scene = new Scene(new StackPane(canvas), 540, 270);
        primaryStage.setTitle("Map Display");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Draw a node based map on canvas graphics context
     */
    public void drawMap(GraphicsContext gc, List<Node> nodes, int scale) {
        for (int x = 0; x < 540; x += scale) {
            for (int y = 0; y < 270; y += scale) {
                boolean isOccupied = false;
                for (Node node : nodes) {
                    if (node.x == x / scale && node.y == y / scale) {
                        isOccupied = true;
                        break;
                    }
                }
                gc.setFill(isOccupied ? Color.BLACK : Color.RED);
                gc.fillRect(x, y, scale, scale);
            }
        }
    }

    /**
     * Draw the path on the map.
     */
    public void drawPath(GraphicsContext gc, List<Node> nodes, List<Node> path, int scale) {
        for (Node node : nodes) {
            if (isOccupied(node.x, node.y)) {
                gc.setFill(Color.BLACK);
            }
            gc.fillOval(node.x * 10, node.y * 10, scale, scale);
        }

        gc.setStroke(Color.BLUE);
        gc.setLineWidth(2);
        gc.beginPath();
        gc.moveTo(path.get(0).x * 10 + 5, path.get(0).y * 10 + 5);
        for (int i = 0; i < path.size() - 1; i++) {
            Node node1 = path.get(i);
            Node node2 = path.get(i + 1);
            double cx = (node1.x * 10 + 5 + node2.x * 10 + 5) / 2.0;
            double cy = (node1.y * 10 + 5 + node2.y * 10 + 5) / 2.0;
            gc.quadraticCurveTo(node1.x * 10 + 5, node1.y * 10 + 5, cx, cy);
        }
        gc.stroke();
    }

    /**
     * Get the node at the specified coordinates.
     */
    public Node getNode(int x, int y, List<Node> nodes) {
        for (Node node : nodes) {
            if (node.x == x && node.y == y) {
                return node;
            }
        }
        return null;
    }

    /**
     * checks if the specified node is occupied by an obstacle
     */
    public boolean isOccupied(int x, int y) {
        List<Rectangle> obstacles = new ArrayList<>();
        obstacles.add(new Rectangle(10, 6, 4, 28)); // First Obstacle
        obstacles.add(new Rectangle(30, 12, 4, 10)); // Second Obstacle
        obstacles.add(new Rectangle(0, 0, 54, 5)); // Third Obstacle
        obstacles.add(new Rectangle(0, 16, 50, 12)); // Fourth Obstacle

        for (Rectangle obstacle : obstacles) {
            if (obstacle.contains(x, y)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Create a graph of nodes avoiding obstacles
     */
    public List<Node> createGraph() {
        List<Node> nodes = new ArrayList<>();
        double FIELD_WIDTH = 54;
        double FIELD_HEIGHT = 27;

        for (int x = 0; x < FIELD_WIDTH; x++) {
            for (int y = 0; y < FIELD_HEIGHT; y++) {
                if (!isOccupied(x, y)) {
                    Node node = new Node(x, y);
                    nodes.add(node);

                    for (int x2 = 0; x2 < FIELD_WIDTH; x2++) {
                        for (int y2 = 0; y2 < FIELD_HEIGHT; y2++) {
                            if (x2 == x && y2 == y) continue;
                            if (!isOccupied(x2, y2)) {
                                boolean isStraight = (x == x2 || y == y2);
                                if (isStraight || !isPathBlocked(x, y, x2, y2)) {
                                    node.addNeighbor(getNode(x2, y2, nodes));
                                }
                            }
                        }
                    }
                }
            }
        }

        return nodes;
    }

    /**
     * Check if there is an obstacle on the path
     */
    private boolean isPathBlocked(int x1, int y1, int x2, int y2) {
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int x = x1;
        int y = y1;
        int n = 1 + dx + dy;
        int x_inc = (x2 > x1) ? 1 : -1;
        int y_inc = (y2 > y1) ? 1 : -1;
        int error = dx - dy;
        dx *= 2;
        dy *= 2;

        for (; n > 0; --n) {
            if (isOccupied(x, y)) {
                return true;
            }
            if (error > 0) {
                x += x_inc;
                error -= dy;
            } else {
                y += y_inc;
                error += dx;
            }
        }

        return false;
    }

    /**
     * Find the path from startNode to endNode using A* algorithm
     */
    public List<Node> findPath(Node startNode, Node endNode, List<Node> nodeList) {
        startNode.setGScore(0);
        startNode.setFScore(getHeuristicDistance(startNode, endNode));
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingDouble(Node::getFScore));
        Set<Node> closedSet = new HashSet<>();
        openSet.add(startNode);

        while (!openSet.isEmpty()) {
            Node currentNode = openSet.poll();

            if (currentNode.equals(endNode)) {
                List<Node> path = new ArrayList<>();
                path.add(currentNode);
                while (currentNode.getParent() != null) {
                    currentNode = currentNode.getParent();
                    path.add(currentNode);
                }
                Collections.reverse(path);
                return path;
            }

            closedSet.add(currentNode);

            for (Node neighbor : getNeighbors(currentNode, nodeList)) {
                if (closedSet.contains(neighbor)) continue;

                double tentativeGScore = currentNode.getGScore() + getDistance(currentNode, neighbor);
                if (tentativeGScore < neighbor.getGScore()) {
                    neighbor.setParent(currentNode);
                    neighbor.setGScore(tentativeGScore);
                    neighbor.setFScore(tentativeGScore + getHeuristicDistance(neighbor, endNode));
                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor);
                    }
                }
            }
        }

        return null;
    }

    /**
     * Gets the neighbors of a node
     */
    private List<Node> getNeighbors(Node node, List<Node> nodeList) {
        int x = node.getX();
        int y = node.getY();
        List<Node> neighbors = new ArrayList<>();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                int nx = x + dx;
                int ny = y + dy;
                Node neighbor = nodeList.stream()
                        .filter(n -> n.getX() == nx && n.getY() == ny)
                        .findFirst().orElse(null);
                if (neighbor != null && !isOccupied(nx, ny)) {
                    neighbors.add(neighbor);
                }
            }
        }
        return neighbors;
    }

    /**
     * Calculate the distance between two nodes
     */
    private double getDistance(Node node1, Node node2) {
        int dx = node1.getX() - node2.getX();
        int dy = node1.getY() - node2.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Calculate the heuristic distance between two nodes
     */
    private double getHeuristicDistance(Node node1, Node node2) {
        int dx = Math.abs(node1.getX() - node2.getX());
        int dy = Math.abs(node1.getY() - node2.getY());
        return Math.sqrt(dx * dx + dy * dy);
    }

    public static void main(String[] args) {
        launch(args);
    }
}