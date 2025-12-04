package edu;

import java.util.Arrays;
import java.util.stream.Collectors;

import edu.Buildings.BuildType;
import edu.Game.TurnPhase;

public class GameState {

    private final Cell[] cells;
    private final int winner;
    private final Player currentPlayer;
    private final TurnPhase turnPhase;
    private final GodCard[] availableGodCards;
    private final boolean canPassBuild;

    private GameState(Cell[] cells, int winner, Player player, TurnPhase turnPhase, GodCard[] availableGodCards, boolean canPassBuild) {
        this.cells = cells;
        this.winner = winner;
        this.currentPlayer = player;
        this.turnPhase = turnPhase;
        this.availableGodCards = availableGodCards;
        this.canPassBuild = canPassBuild;
    }

    public static GameState forGame(Game game) {
        Cell[] cells = getCells(game);
        Player player = game.getCurrentPlayer();
        int winner = game.getWinnerPlayerIndex();
        TurnPhase turnPhase = game.getPhase();
        GodCard[] availableGodCards = GodCard.values(); 

        boolean canPassBuild = false;
        if (game.getPhase() == TurnPhase.BUILD) {
            BuildRule rule = game.getCurrentPlayer().getBuildRule();
            canPassBuild = rule.canSkipExtraBuild();
        }

        return new GameState(cells, winner, player, turnPhase, availableGodCards, canPassBuild);
    }

    public Cell[] getCells() {
        return this.cells;
    }

    /**
     * toString() of GameState will return the string representing
     * the GameState in JSON format.
     */
    @Override
    public String toString() {
        String godArray = Arrays.stream(availableGodCards)
                .map(g -> "\"" + g.name() + "\"") // adds quotes to make valid json
                .collect(Collectors.joining(", ", "[", "]"));
        return """
                {
                    "cells": %s,
                    "currentPlayer": "%s",
                    "winner": %s,
                    "turnPhase": "%s",
                    "godCards": %s,
                    "canPassBuild": %b
                    
                }
                """.formatted(
                Arrays.toString(this.cells),
                this.currentPlayer.getId(),
                this.winner, this.turnPhase.name(),
                godArray, canPassBuild);
    }

    private static Cell[] getCells(Game game) {
        Cell cells[] = new Cell[25];
        Board board = game.getBoard();
        for (int x = 0; x <= 4; x++) {
            for (int y = 0; y <= 4; y++) {

                String text = "";
                boolean playable = false;

                Space space = board.getSpace(x, y);
                boolean hasDome = space.getTower().hasDome();

                // Compose building text based on level and dome with nested brackets
                int level = space.getTower().getLevel();
                if (level == 0) {
                    text = null;
                } else if (level == 1) {
                    text = "[]";
                } else if (level == 2) {
                    text = "[[]]";
                } else if (level == 3) {
                    if (hasDome) {
                        text = "[[[O]]]";
                    } else {
                        text = "[[[]]]";
                    }
                }
                Worker workerHere = space.getOccupiedBy();

                if (workerHere != null) {
                    String playerId = workerHere.getOwner().getId();
                    if (text == null) {
                        // No building, so just show worker as [A], [B], etc.
                        text = "[" + playerId + "]";
                    } else {
                        // Insert playerId inside outermost brackets, e.g. [] -> [A], [[]] -> [[A]]
                        int insertPos = text.lastIndexOf(']');
                        if (insertPos > 0) {
                            text = text.substring(0, insertPos) + playerId + text.substring(insertPos);
                        } else {
                            text += playerId;
                        }
                    }
                }
                // Implement playable logic based on game phase and active worker
                switch (game.getPhase()) {
                    case PLACING:
                        // playable if space not occupied
                        playable = (workerHere == null && !space.getTower().hasDome());
                        break;

                    case GOD_SELECTION:
                        // playable if space not occupied
                        playable = false;
                        break;

                    case SELECTION:
                        // playable if occupied by current player's worker
                        playable = (workerHere != null && workerHere.getOwner() == game.getCurrentPlayer());
                        break;

                    case MOVE:
                        if (game.getActiveWorker() != null) {
                            MoveRule moveRule = game.getCurrentPlayer().getMoveRule();
                            playable = moveRule.isLegalMoveTarget(game.getActiveWorker(), space);
                        }
                        break;

                    case BUILD:
                        if (game.getActiveWorker() != null) {
                            BuildRule buildRule = game.getCurrentPlayer().getBuildRule();
                            playable = buildRule.isLegalBuildTarget(game.getActiveWorker(), space);
                        }
                        break;

                    default:
                        playable = false;
                }
                //text: needs to be null, [], [[]], [[[]]], or [[[O]]] if there's no people on it, and
                // [A], [[B]], [[[A]]] if there's workers from A or B on it

                //playable: a cell needs to be 'playable' if game is in place mode and the target space is empty
                //if game is in select mode and the target space has a worker on it
                //if game is in move mode and activeWorker can move to the target space,
                //or game is in build mode and activeWorker can build on the target space
                cells[5 * y + x] = new Cell(x, y, text, playable);
            }
        }
        return cells;
    }
}

class Cell {
    private final int x;
    private final int y;
    private final String text;
    private final boolean playable;

    Cell(int x, int y, String text, boolean playable) {
        this.x = x;
        this.y = y;
        this.text = text;
        this.playable = playable;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getText() {
        return this.text;
    }

    public boolean isPlayable() {
        return this.playable;
    }

    @Override
    public String toString() {
        return """
                {
                    "text": "%s",
                    "playable": %b,
                    "x": %d,
                    "y": %d
                }
                """.formatted(this.text, this.playable, this.x, this.y);
    }
}