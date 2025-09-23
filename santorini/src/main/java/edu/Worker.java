package edu;

//Worker is also a data holder, and has a setter called moveTo.
public class Worker {
    private Player owner;
    private Space space;

    public Worker(Player owner, Space initialSpace) {
        this.owner = owner;
        this.space = initialSpace;
        this.space.addWorker(this);
    }

    public Player getOwner() {
        return owner;
    }

    public Space getSpace() {
        return space;
    }

    public void moveTo(Space newSpace) {
        this.space = newSpace;
    }
}
