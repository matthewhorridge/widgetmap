package edu.stanford.protege.widgetmap.client;

import com.google.common.base.Optional;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import edu.stanford.protege.widgetmap.client.view.ViewHolder;
import edu.stanford.protege.widgetmap.shared.node.*;

import java.util.Date;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class widgetmap implements EntryPoint {

//    private WidgetMapPanel widget;

//    private boolean undoing = false;

//    private Stack<Optional<Node>> nodeStack = new Stack<Optional<Node>>();

//    private NodeHistoryManager historyManager = new NodeHistoryManager();

    public void onModuleLoad() {
//        final ParentNode root = new ParentNode(Direction.ROW);
//        root.addChild(new TerminalNode(), 0.4);
//        ParentNode right = new ParentNode(Direction.COLUMN);
//        ParentNode hn = new ParentNode(Direction.ROW);
//        NodeProperties props = NodeProperties.builder().setValue("class", "etc").build();
//        TerminalNode child = TerminalNode.builder().withProperties(props).build();
//        hn.addChild(child, 0.5);
//        hn.addChild(new TerminalNode(), 0.5);
//        right.addChild(hn, 0.4);
//        right.addChild(new TerminalNode(), 0.6);
//        root.addChild(right, 0.6);
//        WidgetMapRootWidget rootWidget = new WidgetMapRootWidget();
//        widget = new WidgetMapPanel(rootWidget, new WidgetMapPanelManager(rootWidget, new MyWidgetMapper()));
//        widget.setSize("100%", "100%");
//        widget.setRootNode(root);
//        RootPanel.get("slot1").add(widget);
//        Button dumpButton = new Button("Dump node");
//        RootPanel buttonBar = RootPanel.get("buttonbar");
//        buttonBar.add(dumpButton);
//        dumpButton.addClickHandler(new ClickHandler() {
//            @Override
//            public void onClick(ClickEvent event) {
//                System.out.println("----------------------------------------------------------------");
//                Optional<Node> rootNode = widget.getRootNode();
//                if (rootNode.isPresent()) {
//                    rootNode.get().accept(new NodeDumper());
//                }
//            }
//        });
//        Button dropButton = new Button("Drop");
//        buttonBar.add(dropButton);
//        dropButton.addClickHandler(new ClickHandler() {
//            @Override
//            public void onClick(ClickEvent event) {
//                handleDrop();
//            }
//        });
//        Button backButton = new Button("Back");
//        buttonBar.add(backButton);
//        backButton.addClickHandler(new ClickHandler() {
//            @Override
//            public void onClick(ClickEvent event) {
//                Optional<Node> node = historyManager.pop();
//                if (node.isPresent()) {
//                    undoing = true;
//                    widget.setRootNode(node.get());
//                    undoing = false;
//                }
//            }
//        });
//        widget.addRootNodeChangedHandler(new RootNodeChangedHandler() {
//            @Override
//            public void handleRootNodeChanged(RootNodeChangedEvent event) {
//                System.out.println("The node has changed " + new Date());
//                Optional<Node> from = event.getFrom();
//                if (from.isPresent() && event.getTo().isPresent()) {
//                    if (from.get().equalsIgnoreWeights(event.getTo().get())) {
//                        System.out.println("    Only the weights have changed");
//                    }
//                }
//                if(!undoing) {
//                    historyManager.handleNode(from);
//                }
//
//            }
//        });
    }

//    private void handleDrop() {
//        widget.doDrop(new TerminalNode());
//
//    }

//    private class MyWidgetMapper implements WidgetMapper {
//
//        public static final String DROP_ZONE = "drop-zone";
//
//        public IsWidget getWidget(final TerminalNode terminalNode) {
//
//            TextArea childWidget = new TextArea();
//            childWidget.setText(terminalNode.toString());
//            childWidget.setSize("100%", "100%");
////            Label xxxx = new Label("xxxx");
////            xxxx.setSize("100%", "100%");
////            xxxx.getElement().getStyle().setBorderStyle(Style.BorderStyle.SOLID);
////            xxxx.getElement().getStyle().setBorderWidth(1.0, Style.Unit.PX);
////            xxxx.getElement().getStyle().setBackgroundColor("cyan");
////            xxxx.addStyleName("drop-zone");
////            return xxxx;
//
//            final ViewHolder viewHolder = new ViewHolder(childWidget, terminalNode.getNodeProperties()) {
//
//                private boolean first = true;
//
//                @Override
//                protected void onAttach() {
//                    super.onAttach();
//                }
//            };
//            viewHolder.addStyleName(DROP_ZONE);
//
//            return viewHolder;
//
//        }
//    }

}
