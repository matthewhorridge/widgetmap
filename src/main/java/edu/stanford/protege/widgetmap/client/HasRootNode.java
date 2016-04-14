package edu.stanford.protege.widgetmap.client;

import com.google.common.base.Optional;
import edu.stanford.protege.widgetmap.shared.node.Node;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/01/2014
 */
public interface HasRootNode {

    Optional<Node> getRootNode();
}
