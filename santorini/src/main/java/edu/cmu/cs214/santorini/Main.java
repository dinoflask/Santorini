package edu.cmu.cs214.santorini;

import edu.Board;
import edu.Game;
import edu.Player;
import edu.Worker;
import edu.Space;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Initialize players and board
        Player playerA = new Player("A");
        Player playerB = new Player("B");
        List<Player> players = new ArrayList<>();
        players.add(playerA);
        players.add(playerB);

        Board board = new Board();
        Game game = new Game(players, board);

        // Place workers at starting positions
        System.out.println("Placing workers...");
        printResult(game.placeWorker(playerA, 0, 0), "Player A worker at (0,0)");
        printResult(game.placeWorker(playerA, 1, 0), "Player A worker at (1,0)");
        printResult(game.placeWorker(playerB, 4, 4), "Player B worker at (4,4)");
        printResult(game.placeWorker(playerB, 3, 4), "Player B worker at (3,4)");

        // Simulate moves and builds
        Worker aWorker = playerA.getWorkers().get(0);
        Worker bWorker = playerB.getWorkers().get(0);

        System.out.println("Player A moves worker from (0,0) to (0,1)");
        printResult(game.moveWorker(playerA, aWorker, 0, 1), "Move success");

        System.out.println("Player A builds block at (1,1)");
        printResult(game.buildAt(playerA, 1, 1, false), "Build block success");

        System.out.println("Player B tries to move onto occupied space (0,1)");
        printResult(game.moveWorker(playerB, bWorker, 0, 1), "Move success");

        System.out.println("Player B moves worker from (4,4) to (3,3)");
        printResult(game.moveWorker(playerB, bWorker, 3, 3), "Move success");

        System.out.println("Player B builds block at (3,2)");
        printResult(game.buildAt(playerB, 3, 2, false), "Build block success");

        System.out.println("Player A moves worker from (0,1) to (1,1)");
        printResult(game.moveWorker(playerA, aWorker, 1, 1), "Move success");

        System.out.println("Player A builds block at (0,1)");
        printResult(game.buildAt(playerA, 0, 1, false), "Build block success");

        System.out.println("Player B moves worker from (3,3) to (3,2)");
        printResult(game.moveWorker(playerB, bWorker, 3, 2), "Move success");

        System.out.println("Player B builds block at (3,3)");
        printResult(game.buildAt(playerB, 3, 3, false), "Build block success");

        System.out.println("Player A moves worker from (1,1) to (0,1)");
        printResult(game.moveWorker(playerA, aWorker, 0, 1), "Move success");

        System.out.println("Player A builds block at (1,1)");
        printResult(game.buildAt(playerA, 1, 1, false), "Build block success");

        System.out.println("Player B moves worker from (3,2) to (3,3)");
        printResult(game.moveWorker(playerB, bWorker, 3, 3), "Move success");

        System.out.println("Player B builds block at (3,2)");
        printResult(game.buildAt(playerB, 3, 2, false), "Build block success");

        System.out.println("Player A moves worker from (0,1) to (1,1)");
        printResult(game.moveWorker(playerA, aWorker, 1, 1), "Move success");

        System.out.println("Player A builds block at (0,1)");
        printResult(game.buildAt(playerA, 0, 1, false), "Build block success");

        System.out.println("Player B moves worker from (3,3) to (3,2)");
        printResult(game.moveWorker(playerB, bWorker, 3, 2), "Move success");

        System.out.println("Player B builds block at (3,3)");
        printResult(game.buildAt(playerB, 3, 3, false), "Build block success");

        System.out.println("Player A moves worker from (1,1) to (0,1)");
        printResult(game.moveWorker(playerA, aWorker, 0, 1), "Move success");

        //fail!
        System.out.println("Player A builds dome at (1,1)");
        printResult(game.buildAt(playerA, 1, 1, true), "Build block success");

        System.out.println("Player A builds block at (1,1)");
        printResult(game.buildAt(playerA, 1, 1, false), "Build block success");

        System.out.println("Player B moves worker from (3,2) to (3,3)");
        printResult(game.moveWorker(playerB, bWorker, 3, 3), "Move success");

        System.out.println("Player B builds block at (3,2)");
        printResult(game.buildAt(playerB, 3, 2, false), "Build block success");

        System.out.println("Player A moves worker from (0,1) to (1,1)");
        printResult(game.moveWorker(playerA, aWorker, 1, 1), "Move success");

        if (game.isWinState()) {
            System.out.println("Game won by player: " + game.getWinningPlayer().getName());
            return;
        } else {
            System.out.println("Game not won yet.");
        }
    }

    private static void printResult(boolean outcome, String message) {
        System.out.println(message + ": " + (outcome ? "Success" : "Failure"));
    }
}
