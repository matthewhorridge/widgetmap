package edu.stanford.protege.widgetmap.client;

import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ProvidesResize;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.web.bindery.event.shared.HandlerRegistration;
import edu.stanford.protege.widgetmap.client.drop.DropManager;
import edu.stanford.protege.widgetmap.shared.node.Node;
import edu.stanford.protege.widgetmap.shared.node.TerminalNode;

import java.util.Optional;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/12/2013
 */
public class WidgetMapPanel extends Composite implements RequiresResize, ProvidesResize, HasRootNode, HasRootNodeChangedHandlers {

    private final WidgetMapPanelManager panelManager;

    private HandlerRegistration resizeHandlerRegistration = new HandlerRegistration() {
        @Override
        public void removeHandler() {
        }
    };

    private ResizeHandler resizeHandler = new ResizeHandler() {
        public void onResize(ResizeEvent event) {
            rootWidget.onResize();
        }
    };

    private final IsRootWidget rootWidget;

    public WidgetMapPanel(final IsRootWidget rootWidget, final WidgetMapPanelManager panelManager) {
        this.rootWidget = rootWidget;
        this.panelManager = panelManager;
        initWidget(rootWidget.asWidget());
    }

    @Override
    public Optional<Node> getRootNode() {
        return panelManager.getRootNode();
    }

    @Override
    public HandlerRegistration addRootNodeChangedHandler(RootNodeChangedHandler handler) {
        return panelManager.addRootNodeChangedHandler(handler);
    }

    public void setRootNode(Node rootNode) {
        panelManager.setRootNode(rootNode);
    }

    public void setEmptyWidget(IsWidget widget) {
        panelManager.setEmptyWidget(widget);
    }

    public void doDrop(TerminalNode nodeToDrop) {
        if(!panelManager.getRootNode().isPresent()) {
            panelManager.setRootNode(nodeToDrop);
        }
        else {
            DropManager.showDropElement(new WidgetMapPanelDropHandler(nodeToDrop, panelManager));
        }

    }

    @Override
    public void onResize() {
        rootWidget.onResize();
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        addWindowResizeListener();
        panelManager.rebuild();
    }

    @Override
    protected void onUnload() {
        super.onUnload();
        removeWindowResizeListener();
    }

    public void addWindowResizeListener() {
        resizeHandlerRegistration = Window.addResizeHandler(resizeHandler);
        rootWidget.onResize();
    }

    public void removeWindowResizeListener() {
        resizeHandlerRegistration.removeHandler();
    }
}
