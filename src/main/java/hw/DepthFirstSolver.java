package hw;

import java.util.ArrayList;
import java.util.List;

public class DepthFirstSolver implements Solver {

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
            path.add(new Coordinate(row, col));
            return true;
        }

        if (dfs(grid, visited, row + 1, col, endRow, endCol, path)) { // Down
            path.add(new Coordinate(row, col));
            return true;
        }

        if (dfs(grid, visited, row, col - 1, endRow, endCol, path)) { // Left
            path.add(new Coordinate(row, col));
            return true;
        }

        if (dfs(grid, visited, row, col + 1, endRow, endCol, path)) { // Right
            path.add(new Coordinate(row, col));
            return true;
        }

        return false;
    }

    private boolean isValidCell(Cell[][] grid, boolean[][] visited, int row, int col) {
        return row >= 0 && row < grid.length && col >= 0 && col < grid[0].length &&
                grid[row][col].type() == Cell.Type.PASSAGE && !visited[row][col];
    }
}
