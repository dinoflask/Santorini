package edu.cmu.cs214.santorini;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.Board;
import edu.Space;

import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {
    private Board board;

    @BeforeEach
    public void setUp() {
        board = new Board(5);
    }

    @Test
    public void testBoardInitialization() {
        assertEquals(5, board.getSize(), "Board size should be 5");
        assertNotNull(board.getSpace(0, 0), "Space should exist");
        assertNotNull(board.getSpace(4, 4), "Space should exist");
    }

    @Test
    public void testGetSpace() {
        Space space = board.getSpace(2, 3);
        assertEquals(2, space.getRow(), "Row should be 2");
        assertEquals(3, space.getCol(), "Column should be 3");
    }

    @Test
    public void testInBounds() {
        assertTrue(board.inBounds(0, 0), "0,0 should be in bounds");
        assertTrue(board.inBounds(4, 4), "4,4 should be in bounds");
        assertTrue(board.inBounds(2, 3), "2,3 should be in bounds");

        assertFalse(board.inBounds(-1, 0), "-1,0 should be out of bounds");
        assertFalse(board.inBounds(0, -1), "0,-1 should be out of bounds");
        assertFalse(board.inBounds(5, 0), "5,0 should be out of bounds");
        assertFalse(board.inBounds(0, 5), "0,5 should be out of bounds");
    }

    @Test
    public void testGetSpaceOutOfBounds() {
        assertThrows(IllegalArgumentException.class, () -> {
            board.getSpace(-1, 0);
        }, "Should throw exception for negative row");

        assertThrows(IllegalArgumentException.class, () -> {
            board.getSpace(5, 0);
        }, "Should throw exception for row >= size");
    }

    @Test
    public void testGetNeighborsCorner() {
        Space corner = board.getSpace(0, 0);
        Set<Space> neighbors = board.getNeighborsOf(corner);

        assertEquals(3, neighbors.size(), "Corner should have 3 neighbors");
        assertTrue(neighbors.contains(board.getSpace(0, 1)));
        assertTrue(neighbors.contains(board.getSpace(1, 0)));
        assertTrue(neighbors.contains(board.getSpace(1, 1)));
    }

    @Test
    public void testGetNeighborsEdge() {
        Space edge = board.getSpace(0, 2);
        Set<Space> neighbors = board.getNeighborsOf(edge);

        assertEquals(5, neighbors.size(), "Edge should have 5 neighbors");
    }

    @Test
    public void testGetNeighborsCenter() {
        Space center = board.getSpace(2, 2);
        Set<Space> neighbors = board.getNeighborsOf(center);

        assertEquals(8, neighbors.size(), "Center should have 8 neighbors");
    }

    @Test
    public void testSpacesAreUnique() {
        Space space1 = board.getSpace(2, 3);
        Space space2 = board.getSpace(2, 3);

        assertSame(space1, space2, "Same coordinates should return same space object");
    }
}
