package edu.cmu.cs214.santorini;

import edu.Board;
import edu.Game;
import edu.Player;
import edu.Space;
import edu.Buildings.BuildType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SantoriniGameTest {
    private Game game;
    private Player player1;
    private Player player2;
    private Board board;

    @BeforeEach
    public void setUp() {
        player1 = new Player("Player1");
        player2 = new Player("Player2");
        game = new Game(player1, player2);
        board = game.getBoard();
        
        // Place workers for testing
        player1.getWorkers().get(0).placeTo(board.getSpace(0, 0));
        player1.getWorkers().get(1).placeTo(board.getSpace(0, 1));
        player2.getWorkers().get(0).placeTo(board.getSpace(4, 4));
        player2.getWorkers().get(1).placeTo(board.getSpace(4, 3));
    }

    @Test
    public void testGameInitialization() {
        assertNotNull(game, "Game should be initialized");
        assertNotNull(game.getBoard(), "Board should be initialized");
        assertEquals(2, game.getPlayers().size(), "Game should have 2 players");
        assertEquals(player1, game.getCurrentPlayer(), "Player 1 should start first");
        assertEquals(-1, game.getWinnerPlayerIndex(), "No winner at start");
    }

    @Test
    public void testValidMoveBuildSequence() {
        Space from = board.getSpace(0, 0);
        Space moveTo = board.getSpace(1, 1);
        Space buildOn = board.getSpace(1, 0);
        
        game.takeTurn(0, moveTo, buildOn, BuildType.BLOCK);
        
        assertEquals(player1.getWorkers().get(0), moveTo.getOccupiedBy(), 
            "Worker should be at new position");
        assertNull(from.getOccupiedBy(), "Old position should be empty");
        assertEquals(1, buildOn.getTower().getLevel(), "Tower should have 1 block");
        assertEquals(player2, game.getCurrentPlayer(), "Turn should switch to player 2");
    }

    @Test
    public void testRejectMoveToOccupiedSpace() {
        Space moveTo = board.getSpace(0, 1); // Occupied by player1's worker
        Space buildOn = board.getSpace(1, 0);
        
        assertThrows(IllegalArgumentException.class, () -> {
            game.takeTurn(0, moveTo, buildOn, BuildType.BLOCK);
        }, "Should reject move to occupied space");
    }

    @Test
    public void testRejectMoveTooFar() {
        Space moveTo = board.getSpace(2, 2); // More than 1 space away
        Space buildOn = board.getSpace(1, 0);
        
        assertThrows(IllegalArgumentException.class, () -> {
            game.takeTurn(0, moveTo, buildOn, BuildType.BLOCK);
        }, "Should reject move more than 1 space away");
    }

    @Test
    public void testRejectClimbTwoLevels() {
        Space moveTo = board.getSpace(1, 0);
        
        // Build two levels on target space
        moveTo.getTower().build(BuildType.BLOCK);
        moveTo.getTower().build(BuildType.BLOCK);
        
        Space buildOn = board.getSpace(1, 1);
        
        assertThrows(IllegalArgumentException.class, () -> {
            game.takeTurn(0, moveTo, buildOn, BuildType.BLOCK);
        }, "Should reject climbing more than 1 level");
    }

    @Test
    public void testRejectMoveToDome() {
        Space moveTo = board.getSpace(1, 0);
        
        // Build to level 3 and add dome
        moveTo.getTower().build(BuildType.BLOCK);
        moveTo.getTower().build(BuildType.BLOCK);
        moveTo.getTower().build(BuildType.BLOCK);
        moveTo.getTower().build(BuildType.DOME);
        
        Space buildOn = board.getSpace(1, 1);
        
        assertThrows(IllegalArgumentException.class, () -> {
            game.takeTurn(0, moveTo, buildOn, BuildType.BLOCK);
        }, "Should reject move to space with dome");
    }

    @Test
    public void testRejectBuildOnOccupiedSpace() {
        Space moveTo = board.getSpace(1, 1);
        Space buildOn = board.getSpace(0, 1); // Occupied by player1's worker
        
        assertThrows(IllegalArgumentException.class, () -> {
            game.takeTurn(0, moveTo, buildOn, BuildType.BLOCK);
        }, "Should reject building on occupied space");
    }

    @Test
    public void testRejectBuildTooFar() {
        Space moveTo = board.getSpace(1, 1);
        Space buildOn = board.getSpace(3, 3); // More than 1 space from new position
        
        assertThrows(IllegalArgumentException.class, () -> {
            game.takeTurn(0, moveTo, buildOn, BuildType.BLOCK);
        }, "Should reject building more than 1 space away");
    }

    @Test
    public void testRejectDomeOnLowLevel() {
        Space moveTo = board.getSpace(1, 1);
        Space buildOn = board.getSpace(1, 0); // Level 0
        
        assertThrows(IllegalArgumentException.class, () -> {
            game.takeTurn(0, moveTo, buildOn, BuildType.DOME);
        }, "Should reject dome on level less than 3");
    }

    @Test
    public void testRejectBlockOnDome() {
        Space moveTo = board.getSpace(1, 1);
        Space buildOn = board.getSpace(1, 0);
        
        // Build to level 3 and add dome
        buildOn.getTower().build(BuildType.BLOCK);
        buildOn.getTower().build(BuildType.BLOCK);
        buildOn.getTower().build(BuildType.BLOCK);
        buildOn.getTower().build(BuildType.DOME);
        
        assertThrows(IllegalArgumentException.class, () -> {
            game.takeTurn(0, moveTo, buildOn, BuildType.BLOCK);
        }, "Should reject building on space with dome");
    }


    @Test
    public void testTurnSwitching() {
        Space moveTo = board.getSpace(1, 1);
        Space buildOn = board.getSpace(1, 0);
        
        assertEquals(player1, game.getCurrentPlayer(), "Player 1 should start");
        
        game.takeTurn(0, moveTo, buildOn, BuildType.BLOCK);
        
        assertEquals(player2, game.getCurrentPlayer(), "Should switch to player 2");
        
        Space moveTo2 = board.getSpace(3, 3);
        Space buildOn2 = board.getSpace(3, 2);
        
        game.takeTurn(0, moveTo2, buildOn2, BuildType.BLOCK);
        
        assertEquals(player1, game.getCurrentPlayer(), "Should switch back to player 1");
    }

    
    @Test
    public void testCompleteGameSequence() {
        // This test demonstrates a complete game where Player 1 wins by:
        // 1. Building two adjacent towers progressively
        // 2. Climbing between them as they grow
        // 3. Finally reaching level 3 to win

        // Starting positions: P1 worker at (0,0), will build at (1,0) and (1,1)
        Space tower1 = board.getSpace(1, 0);
        Space tower2 = board.getSpace(1, 1);

        // Turn 1: P1 moves to (1,0) [ground], builds (1,1) to level 1
        game.takeTurn(0, tower1, tower2, BuildType.BLOCK);
        assertEquals(player2, game.getCurrentPlayer());
        assertEquals(1, tower2.getTower().getLevel());

        // Turn 2: P2 moves and builds away from action
        game.takeTurn(0, board.getSpace(3, 3), board.getSpace(3, 2), BuildType.BLOCK);
        assertEquals(player1, game.getCurrentPlayer());

        // Turn 3: P1 moves to (1,1) [level 1], builds (1,0) to level 1
        game.takeTurn(0, tower2, tower1, BuildType.BLOCK);
        assertEquals(1, tower1.getTower().getLevel());

        // Turn 4: P2
        game.takeTurn(0, board.getSpace(3, 2), board.getSpace(3, 3), BuildType.BLOCK);

        // Turn 5: P1 moves to (1,0) [level 1], builds (1,1) to level 2
        game.takeTurn(0, tower1, tower2, BuildType.BLOCK);
        assertEquals(2, tower2.getTower().getLevel());

        // Turn 6: P2
        game.takeTurn(0, board.getSpace(3, 3), board.getSpace(3, 2), BuildType.BLOCK);

        // Turn 7: P1 moves to (1,1) [level 2], builds (1,0) to level 2
        game.takeTurn(0, tower2, tower1, BuildType.BLOCK);
        assertEquals(2, tower1.getTower().getLevel());

        // Turn 8: P2
        game.takeTurn(0, board.getSpace(3, 2), board.getSpace(3, 3), BuildType.BLOCK);

        // Turn 9: P1 moves to (1,0) [level 2], builds (1,1) to level 3
        game.takeTurn(0, tower1, tower2, BuildType.BLOCK);
        assertEquals(3, tower2.getTower().getLevel());

        // Turn 10: P2
        game.takeTurn(0, board.getSpace(3, 3), board.getSpace(3, 2), BuildType.BLOCK);

        // Turn 11: P1 WINS by moving to (1,1) [level 3], builds anywhere
        game.takeTurn(0, tower2, board.getSpace(0, 2), BuildType.BLOCK);

        assertEquals(0, game.getWinnerPlayerIndex(), "Player 1 should win");
    }

}
