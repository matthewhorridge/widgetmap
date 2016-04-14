package edu.stanford.protege.widgetmap.client;

import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/01/2014
 */
public interface HasRootNodeChangedHandlers {

    HandlerRegistration addRootNodeChangedHandler(RootNodeChangedHandler handler);

}
