package edu.stanford.protege.widgetmap.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 23/05/2014
 */
public interface WidgetMapClientBundle extends ClientBundle {

    public static final WidgetMapClientBundle BUNDLE = GWT.create(WidgetMapClientBundle.class);

    @Source("WidgetMap.css")
    WidgetMapCss style();

    public static interface WidgetMapCss extends CssResource {

        @ClassName("poly-split-panel-border")
        String polySplitPanelBorder();

        String animated();

        @ClassName("drop-help-text")
        String dropHelpText();

        @ClassName("poly-split-panel-fixed-splitter")
        String polySplitPanelFixedSplitter();

        @ClassName("poly-split-panel-movable-vertical-splitter")
        String polySplitPanelMovableVerticalSplitter();

        @ClassName("widget-holder")
        String widgetHolder();

        @ClassName("drop-glass")
        String dropGlass();

        @ClassName("poly-split-panel")
        String polySplitPanel();

        @ClassName("widget-holder-header")
        String widgetHolderHeader();

        @ClassName("poly-split-panel-movable-splitter-move")
        String polySplitPanelMovableSplitterMove();

        @ClassName("poly-split-panel-movable-horizontal-splitter")
        String polySplitPanelMovableHorizontalSplitter();

        @ClassName("widget-holder-label")
        String widgetHolderLabel();

        @ClassName("drop-side-indicator")
        String dropSideIndicator();

        @ClassName("poly-split-panel-movable-splitter")
        String polySplitPanelMovableSplitter();

        @ClassName("poly-split-panel-box-sizing")
        String polySplitPanelBoxSizing();

        @ClassName("drop-target-indicator")
        String dropTargetIndicator();


        String pulse();

        String hinge();
    }
}
