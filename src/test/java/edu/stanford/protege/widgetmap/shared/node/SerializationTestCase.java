package edu.stanford.protege.widgetmap.shared.node;

import edu.stanford.protege.widgetmap.server.node.JsonNodeSerializer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 22/02/16
 */
@RunWith(MockitoJUnitRunner.class)
public class SerializationTestCase {

    private JsonNodeSerializer serializer;

    @Before
    public void setUp() throws Exception {
        serializer = new JsonNodeSerializer();
    }

    @Test
    public void shouldSerializeTerminalNode() {
        TerminalNodeId nodeId = new TerminalNodeId("xyz");
        TerminalNode node = new TerminalNode(nodeId);
        String serialization = serializer.serialize(node);
        assertThat(collapse(serialization), is("{\"id\":\"xyz\"}"));
    }

    @Test
    public void shouldSerializeTerminalNodeWithProperties() {
        TerminalNodeId nodeId = new TerminalNodeId("xyz");
        NodeProperties properties = NodeProperties.builder()
                .setValue("propB", "X")
                .setValue("propA", "X")
                .build();
        TerminalNode node = new TerminalNode(nodeId, properties);
        String serialization = serializer.serialize(node);
        assertThat(collapse(serialization), is("{\"id\":\"xyz\",\"propB\":\"X\",\"propA\":\"X\"}"));
    }

    @Test
    public void shouldSerializeParentNodeColumn() {
        TerminalNodeId nodeId = new TerminalNodeId("xyz");
        TerminalNode childNode = new TerminalNode(nodeId);
        ParentNode parentNode = new ParentNode(Direction.COLUMN);
        parentNode.addChild(childNode, 0.77);
        String serialization = serializer.serialize(parentNode);
        assertThat(collapse(serialization), is("{\"direction\":\"COLUMN\",\"children\":[{\"weight\":0.77,\"node\":{\"id\":\"xyz\"}}]}"));
    }

    @Test
    public void shouldSerializeParentNodeRow() {
        TerminalNodeId nodeId = new TerminalNodeId("xyz");
        TerminalNode childNode = new TerminalNode(nodeId);
        ParentNode parentNode = new ParentNode(Direction.ROW);
        parentNode.addChild(childNode, 0.77);
        String serialization = serializer.serialize(parentNode);
        assertThat(collapse(serialization), is("{\"direction\":\"ROW\",\"children\":[{\"weight\":0.77,\"node\":{\"id\":\"xyz\"}}]}"));
    }

    private static String collapse(String s) {
        return s.replaceAll("\\s", "");
    }

}
