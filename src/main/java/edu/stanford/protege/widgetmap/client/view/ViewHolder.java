package edu.stanford.protege.widgetmap.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.HasCloseHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.protege.widgetmap.client.Messages;
import edu.stanford.protege.widgetmap.shared.node.HasNodeProperties;
import edu.stanford.protege.widgetmap.shared.node.NodeProperties;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/12/2013
 */
public class ViewHolder extends Composite implements HasCloseHandlers<Widget>, HasNodeProperties {

    @UiField(provided = true)
    Widget widget;

    @UiField
    Label viewLabel;

    @UiField
    HTMLPanel closeButton;

    @UiField
    Widget buttonBar;

    private NodeProperties nodeProperties = NodeProperties.emptyNodeProperties();

    private HandlerRegistration viewTitleChangedHandlerRegistration;

    private HandlerRegistration closeButtonHandlerRegistration;

    public static final Messages MESSAGES = GWT.create(Messages.class);

    interface ViewHolderUIBinder extends UiBinder<HTMLPanel, ViewHolder> {

    }

    private static ViewHolderUIBinder uiBinder = GWT.create(ViewHolderUIBinder.class);

    private boolean closeable = true;

    public ViewHolder(IsWidget childWidget, NodeProperties nodeProperties) {
        this.widget = childWidget.asWidget();
        this.nodeProperties = nodeProperties;
        HTMLPanel rootElement = uiBinder.createAndBindUi(this);
        initWidget(rootElement);
//        setSize("100%", "100%");
        if (childWidget instanceof HasViewTitle) {
            viewLabel.setText(((HasViewTitle) childWidget).getViewTitle());
        }
        if (childWidget instanceof HasViewTitleChangedHandlers) {
            viewTitleChangedHandlerRegistration = ((HasViewTitleChangedHandlers) childWidget).addViewTitleChangedHandler(new ViewTitleChangedHandler() {
                public void handleViewTitleChanged(ViewTitleChangedEvent event) {
                    viewLabel.setText(event.getViewTitle());
                }
            });
        }
        closeButtonHandlerRegistration = closeButton.addDomHandler(event -> fireCloseEvent(), ClickEvent.getType());
        closeButton.setTitle(MESSAGES.close());
    }

    public void setViewLabel(String label) {
        viewLabel.setText(label);
    }

    public boolean isCloseable() {
        return closeable;
    }

    public void setCloseable(boolean closeable) {
        this.closeable = closeable;
        closeButton.setVisible(closeable);
    }

    /**
     * Adds a {@link com.google.gwt.event.logical.shared.CloseEvent} handler.
     *
     * @param handler the handler
     * @return the registration for the event
     */
    public HandlerRegistration addCloseHandler(CloseHandler<Widget> handler) {
        return addHandler(handler, CloseEvent.getType());
    }

    private void fireCloseEvent() {
        if (viewTitleChangedHandlerRegistration != null) {
            viewTitleChangedHandlerRegistration.removeHandler();
        }
        if (closeButtonHandlerRegistration != null) {
            closeButtonHandlerRegistration.removeHandler();
        }
        CloseEvent.fire(this, this);
    }

    @Override
    public NodeProperties getNodeProperties() {
        return nodeProperties;
    }
}
