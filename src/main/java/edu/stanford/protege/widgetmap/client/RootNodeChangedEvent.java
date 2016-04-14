package edu.stanford.protege.widgetmap.client;

import com.google.common.base.Optional;
import com.google.web.bindery.event.shared.Event;
import edu.stanford.protege.widgetmap.shared.node.Node;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/01/2014
 */
public class RootNodeChangedEvent extends Event<RootNodeChangedHandler> {

    public static final Type<RootNodeChangedHandler> TYPE = new Type<RootNodeChangedHandler>();

    private Optional<Node> from;

    private Optional<Node> to;

    public RootNodeChangedEvent(HasRootNode source, Optional<Node> from, Optional<Node> to) {
        setSource(source);
        this.from = from;
        this.to = to;
    }

    public Optional<Node> getFrom() {
        return from;
    }

    public Optional<Node> getTo() {
        return to;
    }

    @Override
    public HasRootNode getSource() {
        return (HasRootNode) super.getSource();
    }

    @Override
    public Type<RootNodeChangedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(RootNodeChangedHandler handler) {
        handler.handleRootNodeChanged(this);
    }
}
