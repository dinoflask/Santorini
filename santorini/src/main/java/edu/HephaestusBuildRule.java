package edu;

import edu.Buildings.BuildType;

public class HephaestusBuildRule implements BuildRule {

    private final BuildRule base;
    private Space firstBuildSpace = null;
    private boolean usedSecondBuild = false;

    public HephaestusBuildRule(BuildRule base) {
        this.base = base;
    }

    @Override
    public boolean canBuild(Worker worker, Space target) {
        if (!base.canBuild(worker, target)) {
            return false;
        }

        // Hephaestus rule: second build ONLY on the first build space, and only BLOCK
        // (not dome)
        if (firstBuildSpace != null && !usedSecondBuild) {
            if (target != firstBuildSpace) {
                return false; // Must be same space as first build
            }
            // Must be BLOCK (level < 3), not dome
            return target.getTower().getLevel() < 3;
        }

        return true;
    }

    @Override
    public boolean performBuild(Worker worker, Space target) {
        if (firstBuildSpace == null) {
            // First build - record location
            base.performBuild(worker, target);
            firstBuildSpace = target;
            usedSecondBuild = false;
            return true; // Allow optional second build on same space
        } else if (!usedSecondBuild) {
            // Second build: must be BLOCK on first space (enforced by canBuild)
            base.performBuild(worker, target);
            usedSecondBuild = true;
            reset();
            return false;
        }
        return false;
    }

    @Override
    public boolean canSkipExtraBuild() {
        return firstBuildSpace != null; // Same as Demeter
    }

    private void reset() {
        firstBuildSpace = null;
        usedSecondBuild = false;
    }
    
    @Override
    public boolean isLegalBuildTarget(Worker worker, Space target) {
        return canBuild(worker, target);
    }

    

}
