package edu.stanford.protege.widgetmap.client.split;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.layout.client.Layout;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import edu.stanford.protege.widgetmap.client.*;
import edu.stanford.protege.widgetmap.resources.WidgetMapClientBundle;
import edu.stanford.protege.widgetmap.shared.node.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.google.gwt.dom.client.Style.Unit.PX;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/12/2013
 */
public class PolySplitLayoutPanel extends Panel implements ProvidesResize, RequiresResize, HasWeights, HasWeightsChangedHandlers {

    public static final int SPLITTER_WIDTH = 1;

    public static final int MIN_CLIENT_SIZE = 20;

    private WidgetCollection children = new WidgetCollection(this);

    private Layout layout;

    private Direction direction = Direction.COLUMN;

    public PolySplitLayoutPanel(Direction direction) {
        Element mainElement = Document.get().createDivElement();
        setElement(mainElement);
        setSize("100%", "100%");
        addStyleName(WidgetMapClientBundle.BUNDLE.style().polySplitPanel());
        this.direction = direction;
        layout = new Layout(getElement());
        ensureGlassElementExists();

    }

    @Override
    public void add(Widget child) {
        throw new RuntimeException("children should be added with add(IsWidget, double)");
    }

    @Override
    public void add(IsWidget child) {
        throw new RuntimeException("children should be added with add(IsWidget, double)");
    }

    public Direction getDirection() {
        return direction;
    }

    public void add(final IsWidget widget, double weight) {
        add(widget.asWidget(), weight);
    }

    @Override
    public List<Double> getWeights() {
        List<Double> result = new ArrayList<Double>();
        for(ChildWidget childWidget : getChildren()) {
            result.add(childWidget.getWeight());
        }
        return result;
    }

    public static class ChildWidget {

        private IsWidget widget;

        private double weight;

        public ChildWidget(IsWidget widget, double weight) {
            this.widget = widget;
            this.weight = weight;
        }

        public IsWidget getWidget() {
            return widget;
        }

        public double getWeight() {
            return weight;
        }
    }

    /**
     * Gets the child widgets and their weights.
     * @return An ordered map of the child widgets
     */
    public List<ChildWidget> getChildren() {
        List<ChildWidget> result = new ArrayList<ChildWidget>();
        for(int i = 0; i < children.size(); i++) {
            Widget widget = children.get(i);
            if(!(widget instanceof Splitter)) {
                double weight = getWidgetWeight(i) / getTotalWeight();
                result.add(new ChildWidget(widget, weight));
            }
        }
        return result;
    }



    private void add(final Widget widget, double weight) {
        final int widgetCount = children.size();
        if (widgetCount > 0) {
            createAndAddSplitter();
        }
        addWidgetAndCreateLayoutData(widget, weight);
        setupWidgetStyle(widget);
    }

    private void createAndAddSplitter() {
        Splitter splitter = createSplitter();
        // Logical attach
        children.add(splitter);
        // Physical attach
        Layout.Layer splitterLayer = layout.attachChild(splitter.getElement());
        splitter.setLayoutData(new SplitterLayoutData(splitterLayer));
        // Adopt
        adopt(splitter);
    }

    /*
     * COPIED FROM PANEL
     *
     * Adds a child widget.
     *
     * <p>
     * <b>How to Override this Method</b>
     * </p>
     * <p>
     * There are several important things that must take place in the correct
     * order to properly add or insert a Widget to a Panel. Not all of these steps
     * will be relevant to every Panel, but all of the steps must be considered.
     * <ol>
     * <li><b>Validate:</b> Perform any sanity checks to ensure the Panel can
     * accept a new Widget. Examples: checking for a valid index on insertion;
     * checking that the Panel is not full if there is a max capacity.</li>
     * <li><b>Adjust for Reinsertion:</b> Some Panels need to handle the case
     * where the Widget is already a child of this Panel. Example: when performing
     * a reinsert, the index might need to be adjusted to account for the Widget's
     * removal. See {@link ComplexPanel#adjustIndex(Widget, int)}.</li>
     * <li><b>Detach Child:</b> Remove the Widget from its existing parent, if
     * any. Most Panels will simply call {@link Widget#removeFromParent()} on the
     * Widget.</li>
     * <li><b>Logical Attach:</b> Any state variables of the Panel should be
     * updated to reflect the addition of the new Widget. Example: the Widget is
     * added to the Panel's {@link WidgetCollection} at the appropriate index.</li>
     * <li><b>Physical Attach:</b> The Widget's Element must be physically
     * attached to the Panel's Element, either directly or indirectly.</li>
     * <li><b>Adopt:</b> Call {@link #adopt(Widget)} to finalize the add as the
     * very last step.</li>
     * </ol>
     * </p>
     */
    private void addWidgetAndCreateLayoutData(Widget widget, double weight) {
        if(widget.getParent() == this) {
            throw new RuntimeException("Widget is already parented to this widget! " + this);
        }
        Layout.Layer widgetLayer = insertInternal(widget, children.size());
        // Set layout data for every widget.  This basically holds on to its layer and some other book keeping stuff.
        WidgetLayoutData layoutData = new WidgetLayoutData(weight, widgetLayer);
        widget.setLayoutData(layoutData);
    }

