package hw;

import java.util.ArrayList;
import java.util.List;

public class DepthFirstSolver implements Solver {

    private static final int[] ROW_DIRECTIONS = {-1, 1, 0, 0}; // Up, Down
    private static final int[] COL_DIRECTIONS = {0, 0, -1, 1}; // Left, Right

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

        for (int i = 0; i < ROW_DIRECTIONS.length; i++) {
            int newRow = row + ROW_DIRECTIONS[i];
            int newCol = col + COL_DIRECTIONS[i];

            if (isValidCell(grid, visited, newRow, newCol) && dfs(grid, visited, newRow, newCol, endRow, endCol, path)) {
                path.add(new Coordinate(row, col));
                return true;
            }
        }

        return false;
    }

    private boolean isValidCell(Cell[][] grid, boolean[][] visited, int row, int col) {
        return row >= 0 && row < grid.length && col >= 0 && col < grid[0].length &&
                grid[row][col].type() == Cell.Type.PASSAGE && !visited[row][col];
    }
}
