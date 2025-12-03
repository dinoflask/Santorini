package edu;

import edu.Buildings.BuildType;

public class DemeterBuildRule implements BuildRule {

    private final BuildRule base;
    private Space firstBuildSpace = null;
    private boolean usedSecondBuild = false;

    public DemeterBuildRule(BuildRule base) {
        this.base = base;
    }

    @Override
    public boolean canBuild(Worker worker, Space target) {
        // First, respect the base rule
        if (!base.canBuild(worker, target)) {
            return false;
        }

        // If this would be the optional second build, forbid same space
        if (firstBuildSpace != null && !usedSecondBuild) {
            return target != firstBuildSpace;
        }

        return true;
    }

    @Override
    public boolean performBuild(Worker worker, Space target) {
        if (firstBuildSpace == null) {
            // First build of the turn
            base.performBuild(worker, target);
            firstBuildSpace = target;
            usedSecondBuild = false;
            // Allow an optional second build
            return true;
        } else if (!usedSecondBuild) {
            // Second build (must be on different space due to canBuild)
            base.performBuild(worker, target);
            usedSecondBuild = true;
            // No further builds this turn
            reset();
            return false;
        } else {
            // Should not be reached if game respects canBuild/return flags
            return false;
        }
    }

    private void reset() {
        firstBuildSpace = null;
        usedSecondBuild = false;
    }
}