    private void setupWidgetStyle(Widget widget) {
        widget.addStyleName(WidgetMapClientBundle.BUNDLE.style().polySplitPanelBoxSizing());
    }

    /**
     * Removes a child widget.
     * <p/>
     * <p>
     * <b>How to Override this Method</b>
     * </p>
     * <p>
     * There are several important things that must take place in the correct
     * order to properly remove a Widget from a Panel. Not all of these steps will
     * be relevant to every Panel, but all of the steps must be considered.
     * <ol>
     * <li><b>Validate:</b> Make sure this Panel is actually the parent of the
     * child Widget; return <code>false</code> if it is not.</li>
     * <li><b>Orphan:</b> Call {@link #orphan(com.google.gwt.user.client.ui.Widget)} first while the child
     * Widget is still attached.</li>
     * <li><b>Physical Detach:</b> Adjust the DOM to account for the removal of
     * the child Widget. The Widget's Element must be physically removed from the
     * DOM.</li>
     * <li><b>Logical Detach:</b> Update the Panel's state variables to reflect
     * the removal of the child Widget. Example: the Widget is removed from the
     * Panel's {@link com.google.gwt.user.client.ui.WidgetCollection}.</li>
     * </ol>
     * </p>
     *
     * @param child the widget to be removed
     * @return <code>true</code> if the child was present
     */
    @Override
    public boolean remove(Widget child) {
        if (!children.contains(child)) {
            return false;
        }
        if (child instanceof Splitter) {
            throw new RuntimeException("Callers should not remove splitters directly");
        }
        if (children.size() > 1) {
            removeAdjacentSplitter(child);
        }
        removeInternal(child);
        doLayout();
        return true;
    }

    /**
     * Internal implementation to remove any child widget
     *
     * @param child The child widget to be removed.
     */
    private void removeInternal(Widget child) {
        orphan(child);
        // Physical detach
        Layout.Layer layer = getLayer(child);
        layout.removeChild(layer);
        // Logical removal
        children.remove(child);
    }

    private Layout.Layer insertInternal(Widget widget, int beforeIndex) {
        // Detach from any previous parent
        widget.removeFromParent();
        // Logical attach.
        children.insert(widget, beforeIndex);
        // Physical attach
        Layout.Layer widgetLayer = layout.attachChild(widget.getElement());
        // Final step.  We have to call adopt()!
        adopt(widget);
        return widgetLayer;
    }

    /**
     * Removes one of the {@code Splitter} widgets that is adjacent to the specified child.  If there are no adjacent
     * {@code Splitter} widgets then no widgets are removed.  If the specified child is a {@code Splitter} widget
     * then a runtime exception is thrown.
     *
     * @param child The child.
     * @throws RuntimeException if {@code child} is a {@code Splitter}.
     */
    private void removeAdjacentSplitter(Widget child) {
        if (child instanceof Splitter) {
            throw new RuntimeException("child must not be a Splitter widget");
        }
        final int childIndex = children.indexOf(child);
        if (childIndex == -1) {
            return;
        }
        if (isFirstWidgetIndex(childIndex)) {
            // First widget.  Remove the splitter to the right.
            int nextIndex = childIndex + 1;
            if (nextIndex < children.size()) {
                final Widget nextSibling = children.get(nextIndex);
                removeInternal(nextSibling);
            }
        }
        else {
            // Remove the splitter to the left
            int previousIndex = childIndex - 1;
            if (previousIndex > -1) {
                final Widget previousSibling = children.get(previousIndex);
                removeInternal(previousSibling);
            }
        }
    }

