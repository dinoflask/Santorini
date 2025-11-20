package edu;

import java.util.ArrayList;
import java.util.List;

import edu.Buildings.BuildType;

public class Game {
    private boolean winState;
    private final int size;
    private final Board board;
    private final List<Player> players;
    private int currentPlayerIndex = 0;
    private int winnerPlayerIndex;
    private final int workersToPlace = 2; // each player places 2 workers initially
    private int workersPlacedThisTurn = 0;
    private TurnPhase turnPhase = TurnPhase.PLACING;
    private Worker activeWorker = null; // currently selected worker

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

    public TurnPhase getPhase() {
        return turnPhase;
    }

    public Worker getActiveWorker() {
        return activeWorker;
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

    public enum TurnPhase {
        PLACING, 
        SELECTION,
        MOVE,
        BUILD
    }

    // Main game action: take a turn with move and build
    public void takeTurn(int x, int y) {
        Space target = board.getSpace(x, y);

        switch (turnPhase) {
            case PLACING:
                Player currentPlayer = getCurrentPlayer();
                List<Worker> workers = currentPlayer.getWorkers();

                // Find first unplaced worker
                Worker workerToPlace = null;
                for (Worker w : workers) {
                    if (w.getSpace() == null) {
                        workerToPlace = w;
                        break;
                    }
                }

                if (workerToPlace == null) {
                    throw new IllegalStateException("No unplaced worker available");
                }

                // Check if the target space is valid for placement
                if (!workerToPlace.canPlaceTo(target)) {
                    throw new IllegalArgumentException("Invalid space for worker placement");
                }

                // Place the worker
                workerToPlace.placeTo(target);

                workersPlacedThisTurn++;

                // COUNT total workers on board (both players) after placing this worker
                int totalWorkersPlaced = 0;
                for (Player p : players) {
                    for (Worker w : p.getWorkers()) {
                        if (w.getSpace() != null) {
                            totalWorkersPlaced++;
                        }
                    }
                }

                // If all 4 workers (2 per player) have been placed, move to SELECTION phase
                if (totalWorkersPlaced == 4) {
                    turnPhase = TurnPhase.SELECTION;
                } else {
                    // Otherwise continue placing for next player
                    switchTurn();
                }
                break;

            case SELECTION:
                // Find if the current player has a worker at (x, y)
                activeWorker = getCurrentPlayer().getWorkers().stream()
                        .filter(w -> w.getSpace().equals(target))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("No worker found at selected space"));

                turnPhase = TurnPhase.MOVE;
                break;

            case MOVE:
                if (!target.canMoveTo(activeWorker.getSpace())) {
                    throw new IllegalArgumentException("Invalid move");
                }
                activeWorker.moveTo(target);

                if (target.getTower().getLevel() == 3) {
                    winState = true;
                    winnerPlayerIndex = currentPlayerIndex;
                    return;
                }
                
                turnPhase = TurnPhase.BUILD;
                break;

            case BUILD:
                // Determine build type by current tower level
                BuildType buildType = (target.getTower().getLevel() == 3) ? BuildType.DOME : BuildType.BLOCK;

                if (!target.canBuildOn(activeWorker.getSpace(), buildType)) {
                    throw new IllegalArgumentException("Invalid build");
                }

                activeWorker.buildOn(target, buildType);

                // Reset for next turn
                switchTurn();
                turnPhase = TurnPhase.SELECTION;
                activeWorker = null;
                break;
        }
    }

}
