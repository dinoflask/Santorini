package edu.cmu.cs214.santorini;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.Board;
import edu.Player;
import edu.Space;
import edu.Worker;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
    private Player player;

    @BeforeEach
    public void setUp() {
        player = new Player("TestPlayer");
    }

    @Test
    public void testPlayerInitialization() {
        assertEquals("TestPlayer", player.getId(), "Player should have correct ID");
        assertEquals(2, player.getWorkers().size(), "Player should have 2 workers");
    }

    @Test
    public void testSelectWorker() {
        Worker worker0 = player.selectWorker(0);
        Worker worker1 = player.selectWorker(1);

        assertNotNull(worker0, "Should return worker at index 0");
        assertNotNull(worker1, "Should return worker at index 1");
        assertNotSame(worker0, worker1, "Workers should be different");
    }

    @Test
    public void testSelectWorkerInvalidIndex() {
        assertThrows(IllegalArgumentException.class, () -> {
            player.selectWorker(-1);
        }, "Should throw exception for negative index");

        assertThrows(IllegalArgumentException.class, () -> {
            player.selectWorker(2);
        }, "Should throw exception for index >= 2");
    }

    @Test
    public void testGetWorkersReturnsCopy() {
        var workers1 = player.getWorkers();
        var workers2 = player.getWorkers();

        assertNotSame(workers1, workers2, "Should return a new list each time");
    }

    @Test
    public void testPlaceWorker() {
        Board board = new Board(5);
        Space space = board.getSpace(0, 0);

        player.placeWorker(space);

        assertTrue(space.isOccupiedBy(), "Space should be occupied");
        assertEquals(player, space.getOccupiedBy().getOwner(),
                "Worker on space should belong to player");
    }
}
