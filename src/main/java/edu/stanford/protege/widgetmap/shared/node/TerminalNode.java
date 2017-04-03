package edu.stanford.protege.widgetmap.shared.node;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.util.Collections;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/12/2013
 * <p>
 * A {@code TerminalNode} is a leaf node.  It has a {@link edu.stanford.protege.widgetmap.shared.node.TerminalNodeId}
 * which determines its identity.  Two instances with the same node id are equal.
 * </p>
 */
public class TerminalNode extends Node implements HasNodeProperties {

    private NodeProperties nodeProperties;

    private TerminalNodeId nodeId;

    /**
     * Constructs a fresh {@code TerminalNode}.  A fresh {@link TerminalNodeId} will be generated for the node.
     */
    public TerminalNode() {
        this(new TerminalNodeId());
    }

    /**
     * Constructs a {@code TerminalNode} that has the specified {@link TerminalNodeId}.
     *
     * @param nodeId The {@link edu.stanford.protege.widgetmap.shared.node.TerminalNodeId}.  Not {@code null}.
     * @throws java.lang.NullPointerException if {@code nodeId} is {@code null}.
     */
    public TerminalNode(TerminalNodeId nodeId) {
        this(nodeId, NodeProperties.emptyNodeProperties());
    }

    public TerminalNode(TerminalNodeId nodeId, NodeProperties nodeProperties) {
        this.nodeId = checkNotNull(nodeId);
        this.nodeProperties = checkNotNull(nodeProperties);
    }

    public static Builder builder() {
        return builder(new TerminalNodeId());
    }

    public static Builder builder(TerminalNodeId nodeId) {
        return new Builder(nodeId);
    }

    /**
     * Gets the {@link edu.stanford.protege.widgetmap.shared.node.TerminalNodeId} for this node.
     *
     * @return The {@link edu.stanford.protege.widgetmap.shared.node.TerminalNodeId}.  Not {@code null}.
     */
    public TerminalNodeId getNodeId() {
        return nodeId;
    }

    /**
     * Gets this node's {@link edu.stanford.protege.widgetmap.shared.node.NodeProperties}.
     *
     * @return The {@link edu.stanford.protege.widgetmap.shared.node.NodeProperties} for this node.  Not {@code null}.
     */
    public NodeProperties getNodeProperties() {
        return nodeProperties;
    }

    @Override
    public <O> O accept(NodeVisitor<O> visitor) {
        return visitor.visit(this);
    }

    @Override
    public TerminalNode duplicate() {
        TerminalNode result = new TerminalNode(nodeId);
        result.setParent(getParent());
        result.nodeProperties = nodeProperties;
        return result;
    }

    @Override
    public boolean isIsometricWith(Node otherNode) {
        return otherNode instanceof TerminalNode;
    }

    @Override
    public boolean isParentNode() {
        return false;
    }

    @Override
    public Node minimise() {
        return this;
    }

    @Override
    public Set<TerminalNode> getTerminalNodes() {
        return Collections.singleton(this);
    }

    @Override
    public int hashCode() {
        return "TerminalNode".hashCode() + nodeId.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TerminalNode)) {
            return false;
        }
        TerminalNode other = (TerminalNode) o;
        return this.nodeId.equals(other.nodeId);
    }

    @Override
    public boolean equalsIgnoreWeights(Node other) {
        return equals(other);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("TerminalNode").addValue(nodeId).addValue(nodeProperties).toString();
    }

    public Builder toBuilder() {
        return new Builder(nodeId).withProperties(nodeProperties);
    }

    public static class Builder {

        private TerminalNodeId nodeId;

        private NodeProperties nodeProperties;

        public Builder(TerminalNodeId nodeId) {
            this.nodeId = nodeId;
        }

        public Builder withProperties(NodeProperties nodeProperties) {
            this.nodeProperties = nodeProperties;
            return this;
        }

        public TerminalNode build() {
            TerminalNode result = new TerminalNode(nodeId);
            result.nodeProperties = nodeProperties != null ? nodeProperties : NodeProperties.emptyNodeProperties();
            return result;
        }
    }
}
