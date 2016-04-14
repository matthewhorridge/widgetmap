package edu.stanford.protege.widgetmap.client;

import com.google.gwt.user.client.ui.IsWidget;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 20/06/2014
 */
public interface EmptyWidget extends IsWidget {

    void clearText();

    void setText(String text);
}
