package edu.Buildings;

public class Building {
    private int level;
    private boolean hasDome;

    public Building() {
        this.level = 0;
        this.hasDome = false;
    }

    public int getLevel() {
        return level;
    }

    public boolean hasDome() {
        return hasDome;
    }

    public void build(BuildType type) {
        if (type == BuildType.BLOCK) {
            buildBlock();
        } else if (type == BuildType.DOME) {
            buildDome();
        }
    }

    private void buildBlock() {
        if (level >= 3 || hasDome) {
            throw new IllegalStateException("Cannot build block at this level");
        }
        level++;
    }

    private void buildDome() {
        if (hasDome) {
            throw new IllegalStateException("Dome already exists");
        }
        hasDome = true;
    }
}
