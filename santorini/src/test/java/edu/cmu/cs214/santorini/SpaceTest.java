package edu.cmu.cs214.santorini;

import edu.Board;
import edu.Player;
import edu.Space;
import edu.Worker;
import edu.Buildings.BuildType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SpaceTest {
    private Board board;
    private Space space;
    private Player player;
    private Worker worker;

    @BeforeEach
    public void setUp() {
        board = new Board(5);
        space = board.getSpace(2, 2);
        player = new Player("TestPlayer");
        worker = player.getWorkers().get(0);
    }

    @Test
    public void testInitialState() {
        assertFalse(space.isOccupiedBy(), "Space should not be occupied initially");
        assertEquals(0, space.getTower().getLevel(), "Tower level should be 0");
        assertFalse(space.getTower().hasDome(), "Should not have dome initially");
    }

    @Test
    public void testSetOccupiedBy() {
        space.setOccupiedBy(worker);
        assertTrue(space.isOccupiedBy(), "Space should be occupied");
        assertEquals(worker, space.getOccupiedBy(), "Should return correct worker");
    }

    @Test
    public void testCanMoveToAdjacentSpace() {
        Space from = board.getSpace(2, 2);
        Space to = board.getSpace(2, 3);

        assertTrue(to.canMoveTo(from), "Should allow move to adjacent space");
    }

    @Test
    public void testCanMoveToAdjacentDiagonal() {
        Space from = board.getSpace(2, 2);
        Space to = board.getSpace(3, 3);

        assertTrue(to.canMoveTo(from), "Should allow move to diagonal space");
    }

    @Test
    public void testCannotMoveToSameSpace() {
        Space from = board.getSpace(2, 2);
        Space to = board.getSpace(2, 2);

        assertFalse(to.canMoveTo(from), "Should not allow move to same space");
    }

    @Test
    public void testCannotMoveTooFar() {
        Space from = board.getSpace(2, 2);
        Space to = board.getSpace(2, 4);

        assertFalse(to.canMoveTo(from), "Should not allow move 2+ spaces away");
    }

    @Test
    public void testCannotMoveToOccupiedSpace() {
        Space from = board.getSpace(2, 2);
        Space to = board.getSpace(2, 3);
        to.setOccupiedBy(worker);

        assertFalse(to.canMoveTo(from), "Should not allow move to occupied space");
    }

    @Test
    public void testCannotMoveToDome() {
        Space from = board.getSpace(2, 2);
        Space to = board.getSpace(2, 3);

        to.getTower().build(BuildType.BLOCK);
        to.getTower().build(BuildType.BLOCK);
        to.getTower().build(BuildType.BLOCK);
        to.getTower().build(BuildType.DOME);

        assertFalse(to.canMoveTo(from), "Should not allow move to dome");
    }

    @Test
    public void testCanClimbOneLevel() {
        Space from = board.getSpace(2, 2);
        Space to = board.getSpace(2, 3);

        to.getTower().build(BuildType.BLOCK);

        assertTrue(to.canMoveTo(from), "Should allow climbing 1 level");
    }

    @Test
    public void testCannotClimbTwoLevels() {
        Space from = board.getSpace(2, 2);
        Space to = board.getSpace(2, 3);

        to.getTower().build(BuildType.BLOCK);
        to.getTower().build(BuildType.BLOCK);

        assertFalse(to.canMoveTo(from), "Should not allow climbing 2 levels");
    }

    @Test
    public void testCanMoveDown() {
        Space from = board.getSpace(2, 2);
        Space to = board.getSpace(2, 3);

        from.getTower().build(BuildType.BLOCK);
        from.getTower().build(BuildType.BLOCK);
        from.getTower().build(BuildType.BLOCK);

        assertTrue(to.canMoveTo(from), "Should allow moving down any number of levels");
    }

    @Test
    public void testCanBuildOnAdjacentSpace() {
        Space workerSpace = board.getSpace(2, 2);
        Space buildSpace = board.getSpace(2, 3);

        assertTrue(buildSpace.canBuildOn(workerSpace, BuildType.BLOCK),
                "Should allow building on adjacent space");
    }

    @Test
    public void testCannotBuildTooFar() {
        Space workerSpace = board.getSpace(2, 2);
        Space buildSpace = board.getSpace(2, 4);

        assertFalse(buildSpace.canBuildOn(workerSpace, BuildType.BLOCK),
                "Should not allow building 2+ spaces away");
    }

    @Test
    public void testCannotBuildOnOccupiedSpace() {
        Space workerSpace = board.getSpace(2, 2);
        Space buildSpace = board.getSpace(2, 3);
        buildSpace.setOccupiedBy(worker);

        assertFalse(buildSpace.canBuildOn(workerSpace, BuildType.BLOCK),
                "Should not allow building on occupied space");
    }

    @Test
    public void testCannotBuildOnDome() {
        Space workerSpace = board.getSpace(2, 2);
        Space buildSpace = board.getSpace(2, 3);

        buildSpace.getTower().build(BuildType.BLOCK);
        buildSpace.getTower().build(BuildType.BLOCK);
        buildSpace.getTower().build(BuildType.BLOCK);
        buildSpace.getTower().build(BuildType.DOME);

        assertFalse(buildSpace.canBuildOn(workerSpace, BuildType.BLOCK),
                "Should not allow building on dome");
    }

    @Test
    public void testCanBuildDomeOnLevel3Only() {
        Space workerSpace = board.getSpace(2, 2);
        Space buildSpace = board.getSpace(2, 3);

        assertFalse(buildSpace.canBuildOn(workerSpace, BuildType.DOME),
                "Should not allow dome on level 0");

        buildSpace.getTower().build(BuildType.BLOCK);
        assertFalse(buildSpace.canBuildOn(workerSpace, BuildType.DOME),
                "Should not allow dome on level 1");

        buildSpace.getTower().build(BuildType.BLOCK);
        assertFalse(buildSpace.canBuildOn(workerSpace, BuildType.DOME),
                "Should not allow dome on level 2");

        buildSpace.getTower().build(BuildType.BLOCK);
        assertTrue(buildSpace.canBuildOn(workerSpace, BuildType.DOME),
                "Should allow dome on level 3");
    }
}
