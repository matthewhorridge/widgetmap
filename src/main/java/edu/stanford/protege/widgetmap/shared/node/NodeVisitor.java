package edu.stanford.protege.widgetmap.shared.node;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/12/2013
 */
public interface NodeVisitor<O> {

    O visit(TerminalNode terminalNode);

    O visit(ParentNode parentNode);

}
