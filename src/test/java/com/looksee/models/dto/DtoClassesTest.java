package com.looksee.models.dto;

import static org.junit.jupiter.api.Assertions.*;

import com.looksee.models.enums.AuditLevel;
import com.looksee.models.enums.AuditCategory;
import com.looksee.models.enums.ObservationType;
import com.looksee.models.enums.Priority;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for DTO classes.
 */
class DtoClassesTest {

    // ===== Subscription =====
    @Test
    void subscriptionDefaultConstructor() {
        Subscription sub = new Subscription();
        assertNull(sub.getPlan());
        assertNull(sub.getPriceId());
    }

    @Test
    void subscriptionAllArgsConstructor() {
        Subscription sub = new Subscription("PRO", "price_123");
        assertEquals("PRO", sub.getPlan());
        assertEquals("price_123", sub.getPriceId());
    }

    @Test
    void subscriptionSetters() {
        Subscription sub = new Subscription();
        sub.setPlan("ENTERPRISE");
        sub.setPriceId("price_456");
        assertEquals("ENTERPRISE", sub.getPlan());
        assertEquals("price_456", sub.getPriceId());
    }

    // ===== AuditUpdateDto =====
    @Test
    void auditUpdateDtoDefaultConstructor() {
        AuditUpdateDto dto = new AuditUpdateDto();
        assertEquals(0, dto.getId());
        assertNull(dto.getLevel());
    }

    @Test
    void auditUpdateDtoAllArgsConstructor() {
        AuditUpdateDto dto = new AuditUpdateDto(
            1L, AuditLevel.PAGE, 0.5, 0.6, 0.7, 0.8, 0.9, 0.4, 0.3, 0.2, 0.1, "msg", "running"
        );
        assertEquals(1L, dto.getId());
        assertEquals(AuditLevel.PAGE, dto.getLevel());
        assertEquals(0.5, dto.getContentScore());
        assertEquals(0.6, dto.getContentProgress());
        assertEquals(0.7, dto.getInfoArchitectureScore());
        assertEquals(0.8, dto.getInfoArchitectureProgress());
        assertEquals(0.9, dto.getAccessibilityScore());
        assertEquals(0.4, dto.getAccessibilityProgress());
        assertEquals(0.3, dto.getAestheticsScore());
        assertEquals(0.2, dto.getAestheticsProgress());
        assertEquals(0.1, dto.getDataExtractionProgress());
        assertEquals("msg", dto.getMessage());
        assertEquals("running", dto.getStatus());
    }

    @Test
    void auditUpdateDtoSetters() {
        AuditUpdateDto dto = new AuditUpdateDto();
        dto.setId(42L);
        dto.setLevel(AuditLevel.DOMAIN);
        dto.setContentScore(0.95);
        dto.setMessage("complete");
        dto.setStatus("done");
        assertEquals(42L, dto.getId());
        assertEquals(AuditLevel.DOMAIN, dto.getLevel());
        assertEquals(0.95, dto.getContentScore());
        assertEquals("complete", dto.getMessage());
        assertEquals("done", dto.getStatus());
    }

    // ===== PageBuiltMessage =====
    @Test
    void pageBuiltMessageDefaultConstructor() {
        PageBuiltMessage msg = new PageBuiltMessage();
        assertNull(msg.getAccountId());
        assertEquals(0, msg.getPageId());
    }

    @Test
    void pageBuiltMessageTwoArgConstructor() {
        PageBuiltMessage msg = new PageBuiltMessage(10L, 20L);
        assertEquals(10L, msg.getPageId());
        assertEquals(20L, msg.getDomainAuditId());
    }

    @Test
    void pageBuiltMessageAllArgsConstructor() {
        PageBuiltMessage msg = new PageBuiltMessage("acc1", 10L, 20L, 30L);
        assertEquals("acc1", msg.getAccountId());
        assertEquals(10L, msg.getPageId());
        assertEquals(20L, msg.getDomainId());
        assertEquals(30L, msg.getDomainAuditId());
    }

    // ===== DomainDto =====
    @Test
    void domainDtoDefaultConstructor() {
        DomainDto dto = new DomainDto();
        assertNull(dto.getUrl());
        assertEquals(0, dto.getId());
    }

    @Test
    void domainDtoSetters() {
        DomainDto dto = new DomainDto();
        dto.setId(1L);
        dto.setUrl("https://example.com");
        dto.setPageCount(10);
        dto.setPagesAudited(5);
        dto.setContentScore(0.8);
        dto.setContentProgress(0.9);
        dto.setInfoArchitectureScore(0.7);
        dto.setAccessibilityScore(0.6);
        dto.setAestheticsScore(0.5);
        dto.setDataExtractionProgress(0.4);
        dto.setAuditRunning(true);
        dto.setMessage("running");
        dto.setStatus("active");
        assertEquals(1L, dto.getId());
        assertEquals("https://example.com", dto.getUrl());
        assertEquals(10, dto.getPageCount());
        assertTrue(dto.isAuditRunning());
    }

