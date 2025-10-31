package edu;

import edu.Buildings.BuildType;

public class Worker {
    private Space space;
    private final Player owner;

    public Worker(Player owner) {
        this.owner = owner;
        this.space = null;
    }

    public Player getOwner() {
        return owner;
    }

    public Space getSpace() {
        return space;
    }

    public boolean canPlaceTo(Space target) {
        return !target.isOccupiedBy() && !target.getTower().hasDome();
    }

    public void placeTo(Space target) {
        if (this.space != null) {
            this.space.setOccupiedBy(null);
        }
        this.space = target;
        target.setOccupiedBy(this);
    }

    public void moveTo(Space target) {
        if (this.space != null) {
            this.space.setOccupiedBy(null);
        }
        this.space = target;
        target.setOccupiedBy(this);
    }

    public void buildOn(Space target, BuildType type) {
        target.getTower().build(type);
    }
}
