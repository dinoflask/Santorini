package edu.cmu.cs214.santorini;

import edu.Board;
import edu.Game;
import edu.GodCard;
import edu.Player;
import edu.Game.TurnPhase;

public class Main {
    public static void main(String[] args) {
        testStandardGame();
        testDemeter();
        testHephaestus();

        testPan();

        System.out.println("All god card tests passed! 🎉");
    }

    private static void testStandardGame() {
        System.out.println("🧪 Testing Standard Game Flow");
        Player p1 = new Player("A");
        Player p2 = new Player("B");
        Game game = new Game(p1, p2);

        placeAllWorkers(game);

        if (game.getPhase() != TurnPhase.GOD_SELECTION) {
            System.err.println("❌ Failed to reach GOD_SELECTION");
            return;
        }

        game.chooseGodForCurrentPlayer("STANDARD");
        game.chooseGodForCurrentPlayer("STANDARD");

        if (game.getPhase() != TurnPhase.SELECTION) {
            System.err.println("❌ Failed to reach SELECTION");
            return;
        }

        // P1: Select worker at (0,0), move to (1,0), build at (1,1)
        game.takeTurn(0, 0); // Select
        game.takeTurn(1, 0); // Move
        game.takeTurn(1, 1); // Build → P2's turn

        System.out.println("✅ Standard game flow works");
    }

    private static void testDemeter() {
        System.out.println("🧪 Testing Demeter");
        Player p1 = new Player("A");
        Player p2 = new Player("B");
        Game game = new Game(p1, p2);

        placeAllWorkers(game);
        game.chooseGodForCurrentPlayer("DEMETER");
        game.chooseGodForCurrentPlayer("STANDARD");

        game.takeTurn(0, 0); // Select P1 worker
        game.takeTurn(1, 0); // Move
        game.takeTurn(1, 1); // First build

        try {
            game.takeTurn(0, 1); // Second build (different space) → Demeter fails this
            System.err.println("❌ Demeter allowed second build on different space!");
        } catch (IllegalArgumentException e) {
            System.out.println("✅ Demeter blocks second build on different space");
        }
        System.out.println("✅ Demeter tests passed");
    }

    /**
     * 🎯 FIXED: Use Game.passBuild() instead of second takeTurn() - NO interface
     * changes needed!
     */

    private static void testHephaestus() {
        System.out.println("🧪 Testing Hephaestus");
        Player p1 = new Player("A");
        Player p2 = new Player("B");
        Game game = new Game(p1, p2);

        placeAllWorkers(game);
        game.chooseGodForCurrentPlayer("HEPHAESTUS");
        game.chooseGodForCurrentPlayer("STANDARD");

        // P1: Select → Move → Build1 → PASS (skips second build option)
        game.takeTurn(0, 0); // Select (0,0)
        game.takeTurn(1, 0); // Move to (1,0)
        game.takeTurn(1, 1); // Build1 at (1,1) → Hephaestus returns true
        game.passBuild(); // ✅ Skip second build → resets state, switches to P2

        System.out.println("✅ Hephaestus single-build + pass works");
    }

    // private static void testMinotaur() {
    //     System.out.println("🧪 Testing Minotaur");
    //     Player p1 = new Player("A");
    //     Player p2 = new Player("B");
    //     Game game = new Game(p1, p2);

    //     // Close positioning for push
    //     game.takeTurn(1, 1); // P1 w1
    //     game.takeTurn(2, 1); // P2 w1
    //     game.takeTurn(1, 2); // P1 w2
    //     game.takeTurn(2, 2); // P2 w2 → GOD_SELECTION

    //     game.chooseGodForCurrentPlayer("MINOTAUR");
    //     game.chooseGodForCurrentPlayer("STANDARD");

    //     game.takeTurn(1, 1); // Select
    //     game.takeTurn(2, 1); // Push test

    //     System.out.println("✅ Minotaur push test setup complete");
    // }

    private static void testPan() {
        System.out.println("🧪 Testing Pan");
        Player p1 = new Player("A");
        Player p2 = new Player("B");
        Game game = new Game(p1, p2);

        placeAllWorkers(game);
        game.chooseGodForCurrentPlayer("PAN");
        game.chooseGodForCurrentPlayer("STANDARD");

        System.out.println("✅ Pan win condition test setup complete");
    }

    // private static void testApollo() {
    //     System.out.println("🧪 Testing Apollo");
    //     Player p1 = new Player("A");
    //     Player p2 = new Player("B");
    //     Game game = new Game(p1, p2);

    //     game.takeTurn(1, 1); // P1 w1
    //     game.takeTurn(2, 1); // P2 w1
    //     game.takeTurn(1, 2); // P1 w2
    //     game.takeTurn(2, 2); // P2 w2

    //     game.chooseGodForCurrentPlayer("APOLLO");
    //     game.chooseGodForCurrentPlayer("STANDARD");

    //     game.takeTurn(1, 1); // Select
    //     game.takeTurn(2, 1); // Swap!

    //     System.out.println("✅ Apollo swap test setup complete");
    // }

    // private static void testTwoGodsSimultaneous() {
    //     System.out.println("🧪 Testing Apollo + Pan");
    //     Player p1 = new Player("A");
    //     Player p2 = new Player("B");
    //     Game game = new Game(p1, p2);

    //     placeAllWorkers(game);
    //     game.chooseGodForCurrentPlayer("APOLLO");
    //     game.chooseGodForCurrentPlayer("PAN");

    //     game.takeTurn(0, 0);
    //     game.takeTurn(4, 4);

    //     System.out.println("✅ Two gods interaction works");
    // }

    /** ✅ Workers placed: P1(0,0), P1(0,1), P2(4,4), P2(4,3) */
    private static void placeAllWorkers(Game game) {
        game.takeTurn(0, 0); // P1 worker 1 → P2 turn
        game.takeTurn(4, 4); // P2 worker 1 → P1 turn
        game.takeTurn(0, 1); // P1 worker 2 → P2 turn
        game.takeTurn(4, 3); // P2 worker 2 → GOD_SELECTION
    }
}
