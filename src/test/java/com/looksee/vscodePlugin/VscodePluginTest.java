package com.looksee.vscodePlugin;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.looksee.vscodePlugin.structs.*;

/**
 * Unit tests for VS Code plugin classes.
 */
class VscodePluginTest {

    // ===== Message =====
    @Test
    void messageDefaultConstructor() {
        Message msg = new Message();
        assertNotNull(msg);
    }

    @Test
    void messageSetters() {
        Message msg = new Message();
        msg.setAccountKey("acc123");
        msg.setDomain("example.com");
        assertEquals("acc123", msg.getAccountKey());
        assertEquals("example.com", msg.getDomain());
    }

    // ===== Tree =====
    @Test
    void treeDefaultConstructor() {
        Tree<String> tree = new Tree<>();
        assertNotNull(tree);
    }

    @Test
    void treeSetRoot() {
        Tree<String> tree = new Tree<>();
        TreeNode<String> root = new TreeNode<>("root");
        tree.setRoot(root);
        assertEquals("root", tree.getRoot().getData());
    }

    // ===== TreeNode =====
    @Test
    void treeNodeConstructor() {
        TreeNode<String> node = new TreeNode<>("data");
        assertEquals("data", node.getData());
        assertNotNull(node.getChildren());
    }

    @Test
    void treeNodeAddChild() {
        TreeNode<String> parent = new TreeNode<>("parent");
        TreeNode<String> child = new TreeNode<>("child");
        parent.addChild(child);
        assertEquals(1, parent.getChildren().size());
    }

    // ===== SessionTestTracker =====
    @Test
    void sessionTestTrackerSingleton() {
        SessionTestTracker tracker1 = SessionTestTracker.getInstance();
        SessionTestTracker tracker2 = SessionTestTracker.getInstance();
        assertSame(tracker1, tracker2);
    }

    // ===== TestMapper =====
    @Test
    void testMapperDefaultConstructor() {
        TestMapper mapper = new TestMapper();
        assertNotNull(mapper);
    }
}
