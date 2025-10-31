package edu.cmu.cs214.santorini;

import edu.Buildings.Building;
import edu.Buildings.BuildType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BuildingTest {
    private Building building;

    @BeforeEach
    public void setUp() {
        building = new Building();
    }

    @Test
    public void testInitialState() {
        assertEquals(0, building.getLevel(), "Initial level should be 0");
        assertFalse(building.hasDome(), "Should not have dome initially");
    }

    @Test
    public void testBuildBlockIncrementsLevel() {
        building.build(BuildType.BLOCK);
        assertEquals(1, building.getLevel(), "Level should be 1 after building block");
        
        building.build(BuildType.BLOCK);
        assertEquals(2, building.getLevel(), "Level should be 2 after building second block");
    }

    @Test
    public void testBuildBlockToLevel3() {
        building.build(BuildType.BLOCK);
        building.build(BuildType.BLOCK);
        building.build(BuildType.BLOCK);
        
        assertEquals(3, building.getLevel(), "Level should be 3");
        assertFalse(building.hasDome(), "Should not have dome yet");
    }

    @Test
    public void testBuildDomeOnLevel3() {
        building.build(BuildType.BLOCK);
        building.build(BuildType.BLOCK);
        building.build(BuildType.BLOCK);
        building.build(BuildType.DOME);
        
        assertTrue(building.hasDome(), "Should have dome");
        assertEquals(3, building.getLevel(), "Level should remain 3");
    }

    @Test
    public void testRejectBlockAfterLevel3() {
        building.build(BuildType.BLOCK);
        building.build(BuildType.BLOCK);
        building.build(BuildType.BLOCK);
        
        assertThrows(IllegalStateException.class, () -> {
            building.build(BuildType.BLOCK);
        }, "Should not allow block at level 3");
    }

    @Test
    public void testRejectBlockAfterDome() {
        building.build(BuildType.BLOCK);
        building.build(BuildType.BLOCK);
        building.build(BuildType.BLOCK);
        building.build(BuildType.DOME);
        
        assertThrows(IllegalStateException.class, () -> {
            building.build(BuildType.BLOCK);
        }, "Should not allow block after dome");
    }

    @Test
    public void testRejectDuplicateDome() {
        building.build(BuildType.BLOCK);
        building.build(BuildType.BLOCK);
        building.build(BuildType.BLOCK);
        building.build(BuildType.DOME);
        
        assertThrows(IllegalStateException.class, () -> {
            building.build(BuildType.DOME);
        }, "Should not allow duplicate dome");
    }
}