    private Layout.Layer getLayer(Widget widget) {
        AbstractLayoutData layoutData = (AbstractLayoutData) widget.getLayoutData();
        return layoutData.getLayer();
    }

    /**
     * Gets an iterator for the contained widgets. This iterator is required to
     * implement {@link java.util.Iterator#remove()}.
     */
    public Iterator<Widget> iterator() {
        return children.iterator();
    }

//    public void splitWidgetWith(Widget widget, Widget with) {
//        int widgetIndex = children.indexOf(widget);
//        WidgetLayoutData widgetLayoutData = (WidgetLayoutData) widget.getLayoutData();
//        final double halfWeight = widgetLayoutData.getWeight() / 2;
//        widgetLayoutData.setWeight(halfWeight);
//        final Splitter splitter = createSplitter();
//        Layout.Layer splitterLayer = insertInternal(splitter, widgetIndex + 1);
//        splitter.setLayoutData(new SplitterLayoutData(splitterLayer));
//        Layout.Layer layer = insertInternal(with, widgetIndex + 2);
//        with.setLayoutData(new WidgetLayoutData(halfWeight, layer));
//        setupWidgetStyle(with);
//        doLayout();
//    }
//
//    private void replaceWidgetWith(Widget widgetToRemove, Widget widgetToAdd) {
//        int index = children.indexOf(widgetToRemove);
//        removeInternal(widgetToRemove);
//        WidgetLayoutData existingLayoutData = (WidgetLayoutData) widgetToRemove.getLayoutData();
//        Layout.Layer widgetLayer = insertInternal(widgetToAdd, index);
//        final WidgetLayoutData newLayoutData = existingLayoutData.createCopyWithLayer(widgetLayer);
//        widgetToAdd.setLayoutData(newLayoutData);
//        setupWidgetStyle(widgetToAdd);
//        doLayout();
//    }

    public boolean isEmpty() {
        return children.size() == 0;
    }

    private void dumpLayoutData() {
        for (Widget w : children) {
            AbstractLayoutData layoutData = (AbstractLayoutData) w.getLayoutData();
            GWT.log(layoutData.toString());
        }
    }

    private Splitter createSplitter() {
        if (direction == Direction.ROW) {
            return new HorizontalSplitter();
        }
        else {
            return new VerticalSplitter();
        }
    }

    private double getTotalWeight() {
        double totalWeight = 0;
        for (int i = 0; i < children.size(); i++) {
            totalWeight += getWidgetWeight(i);
        }
        return totalWeight;
    }

    private double getWidgetWeight(int widgetIndex) {
        Widget widget = children.get(widgetIndex);
        if (widget instanceof HasFixedPrimaryAxisSize) {
            return 0;
        }
        Object layoutData = widget.getLayoutData();
        if (layoutData instanceof WidgetLayoutData) {
            return ((WidgetLayoutData) layoutData).getWeight();
        }
        return 0;
    }

    public void doLayout() {
        if (direction == Direction.ROW) {
            new HorizontalLayoutMachinery().doLayout();
        }
        else {
            new VerticalLayoutMachinery().doLayout();
        }
        resizeChildrenIfNecessary();
    }

    private void resizeChildrenIfNecessary() {
        for (Widget widget : children) {
            if (widget instanceof RequiresResize) {
                ((RequiresResize) widget).onResize();
            }
        }
    }

    /**
     * This method must be called whenever the implementor's widgetCount has been
     * modified.
     */
    public void onResize() {
        doLayout();
    }

    /**
     * Determines if the specified index is the index of the first widget in the child collection.
     *
     * @param index The index to test for.
     * @return {@code true} if the index is the first index i.e. index == 0, otherwise false.
     */
    private boolean isFirstWidgetIndex(int index) {
        return index == 0;
    }

    /**
     * Determines if the specified index is the index of the last widget in the child collection
     *
     * @param index The index to test for.
     * @return {@code true} if the index is the last index i.e. index == children.size() - 1, otherwise false.
     */
    private boolean isLastWidgetIndex(int index) {
        return index == children.size() - 1;
    }

