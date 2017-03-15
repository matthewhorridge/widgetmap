package edu.stanford.protege.widgetmap.client;

import edu.stanford.protege.widgetmap.shared.node.Node;

import java.util.Optional;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/01/2014
 */
public interface HasRootNode {

    Optional<Node> getRootNode();
}
