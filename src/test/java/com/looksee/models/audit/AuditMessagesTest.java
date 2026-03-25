package com.looksee.models.audit;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.looksee.models.audit.messages.*;
import com.looksee.models.audit.recommend.*;

/**
 * Unit tests for audit message and recommendation classes.
 */
class AuditMessagesTest {

    // ===== UXIssueMessage subclasses =====
    @Test
    void elementStateIssueMessageDefaultConstructor() {
        ElementStateIssueMessage msg = new ElementStateIssueMessage();
        assertNotNull(msg);
    }

    @Test
    void colorContrastIssueMessageDefaultConstructor() {
        ColorContrastIssueMessage msg = new ColorContrastIssueMessage();
        assertNotNull(msg);
    }

    @Test
    void readingComplexityIssueMessageDefaultConstructor() {
        ReadingComplexityIssueMessage msg = new ReadingComplexityIssueMessage();
        assertNotNull(msg);
    }

    @Test
    void sentenceIssueMessageDefaultConstructor() {
        SentenceIssueMessage msg = new SentenceIssueMessage();
        assertNotNull(msg);
    }

    @Test
    void stockImageIssueMessageDefaultConstructor() {
        StockImageIssueMessage msg = new StockImageIssueMessage();
        assertNotNull(msg);
    }

    @Test
    void pageStateIssueMessageDefaultConstructor() {
        PageStateIssueMessage msg = new PageStateIssueMessage();
        assertNotNull(msg);
    }

    @Test
    void colorPaletteIssueMessageDefaultConstructor() {
        ColorPaletteIssueMessage msg = new ColorPaletteIssueMessage();
        assertNotNull(msg);
    }

    @Test
    void typefacesIssueDefaultConstructor() {
        TypefacesIssue msg = new TypefacesIssue();
        assertNotNull(msg);
    }

    @Test
    void stylingMissingIssueMessageDefaultConstructor() {
        StylingMissingIssueMessage msg = new StylingMissingIssueMessage();
        assertNotNull(msg);
    }

    // ===== Recommendation classes =====
    @Test
    void recommendationDefaultConstructor() {
        Recommendation rec = new Recommendation();
        assertNotNull(rec);
    }

    @Test
    void colorContrastRecommendationDefaultConstructor() {
        ColorContrastRecommendation rec = new ColorContrastRecommendation();
        assertNotNull(rec);
    }
}
