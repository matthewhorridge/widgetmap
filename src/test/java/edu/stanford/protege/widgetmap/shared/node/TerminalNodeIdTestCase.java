package edu.stanford.protege.widgetmap.shared.node;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 07/01/2014
 */
public class TerminalNodeIdTestCase {

    @Test(expected = NullPointerException.class)
    public void nullIdShouldThrowNullPointerException() {
        TerminalNodeId.get(null);
    }

    @Test
    public void nodeIdsWithEqualLexicalIdsShouldBeEqual() {
        TerminalNodeId nodeA = TerminalNodeId.get("A");
        TerminalNodeId nodeB = TerminalNodeId.get("A");
        assertEquals(nodeA, nodeB);
    }

    @Test
    public void nodeIdsWithEqualLexicalIdsShouldHaveTheSameHashCode() {
        TerminalNodeId nodeA = TerminalNodeId.get("A");
        TerminalNodeId nodeB = TerminalNodeId.get("A");
        assertEquals(nodeA.hashCode(), nodeB.hashCode());
    }

    @Test
    public void nodeIdsWithDifferentLexicalIdsShouldNotBeEqual() {
        TerminalNodeId nodeA = TerminalNodeId.get("A");
        TerminalNodeId nodeB = TerminalNodeId.get("B");
        assertFalse(nodeA.equals(nodeB));
    }


}
