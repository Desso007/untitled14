package test;

import hw.*;
import org.junit.jupiter.api.Assertions;

import java.util.List;

public class Test {

    @org.junit.jupiter.api.Test
    public void testMazeGeneration() {
        Project2 generator = new RecursiveBacktrackingGenerator();
        Maze maze = generator.generate(10, 10);

        Assertions.assertEquals(10, maze.getHeight());
        Assertions.assertEquals(10, maze.getWidth());
        Assertions.assertNotNull(maze.getGrid());
    }

    @org.junit.jupiter.api.Test
    public void testMazeSolver() {
        Project2 generator = new RecursiveBacktrackingGenerator();
        Solver solver = new DepthFirstSolver();

        Maze maze = generator.generate(10, 10);
        Coordinate start = new Coordinate(0, 0);
        Coordinate end = new Coordinate(maze.getHeight() - 1, maze.getWidth() - 1);

        List<Coordinate> path = solver.solve(maze, start, end);

        Assertions.assertNotNull(path);
    }

    @org.junit.jupiter.api.Test
    public void testRenderer() {
        Project2 generator = new RecursiveBacktrackingGenerator();
        Renderer renderer = new ConsoleRenderer();

        Maze maze = generator.generate(10, 10);

        String renderedMaze = renderer.render(maze);

        // Add your assertions here to validate the rendered maze string

    }
}