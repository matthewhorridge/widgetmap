package edu.stanford.protege.widgetmap.shared.node;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/12/2013
 */
public class DirectionTestCase {

    @Test
    public void getPerpendicularDirectionShouldReturnHorizontalWhenVertical() {
        // Given
        Direction vertical = Direction.COLUMN;
        // When
        Direction perpendicular = vertical.getPerpendicularDirection();
        // Then
        assertEquals(Direction.ROW, perpendicular);
    }

    @Test
    public void getPerpendicularDirectionShouldReturnVerticalWhenHorizontal() {
        // Given
        Direction horizontal = Direction.ROW;
        // When
        Direction perpendicular = horizontal.getPerpendicularDirection();
        // Then
        assertEquals(Direction.COLUMN, perpendicular);
    }

    @Test
    public void getDefaultDirectionShouldReturnHorizontal() {
        assertEquals(Direction.ROW, Direction.getDefaultDirection());
    }
}
