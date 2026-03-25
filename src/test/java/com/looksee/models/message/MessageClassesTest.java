package com.looksee.models.message;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.looksee.models.enums.*;

/**
 * Unit tests for message classes.
 */
class MessageClassesTest {

    // ===== AuditError =====
    @Test
    void auditErrorConstructorAndGetters() {
        AuditError error = new AuditError();
        assertNotNull(error);
    }

    // ===== AuditProgressUpdate =====
    @Test
    void auditProgressUpdateDefaultConstructor() {
        AuditProgressUpdate msg = new AuditProgressUpdate();
        assertNotNull(msg);
    }

    // ===== UrlMessage =====
    @Test
    void urlMessageDefaultConstructor() {
        UrlMessage msg = new UrlMessage();
        assertNotNull(msg);
    }

    // ===== PageBuiltMessage =====
    @Test
    void pageBuiltMessageDefaultConstructor() {
        PageBuiltMessage msg = new PageBuiltMessage();
        assertNotNull(msg);
    }

    // ===== ElementExtractionMessage =====
    @Test
    void elementExtractionMessageDefaultConstructor() {
        ElementExtractionMessage msg = new ElementExtractionMessage();
        assertNotNull(msg);
    }

    // ===== ElementExtractionError =====
    @Test
    void elementExtractionErrorDefaultConstructor() {
        ElementExtractionError msg = new ElementExtractionError();
        assertNotNull(msg);
    }

    // ===== PageDataExtractionError =====
    @Test
    void pageDataExtractionErrorDefaultConstructor() {
        PageDataExtractionError msg = new PageDataExtractionError();
        assertNotNull(msg);
    }

    // ===== ExceededSubscriptionMessage =====
    @Test
    void exceededSubscriptionMessageDefaultConstructor() {
        ExceededSubscriptionMessage msg = new ExceededSubscriptionMessage();
        assertNotNull(msg);
    }

    // ===== DiscoveryActionRequest =====
    @Test
    void discoveryActionRequestDefaultConstructor() {
        DiscoveryActionRequest msg = new DiscoveryActionRequest();
        assertNotNull(msg);
    }

    // ===== AuditStartMessage =====
    @Test
    void auditStartMessageDefaultConstructor() {
        AuditStartMessage msg = new AuditStartMessage();
        assertNotNull(msg);
    }

    // ===== BugMessage =====
    @Test
    void bugMessageDefaultConstructor() {
        BugMessage msg = new BugMessage();
        assertNotNull(msg);
    }

    // ===== AuditMessage =====
    @Test
    void auditMessageDefaultConstructor() {
        AuditMessage msg = new AuditMessage();
        assertNotNull(msg);
    }

    // ===== AuditSetMessage =====
    @Test
    void auditSetMessageDefaultConstructor() {
        AuditSetMessage msg = new AuditSetMessage();
        assertNotNull(msg);
    }
}
