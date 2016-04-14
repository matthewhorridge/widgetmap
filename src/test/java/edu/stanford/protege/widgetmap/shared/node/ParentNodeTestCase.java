package edu.stanford.protege.widgetmap.shared.node;

import com.google.common.base.Optional;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/12/2013
 */
public class ParentNodeTestCase {

    public static final double WEIGHT = 0.3;

    @Test(expected = NullPointerException.class)
    public void nullDirectionInConstructorShouldThrowNullPointerException() {
        new ParentNode(null);
    }

    @Test
    public void getParentShouldNotReturnNull() {
        ParentNode parentNode = new ParentNode();
        assertNotNull(parentNode.getParent());
    }

    @Test
    public void unspecifiedDirectionIsDefault() {
        ParentNode node = new ParentNode();
        assertEquals(Direction.getDefaultDirection(), node.getDirection());
    }

    @Test
    public void isParentShouldReturnTrue() {
        Direction direction = Direction.ROW;
        assertTrue(new ParentNode(direction).isParentNode());
    }

    @Test
    public void addChildShouldAddSpecifiedChild() {
        ParentNode node = new ParentNode();
        Node child = mock(Node.class);
        node.addChild(child, WEIGHT);
        assertEquals(child, node.getChildAt(0));
    }

    @Test
    public void addChildShouldSetParentOnSpecifiedChild() {
        ParentNode node = new ParentNode();
        Node child = mock(Node.class);
        node.addChild(child, WEIGHT);
        verify(child).setParent(Optional.<ParentNode>of(node));
    }

