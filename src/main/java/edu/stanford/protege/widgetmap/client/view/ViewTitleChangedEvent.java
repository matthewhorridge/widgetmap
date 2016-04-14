package edu.stanford.protege.widgetmap.client.view;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/12/2013
 */
public class ViewTitleChangedEvent extends GwtEvent<ViewTitleChangedHandler> {

    private static final Type<ViewTitleChangedHandler> TYPE = new Type<ViewTitleChangedHandler>();

    private String viewTitle;

    public ViewTitleChangedEvent(String viewTitle) {
        this.viewTitle = viewTitle;
    }

    public String getViewTitle() {
        return viewTitle;
    }

    public static Type<ViewTitleChangedHandler> getType() {
        return TYPE;
    }

    @Override
    public Type<ViewTitleChangedHandler> getAssociatedType() {
        return TYPE;
    }

    /**
     * Should only be called by {@link com.google.gwt.event.shared.HandlerManager}. In other words, do not use
     * or call.
     *
     * @param handler handler
     */
    @Override
    protected void dispatch(ViewTitleChangedHandler handler) {
        handler.handleViewTitleChanged(this);
    }

    @Override
    public int hashCode() {
        return "ViewTitleChangedEvent".hashCode() + viewTitle.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ViewTitleChangedEvent)) {
            return false;
        }
        ViewTitleChangedEvent other = (ViewTitleChangedEvent) obj;
        return this.viewTitle.equals(other.viewTitle);
    }
}
