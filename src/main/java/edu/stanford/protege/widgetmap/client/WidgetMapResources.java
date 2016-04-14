package edu.stanford.protege.widgetmap.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 23/05/2014
 */
public interface WidgetMapResources extends ClientBundle {

    public static final WidgetMapResources INSTANCE = GWT.create(WidgetMapResources.class);

    @Source("close-cross.png")
    ImageResource closeIcon();

}
