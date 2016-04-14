package edu.stanford.protege.widgetmap.shared.node;

import com.google.common.base.Optional;
import org.junit.Test;


import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/12/2013
 */
public class TerminalNodeTestCase {

    @Test(expected = NullPointerException.class)
    public void constructorThrowsNullPointerForNullId() {
        new TerminalNode(null);
    }

    @Test
    public void getNodeIdReturnsSuppliedNodeId() {
        TerminalNodeId nodeId = mock(TerminalNodeId.class);
        TerminalNode node = new TerminalNode(nodeId);
        assertSame(nodeId, node.getNodeId());
    }

    @Test
    public void getNodePropertiesReturnsEmptyNodePropertiesForUnsetProperties() {
        TerminalNode node = new TerminalNode();
        assertSame(NodeProperties.emptyNodeProperties(), node.getNodeProperties());
    }

    @Test
    public void getNodePropertiesReturnsSetNodeProperties() {
        NodeProperties properties = mock(NodeProperties.class);
        TerminalNode node = TerminalNode.builder().withProperties(properties).build();
        assertSame(properties, node.getNodeProperties());
    }


    @Test
    public void minimiseReturnsSameObject() {
        TerminalNode node = new TerminalNode();
        assertEquals(node, node.minimise());
    }

    @Test
    public void hashCodeReturnsSameHashCodeForEqualObjects() {
        TerminalNodeId nodeId = mock(TerminalNodeId.class);
        TerminalNode nodeA = new TerminalNode(nodeId);
        TerminalNode nodeB = new TerminalNode(nodeId);
        assertEquals(nodeA.hashCode(), nodeB.hashCode());
    }

    @Test
    public void equalsReturnsTrueForEqualObjects() {
        TerminalNodeId nodeId = mock(TerminalNodeId.class);
        TerminalNode nodeA = new TerminalNode(nodeId);
        TerminalNode nodeB = new TerminalNode(nodeId);
        assertEquals(nodeA, nodeB);
    }

    @Test
    public void duplicateReturnsFreshButEqualObject() {
        TerminalNode node = new TerminalNode();
        TerminalNode duplicate = node.duplicate();
        assertFalse(node == duplicate);
        assertEquals(node, duplicate);
    }


    @Test
    public void isParentNodeShouldReturnFalse() {
        TerminalNode node = new TerminalNode();
        assertFalse(node.isParentNode());
    }

    @Test
    public void getParentNodeShouldReturnAbsent() {
        TerminalNode node = new TerminalNode();
        assertEquals(Optional.<ParentNode>absent(), node.getParent());
    }

    @Test
    public void getParentNodeShouldReturnSetParentNode() {
        Optional<ParentNode> parent = Optional.of(mock(ParentNode.class));
        TerminalNode node = new TerminalNode();
        node.setParent(parent);
        assertSame(parent, node.getParent());
    }

    @Test
    public void acceptShouldCallVisitOnVisitor() {
        TerminalNode node = new TerminalNode();
        NodeVisitor mock = mock(NodeVisitor.class);
        node.accept(mock);
        verify(mock).visit(node);
    }

    @Test
    public void equalsShouldReturnTrueForDifferentNodesWithSameId() {
        TerminalNodeId nodeId = mock(TerminalNodeId.class);
        TerminalNode nodeA = new TerminalNode(nodeId);
        TerminalNode nodeB = new TerminalNode(nodeId);
        assertEquals(nodeA, nodeB);
    }

    @Test
    public void equalsShouldReturnFalseForNull() {
        TerminalNode node = new TerminalNode();
        assertFalse(node.equals(null));
    }

    @Test
    public void equalsShouldReturnTrueForSameObject() {
        TerminalNode node = new TerminalNode();
        assertEquals(node, node);
    }

    @Test
    public void equalsShouldReturnFalseForDifferentClassOfObject() {
        TerminalNode node = new TerminalNode();
        assertFalse(node.equals("test"));
    }

    @Test
    public void isIsometricWithShouldReturnTrueForSameNode() {
        TerminalNode node = new TerminalNode();
        assertTrue(node.isIsometricWith(node));
    }

    @Test
    public void isIsometricWithShouldReturnTrueForNodeWithEqualId() {
        TerminalNodeId nodeId = mock(TerminalNodeId.class);
        TerminalNode nodeA = new TerminalNode(nodeId);
        TerminalNode nodeB = new TerminalNode(nodeId);
        assertTrue(nodeA.isIsometricWith(nodeB));
    }

    @Test
    public void isIsometricWithShouldReturnFalseForParentNode() {
        ParentNode parentNode = mock(ParentNode.class);
        TerminalNode node = new TerminalNode();
        assertFalse(node.isIsometricWith(parentNode));
    }

    @Test
    public void equalsIgnoreWeightsShouldReturnFalseForNull() {
        TerminalNode node = new TerminalNode();
        assertFalse(node.equals(null));
    }

    @Test
    public void equalsIgnoreWeightsShouldReturnTrueForSameObject() {
        TerminalNode node = new TerminalNode();
        assertEquals(node, node);
    }

    @Test
    public void equalsIgnoreWeightsShouldReturnFalseForDifferentClassOfObject() {
        TerminalNode node = new TerminalNode();
        assertFalse(node.equals("test"));
    }

    @Test
    public void equalsIgnoreWeightsShouldReturnTrueForDifferentNodesWithSameId() {
        TerminalNodeId nodeId = mock(TerminalNodeId.class);
        TerminalNode nodeA = new TerminalNode(nodeId);
        TerminalNode nodeB = new TerminalNode(nodeId);
        assertEquals(nodeA, nodeB);
    }
}