    // ===== CompetitorDto =====
    @Test
    void competitorDtoDefaultConstructor() {
        CompetitorDto dto = new CompetitorDto();
        assertNull(dto.getCompanyName());
        assertFalse(dto.isAnalysisRunning());
    }

    @Test
    void competitorDtoSetters() {
        CompetitorDto dto = new CompetitorDto();
        dto.setId(1L);
        dto.setCompanyName("Acme");
        dto.setUrl("https://acme.com");
        dto.setIndustry("tech");
        dto.setAnalysisRunning(true);
        assertEquals(1L, dto.getId());
        assertEquals("Acme", dto.getCompanyName());
        assertEquals("https://acme.com", dto.getUrl());
        assertEquals("tech", dto.getIndustry());
        assertTrue(dto.isAnalysisRunning());
    }

    // ===== DomainSettingsDto =====
    @Test
    void domainSettingsDtoDefaultConstructor() {
        DomainSettingsDto dto = new DomainSettingsDto();
        assertNull(dto.getDesignSystem());
        assertNull(dto.getTestUsers());
    }

    // ===== UXIssueReportDto =====
    @Test
    void uxIssueReportDtoDefaultConstructor() {
        UXIssueReportDto dto = new UXIssueReportDto();
        assertNull(dto.getTitle());
        assertNull(dto.getDescription());
    }

    @Test
    void uxIssueReportDtoSetters() {
        UXIssueReportDto dto = new UXIssueReportDto();
        dto.setTitle("Missing alt text");
        dto.setDescription("Image missing alt attribute");
        dto.setWhyItMatters("Accessibility");
        dto.setRecommendation("Add alt text");
        dto.setPriority("high");
        dto.setType("ELEMENT");
        dto.setCategory("ACCESSIBILITY");
        dto.setWcagCompliance("AA");
        dto.setElementSelector("img.hero");
        dto.setPageUrl("https://example.com");
        Set<String> labels = new HashSet<>();
        labels.add("a11y");
        dto.setLabels(labels);

        assertEquals("Missing alt text", dto.getTitle());
        assertEquals("Image missing alt attribute", dto.getDescription());
        assertEquals("Accessibility", dto.getWhyItMatters());
        assertEquals("Add alt text", dto.getRecommendation());
        assertEquals("high", dto.getPriority());
        assertEquals("ELEMENT", dto.getType());
        assertEquals("ACCESSIBILITY", dto.getCategory());
        assertEquals("AA", dto.getWcagCompliance());
        assertEquals("img.hero", dto.getElementSelector());
        assertEquals("https://example.com", dto.getPageUrl());
        assertEquals(1, dto.getLabels().size());
    }

    // ===== User =====
    @Test
    void userConstructor() {
        User user = new User("test@example.com", "John");
        assertEquals("test@example.com", user.getEmail());
        assertEquals("John", user.getName());
    }

    @Test
    void userSetters() {
        User user = new User("a@b.com", "Name");
        user.setId("user123");
        user.setCompanyName("Acme");
        user.setCompanyDomain("acme.com");
        assertEquals("user123", user.getId());
        assertEquals("Acme", user.getCompanyName());
        assertEquals("acme.com", user.getCompanyDomain());
    }

    // ===== AuditRecordDto =====
    @Test
    void auditRecordDtoSetters() {
        AuditRecordDto dto = new AuditRecordDto();
        dto.setId(1L);
        dto.setUrl("https://example.com");
        dto.setStatus("COMPLETE");
        dto.setLevel("PAGE");
        dto.setContentAuditScore(0.8);
        dto.setInfoArchScore(0.7);
        dto.setAestheticScore(0.6);
        dto.setTargetUserAge("25-34");
        dto.setTargetUserEducation("college");
        assertEquals(1L, dto.getId());
        assertEquals("https://example.com", dto.getUrl());
        assertEquals("COMPLETE", dto.getStatus());
        assertEquals("PAGE", dto.getLevel());
    }

    // ===== PageStatisticDto =====
    @Test
    void pageStatisticDtoSetters() {
        PageStatisticDto dto = new PageStatisticDto();
        dto.setId(1L);
        dto.setUrl("https://example.com");
        dto.setAuditRecordId(42L);
        dto.setScreenshotUrl("https://img.com/ss.png");
        dto.setContentScore(0.9);
        dto.setContentProgress(0.8);
        dto.setInfoArchScore(0.7);
        dto.setAccessibilityScore(0.6);
        dto.setAestheticScore(0.5);
        dto.setDataExtractionProgress(0.4);
        dto.setElementsExtracted(100);
        dto.setElementsReviewed(50);
        dto.setComplete(true);
        assertEquals(1L, dto.getId());
        assertEquals("https://example.com", dto.getUrl());
        assertEquals(42L, dto.getAuditRecordId());
        assertTrue(dto.isComplete());
        assertEquals(100, dto.getElementsExtracted());
    }
}
