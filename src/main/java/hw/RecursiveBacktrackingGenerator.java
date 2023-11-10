package hw;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RecursiveBacktrackingGenerator implements Project2 {

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
        List<Integer> directions = new ArrayList<>();
        directions.add(0); // UP
        directions.add(1); // DOWN
        directions.add(2); // LEFT
        directions.add(3); // RIGHT
        Random random = new Random();

        // Shuffle the directions randomly
        for (int i = 0; i < 4; i++) {
            int j = random.nextInt(4);
            int temp = directions.get(i);
            directions.set(i, directions.get(j));
            directions.set(j, temp);
        }

        // Explore each direction recursively
        for (int direction : directions) {
            int newRow = row + rowOffsets[direction];
            int newCol = col + colOffsets[direction];

            if (isValidCell(grid, newRow, newCol) && grid[newRow][newCol].type() == Cell.Type.WALL) {
                int wallRow = row + rowOffsets[direction] / 2;
                int wallCol = col + colOffsets[direction] / 2;

                grid[wallRow][wallCol] = new Cell(wallRow, wallCol, Cell.Type.PASSAGE);

                generateMaze(grid, newRow, newCol);
            }
        }
    }

    private boolean isValidCell(Cell[][] grid, int row, int col) {
        return row >= 0 && row < grid.length && col >= 0 && col < grid[0].length;
    }

    // Offsets for UP, DOWN, LEFT, RIGHT
    private final int[] rowOffsets = {-1, 1, 0, 0};
    private final int[] colOffsets = {0, 0, -1, 1};
}