    private Widget getPreviousResizableWidgetBeforeIndex(int index) {
        for (int i = index - 1; i > -1; i--) {
            Widget widget = children.get(i);
            if (!(widget instanceof HasFixedPrimaryAxisSize)) {
                return widget;
            }
        }
        return null;
    }

    private Widget getNextResizableWidgetAfterIndex(int index) {
        for (int i = index + 1; i < children.size(); i++) {
            Widget widget = children.get(i);
            if (!(widget instanceof HasFixedPrimaryAxisSize)) {
                return widget;
            }
        }
        return null;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private abstract class AbstractLayoutData {

        private Layout.Layer layer;

        private AbstractLayoutData(Layout.Layer layer) {
            this.layer = layer;
        }

        public Layout.Layer getLayer() {
            return layer;
        }
    }

    private class WidgetLayoutData extends AbstractLayoutData {

        private double initialWeight;

        private double weight;

        private WidgetLayoutData(double initialWeight, Layout.Layer layer) {
            super(layer);
            this.initialWeight = initialWeight;
            this.weight = initialWeight;
        }

        public double getInitialWeight() {
            return initialWeight;
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper("WidgetLayoutData")
                    .add("initialWeight", initialWeight)
                    .add("weight", weight)
                    .toString();
        }

        public WidgetLayoutData createCopyWithLayer(Layout.Layer layer) {
            WidgetLayoutData layoutData = new WidgetLayoutData(initialWeight, layer);
            layoutData.setWeight(weight);
            return layoutData;
        }
    }

    private class SplitterLayoutData extends AbstractLayoutData {

        private SplitterLayoutData(Layout.Layer layer) {
            super(layer);
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper("SplitterLayoutData").toString();
        }
    }

    private void updateWeights() {
        if (direction == Direction.ROW) {
            double totalWidth = getElement().getClientWidth();
            double totalFixedWidth = getTotalFixedPrimaryAxisSize();
            double totalWidgetWidth = totalWidth - totalFixedWidth;
            for (Widget widget : children) {
                if (!(widget instanceof HasFixedPrimaryAxisSize)) {
                    Object layoutData = widget.getLayoutData();
                    if (layoutData instanceof WidgetLayoutData) {
                        double widgetWidth = widget.getElement().getClientWidth();
                        double percentage = widgetWidth / totalWidgetWidth;
                        ((WidgetLayoutData) layoutData).setWeight(percentage);
                    }
                }
            }
        }
        else {
            double totalSize = getElement().getClientHeight();
            double totalFixedWidth = getTotalFixedPrimaryAxisSize();
            double totalWidgetSize = totalSize - totalFixedWidth;
            for (Widget widget : children) {
                Object layoutData = widget.getLayoutData();
                if (layoutData instanceof WidgetLayoutData) {
                    double widgetSize = widget.getElement().getClientHeight();
                    double percentage = widgetSize / totalWidgetSize;
                    ((WidgetLayoutData) layoutData).setWeight(percentage);
                }
            }
        }
        fireEvent(new WeightsChangedEvent(this));
    }

    private int getTotalFixedPrimaryAxisSize() {
        int result = 0;
        for (Widget widget : children) {
            if (widget instanceof HasFixedPrimaryAxisSize) {
                result += ((HasFixedPrimaryAxisSize) widget).getFixedPrimaryAxisSize();
            }
        }
        return result;
    }

    @Override
    public void addWeightsChangedHandler(WeightsChangedHandler handler) {
        addHandler(handler, WeightsChangedEvent.TYPE);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static Element glassElem;

    private static void ensureGlassElementExists() {
        if (glassElem == null) {
            glassElem = Document.get().createDivElement();
            glassElem.getStyle().setPosition(Style.Position.ABSOLUTE);
            glassElem.getStyle().setTop(0, PX);
            glassElem.getStyle().setLeft(0, PX);
            glassElem.getStyle().setMargin(0, PX);
            glassElem.getStyle().setPadding(0, PX);
            glassElem.getStyle().setBorderWidth(0, PX);
            // We need to set the background color or mouse events will go right
            // through the glassElem. If the SplitPanel contains an iframe, the
            // iframe will capture the event and the slider will stop moving.
            glassElem.getStyle().setProperty("background", "white");
            glassElem.getStyle().setOpacity(0.0);
        }
    }

    private static void showGlassElement() {
        /*
        * Resize glassElem to take up the entire scrollable window area,
        * which is the greater of the scroll widgetCount and the client widgetCount.
        */
        int width = Math.max(Window.getClientWidth(), Document.get().getScrollWidth());
        int height = Math.max(Window.getClientHeight(), Document.get().getScrollHeight());
        glassElem.getStyle().setHeight(height, PX);
        glassElem.getStyle().setWidth(width, PX);
        Document.get().getBody().appendChild(glassElem);
    }

    private static void hideGlassElement() {
        glassElem.removeFromParent();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static enum ResizeMode {
        MOVER_LOWER,
        MOVER_UPPER
    }

    private abstract class Splitter extends Widget implements HasFixedPrimaryAxisSize {

        private boolean mouseDown = false;

        private int offset = 0;

        private int clientDown = 0;

        public Splitter() {
            DivElement splitterElement = Document.get().createDivElement();
            DivElement interactionElement = Document.get().createDivElement();
            splitterElement.appendChild(interactionElement);
            setElement(splitterElement);
            sinkEvents(Event.ONMOUSEDOWN | Event.ONMOUSEUP | Event.ONMOUSEMOVE | Event.ONDBLCLICK);
            ensureGlassElementExists();
            addStyleName(WidgetMapClientBundle.BUNDLE.style().polySplitPanelMovableSplitter());
        }

        public int getFixedPrimaryAxisSize() {
            return SPLITTER_WIDTH;
        }

        public Widget getLeftWidget() {
            final int prevWidgetIndex = children.indexOf(this) - 1;
            return children.get(prevWidgetIndex);
        }

        public Layout.Layer getLeftLayer() {
            return ((AbstractLayoutData) getLeftWidget().getLayoutData()).getLayer();
        }

        public Widget getRightWidget() {
            final int nextWidgetIndex = children.indexOf(this) + 1;
            return children.get(nextWidgetIndex);
        }

        public Layout.Layer getRightLayer() {
            return ((AbstractLayoutData) getRightWidget().getLayoutData()).getLayer();
        }

        public Layout.Layer getSplitterLayer() {
            return ((SplitterLayoutData) getLayoutData()).getLayer();
        }

        @Override
        public void onBrowserEvent(Event event) {
            switch (event.getTypeInt()) {
                case Event.ONMOUSEDOWN:
                    mouseDown = true;
                    showGlassElement();
                    clientDown = getEventPosition(event);
                    offset = getEventPosition(event) - getAbsolutePosition();
                    Event.setCapture(getElement());   // Ask the element to receive all mouse events until releaseCapture
                    event.preventDefault();
                    if (isMovable()) {
                        addStyleName(WidgetMapClientBundle.BUNDLE.style().polySplitPanelMovableSplitterMove());
                    }
                    break;
                case Event.ONMOUSEUP:
                    mouseDown = false;
                    hideGlassElement();
                    Event.releaseCapture(getElement());
                    event.preventDefault();
                    if (isMovable()) {
                        removeStyleName(WidgetMapClientBundle.BUNDLE.style().polySplitPanelMovableSplitterMove());
                    }
                    break;
                case Event.ONMOUSEMOVE:
                    if (mouseDown) {
                        final int eventPosition = getEventPosition(event);
                        int diff = eventPosition - clientDown;
                        int delta = eventPosition - getAbsolutePosition() - offset;
                        moveByDelta(delta);
                        event.preventDefault();
                    }
                    break;
            }
        }

        protected abstract int getEventPosition(Event event);

        protected abstract int getAbsolutePosition();

        /**
         * Move this splitter by the specified number of pixels
         *
         * @param delta The number of pixels
         */
        private void moveByDelta(int delta) {
            if (!isMovable()) {
                return;
            }
            int croppedDelta = cropDelta(delta);
            if (croppedDelta == 0) {
                return;
            }
            shunt(croppedDelta);
            layout.layout();
            updateWeights();
            resizeChildrenIfNecessary();
        }

        private void shunt(int delta) {
            final int myIndex = children.indexOf(this);
            // Move fixed and stretch non-fixed (until we find a non-fixed)
            // Left widgets
            for (int i = myIndex - 1; i > -1; i--) {
                Widget widget = children.get(i);
                shuntWidget(widget, delta, ResizeMode.MOVER_UPPER);
                if (!(widget instanceof HasFixedPrimaryAxisSize)) {
                    break;
                }
            }
            // Right widgets, including this splitter
            for (int i = myIndex; i < children.size(); i++) {
                Widget widget = children.get(i);
                shuntWidget(widget, delta, ResizeMode.MOVER_LOWER);
                if (!(widget instanceof HasFixedPrimaryAxisSize)) {
                    break;
                }
            }
        }

        private void shuntWidget(Widget widget, int delta, ResizeMode mode) {
            AbstractLayoutData layoutData = (AbstractLayoutData) widget.getLayoutData();
            Layout.Layer layer = layoutData.getLayer();
            final Element element = layer.getContainerElement();
            if (widget instanceof HasFixedPrimaryAxisSize) {
                // Move
                moveElement(element, layer, delta);
            }
            else {
                // Stretch (Shrink)
                resizeElement(element, layer, mode, delta);
            }
        }

        private void resizeElement(Element element, Layout.Layer layer, ResizeMode mode, int delta) {
            final int position;
            final int size;
            if (mode == ResizeMode.MOVER_UPPER) {
                // Position is fixed, size changes to accommodate move of upper by delta
                position = getOffsetPosition(element);
                size = getClientSize(element) + delta;
            }
            else {
                // Position moves and size changes to accommodate move of lower by delta
                position = getOffsetPosition(element) + delta;
                size = getClientSize(element) - delta;
            }
            setPositionAndSize(layer, position, size);
        }

        private void moveElement(Element element, Layout.Layer layer, int delta) {
            int position = getOffsetPosition(element) + delta;
            int size = getClientSize(element);
            setPositionAndSize(layer, position, size);
        }

        private int cropDelta(int delta) {
            if (delta == 0) {
                return 0;
            }
            if (delta > 0) {
                Widget nextResizable = getNextResizableWidget();
                if (nextResizable == null) {
                    return 0;
                }
                Element element = nextResizable.getElement();
                int maxDelta = getClientSize(element) - MIN_CLIENT_SIZE;
                return delta < maxDelta ? delta : maxDelta;
            }
            else {
                Widget prevResizable = getPreviousResizableWidget();
                if (prevResizable == null) {
                    return 0;
                }
                Element element = prevResizable.getElement();
                int maxDelta = -getClientSize(element) + MIN_CLIENT_SIZE;
                return delta > maxDelta ? delta : maxDelta;
            }
        }

        private Widget getPreviousResizableWidget() {
            return getPreviousResizableWidgetBeforeIndex(children.indexOf(this));
        }

        private Widget getNextResizableWidget() {
            return getNextResizableWidgetAfterIndex(children.indexOf(this));
        }

        private boolean isMovable() {
            return getPreviousResizableWidget() != null && getNextResizableWidget() != null;
        }

        protected abstract int getOffsetPosition(Element element);

        protected abstract int getClientSize(Element element);

        protected abstract void setPositionAndSize(Layout.Layer layer, int position, int size);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private class HorizontalSplitter extends Splitter {

        private HorizontalSplitter() {
        }

        @Override
        protected int getEventPosition(Event event) {
            return event.getClientX();
        }

        @Override
        protected int getAbsolutePosition() {
            return getElement().getAbsoluteLeft();
        }

        @Override
        protected int getOffsetPosition(Element element) {
            return element.getOffsetLeft();
        }

        @Override
        protected int getClientSize(Element element) {
            return element.getClientWidth();
        }

        @Override
        protected void setPositionAndSize(Layout.Layer layer, int position, int size) {
            layer.setLeftWidth(position, PX, size, PX);
        }
    }

    private class VerticalSplitter extends Splitter {

        private VerticalSplitter() {
            Style style = getElement().getStyle();
        }

        @Override
        protected int getEventPosition(Event event) {
            return event.getClientY();
        }

        @Override
        protected int getAbsolutePosition() {
            return getElement().getAbsoluteTop();
        }

        @Override
        protected void setPositionAndSize(Layout.Layer layer, int position, int size) {
            layer.setTopHeight(position, PX, size, PX);
        }

        @Override
        protected int getOffsetPosition(Element element) {
            return element.getOffsetTop();
        }

        @Override
        protected int getClientSize(Element element) {
            return element.getClientHeight();
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private interface LayoutMachinery {

        void doLayout();
    }

    private abstract class AbstractLayoutMachinery implements LayoutMachinery {

        public void doLayout() {
            int primaryAxisCurrentPosition = 0;
            double totalWeight = getTotalWeight();
            final int clientSpace = getPrimaryAxisClientLength();
            final int fixedSpace = getTotalFixedPrimaryAxisSize();
            final int availableWidgetSpace = clientSpace - fixedSpace;
            int consumedSpace = 0;
            for (int i = 0; i < children.size(); i++) {
                Widget widget = children.get(i);
                Object layoutData = widget.getLayoutData();
                int widgetLength;
                if (widget instanceof HasFixedPrimaryAxisSize) {
                    // Length is fixed - does not depend on weight
                    widgetLength = ((HasFixedPrimaryAxisSize) widget).getFixedPrimaryAxisSize();
                }
                else {
                    // Length depends on weight
                    double widgetWeight = 0;
                    if (layoutData instanceof WidgetLayoutData) {
                        widgetWeight = ((WidgetLayoutData) layoutData).getWeight();
                    }
                    double percentageWeight = widgetWeight / totalWeight;
                    widgetLength = (int) (availableWidgetSpace * percentageWeight);
                }
                if (isLastWidgetIndex(i)) {
                    // Do rounding errors mean we need to stretch or shrink by a few pixels on the last child?
                    int remainder = clientSpace - consumedSpace - widgetLength;
                    widgetLength += remainder;
                }
                final AbstractLayoutData layoutLayer = (AbstractLayoutData) widget.getLayoutData();
                if (layoutLayer != null) {
                    Layout.Layer layer = layoutLayer.getLayer();
                    if (layer != null) {
                        setLayerPositionAndSize(primaryAxisCurrentPosition, widgetLength, layer);
                        primaryAxisCurrentPosition += widgetLength;
                    }
                }
                consumedSpace += widgetLength;
                if (widget instanceof Splitter) {
                    boolean moveable = ((Splitter) widget).isMovable();
                    WidgetMapClientBundle.WidgetMapCss style = WidgetMapClientBundle.BUNDLE.style();
                    if (moveable) {
                        widget.addStyleName(style.polySplitPanelMovableSplitter());
                        widget.removeStyleName(style.polySplitPanelFixedSplitter());
                        widget.addStyleName(getMovableSplitterStyleName());
                    }
                    else {
                        widget.addStyleName(style.polySplitPanelFixedSplitter());
                        widget.removeStyleName(style.polySplitPanelMovableSplitter());
                        widget.removeStyleName(getMovableSplitterStyleName());
                    }
                }
            }
            layout.layout();
        }

        protected abstract String getMovableSplitterStyleName();

        protected abstract void setLayerPositionAndSize(int position, int size, Layout.Layer layer);

        protected abstract int getPrimaryAxisClientLength();
    }

    private class HorizontalLayoutMachinery extends AbstractLayoutMachinery {

        @Override
        protected void setLayerPositionAndSize(int position, int size, Layout.Layer layer) {
            layer.setLeftWidth(position, PX, size, PX);
            layer.setTopHeight(0, PX, getElement().getClientHeight(), PX);
        }

        @Override
        protected int getPrimaryAxisClientLength() {
            return getElement().getClientWidth();
        }

        @Override
        protected String getMovableSplitterStyleName() {
            return WidgetMapClientBundle.BUNDLE.style().polySplitPanelMovableHorizontalSplitter();
        }
    }

    private class VerticalLayoutMachinery extends AbstractLayoutMachinery {

        @Override
        protected void setLayerPositionAndSize(int position, int size, Layout.Layer layer) {
            layer.setLeftWidth(0, PX, getElement().getClientWidth(), PX);
            layer.setTopHeight(position, PX, size, PX);
        }

        @Override
        protected int getPrimaryAxisClientLength() {
            return getElement().getClientHeight();
        }

        @Override
        protected String getMovableSplitterStyleName() {
            return WidgetMapClientBundle.BUNDLE.style().polySplitPanelMovableVerticalSplitter();
        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("PolySplitLayoutPanel")
                .add("childCount", children.size())
                .add("direction", direction).toString();
    }
}
