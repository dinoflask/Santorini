package edu;

import edu.Buildings.BuildType;
import edu.Buildings.Building;

public class Space {
    private final Board board;
    private final int row;
    private final int col;
    private Worker occupiedBy;
    private final Building tower;

    public Space(Board board, int row, int col) {
        this.board = board;
        this.row = row;
        this.col = col;
        this.occupiedBy = null;
        this.tower = new Building();
    }

    public Board getBoard() {
        return board;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Worker getOccupiedBy() {
        return occupiedBy;
    }

    public Building getTower() {
        return tower;
    }

    public boolean isOccupiedBy() {
        return occupiedBy != null;
    }

    public void setOccupiedBy(Worker worker) {
        this.occupiedBy = worker;
    }

    public boolean canMoveTo(Space from) {
        if (this.isOccupiedBy() || this.tower.hasDome()) {
            return false;
        }

        // Check adjacency (1 space away in any direction including diagonals)
        int dx = Math.abs(this.col - from.col);
        int dy = Math.abs(this.row - from.row);
        if (dx > 1 || dy > 1 || (dx == 0 && dy == 0)) {
            return false;
        }

        // Check level difference (can't climb more than 1 level)
        if (this.tower.getLevel() - from.tower.getLevel() > 1) {
            return false;
        }

        return true;
    }

    public boolean canBuildOn(Space workerSpace, BuildType type) {
        // Must be adjacent to worker
        int dx = Math.abs(this.col - workerSpace.col);
        int dy = Math.abs(this.row - workerSpace.row);
        if (dx > 1 || dy > 1 || (dx == 0 && dy == 0)) {
            return false;
        }

        // Can't build on occupied space or if dome exists
        if (this.isOccupiedBy() || this.tower.hasDome()) {
            return false;
        }

        // Validate build type
        if (type == BuildType.DOME) {
            return this.tower.getLevel() == 3;
        } else if (type == BuildType.BLOCK) {
            return this.tower.getLevel() < 3;
        }

        return false;
    }
}
