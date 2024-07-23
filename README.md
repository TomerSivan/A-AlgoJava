A* Pathfinding Algorithm Java
This project is a graphical representation and implementation of the A* pathfinding algorithm using Java and JavaFX lib. It was created to experiment with the A* algorithm and pathfinding for robotics implementation, the algorithm is used in many applications such as game development and robotics for finding the shortest path between two points.

Overview
The A* algorithm is a popular pathfinding and graph traversal algorithm. It is used to find the shortest path from a start node to a goal node in a weighted graph. The algorithm maintains a priority queue of nodes to be explored sorted by the estimated total cost of reaching the goal.

Key Concepts
g score: The actual cost from the start node to the current node.
h score: The heuristic estimate of the cost from the current node to the goal node. It represents an optimistic guess of the remaining cost to reach the goal.
f score: The total estimated cost of a node, calculated as f = g + h.

Running the Project
Install JavaFX: Ensure you have JavaFX installed and properly configured in your development environment.
Add / Remove / Change the obstacles by changing the list of Rectangle objects in the isOccupied method.
Compile and Run: Compile and run the Main class to start the JavaFX application. The application will display a graphical representation of the map and the shortest path it found using the A* algorithm.
