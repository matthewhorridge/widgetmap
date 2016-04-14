package edu.stanford.protege.widgetmap.server.node;

import com.google.gson.*;
import edu.stanford.protege.widgetmap.shared.node.Node;
import edu.stanford.protege.widgetmap.shared.node.ParentNode;

import java.lang.reflect.Type;

import static edu.stanford.protege.widgetmap.server.node.SerializationVocabulary.*;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 22/02/16
 */
public class ParentNodeSerializer implements JsonSerializer<ParentNode> {

    @Override
    public JsonElement serialize(ParentNode parentNode, Type type, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        JsonArray children = new JsonArray();
        for(int i = 0; i < parentNode.getChildCount(); i++) {
            Node childNode = parentNode.getChildAt(i);
            double weight = parentNode.getWeightAt(i);
            JsonObject childObject = new JsonObject();
            childObject.addProperty(WEIGHT.getVocabulary(), weight);
            JsonObject childSerialization = (JsonObject) context.serialize(childNode);
            childObject.add(NODE.getVocabulary(), childSerialization);
            children.add(childObject);
        }
        object.addProperty(DIRECTION.getVocabulary(), parentNode.getDirection().name());
        object.add(CHILDREN.getVocabulary(), children);
        return object;
    }
}
