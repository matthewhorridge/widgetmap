package edu.stanford.protege.widgetmap.client.view;

import com.google.gwt.user.client.ui.Widget;
import edu.stanford.protege.widgetmap.client.HasFixedPrimaryAxisSize;
import edu.stanford.protege.widgetmap.shared.node.NodeProperties;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 06/12/2013
 */
public class FixedSizeViewHolder extends ViewHolder implements HasFixedPrimaryAxisSize {

    private int size;

    public FixedSizeViewHolder(Widget childWidget, NodeProperties nodeProperties, int primaryAxisFixedSize) {
        super(childWidget, nodeProperties);
        this.size = primaryAxisFixedSize;
    }

    @Override
    public int getFixedPrimaryAxisSize() {
        return size;
    }
}
