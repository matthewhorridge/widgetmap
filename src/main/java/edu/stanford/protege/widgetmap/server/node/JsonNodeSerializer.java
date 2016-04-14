package edu.stanford.protege.widgetmap.server.node;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.stanford.protege.widgetmap.shared.node.*;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 22/02/16
 */
public class JsonNodeSerializer {

    public JsonNodeSerializer() {

    }

    private Gson createGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        builder.registerTypeAdapter(ParentNode.class, new ParentNodeSerializer());
        builder.registerTypeAdapter(TerminalNode.class, new TerminalNodeSerializer());
        builder.registerTypeAdapter(Node.class, new NodeDeserializer());
        builder.disableHtmlEscaping();
        return builder.create();
    }

    public String serialize(Node node) {
        return createGson().toJson(node);
    }
    public Node deserialize(String s) {
        return createGson().fromJson(s, Node.class);
    }
}
