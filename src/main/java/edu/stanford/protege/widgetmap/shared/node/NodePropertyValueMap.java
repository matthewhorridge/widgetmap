package edu.stanford.protege.widgetmap.shared.node;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.*;

public class NodePropertyValueMap implements Map<String, NodePropertyValue> {

    private Map<String, NodePropertyValue> delegate = new LinkedHashMap<>();

    public NodePropertyValueMap() {
    }

    public NodePropertyValueMap(Map<? extends String, ? extends NodePropertyValue> m) {
        delegate.putAll(m);
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @JsonIgnore
    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return delegate.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return delegate.containsValue(value);
    }

    @Override
    public NodePropertyValue get(Object key) {
        return delegate.get(key);
    }

    @Override
    public NodePropertyValue put(String key, NodePropertyValue value) {
        return delegate.put(key, value);
    }

    @Override
    public NodePropertyValue remove(Object key) {
        return delegate.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ? extends NodePropertyValue> m) {
        delegate.putAll(m);
    }

    @Override
    public void clear() {
        delegate.clear();
    }

    @Override
    public Set<String> keySet() {
        return delegate.keySet();
    }

    @Override
    public Collection<NodePropertyValue> values() {
        return delegate.values();
    }

    @Override
    public Set<Entry<String, NodePropertyValue>> entrySet() {
        return delegate.entrySet();
    }
}
