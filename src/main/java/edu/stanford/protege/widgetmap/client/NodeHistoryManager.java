package edu.stanford.protege.widgetmap.client;

import com.google.common.base.Optional;
import edu.stanford.protege.widgetmap.shared.node.Node;

import java.util.Date;
import java.util.Stack;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 11/01/2014
 */
public class NodeHistoryManager {

    private Stack<Optional<Node>> stack = new Stack<Optional<Node>>();

    private long lastTimeStamp = 0;

    public void handleNode(Optional<Node> node) {
        if(stack.isEmpty() || !markCollapseThreshold() || !canCollapseWith(node, stack.peek())) {
            stack.push(node);
        }
    }

    public Optional<Node> pop() {
        if(stack.isEmpty()) {
            return Optional.absent();
        }
        return stack.pop();
    }

    private boolean markCollapseThreshold() {
        long ts = new Date().getTime();
        long diff = ts - lastTimeStamp;
        lastTimeStamp = ts;
        return diff < 200;
    }

    private boolean canCollapseWith(Optional<Node> node, Optional<Node> with) {
        if(!node.isPresent() && !with.isPresent()) {
            // Absent on absent
            return true;
        }
        return node.isPresent() && with.isPresent() && node.get().equalsIgnoreWeights(with.get());
    }
}
