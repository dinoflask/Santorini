package edu.cmu.cs214.santorini;

import edu.Board;
import edu.Player;
import edu.Space;
import edu.Worker;
import edu.Buildings.BuildType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WorkerTest {
    private Player player;
    private Worker worker;
    private Board board;

    @BeforeEach
    public void setUp() {
        player = new Player("TestPlayer");
        worker = player.getWorkers().get(0);
        board = new Board(5);
    }

    @Test
    public void testWorkerInitialization() {
        assertEquals(player, worker.getOwner(), "Worker should have correct owner");
        assertNull(worker.getSpace(), "Worker should not have space initially");
    }

    @Test
    public void testPlaceTo() {
        Space space = board.getSpace(2, 2);
        worker.placeTo(space);

        assertEquals(space, worker.getSpace(), "Worker should be at placed space");
        assertEquals(worker, space.getOccupiedBy(), "Space should show worker");
    }

    @Test
    public void testMoveTo() {
        Space from = board.getSpace(2, 2);
        Space to = board.getSpace(2, 3);

        worker.placeTo(from);
        worker.moveTo(to);

        assertEquals(to, worker.getSpace(), "Worker should be at new space");
        assertEquals(worker, to.getOccupiedBy(), "New space should show worker");
        assertNull(from.getOccupiedBy(), "Old space should be empty");
    }

    @Test
    public void testCanPlaceToEmptySpace() {
        Space space = board.getSpace(2, 2);
        assertTrue(worker.canPlaceTo(space), "Should allow placing on empty space");
    }

    @Test
    public void testCannotPlaceToOccupiedSpace() {
        Space space = board.getSpace(2, 2);
        Worker otherWorker = player.getWorkers().get(1);
        otherWorker.placeTo(space);

        assertFalse(worker.canPlaceTo(space), "Should not allow placing on occupied space");
    }

    @Test
    public void testCannotPlaceToDome() {
        Space space = board.getSpace(2, 2);
        space.getTower().build(BuildType.BLOCK);
        space.getTower().build(BuildType.BLOCK);
        space.getTower().build(BuildType.BLOCK);
        space.getTower().build(BuildType.DOME);

        assertFalse(worker.canPlaceTo(space), "Should not allow placing on dome");
    }

    @Test
    public void testBuildOn() {
        Space workerSpace = board.getSpace(2, 2);
        Space buildSpace = board.getSpace(2, 3);

        worker.placeTo(workerSpace);
        worker.buildOn(buildSpace, BuildType.BLOCK);

        assertEquals(1, buildSpace.getTower().getLevel(), "Build space should have 1 block");
    }
}
