package edu.stanford.protege.widgetmap.server.node;

import com.google.gson.*;
import edu.stanford.protege.widgetmap.shared.node.*;

import java.lang.reflect.Type;
import java.util.Map;

import static edu.stanford.protege.widgetmap.server.node.SerializationVocabulary.*;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 22/02/16
 */
public class NodeDeserializer implements JsonDeserializer<Node> {


    @Override
    public Node deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        if(jsonObject.has(CHILDREN.getVocabulary())) {
            return deserializeParentNode(jsonObject, context);
        }
        else {
            return deserializeTerminalNode(jsonElement);
        }
    }

    /**
     * Derserialize a parent node.
     * @param jsonElement The Json object that represents the parent node.
     * @param context The deserialization context.
     * @return The deserialized parent node.
     */
    private Node deserializeParentNode(JsonElement jsonElement, JsonDeserializationContext context) {
        if(!jsonElement.isJsonObject()) {
            throw new WidgetMapParseException("Expected a Json object");
        }
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        if(!jsonObject.has(DIRECTION.getVocabulary())) {
            throw new WidgetMapParseException("Expected direction property but did not find it");
        }
        if(!jsonObject.has(CHILDREN.getVocabulary())) {
            throw new WidgetMapParseException("Expected children property but did not find it");
        }
        JsonElement directionElement = jsonObject.get(DIRECTION.getVocabulary());
        String directionValue = directionElement.getAsString();
        if(!(directionValue.equals(Direction.COLUMN.name()) || directionValue.equals(Direction.ROW.name()))) {
            throw new WidgetMapParseException("Expected COLUMN or ROW as value for direction property but found " + directionValue);
        }
        ParentNode parentNode = new ParentNode(Direction.valueOf(directionValue));
        JsonArray childrenArray = jsonObject.getAsJsonArray(CHILDREN.getVocabulary());
        for(JsonElement childElement : childrenArray) {
            deserializeChildNode(parentNode, childElement, context);
        }
        return parentNode;
    }

    /**
     * Deserialize a child of the specified parent node.
     * @param parentNode The parent node.  The child will be added to this parent.
     * @param childElement The Json element that represents the child.
     * @param context The serialization context.
     */
    private void deserializeChildNode(ParentNode parentNode, JsonElement childElement, JsonDeserializationContext context) {
        JsonObject childObject = childElement.getAsJsonObject();
        JsonElement weightElement = childObject.get(WEIGHT.getVocabulary());
        if(weightElement == null) {
            throw new WidgetMapParseException("Expected weight property but did not find it");
        }
        double weight = weightElement.getAsDouble();
        JsonElement childNodeElement = childObject.get(NODE.getVocabulary());
        if(childNodeElement == null) {
            throw new WidgetMapParseException("Expected node property but did not find it");
        }
        Node childNode = context.deserialize(childNodeElement, Node.class);
        parentNode.addChild(childNode, weight);
    }

    /**
     * Deserialize a terminal node.
     * @param jsonElement The Json element that represents the terminal element.
     * @return The deserialized node.
     */
    private Node deserializeTerminalNode(JsonElement jsonElement) {
        JsonObject nodeObject = jsonElement.getAsJsonObject();
        NodeProperties.Builder builder = NodeProperties.builder();
        TerminalNodeId nodeId = deserializeTerminalNodeId(nodeObject);
        for(Map.Entry<String, JsonElement> entry : nodeObject.entrySet()) {
            if (!entry.getKey().equals(ID.getVocabulary())) {
                builder.setValue(entry.getKey(), entry.getValue().getAsString());
            }
        }
        return TerminalNode.builder(nodeId).withProperties(builder.build()).build();
    }


    /**
     * Derserialize a terminal node Id.
     * @param nodeObject The Json object that represents the terminal node.
     * @return The TerminalNodeId
     */
    private TerminalNodeId deserializeTerminalNodeId(JsonObject nodeObject) {
        if(nodeObject.has(ID.getVocabulary())) {
            return new TerminalNodeId(nodeObject.get(ID.getVocabulary()).getAsString());
        }
        else {
            return new TerminalNodeId();
        }
    }
}
