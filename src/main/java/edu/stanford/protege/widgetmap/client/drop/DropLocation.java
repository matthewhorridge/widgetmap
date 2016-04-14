package edu.stanford.protege.widgetmap.client.drop;

import com.google.gwt.dom.client.Element;
import edu.stanford.protege.widgetmap.shared.node.Direction;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 06/12/2013
 */
public enum DropLocation {

    LEFT(Direction.ROW),

    RIGHT(Direction.ROW),

    TOP(Direction.COLUMN),

    BOTTOM(Direction.COLUMN);

    private Direction direction;

    private DropLocation(Direction direction) {
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

    /**
     * Gets the drop location give a point and bound of of a rectangle.  All co-ordinates are absolute.
     * @param ptX
     * @param ptY
     * @param top
     * @param left
     * @param bottom
     * @param right
     * @return
     */
    public static DropLocation getDropLocation(int ptX, int ptY, int top, int left, int bottom, int right) {
        int width = right - left;
        int height = bottom - top;
        if(ptX < left + 0.33 * width) {
            return LEFT;
        }
        else if(ptX > right - 0.33 * width) {
            return RIGHT;
        }
        else if(ptY < top + 0.5 * height) {
            return TOP;
        }
        else {
            return BOTTOM;
        }
    }

    public static DropLocation getDropLocation(int ptX, int ptY, Element e) {
        return getDropLocation(ptX, ptY, e.getAbsoluteTop(), e.getAbsoluteLeft(), e.getAbsoluteBottom(), e.getAbsoluteRight());
    }
}
