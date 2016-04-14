package edu.stanford.protege.widgetmap.shared.node;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/12/2013
 * <p>
 *     Represents the direction of the children within a parent node.
 * </p>
 */
public enum Direction {

    ROW,

    COLUMN;

    public static Direction getDefaultDirection() {
        return ROW;
    }

    /**
     * Gets the direction that is perpendicular to this direction.
     * @return The perpendicular direction.  If this direction is {@link #ROW} then the return value will be
     * {@link #COLUMN}.  If this direction is {@link #COLUMN} then the return value will be {@link #ROW}.
     */
    public Direction getPerpendicularDirection() {
        if(this == ROW) {
            return COLUMN;
        }
        else {
            return ROW;
        }
    }
}
