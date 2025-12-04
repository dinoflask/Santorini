package edu;

public class PanMoveRule implements MoveRule {

    private final MoveRule base;

    public PanMoveRule(MoveRule base) {
        this.base = base;
    }

    @Override
    public boolean canMove(Worker worker, Space target) {
        // Same move legality as standard
        return base.canMove(worker, target);
    }

    @Override
    public boolean performMove(Worker worker, Space target) {
        // Perform move as usual
        return base.performMove(worker, target);
    }

    @Override
    public boolean isLegalMoveTarget(Worker worker, Space target) {
        // Same target legality as standard moves
        return base.isLegalMoveTarget(worker, target);
    }

    @Override
    public boolean canSkipExtraMove() {
        // Pan does not allow extra moves
        return false;
    }

    @Override
    public boolean isWinningMove(Worker worker, Space oldSpace, Space newSpace) {
        int levelDrop = oldSpace.getTower().getLevel() - newSpace.getTower().getLevel();
        return base.isWinningMove(worker, oldSpace, newSpace) || (levelDrop >= 2);
    }

}
