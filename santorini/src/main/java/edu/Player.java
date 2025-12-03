package edu;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;


public class Player {
    private final String id;
    private final List<Worker> workers;
    private final List<GodCard> godCards = new ArrayList<>();
    private MoveRule moveRule = new StandardMoveRule();
    private BuildRule buildRule = new StandardBuildRule();

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
    
    // Return an unmodifiable view to avoid external mutation
    public List<GodCard> getGodCards() {
        return Collections.unmodifiableList(godCards);
    }

    // Replace entire list (useful if you ever assign multiple at once)
    public void setGodCards(List<GodCard> cards) {
        godCards.clear();
        if (cards != null) {
            godCards.addAll(cards);
        }
    }

    // Convenience: add a single God card
    public void addGodCard(GodCard card) {
        if (card != null && !godCards.contains(card)) {
            godCards.add(card);
        }
    }

    // Convenience: check if player has a specific God
    public boolean hasGodCard(GodCard card) {
        return godCards.contains(card);
    }
    
    public MoveRule getMoveRule() {
        return moveRule;
    }

    public void setMoveRule(MoveRule moveRule) {
        this.moveRule = moveRule;
    }

    public BuildRule getBuildRule() {
        return buildRule;
    }

    public void setBuildRule(BuildRule buildRule) {
        this.buildRule = buildRule;
    }

}
