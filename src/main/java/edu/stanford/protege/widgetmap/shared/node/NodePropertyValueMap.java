package edu.stanford.protege.widgetmap.shared.node;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class NodePropertyValueMap extends LinkedHashMap<String, NodePropertyValue> {

    public NodePropertyValueMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public NodePropertyValueMap(int initialCapacity) {
        super(initialCapacity);
    }

    public NodePropertyValueMap() {
    }

    public NodePropertyValueMap(Map<? extends String, ? extends NodePropertyValue> m) {
        super(m);
    }
}
