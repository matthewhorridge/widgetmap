package edu.stanford.protege.widgetmap.client.view;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/12/2013
 */
public interface HasViewTitleChangedHandlers extends HasHandlers {

    HandlerRegistration addViewTitleChangedHandler(ViewTitleChangedHandler handler);
}
