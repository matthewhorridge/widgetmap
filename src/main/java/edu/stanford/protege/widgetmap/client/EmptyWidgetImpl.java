package edu.stanford.protege.widgetmap.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasText;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 20/06/2014
 */
public class EmptyWidgetImpl extends Composite implements EmptyWidget {

    interface EmptyWidgetImplUiBinder extends UiBinder<HTMLPanel, EmptyWidgetImpl> {

    }

    private static EmptyWidgetImplUiBinder ourUiBinder = GWT.create(EmptyWidgetImplUiBinder.class);

    @UiField
    protected HasText hasText;

    public EmptyWidgetImpl() {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
    }

    @Override
    public void clearText() {
        hasText.setText("");
    }

    @Override
    public void setText(String text) {
        hasText.setText(checkNotNull(text));
    }
}
