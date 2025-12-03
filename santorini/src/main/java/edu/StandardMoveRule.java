package edu;

public class StandardMoveRule implements MoveRule {

    @Override
    public boolean canMove(Worker worker, Space target) {
        return target.canMoveTo(worker.getSpace());
    }

    @Override
    public boolean performMove(Worker worker, Space target) {
        worker.moveTo(target);
        // Standard rule: only one move per turn
        return false;
    }
}
