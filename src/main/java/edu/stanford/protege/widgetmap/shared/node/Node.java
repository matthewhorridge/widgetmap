package edu.stanford.protege.widgetmap.shared.node;

import com.google.common.base.Optional;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/12/2013
 */
public abstract class Node implements IsSerializable {

    private Optional<ParentNode> parent = Optional.absent();

    protected Node() {
    }

    public abstract <O> O accept(NodeVisitor<O> visitor);

    public abstract Node duplicate();

    public abstract Set<TerminalNode> getTerminalNodes();

    public abstract boolean isIsometricWith(Node otherNode);

    public abstract boolean equalsIgnoreWeights(Node other);

    public abstract boolean isParentNode();

    public abstract Node minimise();


    /**
     * Gets the parent of this node.
     *
     * @return The optional parent of this node.  Not {@code null}.  If this node does not have any parent then
     * {@link com.google.common.base.Optional#absent()} is returned.
     */
    public Optional<ParentNode> getParent() {
        return parent;
    }

    /**
     * Should only be called by {@link edu.stanford.protege.widgetmap.shared.node.ParentNode} objects to set themselves
     * as the parent of this {@link Node}.
     *
     * @param parent The parent.  Not {@code null}.
     * @throws java.lang.NullPointerException is {@code parent} is {@code null}.
     */
    protected void setParent(Optional<ParentNode> parent) {
        this.parent = checkNotNull(parent);
    }

    /**
     * Removes this node from its parent node.
     */
    public void removeFromParent() {
        if (parent.isPresent()) {
            parent.get().removeChild(this);
        }
    }
}
