package edu.stanford.protege.widgetmap.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 10/06/2014
 */
public class MenuIcon extends Composite implements HasClickHandlers {

    interface MenuIconUiBinder extends UiBinder<HTMLPanel, MenuIcon> {

    }

    private static MenuIconUiBinder ourUiBinder = GWT.create(MenuIconUiBinder.class);

    public MenuIcon() {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
        sinkEvents(Event.ONCLICK);
    }

    @Override
    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return addHandler(handler, ClickEvent.getType());
    }
}
