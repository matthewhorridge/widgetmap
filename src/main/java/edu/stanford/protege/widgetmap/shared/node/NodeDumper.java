package edu.stanford.protege.widgetmap.shared.node;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/12/2013
 */
public class NodeDumper implements NodeVisitor<Void> {

    private int depth = 0;

    @Override
    public Void visit(TerminalNode widgetNode) {
        write(widgetNode.toString());
        return null;
    }

    @Override
    public Void visit(ParentNode parentNode) {
        write(parentNode.getDirection().toString());
        depth++;
        dumpChildren(parentNode);
        depth--;
        return null;
    }

    private void dumpChildren(ParentNode parentNode) {
        for(int i = 0; i < parentNode.getChildCount(); i++) {
            Node childNode = parentNode.getChildAt(i);
            double weight = parentNode.getWeightAt(i);
            write("W: " + weight);
            childNode.accept(this);
        }
    }




    private void write(String s) {
        indent();
        System.out.println(s);
    }

    private void indent() {
        for(int i =0 ; i < depth; i++) {
            System.out.print("    ");
        }
    }
}
