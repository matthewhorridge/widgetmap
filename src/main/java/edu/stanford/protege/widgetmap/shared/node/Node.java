package edu.stanford.protege.widgetmap.shared.node;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.protege.widgetmap.server.node.NodeDeserializer;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/12/2013
 */
@JsonDeserialize(using = NodeDeserializer.class)
public abstract class Node implements IsSerializable {

    @Nullable
    private ParentNode parent = null;

    protected Node() {
    }

    public abstract <O> O accept(NodeVisitor<O> visitor);

    public abstract Node duplicate();

    public abstract Set<TerminalNode> getTerminalNodes();

    @JsonIgnore
    public abstract boolean isIsometricWith(Node otherNode);

    public abstract boolean equalsIgnoreWeights(Node other);

    @JsonIgnore
    public abstract boolean isParentNode();

    public abstract Node minimise();


    /**
     * Gets the parent of this node.
     *
     * @return The optional parent of this node.  Not {@code null}.  If this node does not have any parent then
     * {@link com.google.common.base.Optional#absent()} is returned.
     */
    @JsonIgnore
    public Optional<ParentNode> getParent() {
        return Optional.ofNullable(parent);
    }

    /**
     * Should only be called by {@link edu.stanford.protege.widgetmap.shared.node.ParentNode} objects to set themselves
     * as the parent of this {@link Node}.
     *
     * @param parent The parent.  Not {@code null}.
     * @throws java.lang.NullPointerException is {@code parent} is {@code null}.
     */
    protected void setParent(Optional<ParentNode> parent) {
        this.parent = checkNotNull(parent).orElse(null);
    }

    /**
     * Removes this node from its parent node.
     */
    public void removeFromParent() {
        if (parent != null) {
            parent.removeChild(this);
        }
    }
}
