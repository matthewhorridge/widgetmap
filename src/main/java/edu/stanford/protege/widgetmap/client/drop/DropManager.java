package edu.stanford.protege.widgetmap.client.drop;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.*;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.*;
import edu.stanford.protege.widgetmap.client.Messages;
import edu.stanford.protege.widgetmap.resources.WidgetMapClientBundle;

import java.util.Optional;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/12/2013
 */
public class DropManager {

    private static final DropPanel dropPanel = new DropPanel();

    public static final int ANIMATION_DURATION = 300;

    public static final double OPACITY = 0.7;

    private static DropHandler dropHandler;

    public static void showDropElement(DropHandler dropHandler) {
        DropManager.dropHandler = dropHandler;
        RootPanel.get().add(dropPanel);
        dropPanel.getElement().getStyle().setOpacity(0.0);
        dropPanel.focus();
        Animation animation = new Animation() {
            @Override
            protected void onUpdate(double progress) {
                double opacity = OPACITY * progress;
                dropPanel.getElement().getStyle().setOpacity(opacity);
            }
        };
        animation.run(ANIMATION_DURATION);

    }

    public static void hideDropElement() {
        Animation animation = new Animation() {
            @Override
            protected void onUpdate(double progress) {
                double opacity = OPACITY - (OPACITY * progress);
                dropPanel.getElement().getStyle().setOpacity(opacity);
                if (progress == 1) {
                    RootPanel.get().remove(dropPanel);
                }
            }
        };
        animation.run(ANIMATION_DURATION);
    }

    private static class DropPanel extends Composite {

        private FlowPanel dropTargetIndicator = new FlowPanel();

        private FlowPanel dropPositionIndicator = new FlowPanel();

        public static final Messages MESSAGES = GWT.create(Messages.class);

        private Label dropHelpText = new Label(MESSAGES.dropText());

        private final FocusPanel focuser;

        private DropPanel() {
            final FlowPanel mainPanel = new FlowPanel();
            initWidget(mainPanel);
            WidgetMapClientBundle.WidgetMapCss style = WidgetMapClientBundle.BUNDLE.style();
            addStyleName(style.dropGlass());
            sinkEvents(Event.ONMOUSEDOWN | Event.ONMOUSEUP | Event.ONMOUSEMOVE | Event.ONDBLCLICK);
            mainPanel.add(dropTargetIndicator);
            dropTargetIndicator.addStyleName(style.dropTargetIndicator());
            mainPanel.add(dropPositionIndicator);
            dropPositionIndicator.addStyleName(style.dropSideIndicator());
            mainPanel.add(dropHelpText);
            dropHelpText.addStyleName(style.dropHelpText());
            focuser = new FocusPanel();
            mainPanel.add(focuser);
            focuser.addKeyUpHandler(new KeyUpHandler() {
                @Override
                public void onKeyUp(KeyUpEvent event) {
                    if(event.getNativeKeyCode() == KeyCodes.KEY_ESCAPE) {
                        cancelDrop();
                    }
                }
            });
        }

        public void focus() {
            focuser.setFocus(true);
        }


        @Override
        public void onBrowserEvent(Event event) {
            super.onBrowserEvent(event);
            int typeInt = event.getTypeInt();
            switch (typeInt) {
                case Event.ONMOUSEMOVE:
                    handleMouseMove(event);
                    break;
                case Event.ONMOUSEDOWN:
                    dropWidget(event);
                    hideDropElement();
                    break;
            }
        }

        private void handleMouseMove(Event mouseMoveEvent) {
            GWT.log("Handling mouse event: " + mouseMoveEvent);
            int x = mouseMoveEvent.getClientX();
            int y = mouseMoveEvent.getClientY();
            GWT.log("Mouse is at: (" + x + ", " + y + ")");
            Optional<Element> foundElement = findTargetElement(x, y);
            GWT.log("Target element: " + foundElement);
            if(!foundElement.isPresent()) {
                hideDropIndicator();
                return;
            }
            Element e = foundElement.get();
            if (e.getAbsoluteLeft() < x && x < e.getAbsoluteRight() && e.getAbsoluteTop() < y && y < e.getAbsoluteBottom()) {
                updateDropIndicator(x, y, e);
            }
        }

