package edu.stanford.protege.widgetmap.shared.node;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/12/2013
 */
public class NodePropertiesTestCase {

    @Test
    public void getPropertyValueShouldReturnDefaultValueForPropertyThatIsNotPresent() {
        NodeProperties nodeProperties = NodeProperties.emptyNodeProperties();
        Object val = nodeProperties.getPropertyValue("prop", "x");
        assertEquals("x", val);

    }

    @Test
    public void shouldBuildEmptyNodePropertiesWithNoPropertiesAdded() {
        NodeProperties.Builder builder = NodeProperties.builder();
        NodeProperties properties = builder.build();
        assertSame(NodeProperties.emptyNodeProperties(), properties);
    }

    @Test
    public void shouldAddPropertyViaBuilder() {
        NodeProperties.Builder builder = NodeProperties.builder();
        builder.setValue("prop", "x");
        NodeProperties properties = builder.build();
        assertEquals("x", properties.getPropertyValue("prop", (String) null));
    }
}
