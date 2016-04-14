package edu.stanford.protege.widgetmap.client.drop;

import com.google.gwt.dom.client.Element;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 06/12/2013
 */
public interface DropHandler {

    void handleDrop(Element dropElement, DropLocation dropLocation);

    void handleDropCancel();
}
