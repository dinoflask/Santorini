package edu;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Board {
    private final int size;
    private final List<Space> spaces;

    public Board(int size) {
        this.size = size;
        this.spaces = new ArrayList<>();

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                spaces.add(new Space(this, row, col));
            }
        }
    }

    public int getSize() {
        return size;
    }

    public Space getSpace(int row, int col) {
        if (!inBounds(row, col)) {
            throw new IllegalArgumentException("Coordinates out of bounds");
        }
        return spaces.get(row * size + col);
    }

    public boolean inBounds(int row, int col) {
        return row >= 0 && row < size && col >= 0 && col < size;
    }

    public Set<Space> getNeighborsOf(Space space) {
        Set<Space> neighbors = new HashSet<>();
        int row = space.getRow();
        int col = space.getCol();

        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0)
                    continue;
                int newRow = row + dr;
                int newCol = col + dc;
                if (inBounds(newRow, newCol)) {
                    neighbors.add(getSpace(newRow, newCol));
                }
            }
        }

        return neighbors;
    }
}
