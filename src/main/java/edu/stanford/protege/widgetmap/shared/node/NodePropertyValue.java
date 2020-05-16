package edu.stanford.protege.widgetmap.shared.node;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.gwt.user.client.rpc.IsSerializable;

@JsonSubTypes(
        @JsonSubTypes.Type(value = StringNodePropertyValue.class, name = "String")
)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
public interface NodePropertyValue extends IsSerializable {

}
