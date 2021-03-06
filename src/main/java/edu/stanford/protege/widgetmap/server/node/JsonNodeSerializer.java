package edu.stanford.protege.widgetmap.server.node;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.protege.widgetmap.shared.node.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.IOException;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 22/02/16
 */
public class JsonNodeSerializer {

    private final ObjectMapper objectMapper;

    @Inject
    public JsonNodeSerializer(@Nonnull ObjectMapper objectMapper) {

        this.objectMapper = objectMapper;
    }

    public String serialize(Node node) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    public Node deserialize(String s) {
        try {
            return objectMapper.readValue(s, Node.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
