package edu;

import edu.Buildings.BuildType;

public class StandardBuildRule implements BuildRule {

    @Override
    public boolean canBuild(Worker worker, Space target) {
        int level = target.getTower().getLevel();
        BuildType buildType = (level == 3) ? BuildType.DOME : BuildType.BLOCK;
        return target.canBuildOn(worker.getSpace(), buildType);
    }

    @Override
    public boolean performBuild(Worker worker, Space target) {
        int level = target.getTower().getLevel();
        BuildType buildType = (level == 3) ? BuildType.DOME : BuildType.BLOCK;
        worker.buildOn(target, buildType);

        // Standard rule: only one build per turn
        return false;
    }
    
    @Override
    public boolean canSkipExtraBuild() {
        return false;
    }
    
    @Override
    public boolean isLegalBuildTarget(Worker worker, Space target) {
        return canBuild(worker, target);
    }

}
