package edu;

import java.util.List;

public class Game {
    private final List<Player> players;
    private final Board board;
    private boolean winState;
    private Player winningPlayer;
    private int currentPlayerIndex;

    public Game(List<Player> players, Board board) {
        if (players.size() != 2) {
            throw new IllegalArgumentException("Game requires exactly 2 players.");
        }
        this.players = players;
        this.board = board;
        this.winState = false;
        this.winningPlayer = null;
        this.currentPlayerIndex = 0;
        this.players.get(this.currentPlayerIndex).setTurn(true);
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public void nextTurn() {
        players.get(currentPlayerIndex).setTurn(false);
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        players.get(currentPlayerIndex).setTurn(true);
    }

    private boolean isPlayersTurn(Player player) {
        return player.equals(getCurrentPlayer());
    }

    public boolean isMoveLegal(Space current, Space target) {
        int dx = Math.abs(target.getX() - current.getX());
        int dy = Math.abs(target.getY() - current.getY());
        if (dx > 1 || dy > 1 || (dx == 0 && dy == 0)) {
            return false;
        }
        if (target.isOccupied() || target.hasDome()) {
            return false;
        }
        int currentLevel = current.getLevel();
        int targetLevel = target.getLevel();
        if (targetLevel - currentLevel > 1) {
            return false;
        }
        return true;
    }

    public boolean placeWorker(Player player, int x, int y) {
        Space space = board.getSpace(x, y);
        if (space.isOccupied() || player.getWorkers().size() >= 2) {
            return false;
        }
        Worker worker = new Worker(player, space);
        player.getWorkers().add(worker);
        space.addWorker(worker);
        return true;
    }

    public boolean moveWorker(Player player, Worker worker, int newX, int newY) {
        if (winState) {
            System.out.println("Game is over, no more moves allowed.");
            return false;
        }
        
        if (!isPlayersTurn(player)) {
            System.out.println("Not " + player.getName() + "'s turn");
            return false;
        }
        if (!player.getWorkers().contains(worker)) {
            return false;
        }
        Space current = worker.getSpace();
        Space target = board.getSpace(newX, newY);
        if (!isMoveLegal(current, target)) {
            return false;
        }
        current.removeWorker(worker);
        target.addWorker(worker);
        worker.moveTo(target);
        if (target.getLevel() == 3) {
            endGame(player);
        }
        return true;
    }

    public boolean buildAt(Player player, int x, int y, boolean dome) {
        if (winState) {
            System.out.println("Game is over, no more moves allowed.");
            return false;
        }

        if (!isPlayersTurn(player)) {
            System.out.println("Not " + player.getName() + "'s turn");
            return false;
        }
        Space space = board.getSpace(x, y);
        if (space.isOccupied()) {
            return false;
        }
        if (dome) {
            if (space.getLevel() == 3 && !space.hasDome()) {
                space.buildDome();
                nextTurn();
                return true;
            }
            return false;
        } else {
            if (space.getLevel() < 3 && !space.hasDome()) {
                space.buildBlock();
                nextTurn();
                return true;
            }
            return false;
        }
    }

    public void endGame(Player winner) {
        this.winState = true;
        this.winningPlayer = winner;
        System.out.println("Game Over! Player " + winner.getName() + " wins!");
    }

    public boolean isWinState() {
        return winState;
    }

    public Player getWinningPlayer() {
        return winningPlayer;
    }

    public Board getBoard() {
        return board;
    }
}
