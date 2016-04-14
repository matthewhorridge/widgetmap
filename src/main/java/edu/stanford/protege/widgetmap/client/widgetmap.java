package edu.stanford.protege.widgetmap.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import edu.stanford.protege.widgetmap.resources.WidgetMapClientBundle;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class widgetmap implements EntryPoint {

    public void onModuleLoad() {
        GWT.log("[WidgetMap] Injecting client bundle");
        WidgetMapClientBundle.BUNDLE.style().ensureInjected();
    }
}
