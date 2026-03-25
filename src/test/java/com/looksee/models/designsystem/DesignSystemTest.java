package com.looksee.models.designsystem;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for DesignSystem classes.
 */
class DesignSystemTest {

    @Test
    void designSystemDefaultConstructor() {
        DesignSystem ds = new DesignSystem();
        assertNotNull(ds);
    }

    @Test
    void designSystemSetters() {
        DesignSystem ds = new DesignSystem();
        ds.setName("Test Design System");
        assertEquals("Test Design System", ds.getName());
    }

    @Test
    void designSystemSnapshotDefaultConstructor() {
        DesignSystemSnapshot snapshot = new DesignSystemSnapshot();
        assertNotNull(snapshot);
    }
}
