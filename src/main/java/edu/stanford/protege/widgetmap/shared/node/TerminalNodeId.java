package edu.stanford.protege.widgetmap.shared.node;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 07/01/2014
 */
public class TerminalNodeId implements Serializable, IsSerializable {

    public static final String NODE_ID_PREFIX = "N-";

    private final String id;

    private static int counter = 0;

    public TerminalNodeId() {
        id = NODE_ID_PREFIX + nextId();
    }

    @JsonCreator
    public TerminalNodeId(String id) {
        this.id = checkNotNull(id);
    }

    @JsonValue
    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if(!(o instanceof TerminalNodeId)) {
            return false;
        }
        TerminalNodeId other = (TerminalNodeId) o;
        return this.id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return "TerminalNodeId".hashCode() + id.hashCode();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("Id").addValue(id).toString();
    }

    /**
     * Gets the next internal id.
     * @return The next internal id.
     */
    private int nextId() {
        return counter++;
    }
}
