package edu.stanford.protege.widgetmap.shared.node;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.gwt.user.client.rpc.IsSerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 07/01/2014
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class TerminalNodeId implements Serializable, IsSerializable {

    public static final String NODE_ID_PREFIX = "N-";

    private static int counter = 0;

    public static TerminalNodeId get() {
        String id = NODE_ID_PREFIX + nextId();
        return new AutoValue_TerminalNodeId(id);
    }

    @JsonCreator
    @Nonnull
    public static TerminalNodeId get(String id) {
        return new AutoValue_TerminalNodeId(id);
    }

    @JsonValue
    public abstract String getId();

    /**
     * Gets the next internal id.
     * @return The next internal id.
     */
    private static int nextId() {
        return counter++;
    }
}
