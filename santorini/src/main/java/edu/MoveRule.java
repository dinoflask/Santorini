package edu;

public interface MoveRule {

    /**
     * Is it legal for this worker to move to target?
     */
    boolean canMove(Worker worker, Space target);

    /**
     * Perform the move to target.
     * Returns true if the player may move again this turn (for gods that allow
     * extra moves).
     */
    boolean performMove(Worker worker, Space target);
    
    boolean canSkipExtraMove(); 
    
    boolean isLegalMoveTarget(Worker worker, Space target);
}
