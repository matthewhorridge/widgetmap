package edu.stanford.protege.widgetmap.server.node;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.stanford.protege.widgetmap.shared.node.*;

import java.io.IOException;
import java.util.Iterator;

import static edu.stanford.protege.widgetmap.server.node.SerializationVocabulary.*;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 22/02/16
 */
public class NodeDeserializer extends StdDeserializer<Node> {


    public NodeDeserializer() {
        super(Node.class);
    }

    @Override
    public Node deserialize(JsonParser parser, DeserializationContext ctxt) {
        try {
            ObjectNode objectNode = parser.readValueAsTree();
            if(objectNode.has(CHILDREN.getVocabulary())) {
                return deserializeParentNode(objectNode, ctxt);
            }
            else {
                return deserializeTerminalNode(objectNode, ctxt);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Derserialize a parent node.
     * @param objectNode The Json object that represents the parent node.
     * @param context The deserialization context.
     * @return The deserialized parent node.
     */
    private Node deserializeParentNode(ObjectNode objectNode,
                                       DeserializationContext context) throws JsonProcessingException {
        if(!objectNode.has(DIRECTION.getVocabulary())) {
            throw new WidgetMapParseException("Expected direction property but did not find it");
        }
        if(!objectNode.has(CHILDREN.getVocabulary())) {
            throw new WidgetMapParseException("Expected children property but did not find it");
        }
        JsonNode directionElement = objectNode.get(DIRECTION.getVocabulary());
        String directionValue = directionElement.asText();
        if(!(directionValue.equals(Direction.COLUMN.name()) || directionValue.equals(Direction.ROW.name()))) {
            throw new WidgetMapParseException("Expected COLUMN or ROW as value for direction property but found " + directionValue);
        }
        ParentNode parentNode = new ParentNode(Direction.valueOf(directionValue));
        ArrayNode childrenArray = objectNode.withArray(CHILDREN.getVocabulary());
        for(JsonNode childElement : childrenArray) {
            deserializeChildNode(parentNode, (ObjectNode) childElement, context);
        }
        return parentNode;
    }

    /**
     * Deserialize a child of the specified parent node.
     * @param parentNode The parent node.  The child will be added to this parent.
     * @param childObjectNode The Json element that represents the child.
     * @param context The serialization context.
     */
    private void deserializeChildNode(ParentNode parentNode, ObjectNode childObjectNode, DeserializationContext context) throws JsonProcessingException {
        JsonNode weightElement = childObjectNode.get(WEIGHT.getVocabulary());
        if(weightElement == null) {
            throw new WidgetMapParseException("Expected weight property but did not find it");
        }
        double weight = weightElement.asDouble();
        JsonNode childNodeElement = childObjectNode.get(NODE.getVocabulary());
        if(childNodeElement == null) {
            throw new WidgetMapParseException("Expected node property but did not find it");
        }

        Node childNode = context.getParser().getCodec().treeToValue(childNodeElement, Node.class);
        parentNode.addChild(childNode, weight);
    }

    /**
     * Deserialize a terminal node.
     * @param objectNode The Json element that represents the terminal element.
     * @return The deserialized node.
     */
    private Node deserializeTerminalNode(ObjectNode objectNode, DeserializationContext context) throws JsonProcessingException {
        NodeProperties.Builder builder = NodeProperties.builder();
        TerminalNodeId nodeId = deserializeTerminalNodeId(objectNode);
        for(Iterator<String> it = objectNode.fieldNames(); it.hasNext(); ) {
            String fieldName = it.next();
            if(!fieldName.equals(ID.getVocabulary())) {
                JsonNode valueNode = objectNode.get(fieldName);
                String value = context.getParser().getCodec().treeToValue(valueNode, String.class);
                builder.setValue(fieldName, value);
            }
        }
        return TerminalNode.builder(nodeId).withProperties(builder.build()).build();
    }


    /**
     * Derserialize a terminal node Id.
     * @param nodeObject The Json object that represents the terminal node.
     * @return The TerminalNodeId
     */
    private TerminalNodeId deserializeTerminalNodeId(ObjectNode nodeObject) {
        if(nodeObject.has(ID.getVocabulary())) {
            return new TerminalNodeId(nodeObject.get(ID.getVocabulary()).asText());
        }
        else {
            return new TerminalNodeId();
        }
    }
}