    @Test(expected = NullPointerException.class)
    public void addChildShouldThrowNullPointerExceptionGivenNullArgument() {
        ParentNode node = new ParentNode();
        node.addChild(null, WEIGHT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addChildShouldThrowNullPointerExceptionForNegativeWeight() {
        ParentNode node = new ParentNode();
        Node child = mock(Node.class);
        node.addChild(child, -1);
    }

    @Test
    public void removeChildShouldClearParent() {
        ParentNode node = new ParentNode();
        Node child = mock(Node.class);
        node.addChild(child, WEIGHT);
        node.removeChild(child);
        verify(child).setParent(Optional.<ParentNode>absent());
    }

    @Test
    public void addChildIncrementsGetChildCountByOne() {
        ParentNode node = new ParentNode();
        Node child = mock(Node.class);
        node.addChild(child, WEIGHT);
        assertEquals(1, node.getChildCount());
    }

    @Test
    public void getChildShouldReturnTheChildAddedByAddChild() {
        ParentNode node = new ParentNode();
        Node child = mock(Node.class);
        node.addChild(child, WEIGHT);
        assertEquals(child, node.getChildAt(0));
    }

    @Test
    public void getWeightShouldReturnTheWeightSpecifiedByaddChild() {
        ParentNode node = new ParentNode();
        Node child = mock(Node.class);
        node.addChild(child, WEIGHT);
        assertEquals(WEIGHT, node.getWeightAt(0), 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void getChildAtShouldThrowIndexOutOfBoundsExceptionForIncorrectIndex() {
        ParentNode node = new ParentNode();
        node.getChildAt(0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void getWeightAtShouldThrowIndexOutOfBoundsExceptionForIncorrectIndex() {
        ParentNode node = new ParentNode();
        node.getWeightAt(0);
    }

    @Test
    public void getTotalWeightShouldSumChildWeights() {
        ParentNode parentNode = new ParentNode();
        double totalWeight = 0;
        parentNode.addChild(mock(Node.class), WEIGHT);
        totalWeight += WEIGHT;
        parentNode.addChild(mock(Node.class), 0.2);
        totalWeight += 0.2;
        parentNode.addChild(mock(Node.class), 0.3);
        totalWeight += 0.3;
        assertEquals(totalWeight, parentNode.getTotalWeight(), 0);
    }

    @Test
    public void getTotalWeightReturnsCorrectValueAfterChanges() {
        ParentNode parentNode = new ParentNode();
        double totalWeight = 0;
        Node firstNode = mock(Node.class);
        parentNode.addChild(firstNode, WEIGHT);
        totalWeight += WEIGHT;
        assertEquals(WEIGHT, parentNode.getTotalWeight(), 0);
        parentNode.addChild(mock(Node.class), 0.2);
        totalWeight += 0.2;
        assertEquals(totalWeight, parentNode.getTotalWeight(), 0);
        parentNode.removeChild(firstNode);
        totalWeight -= WEIGHT;
        assertEquals(totalWeight, parentNode.getTotalWeight(), 0);
    }

    @Test
    public void minimiseCalledOnSingleChildShouldReturnChild() {
        ParentNode parentNode = new ParentNode();
        Node child = mock(Node.class);
        when(child.minimise()).thenReturn(child);
        parentNode.addChild(child, WEIGHT);
        Node minimised = parentNode.minimise();
        // Check that the child was minimised
        verify(child).minimise();
        assertEquals(child, minimised);
    }

    @Test
    public void minimiseShouldCollapseNestedUnidirectionalParents() {
        // TODO: This test case is way too complicated
        ParentNode parentNode = new ParentNode();
        ParentNode childNode = new ParentNode();
        parentNode.addChild(childNode, WEIGHT);
        Node nodeC = mock(Node.class);
        when(nodeC.minimise()).thenReturn(nodeC);
        parentNode.addChild(nodeC, WEIGHT);
        Node nodeA = mock(Node.class);
        childNode.addChild(nodeA, WEIGHT);
        when(nodeA.minimise()).thenReturn(nodeA);
        Node nodeB = mock(Node.class);
        childNode.addChild(nodeB, WEIGHT);
        when(nodeB.minimise()).thenReturn(nodeB);
        Node minimisedNode = parentNode.minimise();
        verify(nodeA).minimise();
        verify(nodeB).minimise();
        verify(nodeC).minimise();
        assertTrue(minimisedNode instanceof ParentNode);
        ParentNode minimisedParent = (ParentNode) minimisedNode;
        assertEquals(3, minimisedParent.getChildCount());
        assertEquals(nodeA, minimisedParent.getChildAt(0));
        assertEquals(nodeB, minimisedParent.getChildAt(1));
        assertEquals(nodeC, minimisedParent.getChildAt(2));
        assertEquals(WEIGHT / 2, minimisedParent.getWeightAt(0), 0);
        assertEquals(WEIGHT / 2, minimisedParent.getWeightAt(1), 0);
    }

    @Test
    public void indexOfShouldReturnMinusOneForNonChild() {
        ParentNode parentNode = new ParentNode();
        int index = parentNode.indexOf(mock(Node.class));
        assertEquals(-1, index);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void setWeightAtShouldThrowIndexOutOfBoundExceptionForIndexOutOfBounds() {
        ParentNode parentNode = new ParentNode();
        parentNode.setWeightAt(0, WEIGHT);
    }

    @Test
    public void setWeightShouldSetChildWeight() {
        ParentNode parentNode = new ParentNode();
        Node node = mock(Node.class);
        parentNode.addChild(node, WEIGHT);
        double weight = 0.5;
        parentNode.setWeightAt(0, weight);
        assertEquals(weight, parentNode.getWeightAt(0), 0);
    }

    @Test
    public void acceptCallsVisitMethod() {
        ParentNode parentNode = new ParentNode();
        NodeVisitor<?> visitor = mock(NodeVisitor.class);
        parentNode.accept(visitor);
        verify(visitor).visit(parentNode);
    }

//    @Test
//    public void replaceWithShouldReplaceNodesAndDivideWeightEqually() {
//        ParentNode parentNode = new ParentNode();
//        parentNode.addChild(new TerminalNode(), WEIGHT);
//        TerminalNode nodeA = new TerminalNode();
//        parentNode.addChild(nodeA, WEIGHT);
//        TerminalNode nodeB = new TerminalNode();
//        TerminalNode nodeC = new TerminalNode();
//        parentNode.replaceWith(nodeA, Direction.ROW, nodeB, nodeC);
//
//        assertEquals(Optional.of(parentNode), nodeB.getParent());
//        assertEquals(Optional.of(parentNode), nodeC.getParent());
//        assertEquals(0.25, parentNode.getWeightAt(0), 0);
//        assertEquals(0.25, parentNode.getWeightAt(1), 0);
//    }

    @Test
    public void duplicateShouldReturnCopy() {
        ParentNode parentNode = new ParentNode(Direction.COLUMN);
        Node child = mock(Node.class);
        when(child.duplicate()).thenReturn(child);
        when(child.getParent()).thenReturn(Optional.of(parentNode));
        parentNode.addChild(child, WEIGHT);
        ParentNode duplicate = parentNode.duplicate();
        verify(child).duplicate();
        assertNotSame(parentNode, duplicate);
        assertEquals(1, duplicate.getChildCount());
        assertEquals(child, duplicate.getChildAt(0));
        assertEquals(Direction.COLUMN, duplicate.getDirection());
        assertEquals(WEIGHT, duplicate.getWeightAt(0), 0);
    }

    @Test
    public void isIsometricWithShouldReturnFalseForTerminalNode() {
        ParentNode node = new ParentNode();
        assertFalse(node.isIsometricWith(mock(TerminalNode.class)));
    }

    @Test
    public void isIsometricWithShouldReturnTrueForEmptyParentsWithHorizontalDirection() {
        ParentNode nodeA = new ParentNode(Direction.ROW);
        ParentNode nodeB = new ParentNode(Direction.ROW);
        assertTrue(nodeA.isIsometricWith(nodeB));
    }

    @Test
    public void isIsometricWithShouldReturnTrueForEmptyParentsWithVerticalDirection() {
        ParentNode nodeA = new ParentNode(Direction.COLUMN);
        ParentNode nodeB = new ParentNode(Direction.COLUMN);
        assertTrue(nodeA.isIsometricWith(nodeB));
    }

    @Test
    public void isIsometricWithShouldReturnFalseForEmptyParentsWithDifferentDirection() {
        ParentNode nodeA = new ParentNode(Direction.COLUMN);
        ParentNode nodeB = new ParentNode(Direction.ROW);
        assertFalse(nodeA.isIsometricWith(nodeB));
    }

    @Test
    public void isIsometricWithShouldReturnTrueForIsometricChildrenWithSameWeights() {
        Node childA = mock(Node.class);
        when(childA.isIsometricWith(any(Node.class))).thenReturn(true);
        Node childB = mock(Node.class);
        when(childB.isIsometricWith(any(Node.class))).thenReturn(true);

        ParentNode parentNodeA = new ParentNode();
        parentNodeA.addChild(childA, 0.3);
        parentNodeA.addChild(childB, 0.3);

        ParentNode parentNodeB = new ParentNode();
        parentNodeB.addChild(childA, 0.3);
        parentNodeB.addChild(childB, 0.3);

        assertTrue(parentNodeA.isIsometricWith(parentNodeB));
    }

    @Test
    public void isIsometricWithShouldReturnFalseForIsometricChildrenWithDifferentWeights() {
        Node childA = mock(Node.class);
        when(childA.isIsometricWith(any(Node.class))).thenReturn(true);

        ParentNode parentNodeA = new ParentNode();
        parentNodeA.addChild(childA, 0.3);

        ParentNode parentNodeB = new ParentNode();
        parentNodeB.addChild(childA, 0.5);

        assertFalse(parentNodeA.isIsometricWith(parentNodeB));
    }

    @Test
    public void isIsometricWithShouldReturnFalseForNonIsometricChildrenWithSameWeights() {
        Node childA = mock(Node.class);
        when(childA.isIsometricWith(any(Node.class))).thenReturn(false);

        ParentNode parentNodeA = new ParentNode();
        parentNodeA.addChild(childA, 0.3);

        Node childB = mock(Node.class);
        when(childB.isIsometricWith(any(Node.class))).thenReturn(false);

        ParentNode parentNodeB = new ParentNode();
        parentNodeB.addChild(childB, 0.3);

        assertFalse(parentNodeA.isIsometricWith(parentNodeB));
    }
}
