package edu;

public interface BuildRule {
    boolean canBuild(Worker worker, Space target);

    boolean performBuild(Worker worker, Space target);
    
    boolean canSkipExtraBuild();
    
    boolean isLegalBuildTarget(Worker worker, Space target);

}
