package edu;

public class MinotaurMoveRule implements MoveRule {

    private final MoveRule base;

    public MinotaurMoveRule(MoveRule base) {
        this.base = base;
    }

    @Override
    public boolean canMove(Worker worker, Space target) {
        if (!target.isOccupiedBy()) {
            return base.canMove(worker, target);
        }

        Worker opponent = target.getOccupiedBy();
        if (opponent == null || opponent.getOwner() == worker.getOwner()) {
            return false;
        }

        // Calculate push back space (straight back only)
        Space pushBackSpace = calculatePushBackSpace(worker.getSpace(), target);
        if (pushBackSpace == null)
            return false;

        // Push back space must be unoccupied (ignore level)
        return !pushBackSpace.isOccupiedBy();
    }

    @Override
    public boolean performMove(Worker worker, Space target) {
        if (!target.isOccupiedBy()) {
            return base.performMove(worker, target);
        }

        Worker opponent = target.getOccupiedBy();
        Space pushBackSpace = calculatePushBackSpace(worker.getSpace(), target);

        // Push the opponent back by moving it to pushBackSpace
        opponent.moveTo(pushBackSpace);

        // Now move the current worker into the target space
        worker.moveTo(target);

        // Only one move per turn
        return false;
    }

    @Override
    public boolean canSkipExtraMove() {
        return false;
    }

    private Space calculatePushBackSpace(Space attackerOrigin, Space target) {
        int dx = target.getCol() - attackerOrigin.getCol();
        int dy = target.getRow() - attackerOrigin.getRow();

        // Must be straight line, no diagonal
        if ((dx != 0) && (dy != 0)) {
            return null;
        }

        int pushCol = target.getCol() + dx;
        int pushRow = target.getRow() + dy;

        if (pushCol < 0 || pushCol >= attackerOrigin.getBoard().getSize() ||
                pushRow < 0 || pushRow >= attackerOrigin.getBoard().getSize()) {
            return null;
        }

        return attackerOrigin.getBoard().getSpace(pushRow, pushCol);
    }
    
    @Override
    public boolean isLegalMoveTarget(Worker worker, Space target) {
        // Delegate to canMove - handles both empty spaces AND opponent push spaces
        return canMove(worker, target);
    }

    @Override
    public boolean isWinningMove(Worker worker, Space from, Space to) {
        return to.getTower().getLevel() == 3;
    }

}

