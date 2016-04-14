package edu.stanford.protege.widgetmap.shared.node;

import edu.stanford.protege.widgetmap.server.node.JsonNodeSerializer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 22/02/16
 */
@RunWith(MockitoJUnitRunner.class)
public class DeserializationTestCase {

    private JsonNodeSerializer serializer;

    @Before
    public void setUp() throws Exception {
        serializer = new JsonNodeSerializer();
    }

    @Test
    public void shouldDeserializeTerminalNode() {
        Node node = serializer.deserialize("{\n" +
                "  \"id\": \"xyz\"\n" +
                "}");
        assertThat(node, is((Node)new TerminalNode(new TerminalNodeId("xyz"))));
    }

    @Test
    public void shouldDeserializeTerminalNodeWithProperties() {
        Node node = serializer.deserialize("{\n" +
                "  \"id\": \"xyz\",\n" +
                "  \"propB\": \"X\",\n" +
                "  \"propA\": \"X\"\n" +
                "}");

        TerminalNodeId nodeId = new TerminalNodeId("xyz");
        NodeProperties properties = NodeProperties.builder()
                .setValue("propB", "X")
                .setValue("propA", "X")
                .build();
        Node expectedNode = new TerminalNode(nodeId, properties);
        assertThat(node, is(expectedNode));
    }


    @Test
    public void shouldDeserializeParentNodeColumn() {
        Node node = serializer.deserialize("{\"direction\":\"COLUMN\",\"children\":[{\"weight\":0.77,\"node\":{\"id\":\"xyz\"}}]}");

        TerminalNodeId nodeId = new TerminalNodeId("xyz");
        TerminalNode childNode = new TerminalNode(nodeId);
        ParentNode expectedNode = new ParentNode(Direction.COLUMN);
        expectedNode.addChild(childNode, 0.77);

        assertThat(node, is((Node)expectedNode));
    }

    @Test
    public void shouldSerializeParentNodeRow() {
        Node node = serializer.deserialize("{\"direction\":\"ROW\",\"children\":[{\"weight\":0.77,\"node\":{\"id\":\"xyz\"}}]}");

        TerminalNodeId nodeId = new TerminalNodeId("xyz");
        TerminalNode childNode = new TerminalNode(nodeId);
        ParentNode expectedNode = new ParentNode(Direction.ROW);
        expectedNode.addChild(childNode, 0.77);

        assertThat(node, is((Node)expectedNode));
    }
}
