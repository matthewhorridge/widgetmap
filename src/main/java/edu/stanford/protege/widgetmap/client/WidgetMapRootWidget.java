package edu.stanford.protege.widgetmap.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/01/2014
 */
public class WidgetMapRootWidget extends Composite implements IsRootWidget {

    private SimplePanel simplePanel;

    public WidgetMapRootWidget() {
        simplePanel = new SimplePanel();
        initWidget(simplePanel);
    }

    @Override
    public void setWidget(IsWidget w) {
        simplePanel.setWidget(w);
    }

    @Override
    public void onResize() {
        IsWidget w = simplePanel.getWidget();
        if(w instanceof RequiresResize) {
            ((RequiresResize) w).onResize();
        }
    }
}
