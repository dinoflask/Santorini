package edu;

public class ApolloMoveRule implements MoveRule {

    private final MoveRule base;

    public ApolloMoveRule(MoveRule base) {
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

        // Apollo: Check if target is adjacent to worker (pure geometry, ignore ALL
        // occupancy/levels)
        Space workerSpace = worker.getSpace();
        int dx = Math.abs(target.getCol() - workerSpace.getCol());
        int dy = Math.abs(target.getRow() - workerSpace.getRow());

        // Adjacent: exactly 1 space away horizontally, vertically, or diagonally
        return (dx <= 1 && dy <= 1 && (dx + dy > 0));
    }

    @Override
    public boolean performMove(Worker worker, Space target) {
        if (!target.isOccupiedBy()) {
            return base.performMove(worker, target);
        }

        Worker opponent = target.getOccupiedBy();
        Space workerSpace = worker.getSpace();

        // Apollo swap: Direct swap using setter
        workerSpace.setOccupiedBy(opponent);
        opponent.setSpace(workerSpace); //  Uses new setter

        target.setOccupiedBy(worker);
        worker.setSpace(target); //  Uses new setter

        return false;
    }

    @Override
    public boolean canSkipExtraMove() {
        return false;
    }

    @Override
    public boolean isLegalMoveTarget(Worker worker, Space target) {
        return canMove(worker, target);
    }

    @Override
    public boolean isWinningMove(Worker worker, Space from, Space to) {
        return to.getTower().getLevel() == 3;
    }
}
