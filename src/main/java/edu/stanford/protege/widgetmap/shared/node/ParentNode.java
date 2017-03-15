package edu.stanford.protege.widgetmap.shared.node;

import com.google.common.base.Objects;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/12/2013
 */
public class ParentNode extends Node {

    private Direction direction;

    private List<NodeHolder> children = new ArrayList<NodeHolder>();

    public ParentNode() {
        this(Direction.getDefaultDirection());
    }

    public ParentNode(Direction direction) {
        this.direction = checkNotNull(direction);
    }

    @Override
    public String toString() {
        Objects.ToStringHelper helper = Objects.toStringHelper("ParentNode");
        helper.add("direction", direction);
        for (NodeHolder nodeHolder : children) {
            helper.add("weight", nodeHolder.getWeight());
            helper.addValue(nodeHolder.getNode());
        }
        return helper.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ParentNode)) {
            return false;
        }
        ParentNode other = (ParentNode) o;
        return this.getDirection() == other.getDirection() && this.children.equals(other.children);
    }

    @Override
    public boolean equalsIgnoreWeights(Node o) {
        if(o == this) {
            return true;
        }
        if(!(o instanceof ParentNode)) {
            return false;
        }
        ParentNode other = (ParentNode) o;
        if(this.getDirection() != other.getDirection()) {
            return false;
        }
        int childCount = getChildCount();
        if(childCount != other.getChildCount()) {
            return false;
        }
        for(int i = 0; i < childCount; i++) {
            Node myChild = getChildAt(i);
            Node otherChild = other.getChildAt(i);
            if(!myChild.equalsIgnoreWeights(otherChild)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return "ParentNode".hashCode() + direction.hashCode() + children.hashCode();
    }

    @Override
    public <O> O accept(NodeVisitor<O> visitor) {
        return visitor.visit(this);
    }

    public Node getChildAt(int index) {
        return children.get(index).getNode();
    }

    public int getChildCount() {
        return children.size();
    }

    @Override
    public Set<TerminalNode> getTerminalNodes() {
        Set<TerminalNode> result = new HashSet<TerminalNode>();
        for(NodeHolder holder : children) {
            result.addAll(holder.getNode().getTerminalNodes());
        }
        return result;
    }

    public Direction getDirection() {
        return direction;
    }

    public double getTotalWeight() {
        double result = 0;
        for (NodeHolder wn : children) {
            result += wn.getWeight();
        }
        return result;
    }

    public double getWeightAt(int index) {
        return children.get(index).getWeight();
    }

    public void setWeightAt(int index, double weight) {
        children.get(index).setWeight(weight);
    }

    @Override
    public boolean isIsometricWith(Node otherNode) {
        if(otherNode == this) {
            return true;
        }
        if(!(otherNode instanceof ParentNode)) {
            return false;
        }
        ParentNode otherParent = (ParentNode) otherNode;
        if(getDirection() != otherParent.getDirection()) {
            return false;
        }
        int childCount = getChildCount();
        if(childCount != otherParent.getChildCount()) {
            return false;
        }
        for(int i = 0; i < childCount; i++) {
            double myChildWeight = getWeightAt(i);
            double otherChildWeight = otherParent.getWeightAt(i);
            if(myChildWeight != otherChildWeight) {
                return false;
            }
            Node myChild = getChildAt(i);
            Node otherChild = otherParent.getChildAt(i);
            if(!myChild.isIsometricWith(otherChild)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isParentNode() {
        return true;
    }

    public void addChild(Node child, double weight) {
        checkArgument(weight >= 0);
        children.add(new NodeHolder(checkNotNull(child), weight));
        child.setParent(Optional.of(this));
    }

    public void removeChild(Node child) {
        int index = indexOf(child);
        if (index != -1) {
            children.remove(index);
            child.setParent(Optional.empty());
        }
    }

    @Override
    public ParentNode duplicate() {
        ParentNode parentNode = new ParentNode(direction);
        for (NodeHolder child : children) {
            Node childDuplicate = child.getNode().duplicate();
            parentNode.addChild(childDuplicate, child.getWeight());
        }
        return parentNode;
    }

    @Override
    public Node minimise() {
        if (children.size() == 1) {
            return getFirstNodeMinimised();
        }
        else {
            return minimiseAllChildren();
        }
    }

    public void replaceWith(TerminalNode node, Direction direction, TerminalNode... replacementNodes) {
        int index = indexOf(node);
        if (index == -1) {
            return;
        }
        ParentNode newParentNode = new ParentNode(direction);
        newParentNode.setParent(Optional.of(this));
        children.set(index, new NodeHolder(newParentNode, getWeightAt(index)));
        for (TerminalNode tn : replacementNodes) {
            double weight = 1.0 / replacementNodes.length;
            newParentNode.addChild(tn, weight);
        }
        minimise();
    }

    protected int indexOf(Node child) {
        for (int i = 0; i < children.size(); i++) {
            Node node = children.get(i).getNode();
            if (node.equals(child)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns a minimised version of the first child node.
     *
     * @return The first child node minimised.
     */
    private Node getFirstNodeMinimised() {
        return children.get(0).getNode().minimise();
    }

    /**
     * Minimised all of this nodes children and returns this node.
     *
     * @return This node, with all of the children minimised
     */
    private Node minimiseAllChildren() {
        List<NodeHolder> currentChildren = new ArrayList<NodeHolder>(children);
        final double currentTotalWeight = getTotalWeight();
        children.clear();
        for (NodeHolder child : currentChildren) {
            final Node existingChild = child.getNode();
            final Node minimisedChild = existingChild.minimise();
            if (minimisedChild.isParentNode() && ((ParentNode) minimisedChild).getDirection() == this.getDirection()) {
                double childWeight = child.getWeight();
                double weightProportion = childWeight / currentTotalWeight;
                final ParentNode node = (ParentNode) minimisedChild;
                for (int i = 0; i < node.getChildCount(); i++) {
                    Node grandChildNode = node.getChildAt(i);
                    double grandChildWeight = node.getWeightAt(i);
                    double reProportionedWeight = grandChildWeight * weightProportion;
                    addChild(grandChildNode, reProportionedWeight);
                }
                minimisedChild.setParent(Optional.empty());
            }
            else {
                double childWeight = child.getWeight();
                addChild(minimisedChild, childWeight);
            }
        }
        return this;
    }

    protected static class NodeHolder implements IsSerializable {

        private Node node;

        private double weight;

        @SuppressWarnings("unused")
        private NodeHolder() {
        }

        private NodeHolder(Node node, double weight) {
            this.node = node;
            this.weight = weight;
        }

        private Node getNode() {
            return node;
        }

        private double getWeight() {
            return weight;
        }

        private void setWeight(double weight) {
            this.weight = weight;
        }

        @Override
        public int hashCode() {
            return "NodeHolder".hashCode() + node.hashCode() + (int) weight * 100;
        }

        @Override
        public boolean equals(Object o) {
            if(o == this) {
                return true;
            }
            if(!(o instanceof NodeHolder)) {
                return false;
            }
            NodeHolder other = (NodeHolder) o;
            return this.node.equals(other.node) && this.weight == other.weight;
        }
    }
}