        private void updateDropIndicator(int x, int y, Element e) {
            GWT.log("Updating drop indicator");
            Style dropRectStyle = dropTargetIndicator.getElement().getStyle();
            int left = e.getAbsoluteLeft();
            int top = e.getAbsoluteTop();
            int right = e.getAbsoluteRight();
            int bottom = e.getAbsoluteBottom();
            DropLocation dropLocation = DropLocation.getDropLocation(x, y, e);
            dropRectStyle.setLeft(left - 2, Style.Unit.PX);
            dropRectStyle.setTop(top - 2, Style.Unit.PX);
            dropRectStyle.setWidth(e.getClientWidth() - 4, Style.Unit.PX);
            dropRectStyle.setHeight(e.getClientHeight() - 4, Style.Unit.PX);
            dropRectStyle.setVisibility(Style.Visibility.VISIBLE);

            int indX = 0;
            int indY = 0;
            int indW = 0;
            int indH = 0;
            switch (dropLocation) {
                case LEFT:
                    indX = left;
                    indY = top;
                    indH = bottom - top;
                    indW = (right - left) / 2;
                    break;
                case RIGHT:
                    indX = left + (right - left) / 2;
                    indY = top;
                    indH = bottom - top;
                    indW = (right - left) / 2;
                    break;
                case TOP:
                    indX = left;
                    indY = top;
                    indH = (bottom - top) / 2;
                    indW = (right - left);
                    break;
                case BOTTOM:
                    indX = left;
                    indY = top + (bottom - top) / 2;
                    indH = (bottom - top) / 2;
                    indW = (right - left);
                    break;
            }
            Style dropIndicatorStyle = dropPositionIndicator.getElement().getStyle();
            dropIndicatorStyle.setVisibility(Style.Visibility.VISIBLE);
            dropIndicatorStyle.setWidth(indW, Style.Unit.PX);
            dropIndicatorStyle.setHeight(indH, Style.Unit.PX);
            dropIndicatorStyle.setLeft(indX, Style.Unit.PX);
            dropIndicatorStyle.setTop(indY, Style.Unit.PX);
        }


        private void hideDropIndicator() {
            Style dropIndicatorStyle = dropPositionIndicator.getElement().getStyle();
            Style dropRectStyle = dropTargetIndicator.getElement().getStyle();
            dropRectStyle.setVisibility(Style.Visibility.HIDDEN);
            dropIndicatorStyle.setVisibility(Style.Visibility.HIDDEN);
        }

        private Optional<Element> findTargetElement(int x, int y) {
            BodyElement bodyElement = Document.get().getBody();
            return findElement(bodyElement, x, y);
        }

        private Optional<Element> findElement(Node startFrom, int x, int y) {
            GWT.log("[WidgetMap] findElement");
            for (int i = 0; i < startFrom.getChildCount(); i++) {
                Node e = startFrom.getChild(i);
                Element ee = (Element) e;
                GWT.log("[WidgetMap] Examining element: " + ee.getTagName());
                // Make sure it's not an svg element - in relation to this bug here:
                //     https://github.com/gwtproject/gwt/issues/9124
                if (!"svg".equals(ee.getTagName())) {
                    String className = ee.getClassName();
                    if (className != null && className.contains("drop-zone")) {
                        if (ee.getAbsoluteLeft() < x && x < ee.getAbsoluteRight() && ee.getAbsoluteTop() < y && y < ee.getAbsoluteBottom()) {
                            return Optional.of(ee);
                        }
                    }
                    Optional<Element> foundDesc = findElement(e, x, y);
                    if (foundDesc.isPresent()) {
                        return foundDesc;
                    }
                }
            }
            return Optional.empty();
        }

        private void dropWidget(Event event) {
            int x = event.getClientX();
            int y = event.getClientY();
            Optional<Element> element = findTargetElement(x, y);
            if(!element.isPresent()) {
                GWT.log("[WidgetMap][DropManager] In dropWidget but target element is null");
                return;
            }
            DropLocation dropLocation = DropLocation.getDropLocation(x, y, element.get());
            dropHandler.handleDrop(element.get(), dropLocation);
        }
    }

    private static void cancelDrop() {
        hideDropElement();
        dropHandler.handleDropCancel();
    }

}
