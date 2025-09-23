package edu;

import java.util.HashSet;
import java.util.Set;

public class Space {
    private final int x;
    private final int y;
    private final Set<Worker> workers;
    private int level; 
    private boolean hasDome;

    public Space(int x, int y) {
        this.x = x;
        this.y = y;
        this.workers = new HashSet<>();
        this.level = 0;
        this.hasDome = false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Set<Worker> getWorkers() {
        return new HashSet<>(workers);
    }

    public void addWorker(Worker worker) {
        workers.add(worker);
    }

    public void removeWorker(Worker worker) {
        workers.remove(worker);
    }

    public boolean isOccupied() {
        return !workers.isEmpty() || hasDome;
    }

    public int getLevel() {
        return level;
    }

    public boolean hasDome() {
        return hasDome;
    }

    // Adds a block or dome, assuming valid rules checked outside
    public void buildBlock() {
        if (level < 3 && !hasDome) {
            level++;
        } else {
            throw new IllegalStateException("Cannot build block on this space");
        }
    }

    public void buildDome() {
        if (level == 3 && !hasDome) {
            hasDome = true;
        } else {
            throw new IllegalStateException("Can only build dome on level 3");
        }
    }
}
