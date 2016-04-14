package edu.stanford.protege.widgetmap.client.split;

import com.google.gwt.event.shared.EventHandler;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 06/12/2013
 */
public interface WeightsChangedHandler extends EventHandler {

    void handleWeightsChanged(WeightsChangedEvent event);
}
