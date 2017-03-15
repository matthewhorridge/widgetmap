package edu.stanford.protege.widgetmap.client;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.protege.widgetmap.client.drop.DropHandler;
import edu.stanford.protege.widgetmap.client.drop.DropLocation;
import edu.stanford.protege.widgetmap.shared.node.Node;
import edu.stanford.protege.widgetmap.shared.node.ParentNode;
import edu.stanford.protege.widgetmap.shared.node.TerminalNode;

import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/01/2014
 */
public class WidgetMapPanelDropHandler implements DropHandler {

    private WidgetMapPanelManager panelManager;

    private TerminalNode nodeToDrop;

    public WidgetMapPanelDropHandler(TerminalNode nodeToDrop, WidgetMapPanelManager panelManager) {
        this.nodeToDrop = nodeToDrop;
        this.panelManager = panelManager;
    }

    @Override
    public void handleDropCancel() {
    }

    @Override
    public void handleDrop(Element element, DropLocation dropLocation) {
        java.util.Optional<Node> root = panelManager.getRootNode();
        if (!root.isPresent()) {
            panelManager.setRootNode(nodeToDrop);
            return;
        }
        Optional<TerminalNode> tn = getTerminalNodeForElement(element, root.get());
        GWT.log("[WidgetMap][WidgetMapPanelDropHandler] handleDrop terminal node: " + tn);
        if (!tn.isPresent()) {
            return;
        }

        TerminalNode terminalNode = tn.get();
        final ParentNode parentNode;
        final Node rootNode;
        if (Optional.<Node>of(terminalNode).equals(root)) {
            parentNode = new ParentNode();
            parentNode.addChild(terminalNode, 1.0);
            rootNode = parentNode;
        }
        else {
            Optional<ParentNode> parent = terminalNode.getParent();
            if (!parent.isPresent()) {
                return;
            }
            parentNode = parent.get();
            rootNode = root.get();
        }
        TerminalNode first;
        TerminalNode second;
        if (dropLocation == DropLocation.LEFT || dropLocation == DropLocation.TOP) {
            first = nodeToDrop;
            second = terminalNode;
        }
        else {
            first = terminalNode;
            second = nodeToDrop;
        }
        parentNode.replaceWith(terminalNode, dropLocation.getDirection(), first, second);
        parentNode.minimise();
        panelManager.setRootNode(rootNode);
    }

    private Optional<TerminalNode> getTerminalNodeForElement(Element element, Node rootNode) {
        Set<TerminalNode> terminalNodes = rootNode.getTerminalNodes();
        for (TerminalNode terminalNode : terminalNodes) {
            Optional<IsWidget> optionalWidget = panelManager.getWidgetForNode(terminalNode.getNodeId());
            if (optionalWidget.isPresent()) {
                Widget widget = optionalWidget.get().asWidget();
                if (widget.getElement().equals(element)) {
                    return Optional.of(terminalNode);
                }
            }
        }
        return Optional.absent();
    }
}
