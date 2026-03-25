package com.looksee.utils;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.Test;

import com.looksee.models.ColorData;
import com.looksee.models.audit.Audit;
import com.looksee.models.audit.messages.UXIssueMessage;
import com.looksee.models.enums.*;

/**
 * Unit tests for AuditUtils.
 */
class AuditUtilsTest {

    @Test
    void calculateOverallScoreWithEmptyAudits() {
        Set<Audit> audits = new HashSet<>();
        double score = AuditUtils.calculateOverallScore(audits);
        assertEquals(0.0, score, 0.01);
    }

    @Test
    void calculateOverallScoreWithAudits() {
        Set<Audit> audits = new HashSet<>();
        audits.add(createAudit(AuditCategory.CONTENT, AuditSubcategory.WRITTEN_CONTENT, AuditName.PARAGRAPHING, 8, 10));
        audits.add(createAudit(AuditCategory.AESTHETICS, AuditSubcategory.COLOR_MANAGEMENT, AuditName.COLOR_PALETTE, 6, 10));
        double score = AuditUtils.calculateOverallScore(audits);
        assertTrue(score >= 0.0 && score <= 1.0);
    }

    @Test
    void calculateScoreByCategoryWithEmptyAudits() {
        Set<Audit> audits = new HashSet<>();
        double score = AuditUtils.calculateScoreByCategory(audits, AuditCategory.CONTENT);
        assertEquals(0.0, score, 0.01);
    }

    @Test
    void calculateScoreByCategoryWithMatchingAudits() {
        Set<Audit> audits = new HashSet<>();
        audits.add(createAudit(AuditCategory.CONTENT, AuditSubcategory.WRITTEN_CONTENT, AuditName.PARAGRAPHING, 8, 10));
        audits.add(createAudit(AuditCategory.CONTENT, AuditSubcategory.WRITTEN_CONTENT, AuditName.READING_COMPLEXITY, 5, 10));
        double score = AuditUtils.calculateScoreByCategory(audits, AuditCategory.CONTENT);
        assertTrue(score >= 0.0 && score <= 1.0);
    }

    @Test
    void calculateScoreBySubcategoryWithEmptyAudits() {
        Set<Audit> audits = new HashSet<>();
        double score = AuditUtils.calculateScoreBySubcategory(audits, AuditSubcategory.WRITTEN_CONTENT);
        assertEquals(0.0, score, 0.01);
    }

    @Test
    void calculateScoreByNameWithEmptyAudits() {
        Set<Audit> audits = new HashSet<>();
        double score = AuditUtils.calculateScoreByName(audits, AuditName.ALT_TEXT);
        assertEquals(0.0, score, 0.01);
    }

    @Test
    void calculateProgressByCategory() {
        Set<Audit> audits = new HashSet<>();
        // Empty set should return 0
        double progress = AuditUtils.calculateProgressByCategory(audits, AuditCategory.CONTENT, 5);
        assertEquals(0.0, progress, 0.01);
    }

    @Test
    void isSimilarColorTrue() {
        ColorData c1 = new ColorData(100, 100, 100);
        ColorData c2 = new ColorData(102, 100, 100);
        assertTrue(AuditUtils.isSimilarColor(c1, c2));
    }

    @Test
    void isSimilarColorFalse() {
        ColorData c1 = new ColorData(100, 100, 100);
        ColorData c2 = new ColorData(200, 50, 50);
        assertFalse(AuditUtils.isSimilarColor(c1, c2));
    }

    @Test
    void extractColorPaletteFromEmptySet() {
        Set<ColorData> colors = new HashSet<>();
        List<ColorData> palette = AuditUtils.extractColorPalette(colors);
        assertNotNull(palette);
        assertTrue(palette.isEmpty());
    }

    @Test
    void extractColorPaletteFromColors() {
        Set<ColorData> colors = new HashSet<>();
        colors.add(new ColorData(255, 0, 0));
        colors.add(new ColorData(0, 255, 0));
        colors.add(new ColorData(0, 0, 255));
        List<ColorData> palette = AuditUtils.extractColorPalette(colors);
        assertNotNull(palette);
        assertTrue(palette.size() >= 1);
    }

    private Audit createAudit(AuditCategory cat, AuditSubcategory subcat, AuditName name, int pts, int maxPts) {
        return new Audit(cat, subcat, name, pts, new HashSet<>(), AuditLevel.PAGE, maxPts, "url", "why", "desc", true);
    }
}
