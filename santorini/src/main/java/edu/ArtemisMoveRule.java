package edu;

public class ArtemisMoveRule implements MoveRule {
    private final MoveRule base;
    private Space initialSpace = null; // Track where first move started
    private boolean secondMoveDone = false; // track if second move occurred

    public ArtemisMoveRule(MoveRule base) {
        this.base = base;
    }

    @Override
    public boolean canMove(Worker worker, Space target) {
        if (secondMoveDone) {
            return false; // no moves after second move
        }
        Space currentSpace = worker.getSpace();

        if (initialSpace == null) {
            if (base.canMove(worker, target)) {
                return true;
            }
            return false;
        }

        // second move: can't return to initial spot
        if (target.equals(initialSpace)) {
            return false;
        }
        return base.canMove(worker, target);
    }

    @Override
    public boolean performMove(Worker worker, Space target) {
        if (initialSpace == null) {
            // FIRST move - record starting position, allow second move
            initialSpace = worker.getSpace();
            base.performMove(worker, target);
            return true; // Stay in MOVE phase
        } else {
            // SECOND move - mark as done, go to BUILD
            secondMoveDone = true;
            base.performMove(worker, target);
            return false; // End MOVE phase
        }
    }

    @Override
    public boolean canSkipExtraMove() {
        // Allow pass only after first move but before second move
        return initialSpace != null && !secondMoveDone;
    }

    @Override
    public boolean isLegalMoveTarget(Worker worker, Space target) {
        return canMove(worker, target);
    }

    @Override
    public boolean isWinningMove(Worker worker, Space from, Space to) {
        return to.getTower().getLevel() == 3;
    }

    // Reset for new turn
    public void newTurn() {
        initialSpace = null;
        secondMoveDone = false; // Reset flag here
        base.newTurn();
    }



}
