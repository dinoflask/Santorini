package edu.cmu.cs214.santorini;

import edu.Board;
import edu.Game;
import edu.Player;
import edu.Buildings.BuildType;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Santorini Game Demo ===\n");

        // Initialize game
        Player player1 = new Player("Alice");
        Player player2 = new Player("Bob");
        Game game = new Game(player1, player2);
        Board board = game.getBoard();

        System.out.println("Game initialized with players: Alice and Bob");
        System.out.println("Board size: " + board.getSize() + "x" + board.getSize());
        System.out.println("Current player: " + game.getCurrentPlayer().getId());
        System.out.println();

        // Place workers
        System.out.println("Placing workers on the board...");
        player1.getWorkers().get(0).placeTo(board.getSpace(0, 0));
        player1.getWorkers().get(1).placeTo(board.getSpace(0, 1));
        player2.getWorkers().get(0).placeTo(board.getSpace(4, 4));
        player2.getWorkers().get(1).placeTo(board.getSpace(4, 3));
        System.out.println("Alice's workers at (0,0) and (0,1)");
        System.out.println("Bob's workers at (4,4) and (4,3)");
        System.out.println();

        // Demonstrate a few turns
        System.out.println("--- Turn 1: Alice ---");
        game.takeTurn(0, board.getSpace(1, 1), board.getSpace(1, 0), BuildType.BLOCK);
        System.out.println("Alice moved worker to (1,1) and built a block at (1,0)");
        System.out.println("Current player: " + game.getCurrentPlayer().getId());
        System.out.println();

        System.out.println("--- Turn 2: Bob ---");
        game.takeTurn(0, board.getSpace(3, 3), board.getSpace(3, 4), BuildType.BLOCK);
        System.out.println("Bob moved worker to (3,3) and built a block at (3,4)");
        System.out.println("Current player: " + game.getCurrentPlayer().getId());
        System.out.println();

        System.out.println("--- Turn 3: Alice ---");
        game.takeTurn(0, board.getSpace(2, 1), board.getSpace(1, 0), BuildType.BLOCK);
        System.out.println("Alice moved worker to (2,1) and built at (1,0) - now level 2");
        System.out.println("Tower at (1,0) level: " + board.getSpace(1, 0).getTower().getLevel());
        System.out.println();

        System.out.println("=== Demo Complete ===");
        System.out.println("\nTo run comprehensive tests, use: mvn test");
    }

}
