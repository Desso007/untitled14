package hw;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public interface Project2 {
    Maze generate(int height, int width);
}

record Cell(int row, int col, Type type) {
    public enum Type { WALL, PASSAGE }
}

class Main {

    public static void main(String[] args) {
        Project2 generator = new RecursiveBacktrackingGenerator();
        Solver solver = new DepthFirstSolver();
        Renderer renderer = new ConsoleRenderer();

        // Generate a maze
        Maze maze = generator.generate(10, 10);

        // Print the maze
        System.out.println(renderer.render(maze));

        // Solve the maze
        Coordinate start = new Coordinate(0, 0);
        Coordinate end = new Coordinate(maze.getHeight() - 1, maze.getWidth() - 1);
        List<Coordinate> path = solver.solve(maze, start, end);

        if (path != null) {
            // Print the maze with the path
            System.out.println(renderer.render(maze, path));

            // Print the coordinates of the path
            for (Coordinate coordinate : path) {
                System.out.println(coordinate.row() + ", " + coordinate.col());
            }
        } else {
            System.out.println("No path found.");
        }
    }
}