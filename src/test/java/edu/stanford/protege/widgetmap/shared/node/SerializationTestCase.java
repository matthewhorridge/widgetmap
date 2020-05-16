package edu.stanford.protege.widgetmap.shared.node;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.protege.widgetmap.server.node.JsonNodeSerializer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 22/02/16
 */
@RunWith(MockitoJUnitRunner.class)
public class SerializationTestCase {

    private JsonNodeSerializer serializer;

    @Before
    public void setUp() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerSubtypes(StructuredValue_TestObject.class);
        serializer = new JsonNodeSerializer(objectMapper);
    }

    @Test
    public void shouldSerializeTerminalNode() {
        TerminalNodeId nodeId = TerminalNodeId.get("xyz");
        TerminalNode node = new TerminalNode(nodeId);
        assertRoundTrips(node);
    }

    private void assertRoundTrips(Node node) {
        String serialization = serializer.serialize(node);
        System.out.println(serialization);
        Node deserializedNode = serializer.deserialize(serialization);
        assertThat(deserializedNode, is(equalTo(node)));
    }

    @Test
    public void shouldSerializeTerminalNodeWithProperties() {
        TerminalNodeId nodeId = TerminalNodeId.get("xyz");
        NodeProperties properties = NodeProperties.builder()
                .setValue("propB", "X")
                .setValue("propA", "X")
                .build();
        TerminalNode node = new TerminalNode(nodeId, properties);
        assertRoundTrips(node);
    }

    @Test
    public void shouldSerializeTerminalNodeWithPropertiesWithObjectValues() {
        TerminalNodeId nodeId = TerminalNodeId.get("xyz");
        NodeProperties properties = NodeProperties.builder()
                .setValue("propB", "X")
                .setValue("propA", "X")
                .setValue("propC", new StructuredValue_TestObject(33, "Hello"))
                .build();
        TerminalNode node = new TerminalNode(nodeId, properties);
        assertRoundTrips(node);
    }

    @Test
    public void shouldSerializeParentNodeColumn() {
        TerminalNodeId nodeId = TerminalNodeId.get("xyz");
        TerminalNode childNode = new TerminalNode(nodeId);
        ParentNode parentNode = new ParentNode(Direction.COLUMN);
        parentNode.addChild(childNode, 0.77);
        assertRoundTrips(parentNode);
    }

    @Test
    public void shouldSerializeParentNodeRow() {
        TerminalNodeId nodeId = TerminalNodeId.get("xyz");
        TerminalNode childNode = new TerminalNode(nodeId);
        ParentNode parentNode = new ParentNode(Direction.ROW);
        parentNode.addChild(childNode, 0.77);
        assertRoundTrips(parentNode);
    }
}
