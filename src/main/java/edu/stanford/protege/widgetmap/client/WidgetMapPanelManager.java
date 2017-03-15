package edu.stanford.protege.widgetmap.client;

import com.google.common.base.Optional;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.HasCloseHandlers;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.web.bindery.event.shared.SimpleEventBus;
import edu.stanford.protege.widgetmap.client.split.HasWeights;
import edu.stanford.protege.widgetmap.client.split.PolySplitLayoutPanel;
import edu.stanford.protege.widgetmap.client.split.WeightsChangedEvent;
import edu.stanford.protege.widgetmap.client.split.WeightsChangedHandler;
import edu.stanford.protege.widgetmap.shared.node.*;

import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/12/2013
 */
public class WidgetMapPanelManager implements HasRootNode, HasRootNodeChangedHandlers {

    private Optional<Node> rootNode = Optional.absent();

    private final IsRootWidget rootWidget;

    private WidgetMapper widgetMapper;

    private Map<TerminalNodeId, IsWidget> widgetMap = new HashMap<TerminalNodeId, IsWidget>();

    private Map<HasWeights, ParentNode> hasWeights2ParentNodeMap = new HashMap<HasWeights, ParentNode>();

    private IsWidget emptyWidget;

    private EventBus handlerManager = new SimpleEventBus();

    public WidgetMapPanelManager(IsRootWidget rootWidget, WidgetMapper widgetMapper) {
        this.widgetMapper = widgetMapper;
        this.rootWidget = rootWidget;
        emptyWidget = new EmptyWidgetImpl();
    }

    public void setEmptyWidget(IsWidget emptyWidget) {
        this.emptyWidget = checkNotNull(emptyWidget);
    }

    public java.util.Optional<Node> getRootNode() {
        if(!rootNode.isPresent()) {
            return java.util.Optional.empty();
        }
        else {
            return java.util.Optional.of(rootNode.get().duplicate());
        }
    }

    public void setRootNode(Node rootNode) {
        RootNodeChangeEventManager eventManager = new RootNodeChangeEventManager();
        eventManager.begin();
        this.rootNode = Optional.of(rootNode);
        update();
        eventManager.end();
    }

    @Override
    public HandlerRegistration addRootNodeChangedHandler(RootNodeChangedHandler handler) {
        return handlerManager.addHandler(RootNodeChangedEvent.TYPE, handler);
    }

    private void handleWeightsChanged(WeightsChangedEvent event) {
        if(!rootNode.isPresent()) {
            return;
        }
        RootNodeChangeEventManager eventManager = new RootNodeChangeEventManager();
        eventManager.begin();
        ParentNode pn = hasWeights2ParentNodeMap.get(event.getSource());
        if (pn != null) {
            List<Double> weights = event.getSource().getWeights();
            for (int i = 0; i < weights.size(); i++) {
                Double weight = weights.get(i);
                pn.setWeightAt(i, weight);
            }
            eventManager.end();
        }
    }

    private void update() {
        if(rootNode.isPresent()) {
            rootNode = Optional.of(rootNode.get().minimise());
        }
        rebuild();
    }

    public void rebuild() {
        IsWidget root = build();
        rootWidget.setWidget(root);
        rootWidget.onResize();
    }

    private Optional<IsWidget> getTerminalNodeWidget(TerminalNodeId nodeId) {
        return Optional.fromNullable(widgetMap.get(nodeId));
    }

    private IsWidget build() {
        if(!rootNode.isPresent()) {
            return emptyWidget;
        }
        hasWeights2ParentNodeMap.clear();
        final Set<TerminalNodeId> terminalNodes = new HashSet<TerminalNodeId>();
        IsWidget root = rootNode.get().accept(new NodeVisitor<IsWidget>() {
            public IsWidget visit(TerminalNode terminalNode) {
                terminalNodes.add(terminalNode.getNodeId());
                return getWidgetForTerminalNode(terminalNode);
            }

            public IsWidget visit(ParentNode parentNode) {
                PolySplitLayoutPanel psp = new PolySplitLayoutPanel(parentNode.getDirection());
                for (int i = 0; i < parentNode.getChildCount(); i++) {
                    Node childNode = parentNode.getChildAt(i);
                    double childWeight = parentNode.getWeightAt(i);
                    IsWidget childWidget = childNode.accept(this);
                    psp.add(childWidget, childWeight);
                    psp.addWeightsChangedHandler(new WeightsChangedHandler() {
                        @Override
                        public void handleWeightsChanged(WeightsChangedEvent event) {
                            WidgetMapPanelManager.this.handleWeightsChanged(event);
                        }
                    });
                }
                hasWeights2ParentNodeMap.put(psp, parentNode);
                return psp;
            }
        });
        for (Iterator<TerminalNodeId> it = widgetMap.keySet().iterator(); it.hasNext(); ) {
            if (!terminalNodes.contains(it.next())) {
                it.remove();
            }
        }
        return root;
    }

    public Optional<IsWidget> getWidgetForNode(TerminalNodeId terminalNode) {
        return Optional.fromNullable(widgetMap.get(terminalNode));
    }

    private IsWidget getWidgetForTerminalNode(TerminalNode terminalNode) {
        Optional<IsWidget> existing = getTerminalNodeWidget(terminalNode.getNodeId());
        final IsWidget widget;
        if (existing.isPresent()) {
            widget = existing.get();
        }
        else {
            widget = widgetMapper.getWidget(terminalNode);
            // TODO: REMOVE FROM HERE
            if (widget instanceof HasCloseHandlers<?>) {
                ((HasCloseHandlers<?>) widget).addCloseHandler(new WidgetCloseHandler(terminalNode.getNodeId()));
            }
            widgetMap.put(terminalNode.getNodeId(), widget);
        }
        return widget;
    }

    private void handleClose(TerminalNodeId nodeId) {
        RootNodeChangeEventManager eventManager = new RootNodeChangeEventManager();
        eventManager.begin();
        widgetMap.remove(nodeId);
        if(rootNode.isPresent() && rootNode.get() instanceof TerminalNode && ((TerminalNode) rootNode.get()).getNodeId().equals(nodeId)) {
            rootNode = Optional.absent();
        }
        else {
            for(TerminalNode tn : rootNode.get().getTerminalNodes()) {
                if(tn.getNodeId().equals(nodeId)) {
                    tn.removeFromParent();
                }
            }
        }
        eventManager.end();
        update();
    }


    private Optional<Node> duplicateRoot() {
        if(!rootNode.isPresent()) {
            return rootNode;
        }
        else {
            return Optional.of(rootNode.get().duplicate());
        }
    }


    private class WidgetCloseHandler implements CloseHandler {

        private TerminalNodeId terminalNodeId;

        private WidgetCloseHandler(TerminalNodeId terminalNode) {
            this.terminalNodeId = terminalNode;
        }

        @Override
        public void onClose(CloseEvent event) {
            handleClose(terminalNodeId);
        }
    }


    private class RootNodeChangeEventManager {

        private Optional<Node> startNode;

        private Optional<Node> endNode;

        public void begin() {
            startNode = duplicateRoot();
        }

        public void end() {
            endNode = duplicateRoot();
            if(!endNode.equals(startNode)) {
                fireRootNodeChangedEvent();
            }
        }

        private void fireRootNodeChangedEvent() {
            handlerManager.fireEvent(new RootNodeChangedEvent(WidgetMapPanelManager.this, startNode, endNode));
        }
    }
}
