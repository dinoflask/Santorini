package edu;

import java.util.ArrayList;
import java.util.List;

import edu.Buildings.BuildType;

public class Game {
    private boolean winState;
    private final int size;
    private final Board board;
    private final List<Player> players;
    private int currentPlayerIndex;
    private int winnerPlayerIndex;

    public Game(Player player1, Player player2) {
        this.size = 5;
        this.board = new Board(size);

        this.players = new ArrayList<>();
        this.players.add(player1);
        this.players.add(player2);

        this.currentPlayerIndex = 0;
        this.winState = false;
        this.winnerPlayerIndex = -1;
    }

    public Board getBoard() {
        return board;
    }

    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public int getWinnerPlayerIndex() {
        return winnerPlayerIndex;
    }

    public void switchTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    // Main game action: take a turn with move and build
    public void takeTurn(int workerIndex, Space moveTarget, Space buildTarget, BuildType buildType) {
        if (winState) {
            throw new IllegalStateException("Game is already over");
        }

        Player currentPlayer = getCurrentPlayer();
        Worker worker = currentPlayer.selectWorker(workerIndex);

        // Move phase
        Space currentSpace = worker.getSpace();
        if (currentSpace == null) {
            throw new IllegalStateException("Worker not placed on board");
        }

        if (!moveTarget.canMoveTo(currentSpace)) {
            throw new IllegalArgumentException("Invalid move");
        }

        worker.moveTo(moveTarget);

        // Check win condition (reached level 3)
        if (moveTarget.getTower().getLevel() == 3) {
            winState = true;
            winnerPlayerIndex = currentPlayerIndex;
            return;
        }

        // Build phase
        if (!buildTarget.canBuildOn(moveTarget, buildType)) {
            throw new IllegalArgumentException("Invalid build");
        }

        worker.buildOn(buildTarget, buildType);

        // Switch turn after successful move and build
        switchTurn();
    }
}
