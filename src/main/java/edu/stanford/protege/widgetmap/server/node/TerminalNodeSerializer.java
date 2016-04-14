package edu.stanford.protege.widgetmap.server.node;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import edu.stanford.protege.widgetmap.shared.node.NodeProperties;
import edu.stanford.protege.widgetmap.shared.node.TerminalNode;

import java.lang.reflect.Type;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 22/02/16
 */
public class TerminalNodeSerializer implements JsonSerializer<TerminalNode> {

    @Override
    public JsonElement serialize(TerminalNode terminalNode, Type type, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", terminalNode.getNodeId().getId());
        NodeProperties nodeProperties = terminalNode.getNodeProperties();
        for(String property : nodeProperties.getProperties()) {
            String value = nodeProperties.getPropertyValue(property, "");
            jsonObject.addProperty(property, value);
        }
        return jsonObject;
    }
}
