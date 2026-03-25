package com.looksee.utils;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.Test;

import com.looksee.models.journeys.LoginStep;
import com.looksee.models.journeys.SimpleStep;
import com.looksee.models.journeys.Step;

/**
 * Unit tests for miscellaneous utility classes.
 */
class MiscUtilsTest {

    // ===== JourneyUtils =====
    @Test
    void hasLoginStepReturnsFalseForEmptyList() {
        List<Step> steps = new ArrayList<>();
        assertFalse(JourneyUtils.hasLoginStep(steps));
    }

    @Test
    void hasLoginStepReturnsTrueWhenLoginStepPresent() {
        List<Step> steps = new ArrayList<>();
        steps.add(new SimpleStep());
        steps.add(new LoginStep());
        assertTrue(JourneyUtils.hasLoginStep(steps));
    }

    @Test
    void hasLoginStepReturnsFalseWhenNoLoginStep() {
        List<Step> steps = new ArrayList<>();
        steps.add(new SimpleStep());
        steps.add(new SimpleStep());
        assertFalse(JourneyUtils.hasLoginStep(steps));
    }

    @Test
    void trimPreLoginStepsReturnsEmptyWhenNoLoginStep() {
        List<Step> steps = new ArrayList<>();
        steps.add(new SimpleStep());
        List<Step> result = JourneyUtils.trimPreLoginSteps(steps);
        assertTrue(result.isEmpty());
    }

    @Test
    void trimPreLoginStepsTrimsCorrectly() {
        List<Step> steps = new ArrayList<>();
        steps.add(new SimpleStep()); // pre-login
        steps.add(new LoginStep()); // login step
        steps.add(new SimpleStep()); // post-login
        List<Step> result = JourneyUtils.trimPreLoginSteps(steps);
        assertEquals(2, result.size());
        assertTrue(result.get(0) instanceof LoginStep);
    }

    // ===== LabelSetsUtils =====
    @Test
    void getFormTypeOptionsReturnsNonNull() {
        assertNotNull(LabelSetsUtils.getFormTypeOptions());
    }

    // ===== ListUtils =====
    @Test
    void cloneStepsReturnsNewList() {
        List<Step> steps = new ArrayList<>();
        steps.add(new SimpleStep());
        List<Step> cloned = ListUtils.cloneSteps(steps);
        assertNotNull(cloned);
        assertEquals(steps.size(), cloned.size());
    }

    @Test
    void cloneStepsReturnsEmptyForEmpty() {
        List<Step> steps = new ArrayList<>();
        List<Step> cloned = ListUtils.cloneSteps(steps);
        assertNotNull(cloned);
        assertTrue(cloned.isEmpty());
    }
}
