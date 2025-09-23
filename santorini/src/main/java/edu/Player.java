package edu;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private List<Worker> workers;
    private boolean turn;

    public Player(String name) {
        this.name = name;
        this.workers = new ArrayList<>();
        this.turn = false;
    }

    public String getName() {
        return name;
    }

    public List<Worker> getWorkers() {
        return workers;
    }

    public boolean isTurn() {
        return turn;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }
}
