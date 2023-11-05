package hw;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public interface Project2 {
    Maze generate(int height, int width);
}

interface Solver {
    List<Coordinate> solve(Maze maze, Coordinate start, Coordinate end);
}

interface Renderer {
    String render(Maze maze);
    String render(Maze maze, List<Coordinate> path);
}

final class Maze {
    private final int height;
    private final int width;
    private final Cell[][] grid;

    public Maze(int height, int width, Cell[][] grid) {
        this.height = height;
        this.width = width;
        this.grid = grid;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Cell[][] getGrid() {
        return grid;
    }
}

record Cell(int row, int col, Type type) {
    public enum Type { WALL, PASSAGE }
}

record Coordinate(int row, int col) {}

class RecursiveBacktrackingGenerator implements Project2 {

    @Override
    public Maze generate(int height, int width) {
        Cell[][] grid = new Cell[height][width];

        // Initialize all cells as walls
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                grid[i][j] = new Cell(i, j, Cell.Type.WALL);
            }
        }

        // Generate the maze using recursive backtracking algorithm
        generateMaze(grid, 0, 0);

        return new Maze(height, width, grid);
    }

    private void generateMaze(Cell[][] grid, int row, int col) {
        grid[row][col] = new Cell(row, col, Cell.Type.PASSAGE);

        // Get random directions
        List<Direction> directions = new ArrayList<>();
        directions.add(Direction.UP);
        directions.add(Direction.DOWN);
        directions.add(Direction.LEFT);
        directions.add(Direction.RIGHT);
        Random random = new Random();

        // Shuffle the directions randomly
        for (int i = 0; i < 4; i++) {
            int j = random.nextInt(4);
            Direction temp = directions.get(i);
            directions.set(i, directions.get(j));
            directions.set(j, temp);
        }

        // Explore each direction recursively
        for (Direction direction : directions) {
            int newRow = row + direction.getRowOffset();
            int newCol = col + direction.getColOffset();

            if (isValidCell(grid, newRow, newCol) && grid[newRow][newCol].type() == Cell.Type.WALL) {
                int wallRow = row + direction.getRowOffset() / 2;
                int wallCol = col + direction.getColOffset() / 2;

                grid[wallRow][wallCol] = new Cell(wallRow, wallCol, Cell.Type.PASSAGE);

                generateMaze(grid, newRow, newCol);
            }
        }
    }

    private boolean isValidCell(Cell[][] grid, int row, int col) {
        return row >= 0 && row < grid.length && col >= 0 && col < grid[0].length;
    }

    private enum Direction {
        UP(-1, 0),
        DOWN(1, 0),
        LEFT(0, -1),
        RIGHT(0, 1);

        private final int rowOffset;
        private final int colOffset;

        Direction(int rowOffset, int colOffset) {
            this.rowOffset = rowOffset;
            this.colOffset = colOffset;
        }

        public int getRowOffset() {
            return rowOffset;
        }

        public int getColOffset() {
            return colOffset;
        }
    }
}

class DepthFirstSolver implements Solver {

    @Override
    public List<Coordinate> solve(Maze maze, Coordinate start, Coordinate end) {
        Cell[][] grid = maze.getGrid();

        List<Coordinate> path = new ArrayList<>();

        boolean[][] visited = new boolean[maze.getHeight()][maze.getWidth()];

        if (dfs(grid, visited, start.row(), start.col(), end.row(), end.col(), path)) {
            return path;
        } else {
            return null; // No path found
        }
    }

    private boolean dfs(Cell[][] grid, boolean[][] visited, int row, int col, int endRow, int endCol, List<Coordinate> path) {
        if (row == endRow && col == endCol) {
            path.add(new Coordinate(row, col));
            return true;
        }

        if (!isValidCell(grid, visited, row, col)) {
            return false;
        }

        visited[row][col] = true;

        if (dfs(grid, visited, row - 1, col, endRow, endCol, path)) { // Up
            path.add(new Coordinate(row,col));
            return true;
        }

        if (dfs(grid, visited,row + 1,col,endRow,endCol,path)) { // Down
            path.add(new Coordinate(row,col));
            return true;
        }

        if (dfs(grid ,visited,row ,col - 1,endRow,endCol,path)) { // Left
            path.add(new Coordinate(row,col));
            return true;
        }

        if (dfs(grid ,visited,row ,col + 1,endRow,endCol,path)) { // Right
            path.add(new Coordinate(row,col));
            return true;
        }

        return false;
    }

    private boolean isValidCell(Cell[][] grid, boolean[][] visited, int row, int col) {
        return row >= 0 && row < grid.length && col >= 0 && col < grid[0].length &&
                grid[row][col].type() == Cell.Type.PASSAGE && !visited[row][col];
    }
}

class ConsoleRenderer implements Renderer {

    @Override
    public String render(Maze maze) {
        StringBuilder sb = new StringBuilder();

        Cell[][] grid = maze.getGrid();

        for (int i = 0; i < maze.getHeight(); i++) {
            for (int j = 0; j < maze.getWidth(); j++) {
                if (grid[i][j].type() == Cell.Type.WALL) {
                    sb.append("#");
                } else {
                    sb.append(" ");
                }
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    @Override
    public String render(Maze maze, List<Coordinate> path) {
        StringBuilder sb = new StringBuilder();

        Cell[][] grid = maze.getGrid();

        for (int i = 0; i < maze.getHeight(); i++) {
            for (int j = 0; j < maze.getWidth(); j++) {
                if (grid[i][j].type() == Cell.Type.WALL) {
                    sb.append("#");
                } else if (path.contains(new Coordinate(i, j))) {
                    sb.append("*");
                } else {
                    sb.append(" ");
                }
            }
            sb.append("\n");
        }

        return sb.toString();
    }
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