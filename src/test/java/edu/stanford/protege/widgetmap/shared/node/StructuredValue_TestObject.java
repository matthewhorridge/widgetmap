package edu.stanford.protege.widgetmap.shared.node;

import com.fasterxml.jackson.annotation.*;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

/** @noinspection GwtInconsistentSerializableClass*/
@JsonTypeName("StructuredValue")
public class StructuredValue_TestObject implements NodePropertyValue {

    private int number;

    private String name;

    @JsonCreator
    public StructuredValue_TestObject(@JsonProperty("number") int number,
                                      @JsonProperty("name") String name) {
        this.number = number;
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return name.hashCode() + number;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof StructuredValue_TestObject)) {
            return false;
        }
        StructuredValue_TestObject other = (StructuredValue_TestObject) obj;
        return other.number == this.number && other.name.equals(this.name);
    }
}
