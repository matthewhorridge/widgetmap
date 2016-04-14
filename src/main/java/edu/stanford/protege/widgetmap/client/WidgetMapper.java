package edu.stanford.protege.widgetmap.client;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.protege.widgetmap.shared.node.TerminalNode;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/12/2013
 */
public interface WidgetMapper {

    IsWidget getWidget(TerminalNode terminalNode);
}
