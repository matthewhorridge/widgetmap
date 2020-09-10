package edu.stanford.protege.widgetmap.shared.node;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;

import javax.annotation.Nonnull;

@AutoValue
@JsonTypeName("String")
@GwtCompatible(serializable = true)
public abstract class StringNodePropertyValue implements NodePropertyValue {

    private static final String VALUE = "value";

    @Nonnull
    @JsonCreator
    public static StringNodePropertyValue get(@Nonnull @JsonProperty(VALUE) String value) {
        return new AutoValue_StringNodePropertyValue(value);
    }

    @JsonProperty(VALUE)
    public abstract String getValue();
}
