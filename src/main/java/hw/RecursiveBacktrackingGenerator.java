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
