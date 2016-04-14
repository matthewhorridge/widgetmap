package edu.stanford.protege.widgetmap.client.split;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 06/12/2013
 */
public class WeightsChangedEvent extends GwtEvent<WeightsChangedHandler> {

    public static final Type<WeightsChangedHandler> TYPE = new Type<WeightsChangedHandler>();

    private HasWeights source;

    public WeightsChangedEvent(HasWeights source) {
        this.source = source;
    }

    public HasWeights getSource() {
        return source;
    }

    @Override
    public Type<WeightsChangedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(WeightsChangedHandler handler) {
        handler.handleWeightsChanged(this);
    }
}
