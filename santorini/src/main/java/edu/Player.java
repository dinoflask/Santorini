package edu;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final String id;
    private final List<Worker> workers;


    public Player(String id) {
        this.id = id;
        this.workers = new ArrayList<>();

        // Initialize 2 workers per player
        workers.add(new Worker(this));
        workers.add(new Worker(this));
    }

    public String getId() {
        return id;
    }

    public List<Worker> getWorkers() {
        return new ArrayList<>(workers);
    }

    public Worker selectWorker(int index) {
        if (index < 0 || index >= workers.size()) {
            throw new IllegalArgumentException("Invalid worker index");
        }
        return workers.get(index);
    }

    public void placeWorker(Space target) {
        for (Worker worker : workers) {
            if (worker.getSpace() == null && worker.canPlaceTo(target)) {
                worker.placeTo(target);
                return;
            }
        }
        throw new IllegalStateException("No available worker to place");
    }
}
