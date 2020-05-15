package edu.stanford.protege.widgetmap.shared.node;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;
import com.google.gwt.user.client.rpc.IsSerializable;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/12/2013
 */
public class NodeProperties implements IsSerializable {

    private static final NodeProperties EMPTY_NODE_PROPERTIES = new NodeProperties();

    // Cannot be final because of GWT serialization
    private Map<String, String> properties = new LinkedHashMap<String, String>();

    private NodeProperties() {

    }

    @JsonCreator
    private NodeProperties(Map<String, String> properties) {
        this.properties.putAll(properties);
    }


    public static Builder builder() {
        return new Builder();
    }

    public static NodeProperties emptyNodeProperties() {
        return EMPTY_NODE_PROPERTIES;
    }


    public String getPropertyValue(String propertyName, String defaultValue) {
        String value = properties.get(propertyName);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    @JsonValue
    public Map<String, String> getPropertiesAsMap() {
        return new HashMap<>(properties);
    }

    @JsonIgnore
    @Nonnull
    public List<String> getProperties() {
        return new ArrayList<String>(properties.keySet());
    }

    @Nonnull
    public Builder toBuilder() {
        return new Builder(properties);
    }

    public static class Builder {

        @Nonnull
        private final Map<String, String> propertiesMap;

        public Builder() {
            this(new LinkedHashMap<>());
        }

        public Builder(@Nonnull Map<String, String> propertiesMap) {
            this.propertiesMap = propertiesMap;
        }

        public Builder setValue(String property, String value) {
            propertiesMap.put(property, value);
            return this;
        }

        public NodeProperties build() {
            if(propertiesMap.isEmpty()) {
                return EMPTY_NODE_PROPERTIES;
            }
            else {
                return new NodeProperties(new LinkedHashMap<String, String>(propertiesMap));
            }
        }
    }

    @Override
    public String toString() {
        MoreObjects.ToStringHelper helper = MoreObjects.toStringHelper("NodeProperties");
            for(String property : properties.keySet()) {
                helper.add(property, properties.get(property));
            }
        return helper.toString();
    }
}
