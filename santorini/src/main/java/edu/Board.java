package edu;

import java.util.List;


public class Board {
    private final int size = 5; 
    private final Space[][] spaces;

    // Assume fixed 5x5 board
    public Board() {
        spaces = new Space[size][size];
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                spaces[x][y] = new Space(x, y);
            }
        }
    }

    // Return a Space from the List
    public Space getSpace(int x, int y) {
        if (x < 0 || x >= size || y < 0 || y >= size)
            throw new IllegalArgumentException("Coordinates out of bounds");
        return spaces[x][y];
    }

}
