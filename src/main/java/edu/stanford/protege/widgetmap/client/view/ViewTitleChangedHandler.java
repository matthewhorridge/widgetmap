package edu.stanford.protege.widgetmap.client.view;

import com.google.gwt.event.shared.EventHandler;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/12/2013
 */
public interface ViewTitleChangedHandler extends EventHandler {

    void handleViewTitleChanged(ViewTitleChangedEvent event);
}
