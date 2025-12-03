package edu;

public interface BuildRule {

    /**
     * Is it legal for this worker to build on target right now?
     */
    boolean canBuild(Worker worker, Space target);

    /**
     * Perform the build on target.
     * Returns true if the player may build again this turn (e.g. Demeter’s extra
     * build).
     */
    boolean performBuild(Worker worker, Space target);
}
